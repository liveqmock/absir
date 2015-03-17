/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-3-10 下午11:30:34
 */
package com.absir.appserv.system.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.IUser;
import com.absir.appserv.system.bean.base.JbUser;
import com.absir.appserv.system.bean.proxy.JpMeta;
import com.absir.appserv.system.bean.proxy.JpUserBase;
import com.absir.appserv.system.bean.value.JaCrud;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JeEditable;
import com.absir.appserv.system.crud.PasswordCrudFactory;
import com.absir.property.value.Prop;
import com.absir.validator.value.Email;
import com.absir.validator.value.NotEmpty;

/**
 * @author absir
 * 
 */
@SuppressWarnings("serial")
@MaEntity(parent = { @MaMenu("用户管理") }, name = "用户")
@Entity
public class JUser extends JbUser implements IUser, JpUserBase, JpMeta, Serializable {

	@JaLang("密码")
	@Prop(include = 1)
	@JaEdit(editable = JeEditable.OPTIONAL, types = "password")
	@JaCrud(cruds = { Crud.CREATE, Crud.UPDATE }, factory = PasswordCrudFactory.class)
	@Column(columnDefinition = "char(32)")
	private String password;

	@JaLang(value = "加密", tag = "encryption")
	@Prop(include = 99)
	@JaEdit(editable = JeEditable.DISABLE)
	private String salt;

	@JaLang("最后登录")
	@Prop(include = 99)
	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	private long lastLogin;

	@JaLang("错误登录")
	@Prop(include = 99)
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private int errorLogin;

	@JaLang("最后错误登录")
	@Prop(include = 99)
	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	private long lastErrorLogin;

	@JaLang("邮箱")
	@Prop(include = 1)
	@Email
	@NotEmpty
	private String email;

	@JaLang("手机")
	@Prop(include = 1)
	private String mobile;

	@JaLang("沉默")
	@Transient
	private transient boolean slient;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.proxy.Proxies.JpUserBase#getUserId()
	 */
	@Override
	public Long getUserId() {
		// TODO Auto-generated method stub
		return getId();
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
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt
	 *            the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * @return the lastLogin
	 */
	public long getLastLogin() {
		return lastLogin;
	}

	/**
	 * @param lastLogin
	 *            the lastLogin to set
	 */
	public void setLastLogin(long lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * @return the errorLogin
	 */
	public int getErrorLogin() {
		return errorLogin;
	}

	/**
	 * @param errorLogin
	 *            the errorLogin to set
	 */
	public void setErrorLogin(int errorLogin) {
		this.errorLogin = errorLogin;
	}

	/**
	 * @return the lastErrorLogin
	 */
	public long getLastErrorLogin() {
		return lastErrorLogin;
	}

	/**
	 * @param lastErrorLogin
	 *            the lastErrorLogin to set
	 */
	public void setLastErrorLogin(long lastErrorLogin) {
		this.lastErrorLogin = lastErrorLogin;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the slient
	 */
	public boolean isSlient() {
		return slient;
	}

	/**
	 * @param slient
	 *            the slient to set
	 */
	public void setSlient(boolean slient) {
		this.slient = slient;
	}
}
