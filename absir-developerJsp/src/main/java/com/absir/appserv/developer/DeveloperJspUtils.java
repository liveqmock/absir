/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-3 下午5:18:30
 */
package com.absir.appserv.developer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.absir.appserv.support.web.WebJsplUtils;
import com.absir.bean.inject.value.Inject;

/**
 * @author absir
 * 
 */
@Inject
public class DeveloperJspUtils extends DeveloperUtils {

	static {
		suffix = ".jsp";
	}

	/**
	 * @param filepath
	 * @param pageContext
	 * @param request
	 * @param response
	 * @throws Throwable
	 */
	public static void generate(String filepath, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		generate(filepath, WebJsplUtils.getServletPath(pageContext), pageContext, request, response);
	}

	/**
	 * @param filepath
	 * @param includePath
	 * @param pageContext
	 * @param request
	 * @param response
	 * @throws Throwable
	 */
	public static void generate(String filepath, String includePath, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		generate(WebJsplUtils.getFullIncludePath(filepath, pageContext), includePath, request, response);
	}

	/**
	 * @param filepath
	 * @param includePath
	 * @param pageContext
	 * @param request
	 * @param response
	 * @throws Throwable
	 */
	public static void generateInclude(String filepath, String includePath, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws Throwable {
		filepath = WebJsplUtils.getFullExistIncludePath(filepath, pageContext);
		generate(filepath, includePath, pageContext, request, response);
		WebJsplUtils.include(filepath, pageContext, request, response);
	}

	/**
	 * @param option
	 * @param types
	 * @param pageContext
	 * @param request
	 * @param response
	 * @param relativePaths
	 * @throws Throwable
	 */
	public static void includeExist(String option, List<String> types, PageContext pageContext, HttpServletRequest request, HttpServletResponse response, String... relativePaths) throws Throwable {
		includeExist(option, types, relativePaths, pageContext, request, response);
	}

	/**
	 * @param option
	 * @param entityName
	 * @param pageContext
	 * @param request
	 * @param response
	 * @param relativePaths
	 * @throws Throwable
	 */
	public static void includeExist(String option, String entityName, PageContext pageContext, HttpServletRequest request, HttpServletResponse response, String... relativePaths) throws Throwable {
		includeExist(option, entityName, relativePaths, pageContext, request, response);
	}
}
