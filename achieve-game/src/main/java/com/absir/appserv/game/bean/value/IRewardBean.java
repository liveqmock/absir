/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月20日 下午5:11:06
 */
package com.absir.appserv.game.bean.value;

import com.absir.appserv.game.bean.JbReward;
import com.absir.appserv.game.value.IExp;

/**
 * @author absir
 *
 */
public interface IRewardBean extends IExp {

	// 获取奖励
	public JbReward getReward();

}
