/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-24 上午10:42:15
 */
package com.absir.appserv.configure;

import org.hibernate.Session;

import com.absir.appserv.crud.CrudSupply;
import com.absir.appserv.feature.menu.value.MaSupply;
import com.absir.appserv.system.bean.JConfigure;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.bean.basis.Basis;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.orm.transaction.value.Transaction;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
@Bean
@Basis
@MaSupply(folder = "系统配置", name = "配置", method = "edit", icon = "configure")
public class JConfigureSupply extends CrudSupply<JConfigureBase> {

	/** ME */
	public static final JConfigureSupply ME = BeanFactoryUtils.get(JConfigureSupply.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.CrudSupply#put(java.lang.Class,
	 * java.lang.Class)
	 */
	@Override
	protected void put(Class<?> type, Class<?> beanType) {
		// TODO Auto-generated method stub
		JConfigureUtils.put((Class<? extends JConfigureBase>) type, (Class<? extends JConfigureBase>) beanType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#create(java.lang.String)
	 */
	@Override
	public Object create(String entityName) {
		// TODO Auto-generated method stub
		return JConfigureUtils.getConfigure(getEntityClass(entityName));
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
		((JConfigureBase) entity).merge();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.crud.ICrudSupply#deleteEntity(java.lang.String,
	 * java.lang.Object)
	 */
	@Transaction
	@Override
	public void deleteEntity(String entityName, Object entity) {
		// TODO Auto-generated method stub
		Session session = BeanDao.getSession();
		for (JConfigure configure : ((JConfigureBase) entity).fieldMapConfigure.values()) {
			session.delete(session.merge(configure));
		}
	}
}
