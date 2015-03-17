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

import jetbrick.template.JetTemplate;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.web.JetWebContext;
import jetbrick.util.PathUtils;

import com.absir.appserv.developer.Pag.IPagLang;
import com.absir.appserv.system.server.ServerDiyView;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Value;
import com.absir.context.core.ContextUtils;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 *
 */
@Bean
public class WebJetbrickView extends ServerDiyView implements IPagLang {

	/**
	 * 获取文件完整路径
	 * 
	 * @param ctx
	 * @param path
	 * @return
	 */
	public static String getFullPath(JetTemplate template, String path) {
		return PathUtils.getRelativePath(template.getName(), path);
	}

	/** prefix */
	@Value("web.view.prefix")
	private String prefix = "/WEB-INF/tpl/";

	/** suffix */
	@Value("web.view.suffix")
	private String suffix = ".html";

	/**
	 * @return
	 */
	protected String diyExpression() {
		return "#";
	}

	/**
	 * @return
	 */
	protected String diyInclude() {
		return echo("Pag::getInclude(\",\")");
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
		InterpretContext context = InterpretContext.current();
		context.doIncludeCall(path, null, null);
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
		InterpretContext context = InterpretContext.current();
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
		return getFullPath(InterpretContext.current().getTemplate(), path);
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
		JetWebContext context = (JetWebContext) renders[0];
		WebJetbrickSupply.getEngine().getTemplate(path).render(context, outputStream);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IRenderSuffix#getSuffix()
	 */
	@Override
	public String getSuffix() {
		// TODO Auto-generated method stub
		return suffix;
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

	/**
	 * @param input
	 * @return
	 */
	protected JetWebContext createWebContext(InputRequest input) {
		JetWebContext context = new JetWebContext(input.getRequest(), input.getResponse(), input.getModel());
		context.put("contextTime", ContextUtils.getContextTime());
		return context;
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
		return new Object[] { createWebContext(input), input.getRequest() };
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
		JetWebContext context = renders == null ? createWebContext(input) : (JetWebContext) renders[0];
		WebJetbrickSupply.getEngine().getTemplate(view).render(context, input.getResponse().getOutputStream());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.developer.Pag.IPagLang#getPagLang(java.lang.String)
	 */
	@Override
	public String getPagLang(String transferredName) {
		// TODO Auto-generated method stub
		return "Pag::lang(" + transferredName + ")";
	}
}
