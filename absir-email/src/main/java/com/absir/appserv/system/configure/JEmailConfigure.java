/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月27日 下午4:53:49
 */
package com.absir.appserv.system.configure;

import com.absir.appserv.configure.JConfigureBase;
import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.value.ICrudBean;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.service.utils.EmailServiceUtils;

/**
 * @author absir
 *
 */
@MaEntity(parent = { @MaMenu("接口配置") }, name = "邮件")
public class JEmailConfigure extends JConfigureBase implements ICrudBean {

	@JaLang("发送服务器")
	private String smtp = "smtp.qq.com";

	@JaLang("端口")
	private int port = 25;

	@JaLang("安全连接")
	private boolean starttls;

	@JaLang(value = "发送名", tag = "fromName")
	private String from;

	@JaLang("匿名")
	private boolean anyone;

	@JaLang("用户名")
	private String username;

	@JaLang("密码")
	private String password;

	/**
	 * @return the smtp
	 */
	public String getSmtp() {
		return smtp;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the starttls
	 */
	public boolean isStarttls() {
		return starttls;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @return the anyone
	 */
	public boolean isAnyone() {
		return anyone;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.crud.value.ICrudBean#proccessCrud(com.absir.appserv
	 * .system.bean.value.JaCrud.Crud, com.absir.appserv.crud.CrudHandler)
	 */
	@Override
	public void proccessCrud(Crud crud, CrudHandler handler) {
		// TODO Auto-generated method stub
		if (crud == Crud.UPDATE) {
			EmailServiceUtils.ME.clearSession();
		}
	}
}
