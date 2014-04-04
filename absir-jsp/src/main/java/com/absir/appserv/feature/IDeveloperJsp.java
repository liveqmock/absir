package com.absir.appserv.feature;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.absir.appserv.support.developer.IDeveloper;

/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-8-29 下午3:57:10
 */

/**
 * @author absir
 * 
 */
public interface IDeveloperJsp extends IDeveloper {

	/**
	 * @param filepath
	 * @param includePath
	 * @param request
	 * @param response
	 */
	public void generate(String filepath, String includePath, HttpServletRequest request, ServletResponse response);

}
