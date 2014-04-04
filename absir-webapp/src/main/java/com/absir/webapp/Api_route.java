/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-16 下午5:27:31
 */
package com.absir.webapp;

import java.io.IOException;
import java.util.List;

import org.hibernate.Session;

import com.absir.async.value.Async;
import com.absir.bean.inject.value.Inject;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.transaction.value.Transaction;
import com.absir.server.value.After;
import com.absir.server.value.Body;
import com.absir.server.value.Mapping;
import com.absir.server.value.Server;
import com.absir.server.value.UrlDecode;
import com.absir.servlet.InputRequest;
import com.absir.webapp.bean.JUser;
import com.absir.webapp.service.JUserService;

/**
 * @author absir
 * 
 */
@Mapping("/admin")
@Server
public class Api_route {

	@Inject
	JUserService userService;

	@Body
	public String route() {
		return "133";
	}
	
	public String route(String name, String title, String title2, InputRequest inputRequest) throws IOException {
		return name + title;
	}

	@UrlDecode
	public String index(String name, String title, String title2, InputRequest inputRequest) throws IOException {
		return name;
	}

	@UrlDecode
	@Body
	@Transaction
	public String insert(String name, String password) {
		JUser user = new JUser();
		user.setName(name);
		user.setPassword(password);
		Session session = SessionFactoryUtils.get().getSessionFactory().getCurrentSession();
		session.save(user);
		// userService.test();
		return name + ":insert!!!" + password;
	}

	@SuppressWarnings("unchecked")
	@Async
	@Transaction(readOnly = true)
	@After
	protected void test() {
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
