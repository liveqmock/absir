/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月15日 下午12:39:40
 */
package com.absir.appserv.context;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.absir.appserv.crud.CrudSupply;
import com.absir.appserv.feature.menu.value.MaSupply;
import com.absir.appserv.jdbc.JdbcCondition;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.bean.basis.Basis;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.core.BeanFactoryImpl;
import com.absir.bean.inject.value.Bean;
import com.absir.context.core.Context;
import com.absir.context.core.ContextUtils;
import com.absir.core.base.IBase;

/**
 * @author absir
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Bean
@Basis
@MaSupply(folder = "功能管理")
public class ContextSupply extends CrudSupply<Context> {

	/** entityNameMapIdType */
	private Map<String, Class<Serializable>> entityNameMapIdType = new HashMap<String, Class<Serializable>>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.config.IBeanDefineSupply#getBeanDefines(com.absir.bean
	 * .core.BeanFactoryImpl, java.lang.Class)
	 */
	@Override
	public List<BeanDefine> getBeanDefines(BeanFactoryImpl beanFactory, Class<?> beanType) {
		// TODO Auto-generated method stub
		List<BeanDefine> beanDefines = super.getBeanDefines(beanFactory, beanType);
		if (beanDefines != null) {
			entityNameMapIdType.put(beanType.getSimpleName(), ContextUtils.getIdType((Class<? extends Context>) beanType));
		}

		return beanDefines;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.ICrudSupply#support(com.absir.appserv.system.bean
	 * .value.JaCrud.Crud)
	 */
	@Override
	public boolean support(Crud crud) {
		// TODO Auto-generated method stub
		return crud != Crud.CREATE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.ICrudSupply#getIdentifierName(java.lang.String)
	 */
	@Override
	public String getIdentifierName(String entityName) {
		// TODO Auto-generated method stub
		return "id";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.ICrudSupply#getIdentifierType(java.lang.String)
	 */
	@Override
	public Class<? extends Serializable> getIdentifierType(String entityName) {
		// TODO Auto-generated method stub
		return entityNameMapIdType.get(entityName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#getIdentifier(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public Object getIdentifier(String entityName, Object entity) {
		// TODO Auto-generated method stub
		return ((IBase) entity).getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.CrudSupply#findAll(java.lang.String)
	 */
	@Override
	public Collection findAll(String entityName) {
		// TODO Auto-generated method stub
		Map<Serializable, Context> contextMap = ContextUtils.getContextFactory().findContextMap(getEntityClass(entityName));
		return contextMap == null ? null : contextMap.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#get(java.lang.String,
	 * java.io.Serializable, com.absir.appserv.jdbc.JdbcCondition)
	 */
	@Override
	public Object get(String entityName, Serializable id, JdbcCondition jdbcCondition) {
		// TODO Auto-generated method stub
		return ContextUtils.findContext(getEntityClass(entityName), id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#create(java.lang.String)
	 */
	@Override
	public Object create(String entityName) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#mergeEntity(java.lang.String,
	 * java.lang.Object, boolean)
	 */
	@Override
	public void mergeEntity(String entityName, Object entity, boolean create) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#deleteEntity(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void deleteEntity(String entityName, Object entity) {
		// TODO Auto-generated method stub
		ContextUtils.getContextFactory().clearContext((Context) entity, getEntityClass(entityName), true);
	}
}
