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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.beetl.core.Configuration;
import org.beetl.core.Context;
import org.beetl.core.Format;
import org.beetl.core.Function;
import org.beetl.core.GroupTemplate;
import org.beetl.core.ResourceLoader;
import org.beetl.core.Tag;
import org.beetl.core.Template;
import org.beetl.core.misc.BeetlUtil;
import org.beetl.core.resource.FileResourceLoader;
import org.beetl.ext.web.WebVariableScope;

import com.absir.appserv.support.developer.Pag;
import com.absir.appserv.support.web.WebBeetlSupply.BeetlConfigureFound;
import com.absir.appserv.support.web.value.BaFormat;
import com.absir.appserv.support.web.value.BaFunction;
import com.absir.appserv.support.web.value.BaTag;
import com.absir.bean.basis.Basis;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanScope;
import com.absir.bean.basis.Environment;
import com.absir.bean.config.IBeanDefineSupply;
import com.absir.bean.core.BeanFactoryImpl;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.IMethodSupport;
import com.absir.bean.inject.InjectInvoker;
import com.absir.bean.inject.value.Bean;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.kernel.KernelLang.CauseRuntimeException;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 *
 */
@Basis
@Bean
public class WebBeetlSupply implements IBeanDefineSupply, IMethodSupport<BeetlConfigureFound>, IWebBeetl {

	/**
	 * @author absir
	 *
	 */
	protected static enum BeetlConfigure {

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
			public void process(String name, final Object object, Object found, final Method method, GroupTemplate groupTemplate) {
				// TODO Auto-generated method stub
				final Class<?>[] parameterTypes = method.getParameterTypes();
				groupTemplate.registerFunction(name, new Function() {

					@Override
					public Object call(Object[] paras, Context ctx) {
						// TODO Auto-generated method stub
						try {
							int length = paras.length;
							if (length == 0) {
								return method.invoke(object, ctx);
							}

							Object[] parameters = new Object[length + 1];
							parameters[0] = ctx;
							for (int i = 0; i < length; i++) {
								parameters[i + 1] = paras[i];
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

		FORMAT {
			@Override
			public Object find(Method method) {
				// TODO Auto-generated method stub
				return method.getAnnotation(BaFormat.class);
			}

			@Override
			public String findName(Object found) {
				// TODO Auto-generated method stub
				return ((BaFormat) found).name();
			}

			@Override
			public void process(String name, final Object object, Object found, final Method method, GroupTemplate groupTemplate) {
				// TODO Auto-generated method stub
				Class<?>[] parameterTypes = method.getParameterTypes();
				final Class<?> parameterType = parameterTypes[0];
				Format format = method.getParameterTypes().length == 1 ? new Format() {

					@Override
					public Object format(Object data, String pattern) {
						// TODO Auto-generated method stub
						try {
							return method.invoke(object, DynaBinder.to(data, parameterType));

						} catch (Throwable e) {
							// TODO Auto-generated catch block
							throw new CauseRuntimeException(e);
						}
					}

				} : new Format() {

					@Override
					public Object format(Object data, String pattern) {
						// TODO Auto-generated method stub
						try {
							return method.invoke(object, DynaBinder.to(data, parameterType), pattern);

						} catch (Throwable e) {
							// TODO Auto-generated catch block
							throw new CauseRuntimeException(e);
						}
					}
				};

				groupTemplate.registerFormat(name, format);
				if (((BaFormat) found).defaultion()) {
					groupTemplate.registerDefaultFormat(parameterType, format);
				}
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
		 * @param groupTemplate
		 */
		public abstract void process(String name, final Object object, Object found, final Method method, GroupTemplate groupTemplate);
	}

	/**
	 * @author absir
	 *
	 */
	protected static class BeetlConfigureFound {

		/** beetlConfigure */
		protected BeetlConfigure beetlConfigure;

		/** found */
		protected Object found;

		/**
		 * @param object
		 * @param method
		 * @param groupTemplate
		 */
		protected void process(Object object, Method method) {
			String name = beetlConfigure.findName(found);
			if (KernelString.isEmpty(name)) {
				name = method.getName();
			}

			beetlConfigure.process(name, object, name, method, WebBeetlView.ME.getGroupTemplate());
		}
	}

	/** beetlConfigures */
	private BeetlConfigure[] beetlConfigures = BeetlConfigure.values();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.inject.IMethodSupport#getInject(com.absir.bean.basis.BeanScope
	 * , com.absir.bean.basis.BeanDefine, java.lang.reflect.Method)
	 */
	@Override
	public BeetlConfigureFound getInject(BeanScope beanScope, BeanDefine beanDefine, Method method) {
		// TODO Auto-generated method stub
		for (BeetlConfigure beetlConfigure : beetlConfigures) {
			Object found = beetlConfigure.find(method);
			if (found != null) {
				BeetlConfigureFound beetlConfigureFound = new BeetlConfigureFound();
				beetlConfigureFound.beetlConfigure = beetlConfigure;
				beetlConfigureFound.found = found;
				return beetlConfigureFound;
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
	public InjectInvoker getInjectInvoker(BeetlConfigureFound inject, Method method, Object beanObject) {
		// TODO Auto-generated method stub
		inject.process(beanObject, method);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.core.kernel.KernelList.Orderable#getOrder()
	 */
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 33;
	}

	/** nameMapTag */
	private Map<String, Class<?>> nameMapTag = new HashMap<String, Class<?>>();

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
		if (Tag.class.isAssignableFrom(beanType)) {
			BaTag baTag = beanType.getAnnotation(BaTag.class);
			if (baTag != null) {
				String name = baTag.name();
				if (KernelString.isEmpty(name)) {
					name = beanType.getSimpleName();
					if (name.endsWith("Tag")) {
						name = name.substring(0, name.length() - 3);
					}

					name = KernelString.unCapitalize(name);
				}

				nameMapTag.put(name, beanType);
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.support.web.IBeetlProcessor#process(org.beetl.core.
	 * GroupTemplate)
	 */
	@Override
	public void process(GroupTemplate groupTemplate) {
		// TODO Auto-generated method stub
		ResourceLoader resourceLoader = groupTemplate.getResourceLoader();
		if (resourceLoader instanceof FileResourceLoader) {
			Boolean autoCheck = BeanFactoryUtils.getBeanConfig().getExpressionValue("web.beetl.autoCheck", null, Boolean.class);
			if (autoCheck != null) {
				((FileResourceLoader) resourceLoader).setAutoCheck(autoCheck);
			}
		}

		Configuration configuration = groupTemplate.getConf();
		String[] pkgs = BeanFactoryUtils.getBeanConfig().getExpressionValue("web.beetl.package", null, String[].class);
		if (pkgs != null) {
			for (String pkg : pkgs) {
				configuration.addPkg(pkg);
			}
		}

		if (nameMapTag != null) {
			for (Entry<String, Class<?>> entry : nameMapTag.entrySet()) {
				groupTemplate.registerTag(entry.getKey(), entry.getValue());
			}
		}
		
		groupTemplate.registerFunctionPackage("Pag", Pag.class);
	}

	/**
	 * 获取文件完整路径
	 * 
	 * @param ctx
	 * @param path
	 * @return
	 */
	public static String getRelPath(Context ctx, String path) {
		String resourceId = ctx.getResourceId();
		return BeetlUtil.getRelPath(resourceId, path);
	}

	/**
	 * 引入文件方法
	 * 
	 * @param ctx
	 * @param path
	 */
	@BaFunction
	public static void include(Context ctx, String path) {
		String resourceId = ctx.getResourceId();
		String relPath = BeetlUtil.getRelPath(resourceId, path);
		Template t = ctx.gt.getTemplate(relPath, resourceId);
		// 快速复制父模板的变量
		t.binding(ctx.globalVar);
		if (ctx.objectKeys != null && !ctx.objectKeys.isEmpty()) {
			t.dynamic(ctx.objectKeys);
		}

		t.renderTo(ctx.byteWriter);
	}

	/**
	 * 引入文件方法(忽略异常)
	 * 
	 * @param ctx
	 * @param path
	 */
	@BaFunction(name = "@include")
	public static void includeThrowable(Context ctx, String path) {
		try {
			include(ctx, path);

		} catch (Throwable e) {
			// TODO: handle exception
			if (BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= 0) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 输出变量
	 * 
	 * @param ctx
	 * @param path
	 */
	@BaFunction
	public static Object get(Context ctx, String name) {
		return ((WebVariableScope) ctx.getGlobal("servlet")).get(name);
	}
}
