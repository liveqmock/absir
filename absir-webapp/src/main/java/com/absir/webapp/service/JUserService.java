/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-4 下午5:12:23
 */
package com.absir.webapp.service;

import java.util.List;

import org.hibernate.Session;

import com.absir.bean.inject.value.Bean;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.transaction.value.Transaction;
import com.absir.webapp.bean.JUser;

/**
 * @author absir
 * 
 */
@Bean
public class JUserService {

	@SuppressWarnings("unchecked")
	@Transaction(readOnly = true)
	public void test() {
		Session session = SessionFactoryUtils.get().getSessionFactory().getCurrentSession();
		List<JUser> users = session.createQuery("SELECT o FROM JUser o").list();
		for (JUser user : users) {
			System.out.println("======user======");
			System.out.println("id=" + user.getId());
			System.out.println("name=" + user.getName());
			System.out.println("password=" + user.getPassword());
		}
	}
}
