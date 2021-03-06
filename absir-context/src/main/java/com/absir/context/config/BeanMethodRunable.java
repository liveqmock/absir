/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-24 下午2:36:16
 */
package com.absir.context.config;

import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.InjectMethod;

/**
 * @author absir
 * 
 */
public class BeanMethodRunable implements Runnable {

	/** beanObject */
	private Object beanObject;

	/** injectMethod */
	private InjectMethod injectMethod;

	/**
	 * 
	 */
	public BeanMethodRunable(Object beanObject, InjectMethod injectMethod) {
		this.beanObject = beanObject;
		this.injectMethod = injectMethod;
	}

	/**
	 * @return the beanObject
	 */
	public Object getBeanObject() {
		return beanObject;
	}

	/**
	 * @return the injectMethod
	 */
	public InjectMethod getInjectMethod() {
		return injectMethod;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		injectMethod.invoke(BeanFactoryUtils.get(), beanObject);
	}
}
