/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-3 下午5:15:49
 */
package com.absir.appserv.client.bean;

import javax.persistence.MappedSuperclass;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.absir.appserv.client.configure.xls.XCardDefine;
import com.absir.appserv.client.configure.xls.XPropDefine;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.helper.HelperString;

/**
 * @author absir
 * 
 */
@MappedSuperclass
public class JbRewardBean extends JbReward {

	/**
	 * @return the cardRewards
	 */
	@JsonIgnore
	@JaLang("卡牌奖励")
	public String getCardRewards() {
		return HelperString.paramMap(cardDefines);
	}

	/**
	 * @param cardRewards
	 *            the cardRewards to set
	 */
	public void setCardRewards(String cardRewards) {
		cardDefines = HelperString.paramIdNumberMap(cardRewards, XCardDefine.class, cardDefines);
	}

	/**
	 * @return the propRewards
	 */
	@JsonIgnore
	@JaLang("道具奖励")
	public String getPropRewards() {
		return HelperString.paramMap(propDefines);
	}

	/**
	 * @param propRewards
	 *            the propRewards to set
	 */
	public void setPropRewards(String propRewards) {
		propDefines = HelperString.paramIdNumberMap(propRewards, XPropDefine.class, propDefines);
	}
}
