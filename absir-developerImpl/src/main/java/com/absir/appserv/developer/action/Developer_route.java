/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-10 下午4:54:14
 */
package com.absir.appserv.developer.action;

import com.absir.bean.core.BeanFactoryUtils;
import com.absir.server.in.Input;
import com.absir.server.value.Body;
import com.absir.server.value.Server;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 * 
 */
@Server
public class Developer_route extends DeveloperServer {

	/**
	 * @return
	 */
	@Body
	public Object route(Input input) {
		if (input instanceof InputRequest) {
			return ((InputRequest) input).getRequest().getSession().getServletContext().getRealPath("");
		}

		return BeanFactoryUtils.getBeanConfig().getClassPath();
	}
}
