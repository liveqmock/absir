/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 上午11:14:14
 */
package com.absir.appserv.client.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbBase;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaEntity;
import com.absir.orm.value.JePermission;
import com.absir.validator.value.NotEmpty;

/**
 * @author absir
 * 
 */
@JaEntity(permissions = JePermission.SELECT)
@MaEntity(parent = { @MaMenu("商品管理") }, name = "宝石")
@Entity
public class JDiamond extends JbBase {

	@JaLang("商品ID")
	@Id
	@NotEmpty
	private String id;

	@JaLang("宝石数量")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private int diamond;

	@JaLang("价格")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private float price;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}
}
