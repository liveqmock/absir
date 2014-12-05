/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年12月4日 下午10:21:19
 */
package com.absir.appserv.game.bean;

import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 *
 */
@MaEntity(parent = @MaMenu("抽奖设置"), name = "奖池")
@MappedSuperclass
public class JbLotPool extends JbLotCard {

	@JaLang("道具编号")
	@JaEdit(types = "paramText")
	@Type(type = "com.absir.appserv.system.bean.type.JtJsonDynamic")
	private int[] propIds;

}
