/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 下午5:23:28
 */
package com.absir.appserv.client.api;

import java.util.Collection;

import com.absir.appserv.system.api.ApiServer;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.server.socket.ServerContext;
import com.absir.server.socket.SocketServerContext;
import com.absir.server.value.Server;

/**
 * @author absir
 * 
 */
@Server
public class Api_open extends ApiServer {

	@JaLang("服务列表")
	public Collection<ServerContext> getServerContexts() {
		return SocketServerContext.ME.getServerContexts();
	}
}
