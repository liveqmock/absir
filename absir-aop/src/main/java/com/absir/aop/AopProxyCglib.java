/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-21 下午2:05:23
 */
package com.absir.aop;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author absir
 * 
 */
public class AopProxyCglib extends AopProxyHandler implements MethodInterceptor {

	/**
	 * @param beanType
	 * @param beanObject
	 */
	public AopProxyCglib(Class<?> beanType, Object beanObject) {
		super(beanType, beanObject);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.aop.AopProxyAbstract#invoke(java.lang.reflect.Method,
	 * java.lang.Object[], net.sf.cglib.proxy.MethodProxy)
	 */
	@Override
	public Object invoke(Method method, Object[] args, MethodProxy proxy) throws Throwable {
		// TODO Auto-generated method stub
		if (!method.isAccessible()) {
			method.setAccessible(true);
		}

		return method.invoke(beanObject, args);
	}
}
