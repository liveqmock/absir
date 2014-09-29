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
import com.absir.appserv.system.bean.JYiDongSession;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.bean.value.JePayStatus;
import com.absir.appserv.system.configure.JYiDongConfigure;
import com.absir.bean.inject.value.Bean;

/**
 * @author absir
 * 
 */
@Bean
public class YiDongService implements IdentityService, IPayInterface {

	/** DUO_KU_CONFIGURE */
	public static final JYiDongConfigure YI_DONG_CONFIGURE = JConfigureUtils.getConfigure(JYiDongConfigure.class);

	/** PLAT_FORM_NAME */
	public static final String PLAT_FORM_NAME = "YiDong";

	/**
	 * @param userId
	 * @param key
	 * @param cpId
	 * @param cpServiceId
	 * @param channelId
	 * @param p
	 * @param region
	 * @param Ua
	 * @return
	 */
	public int loginUser(String userId, String key, String cpId, String cpServiceId, String channelId, String p, String region, String Ua) {
		if (YI_DONG_CONFIGURE.getCpId() == null) {
			YI_DONG_CONFIGURE.setCpId(cpId);
			YI_DONG_CONFIGURE.setCpServiceId(cpServiceId);
			YI_DONG_CONFIGURE.merge();

		} else {
			if (!(YI_DONG_CONFIGURE.getCpId().equals(cpId) && YI_DONG_CONFIGURE.getCpServiceId().equals(cpServiceId))) {
				return 1;
			}
		}

		JYiDongSession yiDongSession = new JYiDongSession();
		yiDongSession.setId(key);
		yiDongSession.setUserId(userId);
		yiDongSession.setChannelId(channelId);
		yiDongSession.setP(p);
		yiDongSession.setRegion(region);
		yiDongSession.setUa(Ua);
		BeanService.ME.merge(yiDongSession);
		return 0;
	}

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
			JYiDongSession yiDongSession = BeanService.ME.get(JYiDongSession.class, sessionId);
			if (yiDongSession != null && yiDongSession.getUserId().equals(uid)) {
				return SecurityService.ME.openUserBase(uid, null, PLAT_FORM_NAME);
			}
		}

		return null;
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
		// 充值成功
		if (payTrade.getStatus() == JePayStatus.PAYED) {
			payTrade.setPlatform(PLAT_FORM_NAME);
			BeanService.ME.merge(payTrade);
			return true;
		}

		return false;
	}
}
