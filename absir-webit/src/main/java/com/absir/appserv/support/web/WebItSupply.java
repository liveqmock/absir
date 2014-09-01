/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月24日 下午1:02:10
 */
package com.absir.appserv.support.web;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import webit.script.Context;
import webit.script.global.GlobalManager;
import webit.script.global.GlobalRegister;
import webit.script.method.MethodDeclare;

import com.absir.appserv.support.web.WebItSupply.ConfigureFound;
import com.absir.appserv.support.web.value.BaFunction;
import com.absir.bean.basis.Basis;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanScope;
import com.absir.bean.inject.IMethodSupport;
import com.absir.bean.inject.InjectInvoker;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.InjectType;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.kernel.KernelLang.CauseRuntimeException;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 *
 */
@Basis
@Bean
public class WebItSupply implements IMethodSupport<ConfigureFound>, GlobalRegister {

	/** globalBags */
	private Map<String, Object> globalBags = new HashMap<String, Object>();

	/**
	 * @author absir
	 *
	 */
	protected static enum Configure {

		FUNCTION {
			@Override
			public Object find(Method method) {
				// TODO Auto-generated method stub
				return method.getAnnotation(BaFunction.class);
			}

			@Override
			public String findName(Object found) {
				// TODO Auto-generated method stub
				return ((BaFunction) found).name();
			}

			@Override
			public void process(String name, final Object object, Object found, final Method method, WebItSupply webItSupply) {
				// TODO Auto-generated method stub
				final Class<?>[] parameterTypes = method.getParameterTypes();
				webItSupply.globalBags.put(name, new MethodDeclare() {

					@Override
					public Object invoke(Context context, Object[] args) {
						// TODO Auto-generated method stub
						try {
							int length = args.length;
							if (length == 0) {
								return method.invoke(object, context);
							}

							Object[] parameters = new Object[length + 1];
							parameters[0] = context;
							for (int i = 0; i < length; i++) {
								parameters[i + 1] = args[i];
							}

							DynaBinder.to(parameters, parameterTypes);
							return method.invoke(object, parameters);

						} catch (Throwable e) {
							// TODO Auto-generated catch block
							throw new CauseRuntimeException(e);
						}
					}
				});
			}
		},

		;

		/**
		 * @param method
		 * @return
		 */
		public abstract Object find(Method method);

		/**
		 * @param found
		 * @return
		 */
		public abstract String findName(Object found);

		/**
		 * @param name
		 * @param foundMethod
		 * @param webItSupply
		 */
		public abstract void process(String name, final Object object, Object found, final Method method, WebItSupply webItSupply);
	}

	/**
	 * @author absir
	 *
	 */
	protected class ConfigureFound {

		/** beetlConfigure */
		protected Configure configure;

		/** found */
		protected Object found;

		/**
		 * @param object
		 * @param method
		 * @param groupTemplate
		 */
		protected void process(Object object, Method method) {
			String name = configure.findName(found);
			if (KernelString.isEmpty(name)) {
				name = method.getName();
			}

			configure.process(name, object, name, method, WebItSupply.this);
		}
	}

	/**
	 * @param globalRegisters
	 */
	@Inject(type = InjectType.Selectable)
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
		for (Entry<String, Object> entry : globalBags.entrySet()) {
			manager.getGlobalBag().set(entry.getKey(), entry.getValue());
		}
	}

	/** configures */
	private Configure[] configures = Configure.values();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.inject.IMethodSupport#getInject(com.absir.bean.basis.BeanScope
	 * , com.absir.bean.basis.BeanDefine, java.lang.reflect.Method)
	 */
	@Override
	public ConfigureFound getInject(BeanScope beanScope, BeanDefine beanDefine, Method method) {
		// TODO Auto-generated method stub
		for (Configure configure : configures) {
			Object found = configure.find(method);
			if (found != null) {
				ConfigureFound configureFound = new ConfigureFound();
				configureFound.configure = configure;
				configureFound.found = found;
				return configureFound;
			}
		}

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
	public InjectInvoker getInjectInvoker(ConfigureFound inject, Method method, Object beanObject) {
		// TODO Auto-generated method stub
		inject.process(beanObject, method);
		return null;
	}
}
