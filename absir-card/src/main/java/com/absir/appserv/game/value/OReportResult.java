/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-6 下午2:03:29
 */
package com.absir.appserv.game.value;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author absir
 * 
 */
public class OReportResult extends OReport implements IResult {

	/** done */
	@JsonIgnore
	private boolean done;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.game.value.IResult#isDone()
	 */
	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return done;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.game.value.IResult#setDone(boolean)
	 */
	@Override
	public void setDone(boolean done) {
		// TODO Auto-generated method stub
		this.done = done;
	}

}
