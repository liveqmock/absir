/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月11日 上午10:12:34
 */
package com.absir.system.test.aop;

import org.junit.Test;

import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.value.ICrudBean;
import com.absir.appserv.lang.ILangBase;
import com.absir.appserv.lang.LangBundleImpl;
import com.absir.appserv.lang.value.Langs;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.system.test.AbstractTestInject;

/**
 * @author absir
 *
 */
public class LangTest extends AbstractTestInject {

	public static class LangBean extends JbBean {

		public String name;

		/**
		 * @return the name
		 */
		@Langs
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

	}

	@Test
	public void test() throws Throwable {
		try {
			LangBean langBean = new LangBean();
			langBean.setId(3L);
			langBean.name = "测试";
			langBean = LangBundleImpl.ME.getLangProxy("LangBean", langBean);

			CrudHandler crudHandler = new CrudHandler(null, null, null, null, langBean) {
			};
			((ICrudBean) langBean).proccessCrud(Crud.CREATE, crudHandler);

			((ILangBase) langBean).setLang("name", 33, "test");
			((ICrudBean) langBean).proccessCrud(Crud.UPDATE, crudHandler);

			System.out.println(((ILangBase) langBean).getLang("name", 33, String.class));

			langBean = new LangBean();
			langBean.setId(1L);
			langBean.name = "测试";
			langBean = LangBundleImpl.ME.getLangProxy("LangBean", langBean);

			System.out.println(langBean.getName());
			System.out.println(((ILangBase) langBean).getLang("name", 33, String.class));

		} catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
