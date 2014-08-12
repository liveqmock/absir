/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-12-27 上午10:16:13
 */
package com.absir.appserv.client.context.value;

import java.io.Serializable;

import com.absir.appserv.client.bean.JCard;
import com.absir.appserv.game.value.IResult;

/**
 * @author absir
 * 
 */
public class OCardAgainst extends OCardPlayer {

	/** ssCorrect */
	int ssCorrect;

	/**
	 * @param oFightBase
	 * @param id
	 * @param card
	 */
	public OCardAgainst(OFightBase oFightBase, Serializable id, JCard card) {
		super(oFightBase, id, card);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.game.value.OCard#inTarget()
	 */
	@Override
	public boolean inTarget() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.game.value.OCard#atk()
	 */
	@Override
	public boolean atk() {
		return paused == 0 || --paused == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.game.value.OObject#die(com.absir.appserv.game.value
	 * .IResult)
	 */
	@Override
	public void die(IResult result) {
	}
}
