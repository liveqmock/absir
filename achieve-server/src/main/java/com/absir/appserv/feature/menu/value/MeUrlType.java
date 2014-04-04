/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-10 下午11:58:02
 */
package com.absir.appserv.feature.menu.value;

import com.absir.appserv.feature.menu.MenuContextUtils;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
public enum MeUrlType {

	@JaLang("后台链接")
	ADMIN {
		@Override
		public String getRoute() {
			// TODO Auto-generated method stub
			return MenuContextUtils.getAdminRoute();
		}
	},

	@JaLang("前台链接")
	SITE {
		@Override
		public String getRoute() {
			// TODO Auto-generated method stub
			return MenuContextUtils.getSiteRoute();
		}
	},

	@JaLang("外部链接")
	NONE {
		@Override
		public String getRoute() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	;

	public abstract String getRoute();
}
