/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-6 下午5:34:44
 */
package com.absir.appserv.system.task;

import java.util.HashSet;

import org.hibernate.Session;

import com.absir.appserv.support.Developer;
import com.absir.appserv.system.bean.JUser;
import com.absir.appserv.system.bean.JUserRole;
import com.absir.appserv.system.bean.value.JeUserType;
import com.absir.appserv.system.crud.PasswordCrudFactory;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.JUserDao;
import com.absir.appserv.system.service.utils.CrudServiceUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Started;
import com.absir.orm.transaction.value.Transaction;

/**
 * @author absir
 * 
 */
@Bean
public class InitialTask {

	/**
	 * 
	 */
	@Started
	@Transaction(rollback = Throwable.class)
	protected void started() {
		if (Developer.isDeveloper()) {
			JUserRole userRole = inserUserRole(1L, "系统管理员");
			inserUserRole(2L, "管理员");
			insertUser("absir", "developer", userRole, true);
			insertUser("admin", "admin888", userRole, false);
		}
	}

	/**
	 * @param id
	 * @param rolename
	 * @return
	 */
	private JUserRole inserUserRole(Long id, String rolename) {
		Session session = BeanDao.getSession();
		JUserRole userRole = BeanDao.get(session, JUserRole.class, id);
		if (userRole == null) {
			userRole = new JUserRole();
			userRole.setId(id);
			userRole.setRolename(rolename);
			userRole = (JUserRole) session.merge(userRole);
		}

		return userRole;
	}

	/**
	 * @param username
	 * @param password
	 * @param userRole
	 * @param developer
	 */
	private void insertUser(String username, String password, JUserRole userRole, boolean developer) {
		JUser user = JUserDao.ME.findByUsername(username);
		if (user == null) {
			user = new JUser();
			user.setUsername(username);
			user.setSalt(Integer.toHexString(password.hashCode()));
			user.setPassword(PasswordCrudFactory.getPasswordEncrypt(password, user.getSalt()));
			user.setActivation(true);
			user.setUserType(JeUserType.USER_ADMIN);
			if (user.getUserRoles() == null) {
				user.setUserRoles(new HashSet<JUserRole>());
			}

			user.getUserRoles().add(userRole);
			user.setDeveloper(developer);
			CrudServiceUtils.merge("JUser", null, user, true, null, null);
		}
	}
}
