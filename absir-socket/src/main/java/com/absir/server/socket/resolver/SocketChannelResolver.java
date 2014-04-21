/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-17 下午4:28:46
 */
package com.absir.server.socket.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.channels.SocketChannel;

import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Value;
import com.absir.core.kernel.KernelByte;
import com.absir.server.in.Input;
import com.absir.server.on.OnPut;
import com.absir.server.route.RouteMethod;
import com.absir.server.route.parameter.ParameterResolver;
import com.absir.server.socket.InputSocket;
import com.absir.server.socket.SocketBuffer;

/**
 * @author absir
 * 
 */
@Base
@Bean
public class SocketChannelResolver implements ParameterResolver<Object> {

	/** CHANNEL_RESOLVER */
	public static final SocketChannelResolver ME = BeanFactoryUtils.get(SocketChannelResolver.class);

	/** bufferMax */
	@Value("socket.buffer.max")
	private long bufferMax = 10240;

	/**
	 * @return
	 */
	public SocketBuffer createSocketBuff() {
		return new SocketBuffer();
	}

	/**
	 * @param socketBuffer
	 * @param buffer
	 * @param length
	 */
	public int readByteBuffer(SocketBuffer socketBuffer, byte[] buffer, int position, int length) {
		for (; position < length; position++) {
			if (socketBuffer.getBuff() == null) {
				int lengthIndex = socketBuffer.getLengthIndex();
				if (lengthIndex < 4) {
					int buffLength = lengthIndex == 0 ? buffer[position] : socketBuffer.getLength();
					if (lengthIndex > 0) {
						buffLength += buffer[position] << (8 * lengthIndex);
					}

					socketBuffer.setLength(buffLength);
					socketBuffer.setLengthIndex(++lengthIndex);
					if (lengthIndex == 4) {
						if (buffLength > 0 && buffLength < bufferMax) {
							socketBuffer.setBuffLengthIndex(0);
							socketBuffer.setBuff(new byte[buffLength]);

						} else {
							socketBuffer.setLength(0);
							socketBuffer.setLengthIndex(0);
						}
					}
				}

			} else {
				int buffLengthIndex = socketBuffer.getBuffLengthIndex();
				socketBuffer.getBuff()[buffLengthIndex] = buffer[position];
				socketBuffer.setBuffLengthIndex(++buffLengthIndex);
				if (buffLengthIndex >= socketBuffer.getLength()) {
					break;
				}
			}
		}

		return position;
	}

	/**
	 * @param socketBuffer
	 */
	public void readByteBufferDone(SocketBuffer socketBuffer) {
		socketBuffer.setBuff(null);
		socketBuffer.setLength(0);
		socketBuffer.setLengthIndex(0);
	}

	/**
	 * @param socketChannel
	 * @param headerLength
	 * @param bytes
	 * @return
	 */
	public byte[] writeByteBuffer(SocketChannel socketChannel, int headerLength, byte[] bytes) {
		int length = bytes.length;
		byte[] buffer = new byte[4 + headerLength + length];
		KernelByte.setLength(buffer, 0, headerLength + length);
		KernelByte.copy(bytes, buffer, 0, 4 + headerLength, length);
		return buffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.route.parameter.ParameterResolver#getParameter(int,
	 * java.lang.String[], java.lang.Class<?>[],
	 * java.lang.annotation.Annotation[][], java.lang.reflect.Method)
	 */
	@Override
	public Object getParameter(int i, String[] parameterNames, Class<?>[] parameterTypes, Annotation[][] annotations, Method method) {
		// TODO Auto-generated method stub
		return SocketChannel.class.isAssignableFrom(parameterTypes[i]) ? Boolean.TRUE : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.route.parameter.ParameterResolver#getParameterValue(
	 * com.absir.server.on.OnPut, java.lang.Object, java.lang.Class,
	 * java.lang.String, com.absir.server.route.RouteMethod)
	 */
	@Override
	public Object getParameterValue(OnPut onPut, Object parameter, Class<?> parameterType, String beanName, RouteMethod routeMethod) {
		// TODO Auto-generated method stub
		Input input = onPut.getInput();
		if (input instanceof InputSocket) {
			return ((InputSocket) input).getSocketChannel();
		}

		return null;
	}
}
