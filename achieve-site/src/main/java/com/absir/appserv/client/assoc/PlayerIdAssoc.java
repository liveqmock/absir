/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-21 下午7:27:54
 */
package com.absir.appserv.client.assoc;

import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.CrudProperty;
import com.absir.appserv.crud.ICrudFactory;
import com.absir.appserv.crud.ICrudProcessor;
import com.absir.appserv.jdbc.JdbcCondition;
import com.absir.appserv.jdbc.JdbcCondition.Conditions;
import com.absir.appserv.support.developer.JCrudField;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.dao.IAssocDao;
import com.absir.orm.value.JePermission;
import com.absir.orm.value.JiAssoc;
import com.absir.orm.value.JoEntity;
import com.absir.server.in.Input;
import com.absir.server.on.OnPut;
import com.absir.server.socket.InputSocket;

/**
 * @author absir
 * 
 */
public class PlayerIdAssoc implements IAssocDao, ICrudFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.dao.IAssocDao#supportAssocClass(java.lang.Class,
	 * java.lang.String, com.absir.appserv.system.bean.proxy.JiUserBase,
	 * com.absir.orm.value.JePermission)
	 */
	@Override
	public boolean supportAssocClass(Class<? extends JiAssoc> assocClass, String rootEntityName, JiUserBase user, JePermission permission) {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.dao.IAssocDao#assocConditions(java.lang.String,
	 * com.absir.appserv.system.bean.proxy.JiUserBase,
	 * com.absir.orm.value.JePermission, java.lang.Object,
	 * com.absir.appserv.jdbc.JdbcCondition,
	 * com.absir.appserv.jdbc.JdbcCondition.Conditions,
	 * com.absir.appserv.jdbc.JdbcCondition.Conditions)
	 */
	@Override
	public void assocConditions(String rootEntityName, JiUserBase user, JePermission permission, Object strategies, JdbcCondition jdbcCondition, Conditions includeConditions,
			Conditions excludeConditions) {
		// TODO Auto-generated method stub
		OnPut onPut = OnPut.get();
		if (onPut == null) {
			return;
		}

		Object playerId = onPut.getInput().getId();
		if (playerId != null && playerId.getClass() == Long.class) {
			excludeConditions.add(jdbcCondition.getCurrentPropertyAlias() + ".playerId =");
			excludeConditions.add(playerId);
		}
	}

	/** PLAYER_ID_CRUD_PROCESSOR */
	private static final ICrudProcessor PLAYER_ID_CRUD_PROCESSOR = new ICrudProcessor() {

		@Override
		public void crud(CrudProperty crudProperty, Object entity, CrudHandler crudHandler, JiUserBase user) {
			// TODO Auto-generated method stub
			OnPut onPut = OnPut.get();
			if (onPut != null) {
				Input input = onPut.getInput();
				if (input instanceof InputSocket) {
					crudProperty.set(entity, ((InputSocket) input).getId());
				}
			}
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.ICrudFactory#getProcessor(com.absir.orm.value.
	 * JoEntity, com.absir.appserv.support.developer.JCrudField)
	 */
	@Override
	public ICrudProcessor getProcessor(JoEntity joEntity, JCrudField crudField) {
		// TODO Auto-generated method stub
		return PLAYER_ID_CRUD_PROCESSOR;
	}
}
