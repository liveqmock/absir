/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-30 上午9:57:58
 */
package com.absir.server.socket;

import java.io.Serializable;
import java.nio.channels.SocketChannel;

/**
 * @author absir
 * 
 */
public interface SocketReceiver<T extends Serializable> {

	/**
	 * @param socketChannel
	 * @return
	 * @throws Throwable
	 */
	public boolean accept(SocketChannel socketChannel) throws Throwable;

	/**
	 * @param socketChannel
	 * @param socketBuffer
	 * @throws Throwable
	 */
	public void register(SocketChannel socketChannel, SocketBuffer socketBuffer) throws Throwable;

	/**
	 * @param id
	 * @param socketChannel
	 * @throws Throwable
	 */
	public void unRegister(T id, SocketChannel socketChannel) throws Throwable;

	/**
	 * 
	 */
	public void clearAll();

	/**
	 * @param id
	 * @param socketChannel
	 * @param buffer
	 * @throws Throwable
	 */
	public void receiveByteBuffer(T id, SocketChannel socketChannel, byte[] buffer) throws Throwable;
}
