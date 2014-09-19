/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月30日 上午10:49:12
 */
package com.absir.appserv.developer;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;

import com.absir.appserv.configure.JConfigureBase;
import com.absir.appserv.configure.JConfigureSupply;
import com.absir.appserv.configure.JConfigureUtils;
import com.absir.appserv.feature.menu.MenuContextUtils;
import com.absir.appserv.feature.menu.OMenuBean;
import com.absir.appserv.support.developer.IRender;
import com.absir.appserv.support.developer.RenderUtils;
import com.absir.appserv.system.configure.JSiteConfigure;
import com.absir.appserv.system.helper.HelperLang;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Inject;
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
@Inject
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
	 * @return
	 */
	public static Locale locale() {
		return LangBundle.ME.isI18n() ? LangBundle.ME.getLocale() : getInput().getLocale();
	}

	/**
	 * @param name
	 * @return
	 */
	public static String lang(String name) {
		return getInput().getLang(name);
	}

	/**
	 * @param name
	 * @param echo
	 * @return
	 */
	public static String lang(String name, ServletRequest request) {
		return getInput(request).getLang(name);
	}

	/**
	 * @param lang
	 * @return
	 */
	public static String getLang(String lang) {
		return getLang(lang, true);
	}

	/**
	 * @param lang
	 * @return
	 */
	public static String getLang(String lang, boolean echo) {
		return getLang(HelperLang.getCaptionLang(lang), lang, echo);
	}

	/**
	 * @param lang
	 * @param value
	 * @param echo
	 * @return
	 */
	public static String getLang(String name, String lang, boolean echo) {
		LangBundle.ME.setResourceLang(name, lang);
		return getLangRequest(name, lang, echo);
	}

	/**
	 * @author absir
	 *
	 */
	public static interface IPagLang {

		/**
		 * @param transferredName
		 * @return
		 */
		public String getPagLang(String transferredName);
	}

	/** PAG_LANG */
	private static final IPagLang PAG_LANG = BeanFactoryUtils.get(IPagLang.class);

	/**
	 * @param name
	 * @param lang
	 * @param echo
	 * @return
	 */
	protected static String getLangRequest(String name, String lang, boolean echo) {
		if (LangBundle.ME.isI18n()) {
			name = KernelString.transferred(name);
			name = PAG_LANG == null ? "Pag.lang(" + name + ")" : PAG_LANG.getPagLang(name);
			return echo ? IRender.ME.echo(name) : name;

		} else {
			return echo ? lang : KernelString.transferred(lang);
		}
	}

	/** CONFIGURE */
	public static final JSiteConfigure CONFIGURE = JConfigureUtils.getConfigure(JSiteConfigure.class);

	/**
	 * @return
	 */
	public static JSiteConfigure configure() {
		return CONFIGURE;
	}

	/**
	 * @param name
	 * @return
	 */
	public static JConfigureBase configure(String name) {
		return (JConfigureBase) JConfigureSupply.ME.create(name);
	}

	/**
	 * @param cls
	 * @return
	 */
	public static <T extends JConfigureBase> T getConfigure(Class<T> cls) {
		return JConfigureUtils.getConfigure(cls);
	}

	/**
	 * @param name
	 * @return
	 */
	public static List<OMenuBean> menu(String name) {
		return MenuContextUtils.getMenuBeans(name);
	}

	/**
	 * @param include
	 * @param renders
	 * @return
	 * @throws IOException
	 */
	public static String getInclude(String include, Object... renders) throws IOException {
		return getInclude(include, include, renders);
	}

	/**
	 * @param include
	 * @param generate
	 * @param renders
	 * @return
	 * @throws IOException
	 */
	public static String getInclude(String include, String generate, Object... renders) throws IOException {
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
