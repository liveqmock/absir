/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月24日 下午1:02:10
 */
package com.absir.appserv.support.web;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import jetbrick.template.JetContext;
import jetbrick.template.JetEngine;
import jetbrick.template.JetGlobalVariables;
import jetbrick.template.JetTemplate;
import jetbrick.template.VariableResolverBean;
import jetbrick.template.resource.Resource;
import jetbrick.template.runtime.JetPageContext;
import jetbrick.template.runtime.JetTagContext;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngineLoader;

import com.absir.appserv.developer.Pag;
import com.absir.appserv.developer.Scenario;
import com.absir.appserv.feature.menu.MenuContextUtils;
import com.absir.appserv.support.web.WebJetbrickSupply.ConfigureFound;
import com.absir.appserv.support.web.value.BaFunction;
import com.absir.appserv.support.web.value.BaMethod;
import com.absir.appserv.support.web.value.BaTag;
import com.absir.appserv.system.bean.JEmbedLL;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.helper.HelperString;
import com.absir.bean.basis.Basis;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanScope;
import com.absir.bean.core.BeanConfigImpl;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.IMethodSupport;
import com.absir.bean.inject.InjectInvoker;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Value;
import com.absir.core.helper.HelperFileName;
import com.absir.core.kernel.KernelObject;
import com.absir.core.kernel.KernelString;
import com.absir.core.util.UtilAccessor;
import com.absir.core.util.UtilAccessor.Accessor;
import com.absir.servlet.InDispathFilter;

/**
 * @author absir
 *
 */
@SuppressWarnings("unchecked")
@Basis
@Bean
public class WebJetbrickSupply implements IMethodSupport<ConfigureFound> {

	/** engine */
	private static JetEngine engine;

	/** variableResolverBean */
	private static VariableResolverBean variableResolverBean;

	/** resourceLoaderRoot */
	private static String resourceLoaderRoot;

	/**
	 * @return
	 */
	/**
	 * @return
	 */
	public static JetEngine getEngine() {
		if (engine == null) {
			ServletContext servletContext = InDispathFilter.getServletContext();
			if (servletContext == null) {
				engine = JetEngine.create();

			} else {
				JetWebEngineLoader.setServletContext(servletContext);
				engine = JetWebEngineLoader.getJetEngine();
			}

			Properties configProperties = new Properties();
			BeanConfigImpl.readProperties(null, (Map<String, Object>) (Object) configProperties, new File(BeanFactoryUtils.getBeanConfig().getClassPath("jetbrick.properties")), null);
			engine.getConfig().load(configProperties);
			VariableResolverBean.load(engine, engine.getConfig());
			JetGlobalVariables globalVariables = engine.getGlobalVariables();
			if (globalVariables == null) {
				KernelObject.declaredSet(engine, "globalVariables", new WebGlobalVariables() {

					/** map */
					private Map<String, Object> variables = new HashMap<String, Object>();

					@Override
					public Object get(JetContext context, String name) {
						// TODO Auto-generated method stub
						return variables.get(name);
					}

					@Override
					public void register(String name, Object variable) {
						// TODO Auto-generated method stub
						variables.put(name, variable);
					}
				});

				globalVariables = engine.getGlobalVariables();
			}

			if (globalVariables != null && globalVariables instanceof WebGlobalVariables) {
				WebGlobalVariables webGlobalVariables = (WebGlobalVariables) globalVariables;
				webGlobalVariables.register("APP_NAME", MenuContextUtils.getAppName());
				webGlobalVariables.register("SITE_ROUTE", MenuContextUtils.getSiteRoute());
				webGlobalVariables.register("ADMIN_ROUTE", MenuContextUtils.getAdminRoute());
			}

			// getVariableResolverBean().getVariableResolver().setDynamicResolver(new
			// DynamicResolver() {
			//
			// @Override
			// public String getDynamicMember(Class<?> beanClass, String name,
			// boolean isSafeCall, SegmentCode code) {
			// // TODO Auto-generated method stub
			// return "com.absir.appserv.support.web.WebJetbrickSupply.getter("
			// + code.toString() + ", \"" + name + "\")";
			// }
			// });

			getVariableResolverBean().getVariableResolver().addImportClass(KernelObject.class.getName());
			getVariableResolverBean().getVariableResolver().addImportPackage(JiUserBase.class.getPackage().getName());
			getVariableResolverBean().getVariableResolver().addImportPackage(JEmbedLL.class.getPackage().getName());
			getVariableResolverBean().getVariableResolver().addImportPackage(Pag.class.getPackage().getName());
		}

		return engine;
	}

	/**
	 * @return
	 */
	public static VariableResolverBean getVariableResolverBean() {
		if (variableResolverBean == null) {
			variableResolverBean = new VariableResolverBean(getEngine());
		}

		return variableResolverBean;
	}

	/**
	 * @return
	 */
	public static String getResourceLoaderRoot() {
		if (resourceLoaderRoot == null) {
			try {
				Resource resource = getEngine().getResource("/");
				Object file = KernelObject.declaredGet(resource, "file");
				if (file != null && file instanceof File) {
					resourceLoaderRoot = ((File) file).getPath();
				}

				if (resourceLoaderRoot == null) {
					resourceLoaderRoot = resource.getAbsolutePath();
				}

				resourceLoaderRoot = HelperFileName.getFullPathNoEndSeparator(resourceLoaderRoot + "/");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			public void process(String name, final Object object, Object found, final Method method, WebJetbrickSupply webItSupply) {
				// TODO Auto-generated method stub
				getVariableResolverBean().registerMethod(name, method);
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
			public void process(String name, final Object object, Object found, final Method method, WebJetbrickSupply webItSupply) {
				// TODO Auto-generated method stub
				getVariableResolverBean().registerFunction(name, method);
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
			public void process(String name, final Object object, Object found, final Method method, WebJetbrickSupply webItSupply) {
				// TODO Auto-generated method stub
				getVariableResolverBean().registerTag(name, method);
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
		public abstract void process(String name, final Object object, Object found, final Method method, WebJetbrickSupply webItSupply);
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

			configure.process(name, object, name, method, WebJetbrickSupply.this);
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
	 * java.lang.reflect.Method, java.lang.reflect.Method, java.lang.Object)
	 */
	@Override
	public InjectInvoker getInjectInvoker(ConfigureFound inject, Method method, Method beanMethod, Object beanObject) {
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
		private JetTagContext context;

		/** bodyContent */
		private String bodyContent;

		/**
		 * @param context
		 */
		public TagWrapper(JetTagContext tagContext) {
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
				bodyContent = context.getBodyContent();
			}

			return bodyContent;
		}
	}

	/**
	 * @param obj
	 * @param name
	 * @return
	 */
	public Object getter(Object obj, String name) {
		Accessor accessor = UtilAccessor.getAccessorObj(obj, name);
		return accessor == null ? null : accessor.get(obj);
	}

	/** highlightSpan */
	@Value(value = "<span ${web.view.highlight}>")
	private static String highlightSpan = "<span class=\"highlight\">";

	/**
	 * @param obj
	 * @param highlight
	 * @return
	 */
	@BaMethod
	public static Object highlight(Object obj, String highlight) {
		if (obj != null) {
			return HelperString.replace(obj.toString(), highlight, "<span>" + highlight + "</span>");
		}

		return obj;
	}

	/**
	 * @param ctx
	 * @param include
	 * @return
	 * @throws IOException
	 */
	@BaFunction
	public static String _include(JetPageContext ctx, String include) throws IOException {
		return _include(ctx, include, include);
	}

	/**
	 * @param ctx
	 * @param include
	 * @param generate
	 * @return
	 * @throws IOException
	 */
	@BaFunction
	public static String _include(JetPageContext ctx, String include, String generate) throws IOException {
		return Pag.getInclude(include, generate, ctx, ctx.getContext().get(JetWebContext.REQUEST));
	}

	/**
	 * @param ctx
	 * @param name
	 */
	@BaTag
	public static void _name(JetTagContext ctx, String name) {
		Object request = ctx.getContext().get(JetWebContext.REQUEST);
		if (request != null && request instanceof ServletRequest) {
			if (Scenario.requestName((ServletRequest) request, name)) {
				ctx.writeBodyContent();
			}
		}
	}

	/**
	 * @param ctx
	 * @param file
	 */
	@BaTag
	public static void _layout(JetTagContext ctx, String file) {
		_layout(ctx, file, null);
	}

	/**
	 * @param ctx
	 * @param file
	 * @param parameters
	 */
	@BaTag
	public static void _layout(JetTagContext ctx, String file, Map<String, Object> parameters) {
		JetContext context;
		if (parameters == null || parameters.size() == 0) {
			context = ctx.getContext();

		} else {
			context = new JetContext(ctx.getContext(), parameters);
		}

		ctx.getContext().put("bodyContent", new TagWrapper(ctx));
		file = ctx.getPageContext().getAbsolutionName(file);
		JetTemplate template = ctx.getEngine().getTemplate(file);
		template.render(context, ctx.getWriter());
	}
}
