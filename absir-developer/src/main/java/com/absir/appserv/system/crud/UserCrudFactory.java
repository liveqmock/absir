/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-27 上午9:44:05
 */
package com.absir.appserv.system.crud;

import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.CrudProperty;
import com.absir.appserv.crud.ICrudFactory;
import com.absir.appserv.crud.ICrudProcessor;
import com.absir.appserv.support.developer.JCrudField;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.core.dyna.DynaBinder;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
public class UserCrudFactory implements ICrudFactory {

	/** USER_ID_PROCESSOR */
	private static final ICrudProcessor USER_ID_PROCESSOR = new ICrudProcessor() {

		@Override
		public void crud(CrudProperty crudProperty, Object entity, CrudHandler crudHandler, JiUserBase user) {
			// TODO Auto-generated method stub
			if (user != null) {
				if (crudProperty.getType().isAssignableFrom(user.getClass())) {
					crudProperty.set(entity, user);

				} else {
					crudProperty.set(entity, DynaBinder.to(user.getUserId(), crudProperty.getType()));
				}
			}
		}
	};

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
		return USER_ID_PROCESSOR;
	}
}
