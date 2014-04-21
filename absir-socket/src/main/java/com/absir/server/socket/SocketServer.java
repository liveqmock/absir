/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-22 下午2:02:53
 */
package com.absir.server.socket;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.absir.context.core.ContextUtils;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.socket.resolver.SocketChannelResolver;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SocketServer {

	/** serverSocket */
	private ServerSocketChannel serverSocketChannel;

	/** serverSelector */
	private Selector serverSelector;

	/**
	 * 开始服务
	 * 
	 * @param port
	 * @param backlog
	 * @param inetAddress
	 * @param bufferSize
	 * @param receiveBufferSize
	 * @param sendBufferSize
	 * @param receiver
	 * @throws IOException
	 */
	public synchronized void start(int port, int backlog, InetAddress inetAddress, final int bufferSize, int receiveBufferSize, final int sendBufferSize, final SocketReceiver receiver)
			throws IOException {
		if (serverSocketChannel != null) {
			throw new ServerException(ServerStatus.IN_FAILED);
		}

		// 初始化监听服务
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.socket().setReceiveBufferSize(receiveBufferSize);
		serverSocketChannel.socket().bind(new InetSocketAddress(inetAddress, port), backlog);

		// 接受请求
		serverSelector = Selector.open();
		serverSocketChannel.register(serverSelector, SelectionKey.OP_ACCEPT);
		ContextUtils.getThreadPoolExecutor().execute(new Runnable() {

			@Override
			public void run() {
				Selector selector = serverSelector;
				ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
				while (selector == serverSelector) {
					try {
						selector.select();
						Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
						while (iterator.hasNext()) {
							SelectionKey key = iterator.next();
							iterator.remove();
							if (key.isAcceptable()) {
								// 接受请求
								SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
								socketChannel.configureBlocking(false);
								socketChannel.socket().setSendBufferSize(sendBufferSize);
								try {
									if (receiver.accept(socketChannel)) {
										socketChannel.register(selector, SelectionKey.OP_READ);
										continue;
									}

								} catch (Throwable e) {
								}

								close(socketChannel);

							} else {
								// 处理数据
								SocketBuffer socketBuffer = (SocketBuffer) key.attachment();
								SocketChannel socketChannel = (SocketChannel) key.channel();
								buffer.clear();
								try {
									int length = socketChannel.read(buffer);
									if (length > 0) {
										if (socketBuffer == null) {
											socketBuffer = SocketChannelResolver.ME.createSocketBuff();
											key.attach(socketBuffer);
										}

										byte[] array = buffer.array();
										int position = 0;
										while (position < length) {
											position = SocketChannelResolver.ME.readByteBuffer(socketBuffer, array, position, length);
											if (socketBuffer.getBuff() != null && socketBuffer.getLength() <= socketBuffer.getBuffLengthIndex()) {
												if (socketBuffer.getId() == null) {
													receiver.register(socketChannel, socketBuffer);
													Serializable id = socketBuffer.getId();
													if (id == null) {
														break;
													}

												} else {
													receiver.receiveByteBuffer(socketBuffer.getId(), socketChannel, socketBuffer.getBuff());
												}

												SocketChannelResolver.ME.readByteBufferDone(socketBuffer);
											}

											position++;
										}

										continue;
									}

								} catch (Throwable e) {
									// e.printStackTrace();
								}

								// 注销请求
								key.cancel();
								try {
									if (socketBuffer != null) {
										receiver.unRegister(socketBuffer.getId(), socketChannel);
									}

								} catch (Throwable e) {
								}

								close(socketChannel);
							}
						}

					} catch (Throwable e) {
						// e.printStackTrace();
					}
				}

				try {
					selector.close();

				} catch (Throwable e) {
					// TODO Auto-generated catch block
				}
			}

		});
	}

	/**
	 * 关闭服务
	 */
	public synchronized void close() {
		if (serverSocketChannel != null) {
			try {
				serverSocketChannel.close();

			} catch (Throwable e) {
				// TODO Auto-generated catch block
			}

			serverSocketChannel = null;
			serverSelector = null;
		}
	}

	/**
	 * @param socketChannel
	 */
	public static void close(SocketChannel socketChannel) {
		try {
			// new Exception().printStackTrace();
			socketChannel.close();

		} catch (Throwable e) {
			// TODO Auto-generated catch block
		}
	}
}
