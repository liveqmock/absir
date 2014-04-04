/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-13 上午9:47:05
 */
package com.absir.data.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.absir.orm.value.JaColum;

/**
 * @author absir
 * 
 */
@Entity
public class JUserRef {

	@Id
	@GeneratedValue
	private long id;

	public String name;

	@NotFound(action = NotFoundAction.IGNORE)
	@JaColum(foreignKey = false)
	@ManyToOne
	private JUser user;

	@NotFound(action = NotFoundAction.IGNORE)
	@JaColum(foreignKey = false)
	@ManyToMany(fetch = FetchType.EAGER)
	private List<JUser> users;

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
	 * @return the user
	 */
	public JUser getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(JUser user) {
		this.user = user;
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
	 * @return the users
	 */
	public List<JUser> getUsers() {
		return users;
	}

	/**
	 * @param users
	 *            the users to set
	 */
	public void setUsers(List<JUser> users) {
		this.users = users;
	}

}
