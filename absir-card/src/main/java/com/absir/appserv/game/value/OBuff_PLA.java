/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-5 下午2:34:52
 */
package com.absir.appserv.game.value;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class OBuff_PLA extends OBuffRound<OCard> {

	// 暂停回合数
	public int pla;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OBuffRound#stepRound(com.absir.appserv.game
	 * .value.OObject, long, int, com.absir.appserv.game.value.OResult)
	 */
	/**
	 * @return the pla
	 */
	public int getPla() {
		return pla;
	}

	/**
	 * @param pla
	 *            the pla to set
	 */
	public void setPla(int pla) {
		this.pla = pla;
	}

	@Override
	public void stepRound(OCard object, long time, int round, IResult result) {
		// TODO Auto-generated method stub
		--pla;
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
		if (pla > 0 && pla < object.paused) {
			object.paused -= pla;
		}
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
		pla = getRound();
		object.paused += pla;
	}
}
