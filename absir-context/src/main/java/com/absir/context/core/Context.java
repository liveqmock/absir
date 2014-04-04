/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-14 下午3:56:24
 */
package com.absir.context.core;

import java.io.Serializable;

import com.absir.context.bean.IBean;
import com.absir.core.kernel.KernelObject;

/**
 * @author absir
 * 
 */
public abstract class Context<ID extends Serializable> implements IBean<ID> {

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return KernelObject.hashCode(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}

		return KernelObject.equals(id, ((Context<?>) obj).id);
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