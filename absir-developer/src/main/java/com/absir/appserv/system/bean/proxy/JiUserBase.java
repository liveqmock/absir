/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-4 下午2:06:23
 */
package com.absir.appserv.system.bean.proxy;

import java.util.Collection;

import com.absir.appserv.system.bean.base.JbUserRole;

/**
 * @author absir
 * 
 */
public interface JiUserBase {

	/**
	 * @return
	 */
	public Long getUserId();

	/**
	 * @return
	 */
	public String getUsername();

	/**
	 * @return
	 */
	public boolean isDeveloper();

	/**
	 * @return
	 */
	public int getUserRoleLevel();

	/**
	 * @return
	 */
	public Collection<? extends JiUserRole> userRoles();

	/**
	 * @return
	 */
	public Collection<? extends JbUserRole> getUserRoles();
}
