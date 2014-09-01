/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月24日 下午1:02:10
 */
package com.absir.appserv.support.web;

import java.lang.reflect.Method;

import webit.script.global.GlobalManager;
import webit.script.global.GlobalRegister;

import com.absir.bean.basis.Basis;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanScope;
import com.absir.bean.inject.IMethodSupport;
import com.absir.bean.inject.InjectInvoker;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;

/**
 * @author absir
 *
 */
@Basis
@Bean
public class WebItSupply implements IMethodSupport<String>, GlobalRegister {

	/**
	 * 
	 */
	@Inject
	public void initSupply(GlobalRegister[] globalRegisters) {
		WebItGlobalManager.globalRegisters = globalRegisters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * webit.script.global.GlobalRegister#regist(webit.script.global.GlobalManager
	 * )
	 */
	@Override
	public void regist(GlobalManager manager) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.inject.IMethodSupport#getInject(com.absir.bean.basis.BeanScope
	 * , com.absir.bean.basis.BeanDefine, java.lang.reflect.Method)
	 */
	@Override
	public String getInject(BeanScope beanScope, BeanDefine beanDefine, Method method) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.inject.IMethodSupport#getInjectInvoker(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object)
	 */
	@Override
	public InjectInvoker getInjectInvoker(String inject, Method method, Object beanObject) {
		// TODO Auto-generated method stub
		return null;
	}
}
