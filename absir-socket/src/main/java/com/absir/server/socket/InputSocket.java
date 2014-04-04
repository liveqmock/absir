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
import java.nio.channels.SocketChannel;
import java.util.Map;

import com.absir.core.kernel.KernelDyna;
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

	/** uri */
	private String uri;

	/** input */
	private String input;

	/** socketChannel */
	private SocketChannel socketChannel;

	/**
	 * @param model
	 */
	public InputSocket(InModel model, String uri, String input, SocketChannel socketChannel) {
		super(model);
		// TODO Auto-generated constructor stub
		this.uri = uri;
		this.input = input;
		this.socketChannel = socketChannel;
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
		return InMethod.GET;
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

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		SocketChannelResolver.ME.writeByteBuffer(socketChannel, b);
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
}
