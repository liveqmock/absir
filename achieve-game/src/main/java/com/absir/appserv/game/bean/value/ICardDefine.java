/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年10月20日 下午5:11:06
 */
package com.absir.appserv.game.bean.value;

/**
 * @author absir
 *
 */
public interface ICardDefine {

	/**
	 * 编号
	 * 
	 * @return
	 */
	public Integer getId();

	/**
	 * 品质
	 * 
	 * @return
	 */
	public int getRare();

	/**
	 * 抽奖率
	 * 
	 * @return
	 */
	public float getLotRare();

	/**
	 * 特殊抽奖
	 * 
	 * @return
	 */
	public float getLotSpecial();

	/**
	 * 池抽奖率
	 * 
	 * @return
	 */
	public float getLotPoolRare();

	/**
	 * 特殊池抽奖
	 * 
	 * @return
	 */
	public float getLotPoolSpecial();

	/**
	 * 最大等级
	 * 
	 * @return
	 */
	public int getMaxLevel();

	/**
	 * 出售价格
	 * 
	 * @return
	 */
	public int getPrice();

	/**
	 * 升级经验
	 * 
	 * @return
	 */
	public int getFeedExp();

	/**
	 * 升级金钱
	 * 
	 * @return
	 */
	public int getFeedPrice();

	/**
	 * 进化金钱
	 * 
	 * @return
	 */
	public int getEvolutionPrice();

	/**
	 * 进化需求
	 * 
	 * @return
	 */
	public ICardDefine[] getEvolutionRequires();

}
