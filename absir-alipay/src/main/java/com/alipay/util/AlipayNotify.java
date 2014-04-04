package com.alipay.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.absir.appserv.configure.JConfigureUtils;
import com.absir.appserv.system.configure.JAlipayConfigure;
import com.absir.core.kernel.KernelCharset;
import com.alipay.sign.Signature;

/* *
 *类名：AlipayNotify
 *功能：支付宝通知处理类
 *详细：处理支付宝各接口通知返回
 *版本：3.3
 *日期：2012-08-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考

 *************************注意*************************
 *调试通知返回时，可查看或改写log日志的写入TXT里的数据，来检查通知返回是否正常
 */
public class AlipayNotify {

	/**
	 * 支付宝消息验证地址
	 */
	private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

	/**
	 * 验证消息是否是支付宝发出的合法消息
	 * 
	 * @param params
	 *            通知返回来的参数数组
	 * @return 验证结果
	 */
	public static boolean verify(Map<String, String> params, Signature signature) {
		String sign = params.get("sign");
		if (sign == null) {
			return false;
		}

		if (verifySign(params, sign, signature)) {
			String notifyId = params.get("notify_id");
			if (notifyId != null) {
				return verifyNotifyId(notifyId);
			}

			return true;
		}

		return false;
	}

	/**
	 * 根据反馈回来的信息，生成签名结果
	 * 
	 * @param params
	 *            通知返回来的参数数组
	 * @param sign
	 *            比对的签名结果
	 * @return 生成的签名结果
	 */
	private static boolean verifySign(Map<String, String> params, String sign, Signature signature) {
		// 过滤空值、sign与sign_type参数
		Map<String, String> sParaNew = AlipayCore.filteParams(params);
		// 获取待签名字符串
		String preSignStr = AlipayCore.createLinkString(sParaNew);
		// 获得签名验证结果
		return signature.verify(preSignStr, sign, JConfigureUtils.getConfigure(JAlipayConfigure.class).getPublicKey(), KernelCharset.DEFAULT.displayName());
	}

	/**
	 * 获取远程服务器ATN结果,验证返回URL
	 * 
	 * @param notifyId
	 *            通知校验ID
	 * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
	 *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	 */
	private static boolean verifyNotifyId(String notifyId) {
		// 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
		String partner = JConfigureUtils.getConfigure(JAlipayConfigure.class).getPartner();
		String veryfy_url = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notifyId;
		notifyId = responseLine(veryfy_url);
		return notifyId != null && notifyId.equals("true");
	}

	/**
	 * 获取远程服务器ATN结果
	 * 
	 * @param aliUrl
	 *            指定URL路径地址
	 * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
	 *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	 */
	private static String responseLine(String aliUrl) {
		try {
			URL url = new URL(aliUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			return in.readLine().toString();

		} catch (Exception e) {
			return null;
		}
	}
}
