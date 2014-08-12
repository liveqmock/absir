/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-11 上午10:56:52
 */
package com.absir.appserv.client.api;

import com.absir.appserv.client.context.PlayerContext;
import com.absir.appserv.client.context.PlayerService;
import com.absir.appserv.system.api.ApiServer;
import com.absir.appserv.system.security.SecurityContext;
import com.absir.context.core.ContextUtils;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.in.Input;
import com.absir.server.socket.InputSocket;

/**
 * @author absir
 * 
 */
public class PlayerServer extends ApiServer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.api.MvcApi#onAuthentication(javax.servlet.http
	 * .HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected SecurityContext onAuthentication(Input input) throws Throwable {
		SecurityContext securityContext = super.onAuthentication(input);
		if (securityContext == null) {
			if (input instanceof InputSocket) {
				Long playerId = (Long) input.getId();
				setPlayerContext(input, ContextUtils.getContext(PlayerContext.class, playerId));
				return null;
			}

			throw new ServerException(ServerStatus.NO_LOGIN);
		}

		Long playerId = PlayerService.ME.getPlayerId(null, securityContext.getUser());
		if (playerId == null) {
			throw new ServerException(ServerStatus.NO_LOGIN);
		}

		PlayerContext playerContext = ContextUtils.getContext(PlayerContext.class, playerId);
		if (playerContext.getPlayer().getCard0() == null) {
			throw new ServerException(ServerStatus.NO_LOGIN);
		}

		setPlayerContext(input, playerContext);
		return securityContext;
	}

	/**
	 * @param input
	 * @return
	 */
	public static PlayerContext getPlayerContext(Input input) {
		Object playerContext = input.getAttribute("playerContext");
		return playerContext == null || !(playerContext instanceof PlayerContext) ? null : (PlayerContext) playerContext;
	}

	/**
	 * @param input
	 * @param playerContext
	 */
	public static void setPlayerContext(Input input, PlayerContext playerContext) {
		input.setAttribute("playerContext", playerContext);
	}
}
