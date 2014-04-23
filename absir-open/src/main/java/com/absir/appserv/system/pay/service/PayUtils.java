/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 上午10:04:28
 */
package com.absir.appserv.system.pay.service;

import java.util.Map;

import com.absir.appserv.system.bean.JPayHistory;
import com.absir.appserv.system.bean.JPayTrade;
import com.absir.appserv.system.bean.value.JePayStatus;
import com.absir.appserv.system.pay.IPayInterface;
import com.absir.appserv.system.pay.IPayProccessor;
import com.absir.appserv.system.service.BeanService;
import com.absir.bean.basis.Configure;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.InjectType;

/**
 * @author absir
 * 
 */
@Configure
public abstract class PayUtils {

	/** payInterfaceMap */
	@Inject(value = "PayInterface", type = InjectType.Selectable)
	private static Map<String, IPayInterface> payInterfaceMap;

	/** payService */
	@Inject(type = InjectType.Selectable)
	private static IPayProccessor payService;

	/**
	 * @param platform
	 * @param payTrade
	 * @return
	 */
	public static boolean validator(String platform, JPayTrade payTrade) {
		JePayStatus status = payTrade.getStatus();
		if (status == null || status.compareTo(JePayStatus.ERROR) <= 0) {
			if (payInterfaceMap != null) {
				IPayInterface payInterface = payInterfaceMap.get(platform);
				if (payInterface != null) {
					return payInterface.validator(payTrade);
				}
			}

			return false;
		}

		return false;
	}

	/**
	 * @param payTrade
	 * @return
	 */
	public static Object proccess(JPayTrade payTrade) {
		if (payService != null) {
			try {
				JPayHistory payHistory = new JPayHistory();
				payHistory.setId(payTrade.getId());
				BeanService.ME.persist(payHistory);
				Object tradeObject = payService.proccess(payTrade);
				if (tradeObject != null) {
					payTrade.setStatus(JePayStatus.COMPLETE);
					BeanService.ME.merge(payTrade);
				}

				return tradeObject;

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return null;
	}

	/**
	 * @param platform
	 * @param payTrade
	 * @return
	 */
	public static Object proccess(String platform, JPayTrade payTrade) {
		if (validator(platform, payTrade)) {
			return proccess(payTrade);
		}

		return null;
	}

	/**
	 * @param payInterface
	 * @param payTrade
	 * @param payStatus
	 * @return
	 */
	public static Object notify(IPayInterface payInterface, JPayTrade payTrade, JePayStatus payStatus) {
		if (payStatus == JePayStatus.PAYED || payStatus == JePayStatus.COMPLETE) {
			if (payTrade.getStatus() != JePayStatus.COMPLETE) {
				return proccess(payTrade);
			}

		} else if (payTrade.getStatus() == null) {
			payTrade.setStatus(payStatus);
			BeanService.ME.merge(payTrade);
			return Boolean.TRUE;
		}

		return null;
	}
}
