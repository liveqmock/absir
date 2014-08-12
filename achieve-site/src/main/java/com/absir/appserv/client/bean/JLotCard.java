/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 下午2:30:48
 */
package com.absir.appserv.client.bean;

import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JiActive;

/**
 * @author absir
 * 
 */
@MaEntity(parent = @MaMenu("抽奖设置"), name = "卡组")
@Entity
public class JLotCard extends JbBean implements JiActive {

	@JaLang("名称")
	private String name;

	@JaLang("抽奖卡牌")
	@JaEdit(types = "paramText")
	@Type(type = "com.absir.appserv.system.bean.type.JtJsonDynamic")
	private int[] cardIds;

	@JaLang("开始时间")
	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	private long beginTime;

	@JaLang("过期时间")
	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	private long passTime;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
}
