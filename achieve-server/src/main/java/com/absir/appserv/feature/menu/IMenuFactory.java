/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-5 下午2:36:52
 */
package com.absir.appserv.feature.menu;

import com.absir.server.route.RouteMatcher;

/**
 * @author absir
 * 
 */
public interface IMenuFactory {

	/**
	 * @param menuBeanRoot
	 * @param routeMatcher
	 */
	public void proccess(MenuBeanRoot menuBeanRoot, RouteMatcher routeMatcher);

}
