/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-28 上午9:36:40
 */
package com.absir.appserv.game.bean;

import javax.persistence.MappedSuperclass;

import com.absir.appserv.game.bean.value.ICardDefine;
import com.absir.appserv.game.value.ILevelExp;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.value.JaLang;

/**
 * @author absir
 * 
 */
@MappedSuperclass
public abstract class JbCard extends JbBean implements ILevelExp {

	@JaLang("当前等级")
	private int level;

	@JaLang("经验值")
	private int exp;

	@JaLang("生命值")
	private int hp;

	@JaLang("攻击力")
	private int atk;

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the exp
	 */
	public int getExp() {
		return exp;
	}

	/**
	 * @param exp
	 *            the exp to set
	 */
	public void setExp(int exp) {
		this.exp = exp;
	}

	/**
	 * @return the hp
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * @param hp
	 *            the hp to set
	 */
	public void setHp(int hp) {
		this.hp = hp;
	}

	/**
	 * @return the atk
	 */
	public int getAtk() {
		return atk;
	}

	/**
	 * @param atk
	 *            the atk to set
	 */
	public void setAtk(int atk) {
		this.atk = atk;
	}

	/**
	 * @return
	 */
	public abstract JbPlayer getPlayer();

	/**
	 * @param player
	 */
	public abstract void setPlayer(JbPlayer player);

	/**
	 * @return
	 */
	public abstract ICardDefine getCardDefine();

	/**
	 * @param cardDefine
	 */
	public abstract void setCardDefine(ICardDefine cardDefine);

}
