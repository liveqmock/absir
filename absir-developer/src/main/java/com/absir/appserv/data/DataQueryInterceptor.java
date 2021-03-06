/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-13 下午5:12:29
 */
package com.absir.appserv.data;

import java.lang.reflect.Method;
import java.util.Iterator;

import net.sf.cglib.proxy.MethodProxy;

import com.absir.aop.AopInterceptor;
import com.absir.aop.AopInterceptorAbstract;
import com.absir.aop.AopProxyHandler;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public class DataQueryInterceptor extends AopInterceptorAbstract<DataQueryDetached> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.aop.AopInterceptorAbstract#before(java.lang.Object,
	 * java.util.Iterator, java.lang.Object, com.absir.aop.AopProxyHandler,
	 * java.lang.reflect.Method, java.lang.Object[],
	 * net.sf.cglib.proxy.MethodProxy)
	 */
	@Override
	public Object before(Object proxy, Iterator<AopInterceptor> iterator, DataQueryDetached interceptor, AopProxyHandler proxyHandler, Method method, Object[] args, MethodProxy methodProxy)
			throws Throwable {
		// TODO Auto-generated method stub
		return interceptor.invoke(args);
	}
}
