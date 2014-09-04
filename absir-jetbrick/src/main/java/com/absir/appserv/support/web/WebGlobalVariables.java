/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月4日 下午2:23:42
 */
package com.absir.appserv.support.web;

import jetbrick.template.JetGlobalVariables;

/**
 * @author absir
 *
 */
public interface WebGlobalVariables extends JetGlobalVariables {

	/**
	 * @param name
	 * @param variable
	 */
	public void register(String name, Object variable);
}
