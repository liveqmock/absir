/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月21日 上午11:34:42
 */
package com.absir.appserv.game.bean.value;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author absir
 *
 */
public interface IPropDefine {

	// 编号
	public String getId();

	// 道具价格
	public int getPrice();

	// 道具宝石
	public int getDiamond();

	// 品质
	public int getRare();

	// 池抽奖率
	public float getLotPoolRare();

	// 特殊池抽奖
	public float getLotPoolSpecial();

	// 道具执行对象
	@JsonIgnore
	public Object getPropInvoker();

}
