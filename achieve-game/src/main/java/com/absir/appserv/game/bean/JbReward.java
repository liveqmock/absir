/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-3 下午5:15:49
 */
package com.absir.appserv.game.bean;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;

import com.absir.appserv.configure.xls.value.XaParam;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@Embeddable
public class JbReward {

	@JaLang("金钱奖励")
	private int money;

	@JaLang("宝石奖励")
	private int diamond;

	@JaLang("卡牌奖励")
	@XaParam
	@Type(type = "com.absir.appserv.system.bean.type.JtJsonDynamic")
	protected LinkedHashMap<Integer, Integer> cardDefines;

	@JaLang("道具奖励")
	@XaParam
	@Type(type = "com.absir.appserv.system.bean.type.JtJsonDynamic")
	protected LinkedHashMap<Integer, Integer> propDefines;

	/**
	 * @return the money
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * @param money
	 *            the money to set
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/**
	 * @return the diamond
	 */
	public int getDiamond() {
		return diamond;
	}

	/**
	 * @param diamond
	 *            the diamond to set
	 */
	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}

	/**
	 * @return the cardDefines
	 */
	public Map<Integer, Integer> getCardDefines() {
		return cardDefines;
	}

	/**
	 * @param cardDefines
	 *            the cardDefines to set
	 */
	public void setCardDefines(LinkedHashMap<Integer, Integer> cardDefines) {
		this.cardDefines = cardDefines;
	}

	/**
	 * @return the propDefines
	 */
	public Map<Integer, Integer> getPropDefines() {
		return propDefines;
	}

	/**
	 * @param propDefines
	 *            the propDefines to set
	 */
	public void setPropDefines(LinkedHashMap<Integer, Integer> propDefines) {
		this.propDefines = propDefines;
	}
}
