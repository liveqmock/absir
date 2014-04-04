/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-8 下午4:49:27
 */
package com.absir.appserv.crud;

import com.absir.appserv.support.developer.JCrud;

/**
 * @author absir
 * 
 */
public abstract class CrudProperty {

	/** type */
	protected Class<?> type;

	/** jCrud */
	protected JCrud jCrud;

	/** crudEntity */
	protected CrudEntity crudEntity;

	/** crudProcessor */
	protected ICrudProcessor crudProcessor;

	/**
	 * @return
	 */
	public abstract String getName();

	/**
	 * @param entity
	 * @return
	 */
	public abstract Object get(Object entity);

	/**
	 * @param entity
	 * @param propertyValue
	 */
	public abstract void set(Object entity, Object propertyValue);

	/**
	 * @return
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * @return
	 */
	public JCrud getjCrud() {
		return jCrud;
	}

	/**
	 * @return
	 */
	public CrudEntity getCrudEntity() {
		return crudEntity;
	}
}
