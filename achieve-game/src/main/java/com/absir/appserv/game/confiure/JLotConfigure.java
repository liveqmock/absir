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

/**
 * @author absir
 * 
 */
@MaEntity(parent = @MaMenu("抽奖设置"), name = "参数")
public class JLotConfigure extends JConfigureBase {

	@JaLang("友情免费时间")
	private long friendFreeTime = 3 * 3600000;

	@JaLang("宝石免费时间")
	private long diamondFreeTime = 12 * 3600000;

	@JaLang("抽奖友情数")
	private int friendshipNumber = 200;

	@JaLang("抽奖宝石数")
	private int diamondNumber = 20;

	@JaLang("10抽奖宝石数")
	private int tenDiamondNumber = 180;

	@JaLang("10抽卡牌品质")
	private int tenCardDefineRare = 4;

	@JaLang("卡牌友情概率")
	private float[] friendProbabilities = setProbabilities(new float[] { 32f, 8f, 4f, 1f, 0.5f, 0.02f });

	@JaLang("卡牌宝石概率")
	private float[] diamondProbabilities = setProbabilities(new float[] { 16f, 8f, 4f, 2f, 1f, 0.5f });

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
	 * @return the diamondFreeTime
	 */
	public long getDiamondFreeTime() {
		return diamondFreeTime;
	}

	/**
	 * @param diamondFreeTime
	 *            the diamondFreeTime to set
	 */
	public void setDiamondFreeTime(long diamondFreeTime) {
		this.diamondFreeTime = diamondFreeTime;
	}

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
	 * @return the diamondNumber
	 */
	public int getDiamondNumber() {
		return diamondNumber;
	}

	/**
	 * @param diamondNumber
	 *            the diamondNumber to set
	 */
	public void setDiamondNumber(int diamondNumber) {
		this.diamondNumber = diamondNumber;
	}

	/**
	 * @return the tenDiamondNumber
	 */
	public int getTenDiamondNumber() {
		return tenDiamondNumber;
	}

	/**
	 * @param tenDiamondNumber
	 *            the tenDiamondNumber to set
	 */
	public void setTenDiamondNumber(int tenDiamondNumber) {
		this.tenDiamondNumber = tenDiamondNumber;
	}

	/**
	 * @return the tenCardDefineRare
	 */
	public int getTenCardDefineRare() {
		return tenCardDefineRare;
	}

	/**
	 * @param tenCardDefineRare
	 *            the tenCardDefineRare to set
	 */
	public void setTenCardDefineRare(int tenCardDefineRare) {
		this.tenCardDefineRare = tenCardDefineRare;
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
		this.friendProbabilities = setProbabilities(friendProbabilities);
	}

	/**
	 * @param probabilities
	 */
	private static float[] setProbabilities(float[] probabilities) {
		int length = probabilities.length;
		if (length == 0) {
			return probabilities;
		}

		float probabilityTotle = 0;
		for (int i = 0; i < length; i++) {
			float probability = probabilities[i];
			if (probability < 0) {
				probability = 0;
				probabilities[i] = probability;
			}

			probabilityTotle += probability;
		}

		if (probabilityTotle == 0) {
			probabilities[0] = 1;

		} else {
			for (int i = 0; i < length; i++) {
				probabilities[i] = probabilities[i] / probabilityTotle;
			}
		}

		return probabilities;
	}

	/**
	 * @return the diamondProbabilities
	 */
	public float[] getDiamondProbabilities() {
		return diamondProbabilities;
	}

	/**
	 * @param diamondProbabilities
	 *            the diamondProbabilities to set
	 */
	public void setDiamondProbabilities(float[] diamondProbabilities) {
		this.diamondProbabilities = setProbabilities(diamondProbabilities);
	}
}
