/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月21日 上午11:34:42
 */
package com.absir.appserv.game.bean.value;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author absir
 *
 */
public interface IPropDefine {

	// 道具编号
	public Serializable getId();

	// 道具价格
	public int getPrice();

	// 道具宝石
	public int getDiamond();

	// 道具执行对象
	@JsonIgnore
	public Object getPropInvoker();

}
