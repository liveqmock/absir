/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-3-8 下午12:43:09
 */
package com.absir.appserv.system.bean.base;

import javax.persistence.MappedSuperclass;

import com.absir.appserv.system.bean.proxy.JiUser;
import com.absir.appserv.system.bean.value.JaCrud;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JeEditable;

@MappedSuperclass
public class JbStragety extends JbAssoc implements JiUser {

	@JaLang("关联用户")
	@JaEdit(editable = JeEditable.OPTIONAL)
	@JaCrud(value = "userIdCrudFactory", cruds = JaCrud.Crud.CREATE)
	private Long userId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.proxy.JpUser#getUserId()
	 */
	@Override
	public Long getUserId() {
		// TODO Auto-generated method stub
		return userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.proxy.JpUser#setUserId(java.lang.Long)
	 */
	@Override
	public void setUserId(Long userId) {
		// TODO Auto-generated method stub
		this.userId = userId;
	}
}
