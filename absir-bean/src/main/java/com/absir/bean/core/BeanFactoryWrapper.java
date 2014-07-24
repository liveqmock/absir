/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-8-26 下午1:32:38
 */
package com.absir.bean.core;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.absir.bean.basis.BeanConfig;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanFactory;
import com.absir.bean.basis.BeanScope;
import com.absir.core.kernel.KernelLang.FilterTemplate;

/**
 * @author absir
 * 
 */
public class BeanFactoryWrapper implements BeanFactory {

	/** beanFactory */
	private BeanFactory beanFactory;

	/**
	 * @param beanFactory
	 */
	public BeanFactoryWrapper(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	/**
	 * @return the beanFactory
	 */
	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanFactory#getBeanConfig()
	 */
	@Override
	public BeanConfig getBeanConfig() {
		// TODO Auto-generated method stub
		return beanFactory.getBeanConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanFactory#getBeanObject(java.lang.String)
	 */
	@Override
	public Object getBeanObject(String beanName) {
		// TODO Auto-generated method stub
		return beanFactory.getBeanObject(beanName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanFactory#getBeanObject(java.lang.Class)
	 */
	@Override
	public <T> T getBeanObject(Class<T> beanType) {
		// TODO Auto-generated method stub
		return beanFactory.getBeanObject(beanType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanFactory#getBeanObject(java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public <T> T getBeanObject(String beanName, Class<T> beanType) {
		// TODO Auto-generated method stub
		return beanFactory.getBeanObject(beanName, beanType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanFactory#getBeanObject(java.lang.String,
	 * java.lang.Class, boolean)
	 */
	@Override
	public <T> T getBeanObject(String beanName, Class<T> beanType, boolean forcible) {
		// TODO Auto-generated method stub
		return beanFactory.getBeanObject(beanName, beanType, forcible);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanFactory#getBeanObject(java.lang.String,
	 * java.lang.reflect.Type, boolean)
	 */
	@Override
	public Object getBeanObject(String beanName, Type beanType, boolean forcible) {
		// TODO Auto-generated method stub
		return beanFactory.getBeanObject(beanName, beanType, forcible);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanFactory#getBeanObjects(java.lang.Class)
	 */
	@Override
	public <T> List<T> getBeanObjects(Class<T> beanType) {
		// TODO Auto-generated method stub
		return beanFactory.getBeanObjects(beanType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanFactory#getBeanDefine(java.lang.String)
	 */
	@Override
	public BeanDefine getBeanDefine(String beanName) {
		// TODO Auto-generated method stub
		return beanFactory.getBeanDefine(beanName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanFactory#getBeanDefine(java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public BeanDefine getBeanDefine(String beanName, Class<?> beanType) {
		// TODO Auto-generated method stub
		return beanFactory.getBeanDefine(beanName, beanType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanFactory#getBeanDefines(java.lang.Class)
	 */
	@Override
	public List<BeanDefine> getBeanDefines(Class<?> beanType) {
		// TODO Auto-generated method stub
		return beanFactory.getBeanDefines(beanType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanFactory#getBeanDefineMap(java.lang.Class)
	 */
	@Override
	public Map<String, BeanDefine> getBeanDefineMap(Class<?> beanType) {
		// TODO Auto-generated method stub
		return beanFactory.getBeanDefineMap(beanType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanFactory#processBeanObject(com.absir.bean.basis
	 * .BeanScope, com.absir.bean.basis.BeanDefine, java.lang.Object)
	 */
	@Override
	public void processBeanObject(BeanScope beanScope, BeanDefine beanDefine, Object beanObject) {
		// TODO Auto-generated method stub
		beanFactory.processBeanObject(beanScope, beanDefine, beanObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanFactory#processBeanObject(com.absir.bean.basis
	 * .BeanScope, com.absir.bean.basis.BeanDefine, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void processBeanObject(BeanScope beanScope, BeanDefine beanDefine, Object beanObject, Object beanProxy) {
		// TODO Auto-generated method stub
		beanFactory.processBeanObject(beanScope, beanDefine, beanObject, beanProxy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanFactory#getSoftReferenceBeans(java.lang.Class)
	 */
	@Override
	public <T> List<T> getSoftReferenceBeans(Class<T> beanType) {
		// TODO Auto-generated method stub
		return beanFactory.getSoftReferenceBeans(beanType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanFactory#getSoftReferenceBeans(com.absir.core
	 * .kernel.KernelLang.FilterTemplate)
	 */
	@Override
	public List<Object> getSoftReferenceBeans(FilterTemplate<Object> filter) {
		// TODO Auto-generated method stub
		return beanFactory.getSoftReferenceBeans(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanFactory#registerBeanObject(java.lang.Object)
	 */
	@Override
	public BeanDefine registerBeanObject(Object beanObject) {
		// TODO Auto-generated method stub
		return beanFactory.registerBeanObject(beanObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanFactory#registerBeanObject(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public BeanDefine registerBeanObject(String beanName, Object beanObject) {
		// TODO Auto-generated method stub
		return beanFactory.registerBeanObject(beanName, beanObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanFactory#registerBeanObject(java.lang.String,
	 * com.absir.bean.basis.BeanScope, java.lang.Object)
	 */
	@Override
	public BeanDefine registerBeanObject(String beanName, BeanScope beanScope, Object beanObject) {
		// TODO Auto-generated method stub
		return beanFactory.registerBeanObject(beanName, beanScope, beanObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.basis.BeanFactory#registerBeanObject(java.lang.Class,
	 * java.lang.String, com.absir.bean.basis.BeanScope, java.lang.Object)
	 */
	@Override
	public BeanDefine registerBeanObject(Class<?> beanType, String beanName, BeanScope beanScope, Object beanObject) {
		// TODO Auto-generated method stub
		return beanFactory.registerBeanObject(beanType, beanName, beanScope, beanObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanFactory#unRegisterBeanObject(java.lang.Object)
	 */
	@Override
	public void unRegisterBeanObject(Object beanObject) {
		// TODO Auto-generated method stub
		beanFactory.unRegisterBeanObject(beanObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanFactory#unRegisterBeanObject(java.lang.String)
	 */
	@Override
	public void unRegisterBeanObject(String beanName) {
		// TODO Auto-generated method stub
		beanFactory.unRegisterBeanObject(beanName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanFactory#unRegisterBeanObject(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void unRegisterBeanObject(String beanName, Object beanObject) {
		// TODO Auto-generated method stub
		beanFactory.unRegisterBeanObject(beanName, beanObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanFactory#unRegisterBeanType(java.lang.Class<?>[])
	 */
	@Override
	public void unRegisterBeanType(Class<?>... beanTypes) {
		// TODO Auto-generated method stub
		beanFactory.unRegisterBeanType(beanTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanFactory#unRegisterWithoutBeanType(java.lang.
	 * Class[])
	 */
	@Override
	public void unRegisterWithoutBeanType(Class<?>... beanTypes) {
		// TODO Auto-generated method stub
		beanFactory.unRegisterWithoutBeanType(beanTypes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanFactory#registerBeanSoftObject(java.lang.Object)
	 */
	@Override
	public void registerBeanSoftObject(Object beanObject) {
		// TODO Auto-generated method stub
		beanFactory.registerBeanSoftObject(beanObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.basis.BeanFactory#unRegisterBeanSoftObject(java.lang.Object
	 * )
	 */
	@Override
	public void unRegisterBeanSoftObject(Object beanObject) {
		// TODO Auto-generated method stub
		beanFactory.unRegisterBeanSoftObject(beanObject);
	}
}
