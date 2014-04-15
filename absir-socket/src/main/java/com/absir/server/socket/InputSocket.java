/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-17 下午4:06:43
 */
package com.absir.server.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.channels.SocketChannel;
import java.util.Map;

import com.absir.context.core.ContextUtils;
import com.absir.core.kernel.KernelByte;
import com.absir.core.kernel.KernelDyna;
import com.absir.server.in.InMethod;
import com.absir.server.in.InModel;
import com.absir.server.in.Input;
import com.absir.server.on.OnPut;
import com.absir.server.route.returned.ReturnedResolver;
import com.absir.server.socket.resolver.SocketChannelResolver;
import com.absir.server.socket.resolver.SocketChannelResolver.SocketHeaderProccesor;

/**
 * @author absir
 * 
 */
public class InputSocket extends Input implements SocketHeaderProccesor {

	/** socketChannel */
	private SocketChannel socketChannel;

	/** uri */
	private String uri;

	/** input */
	private String input;

	/** status */
	private int status;

	/** flag */
	private byte flag;

	/** input */
	private int callbackIndex;

	/**
	 * @param model
	 * @param inputSocketAtt
	 * @param socketChannel
	 */
	public InputSocket(InModel model, InputSocketAtt inputSocketAtt, SocketChannel socketChannel) {
		super(model);
		// TODO Auto-generated constructor stub
		this.socketChannel = socketChannel;
		setId(inputSocketAtt.getId());
		uri = inputSocketAtt.getUrl();
		input = inputSocketAtt.getInput();
		flag = inputSocketAtt.getFlag();
		callbackIndex = inputSocketAtt.getCallbackIndex();
	}

	/**
	 * @author absir
	 * 
	 */
	public static class InputSocketAtt {

		/** DEBUG_FLAG */
		public static final byte DEBUG_FLAG = (byte) (0x01 << 7);

		/** CALL_BACK_FLAG */
		public static final byte CALL_BACK_FLAG = (byte) (0x01 << 6);

		/** POST_FLAG */
		public static final byte POST_FLAG = (byte) (0x01 << 5);

		/** id */
		private Serializable id;

		/** buffer */
		private byte[] buffer;

		/** flag */
		private byte flag;

		/** callbackIndex */
		private int callbackIndex;

		/** url */
		private String url;

		/** postDataLength */
		private int postDataLength;

		/**
		 * @param id
		 * @param buffer
		 */
		public InputSocketAtt(Serializable id, byte[] buffer) {
			this.id = id;
			this.buffer = buffer;
			this.flag = buffer[0];
			int headerlength = 1;
			if ((flag & CALL_BACK_FLAG) != 0) {
				callbackIndex = KernelByte.getLength(buffer, headerlength);
				headerlength += 4;
			}

			if ((flag & POST_FLAG) != 0) {
				postDataLength = KernelByte.getLength(buffer, headerlength);
				headerlength += 4;
			}

			url = new String(buffer, headerlength, buffer.length - headerlength - postDataLength, ContextUtils.getCharset());
		}

		/**
		 * @return the id
		 */
		public Serializable getId() {
			return id;
		}

		/**
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}

		/**
		 * @return the input
		 */
		public String getInput() {
			return postDataLength == 0 ? null : new String(buffer, buffer.length - postDataLength, postDataLength, ContextUtils.getCharset());
		}

		/**
		 * @return the flag
		 */
		public byte getFlag() {
			return flag;
		}

		/**
		 * @return
		 */
		public InMethod getMethod() {
			return (flag & POST_FLAG) == 0 ? InMethod.GET : InMethod.POST;
		}

		/**
		 * @return the callbackIndex
		 */
		public int getCallbackIndex() {
			return callbackIndex;
		}
	}

	/**
	 * @return the socketChannel
	 */
	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	@Override
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return getModel().get(name);
	}

	@Override
	public void setAttribute(String name, Object obj) {
		// TODO Auto-generated method stub
		getModel().put(name, obj);
	}

	@Override
	public String getUri() {
		// TODO Auto-generated method stub
		return uri;
	}

	@Override
	public Map<String, Object> getParamMap() {
		// TODO Auto-generated method stub
		return getModel();
	}

	@Override
	public String getParam(String name) {
		// TODO Auto-generated method stub
		return KernelDyna.to(getModel().get(name), String.class);
	}

	@Override
	public InMethod getMethod() {
		// TODO Auto-generated method stub
		return input == null ? InMethod.GET : InMethod.POST;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#setStatus(int)
	 */
	@Override
	public void setStatus(int status) {
		// TODO Auto-generated method stub
		this.status = status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#paramDebug()
	 */
	@Override
	public boolean paramDebug() {
		// TODO Auto-generated method stub
		return (flag & InputSocketAtt.DEBUG_FLAG) != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#getParams(java.lang.String)
	 */
	@Override
	public String[] getParams(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInput() {
		// TODO Auto-generated method stub
		return input;
	}

	@Override
	public void setContentTypeCharset(String contentTypeCharset) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setCharacterEncoding(String charset) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.Input#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.socket.resolver.SocketChannelResolver.SocketHeaderProccesor
	 * #writeSocketHeader(int, int, byte[])
	 */
	@Override
	public void writeSocketHeader(int callbackIndex, int headerLength, byte[] buffer) {
		// TODO Auto-generated method stub
		byte flag = 0;
		if (headerLength >= 5) {
			flag |= InputSocketAtt.CALL_BACK_FLAG;
			KernelByte.setLength(buffer, 5, callbackIndex);
		}

		if (status != 0) {
			flag += InputSocketAtt.DEBUG_FLAG;
		}

		buffer[4] = flag;
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		SocketChannelResolver.ME.writeByteBuffer(socketChannel, callbackIndex, callbackIndex == 0 ? 1 : 5, this, b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.in.Input#getReturnedResolver(com.absir.server.on.OnPut)
	 */
	@Override
	public ReturnedResolver<Object> getReturnedResolver(OnPut onPut) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param socketChannel
	 * @param bytes
	 * @throws IOException
	 */
	public static void writeByteBuffer(SocketChannel socketChannel, byte[] bytes) throws IOException {
		SocketChannelResolver.ME.writeByteBuffer(socketChannel, 0, 0, null, bytes);
	}

	/** SOCKET_HEADER_PROCCESOR */
	private static final SocketHeaderProccesor SOCKET_HEADER_PROCCESOR = new SocketHeaderProccesor() {

		@Override
		public void writeSocketHeader(int callbackIndex, int headerLength, byte[] buffer) {
			// TODO Auto-generated method stub
			byte flag = 0;
			if (headerLength >= 5) {
				flag |= InputSocketAtt.CALL_BACK_FLAG;
				KernelByte.setLength(buffer, 5, callbackIndex);
			}

			buffer[4] = flag;
		}
	};

	/**
	 * @param socketChannel
	 * @param callbackIndex
	 * @param bytes
	 * @throws IOException
	 */
	public static void writeByteBuffer(SocketChannel socketChannel, int callbackIndex, byte[] bytes) throws IOException {
		SocketChannelResolver.ME.writeByteBuffer(socketChannel, callbackIndex, callbackIndex == 0 ? 1 : 5, SOCKET_HEADER_PROCCESOR, bytes);
	}
}
