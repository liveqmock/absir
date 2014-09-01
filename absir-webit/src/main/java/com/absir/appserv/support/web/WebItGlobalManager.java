/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月24日 下午1:02:10
 */
package com.absir.appserv.support.web;

import webit.script.Engine;
import webit.script.global.DefaultGlobalManager;
import webit.script.global.GlobalRegister;

/**
 * @author absir
 *
 */
public class WebItGlobalManager extends DefaultGlobalManager {

	/** globalRegisters */
	protected static GlobalRegister[] globalRegisters;

	// protected static Map<String, Object> globalResiter

	/*
	 * (non-Javadoc)
	 * 
	 * @see webit.script.global.DefaultGlobalManager#init(webit.script.Engine)
	 */
	@Override
	public void init(Engine engine) {
		super.init(engine);
		if (globalRegisters != null) {
			for (GlobalRegister globalRegister : globalRegisters) {
				globalRegister.regist(this);
			}
		}
	}
}
