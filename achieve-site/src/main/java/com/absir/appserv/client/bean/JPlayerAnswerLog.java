/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-3 上午10:28:47
 */
package com.absir.appserv.client.bean;

import javax.persistence.Entity;
import javax.persistence.Index;

import com.absir.appserv.system.bean.JEmbedLL;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaColum;

/**
 * @author absir
 * 
 */
@Entity
public class JPlayerAnswerLog extends JbBean {

	@JaColum(indexs = @Index(name = "embedLL", columnList = "eid,mid"))
	private JEmbedLL embedLL;

	@JaLang("回答选项")
	private int answer;

	@JaLang("回答时间")
	private long answerTime;

	@JaLang("答题时间")
	private long createTime;

	/**
	 * @return the embedLL
	 */
	public JEmbedLL getEmbedLL() {
		return embedLL;
	}

	/**
	 * @param embedLL
	 *            the embedLL to set
	 */
	public void setEmbedLL(JEmbedLL embedLL) {
		this.embedLL = embedLL;
	}

	/**
	 * @return the answer
	 */
	public int getAnswer() {
		return answer;
	}

	/**
	 * @param answer
	 *            the answer to set
	 */
	public void setAnswer(int answer) {
		this.answer = answer;
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

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
