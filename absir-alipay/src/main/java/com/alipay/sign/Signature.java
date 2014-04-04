/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-12 下午2:49:10
 */
package com.alipay.sign;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author absir
 * 
 */
public enum Signature {

	MD5 {

		/**
		 * 签名字符串
		 * 
		 * @param text
		 *            需要签名的字符串
		 * @param key
		 *            密钥
		 * @param input_charset
		 *            编码格式
		 * @return 签名结果
		 */
		public String sign(String text, String key, String input_charset) {
			text = text + key;
			return DigestUtils.md5Hex(getContentBytes(text, input_charset));
		}

		/**
		 * 签名字符串
		 * 
		 * @param text
		 *            需要签名的字符串
		 * @param sign
		 *            签名结果
		 * @param key
		 *            密钥
		 * @param input_charset
		 *            编码格式
		 * @return 签名结果
		 */
		public boolean verify(String text, String sign, String key, String input_charset) {
			text = text + key;
			String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
			if (mysign.equals(sign)) {
				return true;

			} else {
				return false;
			}
		}

		/**
		 * @param content
		 * @param charset
		 * @return
		 * @throws SignatureException
		 * @throws UnsupportedEncodingException
		 */
		private byte[] getContentBytes(String content, String charset) {
			if (charset == null || "".equals(charset)) {
				return content.getBytes();
			}

			try {
				return content.getBytes(charset);

			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
			}
		}

	},

	RSA {

		public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

		/**
		 * RSA签名
		 * 
		 * @param content
		 *            待签名数据
		 * @param privateKey
		 *            商户私钥
		 * @param input_charset
		 *            编码格式
		 * @return 签名值
		 */
		public String sign(String content, String privateKey, String input_charset) {
			try {
				PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
				KeyFactory keyf = KeyFactory.getInstance("RSA");
				PrivateKey priKey = keyf.generatePrivate(priPKCS8);
				java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

				signature.initSign(priKey);
				signature.update(content.getBytes(input_charset));

				byte[] signed = signature.sign();

				return Base64.encode(signed);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * RSA验签名检查
		 * 
		 * @param content
		 *            待签名数据
		 * @param sign
		 *            签名值
		 * @param ali_public_key
		 *            支付宝公钥
		 * @param input_charset
		 *            编码格式
		 * @return 布尔值
		 */
		public boolean verify(String content, String sign, String ali_public_key, String input_charset) {
			try {
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				byte[] encodedKey = Base64.decode(ali_public_key);
				PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

				java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

				signature.initVerify(pubKey);
				signature.update(content.getBytes(input_charset));

				boolean bverify = signature.verify(Base64.decode(sign));
				return bverify;

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}

		/**
		 * 解密
		 * 
		 * @param content
		 *            密文
		 * @param private_key
		 *            商户私钥
		 * @param input_charset
		 *            编码格式
		 * @return 解密后的字符串
		 */
		@SuppressWarnings("unused")
		public String decrypt(String content, String private_key, String input_charset) throws Exception {
			PrivateKey prikey = getPrivateKey(private_key);

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, prikey);

			InputStream ins = new ByteArrayInputStream(Base64.decode(content));
			ByteArrayOutputStream writer = new ByteArrayOutputStream();
			// rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
			byte[] buf = new byte[128];
			int bufl;

			while ((bufl = ins.read(buf)) != -1) {
				byte[] block = null;

				if (buf.length == bufl) {
					block = buf;

				} else {
					block = new byte[bufl];
					for (int i = 0; i < bufl; i++) {
						block[i] = buf[i];
					}
				}

				writer.write(cipher.doFinal(block));
			}

			return new String(writer.toByteArray(), input_charset);
		}

		/**
		 * 得到私钥
		 * 
		 * @param key
		 *            密钥字符串（经过base64编码）
		 * @throws Exception
		 */
		public PrivateKey getPrivateKey(String key) throws Exception {

			byte[] keyBytes;

			keyBytes = Base64.decode(key);

			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

			return privateKey;
		}
	}

	;

	/**
	 * @param text
	 * @param key
	 * @param input_charset
	 * @return
	 */
	public abstract String sign(String text, String key, String input_charset);

	/**
	 * @param text
	 * @param sign
	 * @param key
	 * @param input_charset
	 * @return
	 */
	public abstract boolean verify(String text, String sign, String key, String input_charset);
}
