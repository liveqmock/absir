/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-12 下午12:31:46
 */
package com.absir.appserv.system.asset;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.absir.appserv.system.configure.value.OAlipayOrder;
import com.absir.core.kernel.KernelString;
import com.absir.server.in.Input;
import com.absir.server.value.Attribute;
import com.absir.server.value.Before;
import com.absir.server.value.Body;
import com.absir.server.value.Server;
import com.alipay.sign.Signature;
import com.alipay.util.AlipayNotify;

/**
 * @author absir
 * 
 */
@Server
public class Asset_alipay extends AssetServer {

	/**
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Before
	protected Map<String, String> getAliParams(Input input) {
		Map<String, String> params = new HashMap<String, String>();
		for (Entry<String, String[]> entry : ((Map<String, String[]>) (Object) input.getParamMap()).entrySet()) {
			params.put(entry.getKey(), KernelString.implode(entry.getValue(), ','));
		}

		return params;
	}

	/**
	 * @param params
	 * @return
	 */
	private OAlipayOrder getAlipayOrder(Map<String, String> params) {
		OAlipayOrder alipayOrder = new OAlipayOrder();
		alipayOrder.outTradeNo = params.get("out_trade_no");
		alipayOrder.tradeNo = params.get("trade_no");
		alipayOrder.tradeStatus = params.get("trade_status");
		return alipayOrder;
	}

	/**
	 * @param params
	 * @return
	 */
	@Body
	public String notify(@Attribute Map<String, String> params) {
		if (AlipayNotify.verify(params, Signature.RSA)) {
			OAlipayOrder alipayOrder = getAlipayOrder(params);
//			JPayTrader trader = new JPayTrader();
//			trader.setId(alipayOrder.tradeNo);
//			trader.setOrderNo(alipayOrder.outTradeNo);
//			trader.setCreateTime(System.currentTimeMillis());
//			try {
//				BeanService.ME.persist(trader);
//				if (PayUtils.notify(trader)) {
//					return "success";
//				}
//
//			} catch (NonUniqueObjectException e) {
//				// TODO: handle exception
//			}
		}

		return "fail";
	}
}
