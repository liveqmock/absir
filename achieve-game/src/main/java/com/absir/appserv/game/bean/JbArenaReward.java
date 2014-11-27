/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年11月27日 上午9:44:46
 */
package com.absir.appserv.game.bean;

import com.absir.appserv.configure.xls.XlsBaseUpdate;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 *
 */
public abstract class JbArenaReward extends XlsBaseUpdate {

	@JaLang("排名")
	protected int arena;

	/**
	 * @return
	 */
	public abstract JbReward getReward();

}
