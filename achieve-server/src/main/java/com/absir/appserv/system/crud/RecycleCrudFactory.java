/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-7-9 下午12:41:10
 */
package com.absir.appserv.system.crud;

import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.CrudProperty;
import com.absir.appserv.crud.ICrudFactory;
import com.absir.appserv.crud.ICrudProcessor;
import com.absir.appserv.support.developer.JCrudField;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.service.utils.CrudServiceUtils;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelObject;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
public class RecycleCrudFactory implements ICrudFactory, ICrudProcessor {

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
		String recycleName = crudProperty.getCrudEntity().getJoEntity().getEntityName() + "Recycle";
		Class<?> recycleClass = SessionFactoryUtils.getEntityClass(recycleName);
		if (recycleClass != null) {
			Object recycle = KernelClass.newInstance(recycleClass);
			if (recycle != null) {
				KernelObject.copy(entity, recycle);
			}

			CrudServiceUtils.merge(recycleName, handler.getCrudRecord(), recycle, true, user, null);
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
