/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-10 上午10:49:18
 */
package com.absir.appserv.system.domain;

import java.util.List;

import org.hibernate.event.spi.PostUpdateEvent;

import com.absir.appserv.system.bean.value.JiActivity;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.utils.QueryDaoUtils;
import com.absir.context.core.ContextUtils;
import com.absir.core.kernel.KernelClass;
import com.absir.core.kernel.KernelDyna;
import com.absir.orm.hibernate.SessionFactoryUtils;
import com.absir.orm.hibernate.boost.IEntityMerge.MergeType;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
public class DActitity<T extends JiActivity> {

	/** 实体名称 */
	private String entityName;

	/** 下次更新时间 */
	private long nextTime;

	/** 延时更新时间 */
	private static final int DELAY_NEXT_TIME = 60000;

	/** 最大更新时间 */
	private static final int MAX_NEXT_TIME = 24 * 3600000;

	/**
	 * @param entityName
	 */
	public DActitity(String entityName) {
		if (entityName == null) {
			entityName = SessionFactoryUtils.getJpaEntityName(KernelClass.componentClass(getClass()));
		}

		this.entityName = entityName;
	}

	/**
	 * @return the nextTime
	 */
	public long getNextTime() {
		return nextTime;
	}

	/**
	 * @param nextTime
	 *            the nextTime to set
	 */
	public void setNextTime(long nextTime) {
		this.nextTime = nextTime;
	}

	/**
	 * @param entity
	 * @param mergeType
	 */
	public void merge(T entity, MergeType mergeType, Object mergeEvent) {
		long contextTime = ContextUtils.getContextTime();
		if (mergeType == MergeType.DELETE) {
			if (entity.getBeginTime() > contextTime && entity.getPassTime() < contextTime) {
				nextTime = contextTime + DELAY_NEXT_TIME;
			}

		} else {
			if (mergeType == MergeType.INSERT) {
				if (contextTime < entity.getPassTime()) {
					if (nextTime > entity.getBeginTime()) {
						contextTime = entity.getBeginTime() + DELAY_NEXT_TIME;
						nextTime = contextTime <= entity.getBeginTime() ? contextTime : entity.getBeginTime();
					}
				}

			} else {
				PostUpdateEvent postUpdateEvent = (PostUpdateEvent) mergeEvent;
				Long beginTime = null;
				Long passTime = null;
				String[] propertyNames = postUpdateEvent.getPersister().getPropertyNames();
				int update = 2;
				for (int i : postUpdateEvent.getDirtyProperties()) {
					if ("beginTime".equals(propertyNames[i])) {
						beginTime = KernelDyna.to(postUpdateEvent.getOldState()[i], Long.class);
						if (--update == 0) {
							break;
						}

					} else if ("passTime".equals(propertyNames[i])) {
						passTime = KernelDyna.to(postUpdateEvent.getOldState()[i], Long.class);
						if (--update == 0) {
							break;
						}
					}
				}

				if (beginTime != null) {
					if (merge(contextTime, entity.getBeginTime(), beginTime)) {
						return;
					}
				}

				if (passTime != null) {
					if (merge(contextTime, entity.getPassTime(), passTime)) {
						return;
					}
				}
			}
		}
	}

	/**
	 * @param currentTime
	 * @param oldTime
	 */
	private boolean merge(long contextTime, long currentTime, long oldTime) {
		if (contextTime <= currentTime) {
			oldTime = contextTime >= oldTime ? 0 : 1;

		} else {
			if (contextTime <= oldTime) {
				oldTime = 0;

			} else {
				if (nextTime > currentTime) {
					contextTime += DELAY_NEXT_TIME;
					nextTime = contextTime < currentTime ? contextTime : contextTime;
				}

				return true;
			}
		}

		if (oldTime == 0) {
			contextTime += DELAY_NEXT_TIME;
			if (nextTime > contextTime) {
				nextTime = contextTime;
			}
		}

		return false;
	}

	/**
	 * 
	 * @param activities
	 */
	public void setLastedActivites(long contextTime, List<T> activities) {
		// 最大延时1天更新
		nextTime = contextTime + MAX_NEXT_TIME;
		if (activities.size() > 0) {
			T actitity = activities.get(0);
			if (nextTime > actitity.getBeginTime()) {
				nextTime = actitity.getBeginTime();
			}
		}
	}

	/**
	 * @param activities
	 */
	public void setCurrentActivites(List<T> activities) {
		for (T actitity : activities) {
			if (nextTime > actitity.getPassTime()) {
				nextTime = actitity.getPassTime();
			}
		}
	}

	/**
	 * @return the actitities
	 */
	public List<T> reloadActivities(long contextTime) {
		List<T> activities = QueryDaoUtils.selectQuery(BeanDao.getSession(), entityName, "o", new Object[] { "o.beginTime >", contextTime }, "ORDER BY o.beginTime", 0, 1);
		setLastedActivites(contextTime, activities);
		activities = QueryDaoUtils.selectQuery(BeanDao.getSession(), entityName, new Object[] { "o.beginTime <=", contextTime, "o.passTime >=", contextTime }, 0, 0);
		setCurrentActivites(activities);
		return activities;
	}
}
