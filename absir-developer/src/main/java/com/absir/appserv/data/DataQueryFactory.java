/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-13 下午5:11:54
 */
package com.absir.appserv.data;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.absir.aop.AopMethodDefineAbstract;
import com.absir.appserv.data.value.Query;
import com.absir.appserv.data.value.Respository;
import com.absir.bean.basis.Basis;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.config.IBeanDefineSupply;
import com.absir.bean.core.BeanDefineType;
import com.absir.bean.core.BeanFactoryImpl;
import com.absir.bean.inject.value.Bean;

/**
 * @author absir
 * 
 */
@Basis
@Bean
public class DataQueryFactory extends AopMethodDefineAbstract<DataQueryInterceptor, DataQueryDetached, String> implements IBeanDefineSupply {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.config.IBeanDefineSupply#getBeanDefines(com.absir.bean
	 * .core.BeanFactoryImpl, java.lang.Class)
	 */
	@Override
	public List<BeanDefine> getBeanDefines(BeanFactoryImpl beanFactory, Class<?> beanType) {
		// TODO Auto-generated method stub
		if (beanType.isInterface() || Modifier.isAbstract(beanType.getModifiers())) {
			Respository respository = beanType.getAnnotation(Respository.class);
			if (respository != null) {
				DataQueryDefine beanDefine = new DataQueryDefine(BeanDefineType.getBeanName(respository.value(), beanType), beanType, respository.name());
				List<BeanDefine> beanDefines = new ArrayList<BeanDefine>();
				beanDefines.add(beanDefine);
				return beanDefines;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.aop.AopMethodDefine#getAopInterceptor(com.absir.bean.basis.
	 * BeanDefine, java.lang.Object)
	 */
	@Override
	public DataQueryInterceptor getAopInterceptor(BeanDefine beanDefine, Object beanObject) {
		// TODO Auto-generated method stub
		return BeanFactoryImpl.getBeanDefine(beanDefine, DataQueryDefine.class) == null ? null : new DataQueryInterceptor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.aop.AopMethodDefineAbstract#getVariable(com.absir.aop.
	 * AopInterceptorAbstract, com.absir.bean.basis.BeanDefine,
	 * java.lang.Object)
	 */
	@Override
	public String getVariable(DataQueryInterceptor aopInterceptor, BeanDefine beanDefine, Object beanObject) {
		return BeanFactoryImpl.getBeanDefine(beanDefine, DataQueryDefine.class).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.aop.AopMethodDefine#getAopInterceptor(java.lang.Object,
	 * java.lang.Class)
	 */
	@Override
	public DataQueryDetached getAopInterceptor(String variable, Class<?> beanType) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.aop.AopMethodDefine#getAopInterceptor(java.lang.Object,
	 * java.lang.Object, java.lang.Class, java.lang.reflect.Method)
	 */
	@Override
	public DataQueryDetached getAopInterceptor(DataQueryDetached interceptor, String variable, Class<?> beanType, Method method) {
		// TODO Auto-generated method stub
		Query query = method.getAnnotation(Query.class);
		return query == null ? null : new DataQueryDetached(query.value(), query.nativeQuery(), null, method.getReturnType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.core.kernel.KernelList.Orderable#getOrder()
	 */
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}
}
