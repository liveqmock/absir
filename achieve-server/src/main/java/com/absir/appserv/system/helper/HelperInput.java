/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-13 下午3:55:01
 */
package com.absir.appserv.system.helper;

import com.absir.server.in.Input;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 * 
 */
public class HelperInput {

	/**
	 * @param input
	 * @return
	 */
	public static boolean isAjax(Input input) {
		return input instanceof InputRequest && (((InputRequest) input).getRequest()).getHeader("X-Requested-With") != null;
	}

}
