/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月1日 下午12:14:05
 */
package com.absir.appserv.support.web;

import java.io.IOException;
import java.io.OutputStream;

import jetbrick.template.JetContext;
import jetbrick.template.JetTemplate;
import jetbrick.template.runtime.JetPageContext;
import jetbrick.template.web.JetWebContext;

import com.absir.appserv.feature.menu.MenuContextUtils;
import com.absir.appserv.system.server.ServerDiyView;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Value;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 *
 */
@Bean
public class WebJetbrickView extends ServerDiyView {

	/**
	 * 获取文件完整路径
	 * 
	 * @param ctx
	 * @param path
	 * @return
	 */
	public static String getFullPath(JetPageContext context, String path) {
		return context.getAbsolutionName(path);
	}

	/** prefix */
	@Value("web.view.prefix")
	private String prefix = "/WEB-INF/tpl/";

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
		return "#include(\"" + path + "\")\r\n";
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
		JetPageContext context = (JetPageContext) renders[0];
		JetTemplate template = context.getEngine().getTemplate(path);
		template.render(context.getContext(), context.getWriter());
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
		JetPageContext context = (JetPageContext) renders[0];
		return context.getTemplate().getName();
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
		return getFullPath((JetPageContext) renders[0], path);
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
		return WebJetbrickSupply.getResourceLoaderRoot() + path;
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
		JetPageContext context = (JetPageContext) renders[0];
		context.getEngine().getTemplate(path).render(context.getContext(), outputStream);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IRenderSuffix#getSuffix()
	 */
	@Override
	public String getSuffix() {
		// TODO Auto-generated method stub
		return "";
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
		return prefix + view + getSuffix();
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
		JetPageContext context = new WebJetbrickContext(new JetWebContext(input.getRequest(), input.getResponse(), input.getModel()), WebJetbrickSupply.getEngine());
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
	protected void renderView(String view, Object[] renders, InputRequest input) throws Exception {
		// TODO Auto-generated method stub
		JetPageContext context = (JetPageContext) renders[0];
		JetContext jetContext = context.getContext();
		jetContext.put("APP_NAME", MenuContextUtils.getAppName());
		jetContext.put("SITE_ROUTE", MenuContextUtils.getSiteRoute());
		jetContext.put("ADMIN_ROUTE", MenuContextUtils.getAdminRoute());
		context.getEngine().getTemplate(view).render(jetContext, input.getResponse().getOutputStream());
	}
}
