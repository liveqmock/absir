/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-12 下午6:03:36
 */
package com.absir.appserv.game.service;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.absir.appserv.game.context.JbPlayerContext;
import com.absir.appserv.game.context.PlayerServiceBase;
import com.absir.appserv.game.context.value.IFight;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.helper.HelperServer;
import com.absir.appserv.system.server.ServerResolverBody;
import com.absir.appserv.system.service.impl.IdentityServiceLocal;
import com.absir.bean.basis.Base;
import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.context.core.ContextUtils;
import com.absir.server.socket.InputSocket;
import com.absir.server.socket.ServerContext;
import com.absir.server.socket.SocketServer;
import com.absir.server.socket.resolver.SocketSessionResolver;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Base
@Bean
public class SocketService implements SocketSessionResolver {

	/** LOGGER */
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.socket.resolver.SocketAuthorResolver#accept(java.nio
	 * .channels.SocketChannel, com.absir.server.socket.ServerContext)
	 */
	@Override
	public boolean accept(SocketChannel socketChannel, ServerContext serverContext) throws Throwable {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.socket.resolver.SocketAuthorResolver#register(java.nio
	 * .channels.SocketChannel, com.absir.server.socket.JbServerContext, byte[])
	 */
	@Override
	public Serializable register(SocketChannel socketChannel, ServerContext serverContext, byte[] buffer) throws Throwable {
		// TODO Auto-generated method stub
		JiUserBase userBase = IdentityServiceLocal.getUserBase(new String(buffer));
		Long id = PlayerServiceBase.ME.getPlayerId(serverContext.getServer().getId(), userBase);
		if (id != null) {
			JbPlayerContext playerContext = ContextUtils.getContext(JbPlayerContext.COMPONENT.PLAYER_CONTEXT_CLASS, id);
			if (playerContext.getPlayer().getCard() == 0) {
				return null;
			}

			synchronized (playerContext) {
				if (playerContext.getSocketChannel() != null) {
					SocketServer.close(playerContext.getSocketChannel());
				}

				playerContext.setSocketChannel(socketChannel);
			}
		}

		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.socket.resolver.SocketAuthorResolver#doBeat(java.io.
	 * Serializable, java.nio.channels.SocketChannel,
	 * com.absir.server.socket.ServerContext)
	 */
	@Override
	public boolean doBeat(Serializable id, SocketChannel socketChannel, ServerContext serverContext) {
		// TODO Auto-generated method stub
		ContextUtils.getContext(JbPlayerContext.COMPONENT.PLAYER_CONTEXT_CLASS, (Long) id);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.socket.resolver.SocketAuthorResolver#unRegister(java
	 * .io.Serializable, java.nio.channels.SocketChannel,
	 * com.absir.server.socket.JbServerContext)
	 */
	@Override
	public void unRegister(Serializable id, SocketChannel socketChannel, ServerContext serverContext) throws Throwable {
		// TODO Auto-generated method stub
		if (id instanceof Long) {
			JbPlayerContext playerContext = ContextUtils.getContext(JbPlayerContext.COMPONENT.PLAYER_CONTEXT_CLASS, (Long) id);
			IFight fight = playerContext.getFight();
			if (fight != null) {
				fight.disconnect();
			}

			synchronized (playerContext) {
				if (playerContext.getSocketChannel() == socketChannel) {
					playerContext.setSocketChannel(null);
				}
			}
		}
	}

	/** CALLBACK_MODIFY */
	public static final int CALLBACK_MODIFY = 7;

	/** CALLBACK_REWARD */
	public static final int CALLBACK_REWARD = 8;

	/** CALLBACK_FIGHT */
	public static final int CALLBACK_FIGHT = 9;

	/** CALLBACK_ACTIVITY */
	public static final int CALLBACK_ACTIVITY = 10;

	/** CALLBACK_AGAINST */
	public static final int CALLBACK_AGAINST = 11;

	/** CALLBACK_MESSAGE */
	public static final int CALLBACK_MESSAGE = 12;

	/** CALLBACK_CHAT */
	public static final int CALLBACK_CHAT = 13;

	/**
	 * @param playerId
	 * @param callbackIndex
	 * @param obj
	 * @return
	 */
	public static boolean writeByteObject(Long playerId, int callbackIndex, Object obj) {
		return writeByteObject(JbPlayerContext.COMPONENT.find(playerId), callbackIndex, obj);
	}

	/**
	 * @param playerContext
	 * @param callbackIndex
	 * @param obj
	 * @return
	 */
	public static boolean writeByteObject(JbPlayerContext playerContext, int callbackIndex, Object obj) {
		if (playerContext != null) {
			return writeByteObject(playerContext.getSocketChannel(), callbackIndex, obj);
		}

		return false;
	}

	/**
	 * @param socketChannel
	 * @param callbackIndex
	 * @param obj
	 */
	public static boolean writeByteObject(SocketChannel socketChannel, int callbackIndex, Object obj) {
		return writeByteObject(socketChannel, callbackIndex, obj, false);
	}

	/**
	 * @param socketChannel
	 * @param callbackIndex
	 * @param obj
	 * @param block
	 */
	public static boolean writeByteObject(SocketChannel socketChannel, int callbackIndex, Object obj, boolean block) {
		if (socketChannel == null) {
			return false;
		}

		if (obj != null) {
			try {
				byte[] bytes = obj.getClass() == String.class ? ((String) obj).getBytes(ContextUtils.getCharset()) : ServerResolverBody.ME.getObjectMapper().writeValueAsBytes(obj);
				if (block) {
					return writeByteBufferBlock(socketChannel, callbackIndex, bytes);

				} else {
					writeByteBuffer(socketChannel, callbackIndex, bytes);
				}

			} catch (Throwable e) {
				// TODO Auto-generated catch block
				if (BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= 0) {
					e.printStackTrace();
				}

				LOGGER.error("", e);
				return false;
			}
		}

		return true;
	}

	/**
	 * @param socketChannel
	 * @param callbackIndex
	 * @param bytes
	 */
	public static void writeByteBuffer(final SocketChannel socketChannel, final int callbackIndex, final byte[] bytes) {
		if (bytes != null) {
			ContextUtils.getThreadPoolExecutor().execute(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					writeByteBufferBlock(socketChannel, callbackIndex, bytes);
				}
			});
		}
	}

	/**
	 * @param socketChannel
	 * @param callbackIndex
	 * @param bytes
	 * @return
	 */
	public static boolean writeByteBufferBlock(SocketChannel socketChannel, int callbackIndex, byte[] bytes) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			HelperServer.zipCompress(bytes, 0, bytes.length, outStream);
			InputSocket.writeByteBuffer(socketChannel, callbackIndex, outStream.toByteArray());
			return true;

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			if (BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= 0) {
				e.printStackTrace();
			}

			LOGGER.error("", e);
		}

		return false;
	}
}
