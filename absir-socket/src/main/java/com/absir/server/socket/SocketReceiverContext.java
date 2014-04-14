/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-17 下午3:22:56
 */
package com.absir.server.socket;

import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.SocketChannel;

import com.absir.context.core.ContextUtils;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.in.InDispatcher;
import com.absir.server.in.InMethod;
import com.absir.server.in.InModel;
import com.absir.server.in.Input;
import com.absir.server.socket.InputSocket.InputSocketAtt;
import com.absir.server.socket.resolver.SocketChannelResolver;

/**
 * @author absir
 * 
 */
public class SocketReceiverContext extends InDispatcher<SocketChannel, InputSocketAtt> implements SocketReceiver<Serializable> {

	/** serverContext */
	private ServerContext serverContext;

	/** UN_REGISTER_ID */
	@SuppressWarnings("serial")
	public static final Serializable UN_REGISTER_ID = new Serializable() {
	};

	/**
	 * @param serverContext
	 */
	public SocketReceiverContext(ServerContext serverContext) {
		this.serverContext = serverContext;
	}

	/**
	 * @return the serverContext
	 */
	public ServerContext getServerContext() {
		return serverContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.socket.SocketReceiver#accept(java.nio.channels.SocketChannel
	 * )
	 */
	@Override
	public boolean accept(SocketChannel socketChannel) throws Throwable {
		// TODO Auto-generated method stub
		return SocketServerContext.get().getSessionResolver().accept(socketChannel, serverContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.socket.SocketReceiver#register(java.nio.channels.
	 * SocketChannel, com.absir.server.socket.SocketBuffer)
	 */
	@Override
	public void register(final SocketChannel socketChannel, final SocketBuffer socketBuffer) throws Throwable {
		// TODO Auto-generated method stub
		socketBuffer.setId(UN_REGISTER_ID);
		final byte[] buffer = socketBuffer.getBuff();
		ContextUtils.getThreadPoolExecutor().execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Serializable id = SocketServerContext.get().getSessionResolver().register(socketChannel, serverContext, buffer);
					if (id == null) {
						socketBuffer.setId(null);
						SocketChannelResolver.ME.writeByteBuffer(socketChannel, 0, null, SocketServerContext.get().getFailed());

					} else {
						socketBuffer.setId(id);
						SocketChannelResolver.ME.writeByteBuffer(socketChannel, 0, null, SocketServerContext.get().getOk());
						serverContext.loginSocketChannelContext(id, createSocketChannelContext(id, socketChannel));
					}

				} catch (Throwable e) {
					SocketServer.close(socketChannel);
				}
			}
		});
	}

	/**
	 * @param id
	 * @param socketChannel
	 * @return
	 */
	protected SocketChannelContext createSocketChannelContext(Serializable id, SocketChannel socketChannel) {
		SocketChannelContext socketChannelContext = new SocketChannelContext();
		socketChannelContext.setSocketChannel(socketChannel);
		socketChannelContext.retainAt();
		return socketChannelContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.socket.SocketReceiver#unRegister(java.io.Serializable,
	 * java.nio.channels.SocketChannel)
	 */
	@Override
	public void unRegister(Serializable id, SocketChannel socketChannel) throws Throwable {
		// TODO Auto-generated method stub
		serverContext.logoutSocketChannelContext(id);
		SocketServerContext.get().getSessionResolver().unRegister(id, socketChannel, serverContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.socket.SocketReceiver#receiveByteBuffer(java.io.Serializable
	 * , java.nio.channels.SocketChannel, byte[])
	 */
	@Override
	public void receiveByteBuffer(final Serializable id, final SocketChannel socketChannel, final byte[] buffer) throws Throwable {
		// TODO Auto-generated method stub
		if (id == null || id == UN_REGISTER_ID) {
			throw new ServerException(ServerStatus.NO_LOGIN);
		}

		ContextUtils.getThreadPoolExecutor().execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (!doBeat(id, socketChannel, buffer, SocketServerContext.get().getBeat())) {
						if (buffer.length > 1) {
							InputSocketAtt inputSocketAtt = new InputSocketAtt(id, buffer);
							on(inputSocketAtt.getUrl(), socketChannel, inputSocketAtt);
						}
					}

				} catch (Throwable e) {
					// TODO Auto-generated catch block
					SocketServer.close(socketChannel);
				}
			}
		});
	}

	/**
	 * @param id
	 * @param socketChannel
	 * @param buffer
	 * @param beat
	 * @return
	 * @throws IOException
	 */
	protected boolean doBeat(Serializable id, final SocketChannel socketChannel, byte[] buffer, final byte[] beat) throws Throwable {
		int length = beat.length;
		if (buffer.length == length) {
			for (int i = 0; i < length; i++) {
				if (buffer[i] != beat[i]) {
					return false;
				}
			}

			SocketChannelResolver.ME.writeByteBuffer(socketChannel, 0, null, beat);
			serverContext.getChannelContexts().get(id).retainAt();
			SocketServerContext.get().getSessionResolver().doBeat(id, socketChannel, serverContext);
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.IDispatcher#getInMethod(java.lang.Object)
	 */
	@Override
	public InMethod getInMethod(SocketChannel req) {
		// TODO Auto-generated method stub
		return InMethod.GET;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.IDispatcher#decodeUri(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public String decodeUri(String uri, SocketChannel req) {
		// TODO Auto-generated method stub
		return uri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.InDispatcher#input(java.lang.String,
	 * com.absir.server.in.InMethod, com.absir.server.in.InModel,
	 * java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Input input(String uri, InMethod inMethod, InModel model, SocketChannel req, InputSocketAtt res) {
		// TODO Auto-generated method stub
		InputSocket socketInput = new InputSocket(model, req, res);
		return socketInput;
	}
}
