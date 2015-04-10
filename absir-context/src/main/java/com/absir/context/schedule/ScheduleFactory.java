/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-23 下午12:49:50
 */
package com.absir.context.schedule;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.absir.bean.basis.BeanDefine;
import com.absir.bean.basis.BeanScope;
import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.IMethodInject;
import com.absir.bean.inject.InjectMethod;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.InjectType;
import com.absir.bean.inject.value.Started;
import com.absir.bean.inject.value.Value;
import com.absir.context.config.BeanMethodRunable;
import com.absir.context.core.ContextUtils;
import com.absir.context.schedule.cron.CronExpressionRunable;
import com.absir.context.schedule.cron.CronFixDelayRunable;
import com.absir.context.schedule.value.Schedule;
import com.absir.core.util.UtilNode;

/**
 * @author absir
 * 
 */
@Bean
public class ScheduleFactory implements IMethodInject<Schedule> {

	/** LOGGER */
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleFactory.class);

	/** scheduleRunables */
	@Inject(type = InjectType.Selectable)
	private List<ScheduleRunable> scheduleRunables = new ArrayList<ScheduleRunable>();

	/** scheduleRunableHeader */
	private UtilNode<ScheduleRunable> scheduleRunableHeader = new UtilNode<ScheduleRunable>();

	@Value(value = "schedule.max")
	private long scheduleMax = 300000;

	/** contextTimerTask */
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				long time = ContextUtils.getContextTime();
				Date date = new Date(time);
				UtilNode<ScheduleRunable> scheduleRunableNode = scheduleRunableHeader.getNext();
				UtilNode<ScheduleRunable> scheduleRunableNext = null;
				ScheduleRunable runable;
				while (scheduleRunableNode != null) {
					scheduleRunableNext = scheduleRunableNode.getNext();
					runable = scheduleRunableNode.getElement();
					if (runable.getScheduleTime() < time) {
						try {
							runable.run(date);

						} catch (Throwable e) {
							LOGGER.error("failed!", e);
						}

						if (runable.getScheduleTime() <= 0) {
							scheduleRunableNode.remove();

						} else {
							sortScheduleRunableNode(scheduleRunableNode);
						}

					} else {
						break;
					}

					scheduleRunableNode = scheduleRunableNext;
				}

				if (!scheduleRunables.isEmpty()) {
					List<ScheduleRunable> schedules = scheduleRunables;
					synchronized (this) {
						scheduleRunables = new ArrayList<ScheduleRunable>();
					}

					for (ScheduleRunable schedule : schedules) {
						schedule.start(date);
						if (schedule.getScheduleTime() < time) {
							try {
								schedule.run(date);

							} catch (Throwable e) {
								LOGGER.error("failed!", e);
							}

							if (schedule.getScheduleTime() <= 0) {
								continue;
							}
						}

						insertScheduleRunableNode(scheduleRunableHeader, schedule);
					}
				}

				scheduleRunableNode = scheduleRunableHeader.getNext();
				if (scheduleRunableNode == null) {
					time = scheduleMax;

				} else {
					time = scheduleRunableNode.getElement().getScheduleTime() - time;
					if (time > scheduleMax) {
						time = scheduleMax;
					}
				}

				try {
					Thread.sleep(time);

				} catch (Throwable e) {
					// TODO Auto-generated catch block
					break;
				}
			}
		}
	};

	/**
	 * 
	 */
	@Started
	private void started() {
		ContextUtils.getThreadPoolExecutor().execute(runnable);
	}

	/**
	 * @param scheduleRunable
	 */
	public synchronized void addScheduleRunable(ScheduleRunable scheduleRunable) {
		scheduleRunables.add(scheduleRunable);
	}

	/**
	 * @param scheduleRunable
	 * @return
	 */
	public synchronized boolean removeScheduleRunable(ScheduleRunable scheduleRunable) {
		return scheduleRunables.remove(scheduleRunable);
	}

	/**
	 * @param scheduleRunableNode
	 */
	public static void sortScheduleRunableNode(UtilNode<ScheduleRunable> scheduleRunableNode) {
		long scheduleTime = scheduleRunableNode.getElement().getScheduleTime();
		UtilNode<ScheduleRunable> scheduleRunableCompare = scheduleRunableNode.getNext();
		if (scheduleRunableCompare == null || scheduleTime <= scheduleRunableCompare.getElement().getScheduleTime()) {
			scheduleRunableCompare = scheduleRunableNode;
			ScheduleRunable runableCompare;
			while (true) {
				scheduleRunableCompare = scheduleRunableCompare.getPrevious();
				runableCompare = scheduleRunableCompare.getElement();
				if (runableCompare == null || scheduleTime >= runableCompare.getScheduleTime()) {
					scheduleRunableCompare.afterInsert(scheduleRunableNode);
					break;
				}
			}

		} else {
			UtilNode<ScheduleRunable> scheduleRunableNext = scheduleRunableNode.getNext();
			while (true) {
				if (scheduleTime <= scheduleRunableCompare.getElement().getScheduleTime()) {
					scheduleRunableCompare.beforeInsert(scheduleRunableNode);
					break;
				}

				scheduleRunableNext = scheduleRunableCompare.getNext();
				if (scheduleRunableNext == null) {
					scheduleRunableCompare.afterInsert(scheduleRunableNode);
					break;
				}

				scheduleRunableCompare = scheduleRunableNext;
			}
		}
	}

	/**
	 * @param scheduleRunableHeader
	 * @param runable
	 */
	public static void insertScheduleRunableNode(UtilNode<ScheduleRunable> scheduleRunableHeader, ScheduleRunable runable) {
		long scheduleTime = runable.getScheduleTime();
		UtilNode<ScheduleRunable> scheduleRunableNext;
		while (true) {
			scheduleRunableNext = scheduleRunableHeader.getNext();
			if (scheduleRunableNext == null || scheduleTime <= scheduleRunableNext.getElement().getScheduleTime()) {
				scheduleRunableHeader.afterAdd(new UtilNode<ScheduleRunable>(runable));
				break;
			}

			scheduleRunableHeader = scheduleRunableNext;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.bean.inject.IMethodInject#isRequired()
	 */
	@Override
	public boolean isRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.inject.IMethodInject#getInjects(com.absir.bean.basis.BeanScope
	 * , com.absir.bean.basis.BeanDefine, java.lang.reflect.Method)
	 */
	@Override
	public Schedule getInjects(BeanScope beanScope, BeanDefine beanDefine, Method method) {
		// TODO Auto-generated method stub
		return method.getAnnotation(Schedule.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.bean.inject.IMethodInject#setInjectMethod(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object,
	 * com.absir.bean.inject.InjectMethod)
	 */
	@Override
	public void setInjectMethod(Schedule inject, Method method, Object beanObject, InjectMethod injectMethod) {
		// TODO Auto-generated method stub
		ScheduleRunableAbstract scheduleRunable;
		BeanMethodRunable beanMethodRunable = new BeanMethodRunable(beanObject, injectMethod);
		if ("".equals(inject.cron())) {
			scheduleRunable = new CronFixDelayRunable(beanMethodRunable, inject.fixedDelay());

		} else {
			scheduleRunable = new CronExpressionRunable(beanMethodRunable, inject.fixedDelay(), inject.cron());
		}

		if (BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= 0) {
			System.out.println("ScheduleRunable: " + beanObject + "=>" + injectMethod.getMethod());
		}

		scheduleRunable.setScheduleTime(inject.initialDelay());
		addScheduleRunable(scheduleRunable);
	}
}
