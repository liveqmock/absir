/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-3 下午5:58:37
 */
package com.absir.appserv.system.api;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.absir.appserv.feature.transaction.TransactionIntercepter;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.security.SecurityContext;
import com.absir.appserv.system.security.SecurityManager;
import com.absir.appserv.system.server.value.Bodys;
import com.absir.appserv.system.service.SecurityService;
import com.absir.appserv.system.service.impl.IdentityServiceLocal;
import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.server.exception.ServerException;
import com.absir.server.exception.ServerStatus;
import com.absir.server.in.Input;
import com.absir.server.in.Interceptor;
import com.absir.server.on.OnPut;
import com.absir.server.value.Before;
import com.absir.server.value.Interceptors;
import com.absir.server.value.Mapping;
import com.absir.server.value.OnException;
import com.absir.servlet.InputRequest;

/**
 * @author absir
 * 
 */
@Mapping("/api")
@Interceptors(ApiServer.Route.class)
public abstract class ApiServer {

	/** LOGGER */
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiServer.class);

	/**
	 * 统一返回类型 权限判断
	 * 
	 * @param input
	 * @throws Throwable
	 */
	@Bodys
	@Before
	protected SecurityContext onAuthentication(Input input) throws Throwable {
		return SecurityService.ME.getSecurityContext(input);
	}

	/**
	 * 消息对象
	 * 
	 * @author absir
	 * 
	 */
	public static class MessageCode {

		/** message */
		public String message;

		/** error */
		public int code;

		/**
		 * @param e
		 */
		public MessageCode(Throwable e) {
			message = e.toString();
			code = e instanceof ServerException ? ((ServerException) e).getServerStatus().getCode() : ServerStatus.ON_ERROR.getCode();
		}
	}

	/**
	 * 统一异常返回
	 * 
	 * @param e
	 * @return
	 */
	@Bodys
	@OnException(Throwable.class)
	protected Object onException(Throwable e, Input input) {
		input.setStatus(ServerStatus.ON_ERROR.getCode());
		if (BeanFactoryUtils.getEnvironment() == Environment.DEVELOP) {
			e.printStackTrace();
		}

		if (BeanFactoryUtils.getEnvironment().compareTo(Environment.DEBUG) <= 0 || input.isDebug() || !(e instanceof ServerException)) {
			LOGGER.error("", e);
		}

		return new MessageCode(e);
	}

	/**
	 * @author absir
	 * 
	 */
	public static class Route implements Interceptor {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.server.in.Interceptor#intercept(java.util.Iterator,
		 * com.absir.server.in.Input)
		 */
		@Override
		public OnPut intercept(Iterator<Interceptor> iterator, Input input) throws Throwable {
			// TODO Auto-generated method stub
			SecurityContext securityContext = SecurityService.ME.autoLogin("api", true, -1, input);
			if (securityContext == null && input instanceof InputRequest) {
				InputRequest inputRequest = (InputRequest) input;
				JiUserBase userBase = getInputUserBase(inputRequest);
				if (userBase != null) {
					SecurityManager securityManager = SecurityService.ME.getSecurityManager("api");
					long remember = securityManager.getSessionExpiration();
					if (remember < securityManager.getSessionLife()) {
						remember = securityManager.getSessionLife();
					}

					SecurityService.ME.loginUser(securityManager, userBase, remember, inputRequest);
				}
			}

			return input.intercept(iterator);
		}

		/**
		 * @param input
		 * @return
		 */
		protected JiUserBase getInputUserBase(InputRequest inputRequest) {
			return IdentityServiceLocal.getUserBase(inputRequest.getRequest().getHeader("identity"));
		}
	}

	/**
	 * @author absir
	 * 
	 */
	public static class TransactionRoute extends TransactionIntercepter {

	}
}
