/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-14 下午3:15:31
 */
package com.absir.context.bean;

import java.io.Serializable;

/**
 * @author absir
 * 
 */
public interface IBean<ID extends Serializable> {

	/**
	 * @return
	 */
	public ID getId();

}
