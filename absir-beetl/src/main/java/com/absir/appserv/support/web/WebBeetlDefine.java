/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月24日 下午1:02:10
 */
package com.absir.appserv.support.web;

import java.lang.reflect.Method;

import org.beetl.core.GroupTemplate;

import com.absir.appserv.support.web.WebBeetlDefine.BeetlConfigure;
import com.absir.bean.basis.Basis;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.inject.IMethodDefine;
import com.absir.bean.inject.value.Bean;

/**
 * @author absir
 *
 */
@Basis
@Bean
public class WebBeetlDefine implements IMethodDefine<BeetlConfigure>, IWebBeetl {

	/**
	 * @author absir
	 *
	 */
	public static enum BeetlConfigure {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.inject.IMethodDefine#getDefine(java.lang.Class,
	 * java.lang.reflect.Method, com.absir.bean.basis.BeanDefine)
	 */
	@Override
	public BeetlConfigure getDefine(Class<?> beanType, Method method, BeanDefine beanDefine) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.inject.IMethodDefine#setDefine(java.lang.Object,
	 * java.lang.Class, java.lang.reflect.Method, java.lang.reflect.Method,
	 * com.absir.bean.basis.BeanDefine)
	 */
	@Override
	public void setDefine(BeetlConfigure define, Class<?> beanType, Method beanMethod, Method method, BeanDefine beanDefine) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.support.web.IWebBeetl#process(org.beetl.core.GroupTemplate
	 * )
	 */
	@Override
	public void process(GroupTemplate groupTemplate) {
		// TODO Auto-generated method stub

	}
}
