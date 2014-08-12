/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-2-25 上午10:24:04
 */
package com.absir.appserv.client.bean;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.absir.appserv.client.bean.value.JeRepetition;
import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.dto.EnumSerializer;
import com.absir.appserv.system.bean.proxy.JpUpdate;
import com.absir.appserv.system.bean.value.JaCrud;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JiActiveRepetition;
import com.absir.appserv.system.crud.UploadCrudFactory;
import com.absir.orm.value.JaEntity;
import com.absir.orm.value.JePermission;

/**
 * @author absir
 * 
 */
@JaEntity(permissions = JePermission.SELECT)
@MaEntity(parent = { @MaMenu("活动管理") }, name = "活动")
@Entity
public class JActivity extends JbBean implements JiActiveRepetition, JpUpdate {

	@JaLang("活动基本信息")
	private JActivityBase activityBase;

	@JaLang("重复类型")
	@Enumerated
	@JsonSerialize(using = EnumSerializer.class)
	private JeRepetition repetition;

	@JaLang("开始时间")
	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	private long beginTime;

	@JaLang("过期时间")
	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	private long passTime;

	@JaLang("活动背景")
	@JaEdit(types = "file")
	@JaCrud(factory = UploadCrudFactory.class, cruds = { Crud.CREATE, Crud.UPDATE, Crud.DELETE }, parameters = { "2048", "png" })
	private String backgroud;

	@JaLang("关联题库")
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	private List<JQuestion> questions;

	/**
	 * @return the activityBase
	 */
	public JActivityBase getActivityBase() {
		return activityBase;
	}

	/**
	 * @param activityBase
	 *            the activityBase to set
	 */
	public void setActivityBase(JActivityBase activityBase) {
		this.activityBase = activityBase;
	}

	/**
	 * @return the repetition
	 */
	public JeRepetition getRepetition() {
		return repetition;
	}

	/**
	 * @param repetition
	 *            the repetition to set
	 */
	public void setRepetition(JeRepetition repetition) {
		this.repetition = repetition;
	}

	/**
	 * @return the beginTime
	 */
	public long getBeginTime() {
		return beginTime;
	}

	/**
	 * @param beginTime
	 *            the beginTime to set
	 */
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * @return the passTime
	 */
	public long getPassTime() {
		return passTime;
	}

	/**
	 * @param passTime
	 *            the passTime to set
	 */
	public void setPassTime(long passTime) {
		this.passTime = passTime;
	}

	/**
	 * @return the backgroud
	 */
	public String getBackgroud() {
		return backgroud;
	}

	/**
	 * @param backgroud
	 *            the backgroud to set
	 */
	public void setBackgroud(String backgroud) {
		this.backgroud = backgroud;
	}

	/**
	 * @return the questions
	 */
	public List<JQuestion> getQuestions() {
		return questions;
	}

	/**
	 * @param questions
	 *            the questions to set
	 */
	public void setQuestions(List<JQuestion> questions) {
		this.questions = questions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.bean.value.JiActiveRepetition#getNextPassTime()
	 */
	@Override
	public long getNextPassTime(long contextTime) {
		// TODO Auto-generated method stub
		return repetition == null ? passTime : repetition.getNextTime(passTime, contextTime);
	}
}
