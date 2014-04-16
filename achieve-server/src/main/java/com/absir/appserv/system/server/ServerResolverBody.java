/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-11 下午5:27:56
 */
package com.absir.appserv.system.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.core.kernel.KernelArray;
import com.absir.server.in.InMethod;
import com.absir.server.in.Input;
import com.absir.server.on.OnPut;
import com.absir.server.route.RouteMethod;
import com.absir.server.route.parameter.ParameterResolver;
import com.absir.server.route.parameter.ParameterResolverMethod;
import com.absir.server.route.returned.ReturnedResolverBody;
import com.absir.server.value.Body;

/**
 * @author absir
 * 
 */
@Base
@Bean
public class ServerResolverBody extends ReturnedResolverBody implements ParameterResolver<Boolean>, ParameterResolverMethod {

	/** ME */
	public static final ServerResolverBody ME = BeanFactoryUtils.get(ServerResolverBody.class);

	/** objectMapper */
	protected ObjectMapper objectMapper;

	/**
	 * @return the objectMapper
	 */
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	/**
	 * @return
	 */
	@Inject
	protected void initResolver() {
		objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
	}

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
		return KernelArray.getAssignable(annotations[i], Body.class) == null ? null : Boolean.TRUE;
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
		Input input = onPut.getInput();
		if (String.class.isAssignableFrom(parameterType)) {
			return input.getInput();
		}

		InputStream inputStream = input.getInputStream();
		return inputStream == null ? objectMapper.readValue(input.getInput(), parameterType) : objectMapper.readValue(inputStream, parameterType);
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
	 * com.absir.server.route.returned.ReturnedResolver#resolveReturnedValue
	 * (java.lang.Object, java.lang.Object, com.absir.server.on.OnPut)
	 */
	@Override
	public void resolveReturnedValue(Object returnValue, Object returned, OnPut onPut) throws Exception {
		// TODO Auto-generated method stub
		if (returnValue != null) {
			Input input = onPut.getInput();
			input.setCharacterEncoding(charset);
			input.setContentTypeCharset(contentTypeCharset);
			if (returnValue instanceof String) {
				onPut.getInput().write((String) returnValue);

			} else {
				OutputStream outputStream = input.getOutputStream();
				if (outputStream == null) {
					input.write(objectMapper.writeValueAsString(returnValue));

				} else {
					objectMapper.writeValue(outputStream, returnValue);
				}
			}
		}
	}
}
