/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-21 下午2:51:50
 */
package com.absir.aop;

import java.lang.reflect.Method;
import java.util.Iterator;

import net.sf.cglib.proxy.MethodProxy;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public interface AopInterceptor<T> {

	/**
	 * @param proxyHandler
	 * @param beanObject
	 * @param method
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	public T getInterceptor(AopProxyHandler proxyHandler, Object beanObject, Method method, Object[] args) throws Throwable;

	/**
	 * @param iterator
	 * @param interceptor
	 * @param proxyHandler
	 * @param method
	 * @param args
	 * @param proxy
	 * @return
	 * @throws Throwable
	 */
	public Object before(Iterator<AopInterceptor> iterator, T interceptor, AopProxyHandler proxyHandler, Method method, Object[] args, MethodProxy proxy) throws Throwable;

	/**
	 * @param interceptor
	 * @param value
	 * @param proxyHandler
	 * @param beanObject
	 * @param method
	 * @param args
	 * @param e
	 * @return
	 * @throws Throwable
	 */
	public Object after(T interceptor, Object value, AopProxyHandler proxyHandler, Object beanObject, Method method, Object[] args, Throwable e) throws Throwable;
}
