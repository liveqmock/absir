/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-25 上午10:26:35
 */
package com.absir.appserv.client.bean.value;

import java.util.Calendar;

import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
public enum JeRepetition {

	@JaLang("测试")
	TEST {
		@Override
		public long getNextTime(long contextTime) {
			// TODO Auto-generated method stub
			return contextTime + 900000;
		}
	},

	@JaLang("每天")
	DAYLY {
		@Override
		public long getNextTime(long contextTime) {
			// TODO Auto-generated method stub
			return contextTime + 24 * 3600000;
		}
	},

	@JaLang("每周")
	WEEKLY {
		@Override
		public long getNextTime(long contextTime) {
			// TODO Auto-generated method stub
			return contextTime + 7 * 24 * 3600000;
		}
	},

	@JaLang("每月")
	MOTHLY {
		@Override
		public long getNextTime(long contextTime) {
			// TODO Auto-generated method stub
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(contextTime);
			int month = calendar.get(Calendar.MONTH);
			if (++month > 12) {
				calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
				month = 1;
			}

			calendar.set(Calendar.MONTH, month);
			return calendar.getTimeInMillis();
		}
	},

	@JaLang("每年")
	YEARLY {
		@Override
		public long getNextTime(long contextTime) {
			// TODO Auto-generated method stub
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(contextTime);
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
			return calendar.getTimeInMillis();
		}
	},

	;

	/**
	 * @param contextTime
	 * @return
	 */
	public abstract long getNextTime(long contextTime);

	/**
	 * @param time
	 * @param contextTime
	 * @return
	 */
	public long getNextTime(long time, long contextTime) {
		while (time < contextTime) {
			long nextTime = getNextTime(time);
			if (nextTime <= time) {
				break;
			}

			time = nextTime;
		}

		return time;
	}
}
