/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-8 下午3:48:05
 */
package com.absir.appserv.crud;

import com.absir.appserv.crud.CrudHandler.CrudInvoker;
import com.absir.appserv.system.bean.value.JaCrud.Crud;

/**
 * @author absir
 * 
 */
public abstract class CrudPropertyReference {

	/** crudProperty */
	protected CrudProperty crudProperty;

	/** cruds */
	protected Crud[] cruds;

	/** valueCrudEntity */
	protected CrudEntity valueCrudEntity;

	/**
	 * @return
	 */
	public CrudProperty getCrudProperty() {
		return crudProperty;
	}

	/**
	 * @param entity
	 * @param crudInvoker
	 */
	protected abstract void crud(Object entity, CrudInvoker crudHandler);
}
