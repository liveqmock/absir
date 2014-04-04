/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-21 下午2:51:50
 */
package com.absir.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.cglib.proxy.MethodProxy;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public class AopInterceptorAbstract<T> implements AopInterceptor<T> {

	/** methodMapInterceptor */
	protected Map<Method, T> methodMapInterceptor = new HashMap<Method, T>();

	/**
	 * @return the methodMapInterceptor
	 */
	public Map<Method, T> getMethodMapInterceptor() {
		return methodMapInterceptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.aop.AopInterceptor#getInterceptor(com.absir.aop.AopProxyHandler
	 * , java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public T getInterceptor(AopProxyHandler proxyHandler, Object beanObject, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		return methodMapInterceptor.get(method);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.aop.AopInterceptor#before(java.util.Iterator,
	 * java.lang.Object, com.absir.aop.AopProxyHandler,
	 * java.lang.reflect.Method, java.lang.Object[],
	 * net.sf.cglib.proxy.MethodProxy)
	 */
	@Override
	public Object before(Iterator<AopInterceptor> iterator, T interceptor, AopProxyHandler proxyHandler, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		// TODO Auto-generated method stub
		return AopProxyHandler.VOID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.aop.AopInterceptor#after(java.lang.Object,
	 * java.lang.Object, com.absir.aop.AopProxyHandler, java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[], java.lang.Throwable)
	 */
	@Override
	public Object after(T interceptor, Object value, AopProxyHandler proxyHandler, Object beanObject, Method method, Object[] args, Throwable e) throws Throwable {
		// TODO Auto-generated method stub
		return value;
	}
}
