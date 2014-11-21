/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年11月21日 下午1:53:34
 */
package com.absir.appserv.game.context.value;

import com.absir.appserv.game.bean.JbCard;
import com.absir.appserv.game.context.JbPlayerContext;

/**
 * @author absir
 *
 */
public interface IPropCard<C extends JbCard, P extends JbPlayerContext<C, ?, ?, ?, ?, ?>> {

	/**
	 * @param card
	 * @param playerContext
	 * @return
	 */
	public Object userCard(C card, P playerContext);

}
