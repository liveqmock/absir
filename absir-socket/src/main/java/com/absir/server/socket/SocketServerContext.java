/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-22 下午2:02:53
 */
package com.absir.server.socket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.utils.QueryDaoUtils;
import com.absir.appserv.system.domain.DActiver;
import com.absir.appserv.system.domain.DActiverMap;
import com.absir.appserv.system.service.ActiveService;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.Started;
import com.absir.bean.inject.value.Stopping;
import com.absir.bean.inject.value.Value;
import com.absir.core.kernel.KernelClass;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.hibernate.boost.IEntityMerge;
import com.absir.server.socket.resolver.SocketSessionResolver;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "unchecked" })
@Base
@Bean
public class SocketServerContext extends ActiveService<JbServer, SocketServer> implements IEntityMerge<JbServer> {

	/** ME */
	@Inject
	public static SocketServerContext ME = BeanFactoryUtils.get(SocketServerContext.class);

	/** LOGGER */
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketServerContext.class);

	/** option */
	@Value("server.socket.option")
	private boolean option = false;

	/** port */
	@Value("server.socket.port")
	private int port = 18890;

	/** ip */
	@Value("server.socket.ip")
	private String ip;

	/** ip */
	@Value("server.socket.ipv6")
	private String ipv6;

	/** backlog */
	@Value("server.socket.backlog")
	private int backlog = 50;

	/** bufferSize */
	@Value("server.socket.bufferSize")
	private int bufferSize = 1024;

	/** bufferSize */
	@Value("server.socket.receiveBufferSize")
	private int receiveBufferSize = 10240;

	/** bufferSize */
	@Value("server.socket.sendBufferSize")
	private int sendBufferSize = 10240;

	/** beat */
	@Value("server.socket.beat")
	protected byte[] beat = "b".getBytes();

	/** ok */
	@Value("server.socket.ok")
	protected byte[] ok = "ok".getBytes();

	/** failed */
	@Value("server.socket.fail")
	protected byte[] failed = "failed".getBytes();

	/** logined */
	@Value("server.socket.logined")
	protected byte[] logined = "logined".getBytes();

	/** timeout */
	@Value("server.socket.timeout")
	protected long timeout = 30000;

	/** serverClass */
	private Class<? extends JbServer> serverClass = (Class<? extends JbServer>) SessionFactoryUtils.getEntityClass("JServer");

	/** sessionResolver */
	private SocketSessionResolver sessionResolver = BeanFactoryUtils.get(SocketSessionResolver.class);

	/** socketServerContextMap */
	private DActiverMap<JbServer, ServerContext> serverContextMap = new DActiverMap<JbServer, ServerContext>() {

		@Override
		protected boolean isClosed(ServerContext activeContext) {
			// TODO Auto-generated method stub
			return activeContext.isClosed();
		}

		@Override
		protected Map<Serializable, ServerContext> createActiveContexts() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected ServerContext createActiveContext(JbServer active) {
			// TODO Auto-generated method stub
			return createServerContext(active);
		}

		@Override
		protected ServerContext updateActiveContext(JbServer active, ServerContext activeContext) {
			// TODO Auto-generated method stub
			activeContext.setServer(active);
			return activeContext;
		}

		@Override
		protected void closeActiveContext(Serializable id, ServerContext activeContext) {
			// TODO Auto-generated method stub
		}
	};

	/**
	 * @return
	 */
	public static SocketServerContext get() {
		return ME;
	}

	/**
	 * @return the beat
	 */
	public byte[] getBeat() {
		return beat;
	}

	/**
	 * @return the ok
	 */
	public byte[] getOk() {
		return ok;
	}

	/**
	 * @return the failed
	 */
	public byte[] getFailed() {
		return failed;
	}

	/**
	 * @return the logined
	 */
	public byte[] getLogined() {
		return logined;
	}

	/**
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @return the sessionResolver
	 */
	public SocketSessionResolver getSessionResolver() {
		return sessionResolver;
	}

	/**
	 * 开启服务
	 */
	@Started
	protected synchronized void started() {
		if (activer == null && serverClass != null) {
			activer = new DActiver<JbServer>("JServer");
		}

		if (!option) {
			super.started();
			if (activer == null) {
				startDefault();
			}
		}
	}

	/**
	 * 开启默认
	 */
	protected void startDefault() {
		List<JbServer> servers = new ArrayList<JbServer>();
		JbServer server = serverClass == null ? new JbServer() : KernelClass.newInstance(serverClass);
		if (server != null) {
			server.setId(0L);
			server.setPort(port);
			servers.add(server);
		}

		serverContextMap.setActives(servers);
		activerMap.setActives(servers);
	}

	/**
	 * 关闭服务
	 */
	@Stopping
	protected synchronized void stopping() {
		for (SocketServer server : activerMap.getOnlineActiveContexts().values()) {
			server.close();
		}

		activer = null;
		activerMap = null;
	}

	/**
	 * 获取服务
	 * 
	 * @param id
	 * @return
	 */
	public ServerContext getServerContext(Serializable id) {
		return serverContextMap.getOnlineActiveContexts().get(id);
	}

	/**
	 * 获取当前服务
	 * 
	 * @return the serverContexts
	 */
	public Collection<ServerContext> getServerContexts() {
		return serverContextMap.getOnlineActiveContexts().values();
	}

	/**
	 * 
	 * 创建服务对象
	 * 
	 * @param server
	 * @param from
	 */
	protected ServerContext createServerContext(JbServer server) {
		ServerContext serverContext = new ServerContext();
		serverContext.setServer(server);
		return serverContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.context.core.ContextService#step(long)
	 */
	@Override
	public void step(long contextTime) {
		// TODO Auto-generated method stub
		super.step(contextTime);
		for (ServerContext serverContext : getServerContexts()) {
			serverContext.stepDone(contextTime);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.service.ActiveService#getInstance()
	 */
	@Override
	protected ActiveService<JbServer, SocketServer> getInstance() {
		// TODO Auto-generated method stub
		return ME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.service.ActiveService#createActiveContexts()
	 */
	@Override
	protected Map<Serializable, SocketServer> createActiveContexts() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.service.ActiveService#isClosed(com.absir.appserv
	 * .system.bean.value.JiActive)
	 */
	@Override
	protected boolean isClosed(SocketServer activeContext) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * 开启服务
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.service.ActiveService#createActiveContext(com
	 * .absir.appserv.system.bean.value.JiActive)
	 */
	@Override
	protected SocketServer createActiveContext(JbServer active) {
		// TODO Auto-generated method stub
		ServerContext serverContext = getServerContext(active.getId());
		if (serverContext == null) {
			return null;
		}

		serverContext.setClosed(false);
		SocketServer socketServer = new SocketServer();
		if (active.getPort() > 0) {
			try {
				socketServer.start(serverContext.getServer().getPort(), backlog, serverContext.getServer().getInetAddress(), bufferSize, receiveBufferSize, sendBufferSize,
						createSocketReceiverContext(serverContext));

			} catch (Throwable e) {
				// TODO Auto-generated catch block
				LOGGER.error("server [" + serverContext.getServer().getIp() + "] start error", e);
				serverContext.setClosed(true);
			}
		}

		return socketServer;
	}

	/**
	 * 创建服务监听
	 * 
	 * @param serverContext
	 * @return
	 */
	protected SocketReceiverContext createSocketReceiverContext(ServerContext serverContext) {
		return serverContext.getServer().isMultiPort() ? new SocketReceiverMutilContext(serverContext) : new SocketReceiverContext(serverContext);
	}

	/*
	 * 关闭服务
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.service.ActiveService#closeActiveContext(java
	 * .lang.Object)
	 */
	@Override
	protected void closeActiveContext(Serializable id, SocketServer activeContext) {
		// TODO Auto-generated method stub
		ServerContext serverContext = getServerContext(id);
		if (serverContext != null) {
			serverContext.setClosed(true);
		}

		activeContext.close();
	}

	/*
	 * 重新载入服务
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.service.ActiveService#reloadActives(long)
	 */
	@Override
	protected void reloadActives(long contextTime) {
		// TODO Auto-generated method stub
		List<JbServer> servers = QueryDaoUtils.createQueryArray(BeanDao.getSession(), "SELECT o FROM JServer o").list();
		if (!option && servers.isEmpty()) {
			option = true;
			startDefault();

		} else {
			serverContextMap.setActives(servers);
			super.reloadActives(contextTime);
		}
	}
}
