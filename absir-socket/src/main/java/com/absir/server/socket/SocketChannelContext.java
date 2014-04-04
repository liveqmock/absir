/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-12 下午5:38:07
 */
package com.absir.server.socket;

import java.nio.channels.SocketChannel;

import com.absir.context.core.ContextBase;

/**
 * @author absir
 * 
 */
public class SocketChannelContext extends ContextBase {

	/** socketChannel */
	private SocketChannel socketChannel;

	/**
	 * @return the socketChannel
	 */
	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	/**
	 * @param socketChannel
	 *            the socketChannel to set
	 */
	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.context.JContextBase#getLifeTime()
	 */
	@Override
	protected long getLifeTime() {
		return SocketServerContext.get().getTimeout();
	}
}
