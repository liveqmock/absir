/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月30日 上午10:49:12
 */
package com.absir.appserv.support.developer;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;

import com.absir.appserv.system.helper.HelperLang;
import com.absir.bean.basis.Configure;
import com.absir.context.lang.LangBundle;
import com.absir.core.dyna.DynaBinder;
import com.absir.core.kernel.KernelCollection;
import com.absir.core.kernel.KernelDyna;
import com.absir.core.kernel.KernelString;
import com.absir.server.in.Input;
import com.absir.server.on.OnPut;
import com.absir.servlet.InDispathFilter;

/**
 * @author absir
 *
 */
@Configure
public class Pag {

	/**
	 * @return
	 */
	public static Input getInput() {
		return OnPut.get().getInput();
	}

	/**
	 * @param request
	 * @return
	 */
	public static Input getInput(ServletRequest request) {
		return InDispathFilter.getInput(request);
	}

	/**
	 * @param name
	 * @return
	 */
	public String lang(String name) {
		return getInput().getLang(name);
	}

	/**
	 * @param name
	 * @param echo
	 * @return
	 */
	public String lang(String name, ServletRequest request) {
		return getInput(request).getLang(name);
	}

	/**
	 * @param lang
	 * @return
	 */
	public String getLang(String lang) {
		return getLang(lang, true);
	}

	/**
	 * @param lang
	 * @return
	 */
	public String getLang(String lang, boolean echo) {
		return getLang(HelperLang.getCaptionLang(lang), lang, echo);
	}

	/**
	 * @param lang
	 * @param value
	 * @param echo
	 * @return
	 */
	public String getLang(String name, String lang, boolean echo) {
		LangBundle.ME.setResourceLang(name, lang);
		return getLangRequest(name, lang, echo);
	}

	/**
	 * @param name
	 * @param lang
	 * @param echo
	 * @return
	 */
	protected String getLangRequest(String name, String lang, boolean echo) {
		if (LangBundle.ME.isI18n()) {
			name = "Pag.lang(" + KernelString.transferred(name) + ")";
			return echo ? IRender.ME.echo(name) : name;

		} else {
			return echo ? lang : KernelString.transferred(lang);
		}
	}

	/**
	 * @param include
	 * @param renders
	 * @return
	 * @throws IOException
	 */
	public String getInclude(String include, Object... renders) throws IOException {
		return getInclude(include, include, renders);
	}

	/**
	 * @param include
	 * @param generate
	 * @param renders
	 * @return
	 * @throws IOException
	 */
	public String getInclude(String include, String generate, Object... renders) throws IOException {
		RenderUtils.generate(include, generate, renders);
		return IRender.ME.include(include);
	}

	/**
	 * @param obj
	 * @return
	 */
	public static String value(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * @param obj
	 * @return
	 */
	public static String dateValue(Object obj) {
		return dateValue(obj, 0);
	}

	/**
	 * @param obj
	 * @param type
	 * @return
	 */
	public static String dateValue(Object obj, int type) {
		Date date = KernelDyna.toDate(obj);
		if (type >= 0) {
			if (date == null) {
				date = new Date();
			}

		} else {
			type = -type - 1;
		}

		return value(KernelDyna.toString(date, type));
	}

	/**
	 * @param obj
	 * @return
	 */
	public static String enumValue(Object obj) {
		if (obj != null) {
			if (obj instanceof Enum) {
				return ((Enum<?>) obj).name();
			}
		}

		return value(obj);
	}

	/**
	 * @param obj
	 * @return
	 */
	public static String paramsValue(Object obj) {
		if (obj != null) {
			if (obj.getClass().isArray()) {
				return KernelString.implode(DynaBinder.to(obj, Object[].class), ",");
			}
		}

		return value(obj);
	}

	/**
	 * @param obj
	 * @return
	 */
	public static Map<?, ?> mapValue(Object obj) {
		if (obj != null) {
			if (obj instanceof Collection) {
				return KernelCollection.toMap((Collection<?>) obj);
			}
		}

		return null;
	}
}
