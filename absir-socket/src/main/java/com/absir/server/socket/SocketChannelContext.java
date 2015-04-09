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
import com.absir.context.core.ContextUtils;

/**
 * @author absir
 * 
 */
public class SocketChannelContext extends ContextBase {

	/** socketChannel */
	private SocketChannel socketChannel;

	/** socketReceiverContext */
	private SocketReceiverContext socketReceiverContext;

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

	/**
	 * @return the socketReceiverContext
	 */
	public SocketReceiverContext getSocketReceiverContext() {
		return socketReceiverContext;
	}

	/**
	 * @param socketReceiverContext
	 *            the socketReceiverContext to set
	 */
	public void setSocketReceiverContext(SocketReceiverContext socketReceiverContext) {
		this.socketReceiverContext = socketReceiverContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.context.JContextBase#getLifeTime()
	 */
	@Override
	protected long getLifeTime() {
		return SocketServerContext.ME.getTimeout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.context.core.ContextBase#stepDone(long)
	 */
	@Override
	public boolean stepDone(long contextTime) {
		// TODO Auto-generated method stub
		boolean stepDone = super.stepDone(contextTime);
		if (stepDone && socketChannel != null) {
			retainAt();
			stepDone = true;
			ContextUtils.getThreadPoolExecutor().execute(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					InputSocket.writeByteBuffer(socketChannel, SocketServerContext.ME.getBeat());
				}
			});
		}

		return stepDone;
	}

}
