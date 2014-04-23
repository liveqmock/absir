/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-23 下午12:26:18
 */
package com.absir.appserv.system.service;

import com.absir.appserv.system.bean.proxy.JiUserBase;

/**
 * @author absir
 * 
 */
public interface IdentityService {

	/**
	 * @param parameters
	 * @return
	 */
	public JiUserBase getUserBase(String[] parameters);

}
