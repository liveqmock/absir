/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-17 下午4:28:46
 */
package com.absir.server.socket.resolver;

import java.nio.channels.SocketChannel;

import com.absir.server.socket.SocketBuffer;

/**
 * @author absir
 * 
 */
public interface SocketChannelResolver {

	/**
	 * @return
	 */
	public SocketBuffer createSocketBuff();

	/**
	 * @param socketBuffer
	 * @param buffer
	 * @param length
	 */
	public int readByteBuffer(SocketBuffer socketBuffer, byte[] buffer, int position, int length);

	/**
	 * @param socketBuffer
	 */
	public void readByteBufferDone(SocketBuffer socketBuffer);

	/**
	 * @param socketChannel
	 * @param headerLength
	 * @param bytes
	 * @return
	 */
	public byte[] writeByteBuffer(SocketChannel socketChannel, int headerLength, byte[] bytes);

}
