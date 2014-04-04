/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-6 下午12:42:34
 */
package com.absir.appserv.system.menu;

import com.absir.appserv.feature.menu.IMenuFactory;
import com.absir.appserv.feature.menu.MenuBeanRoot;
import com.absir.bean.inject.value.Bean;
import com.absir.server.route.RouteMatcher;

/**
 * @author absir
 * 
 */
@Bean
public class CrudMenuFactory implements IMenuFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.feature.menu.IMenuFactory#proccess(com.absir.appserv
	 * .feature.menu.MenuBeanRoot, com.absir.server.route.RouteMatcher)
	 */
	@Override
	public void proccess(MenuBeanRoot menuBeanRoot, RouteMatcher routeMatcher) {
		// TODO Auto-generated method stub

	}

}
