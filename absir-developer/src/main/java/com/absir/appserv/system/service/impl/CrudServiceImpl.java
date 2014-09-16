/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-6 下午1:34:05
 */
package com.absir.appserv.system.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.absir.appserv.crud.CrudUtils;
import com.absir.appserv.crud.ICrudSupply;
import com.absir.appserv.jdbc.JdbcCondition;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.service.CrudService;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.InjectType;
import com.absir.core.kernel.KernelLang.PropertyFilter;
import com.absir.orm.value.JoEntity;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes" })
@Bean
public class CrudServiceImpl implements CrudService {

	/** crudSupports */
	@Inject(type = InjectType.Selectable)
	private ICrudSupply[] crudSupplies;

	/** entityNameMap */
	private Map<String, ICrudSupply> entityNameMapCrudSupply = new HashMap<String, ICrudSupply>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.service.CrudService#getCrudSupport(java.lang
	 * .String)
	 */
	@Override
	public ICrudSupply getCrudSupply(String entityName) {
		// TODO Auto-generated method stub
		ICrudSupply crudSupply = entityNameMapCrudSupply.get(entityName);
		if (crudSupply == null && crudSupplies != null) {
			for (ICrudSupply supply : crudSupplies) {
				if (supply.getEntityClass(entityName) != null) {
					crudSupply = supply;
					entityNameMapCrudSupply.put(entityName, crudSupply);
				}
			}
		}

		return crudSupply;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.service.CrudService#merge(java.lang.String,
	 * java.util.Map, java.lang.Object, com.absir.appserv.crud.ICrudSupply,
	 * boolean, com.absir.appserv.system.bean.proxy.JiUserBase,
	 * com.absir.core.kernel.KernelLang.PropertyFilter)
	 */
	@Override
	public void merge(String entityName, Map<String, Object> crudRecord, Object entity, ICrudSupply crudSupply, boolean create, JiUserBase user, PropertyFilter filter) {
		// TODO Auto-generated method stub
		CrudUtils.crud(create ? Crud.CREATE : Crud.UPDATE, crudRecord, new JoEntity(entityName, entity.getClass()), entity, filter, user);
		crudSupply.mergeEntity(entityName, entity, create);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.service.CrudService#delete(java.lang.String,
	 * java.io.Serializable, com.absir.appserv.crud.ICrudSupply,
	 * com.absir.appserv.jdbc.JdbcCondition,
	 * com.absir.appserv.system.bean.proxy.JiUserBase)
	 */
	@Override
	public Object delete(String entityName, Serializable id, ICrudSupply crudSupply, JdbcCondition jdbcCondition, JiUserBase user) {
		// TODO Auto-generated method stub
		Object entity = crudSupply.get(entityName, id, jdbcCondition);
		if (entity == null) {
			return null;
		}

		CrudUtils.crud(Crud.DELETE, null, new JoEntity(entityName, entity.getClass()), entity, null, user);
		crudSupply.deleteEntity(entityName, entity);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.service.CrudService#delete(java.lang.String,
	 * com.absir.appserv.crud.ICrudSupply, com.absir.appserv.jdbc.JdbcCondition,
	 * com.absir.appserv.system.bean.proxy.JiUserBase)
	 */
	@Override
	public List delete(String entityName, ICrudSupply crudSupply, JdbcCondition jdbcCondition, JiUserBase user) {
		// TODO Auto-generated method stub
		JoEntity joEntity = null;
		List entities = crudSupply.list(entityName, jdbcCondition, null, 0, 0);
		for (Object entity : entities) {
			if (entity != null) {
				if (joEntity == null) {
					joEntity = new JoEntity(entityName, entity.getClass());
				}

				CrudUtils.crud(Crud.DELETE, null, new JoEntity(entityName, entity.getClass()), entity, null, user);
				crudSupply.deleteEntity(entityName, entity);
			}
		}

		return entities;
	}
}
