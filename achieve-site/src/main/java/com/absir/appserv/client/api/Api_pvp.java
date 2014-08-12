/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-22 下午4:02:47
 */
package com.absir.appserv.client.api;

import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.service.MatcherService;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.bean.inject.value.Inject;
import com.absir.server.value.Attribute;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Server
public class Api_pvp extends PlayerServer {

	@Inject
	MatcherService matcherService;

	@JaLang("准备对战")
	public void sign(Long categoryId, @Attribute PlayerContext playerContext) {
		matcherService.sign(categoryId, playerContext);
	}

	@JaLang("退出对战")
	public void exit(Long categoryId, @Attribute PlayerContext playerContext) {
		matcherService.exit(categoryId, playerContext);
	}

	@JaLang("进入对战")
	public void attend(Long attendId, @Attribute PlayerContext playerContext) {
		matcherService.attend(attendId, playerContext);
	}
}
