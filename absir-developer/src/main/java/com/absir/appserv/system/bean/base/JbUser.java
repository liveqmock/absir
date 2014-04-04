/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-26 下午3:49:42
 */
package com.absir.appserv.system.bean.base;

import javax.persistence.MappedSuperclass;

import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JeEditable;
import com.absir.orm.value.JaField;

/**
 * @author absir
 * 
 */
@MappedSuperclass
public class JbUser extends JbBean {

	@JaLang("开发者")
	@JaEdit(editable = JeEditable.DISABLE)
	@JaField(assocClasses = JbPermission.class, referenceEntityName = "developerAssocDao")
	private boolean developer;

	/**
	 * @return the developer
	 */
	public boolean isDeveloper() {
		return developer;
	}

	/**
	 * @param developer
	 *            the developer to set
	 */
	public void setDeveloper(boolean developer) {
		this.developer = developer;
	}
}
