/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-7-5 下午4:48:35
 */
package com.absir.appserv.support.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author absir
 * 
 */
public class WebRequestWrapper extends HttpServletRequestWrapper {

	/** requestURI */
	private String requestURI;

	/**
	 * @param request
	 */
	public WebRequestWrapper(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the requestURI
	 */
	public String getRequestURI() {
		return requestURI == null ? super.getRequestURI() : requestURI;
	}

	/**
	 * @param requestURI
	 *            the requestURI to set
	 */
	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

}
