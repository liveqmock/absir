/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-10 下午1:59:13
 */
package com.absir.appserv.client.bean;

import java.util.List;

import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;

import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@Embeddable
public class JActivityBase {

	@JaLang("活动名称")
	@JaEdit(groups = JaEdit.GROUP_SUGGEST)
	private String name;

	@JaLang("市场奖励")
	private String marketReward;

	@JaLang("奖励梯度")
	@Type(type = "com.absir.appserv.system.bean.type.JtJsonDynamic")
	private List<JActivityReward> activityRewards;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the marketReward
	 */
	public String getMarketReward() {
		return marketReward;
	}

	/**
	 * @param marketReward
	 *            the marketReward to set
	 */
	public void setMarketReward(String marketReward) {
		this.marketReward = marketReward;
	}

	/**
	 * @return the activityRewards
	 */
	public List<JActivityReward> getActivityRewards() {
		return activityRewards;
	}

	/**
	 * @param activityRewards
	 *            the activityRewards to set
	 */
	public void setActivityRewards(List<JActivityReward> activityRewards) {
		this.activityRewards = activityRewards;
	}
}
