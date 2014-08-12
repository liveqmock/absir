/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-26 下午5:11:34
 */
package com.absir.appserv.client.configure.xls;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.absir.appserv.client.bean.value.JeCamp;
import com.absir.appserv.configure.xls.XlsBeanUpdate;
import com.absir.appserv.system.bean.dto.EnumSerializer;
import com.absir.appserv.system.bean.dto.IBaseSerializer;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.orm.value.JaEntity;
import com.absir.orm.value.JePermission;

/**
 * @author absir
 * 
 */
@JaEntity(permissions = JePermission.SELECT)
public class XCardDefine extends XlsBeanUpdate<Integer> {

	@JaLang("卡牌名称")
	private String name;

	@JaLang("卡片介绍")
	private String desc;

	@JaLang("阵营")
	@JsonSerialize(using = EnumSerializer.class)
	private JeCamp camp;

	@JaLang("稀有度")
	private int rare;

	@JaLang("最大等级")
	private transient int maxLevel;

	@JaLang("最大技能卡槽")
	private transient int maxSkill;

	@JaLang("掉落概率")
	private float drop;

	@JaLang("领导力")
	private int cost;

	@JaLang("基础生命值")
	private int hp;

	@JaLang("最小生命值")
	private int minHp;

	@JaLang("最大生命值")
	private int maxHp;

	@JaLang("基础攻击力")
	private int atk;

	@JaLang("最小攻击力")
	private int minAtk;

	@JaLang("最大攻击力")
	private int maxAtk;

	@JaLang("基本水防")
	private int water;

	@JaLang("最大水防")
	private int minWater;

	@JaLang("最大水防")
	private int maxWater;

	@JaLang("基础火防")
	private int fire;

	@JaLang("最小火防")
	private int minFire;

	@JaLang("最大火防")
	private int maxFire;

	@JaLang("基础雷防")
	private int thunder;

	@JaLang("最小雷防")
	private int minThunder;

	@JaLang("最大雷防")
	private int maxThunder;

	@JaLang("价格")
	private int price;

	@JaLang("经验")
	private int exp;

	@JaLang("升级费用")
	private int levelPrice;

	@JaLang("进化编号")
	@JsonSerialize(using = IBaseSerializer.class)
	private XCardDefine evolution;

	@JaLang("进化费用")
	private int evolutionPrice;

	@JaLang("进化素材")
	// @XaParam
	@JsonSerialize(contentUsing = IBaseSerializer.class)
	private XCardDefine[] evolutionRequires;

	@JaLang("进化来源")
	@JsonIgnore
	private transient XCardDefine evolutionFrom;

	@JaLang("进化序列")
	private transient List<Integer> evolutionQueue;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @return the rare
	 */
	public int getRare() {
		return rare;
	}

	/**
	 * @param rare
	 */
	protected void setRare(int rare) {
		this.rare = rare;
		maxLevel = 30 + rare * 10;
		maxSkill = 1 + rare / 2;
	}

	/**
	 * @return the maxLevel
	 */
	public int getMaxLevel() {
		return maxLevel;
	}

	/**
	 * @return the maxSkill
	 */
	public int getMaxSkill() {
		return maxSkill;
	}

	/**
	 * @return the drop
	 */
	public float getDrop() {
		return drop;
	}

	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * @return the hp
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * @return the minHp
	 */
	public int getMinHp() {
		return minHp;
	}

	/**
	 * @return the maxHp
	 */
	public int getMaxHp() {
		return maxHp;
	}

	/**
	 * @return the atk
	 */
	public int getAtk() {
		return atk;
	}

	/**
	 * @return the minAtk
	 */
	public int getMinAtk() {
		return minAtk;
	}

	/**
	 * @return the maxAtk
	 */
	public int getMaxAtk() {
		return maxAtk;
	}

	/**
	 * @return the water
	 */
	public int getWater() {
		return water;
	}

	/**
	 * @return the minWater
	 */
	public int getMinWater() {
		return minWater;
	}

	/**
	 * @return the maxWater
	 */
	public int getMaxWater() {
		return maxWater;
	}

	/**
	 * @return the fire
	 */
	public int getFire() {
		return fire;
	}

	/**
	 * @return the minFire
	 */
	public int getMinFire() {
		return minFire;
	}

	/**
	 * @return the maxFire
	 */
	public int getMaxFire() {
		return maxFire;
	}

	/**
	 * @return the thunder
	 */
	public int getThunder() {
		return thunder;
	}

	/**
	 * @return the minThunder
	 */
	public int getMinThunder() {
		return minThunder;
	}

	/**
	 * @return the maxThunder
	 */
	public int getMaxThunder() {
		return maxThunder;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @return the camp
	 */
	public JeCamp getCamp() {
		return camp;
	}

	/**
	 * @return the exp
	 */
	public int getExp() {
		return exp;
	}

	/**
	 * @return the levelPrice
	 */
	public int getLevelPrice() {
		return levelPrice;
	}

	/**
	 * @return the evolution
	 */
	public XCardDefine getEvolution() {
		return evolution;
	}

	/**
	 * @param evolution
	 *            the evolution to set
	 */
	protected void setEvolution(XCardDefine evolution) {
		this.evolution = evolution;
		if (evolution != null) {
			evolution.evolutionFrom = this;
		}
	}

	/**
	 * @return the evolutionPrice
	 */
	public int getEvolutionPrice() {
		return evolutionPrice;
	}

	/**
	 * @return the evolutionRequires
	 */
	public XCardDefine[] getEvolutionRequires() {
		return evolutionRequires;
	}

	/**
	 * @return the evolutionFrom
	 */
	@JsonIgnore
	public XCardDefine getEvolutionFrom() {
		return evolutionFrom;
	}

	/**
	 * @return the evolutionQueue
	 */
	public List<Integer> getEvolutionQueue() {
		if (evolutionQueue == null) {
			XCardDefine root = this;
			while (root.evolutionFrom != null) {
				root = root.evolutionFrom;
			}

			if (root == this) {
				evolutionQueue = new ArrayList<Integer>();
				while (root != null) {
					Integer id = root.getId();
					if (evolutionQueue.contains(id)) {
						break;
					}

					evolutionQueue.add(id);
					root = root.evolution;
				}

			} else {
				evolutionQueue = root.getEvolutionQueue();
			}
		}

		return evolutionQueue;
	}
}
