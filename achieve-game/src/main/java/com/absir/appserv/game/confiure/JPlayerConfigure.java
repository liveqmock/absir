/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-28 上午10:19:47
 */
package com.absir.appserv.game.confiure;

import com.absir.appserv.configure.JConfigureBase;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JaName;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("游戏配置") }, name = "玩家")
public class JPlayerConfigure extends JConfigureBase {

	@JaLang(value = "初始金钱", tag = "initialMoney")
	private int money = 0;

	@JaLang(value = "初始宝石", tag = "initialDiamond")
	private int diamond = 0;

	@JaLang(value = "初始卡牌", tag = "initialCardIds")
	@JaName("XCardDefine")
	private int[] cardIds = new int[] { 3, 17, 31 };

	@JaLang("其他卡牌")
	@JaName("XCardDefine")
	private int[] otherCardIds = new int[] { 9, 26, 37, 12, 23, 40 };

	@JaLang("最大等级")
	private int maxLevel = 60;

	@JaLang("卡牌数宝石")
	private int cardNumberDiamond = 5;

	@JaLang("卡牌数增加")
	private int cardNumberAdd = 2;

	@JaLang("好友数宝石")
	private int friendNumberDiamond = 10;

	@JaLang("好友数增加")
	private int friendNumberAdd = 2;

	@JaLang("行动力宝石")
	private int epDiamond = 50;

	@JaLang("行动力增加")
	private int epAdd = 5;

	@JaLang("金钱宝石")
	private int moneyDiamond = 50;

	@JaLang("竞技场行动力")
	private int arenaEp = 7;

	@JaLang("一波敌人")
	private boolean onceEnemy;

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
	 * @return the cardNumberDiamond
	 */
	public int getCardNumberDiamond() {
		return cardNumberDiamond;
	}

	/**
	 * @param cardNumberDiamond
	 *            the cardNumberDiamond to set
	 */
	public void setCardNumberDiamond(int cardNumberDiamond) {
		this.cardNumberDiamond = cardNumberDiamond;
	}

	/**
	 * @return the cardNumberAdd
	 */
	public int getCardNumberAdd() {
		return cardNumberAdd;
	}

	/**
	 * @param cardNumberAdd
	 *            the cardNumberAdd to set
	 */
	public void setCardNumberAdd(int cardNumberAdd) {
		this.cardNumberAdd = cardNumberAdd;
	}

	/**
	 * @return the friendNumberDiamond
	 */
	public int getFriendNumberDiamond() {
		return friendNumberDiamond;
	}

	/**
	 * @param friendNumberDiamond
	 *            the friendNumberDiamond to set
	 */
	public void setFriendNumberDiamond(int friendNumberDiamond) {
		this.friendNumberDiamond = friendNumberDiamond;
	}

	/**
	 * @return the friendNumberAdd
	 */
	public int getFriendNumberAdd() {
		return friendNumberAdd;
	}

	/**
	 * @param friendNumberAdd
	 *            the friendNumberAdd to set
	 */
	public void setFriendNumberAdd(int friendNumberAdd) {
		this.friendNumberAdd = friendNumberAdd;
	}

	/**
	 * @return the epDiamond
	 */
	public int getEpDiamond() {
		return epDiamond;
	}

	/**
	 * @param epDiamond
	 *            the epDiamond to set
	 */
	public void setEpDiamond(int epDiamond) {
		this.epDiamond = epDiamond;
	}

	/**
	 * @return the epAdd
	 */
	public int getEpAdd() {
		return epAdd;
	}

	/**
	 * @param epAdd
	 *            the epAdd to set
	 */
	public void setEpAdd(int epAdd) {
		this.epAdd = epAdd;
	}

	/**
	 * @return the moneyDiamond
	 */
	public int getMoneyDiamond() {
		return moneyDiamond;
	}

	/**
	 * @param moneyDiamond
	 *            the moneyDiamond to set
	 */
	public void setMoneyDiamond(int moneyDiamond) {
		this.moneyDiamond = moneyDiamond;
	}

	/**
	 * @return the arenaEp
	 */
	public int getArenaEp() {
		return arenaEp;
	}

	/**
	 * @param arenaEp
	 *            the arenaEp to set
	 */
	public void setArenaEp(int arenaEp) {
		this.arenaEp = arenaEp;
	}

	/**
	 * @return the onceEnemy
	 */
	public boolean isOnceEnemy() {
		return onceEnemy;
	}

	/**
	 * @param onceEnemy
	 *            the onceEnemy to set
	 */
	public void setOnceEnemy(boolean onceEnemy) {
		this.onceEnemy = onceEnemy;
	}
}
