/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-11 下午1:56:46
 */
package com.absir.appserv.system.crud;

import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.CrudProperty;
import com.absir.appserv.crud.ICrudFactory;
import com.absir.appserv.crud.ICrudProcessor;
import com.absir.appserv.support.developer.JCrudField;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.value.JaCrud;
import com.absir.core.kernel.KernelDyna;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
public class DateCrudFactory implements ICrudFactory, ICrudProcessor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudProcessor#crud(com.absir.appserv.crud.
	 * CrudProperty, java.lang.Object, com.absir.appserv.crud.CrudHandler,
	 * com.absir.appserv.system.bean.proxy.JiUserBase)
	 */
	@Override
	public void crud(CrudProperty crudProperty, Object entity, CrudHandler crudHandler, JiUserBase user) {
		// TODO Auto-generated method stub
		if (crudHandler.getCrud() != JaCrud.Crud.CREATE || KernelDyna.to(crudProperty.get(entity), long.class) <= 0) {
			crudProperty.set(entity, KernelDyna.to(System.currentTimeMillis(), crudProperty.getType()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.ICrudFactory#getProcessor(com.absir.appserv.support
	 * .entity.value.JoEntity, com.absir.appserv.support.developer.JCrudField)
	 */
	@Override
	public ICrudProcessor getProcessor(JoEntity joEntity, JCrudField crudField) {
		// TODO Auto-generated method stub
		return this;
	}
}
