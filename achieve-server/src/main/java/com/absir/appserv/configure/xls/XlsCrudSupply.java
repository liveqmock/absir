/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-23 上午10:57:02
 */
package com.absir.appserv.configure.xls;

import java.io.Serializable;
import java.util.Collection;

import com.absir.appserv.crud.CrudSupply;
import com.absir.appserv.feature.menu.value.MaSupply;
import com.absir.appserv.jdbc.JdbcCondition;
import com.absir.bean.basis.Basis;
import com.absir.bean.inject.value.Bean;
import com.absir.core.base.IBase;
import com.absir.core.kernel.KernelClass;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes" })
@Bean
@Basis
@MaSupply
public class XlsCrudSupply extends CrudSupply<XlsBase> {

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
		return XlsUtils.getXlsDao(getEntityClass(entityName)).getIdType();
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
		return XlsUtils.getXlsDao(getEntityClass(entityName)).getAll();
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
		return XlsUtils.getXlsDao(getEntityClass(entityName)).get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#create(java.lang.String)
	 */
	@Override
	public Object create(String entityName) {
		// TODO Auto-generated method stub
		return KernelClass.newInstance(getEntityClass(entityName));
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
	}
}
