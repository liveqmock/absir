/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月10日 下午4:54:30
 */
package com.absir.appserv.advice;

import java.lang.reflect.Method;

/**
 * @author absir
 *
 */
public abstract class MethodRound extends MethodAdvice {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.advice.IMethodAdvice#before(com.absir.appserv.advice
	 * .AdviceInvoker, java.lang.Object, java.lang.reflect.Method,
	 * java.lang.Object[])
	 */
	@Override
	public Object before(AdviceInvoker invoker, Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		return advice(invoker, proxy, method, args);
	}

	/**
	 * @param proxy
	 * @param method
	 * @param args
	 */
	public abstract Object advice(AdviceInvoker invoker, Object proxy, Method method, Object[] args);

}
