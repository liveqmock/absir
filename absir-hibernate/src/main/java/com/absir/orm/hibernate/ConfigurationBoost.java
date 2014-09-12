/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-14 下午7:13:59
 */
package com.absir.orm.hibernate;

import java.util.Locale;
import java.util.Map;
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
import com.absir.context.lang.LangBundle;
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

	/** boostLocale */
	private boolean boostLocale;

	/**
	 * 添加本地化基础实体
	 */
	public void boostLocale() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=\"1.0\"?>\r\n");
		stringBuilder.append("<!DOCTYPE hibernate-mapping PUBLIC\r\n");
		stringBuilder.append("\"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"\r\n");
		stringBuilder.append("\"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">\r\n");
		stringBuilder.append("<hibernate-mapping>\r\n");
		stringBuilder.append("<class entity-name=\"JLocale\">\r\n");
		stringBuilder.append("<composite-id mapped=\"true\">\r\n");
		stringBuilder.append("<key-property name=\"entity\" type=\"java.lang.String\" length=\"63\"></key-property>\r\n");
		stringBuilder.append("<key-property name=\"id\" type=\"java.lang.String\" length=\"255\"></key-property>\r\n");
		stringBuilder.append("<key-property name=\"name\" type=\"java.lang.String\" length=\"63\"></key-property>\r\n");
		stringBuilder.append("<generator class=\"assigned\"></generator>\r\n");
		stringBuilder.append("</composite-id>\r\n");
		Map<Integer, Locale> codeMaplocale = LangBundle.ME.getCodeMaplocale();
		if (codeMaplocale != null) {
			for (Integer code : codeMaplocale.keySet()) {
				stringBuilder.append("<property name=\"_" + code + "\" type=\"java.lang.String\" length=\"65536\" />\r\n");
			}
		}

		stringBuilder.append("</class>\r\n");
		stringBuilder.append("</hibernate-mapping>");
		addXML(stringBuilder.toString());
		boostLocale = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.cfg.Configuration#buildSettings(java.util.Properties,
	 * org.hibernate.service.ServiceRegistry)
	 */
	@Override
	public Settings buildSettings(Properties props, ServiceRegistry serviceRegistry) throws HibernateException {
		sessionFactoryBoost.beforeBuildConfiguration(this, boostLocale);
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
