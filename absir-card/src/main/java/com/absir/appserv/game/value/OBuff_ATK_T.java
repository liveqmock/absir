/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-22 上午10:26:40
 */
package com.absir.appserv.game.value;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public class OBuff_ATK_T extends OBuffRound<OCard> {

	// 提升攻击力
	private int atkT;

	// 攻击提升累计
	private int atkTR;

	/**
	 * @return the atkT
	 */
	public int getAtkT() {
		return atkT;
	}

	/**
	 * @param atkT
	 *            the atkT to set
	 */
	public void setAtkT(int atkT) {
		this.atkT = atkT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuffRound#stepRound(com.absir.appserv.game
	 * .value.OObject, long, int, com.absir.appserv.game.value.OResult)
	 */
	@Override
	public void stepRound(OCard object, long time, int round, IResult result) {
		// TODO Auto-generated method stub
		atkTR += atkT;
		object.setAtk((int) object.getBuffAtt("atk", object.baseAtk(), atkT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuffReverse#revert(com.absir.appserv.game
	 * .value.OObject, com.absir.appserv.game.value.OResult)
	 */
	@Override
	public void revert(OCard object, IResult result) {
		// TODO Auto-generated method stub
		object.setAtk((int) object.getBuffAtt("atk", object.baseAtk(), -atkTR));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuff#effect(com.absir.appserv.game.value
	 * .OObject, com.absir.appserv.game.value.OResult)
	 */
	@Override
	public void effect(OCard object, IResult result) {
		// TODO Auto-generated method stub
	}

}
