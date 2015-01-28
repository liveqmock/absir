/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-13 下午4:47:43
 */
package com.absir.bean.core;

import com.absir.bean.basis.BeanFactory;
import com.absir.bean.basis.BeanScope;

/**
 * @author absir
 * 
 */
public class BeanDefineSingleton extends BeanDefineAbstract {

	/** beanObject */
	protected Object beanObject;

	/**
	 * @param beanObject
	 */
	public BeanDefineSingleton(Object beanObject) {
		this(null, beanObject);
	}

	/**
	 * @param beanName
	 * @param beanObject
	 */
	public BeanDefineSingleton(String beanName, Object beanObject) {
		this.beanName = beanObject == null ? beanName : BeanDefineType.getBeanName(beanName, beanObject.getClass());
		this.beanObject = beanObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.android.bean.IBeanDefine#getBeanType()
	 */
	@Override
	public Class<?> getBeanType() {
		// TODO Auto-generated method stub
		return beanObject.getClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanDefine#getBeanObject(com.absir.bean.basis.
	 * BeanFactory)
	 */
	@Override
	public Object getBeanObject(BeanFactory beanFactory) {
		// TODO Auto-generated method stub
		return beanObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.android.bean.IBeanDefine#getBeanScope()
	 */
	@Override
	public BeanScope getBeanScope() {
		// TODO Auto-generated method stub
		return BeanScope.SINGLETON;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.android.bean.value.IBeanDefine#getBeanComponent()
	 */
	@Override
	public Object getBeanComponent() {
		// TODO Auto-generated method stub
		return null;
	}
}
