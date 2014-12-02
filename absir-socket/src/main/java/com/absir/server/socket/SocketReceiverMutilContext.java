/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年11月28日 上午10:36:26
 */
package com.absir.server.socket;

import java.io.Serializable;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author absir
 *
 */
public class SocketReceiverMutilContext extends SocketReceiverContext {

	/** idMapMutilContext */
	protected Map<Serializable, ServerContext> idMapMutilContext = new HashMap<Serializable, ServerContext>();

	/**
	 * @param serverContext
	 */
	public SocketReceiverMutilContext(ServerContext serverContext) {
		super(serverContext);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.socket.SocketReceiverContext#registerSocketChannelContext
	 * (com.absir.server.socket.ServerContext, java.io.Serializable,
	 * com.absir.server.socket.SocketChannelContext)
	 */
	@Override
	protected void registerSocketChannelContext(ServerContext mutilContext, Serializable id, SocketChannelContext socketChannelContext) {
		// TODO Auto-generated method stub
		if (mutilContext == null || mutilContext == getServerContext()) {
			super.registerSocketChannelContext(mutilContext, id, socketChannelContext);

		} else {
			idMapMutilContext.put(id, mutilContext);
			mutilContext.loginSocketChannelContext(id, socketChannelContext);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.socket.SocketReceiverContext#unregisterSocketChannel
	 * (java.io.Serializable, java.nio.channels.SocketChannel)
	 */
	@Override
	protected void unregisterSocketChannel(Serializable id, SocketChannel socketChannel) {
		// TODO Auto-generated method stub
		ServerContext mutilContext = idMapMutilContext.remove(id);
		if (mutilContext == null) {
			super.unregisterSocketChannel(id, socketChannel);

		} else {
			mutilContext.logoutSocketChannelContext(id, socketChannel);
		}
	}
}
