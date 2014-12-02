/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月20日 下午5:11:06
 */
package com.absir.appserv.game.bean.value;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.absir.appserv.game.context.value.OReward;

/**
 * @author absir
 *
 */
public interface IRewardDefine extends IRewardBean {

	// 获取奖励ID
	public String getId();

	// 获取奖励执行
	@JsonIgnore
	public OReward getRewardInvoker();

}
