/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月29日 下午5:15:42
 */
package com.absir.appserv.lang;

import java.io.File;

import com.absir.appserv.support.Developer;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanConfigImpl;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Stopping;
import com.absir.context.lang.LangBundle;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 *
 */
@Base(order = -1)
@Bean
public class LangBundler extends LangBundle {

	/**
	 * 内置国际化资源写入
	 */
	@Stopping
	public void stopping() {
		if (!resourceLangs.isEmpty()) {
			String var = locale.getLanguage();
			if (!KernelString.isEmpty(var)) {
				String resource = langResource + var;
				var = locale.getCountry();
				if (!KernelString.isEmpty(var)) {
					resource += '_' + var;
					var = locale.getVariant();
					if (!KernelString.isEmpty(var)) {
						resource += '_' + var;
					}
				}

				File file = new File(resource + "/general.properties");
				BeanConfigImpl.writeProperties(resourceLangs, file);
				Developer.doEntry(file);
			}
		}
	}
}
