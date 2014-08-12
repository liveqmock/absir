/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-7 下午5:09:39
 */
package com.absir.appserv.client.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.absir.appserv.system.bean.JEmbedLL;

/**
 * @author absir
 * 
 */
@Entity
public class JPlayerAnswer extends JbAnswer {

	@Id
	private JEmbedLL id;

	/**
	 * @return the id
	 */
	public JEmbedLL getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(JEmbedLL id) {
		this.id = id;
	}
}
