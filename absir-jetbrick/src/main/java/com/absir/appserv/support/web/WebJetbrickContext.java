/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月3日 下午4:47:52
 */
package com.absir.appserv.support.web;

import jetbrick.template.JetContext;
import jetbrick.template.JetEngine;
import jetbrick.template.runtime.JetPageContext;

/**
 * @author absir
 *
 */
public class WebJetbrickContext extends JetPageContext {

	/** engine */
	private JetEngine engine;

	/**
	 * @param context
	 * @param engine
	 */
	public WebJetbrickContext(JetContext context, JetEngine engine) {
		super(null, context, null);
		// TODO Auto-generated constructor stub
		this.engine = engine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jetbrick.template.runtime.JetPageContext#getEngine()
	 */
	@Override
	public JetEngine getEngine() {
		// TODO Auto-generated method stub
		return engine;
	}
}
