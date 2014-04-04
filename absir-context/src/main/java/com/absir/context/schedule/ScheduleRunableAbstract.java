/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-23 下午12:51:31
 */
package com.absir.context.schedule;

import java.util.Date;

/**
 * @author absir
 * 
 */
public abstract class ScheduleRunableAbstract implements ScheduleRunable {

	/** runnable */
	protected Runnable runnable;

	/** scheduleTime */
	protected long scheduleTime;

	/**
	 * @param runnable
	 */
	public ScheduleRunableAbstract(Runnable runnable) {
		this.runnable = runnable;
	}

	/**
	 * @return the runnable
	 */
	public Runnable getRunnable() {
		return runnable;
	}

	/**
	 * @return the scheduleTime
	 */
	public long getScheduleTime() {
		return scheduleTime;
	}

	/**
	 * @param scheduleTime
	 *            the scheduleTime to set
	 */
	public void setScheduleTime(long scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	/**
	 * @param date
	 */
	public final void run(Date date) {
		runnable.run();
		scheduleTime = getNextScheduleTime(date);
	}

	/**
	 * @return
	 */
	public abstract long getNextScheduleTime(Date date);
}
