/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月28日 上午10:24:08
 */
package com.absir.appserv.system.service.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.absir.appserv.system.service.EmailService;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Inject;
import com.absir.context.core.ContextUtils;

/**
 * @author absir
 *
 */
@Inject
public class EmailServiceUtils {

	/** ME */
	public static final EmailService ME = BeanFactoryUtils.get(EmailService.class);

	/** HTML_CONTENT_TYPE */
	public static final String HTML_CONTENT_TYPE = "text/html;charset=" + ContextUtils.getCharset().displayName();

	/**
	 * @param mimeMessage
	 * @param subject
	 * @param content
	 * @throws MessagingException
	 */
	public static void sendMimeMessageText(MimeMessage mimeMessage, String subject, String content) throws MessagingException {
		String charset = ContextUtils.getCharset().displayName();
		mimeMessage.setSubject(subject, charset);
		mimeMessage.setText(content, charset);
		ME.sendMimeMessage(mimeMessage);
	}

	/**
	 * @param mimeMessage
	 * @param subject
	 * @param content
	 * @throws MessagingException
	 */
	public static void sendMimeMessageHtml(MimeMessage mimeMessage, String subject, String content) throws MessagingException {
		mimeMessage.setSubject(subject, ContextUtils.getCharset().displayName());
		mimeMessage.setContent(content, HTML_CONTENT_TYPE);
		ME.sendMimeMessage(mimeMessage);
	}

	/**
	 * @param mimeMessage
	 * @param subject
	 * @param content
	 * @param files
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public static void sendMimeMessageMultipart(MimeMessage mimeMessage, String subject, String content, File[] files) throws MessagingException, UnsupportedEncodingException {
		mimeMessage.setSubject(subject, ContextUtils.getCharset().displayName());
		Multipart multipart = new MimeMultipart();
		BodyPart contentPart = new MimeBodyPart();
		contentPart.setContent(content, HTML_CONTENT_TYPE);
		multipart.addBodyPart(contentPart);
		for (File file : files) {
			contentPart = new MimeBodyPart();
			FileDataSource fileDataSource = new FileDataSource(file.getPath());
			contentPart.setDataHandler(new DataHandler(fileDataSource));
			contentPart.setFileName(MimeUtility.encodeText(fileDataSource.getName()));
			multipart.addBodyPart(contentPart);
		}

		mimeMessage.setContent(multipart, HTML_CONTENT_TYPE);
		ME.sendMimeMessage(mimeMessage);
	}

	/**
	 * @param to
	 * @param subject
	 * @param content
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void sendMimeMessageText(String to, String subject, String content) throws AddressException, MessagingException {
		sendMimeMessageText(ME.createMimeMessage(to), subject, content);
	}

	/**
	 * @param tos
	 * @param subject
	 * @param content
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void sendsMimeMessageText(String[] tos, String subject, String content) throws AddressException, MessagingException {
		sendMimeMessageText(ME.createMimeMessage(tos), subject, content);
	}

	/**
	 * @param to
	 * @param subject
	 * @param content
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void sendMimeMessageHtml(String to, String subject, String content) throws AddressException, MessagingException {
		sendMimeMessageHtml(ME.createMimeMessage(to), subject, content);
	}

	/**
	 * @param tos
	 * @param subject
	 * @param content
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void sendsMimeMessageHtml(String[] tos, String subject, String content) throws AddressException, MessagingException {
		sendMimeMessageHtml(ME.createMimeMessage(tos), subject, content);
	}

	/**
	 * @param to
	 * @param subject
	 * @param content
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public static void sendMimeMessageMultipart(String to, String subject, String content, File[] files) throws AddressException, MessagingException, UnsupportedEncodingException {
		if (files == null) {
			sendMimeMessageHtml(ME.createMimeMessage(to), subject, content);

		} else {
			sendMimeMessageMultipart(ME.createMimeMessage(to), subject, content, files);
		}
	}

	/**
	 * @param tos
	 * @param subject
	 * @param content
	 * @param files
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public static void sendsMimeMessageMultipart(String[] tos, String subject, String content, File[] files) throws AddressException, MessagingException, UnsupportedEncodingException {
		if (files == null) {
			sendMimeMessageHtml(ME.createMimeMessage(tos), subject, content);

		} else {
			sendMimeMessageMultipart(ME.createMimeMessage(tos), subject, content, files);
		}
	}
}
