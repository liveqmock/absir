/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月12日 上午9:56:42
 */
package com.absir.appserv.lang;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.cglib.proxy.MethodProxy;

import com.absir.aop.AopInterceptor;
import com.absir.aop.AopProxy;
import com.absir.aop.AopProxyHandler;
import com.absir.aop.AopProxyUtils;
import com.absir.appserv.crud.value.ICrudBean;
import com.absir.appserv.lang.value.Langs;
import com.absir.bean.inject.IMethodEntry;
import com.absir.bean.inject.InjectBeanFactory;
import com.absir.context.lang.LangBundle;
import com.absir.core.kernel.KernelLang;
import com.absir.core.kernel.KernelLang.ObjectEntry;
import com.absir.core.kernel.KernelString;
import com.absir.server.on.OnPut;

/**
 * @author absir
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LangUtils extends LangBundle implements IMethodEntry<Entry<String, Class<?>>> {

	/** langInterfaces */
	private Set<Class<?>> langInterfaces = new HashSet<Class<?>>();

	/** methodMapLangEntryImpl */
	private Map<Method, LangEntryImpl> methodMapLangEntryImpl = new HashMap<Method, LangUtils.LangEntryImpl>();

	/** entityMapLangInterceptors */
	private Map<String, Map<Method, Entry<String, Class<?>>>> entityMapLangInterceptors;

	/** entityClassMapLangInterceptors */
	private Map<Class<?>, Map<Method, Entry<String, Class<?>>>> entityClassMapLangInterceptors;

	/**
	 * @author absir
	 *
	 */
	protected static abstract class LangEntry extends ObjectEntry<String, Class<?>> {

		/**
		 * @param value
		 */
		public LangEntry() {
			super(null, null);
		}

		/**
		 * @param entity
		 * @param method
		 * @param args
		 * @return
		 */
		public abstract Object invoke(Object entity, Method method, Object[] args);
	}

	/**
	 * @author absir
	 *
	 */
	protected static abstract class LangEntryImpl extends LangEntry {

		/**
		 * @return
		 */
		public abstract LangEntry getLangEnry();
	}

	/**
	 * @author absir
	 *
	 */
	protected class LangIterceptor implements AopInterceptor<Entry<String, Class<?>>> {

		/** langInterceptors */
		private Map<Method, Entry<String, Class<?>>> langInterceptors;

		/**
		 * @param langInterceptors
		 */
		public LangIterceptor(Map<Method, Entry<String, Class<?>>> langInterceptors) {
			this.langInterceptors = langInterceptors;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.aop.AopInterceptor#getInterface()
		 */
		@Override
		public Class<?> getInterface() {
			// TODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.aop.AopInterceptor#getInterceptor(com.absir.aop.AopProxyHandler
		 * , java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
		 */
		@Override
		public Entry<String, Class<?>> getInterceptor(AopProxyHandler proxyHandler, Object beanObject, Method method, Object[] args) throws Throwable {
			// TODO Auto-generated method stub
			return langInterceptors.get(method);
		}

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
			OnPut onPut = OnPut.get();
			if (onPut != null) {
				Locale locale = onPut.getInput().getLocale();
				if (locale != null && locale != ME.getLocale()) {
					return ((ILangBase) proxy).getLang(interceptor.getKey(), locale, interceptor.getValue());
				}
			}

			return AopProxyHandler.VOID;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.aop.AopInterceptor#after(java.lang.Object,
		 * java.lang.Object, java.lang.Object, com.absir.aop.AopProxyHandler,
		 * java.lang.reflect.Method, java.lang.Object[], java.lang.Throwable)
		 */
		@Override
		public Object after(Object proxy, Object returnValue, Entry<String, Class<?>> interceptor, AopProxyHandler proxyHandler, Method method, Object[] args, Throwable e) throws Throwable {
			// TODO Auto-generated method stub
			return returnValue;
		}
	}

	public LangUtils() {
		langInterfaces.add(ILangBase.class);
		langInterfaces.add(ICrudBean.class);
		for (Method method : ILangBase.class.getMethods()) {
			methodMapLangEntryImpl.put(method, new LangEntryImpl() {

				@Override
				public Object invoke(Object entity, Method method, Object[] args) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public LangEntry getLangEnry() {
					// TODO Auto-generated method stub
					return null;
				}
			});
		}

		for (Method method : ICrudBean.class.getMethods()) {

		}
	}

	/**
	 * @param entityName
	 * @param entity
	 * @return
	 */
	public <T> T getLangProxy(String entityName, T entity) {
		if (LangBundle.ME.isI18n()) {
			Map<Method, Entry<String, Class<?>>> langInterceptors = entityMapLangInterceptors.get(entityName);
			if (langInterceptors == null) {
				Class<?> entityClass = entity.getClass();
				langInterceptors = new HashMap<Method, Map.Entry<String, Class<?>>>();
				entityClassMapLangInterceptors.put(entityClass, langInterceptors);
				InjectBeanFactory.getInstance().getMethodEntries(entityClass, this);
				if (langInterceptors.isEmpty()) {
					langInterceptors = (Map<Method, Entry<String, Class<?>>>) (Object) KernelLang.NULL_MAP;
				}

				entityMapLangInterceptors.put(entityName, langInterceptors);
				entityClassMapLangInterceptors.remove(entityClass);
			}

			if ((Object) langInterceptors == KernelLang.NULL_MAP) {
				return entity;
			}

			AopProxy proxy = AopProxyUtils.getProxy(entity, langInterfaces, false, true);
			proxy.getAopInterceptors().add(new LangIterceptor(langInterceptors));
			return (T) proxy;
		}

		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.inject.IMethodEntry#getMethod(java.lang.Class,
	 * java.lang.reflect.Method)
	 */
	@Override
	public Entry<String, Class<?>> getMethod(Class<?> beanType, Method method) {
		// TODO Auto-generated method stub
		if (method.getParameterTypes().length == 0 && method.getName().startsWith("get") && method.getAnnotation(Langs.class) != null) {
			return new ObjectEntry<String, Class<?>>(KernelString.unCapitalize(method.getName().substring(3)), method.getReturnType());
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.inject.IMethodEntry#setMethodEntry(java.lang.Object,
	 * java.lang.Class, java.lang.reflect.Method, java.lang.reflect.Method)
	 */
	@Override
	public void setMethodEntry(Entry<String, Class<?>> define, Class<?> beanType, Method beanMethod, Method method) {
		// TODO Auto-generated method stub
		entityClassMapLangInterceptors.get(beanType).put(beanMethod, define);
	}
}
