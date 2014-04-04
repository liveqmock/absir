/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-6 下午10:36:34
 */
package com.absir.appserv.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.absir.appserv.support.web.WebJsplUtils;
import com.absir.bean.basis.Configure;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.InjectType;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
@Configure
public abstract class WebJsplDeveloper {

	@Inject(type = InjectType.Selectable)
	private static IDeveloperJsp developer;

	/** WebJsplDeveloperStacks */
	private static String WebJsplDeveloperStacks = WebJsplDeveloper.class.getName() + "_STACKS";

	/**
	 * @param request
	 * @return
	 */
	public static Map<String, Stack<Object>> getDeveloperStatcks(ServletRequest request) {
		Object stacksObject = request.getAttribute(WebJsplDeveloperStacks);
		if (stacksObject != null && stacksObject instanceof Map) {
			return (Map<String, Stack<Object>>) stacksObject;
		}

		Map<String, Stack<Object>> developerStatcks = new HashMap<String, Stack<Object>>();
		request.setAttribute(WebJsplDeveloperStacks, developerStatcks);
		return developerStatcks;
	}

	/**
	 * @param name
	 * @param value
	 * @param request
	 */
	public static void push(String name, Object value, ServletRequest request) {
		Map<String, Stack<Object>> developerStatcks = getDeveloperStatcks(request);
		Stack<Object> stack = developerStatcks.get(name);
		if (stack == null) {
			stack = new Stack<Object>();
			developerStatcks.put(name, stack);
		}

		stack.push(value);
	}

	/**
	 * @param name
	 * @param request
	 * @return
	 */
	public static Object peek(String name, ServletRequest request) {
		Stack<Object> stack = getDeveloperStatcks(request).get(name);
		return stack == null || stack.size() <= 0 ? null : stack.peek();
	}

	/**
	 * @param name
	 * @param request
	 * @return
	 */
	public static Object pop(String name, ServletRequest request) {
		Stack<Object> stack = getDeveloperStatcks(request).get(name);
		return stack == null || stack.size() <= 0 ? null : stack.pop();
	}

	private static final String SCENARIO = "SCENARIO";

	/**
	 * @param value
	 * @param request
	 */
	public static void setScenario(String value, ServletRequest request) {
		push(SCENARIO, value, request);
	}

	/**
	 * @param request
	 * @return
	 */
	public static String getScenario(ServletRequest request) {
		return (String) peek(SCENARIO, request);
	}

	/**
	 * @param request
	 * @return
	 */
	public static String popScenario(ServletRequest request) {
		return (String) pop(SCENARIO, request);
	}

	/**
	 * @param filepath
	 * @param pageContext
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void generate(String filepath, PageContext pageContext, HttpServletRequest request, ServletResponse response) throws ServletException, IOException {
		if (developer != null) {
			generate(filepath, WebJsplUtils.getServletPath(pageContext), pageContext, request, response);
		}
	}

	/**
	 * @param filepath
	 * @param includePath
	 * @param pageContext
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void generate(String filepath, String includePath, PageContext pageContext, HttpServletRequest request, ServletResponse response) throws ServletException, IOException {
		if (developer != null) {
			developer.generate(WebJsplUtils.getFullIncludePath(filepath, pageContext), WebJsplUtils.getFullIncludePath(includePath, pageContext), request, response);
		}
	}

	/**
	 * @param filepath
	 * @param pageContext
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void include(String filepath, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		include(filepath, WebJsplUtils.getServletPath(pageContext.getRequest()), pageContext, request, response);
	}

	/**
	 * @param filepath
	 * @param includePath
	 * @param pageContext
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void include(String filepath, String includePath, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		generate(filepath, includePath, pageContext, request, response);
		WebJsplUtils.include(filepath, pageContext, request, response);
	}

	/**
	 * @param includePath
	 * @param pageContext
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public static String getIncludeContent(String includePath, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return getIncludeContent(WebJsplUtils.getServletPath(pageContext.getRequest()), includePath, pageContext, request, response);
	}

	/**
	 * @param filepath
	 * @param includePath
	 * @param pageContext
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public static String getIncludeContent(String filepath, String includePath, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		generate(filepath, includePath, pageContext, request, response);
		return WebJsplUtils.getIncludeContent(includePath, request, response);
	}
}
