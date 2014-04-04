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
public interface ScheduleRunable {

	/**
	 * @param date
	 */
	public void start(Date date);

	/**
	 * @return
	 */
	public long getScheduleTime();

	/**
	 * @param date
	 */
	public void run(Date date);
}
