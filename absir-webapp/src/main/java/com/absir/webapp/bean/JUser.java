/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-23 上午11:22:50
 */
package com.absir.webapp.bean;

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
	private Long id;

	private String name;

	private String password;

	private String slat;

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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the slat
	 */
	public String getSlat() {
		return slat;
	}

	/**
	 * @param slat
	 *            the slat to set
	 */
	public void setSlat(String slat) {
		this.slat = slat;
	}
}
