/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-28 上午10:19:47
 */
package com.absir.appserv.client.configure;

import com.absir.appserv.client.configure.xls.XCardDefine;
import com.absir.appserv.configure.JConfigureBase;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaClasses;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("游戏配置") }, name = "玩家")
public class JPlayerConfigure extends JConfigureBase {

	@JaLang("初始金钱")
	private int money = 0;

	@JaLang("初始宝石")
	private int diamond = 0;

	@JaLang("初始卡牌")
	@JaClasses(XCardDefine.class)
	private int[] cardIds = new int[] { 3, 17, 31 };

	@JaLang("附加卡牌")
	@JaClasses(XCardDefine.class)
	private int[] otherCardIds = new int[] { 9, 26, 37, 12, 23, 40 };

	@JaLang("最大等级")
	private int maxLevel = 60;

	@JaLang("购买卡牌栏")
	private int cardNumber = 3;

	@JaLang("购买好友数")
	private int friendNumber = 3;

	@JaLang("进化概率")
	// new float[] { 1, 0.8f, 0.6f, 0.4f, 0.2f, 0.1f, 0.1f };
	private float[] evoluteProb = new float[] { 1, 1, 1, 1, 1, 1, 1 };

	@JaLang("锁定技能价格")
	private int lockDiamond = 20;

	@JaLang("新技能概率")
	// 1, 0.8f, 0.6f, 0.4f, 0.2f, 0.1f, 0.1f
	private float[] skillProb = new float[] { 1, 1, 1, 1, 1, 1, 1 };

	@JaLang("任务一波")
	// 1, 0.8f, 0.6f, 0.4f, 0.2f, 0.1f, 0.1f
	private boolean taskOnce;

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
	 * @return the cardIds
	 */
	public int[] getCardIds() {
		return cardIds;
	}

	/**
	 * @param cardIds
	 *            the cardIds to set
	 */
	public void setCardIds(int[] cardIds) {
		this.cardIds = cardIds;
	}

	/**
	 * @return the otherCardIds
	 */
	public int[] getOtherCardIds() {
		return otherCardIds;
	}

	/**
	 * @param otherCardIds
	 *            the otherCardIds to set
	 */
	public void setOtherCardIds(int[] otherCardIds) {
		this.otherCardIds = otherCardIds;
	}

	/**
	 * @return the maxLevel
	 */
	public int getMaxLevel() {
		return maxLevel;
	}

	/**
	 * @param maxLevel
	 *            the maxLevel to set
	 */
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	/**
	 * @return the cardNumber
	 */
	public int getCardNumber() {
		return cardNumber;
	}

	/**
	 * @param cardNumber
	 *            the cardNumber to set
	 */
	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	 * @return the friendNumber
	 */
	public int getFriendNumber() {
		return friendNumber;
	}

	/**
	 * @param friendNumber
	 *            the friendNumber to set
	 */
	public void setFriendNumber(int friendNumber) {
		this.friendNumber = friendNumber;
	}

	/**
	 * @return the evoluteProb
	 */
	public float[] getEvoluteProb() {
		return evoluteProb;
	}

	/**
	 * @param evoluteProb
	 *            the evoluteProb to set
	 */
	public void setEvoluteProb(float[] evoluteProb) {
		this.evoluteProb = evoluteProb;
	}

	/**
	 * @return the lockDiamond
	 */
	public int getLockDiamond() {
		return lockDiamond;
	}

	/**
	 * @param lockDiamond
	 *            the lockDiamond to set
	 */
	public void setLockDiamond(int lockDiamond) {
		this.lockDiamond = lockDiamond;
	}

	/**
	 * @return the skillProb
	 */
	public float[] getSkillProb() {
		return skillProb;
	}

	/**
	 * @param skillProb
	 *            the skillProb to set
	 */
	public void setSkillProb(float[] skillProb) {
		this.skillProb = skillProb;
	}

	/**
	 * @return the taskOnce
	 */
	public boolean isTaskOnce() {
		return taskOnce;
	}

	/**
	 * @param taskOnce
	 *            the taskOnce to set
	 */
	public void setTaskOnce(boolean taskOnce) {
		this.taskOnce = taskOnce;
	}

}
