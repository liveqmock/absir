/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-20 下午7:02:36
 */
package com.absir.data;

import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.hibernate.Session;

import com.absir.bean.core.BeanFactoryProvider;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.data.domain.JUser;
import com.absir.data.domain.JUserRef;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.transaction.value.Transaction;

/**
 * @author absir
 * 
 */
public class BoneCP {

	public interface Test {

		public final String name = "123";
	}

	@Bean
	public static class BoneService {

		@Inject
		public static BoneService Instance;

		@Transaction
		public void insertJUserRef() {
			Session session = SessionFactoryUtils.get().getSessionFactory().getCurrentSession();
			JUser user = (JUser) session.get(JUser.class, 1L);
			if (user == null) {
				user = new JUser();
				user.setId(1L);
				user.setName("test");
				session.merge(user);
			}

			JUserRef userRef = new JUserRef();
			userRef.setId(1L);
			userRef.setUser(user);

			List<JUser> users = new ArrayList<JUser>();
			users.add(user);
			userRef.setUsers(users);
			session.merge(userRef);
		}

		@Transaction
		public void removeJUser() {
			Session session = SessionFactoryUtils.get().getSessionFactory().getCurrentSession();
			JUser user = (JUser) session.get(JUser.class, 1L);
			if (user != null) {
				session.delete(user);
			}
		}

		@Transaction(readOnly = true)
		public JUserRef printJUserRef() {
			Session session = SessionFactoryUtils.get().getSessionFactory().getCurrentSession();
			JUserRef userRef = (JUserRef) session.get(JUserRef.class, 1L);
			System.out.println(userRef);
			return userRef;
		}
		
		@Transaction
		public void testUser() {
			Session session = SessionFactoryUtils.get().getSessionFactory().getCurrentSession();
			JUserRef user = printJUserRef();
			
			user.setName("12333");
			
			session.merge(user);
		}

	}

	/**
	 * @param args
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 * @throws SystemException
	 * @throws NotSupportedException
	 * @throws HeuristicRollbackException
	 * @throws HeuristicMixedException
	 * @throws RollbackException
	 * @throws IllegalStateException
	 * @throws SecurityException
	 * @throws NamingException
	 */
	public static void main(String... args) throws NotFoundException, CannotCompileException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException, NamingException {
		
		//System.out.println(KernelReflect.declaredMethod(BoneService.class, "test"));
		//
		// for (Method method : BoneService.class.getDeclaredMethods()) {
		// System.out.println(method);
		// }
		//
		// Map<String, String> map = new TreeMap<String, String>();
		//
		// map.put("1", "v");
		// map.put("2", "v");
		// map.put("3", "v");
		// map.put("4", "v");
		// map.put("5", "v");
		//
		// Object value = map.values();
		// System.out.println(value);
		//
		// value = map.values();
		// System.out.println(value);
		
		BeanFactoryProvider beanFactoryProvider = new BeanFactoryProvider(null, null, null);
		beanFactoryProvider.scan(null, null);
		beanFactoryProvider.started();

		//BoneService.Instance.insertJUserRef();
		//BoneService.Instance.removeJUser();
		BoneService.Instance.testUser();

		beanFactoryProvider.stopping();
	}
}
