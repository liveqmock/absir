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
import com.absir.appserv.crud.value.ICrudBean;
import com.absir.appserv.support.developer.JCrudField;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
public class BeanCrudFactory implements ICrudFactory {

	/**
	 * @author absir
	 *
	 */
	public static class Proccessor implements ICrudProcessor {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.absir.appserv.crud.ICrudProcessor#crud(com.absir.appserv.crud
		 * .CrudProperty, java.lang.Object, com.absir.appserv.crud.CrudHandler,
		 * com.absir.appserv.system.bean.proxy.JiUserBase)
		 */
		@Override
		public void crud(CrudProperty crudProperty, Object entity, CrudHandler crudHandler, JiUserBase user) {
			// TODO Auto-generated method stub
			if (crudHandler.getRoot() != entity && entity instanceof ICrudBean) {
				((ICrudBean) entity).proccessCrud(crudHandler.getCrud(), crudHandler);
			}
		}
	}

	/** BEAN_PROCESSOR */
	private final ICrudProcessor BEAN_PROCESSOR = new Proccessor();

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

		return BEAN_PROCESSOR;
	}
}
