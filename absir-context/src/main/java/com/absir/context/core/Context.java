/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-14 下午3:56:24
 */
package com.absir.context.core;

import java.io.Serializable;

/**
 * @author absir
 * 
 */
public abstract class Context<ID extends Serializable> extends Base<ID> {

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
	protected void setId(ID id) {
		this.id = id;
	}

	/**
	 * 
	 */
	protected abstract void initialize();

	/**
	 * 
	 */
	public abstract void uninitialize();
}