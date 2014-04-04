/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-20 下午2:41:30
 */
package com.absir.appserv.system.bean.base;

import javax.persistence.MappedSuperclass;

import com.absir.appserv.system.bean.JEmbedSS;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@MappedSuperclass
public class JbBeanSS extends JbBase {

	@JaLang("编号")
	private JEmbedSS id;

	/**
	 * @return the id
	 */
	public JEmbedSS getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(JEmbedSS id) {
		this.id = id;
	}

}
