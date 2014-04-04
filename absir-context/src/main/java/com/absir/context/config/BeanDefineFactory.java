/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-13 下午4:17:14
 */
package com.absir.context.config;

import java.lang.reflect.Method;

import com.absir.bean.basis.BeanFactory;
import com.absir.core.kernel.KernelReflect;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public class BeanDefineFactory extends BeanDefineBean {

	/**
	 * @param beanType
	 * @param beanName
	 * @param constructorBeanDefine
	 * @return
	 */
	public static BeanDefineBean getBeanDefineBean(Class<?> beanType, String beanName, BeanDefineArray constructorBeanDefine) {
		Method method = null;
		if (FactoryBean.class.isAssignableFrom(beanType)) {
			method = KernelReflect.declaredMethod(beanType, "getBeanObject");
		}

		if (method == null) {
			return new BeanDefineBean(beanType, beanName, constructorBeanDefine);
		}

		BeanDefineFactory beanDefineFactory = new BeanDefineFactory(beanType, beanName, constructorBeanDefine);
		beanDefineFactory.beanType = method.getReturnType();
		return beanDefineFactory;
	}

	/**
	 * @param beanType
	 * @param beanName
	 * @param constructorBeanDefine
	 */
	private BeanDefineFactory(Class<?> beanType, String beanName, BeanDefineArray constructorBeanDefine) {
		super(beanType, beanName, constructorBeanDefine);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.context.config.BeanDefineBean#getBeanObject(com.absir.bean.
	 * basis.BeanFactory)
	 */
	@Override
	public Object getBeanObject(BeanFactory beanFactory) {
		// TODO Auto-generated method stub
		Object beanObject = super.getBeanObject(beanFactory);
		return beanObject == null ? null : ((FactoryBean) beanObject).getBeanObject();
	}
}
