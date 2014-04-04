/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-3-8 下午12:43:09
 */
package com.absir.appserv.system.bean.base;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.absir.appserv.system.bean.proxy.JiPass;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@MappedSuperclass
public class JbVerifier extends JbBase implements JiPass {

	@JaLang("验证主键")
	@Id
	private String id;

	@JaLang("过期时间")
	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	private long passTime;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.proxy.JiPass#setPassTime(long)
	 */
	@Override
	public void setPassTime(long passTime) {
		// TODO Auto-generated method stub
		this.passTime = passTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.proxy.JiPass#getPassTime()
	 */
	@Override
	public long getPassTime() {
		// TODO Auto-generated method stub
		return passTime;
	}

}