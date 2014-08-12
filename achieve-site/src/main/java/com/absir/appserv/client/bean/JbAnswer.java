/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-7 下午5:15:14
 */
package com.absir.appserv.client.bean;

import javax.persistence.MappedSuperclass;

import com.absir.appserv.system.bean.base.JbBase;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@MappedSuperclass
public abstract class JbAnswer extends JbBase {

	@JaLang("答题总数")
	private long answerCount;

	@JaLang("答题正确数")
	private long answerCorrect;

	@JaLang("答题总时间")
	private long answerTime;

	/**
	 * @return the answerCount
	 */
	public long getAnswerCount() {
		return answerCount;
	}

	/**
	 * @param answerCount
	 *            the answerCount to set
	 */
	public void setAnswerCount(long answerCount) {
		this.answerCount = answerCount;
	}

	/**
	 * @return the answerCorrect
	 */
	public long getAnswerCorrect() {
		return answerCorrect;
	}

	/**
	 * @param answerCorrect
	 *            the answerCorrect to set
	 */
	public void setAnswerCorrect(long answerCorrect) {
		this.answerCorrect = answerCorrect;
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
