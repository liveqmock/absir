/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月29日 下午5:15:42
 */
package com.absir.appserv.lang;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

import net.sf.cglib.proxy.MethodProxy;

import com.absir.aop.AopInterceptor;
import com.absir.aop.AopInterceptorAbstract;
import com.absir.aop.AopMethodDefine;
import com.absir.aop.AopProxyHandler;
import com.absir.appserv.lang.LangBundleImpl.LangIterceptor;
import com.absir.appserv.support.Developer;
import com.absir.bean.basis.Base;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.core.BeanConfigImpl;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Stopping;
import com.absir.context.lang.LangBundle;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.kernel.KernelLang.ObjectEntry;
import com.absir.core.kernel.KernelString;
import com.absir.server.on.OnPut;

/**
 * @author absir
 *
 */
@SuppressWarnings("rawtypes")
@Base(order = -1)
@Bean
public class LangBundleImpl extends LangBundle implements AopMethodDefine<LangIterceptor, Entry<String, Class<?>>, Boolean> {

	/**
	 * @param langBase
	 * @param value
	 */
	public static void setLangEntity(ILangBase langBase, String value) {
		String[] langs = value.split(",", 3);
		if (langs.length == 3) {
			langBase.setLang(langs[0], LangBundle.ME.getLocale(DynaBinder.to(langs[1], int.class)), langs[2]);
		}
	}

	/**
	 * @author absir
	 *
	 */
	public static class LangIterceptor extends AopInterceptorAbstract<Entry<String, Class<?>>> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.aop.AopInterceptor#before(java.lang.Object,
		 * java.util.Iterator, java.lang.Object, com.absir.aop.AopProxyHandler,
		 * java.lang.reflect.Method, java.lang.Object[],
		 * net.sf.cglib.proxy.MethodProxy)
		 */
		@Override
		public Object before(Object proxy, Iterator<AopInterceptor> iterator, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler, Method method, Object[] args, MethodProxy methodProxy)
				throws Throwable {
			// TODO Auto-generated method stub
			if (LangBundleImpl.ME.isI18n()) {
				OnPut onPut = OnPut.get();
				if (onPut != null) {
					Locale locale = onPut.getInput().getLocale();
					if (locale != null) {
						return ((ILangBase) proxy).getLang(interceptor.getKey(), locale, interceptor.getValue());
					}
				}
			}

			return AopProxyHandler.VOID;
		}
	}

	/**
	 * 内置国际化资源写入
	 */
	@Stopping
	public void stopping() {
		if (!resourceLangs.isEmpty()) {
			String var = locale.getLanguage();
			if (!KernelString.isEmpty(var)) {
				String resource = langResource + var;
				var = locale.getCountry();
				if (!KernelString.isEmpty(var)) {
					resource += '_' + var;
					var = locale.getVariant();
					if (!KernelString.isEmpty(var)) {
						resource += '_' + var;
					}
				}

				File file = new File(resource + "/general.properties");
				BeanConfigImpl.writeProperties(resourceLangs, file);
				Developer.doEntry(file);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.core.kernel.KernelList.Orderable#getOrder()
	 */
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 1024;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.aop.AopMethodDefine#getAopInterceptor(com.absir.bean.basis.
	 * BeanDefine, java.lang.Object)
	 */
	@Override
	public LangIterceptor getAopInterceptor(BeanDefine beanDefine, Object beanObject) {
		// TODO Auto-generated method stub
		return new LangIterceptor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.aop.AopMethodDefine#isEmpty(com.absir.aop.AopInterceptor)
	 */
	@Override
	public boolean isEmpty(LangIterceptor aopInterceptor) {
		// TODO Auto-generated method stub
		return !aopInterceptor.setunmodifiableMethodMapInterceptor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.aop.AopMethodDefine#getVariable(com.absir.aop.AopInterceptor,
	 * com.absir.bean.basis.BeanDefine, java.lang.Object)
	 */
	@Override
	public Boolean getVariable(LangIterceptor aopInterceptor, BeanDefine beanDefine, Object beanObject) {
		// TODO Auto-generated method stub
		return beanDefine.getBeanType().isAssignableFrom(ILangBase.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.aop.AopMethodDefine#getAopInterceptor(java.lang.Object,
	 * java.lang.Class)
	 */
	@Override
	public Entry<String, Class<?>> getAopInterceptor(Boolean variable, Class<?> beanType) {
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
	public Entry<String, Class<?>> getAopInterceptor(Entry<String, Class<?>> interceptor, Boolean variable, Class<?> beanType, Method method) {
		// TODO Auto-generated method stub
		if (variable == Boolean.TRUE && isI18n() && method.getParameterTypes().length == 0) {
			String name = method.getName();
			int length = name.length();
			if (length > 3) {
				if (name.startsWith("get")) {
					return new ObjectEntry<String, Class<?>>(KernelString.unCapitalize(name.substring(3, length)), method.getReturnType());
				}
			}

			if (length > 2) {
				if (name.startsWith("is")) {
					return new ObjectEntry<String, Class<?>>(KernelString.unCapitalize(name.substring(2, length)), method.getReturnType());
				}
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.aop.AopMethodDefine#setAopInterceptor(java.lang.Object,
	 * com.absir.aop.AopInterceptor, java.lang.Class, java.lang.reflect.Method,
	 * java.lang.reflect.Method)
	 */
	@Override
	public void setAopInterceptor(Entry<String, Class<?>> interceptor, LangIterceptor aopInterceptor, Class<?> beanType, Method method, Method beanMethod) {
		// TODO Auto-generated method stub
		aopInterceptor.getMethodMapInterceptor().put(beanMethod, interceptor);
	}
}