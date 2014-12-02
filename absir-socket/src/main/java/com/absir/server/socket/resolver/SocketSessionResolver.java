/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-19 上午10:23:31
 */
package com.absir.server.socket.resolver;

import java.io.Serializable;
import java.nio.channels.SocketChannel;

import com.absir.server.socket.ServerContext;
import com.absir.server.socket.SocketBuffer;

/**
 * @author absir
 * 
 */
public interface SocketSessionResolver {

	/**
	 * @param socketChannel
	 * @param serverContext
	 * @throws Throwable
	 * @return
	 */
	public boolean accept(SocketChannel socketChannel, ServerContext serverContext) throws Throwable;

	/**
	 * @param socketChannel
	 * @param serverContext
	 * @param buffer
	 * @param socketBuffer
	 * @return
	 * @throws Throwable
	 */
	public ServerContext register(SocketChannel socketChannel, ServerContext serverContext, byte[] buffer, SocketBuffer socketBuffer) throws Throwable;

	/**
	 * @param id
	 * @param socketChannel
	 * @param serverContext
	 * @return
	 */
	public boolean doBeat(Serializable id, SocketChannel socketChannel, ServerContext serverContext);

	/**
	 * @param id
	 * @param socketChannel
	 * @param serverContext
	 * @throws Throwable
	 */
	public void unRegister(Serializable id, SocketChannel socketChannel, ServerContext serverContext) throws Throwable;

}
