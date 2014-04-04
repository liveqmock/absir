/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-21 上午11:34:37
 */
package com.absir.data.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author absir
 * 
 */
@Entity
public class JUser {

	@Id
	@GeneratedValue
	private long id;

	private String name;

	private String aname;

	private String bname;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
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

	/**
	 * @return the aname
	 */
	public String getAname() {
		return aname;
	}

	/**
	 * @param aname
	 *            the aname to set
	 */
	public void setAname(String aname) {
		this.aname = aname;
	}

	/**
	 * @return the bname
	 */
	public String getBname() {
		return bname;
	}

	/**
	 * @param bname
	 *            the bname to set
	 */
	public void setBname(String bname) {
		this.bname = bname;
	}
}
