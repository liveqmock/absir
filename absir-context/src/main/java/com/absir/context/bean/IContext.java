/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-11 下午4:44:15
 */
package com.absir.context.bean;

/**
 * @author absir
 * 
 */
public interface IContext {

	/**
	 * @param contextTime
	 */
	public void retainAt(long contextTime);

	/**
	 * @param contextTime
	 * @return
	 */
	public boolean stepDone(long contextTime);

	/**
	 * 
	 */
	public void uninitialize();
}
