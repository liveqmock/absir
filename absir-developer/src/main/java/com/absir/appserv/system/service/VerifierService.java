/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月29日 下午4:46:32
 */
package com.absir.appserv.system.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.absir.appserv.system.bean.JVerifier;
import com.absir.appserv.system.bean.base.JbVerifier;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.utils.QueryDaoUtils;
import com.absir.appserv.system.helper.HelperRandom;
import com.absir.bean.basis.Base;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Started;
import com.absir.context.core.ContextUtils;
import com.absir.context.schedule.cron.CronFixDelayRunable;
import com.absir.core.kernel.KernelCollection;
import com.absir.orm.hibernate.SessionFactoryBean;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.transaction.value.Transaction;

/**
 * @author absir
 *
 */
@SuppressWarnings("unchecked")
@Base
@Bean
public class VerifierService {

	/** ME */
	public static final VerifierService ME = BeanFactoryUtils.get(VerifierService.class);

	/**
	 * @param dist
	 * @return
	 */
	public static String randVerifierId(Object dist) {
		return HelperRandom.randSecendId(ContextUtils.getContextTime(), 8, dist.hashCode());
	}

	/**
	 * 添加验证
	 * 
	 * @param dist
	 * @param tag
	 * @param value
	 * @param lifeTime
	 */
	public void persistVerifier(Object dist, String tag, String value, long lifeTime) {
		String id = randVerifierId(dist);
		JVerifier verifier = new JVerifier();
		verifier.setId(id);
		verifier.setTag(tag);
		verifier.setValue(value);
		verifier.setPassTime(ContextUtils.getContextTime() + lifeTime);
		BeanService.ME.persist(verifier);
	}

	/**
	 * 查找验证
	 * 
	 * @param id
	 * @return
	 */
	@Transaction(readOnly = true)
	public JVerifier findVerifier(String id, String tag) {
		Query query = BeanDao.getSession().createQuery("SELECT o FROM JVerifier o WHERE o.id = ? AND o.passTime > ? AND o.tag = ?");
		query.setMaxResults(1);
		query.setParameter(0, id);
		query.setParameter(1, ContextUtils.getContextTime());
		query.setParameter(2, tag);
		Iterator<JVerifier> iterator = query.iterate();
		return iterator.hasNext() ? iterator.next() : null;
	}

	/**
	 * 处理过期对象
	 */
	@Started
	protected void initVerifierNames() {
		SessionFactoryBean sessionFactoryBean = SessionFactoryUtils.get();
		SessionFactory sessionFactory = sessionFactoryBean.getSessionFactory();
		if (sessionFactory != null) {
			Collection<String> names = new HashSet<String>();
			for (Entry<String, Entry<Class<?>, SessionFactory>> entry : sessionFactoryBean.getJpaEntityNameMapEntityClassFactory().entrySet()) {
				Entry<Class<?>, SessionFactory> value = entry.getValue();
				if (value.getValue() == sessionFactory && JbVerifier.class.isAssignableFrom(value.getClass())) {
					names.add(entry.getKey());
				}
			}

			if (!names.isEmpty()) {
				final String[] verifierNames = KernelCollection.toArray(names, String.class);
				ContextUtils.getScheduleFactory().addScheduleRunable(new CronFixDelayRunable(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						ME.removeExpiredSession(verifierNames);
					}

				}, 24 * 36000));
			}

		}
	}

	/**
	 * 清除过期验证
	 */
	@Transaction
	protected void removeExpiredSession(String[] verifierNames) {
		long contextTime = ContextUtils.getContextTime();
		for (String verifierName : verifierNames) {
			QueryDaoUtils.createQueryArray(BeanDao.getSession(), "DELETE o FROM " + verifierName + " o WHERE o.passTime < ?", contextTime).executeUpdate();
		}
	}
}
