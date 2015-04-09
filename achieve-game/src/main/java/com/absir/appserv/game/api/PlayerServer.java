/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-11 上午10:56:52
 */
package com.absir.appserv.game.api;

import com.absir.appserv.game.context.JbPlayerContext;
import com.absir.appserv.game.context.PlayerServiceBase;
import com.absir.appserv.system.api.ApiServer;
import com.absir.appserv.system.security.SecurityContext;
import com.absir.context.core.ContextUtils;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.in.Input;
import com.absir.server.socket.InputSocketImpl;

/**
 * @author absir
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
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
			if (input instanceof InputSocketImpl) {
				Long playerId = (Long) input.getId();
				JbPlayerContext playerContext = ContextUtils.getContext(JbPlayerContext.COMPONENT.PLAYER_CONTEXT_CLASS, playerId);
				setPlayerContext(input, playerContext);
				return null;
			}

			throw new ServerException(ServerStatus.NO_LOGIN);
		}

		Long playerId = PlayerServiceBase.ME.getPlayerId(null, securityContext.getUser());
		if (playerId == null) {
			throw new ServerException(ServerStatus.NO_LOGIN);
		}

		JbPlayerContext playerContext = ContextUtils.getContext(JbPlayerContext.COMPONENT.PLAYER_CONTEXT_CLASS, playerId);
		if (playerContext.getPlayer().getCard() == 0) {
			throw new ServerException(ServerStatus.NO_LOGIN);
		}

		setPlayerContext(input, playerContext);
		return securityContext;
	}

	/**
	 * @param input
	 * @return
	 */
	public static JbPlayerContext getPlayerContext(Input input) {
		Object playerContext = input.getAttribute("playerContext");
		return playerContext == null || !(playerContext instanceof JbPlayerContext) ? null : (JbPlayerContext) playerContext;
	}

	/**
	 * @param input
	 * @param playerContext
	 */
	public static void setPlayerContext(Input input, JbPlayerContext playerContext) {
		input.setAttribute("playerContext", playerContext);
	}
}
