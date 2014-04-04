/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-24 下午1:05:52
 */
package com.absir.appserv.crud;

import com.absir.appserv.system.bean.proxy.JiUserBase;

/**
 * @author absir
 * 
 */
public abstract class CrudProcessorInput<T> implements ICrudProcessorInput<T> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudProcessor#crud(com.absir.appserv.crud.
	 * CrudProperty, java.lang.Object, com.absir.appserv.crud.CrudHandler,
	 * com.absir.appserv.system.bean.proxy.JiUserBase)
	 */
	@Override
	public void crud(CrudProperty crudProperty, Object entity, CrudHandler handler, JiUserBase user) {
		// TODO Auto-generated method stub
	}
}
