/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-26 下午4:54:27
 */
package com.absir.appserv.client.configure.xls;

import com.absir.appserv.configure.xls.XlsBaseUpdate;
import com.absir.appserv.game.value.IExp;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaEntity;
import com.absir.orm.value.JePermission;

/**
 * @author absir
 * 
 */
@JaEntity(permissions = JePermission.SELECT)
public class XCardExpDefine extends XlsBaseUpdate implements IExp {

	@JaLang("升级经验")
	private int exp;

	/**
	 * @return the exp
	 */
	public int getExp() {
		return exp;
	}
}
