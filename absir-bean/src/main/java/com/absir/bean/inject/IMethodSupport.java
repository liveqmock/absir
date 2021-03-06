/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-20 下午12:48:33
 */
package com.absir.bean.inject;

import java.lang.reflect.Method;

import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanScope;

/**
 * @author absir
 * 
 */
public interface IMethodSupport<T> {

	/**
	 * @param beanScope
	 * @param beanDefine
	 * @param method
	 * @return
	 */
	public T getInject(BeanScope beanScope, BeanDefine beanDefine, Method method);

	/**
	 * @param inject
	 * @param method
	 * @param beanMethod
	 * @param beanObject
	 * @return
	 */
	public InjectInvoker getInjectInvoker(T inject, Method method, Method beanMethod, Object beanObject);

}
