/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-15 下午4:58:40
 */
package com.absir.context.core;

import java.io.Serializable;

import com.absir.core.base.Base;

/**
 * @author absir
 * 
 */
public class Bean<ID extends Serializable> extends Base<ID> {

	/** id */
	private ID id;

	/**
	 * @return the id
	 */
	public ID getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(ID id) {
		this.id = id;
	}
}
