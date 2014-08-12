/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-10 下午6:37:40
 */
package com.absir.appserv.client.context.value;

import com.absir.appserv.client.bean.JPlayer;

/**
 * @author absir
 * 
 */
/**
 * @author absir
 * 
 */
public class OPlayerResult extends OPlayer {

	/** level */
	private int level;

	/** correctCount */
	public int correctCount;

	/** answerTime */
	private long answerTime;

	/**
	 * @param player
	 */
	public OPlayerResult(JPlayer player) {
		super(player);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the correctCount
	 */
	public int getCorrectCount() {
		return correctCount;
	}

	/**
	 * @param correctCount
	 *            the correctCount to set
	 */
	public void setCorrectCount(int correctCount) {
		this.correctCount = correctCount;
	}

	/**
	 * @return the answerTime
	 */
	public long getAnswerTime() {
		return answerTime;
	}

	/**
	 * @param answerTime
	 *            the answerTime to set
	 */
	public void setAnswerTime(long answerTime) {
		this.answerTime = answerTime;
	}
}
