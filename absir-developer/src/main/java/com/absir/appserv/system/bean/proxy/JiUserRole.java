/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-20 上午9:14:20
 */
package com.absir.appserv.system.bean.proxy;

/**
 * @author absir
 * 
 */
public interface JiUserRole extends JiBase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.proxy.JiBase#getId()
	 */
	@Override
	public Long getId();

	/**
	 * @return
	 */
	public String getRolename();

}
