/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-30 下午3:00:07
 */
package com.absir.server.route.returned;

import java.lang.reflect.Method;

import com.absir.bean.basis.Base;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Value;
import com.absir.context.core.ContextUtils;
import com.absir.server.in.Input;
import com.absir.server.on.OnPut;
import com.absir.server.value.Body;

/**
 * @author absir
 * 
 */
@Base(order = -128)
@Bean
public class ReturnedResolverBody implements ReturnedResolver<Integer> {

	/** charset */
	protected String charset = ContextUtils.getCharset().displayName();

	/** contentTypeCharset */
	@Value("server.body.contentType")
	protected String contentTypeCharset = "text/html;" + charset;

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @return the contentTypeCharset
	 */
	public String getContentTypeCharset() {
		return contentTypeCharset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.route.returned.ReturnedResolver#getReturned(java.lang
	 * .reflect.Method)
	 */
	@Override
	public Integer getReturned(Method method) {
		// TODO Auto-generated method stub
		Body body = method.getAnnotation(Body.class);
		return body == null ? null : body.value();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.route.returned.ReturnedResolver#getReturned(java.lang
	 * .Class)
	 */
	@Override
	public Integer getReturned(Class<?> beanClass) {
		// TODO Auto-generated method stub
		Body body = beanClass.getAnnotation(Body.class);
		return body == null ? null : body.value();
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
			input.write(returnValue.toString());
		}
	}
}