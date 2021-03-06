/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-24 下午2:31:28
 */
package com.absir.context.schedule.cron;

import java.util.Date;

/**
 * @author absir
 * 
 */
public class CronExpressionRunable extends CronFixDelayRunable {

	/** cronSequenceGenerator */
	private CronSequenceGenerator cronSequenceGenerator;

	/**
	 * @param runnable
	 * @param fixDelay
	 * @param expression
	 */
	public CronExpressionRunable(Runnable runnable, long fixDelay, String expression) {
		super(runnable, fixDelay);
		// TODO Auto-generated constructor stub
		cronSequenceGenerator = new CronSequenceGenerator(expression);
	}

	/**
	 * @return the cronSequenceGenerator
	 */
	public CronSequenceGenerator getCronSequenceGenerator() {
		return cronSequenceGenerator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.context.schedule.ScheduleRunable#start(java.util.Date)
	 */
	@Override
	public void start(Date date) {
		// TODO Auto-generated method stub
		scheduleTime += cronSequenceGenerator.next(new Date(date.getTime() + scheduleTime)).getTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.context.schedule.cron.CronFixDelayRunable#getNextScheduleTime
	 * (java.util.Date)
	 */
	@Override
	public long getNextScheduleTime(Date date) {
		// TODO Auto-generated method stub
		return cronSequenceGenerator.next(date).getTime() + getFixDelay();
	}
}
