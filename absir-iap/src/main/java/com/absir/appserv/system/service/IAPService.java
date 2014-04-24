/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-4-24 上午10:39:54
 */
package com.absir.appserv.system.service;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import com.absir.appserv.configure.JConfigureUtils;
import com.absir.appserv.system.bean.JPayTrade;
import com.absir.appserv.system.bean.value.JePayStatus;
import com.absir.appserv.system.configure.JIAPConfigure;
import com.absir.appserv.system.helper.HelperClient;
import com.absir.bean.inject.value.Bean;

/**
 * @author absir
 * 
 */
@Bean
public class IAPService implements IPayInterface {

	/** IAP_CONFIGURE */
	public static final JIAPConfigure IAP_CONFIGURE = JConfigureUtils.getConfigure(JIAPConfigure.class);

	/** PLAT_FORM_NAME */
	public static final String PLAT_FORM_NAME = "IAP";

	/** VERIFY_RECEIPT */
	private static final String VERIFY_RECEIPT = "https://buy.itunes.apple.com/verifyReceipt";

	/** VERIFY_RECEIPT_SANDBOX */
	private static final String VERIFY_RECEIPT_SANDBOX = "https://sandbox.itunes.apple.com/verifyReceipt";

	/**
	 * @param productId
	 * @param quantity
	 * @return
	 */
	public static String getPlatformData(String productId, Object quantity) {
		return productId + "," + quantity;
	}

	/**
	 * @author absir
	 * 
	 */
	public static class ItunesTradeInfo {

		/** status */
		public int status;

		/** receipt */
		public ItunesReceipt receipt;
	}

	/**
	 * @author absir
	 * 
	 */
	public static class ItunesReceipt {

		public String unique_identifier;

		public String transaction_id;

		public String original_transaction_id;

		public int item_id;

		public String bid;

		public String product_id;

		public int quantity;

		public String unique_vendor_identifier;

		public float bvrs;

		// public Date purchase_date;

		public long purchase_date_ms;

		// public Date original_purchase_date;

		// public Date purchase_date_pst;

		public long original_purchase_date_ms;

		// public Date original_purchase_date_pst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.service.IPayInterface#validator(com.absir.appserv
	 * .system.bean.JPayTrade)
	 */
	@Override
	public boolean validator(JPayTrade payTrade) {
		// TODO Auto-generated method stub
		if (payTrade.getPlatform() == null || !payTrade.getPlatform().equals(PLAT_FORM_NAME)) {
			return false;
		}

		try {
			URL url = new URL(IAP_CONFIGURE.isSandbox() ? VERIFY_RECEIPT_SANDBOX : VERIFY_RECEIPT);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("content-type", "text/json");
			urlConnection.setRequestProperty("Proxy-Connection", "Keep-Alive");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
			out.write(String.format(Locale.CHINA, "{\"receipt-data\":\"" + payTrade.getTradeNo() + "\"}"));
			out.flush();
			out.close();
			// 获取请求对象
			ItunesTradeInfo itunesReceipt = HelperClient.openConnectionJson(urlConnection, ItunesTradeInfo.class);
			if (itunesReceipt.status == 0) {
				if (IAP_CONFIGURE.equals(itunesReceipt.receipt.bid)) {
					if (getPlatformData(itunesReceipt.receipt.product_id, itunesReceipt.receipt.quantity).equals(payTrade.getPlatformData())) {
						if (payTrade.getStatus() != JePayStatus.PAYED) {
							payTrade.setStatus(JePayStatus.PAYED);
							BeanService.ME.merge(payTrade);
						}

						return true;
					}
				}
			}

		} catch (Throwable e) {
		}

		payTrade.setStatus(JePayStatus.ERROR);
		BeanService.ME.merge(payTrade);
		return false;
	}

}
