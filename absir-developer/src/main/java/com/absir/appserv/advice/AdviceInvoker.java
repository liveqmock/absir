/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月10日 下午5:43:08
 */
package com.absir.appserv.advice;

import java.lang.reflect.Method;

/**
 * @author absir
 *
 */
public abstract class AdviceInvoker {

	/** index */
	protected int index;

	/** length */
	protected int length;

	/**
	 * @param advices
	 */
	public AdviceInvoker(IMethodAdvice[] advices) {
		length = advices.length;
	}

	/**
	 * @return
	 * @throws Throwable
	 */
	public abstract Object invoke() throws Throwable;

	/**
	 * @param advices
	 * @param proxy
	 * @param method
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	protected Object before(IMethodAdvice[] advices, Object proxy, Method method, Object[] args) throws Throwable {
		return advices[index++].before(proxy, method, args);
	}

	/**
	 * @param advices
	 * @param proxy
	 * @param returnValue
	 * @param method
	 * @param args
	 * @param e
	 * @return
	 * @throws Throwable
	 */
	protected Object after(IMethodAdvice[] advices, Object proxy, Object returnValue, Method method, Object[] args, Throwable e) throws Throwable {
		return advices[--index].after(proxy, returnValue, method, args, e);
	}
}
