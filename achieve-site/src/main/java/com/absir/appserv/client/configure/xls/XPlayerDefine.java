/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-26 下午3:51:51
 */
package com.absir.appserv.client.configure.xls;

import com.absir.appserv.configure.xls.XlsBaseUpdate;
import com.absir.appserv.game.value.IExp;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaEntity;
import com.absir.orm.value.JePermission;

/**
 * @author absir
 * 
 */
@JaEntity(permissions = JePermission.SELECT)
public class XPlayerDefine extends XlsBaseUpdate implements IExp {

	@JaLang("升级经验")
	private int exp;

	@JaLang("领导力")
	private int cost;

	@JaLang("行动力")
	private int ep;

	@JaLang("卡牌数量")
	private int cardNumber;

	@JaLang("好友数量")
	private int friendNumber;

	/**
	 * @return the exp
	 */
	public int getExp() {
		return exp;
	}

	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * @return the ep
	 */
	public int getEp() {
		return ep;
	}

	/**
	 * @return the CardNumber
	 */
	public int getCardNumber() {
		return cardNumber;
	}

	/**
	 * @return the friendNumber
	 */
	public int getFriendNumber() {
		return friendNumber;
	}
}
