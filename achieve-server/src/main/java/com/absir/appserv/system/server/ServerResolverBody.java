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
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.absir.appserv.system.server.value.Result;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.binder.BinderData;
import com.absir.core.kernel.KernelArray;
import com.absir.core.kernel.KernelString;
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
public class ServerResolverBody extends ReturnedResolverBody implements ParameterResolver<Object>, ParameterResolverMethod, IServerResolverBody {

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
		objectMapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
		return getParameter(this, i, parameterNames, parameterTypes, annotations, method, true);
	}

	/**
	 * @param body
	 * @param i
	 * @param parameterNames
	 * @param parameterTypes
	 * @param annotations
	 * @param method
	 * @return
	 */
	public Object getParameter(IServerResolverBody body, int i, String[] parameterNames, Class<?>[] parameterTypes, Annotation[][] annotations, Method method) {
		return getParameter(body, i, parameterNames, parameterTypes, annotations, method, false);
	}

	/**
	 * @param body
	 * @param i
	 * @param parameterNames
	 * @param parameterTypes
	 * @param annotations
	 * @param method
	 * @param defaultBody
	 * @return
	 */
	protected Object getParameter(IServerResolverBody body, int i, String[] parameterNames, Class<?>[] parameterTypes, Annotation[][] annotations, Method method, boolean defaultBody) {
		Integer value = body.getBodyParameter(i, parameterNames, parameterTypes, annotations, method);
		if (defaultBody || value != null) {
			Result binderResult = KernelArray.getAssignable(annotations[i], Result.class);
			return binderResult == null ? value : binderResult;
		}

		return null;
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
	public Integer getBodyParameter(int i, String[] parameterNames, Class<?>[] parameterTypes, Annotation[][] annotations, Method method) {
		// TODO Auto-generated method stub
		Body body = KernelArray.getAssignable(annotations[i], Body.class);
		return body == null ? null : body.value();
	}

	/** BODY_OBJECT_NAME */
	private static final String BODY_OBJECT_NAME = ServerResolverBody.class.getName() + "@BODY_OBJECT_NAME";

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
		return getParameterValue(this, onPut, parameter, parameterType, beanName, routeMethod);
	}

	/**
	 * @param body
	 * @param onPut
	 * @param parameter
	 * @param parameterType
	 * @param beanName
	 * @param routeMethod
	 * @return
	 * @throws Exception
	 */
	public Object getParameterValue(IServerResolverBody body, OnPut onPut, Object parameter, Class<?> parameterType, String beanName, RouteMethod routeMethod) throws Exception {
		// TODO Auto-generated method stub
		if (parameter.getClass() == Result.class) {
			Object bodyObject = onPut.getInput().getAttribute(BODY_OBJECT_NAME);
			if (bodyObject == null) {
				bodyObject = body.getBodyParameterValue(onPut, 0, Object.class, null, routeMethod);
				onPut.getInput().setAttribute(BODY_OBJECT_NAME, bodyObject);
			}

			Result result = (Result) parameter;
			BinderData binderData = onPut.getBinderData();
			binderData.getBinderResult().setGroup(result.group());
			binderData.getBinderResult().setValidation(result.validation());
			String name = result.name();
			return binderData.bind(KernelString.isEmpty(name) || !(bodyObject instanceof Map) ? bodyObject : ((Map<?, ?>) bodyObject).get(name), beanName, parameterType);

		} else {
			return body.getBodyParameterValue(onPut, (Integer) parameter, parameterType, beanName, routeMethod);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.server.IServerResolverBody#getBodyParameterValue
	 * (com.absir.server.on.OnPut, int, java.lang.Class, java.lang.String,
	 * com.absir.server.route.RouteMethod)
	 */
	@Override
	public Object getBodyParameterValue(OnPut onPut, int group, Class<?> parameterType, String beanName, RouteMethod routeMethod) throws Exception {
		// TODO Auto-generated method stub
		Input input = onPut.getInput();
		if (parameterType == String.class) {
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
	public void resolveReturnedValue(Object returnValue, Integer returned, OnPut onPut) throws Exception {
		// TODO Auto-generated method stub
		if (returnValue != null) {
			Input input = onPut.getInput();
			input.setCharacterEncoding(charset);
			input.setContentTypeCharset(contentTypeCharset);
			if (returnValue.getClass() == String.class) {
				onPut.getInput().write((String) returnValue);

			} else {
				OutputStream outputStream = input.getOutputStream();
				if (outputStream == null) {
					input.write(objectMapper.writeValueAsBytes(returnValue));

				} else {
					objectMapper.writeValue(outputStream, returnValue);
				}
			}
		}
	}
}
