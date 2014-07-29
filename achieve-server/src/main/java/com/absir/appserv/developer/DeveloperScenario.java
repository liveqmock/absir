/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月28日 下午4:15:38
 */
package com.absir.appserv.developer;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.ServletRequest;

import com.absir.orm.value.JoEntity;

/**
 * @author absir
 *
 */
@SuppressWarnings("unchecked")
public class DeveloperScenario {

	/** DeveloperScenarioStacks */
	private static String DeveloperScenarioStacks = DeveloperScenario.class.getName() + "_STACKS";

	/**
	 * @param request
	 * @return
	 */
	public static Map<String, Stack<Object>> getDeveloperStatcks(ServletRequest request) {
		Object stacksObject = request.getAttribute(DeveloperScenarioStacks);
		if (stacksObject != null && stacksObject instanceof Map) {
			return (Map<String, Stack<Object>>) stacksObject;
		}

		Map<String, Stack<Object>> developerStatcks = new HashMap<String, Stack<Object>>();
		request.setAttribute(DeveloperScenarioStacks, developerStatcks);
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

	public static final String SCENARIO = "SCENARIO";

	/**
	 * @param value
	 * @param request
	 */
	public static void set(String value, ServletRequest request) {
		push(SCENARIO, value, request);
	}

	/**
	 * @param request
	 * @return
	 */
	public static String get(ServletRequest request) {
		return (String) peek(SCENARIO, request);
	}

	/**
	 * @param request
	 * @return
	 */
	public static String pop(ServletRequest request) {
		return (String) pop(SCENARIO, request);
	}

	private static final String JOENTITY = "joEntity";

	/**
	 * @param joEntity
	 */
	public static void pushJoEntity(JoEntity value, ServletRequest request) {
		push(JOENTITY, value, request);
	}

	/**
	 * @param request
	 * @return
	 */
	public static JoEntity getJoEntity(ServletRequest request) {
		return (JoEntity) peek(JOENTITY, request);
	}

	/**
	 * @param request
	 * @return
	 */
	public static JoEntity popJoEntity(ServletRequest request) {
		return (JoEntity) pop(JOENTITY, request);
	}
}
