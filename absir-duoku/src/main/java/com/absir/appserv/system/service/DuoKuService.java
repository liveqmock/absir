/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-23 下午1:14:29
 */
package com.absir.appserv.system.service;

import com.absir.appserv.configure.JConfigureUtils;
import com.absir.appserv.system.bean.JPayTrade;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.value.JePayStatus;
import com.absir.appserv.system.bean.value.OErrorData;
import com.absir.appserv.system.configure.JDuoKuConfigure;
import com.absir.appserv.system.helper.HelperClient;
import com.absir.appserv.system.helper.HelperEncrypt;
import com.absir.appserv.system.pay.IPayInterface;
import com.absir.bean.inject.value.Bean;

/**
 * @author absir
 * 
 */
@Bean
public class DuoKuService implements IdentityService, IPayInterface {

	/** DUO_KU_CONFIGURE */
	public static final JDuoKuConfigure DUO_KU_CONFIGURE = JConfigureUtils.getConfigure(JDuoKuConfigure.class);

	/** CHECH_SESSION_URL */
	private static final String CHECH_SESSION_URL = "http://sdk.m.duoku.com/openapi/sdk/checksession";

	/** CARD_RESULT */
	private static final String CARD_RESULT = "http://sdk.m.duoku.com/openapi/sdk/duokoo_card_result";

	/** CARD_RESULT_SANDBOX */
	private static final String CARD_RESULT_SANDBOX = "http://sdk.m.duoku.com/openapi/sdk/duokoo_card_result";

	/** PLAT_FORM_NAME */
	public static final String PLAT_FORM_NAME = "DuoKu";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.service.IdentityService#getUserBase(java.lang
	 * .String[])
	 */
	@Override
	public JiUserBase getUserBase(String[] parameters) {
		// TODO Auto-generated method stub
		if (parameters.length == 3) {
			String uid = parameters[1];
			String sessionId = parameters[2];
			String clientsecret = DUO_KU_CONFIGURE.getAppid() + DUO_KU_CONFIGURE.getAppkey() + uid + sessionId + DUO_KU_CONFIGURE.getAppSecret();
			clientsecret = HelperEncrypt.encryptionMD5(clientsecret).toLowerCase();
			OErrorData errorData = HelperClient.openConnection(CHECH_SESSION_URL + "?appid=" + DUO_KU_CONFIGURE.getAppid() + "&appkey=" + DUO_KU_CONFIGURE.getAppkey() + "&uid=" + uid + "&sessionid="
					+ sessionId + "&clientsecret=" + clientsecret, null, OErrorData.class);
			if (errorData != null && errorData.error_code == 0) {
				return SecurityService.ME.openUserBase(PLAT_FORM_NAME + '@' + uid, null);
			}
		}

		return null;
	}

	/**
	 * @author absir
	 * 
	 */
	public static class ResultData extends OErrorData {

		public int Result;

		public int Amount;

		public String client_secret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.pay.IPayInterface#validator(com.absir.appserv
	 * .system.bean.JPayTrade)
	 */
	@Override
	public boolean validator(JPayTrade payTrade) {
		// TODO Auto-generated method stub
		String url = DUO_KU_CONFIGURE.isSandbox() ? CARD_RESULT_SANDBOX : CARD_RESULT;
		String orderId = payTrade.getId();
		String clientsecret = DUO_KU_CONFIGURE.getAppid() + DUO_KU_CONFIGURE.getAppkey() + orderId + DUO_KU_CONFIGURE.getAppSecret();
		clientsecret = HelperEncrypt.encryptionMD5(clientsecret).toLowerCase();
		ResultData resultData = HelperClient.openConnection(url + "?appid=" + DUO_KU_CONFIGURE.getAppid() + "&appkey=" + DUO_KU_CONFIGURE.getAppkey() + "&orderid=" + orderId + "&clientsecret="
				+ clientsecret, null, ResultData.class);
		if (resultData != null) {
			if ((int) Math.floor(payTrade.getAmount()) == resultData.Amount) {
				// 查询失败
				if (resultData.Result == 3) {
					return false;
				}

				// 充值失败
				if (resultData.Result == 2) {
					payTrade.setPlatform(PLAT_FORM_NAME);
					payTrade.setStatus(JePayStatus.ERROR);
					BeanService.ME.merge(payTrade);
					return false;
				}

				// 0 充值已提交 1 充值成功
				if (resultData.Result == 1 && payTrade.getStatus() != JePayStatus.PAYED) {
					payTrade.setPlatform(PLAT_FORM_NAME);
					payTrade.setStatus(JePayStatus.PAYED);
					BeanService.ME.merge(payTrade);
				}

				return true;
			}
		}

		return false;
	}
}
