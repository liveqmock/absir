/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-22 上午9:49:44
 */
package com.absir.orm.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;

import com.absir.bean.basis.Basis;
import com.absir.bean.basis.BeanFactory;
import com.absir.bean.config.IBeanFactoryStopping;
import com.absir.bean.inject.value.Value;
import com.absir.core.kernel.KernelString;
import com.absir.orm.hibernate.boost.EntityAssoc.AssocEntity;
import com.absir.orm.hibernate.boost.EntityAssoc.AssocField;
import com.absir.orm.hibernate.boost.EntityAssoc.EntityAssocEntity;
import com.absir.orm.value.JePermission;

/**
 * @author absir
 * 
 */
@Basis
public class SessionFactoryBean implements IBeanFactoryStopping {

	/** sessionFactory */
	private SessionFactoryImpl sessionFactory;

	/** nameMapSessionFactory */
	private Map<String, SessionFactoryImpl> nameMapSessionFactory = new HashMap<String, SessionFactoryImpl>();

	/** sessionFactoryMapName */
	private Map<SessionFactoryImpl, String> sessionFactoryMapName = new HashMap<SessionFactoryImpl, String>();

	@Value(value = "entity.assoc.depth")
	private int assocDepth = 8;

	/** nameMapPermissions */
	private Map<String, JePermission[]> nameMapPermissions = new HashMap<String, JePermission[]>();

	/** nameMapAssocEntities */
	private Map<String, List<AssocEntity>> nameMapAssocEntities = new HashMap<String, List<AssocEntity>>();

	/** nameMapAssocFields */
	private Map<String, List<AssocField>> nameMapAssocFields = new HashMap<String, List<AssocField>>();

	/** nameMapEntityAssocEntity */
	private Map<String, EntityAssocEntity> nameMapEntityAssocEntity = new HashMap<String, EntityAssocEntity>();

	/** entityNameMapJpaEntityName */
	private Map<String, String> entityNameMapJpaEntityName = new HashMap<String, String>();

	/** jpaEntityNameMapEntityClassFactory */
	private Map<String, Entry<Class<?>, SessionFactory>> jpaEntityNameMapEntityClassFactory = new HashMap<String, Entry<Class<?>, SessionFactory>>();

	/**
	 * @param name
	 * @param sessionFactory
	 */
	protected void setSessionFactory(String name, SessionFactoryImpl sessionFactory) {
		if (KernelString.isEmpty(name)) {
			this.sessionFactory = sessionFactory;

		} else {
			nameMapSessionFactory.put(name, sessionFactory);
			sessionFactoryMapName.put(sessionFactory, name);
		}
	}

	/**
	 * @return the sessionFactory
	 */
	public SessionFactoryImpl getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param name
	 * @return
	 */
	public SessionFactory getNameMapSessionFactory(String name) {
		return name == null ? sessionFactory : nameMapSessionFactory.get(name);
	}

	/**
	 * @param sessionFactory
	 * @return
	 */
	public String getSessionFactoryMapName(SessionFactory sessionFactory) {
		return sessionFactoryMapName.get(sessionFactory);
	}

	/**
	 * @return
	 */
	public Set<String> getNameMapSessionFactoryNames() {
		return nameMapSessionFactory.keySet();
	}

	/**
	 * @return the assocDepth
	 */
	public int getAssocDepth() {
		return assocDepth;
	}

	/**
	 * @return the nameMapPermissions
	 */
	public Map<String, JePermission[]> getNameMapPermissions() {
		return nameMapPermissions;
	}

	/**
	 * @return the nameMapAssocEntities
	 */
	public Map<String, List<AssocEntity>> getNameMapAssocEntities() {
		return nameMapAssocEntities;
	}

	/**
	 * @return the nameMapAssocFields
	 */
	public Map<String, List<AssocField>> getNameMapAssocFields() {
		return nameMapAssocFields;
	}

	/**
	 * @return the nameMapEntityAssocEntity
	 */
	public Map<String, EntityAssocEntity> getNameMapEntityAssocEntity() {
		return nameMapEntityAssocEntity;
	}

	/**
	 * @return the entityNameMapJpaEntityName
	 */
	public Map<String, String> getEntityNameMapJpaEntityName() {
		return entityNameMapJpaEntityName;
	}

	/**
	 * @return the jpaEntityNameMapEntityClassFactory
	 */
	public Map<String, Entry<Class<?>, SessionFactory>> getJpaEntityNameMapEntityClassFactory() {
		return jpaEntityNameMapEntityClassFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.core.kernel.KernelList.Orderable#getOrder()
	 */
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 255;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.config.IBeanFactoryStopping#stopping(com.absir.bean.basis
	 * .BeanFactory)
	 */
	@Override
	public void stopping(BeanFactory beanFactory) {
		// TODO Auto-generated method stub
	}
}
