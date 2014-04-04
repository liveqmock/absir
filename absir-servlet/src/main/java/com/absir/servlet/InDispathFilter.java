/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-8 下午4:47:28
 */
package com.absir.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.absir.bean.basis.BeanFactory;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.context.core.ContextUtils;
import com.absir.core.kernel.KernelDyna;
import com.absir.server.in.InDispatcher;
import com.absir.server.in.InMethod;
import com.absir.server.in.InModel;
import com.absir.server.in.Input;

/**
 * @author absir
 * 
 */
public class InDispathFilter extends InDispatcher<HttpServletRequest, HttpServletResponse> implements Filter {

	/** servletContext */
	private static ServletContext servletContext;

	/** contextPath */
	private String contextPath;

	/** contextPathLength */
	private int contextPathLength;

	/** urlDecode */
	private boolean urlDecode;

	/**
	 * @return the servletContext
	 */
	public static ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * @return the contextPath
	 */
	public String getContextPath() {
		return contextPath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		servletContext = filterConfig.getServletContext();
		contextPath = filterConfig.getInitParameter("contextPath");
		if (contextPath == null) {
			BeanFactory beanFactory = BeanFactoryUtils.get();
			if (beanFactory != null) {
				contextPath = KernelDyna.to(beanFactory.getBeanConfig().getValue("contextPath"), String.class);
			}

			if (contextPath == null) {
				contextPath = filterConfig.getServletContext().getContextPath();
			}
		}

		contextPathLength = contextPath.length();
		String urlDecodeing = filterConfig.getInitParameter("urlDecode");
		if (urlDecodeing == null) {
			BeanFactory beanFactory = BeanFactoryUtils.get();
			if (beanFactory != null) {
				urlDecodeing = KernelDyna.to(beanFactory.getBeanConfig().getValue("urlDecode"), String.class);
			}
		}

		if (urlDecodeing != null) {
			urlDecode = KernelDyna.to(urlDecodeing, boolean.class);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		try {
			if (!(request instanceof HttpServletRequest && response instanceof HttpServletResponse && on(getUri(request), (HttpServletRequest) request, (HttpServletResponse) response))) {
				chain.doFilter(request, response);
			}

		} catch (Throwable e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @param request
	 * @return
	 */
	private String getUri(ServletRequest request) {
		if (request instanceof HttpServletRequest) {
			String uri = ((HttpServletRequest) request).getRequestURI();
			return uri.length() < contextPathLength ? null : uri.substring(contextPathLength + 1);
		}

		return request.getParameter("uri");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.IDispatcher#getInMethod(java.lang.Object)
	 */
	@Override
	public InMethod getInMethod(HttpServletRequest req) {
		// TODO Auto-generated method stub
		if (req instanceof HttpServletRequest) {
			try {
				return InMethod.valueOf(((HttpServletRequest) req).getMethod().toUpperCase());

			} catch (Exception e) {
			}
		}

		return InMethod.GET;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.IDispatcher#decodeUri(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public String decodeUri(String uri, HttpServletRequest req) {
		// TODO Auto-generated method stub
		if (urlDecode) {
			return uri;
		}

		try {
			String enc = req.getCharacterEncoding();
			if (enc == null) {
				enc = ContextUtils.getCharset().displayName();
			}

			return URLDecoder.decode(uri, enc);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			return uri;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.in.InDispatcher#input(java.lang.String,
	 * com.absir.server.in.InMethod, java.util.Map, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	protected Input input(String uri, InMethod inMethod, InModel model, HttpServletRequest req, HttpServletResponse res) {
		// TODO Auto-generated method stub
		return new InputRequest(uri, inMethod, model, req, res);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
}
