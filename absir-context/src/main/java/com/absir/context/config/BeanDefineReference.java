/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-13 下午8:30:53
 */
package com.absir.context.config;

import com.absir.bean.basis.BeanFactory;
import com.absir.bean.basis.BeanScope;
import com.absir.bean.core.BeanDefineAbstract;
import com.absir.core.kernel.KernelDyna;

/**
 * @author absir
 * 
 */
public class BeanDefineReference extends BeanDefineAbstract {

	/** required */
	private boolean required;

	/**
	 * @param beanName
	 */
	public BeanDefineReference(String beanName, String required) {
		this.beanName = beanName;
		this.required = KernelDyna.to(required, boolean.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanDefine#getBeanType()
	 */
	@Override
	public Class<?> getBeanType() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanDefine#getBeanScope()
	 */
	@Override
	public BeanScope getBeanScope() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanDefine#getBeanComponent()
	 */
	@Override
	public Object getBeanComponent() {
		// TODO Auto-generated method stub
		return null;
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
		return null;
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}
}
