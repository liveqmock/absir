/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年8月1日 上午11:35:23
 */
package org.beetl.ext.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;

import com.absir.servlet.InDispathFilter;

/**
 * @author absir
 *
 */
public class WebVariableScope extends WebVariable {

	/**
	 * @author absir
	 *
	 */
	public static class ScopeSession extends SessionWrapper {

		/**
		 * @param session
		 */
		public ScopeSession(HttpSession session) {
			super(session);
			// TODO Auto-generated constructor stub
		}

		public Object get(String key) {

			return session == null ? null : session.getAttribute(key);
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public Object get(String name) {
		Object value = request.getAttribute(name);
		if (value == null) {
			value = session == null ? null : session.getAttribute(name);
			if (value == null) {
				value = InDispathFilter.getServletContext().getAttribute(name);
			}
		}

		return value;
	}

	/**
	 * @param gt
	 * @param key
	 * @param request
	 * @return
	 */
	public Template ready(GroupTemplate gt, String key, HttpServletRequest request) {
		setRequest(request);
		setResponse(response);
		setSession(request.getSession(false));
		Template template = gt.getTemplate(key);
		template.binding("session", new ScopeSession(session));
		template.binding("servlet", this);
		template.binding("request", request);
		template.binding("ctxPath", request.getContextPath());
		return template;
	}
}
