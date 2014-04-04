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

import com.absir.bean.basis.Base;
import com.absir.bean.inject.value.Bean;
import com.absir.server.in.IDispatcher;
import com.absir.server.on.OnPut;
import com.absir.server.route.RouteMethod;
import com.absir.server.route.parameter.ParameterResolver;
import com.absir.server.socket.JbServer;
import com.absir.server.socket.ServerContext;
import com.absir.server.socket.SocketReceiverContext;

/**
 * @author absir
 * 
 */
@Base
@Bean
public class SocketContextResolver implements ParameterResolver<Boolean> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.route.parameter.ParameterResolver#getParameter(int,
	 * java.lang.String[], java.lang.Class<?>[],
	 * java.lang.annotation.Annotation[][], java.lang.reflect.Method)
	 */
	@Override
	public Boolean getParameter(int i, String[] parameterNames, Class<?>[] parameterTypes, Annotation[][] annotations, Method method) {
		// TODO Auto-generated method stub
		Class<?> parameterType = parameterTypes[i];
		return ServerContext.class.isAssignableFrom(parameterType) ? Boolean.TRUE : JbServer.class.isAssignableFrom(parameterType) ? Boolean.FALSE : null;
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
	public Object getParameterValue(OnPut onPut, Boolean parameter, Class<?> parameterType, String beanName, RouteMethod routeMethod) throws Exception {
		// TODO Auto-generated method stub
		IDispatcher<?> dispatcher = onPut.getInput().getDispatcher();
		if (dispatcher instanceof SocketReceiverContext) {
			SocketReceiverContext receiverContext = (SocketReceiverContext) dispatcher;
			return parameter == Boolean.TRUE ? receiverContext.getServerContext() : receiverContext.getServerContext().getServer();
		}

		return null;
	}
}
