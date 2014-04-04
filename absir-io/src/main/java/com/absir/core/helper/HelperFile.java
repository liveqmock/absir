/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-3 下午5:18:30
 */
package com.absir.core.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import com.absir.core.kernel.KernelLang.CallbackBreak;

/**
 * @author absir
 * 
 */
public class HelperFile extends FileUtils {

	/**
	 * @param filename
	 * @return
	 */
	public static URL existUrl(String filename) {
		try {
			return new URL(filename);

		} catch (MalformedURLException e) {
		}

		return null;
	}

	/**
	 * @param filename
	 * @return
	 */
	public static boolean urlExists(String filename) {
		return existUrl(filename) != null;
	}

	/**
	 * @param filename
	 * @return
	 */
	public static File existFile(String filename) {
		File fl = new File(filename);
		return (fl.exists() && fl.isFile()) ? fl : null;
	}

	/**
	 * @param filename
	 * @return
	 */
	public static boolean fileExists(String filename) {
		return existFile(filename) != null;
	}

	/**
	 * @param filename
	 * @return
	 */
	public static File existDirectory(String filename) {
		File fl = new File(filename);
		return (fl.exists() && fl.isDirectory()) ? fl : null;
	}

	/**
	 * @param filename
	 * @return
	 */
	public static boolean directoryExists(String filename) {
		return existDirectory(filename) != null;
	}

	/**
	 * @param file
	 * @param callback
	 * @throws IOException
	 */
	public static void doWithReadLine(File file, CallbackBreak<String> callback) throws IOException {
		doWithReadLine(file, Charset.defaultCharset(), callback);
	}

	/**
	 * @param file
	 * @param encoding
	 * @param callback
	 * @throws IOException
	 */
	public static void doWithReadLine(File file, String encoding, CallbackBreak<String> callback) throws IOException {
		doWithReadLine(file, Charsets.toCharset(encoding), callback);
	}

	/**
	 * @param file
	 * @param encoding
	 * @param callback
	 * @throws IOException
	 */
	public static void doWithReadLine(File file, Charset encoding, CallbackBreak<String> callback) throws IOException {
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			HelperIO.doWithReadLine(input, encoding, callback);

		} finally {
			if (input != null) {
				input.close();
			}
		}
	}

	/**
	 * @param file
	 * @param input
	 * @throws IOException
	 */
	public static void write(File file, InputStream input) throws IOException {
		HelperIO.copy(input, openOutputStream(file));
	}

	/**
	 * @param file
	 * @param lastModified
	 * @return
	 * @throws IOException
	 */
	public static FileOutputStream openOutputStream(File file, Long lastModified) throws IOException {
		if (!file.exists() || lastModified == null || file.lastModified() < lastModified) {
			synchronized (file) {
				if (!file.exists()) {
					lastModified = null;
				}

				FileOutputStream output = null;
				try {
					output = HelperFile.openOutputStream(file);
					if (output.getChannel().tryLock() != null) {
						return output;
					}

				} catch (Exception e) {
					if (output != null) {
						output.close();
					}

					throw (IOException) e;
				}
			}
		}

		return null;
	}
}
