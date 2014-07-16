/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-11 下午5:27:56
 */
package com.absir.appserv.system.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.absir.appserv.system.helper.HelperServer;
import com.absir.appserv.system.server.value.Bodys;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.context.core.ContextUtils;
import com.absir.core.kernel.KernelArray;
import com.absir.server.in.InMethod;
import com.absir.server.in.Input;
import com.absir.server.on.OnPut;
import com.absir.server.route.RouteMethod;
import com.absir.server.route.parameter.ParameterResolver;
import com.absir.server.route.parameter.ParameterResolverMethod;
import com.absir.server.route.returned.ReturnedResolver;
import com.absir.server.value.Body;

/**
 * @author absir
 * 
 */
@Base
@Bean
public class ServerResolverBodys implements ParameterResolver<Object>, ParameterResolverMethod, ReturnedResolver<Class<?>>, IServerResolverBody {

	/** ME */
	public static final ServerResolverBodys ME = BeanFactoryUtils.get(ServerResolverBodys.class);

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
		return ServerResolverBody.ME.getParameter(this, i, parameterNames, parameterTypes, annotations, method);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.server.IServerResolverBody#getBodyParameter(int,
	 * java.lang.String[], java.lang.Class[],
	 * java.lang.annotation.Annotation[][], java.lang.reflect.Method)
	 */
	@Override
	public Class<?> getBodyParameter(int i, String[] parameterNames, Class<?>[] parameterTypes, Annotation[][] annotations, Method method) {
		// TODO Auto-generated method stub
		return KernelArray.getAssignable(annotations[i], Bodys.class) == null ? null : Body.class;
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
	public Object getParameterValue(OnPut onPut, Object parameter, Class<?> parameterType, String beanName, RouteMethod routeMethod) throws Exception {
		// TODO Auto-generated method stub
		return ServerResolverBody.ME.getParameterValue(onPut.getInput().isDebug() ? ServerResolverBody.ME : this, onPut, parameter, parameterType, beanName, routeMethod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.server.IServerResolverBody#getBodyParameterValue
	 * (com.absir.server.on.OnPut, java.lang.Object, java.lang.Class,
	 * java.lang.String, com.absir.server.route.RouteMethod)
	 */
	@Override
	public Object getBodyParameterValue(OnPut onPut, Object parameter, Class<?> parameterType, String beanName, RouteMethod routeMethod) throws Exception {
		// TODO Auto-generated method stub
		Input input = onPut.getInput();
		InputStream inputStream = input.getInputStream();
		if (inputStream == null) {
			if (input.getInput() == null) {
				return null;

			} else {
				inputStream = new ByteArrayInputStream(input.getInput().getBytes(ContextUtils.getCharset()));
			}
		}

		GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
		try {
			return ServerResolverBody.ME.getObjectMapper().readValue(gzipInputStream, parameterType);

		} finally {
			gzipInputStream.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.route.parameter.ParameterResolverMethod#resolveMethods
	 * (java.lang.Object, java.util.List)
	 */
	@Override
	public List<InMethod> resolveMethods(Object parameter, List<InMethod> inMethods) {
		// TODO Auto-generated method stub
		if (inMethods == null) {
			inMethods = new ArrayList<InMethod>();
		}

		if (inMethods.isEmpty()) {
			inMethods.add(InMethod.POST);
		}

		return inMethods;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.route.returned.ReturnedResolver#getReturned(java.lang
	 * .reflect.Method)
	 */
	@Override
	public Class<?> getReturned(Method method) {
		// TODO Auto-generated method stub
		return method.getAnnotation(Bodys.class) == null ? null : Body.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.route.returned.ReturnedResolver#resolveReturnedValue
	 * (java.lang.Object, java.lang.Object, com.absir.server.on.OnPut)
	 */
	@Override
	public void resolveReturnedValue(Object returnValue, Class<?> returned, OnPut onPut) throws Exception {
		// TODO Auto-generated method stub
		if (onPut.getInput().isDebug()) {
			ServerResolverBody.ME.resolveReturnedValue(returnValue, returned, onPut);

		} else {
			sResolveReturnedValue(returnValue, returned, onPut);
		}
	}

	/**
	 * @param returnValue
	 * @param returned
	 * @param onPut
	 * @throws Exception
	 */
	public void sResolveReturnedValue(Object returnValue, Class<?> returned, OnPut onPut) throws Exception {
		if (returnValue != null) {
			ServerResolverBody serverResolverBody = ServerResolverBody.ME;
			Input input = onPut.getInput();
			input.setCharacterEncoding(serverResolverBody.getCharset());
			input.setContentTypeCharset(serverResolverBody.getContentTypeCharset());
			byte[] bufferBytes = returnValue.getClass() == String.class ? ((String) returnValue).getBytes(ContextUtils.getCharset()) : serverResolverBody.getObjectMapper().writeValueAsBytes(
					returnValue);
			OutputStream outputStream = input.getOutputStream();
			if (outputStream == null) {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				HelperServer.zipCompress(bufferBytes, 0, bufferBytes.length, byteArrayOutputStream);
				bufferBytes = byteArrayOutputStream.toByteArray();
				input.write(bufferBytes);

			} else {
				HelperServer.zipCompress(bufferBytes, 0, bufferBytes.length, outputStream);
			}
		}
	}
}
