/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-10 上午10:06:42
 */
package com.absir.appserv.system.bean.value;

import com.absir.appserv.system.bean.proxy.JiPass;

/**
 * @author absir
 * 
 */
public interface JiActivity extends JiPass {

	/**
	 * @return
	 */
	public long getBeginTime();

	/**
	 * @param beginTime
	 */
	public void setBeginTime(long beginTime);
}
