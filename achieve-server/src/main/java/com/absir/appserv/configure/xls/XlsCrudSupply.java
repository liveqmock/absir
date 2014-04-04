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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.absir.appserv.crud.CrudSupply;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.jdbc.JdbcCondition;
import com.absir.bean.basis.Basis;
import com.absir.bean.basis.BeanDefine;
import com.absir.bean.config.IBeanDefineSupply;
import com.absir.bean.core.BeanFactoryImpl;
import com.absir.bean.inject.value.Bean;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelLang;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.value.JaEntity;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Bean
@Basis
public class XlsCrudSupply extends CrudSupply implements IBeanDefineSupply {

	/** Name_Map_Xls_Class */
	private static Map<String, Class<? extends XlsBase>> Name_Map_Xls_Class = new HashMap<String, Class<? extends XlsBase>>();

	/**
	 * @return
	 */
	public static Map<String, Class<? extends XlsBase>> getNameMapXlsClass() {
		return Name_Map_Xls_Class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.core.kernel.KernelList.Orderable#getOrder()
	 */
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return -256;
	}

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
		if (XlsBase.class.isAssignableFrom(beanType)) {
			JaEntity jaEntity = beanType.getAnnotation(JaEntity.class);
			if (jaEntity != null || beanType.getAnnotation(MaEntity.class) != null) {
				String entityName = beanType.getSimpleName();
				Name_Map_Xls_Class.put(entityName, (Class<? extends XlsBase>) beanType);
				if (jaEntity != null && jaEntity.permissions().length > 0) {
					SessionFactoryUtils.get().getNameMapPermissions().put(entityName, jaEntity.permissions());
				}

				return KernelLang.NULL_LIST_SET;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#getTransactionName()
	 */
	@Override
	public String getTransactionName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#getEntityClass(java.lang.String)
	 */
	@Override
	public Class<?> getEntityClass(String entityName) {
		// TODO Auto-generated method stub
		return Name_Map_Xls_Class.get(entityName);
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
		return XlsUtils.getXlsDao(Name_Map_Xls_Class.get(entityName)).getIdType();
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
		return ((XlsBase) entity).getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.CrudSupply#findAll(java.lang.String)
	 */
	@Override
	public Collection findAll(String entityName) {
		// TODO Auto-generated method stub
		return XlsUtils.getXlsDao(Name_Map_Xls_Class.get(entityName)).getAll();
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
		return XlsUtils.getXlsDao(Name_Map_Xls_Class.get(entityName)).get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#create(java.lang.String)
	 */
	@Override
	public Object create(String entityName) {
		// TODO Auto-generated method stub
		return KernelClass.newInstance(Name_Map_Xls_Class.get(entityName));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#evict(java.lang.Object)
	 */
	@Override
	public void evict(Object entity) {
		// TODO Auto-generated method stub
	}
}
