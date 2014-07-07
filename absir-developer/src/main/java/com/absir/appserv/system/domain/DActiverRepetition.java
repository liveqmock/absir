/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月7日 下午2:45:02
 */
package com.absir.appserv.system.domain;

import java.util.List;

import org.hibernate.Session;

import com.absir.appserv.system.bean.value.JiActiveRepetition;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.utils.QueryDaoUtils;

/**
 * @author absir
 *
 */
@SuppressWarnings("unchecked")
public class DActiverRepetition<T extends JiActiveRepetition> extends DActiver<T> {

	/** repeated */
	private boolean repeated;

	/** allQueryString */
	private String allQueryString;

	/**
	 * @param entityName
	 */
	public DActiverRepetition(String entityName) {
		super(entityName);
		// TODO Auto-generated constructor stub
		allQueryString = "SELECT o FROM " + entityName + " o WHERE o.beginTime <= ? AND o.passTime <= ?";
	}

	/**
	 * @param contextTime
	 * @return
	 */
	public List<T> reloadActives(long contextTime) {
		if (!repeated) {
			repeated = true;
			Session session = BeanDao.getSession();
			List<T> actives = QueryDaoUtils.createQueryArray(session, allQueryString, contextTime, contextTime).list();
			for (JiActiveRepetition active : actives) {
				long nextPassTime = active.getNextPassTime(contextTime);
				if (nextPassTime > contextTime) {
					long nextBeginTime = active.getBeginTime() + nextPassTime - active.getPassTime();
					if (nextBeginTime <= contextTime) {
						active.setBeginTime(nextBeginTime);
						active.setPassTime(nextPassTime);
						session.merge(active);
					}
				}
			}
		}

		return super.reloadActives(contextTime);
	}

}
