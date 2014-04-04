/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-13 下午5:25:49
 */
package com.absir.appserv.data;

import com.absir.aop.AopProxyUtils;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanFactory;
import com.absir.bean.basis.BeanScope;
import com.absir.bean.core.BeanDefineAbstractor;
import com.absir.bean.core.BeanFactoryImpl;

/**
 * @author absir
 * 
 */
public class DataQueryDefine extends BeanDefineAbstractor {

	/** beanType */
	private Class<?> beanType;

	/** name */
	private String name;

	/**
	 * @param beanName
	 * @param beanType
	 */
	public DataQueryDefine(String beanName, Class<?> beanType, String name) {
		this.beanName = beanName;
		this.beanType = beanType;
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanDefine#getBeanType()
	 */
	@Override
	public Class<?> getBeanType() {
		// TODO Auto-generated method stub
		return beanType;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanDefine#getBeanScope()
	 */
	@Override
	public BeanScope getBeanScope() {
		// TODO Auto-generated method stub
		return BeanScope.SINGLETON;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanDefine#getBeanComponent()
	 */
	@Override
	public Object getBeanComponent() {
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
		BeanDefine beanDefine = null;
		for (Class<?> cls : beanType.getInterfaces()) {
			beanDefine = beanFactory.getBeanDefine(null, cls);
			if (beanDefine != null) {
				if (BeanFactoryImpl.getBeanDefine(beanDefine, DataQueryDefine.class) == null && beanDefine.getBeanScope() == BeanScope.SINGLETON) {
					break;
				}

				beanDefine = null;
			}
		}

		return AopProxyUtils.getProxy(beanDefine == null ? null : beanDefine.getBeanObject(beanFactory), beanType, null, true, true);
	}
}
