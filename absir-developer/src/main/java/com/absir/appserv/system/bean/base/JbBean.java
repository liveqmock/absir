/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-3-8 下午12:43:09
 */
package com.absir.appserv.system.bean.base;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@MappedSuperclass
public class JbBean extends JbBase {

	@JaLang("纪录编号")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
}
