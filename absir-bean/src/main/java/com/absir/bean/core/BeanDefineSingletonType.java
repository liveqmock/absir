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
public class BeanDefineSingletonType extends BeanDefineAbstract {

	/** beanType */
	protected Class<?> beanType;

	/** beanObject */
	protected Object beanObject;

	/**
	 * @param beanType
	 * @param beanObject
	 */
	public BeanDefineSingletonType(Class<?> beanType, Object beanObject) {
		this(beanType, null, beanObject);
	}

	/**
	 * @param beanType
	 * @param beanName
	 * @param beanObject
	 */
	public BeanDefineSingletonType(Class<?> beanType, String beanName, Object beanObject) {
		this.beanType = beanType;
		this.beanName = BeanDefineType.getBeanName(beanName, beanObject.getClass());
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
		return beanType;
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
