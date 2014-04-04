/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-14 上午10:58:43
 */
package com.absir.appserv.support.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.absir.server.in.IAttributes;

/**
 * @author absir
 * 
 */
public class WebRequestAttributes extends HttpServletRequestWrapper implements IAttributes {

	/**
	 * @param request
	 */
	public WebRequestAttributes(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

}
