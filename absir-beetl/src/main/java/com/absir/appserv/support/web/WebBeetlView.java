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
import java.lang.reflect.Field;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beetl.core.Configuration;
import org.beetl.core.Context;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.FileResourceLoader;
import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.web.WebRender;

import com.absir.appserv.support.developer.IDeveloper;
import com.absir.appserv.support.developer.IRender;
import com.absir.bean.basis.Base;
import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.Value;
import com.absir.context.core.ContextUtils;
import com.absir.core.kernel.KernelReflect;
import com.absir.server.in.Input;
import com.absir.server.on.OnPut;
import com.absir.server.route.returned.ReturnedResolverView;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 *
 */
@Base(order = -1)
@Bean
public class WebBeetlView extends ReturnedResolverView implements IRender {

	/** ME */
	public static final WebBeetlView ME = BeanFactoryUtils.get(WebBeetlView.class);

	@Value("web.view.prefix")
	private String prefix = "/WEB-INF/beetl/";

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

	/** contextField */
	private Field ctxField;

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

		ctxField = KernelReflect.declaredField(Template.class, "ctx");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.route.returned.ReturnedResolverView#resolveReturnedView
	 * (java.lang.String, com.absir.server.on.OnPut)
	 */
	@Override
	public void resolveReturnedView(String view, OnPut onPut) throws Exception {
		// TODO Auto-generated method stub
		Input input = onPut.getInput();
		if (input instanceof InputRequest) {
			InputRequest inputRequest = (InputRequest) input;
			renderView(prefix + view + suffix, input, inputRequest.getRequest(), inputRequest.getResponse());

		} else {
			onPut.getInput().write(view);
		}
	}

	/**
	 * @param view
	 * @param input
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void renderView(final String view, final Input input, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding(ContextUtils.getCharset().displayName());
		new WebRender(groupTemplate) {
			@Override
			protected void modifyTemplate(Template template, String key, HttpServletRequest request, HttpServletResponse response, Object... args) {
				template.binding("input", input);
				for (Entry<String, Object> entry : input.getModel().entrySet()) {
					template.binding(entry.getKey(), entry.getValue());
				}

				if (BeanFactoryUtils.getEnvironment() != Environment.DEVELOP && IDeveloper.ME != null) {
					try {
						Context ctx = (Context) ctxField.get(template);
						ctx.gt = groupTemplate;
						IDeveloper.ME.generate(view, view, ctx, request);

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}

		}.render(view, request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.support.developer.IRender#echo(java.lang.String)
	 */
	@Override
	public String echo(String value) {
		// TODO Auto-generated method stub
		return "print(" + value + ")";
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
		return "include(" + path + ")";
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
}
