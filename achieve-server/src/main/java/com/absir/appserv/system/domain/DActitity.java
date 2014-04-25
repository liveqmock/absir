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
			entityName = SessionFactoryUtils.getJpaEntityName(KernelClass.argumentClass(getClass()));
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
			if (entity.getBeginTime() < contextTime && entity.getPassTime() > contextTime) {
				contextTime += DELAY_NEXT_TIME;
				if (nextTime > contextTime) {
					nextTime = contextTime;
				}
			}

		} else {
			if (mergeType == MergeType.INSERT) {
				if (entity.getPassTime() > contextTime) {
					if (entity.getBeginTime() <= contextTime) {
						contextTime += DELAY_NEXT_TIME;
						if (nextTime > contextTime) {
							nextTime = contextTime;
						}

					} else {
						if (entity.getBeginTime() < nextTime) {
							contextTime += DELAY_NEXT_TIME;
							nextTime = contextTime <= entity.getBeginTime() ? contextTime : entity.getBeginTime();
						}
					}
				}

			} else {
				PostUpdateEvent postUpdateEvent = (PostUpdateEvent) mergeEvent;
				long beginTime = entity.getBeginTime();
				long passTime = entity.getPassTime();
				String[] propertyNames = postUpdateEvent.getPersister().getPropertyNames();
				int update = 2;
				for (int i : postUpdateEvent.getDirtyProperties()) {
					if ("beginTime".equals(propertyNames[i])) {
						beginTime = KernelDyna.to(postUpdateEvent.getOldState()[i], long.class);
						if (--update == 0) {
							break;
						}

					} else if ("passTime".equals(propertyNames[i])) {
						passTime = KernelDyna.to(postUpdateEvent.getOldState()[i], long.class);
						if (--update == 0) {
							break;
						}
					}
				}

				if ((beginTime <= contextTime && passTime > contextTime) || (entity.getBeginTime() <= contextTime && entity.getPassTime() > contextTime)) {
					contextTime += DELAY_NEXT_TIME;
					if (nextTime > contextTime) {
						nextTime = contextTime;
					}

				} else {
					if (entity.getPassTime() > contextTime) {
						if (entity.getBeginTime() < nextTime) {
							contextTime += DELAY_NEXT_TIME;
							nextTime = contextTime <= entity.getBeginTime() ? contextTime : entity.getBeginTime();
						}
					}
				}
			}
		}
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
