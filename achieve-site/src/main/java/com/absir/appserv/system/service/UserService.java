/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月16日 下午12:50:48
 */
package com.absir.appserv.system.service;

import com.absir.appserv.system.bean.base.IUser;
import com.absir.appserv.system.crud.PasswordCrudFactory;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.orm.transaction.value.Transaction;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;

/**
 * @author absir
 *
 */
@Bean
public abstract class UserService {

	/** ME */
	public static final UserService ME = BeanFactoryUtils.get(UserService.class);

	/**
	 * @param user
	 * @return
	 */
	public static String getPasswordEntry(String password, IUser user) {
		return PasswordCrudFactory.getPasswordEncrypt(password, user.getSalt());
	}

	/**
	 * @param user
	 */
	@Transaction(rollback = Throwable.class)
	public void register(IUser user) {
		user.setPassword(getPasswordEntry(user.getPassword(), user));
		BeanDao.getSession().persist(user);
	}

	/**
	 * @param user
	 * @param password
	 * @param newPassword
	 */
	@Transaction(rollback = Throwable.class)
	public void setPassword(IUser user, String password, String newPassword) {
		if (password != null) {
			if (!getPasswordEntry(password, user).equals(password)) {
				throw new ServerException(ServerStatus.ON_DENIED);
			}
		}

		user.setPassword(getPasswordEntry(newPassword, user));
		BeanDao.getSession().merge(user);
	}
}
