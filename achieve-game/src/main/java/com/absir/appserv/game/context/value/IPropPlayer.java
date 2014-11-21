/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年11月21日 下午1:53:17
 */
package com.absir.appserv.game.context.value;

import com.absir.appserv.game.context.JbPlayerContext;

/**
 * @author absir
 *
 */
public interface IPropPlayer<P extends JbPlayerContext<?, ?, ?, ?, ?, ?, ?>> {

	/**
	 * @param playerContext
	 * @return
	 */
	public Object userPlayer(P playerContext);
}
