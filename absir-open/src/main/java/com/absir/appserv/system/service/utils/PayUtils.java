/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-14 上午10:04:28
 */
package com.absir.appserv.system.service.utils;

import java.util.Map;

import com.absir.appserv.system.bean.JPayHistory;
import com.absir.appserv.system.bean.JPayTrade;
import com.absir.appserv.system.bean.value.JePayStatus;
import com.absir.appserv.system.service.BeanService;
import com.absir.appserv.system.service.IPayInterface;
import com.absir.appserv.system.service.IPayProccessor;
import com.absir.bean.basis.Configure;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.InjectType;
import com.absir.core.kernel.KernelString;

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
				String tradeNo = payTrade.getTradeNo();
				if (KernelString.isEmpty(tradeNo)) {
					tradeNo = payTrade.getId();
				}

				JPayHistory payHistory = new JPayHistory();
				payHistory.setId(payTrade.getId());
				payHistory.setTradeNo(tradeNo);
				BeanService.ME.persist(payHistory);
				return payService.proccess(payTrade);

			} catch (Exception e) {
				// TODO: handle exception

			} finally {
				if (payTrade.getStatus() != JePayStatus.COMPLETE) {
					payTrade.setStatus(JePayStatus.COMPLETE);
					BeanService.ME.merge(payTrade);
				}
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
		if (payStatus != JePayStatus.COMPLETE) {
			if (payStatus == JePayStatus.PAYED) {
				return proccess(payTrade);

			} else if (payTrade.getStatus() == null) {
				payTrade.setStatus(payStatus);
				BeanService.ME.merge(payTrade);
				return Boolean.TRUE;
			}
		}

		return null;
	}
}
