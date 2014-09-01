/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月1日 下午12:14:05
 */
package com.absir.appserv.support.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import webit.script.Context;
import webit.script.Template;
import webit.script.exceptions.ResourceNotFoundException;
import webit.script.io.impl.OutputStreamOut;
import webit.script.loaders.Loader;
import webit.script.util.keyvalues.KeyValuesUtil;
import webit.script.web.WebEngineManager;

import com.absir.appserv.feature.menu.MenuContextUtils;
import com.absir.appserv.system.server.ServerDiyView;
import com.absir.bean.core.BeanConfigImpl;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.InjectType;
import com.absir.bean.inject.value.Value;
import com.absir.server.in.InModel;
import com.absir.servlet.InDispathFilter;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 *
 */
@Bean
public class WebItView extends ServerDiyView {

	/** engineManager */
	@Inject(type = InjectType.Selectable)
	protected static WebEngineManager engineManager;

	/**
	 * @return the engineManager
	 */
	public static WebEngineManager getEngineManager() {
		return engineManager;
	}

	/** resourceLoader */
	private static Loader resourceLoader;

	/** resourceLoaderRoot */
	private static String resourceLoaderRoot;

	/**
	 * @return
	 */
	public static String getResourceLoaderRoot() {
		if (resourceLoader != engineManager.getEngine().getResourceLoader()) {
			resourceLoader = engineManager.getEngine().getResourceLoader();
			try {
				resourceLoaderRoot = resourceLoader.get("").toString();

			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resourceLoaderRoot;
	}

	/**
	 * 获取文件完整路径
	 * 
	 * @param ctx
	 * @param path
	 * @return
	 */
	public static String getFullPath(Context context, String path) {
		Template template = context.template;
		return template.engine.getResourceLoader().concat(template.name, path);
	}

	/**
	 * 获取文件真实路径
	 * 
	 * @param context
	 * @param path
	 * @return
	 */
	public static String getRealPath(Context context, String path) {
		Template template = context.template;
		String name = path == null ? template.name : getFullPath(context, path);
		return getResourceLoaderRoot() + name;
	}

	/** prefix */
	@Value("web.view.prefix")
	private String prefix = "/WEB-INF/tpl/";

	/**
	 * 
	 */
	@Inject
	private void initView() {
		if (engineManager == null) {
			engineManager = new WebEngineManager(InDispathFilter.getServletContext());
		}

		Map<String, Object> engineManagerProperties = new LinkedHashMap<String, Object>();
		engineManagerProperties.put("engine.globalManager", "com.absir.appserv.support.web.WebItGlobalManager");
		BeanConfigImpl.readProperties(null, engineManagerProperties, new File(BeanFactoryUtils.getBeanConfig().getClassPath("webit-script-manager.props")), null);
		engineManager.setProperties(engineManagerProperties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IRender#echo(java.lang.String)
	 */
	@Override
	public String echo(String value) {
		// TODO Auto-generated method stub
		return "${" + value + '}';
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.support.developer.IRender#include(java.lang.String)
	 */
	@Override
	public String include(String path) {
		// TODO Auto-generated method stub
		return "import \"path\";";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.support.developer.IRender#include(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public void include(String path, Object... renders) throws IOException {
		// TODO Auto-generated method stub
		Context context = (Context) renders[0];
		Template template = context.template;
		template.engine.getTemplate(template.name, path).mergeForInlude(context, KeyValuesUtil.EMPTY_KEY_VALUES);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.support.developer.IRender#getPath(java.lang.Object[])
	 */
	@Override
	public String getPath(Object... renders) throws IOException {
		// TODO Auto-generated method stub
		return ((Context) renders[0]).template.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.support.developer.IRender#getFullPath(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public String getFullPath(String path, Object... renders) throws IOException {
		// TODO Auto-generated method stub
		return getFullPath((Context) renders[0], path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.support.developer.IRender#getRealPath(java.lang.String,
	 * java.lang.Object[])
	 */
	@Override
	public String getRealPath(String path, Object... renders) throws IOException {
		// TODO Auto-generated method stub
		return getRealPath((Context) renders[0], path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.support.developer.IRender#rend(java.io.OutputStream,
	 * java.lang.String, java.lang.Object[])
	 */
	@Override
	public void rend(OutputStream outputStream, String path, Object... renders) throws IOException {
		// TODO Auto-generated method stub
		Context context = (Context) renders[0];
		Template template = engineManager.getEngine().getTemplate(path);
		context = new Context(context, template, KeyValuesUtil.EMPTY_KEY_VALUES);
		template.mergeForInlude(context, KeyValuesUtil.EMPTY_KEY_VALUES);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.server.ServerDiyView#getView(java.lang.String)
	 */
	@Override
	protected String getView(String view) {
		// TODO Auto-generated method stub
		return prefix + view + engineManager.getEngine().getSuffix();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.server.ServerDiyView#getRender(java.lang.String,
	 * com.absir.servlet.InputRequest)
	 */
	@Override
	protected Object getRender(String view, InputRequest input) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.server.ServerDiyView#getRenders(java.lang.Object
	 * , com.absir.servlet.InputRequest)
	 */
	@Override
	protected Object[] getRenders(Object render, InputRequest input) {
		// TODO Auto-generated method stub
		Context context = new Context(null, new OutputStreamOut(null, engineManager.getEngine()), KeyValuesUtil.wrap(input.getModel()));
		return new Object[] { context, input.getRequest() };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.server.ServerDiyView#renderView(java.lang.String
	 * , java.lang.Object[], com.absir.servlet.InputRequest)
	 */
	@Override
	protected void renderView(String view, Object[] renders, InputRequest input) {
		// TODO Auto-generated method stub
		try {
			InModel inModel = input.getModel();
			inModel.put("APP_NAME", MenuContextUtils.getAppName());
			inModel.put("SITE_ROUTE", MenuContextUtils.getSiteRoute());
			inModel.put("ADMIN_ROUTE", MenuContextUtils.getAdminRoute());
			inModel.put("request", input.getRequest());
			engineManager.renderTemplate(view, KeyValuesUtil.wrap(input.getModel()), input.getResponse());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
