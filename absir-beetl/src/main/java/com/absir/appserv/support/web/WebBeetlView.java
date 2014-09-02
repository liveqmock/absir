/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月21日 下午12:49:15
 */
package com.absir.appserv.support.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.beetl.core.Configuration;
import org.beetl.core.Context;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.exception.BeetlException;
import org.beetl.core.resource.FileResourceLoader;
import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.web.WebVariableScope;

import com.absir.appserv.feature.menu.MenuContextUtils;
import com.absir.appserv.support.developer.IRenderSuffix;
import com.absir.appserv.system.server.ServerDiyView;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.Value;
import com.absir.context.core.ContextUtils;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 *
 */
@Base(order = -1)
@Bean
public class WebBeetlView extends ServerDiyView implements IRenderSuffix {

	/** ME */
	public static final WebBeetlView ME = BeanFactoryUtils.get(WebBeetlView.class);

	@Value("web.view.prefix")
	private String prefix = "/WEB-INF/tpl/";

	@Value("web.view.suffix")
	private String suffix = ".html";

	/** groupTemplate */
	protected GroupTemplate groupTemplate;

	/** fileResourceLoader */
	protected FileResourceLoader fileResourceLoader;

	/**
	 * @return the groupTemplate
	 */
	public GroupTemplate getGroupTemplate() {
		return groupTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.server.ServerDiyView#diyExpression()
	 */
	@Override
	protected String diyExpression() {
		return groupTemplate.getConf().getStatementStart() + " , " + groupTemplate.getConf().getStatementEnd();
	}

	/**
	 * @throws IOException
	 * 
	 */
	@Inject
	protected void initView() throws IOException {
		if (groupTemplate == null) {
			groupTemplate = BeanFactoryUtils.get(GroupTemplate.class);
		}

		if (groupTemplate == null) {
			groupTemplate = new GroupTemplate(new WebAppResourceLoader(), Configuration.defaultConfiguration());
		}

		for (IWebBeetl webBeetl : BeanFactoryUtils.getOrderBeanObjects(IWebBeetl.class)) {
			webBeetl.process(groupTemplate);
		}

		if (groupTemplate.getResourceLoader() instanceof FileResourceLoader) {
			fileResourceLoader = (FileResourceLoader) groupTemplate.getResourceLoader();
		}
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
		return prefix + view + suffix;
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
		Context ctx = new Context();
		ctx.gt = groupTemplate;
		ctx.set("input", input);
		for (Entry<String, Object> entry : input.getModel().entrySet()) {
			ctx.set(entry.getKey(), entry.getValue());
		}

		return new Object[] { ctx, input.getRequest() };
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
		Template template = new WebVariableScope().ready(groupTemplate, view, input.getRequest());
		template.binding("APP_NAME", MenuContextUtils.getAppName());
		template.binding("SITE_ROUTE", MenuContextUtils.getSiteRoute());
		template.binding("ADMIN_ROUTE", MenuContextUtils.getAdminRoute());
		for (Entry<String, Object> entry : input.getModel().entrySet()) {
			template.binding(entry.getKey(), entry.getValue());
		}

		renderView(template, input.getResponse());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IRender#echo(java.lang.String)
	 */
	@Override
	public String echo(String value) {
		// TODO Auto-generated method stub
		return groupTemplate.getConf().getPlaceholderStart() + value + groupTemplate.getConf().getPlaceholderEnd();
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
		return groupTemplate.getConf().getStatementStart() + " include(\"" + path + "\"); " + groupTemplate.getConf().getStatementEnd();
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
		WebBeetlSupply.include((Context) renders[0], path);
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
		return ((Context) renders[0]).getResourceId();
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
		return WebBeetlSupply.getRelPath((Context) renders[0], path);
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
		return fileResourceLoader == null ? path : (fileResourceLoader.getRoot() + path);
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
		Context ctx = (Context) renders[0];
		Template t = ctx.gt.getTemplate(path);
		// 快速复制父模板的变量
		t.binding(ctx.globalVar);
		if (ctx.objectKeys != null && !ctx.objectKeys.isEmpty()) {
			t.dynamic(ctx.objectKeys);
		}

		t.renderTo(outputStream);
	}

	/**
	 * @param template
	 * @param response
	 */
	public static void renderView(Template template, HttpServletResponse response) {
		response.setCharacterEncoding(ContextUtils.getCharset().displayName());
		Writer writer = null;
		OutputStream os = null;
		try {
			// response.setContentType(contentType);
			if (template.gt.getConf().isDirectByteOutput()) {
				os = response.getOutputStream();
				template.renderTo(os);
			} else {
				writer = response.getWriter();
				template.renderTo(writer);
			}

		} catch (IOException e) {
			handleClientError(e);

		} catch (BeetlException e) {
			handleBeetlException(e);
		}

		finally {
			try {
				if (writer != null)
					writer.flush();
				if (os != null) {
					os.flush();
				}
			} catch (IOException e) {
				handleClientError(e);
			}
		}
	}

	/**
	 * 处理客户端抛出的IO异常
	 * 
	 * @param ex
	 */
	protected static void handleClientError(IOException ex) {
		// do nothing
	}

	/**
	 * 处理客户端抛出的IO异常
	 * 
	 * @param ex
	 */
	protected static void handleBeetlException(BeetlException ex) {
		throw ex;
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
}
