/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-23 下午2:56:51
 */
package com.absir.appserv.client.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.absir.appserv.configure.xls.value.XaReferenced;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.dto.IBeanLazySerializer;
import com.absir.appserv.system.bean.proxy.JpUpdate;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaColum;
import com.absir.validator.value.NotEmpty;

/**
 * @author absir
 * 
 */
@MaEntity(parent = { @MaMenu("题库管理") }, name = "题目")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
public class JQuestion extends JbBean implements JpUpdate {

	@JaLang("题目分类")
	@JsonSerialize(using = IBeanLazySerializer.class)
	@JaEdit(groups = JaEdit.GROUP_LIST)
	@ManyToOne
	@XaReferenced(value = false)
	private JQuestionCategory category;

	@JaLang("标题")
	@JaEdit(groups = JaEdit.GROUP_SUGGEST)
	@NotEmpty
	private String title;

	@JaLang("正确率")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private float difficult;

	@JaLang("最小等级")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	@JaColum(indexs = @Index(name = "minLevel", columnList = "minlevel,maxLevel"))
	private int minLevel;

	@JaLang("最大等级")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private int maxLevel;

	@JaLang("答案")
	@JsonIgnore
	@JaEdit(groups = JaEdit.GROUP_LIST)
	// @JaSubField("choices")
	private int correct;

	@JaLang("解释")
	private String answer;

	@JaLang("详细解释")
	@Column(length = 1024)
	@JaEdit(types = "text")
	private String answerDesc;

	@JaLang("选项A")
	@JaEdit(types = "text")
	private String choiceA;

	@JaLang("选项B")
	@JaEdit(types = "text")
	private String choiceB;

	@JaLang("选项C")
	@JaEdit(types = "text")
	private String choiceC;

	@JaLang("选项D")
	@JaEdit(types = "text")
	private String choiceD;

	/**
	 * @return the category
	 */
	public JQuestionCategory getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(JQuestionCategory category) {
		this.category = category;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the difficult
	 */
	public float getDifficult() {
		return difficult;
	}

	/**
	 * @param difficult
	 *            the difficult to set
	 */
	public void setDifficult(float difficult) {
		this.difficult = difficult;
	}

	/**
	 * @return the minLevel
	 */
	public int getMinLevel() {
		return minLevel;
	}

	/**
	 * @param minLevel
	 *            the minLevel to set
	 */
	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
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
	 * @return the correct
	 */
	public int getCorrect() {
		return correct;
	}

	/**
	 * @param correct
	 *            the correct to set
	 */
	public void setCorrect(int correct) {
		this.correct = correct;
	}

	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @param answer
	 *            the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * @return the answerDesc
	 */
	public String getAnswerDesc() {
		return answerDesc;
	}

	/**
	 * @param answerDesc
	 *            the answerDesc to set
	 */
	public void setAnswerDesc(String answerDesc) {
		this.answerDesc = answerDesc;
	}

	/**
	 * @return the choiceA
	 */
	public String getChoiceA() {
		return choiceA;
	}

	/**
	 * @param choiceA
	 *            the choiceA to set
	 */
	public void setChoiceA(String choiceA) {
		this.choiceA = choiceA;
	}

	/**
	 * @return the choiceB
	 */
	public String getChoiceB() {
		return choiceB;
	}

	/**
	 * @param choiceB
	 *            the choiceB to set
	 */
	public void setChoiceB(String choiceB) {
		this.choiceB = choiceB;
	}

	/**
	 * @return the choiceC
	 */
	public String getChoiceC() {
		return choiceC;
	}

	/**
	 * @param choiceC
	 *            the choiceC to set
	 */
	public void setChoiceC(String choiceC) {
		this.choiceC = choiceC;
	}

	/**
	 * @return the choiceD
	 */
	public String getChoiceD() {
		return choiceD;
	}

	/**
	 * @param choiceD
	 *            the choiceD to set
	 */
	public void setChoiceD(String choiceD) {
		this.choiceD = choiceD;
	}

	/**
	 * @param position
	 * @return
	 */
	public String getChoice(int position) {
		switch (position) {
		case 0:
			return choiceA;
		case 1:
			return choiceB;
		case 2:
			return choiceC;
		default:
			return choiceD;
		}
	}

	/**
	 * @param choice
	 * @param position
	 */
	public void setChoice(String choice, int position) {
		switch (position) {
		case 0:
			choiceA = choice;
			break;
		case 1:
			choiceB = choice;
			break;
		case 2:
			choiceC = choice;
			break;
		default:
			choiceD = choice;
			break;
		}
	}
}
