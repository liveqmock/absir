/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月20日 下午5:11:06
 */
package com.absir.appserv.game.bean.value;

import com.absir.appserv.game.value.IExp;

/**
 * @author absir
 *
 */
public interface IPlayerDefine extends IExp {

	/**
	 * 行动力
	 * 
	 * @return
	 */
	public int getEp();

	/**
	 * 卡牌数量
	 * 
	 * @return
	 */
	public int getCardNumber();

	/**
	 * 好友数量
	 * 
	 * @return
	 */
	public int getFriendNumber();

}
