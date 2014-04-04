/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-11 上午10:48:41
 */
package com.absir.appserv.system.crud;

import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.CrudProperty;
import com.absir.appserv.crud.ICrudFactory;
import com.absir.appserv.crud.ICrudProcessor;
import com.absir.appserv.support.developer.JCrudField;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.dao.BaseDao;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
public class DaoCrudFactory implements ICrudFactory {

	/** DAO_PROCESSOR */
	private final ICrudProcessor DAO_PROCESSOR = new ICrudProcessor() {

		@Override
		public void crud(CrudProperty crudProperty, Object entity, CrudHandler crudHandler, JiUserBase user) {
			// TODO Auto-generated method stub
			BaseDao<Object, ?> baseDao = (BaseDao<Object, ?>) BeanDao.getBaseDao(entity.getClass());
			if (baseDao == null) {
				return;
			}

			baseDao.crud(crudHandler.getCrud(), crudProperty, crudHandler, entity);
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
		if (joEntity.getEntityName() == null || joEntity.getEntityClass() == null) {
			return null;
		}

		return DAO_PROCESSOR;
	}
}
