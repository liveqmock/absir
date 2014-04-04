/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-18 下午1:39:44
 */
package com.absir.bean.inject;

import java.lang.reflect.Method;

import com.absir.bean.inject.value.InjectType;

/**
 * @author absir
 * 
 */
public class InjectMethodOrder extends InjectMethod {

	/** order */
	int order;

	/**
	 * @param method
	 * @param injectName
	 * @param injectType
	 */
	public InjectMethodOrder(Method method, String injectName, InjectType injectType, int order) {
		super(method, injectName, injectType);
		// TODO Auto-generated constructor stub
		this.order = order;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.inject.InjectInvoker#getOrder()
	 */
	@Override
	public int getOrder() {
		return order;
	}

}
