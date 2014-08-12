/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-28 上午9:36:40
 */
package com.absir.appserv.client.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.absir.appserv.client.configure.xls.XCardDefine;
import com.absir.appserv.client.configure.xls.XSkillDefine;
import com.absir.appserv.game.value.ILevelExp;
import com.absir.appserv.system.bean.base.JbBean;
import com.absir.appserv.system.bean.dto.IBaseSerializer;
import com.absir.appserv.system.bean.dto.IBeanLazySerializer;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.helper.HelperRandom;

/**
 * @author absir
 * 
 */
@Entity
public class JCard extends JbBean implements ILevelExp {

	@JaLang("所属玩家")
	@JsonSerialize(using = IBeanLazySerializer.class)
	@ManyToOne(fetch = FetchType.LAZY)
	private JPlayer player;

	@JaLang("基础卡牌")
	@JsonSerialize(using = IBaseSerializer.class)
	private XCardDefine cardDefine;

	@JaLang("当前等级")
	private int level;

	@JaLang("经验值")
	private int exp;

	@JaLang("生命值")
	private int hp;

	@JaLang("生命成长")
	private float hpf;

	@JaLang("攻击力")
	private int atk;

	@JaLang("攻击成长")
	private float atkf;

	@JaLang("水性")
	private int water;

	@JaLang("水性成长")
	private float waterf;

	@JaLang("火性")
	private int fire;

	@JaLang("火性成长")
	private float firef;

	@JaLang("雷性")
	private int thunder;

	@JaLang("雷性成长")
	private float thunderf;

	@JaLang("技能卡槽")
	private int skillm;

	@JaLang("技能列表")
	@ElementCollection(fetch = FetchType.EAGER)
	private List<JbSkill> skills;

	/**
	 * @return the player
	 */
	public JPlayer getPlayer() {
		return player;
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(JPlayer player) {
		this.player = player;
	}

	/**
	 * @return the cardDefine
	 */
	public XCardDefine getCardDefine() {
		return cardDefine;
	}

	/**
	 * @param cardDefine
	 *            the cardDefine to set
	 */
	public void setCardDefine(XCardDefine cardDefine) {
		this.cardDefine = cardDefine;
	}

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
	 * @return the hpf
	 */
	public float getHpf() {
		return hpf;
	}

	/**
	 * @param hpf
	 *            the hpf to set
	 */
	public void setHpf(float hpf) {
		this.hpf = hpf;
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
	 * @return the atkf
	 */
	public float getAtkf() {
		return atkf;
	}

	/**
	 * @param atkf
	 *            the atkf to set
	 */
	public void setAtkf(float atkf) {
		this.atkf = atkf;
	}

	/**
	 * @return the water
	 */
	public int getWater() {
		return water;
	}

	/**
	 * @param water
	 *            the water to set
	 */
	public void setWater(int water) {
		this.water = water;
	}

	/**
	 * @return the waterf
	 */
	public float getWaterf() {
		return waterf;
	}

	/**
	 * @param waterf
	 *            the waterf to set
	 */
	public void setWaterf(float waterf) {
		this.waterf = waterf;
	}

	/**
	 * @return the fire
	 */
	public int getFire() {
		return fire;
	}

	/**
	 * @param fire
	 *            the fire to set
	 */
	public void setFire(int fire) {
		this.fire = fire;
	}

	/**
	 * @return the firef
	 */
	public float getFiref() {
		return firef;
	}

	/**
	 * @param firef
	 *            the firef to set
	 */
	public void setFiref(float firef) {
		this.firef = firef;
	}

	/**
	 * @return the thunder
	 */
	public int getThunder() {
		return thunder;
	}

	/**
	 * @param thunder
	 *            the thunder to set
	 */
	public void setThunder(int thunder) {
		this.thunder = thunder;
	}

	/**
	 * @return the thunderf
	 */
	public float getThunderf() {
		return thunderf;
	}

	/**
	 * @param thunderf
	 *            the thunderf to set
	 */
	public void setThunderf(float thunderf) {
		this.thunderf = thunderf;
	}

	/**
	 * @return the skillm
	 */
	public int getSkillm() {
		return skillm;
	}

	/**
	 * @param skillm
	 *            the skillm to set
	 */
	public void setSkillm(int skillm) {
		this.skillm = skillm;
	}

	/**
	 * @return the skills
	 */
	public List<JbSkill> getSkills() {
		return skills;
	}

	/**
	 * @param skills
	 *            the skills to set
	 */
	public void setSkills(List<JbSkill> skills) {
		this.skills = skills;
	}

	/**
	 * @param skillDefine
	 */
	public synchronized void addSkill(XSkillDefine skillDefine) {
		if (skills == null) {
			skills = new ArrayList<JbSkill>();
		}

		JbSkill skill = new JbSkill();
		skill.setSkillDefine(skillDefine);
		skills.add(skill);
	}

	/**
	 * @param skillDefine
	 */
	public synchronized void replaceSkill(XSkillDefine skillDefine) {
		if (skills != null) {
			int size = skills.size();
			if (size > 0) {
				int rnd = HelperRandom.nextInt(size);
				int r = -1;
				int f = -1;
				JbSkill skill;
				for (int i = 0; i < size; i++) {
					skill = skills.get(i);
					if (!skill.isLocked()) {
						if (r == rnd) {
							r = i;

						} else if (r == -1) {
							r = i;
						}
					}
				}

				if (f >= 0) {
					r = f;
				}

				skill = skills.get(f < 0 ? r : f);
				skill.setSkillDefine(skillDefine);
			}
		}
	}
}
