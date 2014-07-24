/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月21日 下午12:49:15
 */
package com.absir.appserv.support.web;

import java.io.IOException;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.web.WebRender;

import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.Value;
import com.absir.context.core.ContextUtils;
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
public class WebBeetlView extends ReturnedResolverView {

	/** ME */
	public static final WebBeetlView ME = BeanFactoryUtils.get(WebBeetlView.class);

	@Value("web.view.prefix")
	private String prefix = "/WEB-INF/beetl/";

	@Value("web.view.suffix")
	private String suffix = ".html";

	/** groupTemplate */
	protected GroupTemplate groupTemplate;

	/**
	 * @return the groupTemplate
	 */
	public GroupTemplate getGroupTemplate() {
		return groupTemplate;
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
	public void renderView(String view, final Input input, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding(ContextUtils.getCharset().displayName());
		new WebRender(groupTemplate) {
			@Override
			protected void modifyTemplate(Template template, String key, HttpServletRequest request, HttpServletResponse response, Object... args) {
				template.binding("input", input);
				for (Entry<String, Object> entry : input.getModel().entrySet()) {
					template.binding(entry.getKey(), entry.getValue());
				}
			}

		}.render(view, request, response);
	}
}
