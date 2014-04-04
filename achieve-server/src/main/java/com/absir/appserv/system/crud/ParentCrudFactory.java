/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-7-17 下午2:57:22
 */
package com.absir.appserv.system.crud;

import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.CrudProperty;
import com.absir.appserv.crud.ICrudFactory;
import com.absir.appserv.crud.ICrudProcessor;
import com.absir.appserv.support.developer.JCrudField;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.service.utils.CrudServiceUtils;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
public class ParentCrudFactory implements ICrudFactory {

	/** PARENT_PROCESSOR */
	private final ICrudProcessor PARENT_PROCESSOR = new ICrudProcessor() {

		@Override
		public void crud(CrudProperty crudProperty, Object entity, CrudHandler crudHandler, JiUserBase user) {
			// TODO Auto-generated method stub
			if (crudHandler.getRoot() == entity) {
				entity = crudProperty.get(entity);
				if (entity != null) {
					CrudServiceUtils.merge(null, entity, false, user, null);
				}

				if (crudHandler.getCrud() != Crud.CREATE) {
					Object loaded = crudProperty.get(crudHandler.getEntity());
					if (loaded != null && loaded != entity) {
						CrudServiceUtils.merge(null, loaded, false, user, null);
					}
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
		return PARENT_PROCESSOR;
	}

}
