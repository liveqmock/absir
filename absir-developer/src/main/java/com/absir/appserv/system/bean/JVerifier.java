/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月29日 下午3:37:43
 */
package com.absir.appserv.system.bean;

import javax.persistence.Entity;

import com.absir.appserv.system.bean.base.JbVerifier;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 *
 */
@Entity
public class JVerifier extends JbVerifier {

	/** tag */
	@JaLang("标示")
	private String tag;

	/** value */
	@JaLang("内容")
	private String value;

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
