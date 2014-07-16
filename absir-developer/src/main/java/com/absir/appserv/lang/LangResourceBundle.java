/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月16日 上午10:38:20
 */
package com.absir.appserv.lang;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanConfigImpl;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.Value;

/**
 * @author absir
 *
 */
@Base
@Bean
public class LangResourceBundle {

	@Value("lang.baseName")
	protected String baseName = "message";

	/** local */
	private Locale locale;

	/** defaultResourceBundle */
	ResourceBundle defaultResourceBundle;

	/** localeMapResourceBunlde */
	private Map<Locale, ResourceBundle> localeMapResourceBunlde = new HashMap<Locale, ResourceBundle>();

	/**
	 * 
	 */
	@Inject
	protected void initBundle() {
		BeanConfigImpl
		String language = BeanFactoryUtils.getBeanConfigValue("local.language", String.class);
		String country = BeanFactoryUtils.getBeanConfigValue("local.country", String.class);
		String variant = BeanFactoryUtils.getBeanConfigValue("local.variant", String.class);
		locale = language == null || country == null ? Locale.getDefault() : new Locale(language, country, variant);
		defaultResourceBundle = ResourceBundle.getBundle(baseName, locale);
		defaultResourceBundle.getBundle(baseName);
	}

	/**
	 * @param locale
	 * @return
	 */
	public ResourceBundle getResourceBundle(String baseName, Locale locale) {
		return ResourceBundle.getBundle(baseName, locale);
	}
}
