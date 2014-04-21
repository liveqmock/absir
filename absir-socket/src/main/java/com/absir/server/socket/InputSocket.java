/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-17 下午4:06:43
 */
package com.absir.server.socket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

import com.absir.context.core.ContextUtils;
import com.absir.core.kernel.KernelByte;
import com.absir.core.kernel.KernelDyna;
import com.absir.server.exception.ServerStatus;
import com.absir.server.in.InMethod;
import com.absir.server.in.InModel;
import com.absir.server.in.Input;
import com.absir.server.on.OnPut;
import com.absir.server.route.returned.ReturnedResolver;
import com.absir.server.socket.resolver.SocketChannelResolver;

/**
 * @author absir
 * 
 */
public class InputSocket extends Input {

	/** socketChannel */
	private SocketChannel socketChannel;

	/** uri */
	private String uri;

	/** status */
	private int status = ServerStatus.ON_SUCCESS.getCode();

	/** flag */
	private byte flag;

	/** input */
	private int callbackIndex;

	/** input */
	private String input;

	/** inputStream */
	private byte[] inputBuffer;

	/** inputPos */
	private int inputPos;

	/** inputCount */
	private int inputCount;

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
		flag = inputSocketAtt.getFlag();
		callbackIndex = inputSocketAtt.getCallbackIndex();
		inputBuffer = inputSocketAtt.getBuffer();
		if (inputBuffer != null) {
			inputCount = inputSocketAtt.getPostDataLength();
			inputPos = inputBuffer.length - inputCount;
		}
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

		/**
		 * @return the buffer
		 */
		public byte[] getBuffer() {
			return buffer;
		}

		/**
		 * @return the postDataLength
		 */
		public int getPostDataLength() {
			return postDataLength;
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
		return inputBuffer == null ? null : new ByteArrayInputStream(inputBuffer, inputPos, inputCount);
	}

	@Override
	public String getInput() {
		// TODO Auto-generated method stub
		if (input == null && inputBuffer != null) {
			input = new String(inputBuffer, inputPos, inputCount, ContextUtils.getCharset());
			inputBuffer = null;
		}

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

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		writeByteBufferSuccess(socketChannel, status == ServerStatus.ON_SUCCESS.getCode() ? true : false, callbackIndex, b);
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
	 * @return
	 */
	public static boolean writeByteBuffer(SocketChannel socketChannel, byte[] bytes) {
		byte[] buffer = SocketChannelResolver.ME.writeByteBuffer(socketChannel, 0, bytes);
		try {
			socketChannel.write(ByteBuffer.wrap(buffer));
			return true;

		} catch (Throwable e) {
			SocketServer.close(socketChannel);
			return false;
		}
	}

	/**
	 * @param socketChannel
	 * @param callbackIndex
	 * @param bytes
	 * @return
	 */
	public static boolean writeByteBuffer(SocketChannel socketChannel, int callbackIndex, byte[] bytes) {
		return writeByteBuffer(socketChannel, (byte) 0, callbackIndex, bytes);
	}

	/**
	 * @param socketChannel
	 * @param flag
	 * @param callbackIndex
	 * @param bytes
	 * @return
	 */
	public static boolean writeByteBuffer(SocketChannel socketChannel, byte flag, int callbackIndex, byte[] bytes) {
		int headerLength = callbackIndex == 0 ? 1 : 5;
		byte[] buffer = SocketChannelResolver.ME.writeByteBuffer(socketChannel, headerLength, bytes);
		headerLength = buffer.length - bytes.length - headerLength;
		if (callbackIndex != 0) {
			flag |= InputSocketAtt.CALL_BACK_FLAG;
			KernelByte.setLength(buffer, headerLength + 1, callbackIndex);
		}

		buffer[headerLength] = flag;
		try {
			socketChannel.write(ByteBuffer.wrap(buffer));
			return true;

		} catch (Throwable e) {
			SocketServer.close(socketChannel);
			return false;
		}
	}

	/**
	 * @param socketChannel
	 * @param success
	 * @param callbackIndex
	 * @param bytes
	 * @return
	 */
	public static boolean writeByteBufferSuccess(SocketChannel socketChannel, boolean success, int callbackIndex, byte[] bytes) {
		return writeByteBuffer(socketChannel, success == true ? 0 : InputSocketAtt.DEBUG_FLAG, callbackIndex, bytes);
	}
}
