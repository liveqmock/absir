/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月24日 下午1:02:10
 */
package com.absir.appserv.support.web;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.parser.grammer.TinyTemplateParser.TemplateContext;

import com.absir.appserv.feature.menu.MenuContextUtils;
import com.absir.appserv.support.web.WebTinySupply.ConfigureFound;
import com.absir.appserv.support.web.value.BaFunction;
import com.absir.appserv.support.web.value.BaMethod;
import com.absir.appserv.support.web.value.BaTag;
import com.absir.bean.basis.Basis;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanScope;
import com.absir.bean.core.BeanConfigImpl;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.IMethodSupport;
import com.absir.bean.inject.InjectInvoker;
import com.absir.bean.inject.value.Bean;
import com.absir.core.kernel.KernelString;
import com.absir.servlet.InDispathFilter;

/**
 * @author absir
 *
 */
@SuppressWarnings("unchecked")
@Basis
@Bean
public class WebTinySupply implements IMethodSupport<ConfigureFound> {

	/** engine */
	private static TemplateEngine engine;

	/** resourceLoaderRoot */
	private static String resourceLoaderRoot;

	/**
	 * @return
	 */
	/**
	 * @return
	 */
	public static TemplateEngine getEngine() {
		if (engine == null) {
			engine = new TemplateEngineDefault();
			ServletContext servletContext = InDispathFilter.getServletContext();
			if (servletContext != null) {
				engine.put("servlet", servletContext);
			}

			engine.put("APP_NAME", MenuContextUtils.getAppName());
			engine.put("SITE_ROUTE", MenuContextUtils.getSiteRoute());
			engine.put("ADMIN_ROUTE", MenuContextUtils.getAdminRoute());
			Properties configProperties = new Properties();
			BeanConfigImpl.readProperties(null, (Map<String, Object>) (Object) configProperties, new File(BeanFactoryUtils.getBeanConfig().getClassPath("jetbrick.properties")), null);

		}

		return engine;
	}

	/**
	 * @return
	 */
	public static String getResourceLoaderRoot() {
		if (resourceLoaderRoot == null) {
		}

		return resourceLoaderRoot;
	}

	/**
	 * @author absir
	 *
	 */
	protected static enum Configure {

		METHOD {
			@Override
			public Object find(Method method) {
				// TODO Auto-generated method stub
				return method.getAnnotation(BaMethod.class);
			}

			@Override
			public String findName(Object found) {
				// TODO Auto-generated method stub
				return ((BaMethod) found).name();
			}

			@Override
			public void process(String name, final Object object, Object found, final Method method, WebTinySupply webItSupply) {
				// TODO Auto-generated method stub
				// getVariableResolverBean().registerMethod(name, method);
			}
		},

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
			public void process(String name, final Object object, Object found, final Method method, WebTinySupply webItSupply) {
				// TODO Auto-generated method stub
				// getVariableResolverBean().registerFunction(name, method);
			}
		},

		TAG {
			@Override
			public Object find(Method method) {
				// TODO Auto-generated method stub
				return method.getAnnotation(BaTag.class);
			}

			@Override
			public String findName(Object found) {
				// TODO Auto-generated method stub
				return ((BaTag) found).name();
			}

			@Override
			public void process(String name, final Object object, Object found, final Method method, WebTinySupply webItSupply) {
				// TODO Auto-generated method stub
				// getVariableResolverBean().registerTag(name, method);
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
		public abstract void process(String name, final Object object, Object found, final Method method, WebTinySupply webItSupply);
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

			configure.process(name, object, name, method, WebTinySupply.this);
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

	/**
	 * @author absir
	 *
	 */
	public static class TagWrapper {

		/** context */
		private TemplateContext context;

		/** bodyContent */
		private String bodyContent;

		/**
		 * @param context
		 */
		public TagWrapper(TemplateContext tagContext) {
			context = tagContext;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			if (bodyContent == null) {
				bodyContent = context.getText();
			}

			return bodyContent;
		}
	}
}
