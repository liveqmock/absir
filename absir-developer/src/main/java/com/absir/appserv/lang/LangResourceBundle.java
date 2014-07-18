/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月16日 上午10:38:20
 */
package com.absir.appserv.lang;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.absir.bean.basis.Base;
import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanConfigImpl;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.Value;
import com.absir.context.core.ContextUtils;
import com.absir.context.schedule.cron.CronFixDelayRunable;

/**
 * @author absir
 *
 */
@SuppressWarnings("unchecked")
@Base
@Bean
public class LangResourceBundle {

	/** langResource */
	@Value(value = "lang.resouce", defaultValue = "${classPath}lang/")
	protected String langResource;

	/** reloadTime */
	protected long reloadTime;

	/** reloadRunable */
	protected CronFixDelayRunable reloadRunable;

	/** local */
	protected Locale locale;

	/** resourceBundle */
	protected Map<String, String> resourceBundle;

	/** localeMapResourceBunlde */
	protected Map<Locale, Map<String, String>> localeMapResourceBunlde = new HashMap<Locale, Map<String, String>>();

	/**
	 * @param reloadTime
	 *            the reloadTime to set
	 */
	public void setReloadTime(long reloadTime) {
		if (this.reloadTime != reloadTime) {
			this.reloadTime = reloadTime;
			if (reloadTime <= 0) {
				if (reloadRunable != null) {
					reloadRunable.setFixDelay(0);
					reloadRunable = null;
				}

			} else {
				reloadRunable = reloadRunable == null ? new CronFixDelayRunable(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						resourceBundle = null;
						localeMapResourceBunlde.clear();
					}

				}, reloadTime) : reloadRunable.transformCronFixDelayRunable(reloadTime);
				ContextUtils.getScheduleFactory().addScheduleRunable(reloadRunable);
			}
		}
	}

	/**
	 * 
	 */
	@Inject
	protected void initBundle() {
		Long reload = BeanFactoryUtils.getBeanConfigValue("local.reload", Long.class);
		setReloadTime(reload == null ? BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= 0 ? -1 : 0 : reload);
		String language = BeanFactoryUtils.getBeanConfigValue("local.language", String.class);
		String country = BeanFactoryUtils.getBeanConfigValue("local.country", String.class);
		String variant = BeanFactoryUtils.getBeanConfigValue("local.variant", String.class);
		locale = language == null || country == null ? Locale.getDefault() : new Locale(language, country, variant);
	}

	/**
	 * @return
	 */
	public Map<String, String> getResourceBundle() {
		if (reloadTime < 0) {
			return getResourceBundle(locale);
		}

		if (resourceBundle == null) {
			resourceBundle = getResourceBundle(locale);
		}

		return resourceBundle;
	}

	/**
	 * @param locale
	 * @return
	 */
	public Map<String, String> getResourceBundle(Locale locale) {
		Map<String, String> resourceBundle = localeMapResourceBunlde.get(locale);
		if (resourceBundle != null) {
			return resourceBundle;
		}

		String var = locale.getLanguage();
		if (var != null) {
			String resource = langResource + var;
			File resourceFile = new File(resource);
			if (resourceFile.exists()) {
				resourceBundle = new HashMap<String, String>();
				BeanConfigImpl.readDirProperties(null, (Map<String, Object>) (Object) resourceBundle, resourceFile, null);
				if (reloadTime >= 0) {
					localeMapResourceBunlde.put(locale, resourceBundle);
				}

				var = locale.getCountry();
				if (var != null) {
					resource = langResource + var;
					resourceFile = new File(resource);
					if (resourceFile.exists()) {
						BeanConfigImpl.readDirProperties(null, (Map<String, Object>) (Object) resourceBundle, resourceFile, null);
						var = locale.getVariant();
						if (var != null) {
							resource = langResource + var;
							resourceFile = new File(resource);
							if (resourceFile.exists()) {
								BeanConfigImpl.readDirProperties(null, (Map<String, Object>) (Object) resourceBundle, resourceFile, null);
							}
						}
					}
				}

				return resourceBundle;
			}
		}

		return getResourceBundle();
	}

	/**
	 * 
	 */
	public void clearResouceBundle() {
		resourceBundle = null;
		localeMapResourceBunlde.clear();
	}

	/**
	 * @param lang
	 * @param locale
	 * @return
	 */
	public String getLangResource(String lang, Locale locale) {
		return getLangResource(lang, getResourceBundle(locale), locale);
	}

	/**
	 * @param lang
	 * @param locale
	 * @return
	 */
	public String getLangResource(String lang, Map<String, String> resouceBunlde, Locale locale) {
		String value = resouceBunlde.get(lang);
		if (value == null) {
			value = getResourceBundle().get(lang);
			if (value == null) {
				value = lang;
			}
		}

		return value;
	}
}
