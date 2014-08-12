/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 下午5:23:28
 */
package com.absir.appserv.client.api;

import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.service.ActivityService;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.server.value.Attribute;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Server
public class Api_activity extends PlayerServer {

	@JaLang("活动详情")
	public Object route(long id) {
		return ActivityService.ME.getActivity(id);
	}

	@JaLang("参加活动")
	public Object attend(long id, @Attribute PlayerContext playerContext) {
		return ActivityService.ME.attend(id, playerContext);
	}
}
