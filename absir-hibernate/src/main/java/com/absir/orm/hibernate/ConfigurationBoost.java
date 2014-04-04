/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-14 下午7:13:59
 */
package com.absir.orm.hibernate;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Settings;
import org.hibernate.cfg.SettingsFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.BasicType;
import org.hibernate.type.TypeResolverLocal;

import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.core.kernel.KernelObject;

/**
 * @author absir
 * 
 */
@SuppressWarnings("serial")
public class ConfigurationBoost extends Configuration {

	/** sessionFactoryBoost */
	protected static SessionFactoryBoost sessionFactoryBoost;

	/**
	 * 
	 */
	public ConfigurationBoost() {
	}

	/**
	 * @param settingsFactory
	 */
	public ConfigurationBoost(SettingsFactory settingsFactory) {
		super(settingsFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.cfg.Configuration#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		if (sessionFactoryBoost.getBasicTypes() != null && sessionFactoryBoost.getBasicTypes().length > 0) {
			TypeResolverLocal typeResolverLocal = new TypeResolverLocal(getTypeResolver());
			KernelObject.declaredSet(this, "typeResolver", typeResolverLocal);
			try {
				for (BasicType basicType : sessionFactoryBoost.getBasicTypes()) {
					if (BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= 0) {
						System.out.println("register basic type, " + basicType.getClass());
					}

					typeResolverLocal.registerTypeOverride(basicType);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.cfg.Configuration#buildSettings(java.util.Properties,
	 * org.hibernate.service.ServiceRegistry)
	 */
	@Override
	public Settings buildSettings(Properties props, ServiceRegistry serviceRegistry) throws HibernateException {
		sessionFactoryBoost.beforeBuildConfiguration(this);
		return super.buildSettings(props, serviceRegistry);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.cfg.Configuration#buildSessionFactory(org.hibernate
	 * .service.ServiceRegistry)
	 */
	@Override
	public SessionFactory buildSessionFactory(ServiceRegistry serviceRegistry) throws HibernateException {
		SessionFactory sessionFactory = super.buildSessionFactory(serviceRegistry);
		sessionFactoryBoost.afterBuildConfiguration(this, (SessionFactoryImpl) sessionFactory);
		return sessionFactory;
	}
}
