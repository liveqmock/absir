/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-22 下午2:02:53
 */
package com.absir.server.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.absir.appserv.system.service.BeanService;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.Started;
import com.absir.bean.inject.value.Stopping;
import com.absir.bean.inject.value.Value;
import com.absir.context.core.ContextBase;
import com.absir.context.core.ContextUtils;
import com.absir.core.kernel.KernelClass;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.server.socket.resolver.SocketSessionResolver;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "unchecked" })
@Base
@Bean
public class SocketServerContext extends ContextBase {

	/** ME */
	@Inject
	private static SocketServerContext ME;

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
	private int bufferSize = 256;

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

	/** nextStartTime */
	private long nextServerTime = 0;

	/** socketServerMap */
	private Map<Long, SocketServer> socketServerMap = new HashMap<Long, SocketServer>();

	/** serverContextMap */
	private Map<Long, ServerContext> serverContextMap = new TreeMap<Long, ServerContext>();

	/** serverContexts */
	private List<ServerContext> serverContexts = null;

	/** sessionResolver */
	private SocketSessionResolver sessionResolver = BeanFactoryUtils.get(SocketSessionResolver.class);

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
	 * 服务分区实体监听
	 * 
	 * @author absir
	 * 
	 */
	public static class Listener {

		/**
		 * 添加, 修改服务分区
		 * 
		 */
		@PostPersist
		@PostUpdate
		public void merge(JbServer server) {
			SocketServerContext.ME.merge(server, false);
		}

		/**
		 * 删除服务分区
		 * 
		 * @param lotCard
		 */
		@PostRemove
		public void remove(JbServer server) {
			SocketServerContext.ME.merge(server, true);
		}
	}

	/**
	 * 开启服务
	 */
	@Started
	protected synchronized void started() {
		List<JbServer> servers = null;
		if (serverClass != null) {
			servers = BeanService.ME.findAll("JServer");
		}

		if (servers == null || servers.isEmpty()) {
			if (!option) {
				option = true;
				if (servers == null) {
					servers = new ArrayList<JbServer>();
				}

				JbServer server = serverClass == null ? new JbServer() : KernelClass.newInstance(serverClass);
				if (server != null) {
					server.setId(-1L);
					server.setPort(port);
					server.setName("default");
					servers.add(server);
				}
			}
		}

		if (servers != null) {
			for (JbServer server : servers) {
				merge(server, false);
			}
		}
	}

	/**
	 * 关闭服务
	 */
	@Stopping
	protected synchronized void stopping() {
		for (SocketServer server : socketServerMap.values()) {
			server.close();
		}

		socketServerMap = null;
	}

	/**
	 * 获取服务
	 * 
	 * @param id
	 * @return
	 */
	public ServerContext getServerContext(Long id) {
		return serverContextMap.get(id);
	}

	/**
	 * 获取当前服务
	 * 
	 * @return the serverContexts
	 */
	public List<ServerContext> getServerContexts() {
		if (serverContexts == null) {
			synchronized (this) {
				serverContexts = new ArrayList<ServerContext>(serverContextMap.values());
			}
		}

		return serverContexts;
	}

	/*
	 * 服务步进
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.absir.context.bean.IContext#stepDone(long)
	 */
	@Override
	public boolean stepDone(long contextTime) {
		// TODO Auto-generated method stub
		for (ServerContext serverContext : getServerContexts()) {
			serverContext.stepDone(contextTime);
		}

		return false;
	}

	/**
	 * 服务更改
	 * 
	 * @param server
	 * @param remove
	 */
	protected synchronized void merge(JbServer server, boolean remove) {
		if (socketServerMap == null) {
			return;
		}

		Long id = server.getId();
		if (remove) {
			SocketServer socketServer = socketServerMap.remove(id);
			if (socketServer != null) {
				socketServer.close();
			}

			if (serverContextMap.remove(id) != null) {
				serverContexts = null;
			}

		} else {
			ServerContext serverContext = serverContextMap.get(id);
			if (serverContext == null) {
				serverContext = createServerContext(server);
				serverContextMap.put(id, serverContext);
				serverContexts = null;
			}

			serverContext.setServer(server);
			long contextTime = ContextUtils.getContextTime();
			if (server.getStartTime() <= contextTime) {
				startSocketServer(serverContext);

			} else {
				SocketServer socketServer = socketServerMap.remove(id);
				if (socketServer != null) {
					socketServer.close();
					serverContext = createServerContext(server);
					serverContextMap.put(id, serverContext);
					serverContexts = null;
				}

				if (nextServerTime <= 0 || nextServerTime > server.getStartTime()) {
					nextServerTime = server.getStartTime();
				}
			}
		}
	}

	/**
	 * 开启一个服务
	 * 
	 * @param serverContext
	 */
	protected void startSocketServer(ServerContext serverContext) {
		Long id = serverContext.getServer().getId();
		SocketServer socketServer = socketServerMap.get(id);
		if (socketServer == null) {
			socketServer = new SocketServer();
			try {
				socketServer.start(serverContext.getServer().getPort(), backlog, serverContext.getServer().getInetAddress(), bufferSize, createSocketReceiverContext(serverContext));
				socketServerMap.put(id, socketServer);

			} catch (Throwable e) {
				// TODO Auto-generated catch block
				LOGGER.error("server [" + serverContext.getServer().getIp() + "] start error", e);
				serverContext.setClosed(true);
			}
		}
	}

	/**
	 * 
	 * @param server
	 * @param from
	 */
	protected ServerContext createServerContext(JbServer server) {
		return new ServerContext();
	}

	/**
	 * @param serverContext
	 * @return
	 */
	protected SocketReceiverContext createSocketReceiverContext(ServerContext serverContext) {
		return new SocketReceiverContext(serverContext);
	}
}
