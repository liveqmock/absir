/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 下午2:49:24
 */
package com.absir.appserv.game.confiure;

import com.absir.appserv.configure.JConfigureBase;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.helper.HelperRandom;

/**
 * @author absir
 * 
 */
@MaEntity(parent = @MaMenu("抽奖设置"), name = "参数")
public class JLotConfigure extends JConfigureBase {

	@JaLang("抽奖友情数")
	private int friendshipNumber = 200;

	@JaLang("友情免费时间")
	private long friendFreeTime = 3 * 3600000;

	@JaLang("卡牌友情概率")
	private float[] friendProbabilities = HelperRandom.getProbabilities(new float[] { 32f, 8f, 4f, 1f, 0.5f, 0.02f });

	@JaLang("卡牌宝石数")
	private int cardDiamondNumber = 20;

	@JaLang("特别卡牌宝石数")
	private int specialCardDiamondNumber = 180;

	@JaLang("卡牌宝石免费时间")
	private long cardDiamondFreeTime = 12 * 3600000;

	@JaLang("池宝石数")
	private int poolDiamondNumber = 20;

	@JaLang("特别池宝石数")
	private int specialPoolDiamondNumber = 180;

	@JaLang("卡牌概率")
	private float[] cardProbabilities = HelperRandom.getProbabilities(new float[] { 16f, 8f, 4f, 2f, 1f, 0.5f });

	@JaLang("池概率")
	private float[] poolProbabilities = HelperRandom.getProbabilities(new float[] { 100, 1 });

	@JaLang("道具概率")
	private float[] propProbabilities = HelperRandom.getProbabilities(new float[] { 16f, 8f, 4f, 2f, 1f, 0.5f });

	/**
	 * @return the friendshipNumber
	 */
	public int getFriendshipNumber() {
		return friendshipNumber;
	}

	/**
	 * @param friendshipNumber
	 *            the friendshipNumber to set
	 */
	public void setFriendshipNumber(int friendshipNumber) {
		this.friendshipNumber = friendshipNumber;
	}

	/**
	 * @return the friendFreeTime
	 */
	public long getFriendFreeTime() {
		return friendFreeTime;
	}

	/**
	 * @param friendFreeTime
	 *            the friendFreeTime to set
	 */
	public void setFriendFreeTime(long friendFreeTime) {
		this.friendFreeTime = friendFreeTime;
	}

	/**
	 * @return the friendProbabilities
	 */
	public float[] getFriendProbabilities() {
		return friendProbabilities;
	}

	/**
	 * @param friendProbabilities
	 *            the friendProbabilities to set
	 */
	public void setFriendProbabilities(float[] friendProbabilities) {
		this.friendProbabilities = HelperRandom.getProbabilities(friendProbabilities);
	}

	/**
	 * @return the cardDiamondNumber
	 */
	public int getCardDiamondNumber() {
		return cardDiamondNumber;
	}

	/**
	 * @param cardDiamondNumber
	 *            the cardDiamondNumber to set
	 */
	public void setCardDiamondNumber(int cardDiamondNumber) {
		this.cardDiamondNumber = cardDiamondNumber;
	}

	/**
	 * @return the specialCardDiamondNumber
	 */
	public int getSpecialCardDiamondNumber() {
		return specialCardDiamondNumber;
	}

	/**
	 * @param specialCardDiamondNumber
	 *            the specialCardDiamondNumber to set
	 */
	public void setSpecialCardDiamondNumber(int specialCardDiamondNumber) {
		this.specialCardDiamondNumber = specialCardDiamondNumber;
	}

	/**
	 * @return the cardDiamondFreeTime
	 */
	public long getCardDiamondFreeTime() {
		return cardDiamondFreeTime;
	}

	/**
	 * @param cardDiamondFreeTime
	 *            the cardDiamondFreeTime to set
	 */
	public void setCardDiamondFreeTime(long cardDiamondFreeTime) {
		this.cardDiamondFreeTime = cardDiamondFreeTime;
	}

	/**
	 * @return the poolDiamondNumber
	 */
	public int getPoolDiamondNumber() {
		return poolDiamondNumber;
	}

	/**
	 * @param poolDiamondNumber
	 *            the poolDiamondNumber to set
	 */
	public void setPoolDiamondNumber(int poolDiamondNumber) {
		this.poolDiamondNumber = poolDiamondNumber;
	}

	/**
	 * @return the specialPoolDiamondNumber
	 */
	public int getSpecialPoolDiamondNumber() {
		return specialPoolDiamondNumber;
	}

	/**
	 * @param specialPoolDiamondNumber
	 *            the specialPoolDiamondNumber to set
	 */
	public void setSpecialPoolDiamondNumber(int specialPoolDiamondNumber) {
		this.specialPoolDiamondNumber = specialPoolDiamondNumber;
	}

	/**
	 * @return the cardProbabilities
	 */
	public float[] getCardProbabilities() {
		return cardProbabilities;
	}

	/**
	 * @param cardProbabilities
	 *            the cardProbabilities to set
	 */
	public void setCardProbabilities(float[] cardProbabilities) {
		this.cardProbabilities = HelperRandom.getProbabilities(cardProbabilities);
	}

	/**
	 * @return the poolProbabilities
	 */
	public float[] getPoolProbabilities() {
		return poolProbabilities;
	}

	/**
	 * @param poolProbabilities
	 *            the poolProbabilities to set
	 */
	public void setPoolProbabilities(float[] poolProbabilities) {
		this.poolProbabilities = HelperRandom.getProbabilities(poolProbabilities);
	}

	/**
	 * @return the propProbabilities
	 */
	public float[] getPropProbabilities() {
		return propProbabilities;
	}

	/**
	 * @param propProbabilities
	 *            the propProbabilities to set
	 */
	public void setPropProbabilities(float[] propProbabilities) {
		this.propProbabilities = HelperRandom.getProbabilities(propProbabilities);
	}

}
