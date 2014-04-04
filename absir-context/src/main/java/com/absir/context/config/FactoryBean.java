/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-21 上午10:02:42
 */
package com.absir.context.config;

/**
 * @author absir
 * 
 */
public interface FactoryBean<T> {

	/**
	 * @return
	 */
	public T getBeanObject();
}
