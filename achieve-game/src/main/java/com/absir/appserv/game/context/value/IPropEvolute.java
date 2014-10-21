/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-21 下午5:32:23
 */
package com.absir.appserv.game.context.value;

import com.absir.appserv.game.bean.JbCard;

/**
 * @author absir
 * 
 */
public interface IPropEvolute<C extends JbCard> {

	/**
	 * @return
	 */
	public float getLuck(C card);

}
