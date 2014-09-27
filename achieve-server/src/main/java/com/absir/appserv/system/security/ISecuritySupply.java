/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-5-13 下午6:52:38
 */
package com.absir.appserv.system.security;

/**
 * @author absir
 * 
 */
public interface ISecuritySupply {

	/**
	 * @param securityContext
	 */
	public void saveSession(SecurityContext securityContext);

	/**
	 * @param securityContext
	 */
	public void removeSession(SecurityContext securityContext);
}
