/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-4-3 下午5:18:30
 */
package com.absir.core.helper;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import com.absir.core.kernel.KernelString;

/**
 * @author absir
 * 
 */
public class HelperFileName extends FilenameUtils {

	/**
	 * The Unix separator character.
	 */
	public static final char UNIX_SEPARATOR = '/';

	/**
	 * The Windows separator character.
	 */
	public static final char WINDOWS_SEPARATOR = '\\';

	/**
	 * The system separator character.
	 */
	public static final char SYSTEM_SEPARATOR = File.separatorChar;

	/**
	 * The separator character that is the opposite of the system separator.
	 */
	public static final char OTHER_SEPARATOR = SYSTEM_SEPARATOR == WINDOWS_SEPARATOR ? UNIX_SEPARATOR : WINDOWS_SEPARATOR;

	/**
	 * @param filename
	 * @return
	 */
	public static String unixFilename(String filename) {
		return filename.replace(WINDOWS_SEPARATOR, UNIX_SEPARATOR);
	}

	/**
	 * @param basepath
	 * @param pathNames
	 * @param filename
	 * @return
	 */
	public static String iterateFilename(String basepath, String pathNames, String filename) {
		pathNames = HelperFileName.getFullPathNoEndSeparator(pathNames);
		return HelperFileName.iterateFilename(basepath + SYSTEM_SEPARATOR + pathNames, pathNames.split(Character.toString(UNIX_SEPARATOR)), filename);
	}

	/**
	 * @param basePath
	 * @param pathNames
	 * @param filename
	 * @return
	 */
	public static String iterateFilename(String basePath, String[] pathNames, String filename) {
		int length = basePath.length();
		int last = pathNames.length - 1;
		for (int i = last; i > 0; i--) {
			String pathname = pathNames[i];
			if (pathname.indexOf(EXTENSION_SEPARATOR) >= 0 && i < last) {
				break;
			}

			length -= pathname.length() + 1;
			pathname = basePath.substring(0, length) + SYSTEM_SEPARATOR + filename;
			if (HelperFile.fileExists(pathname)) {
				return pathname;
			}
		}

		return null;
	}

	/**
	 * @param filename
	 * @param subExtension
	 * @return
	 */
	public static String addFilenameSubExtension(String filename, String subExtension) {
		String extension = getExtension(filename);
		if (KernelString.isEmpty(extension)) {
			return filename + EXTENSION_SEPARATOR + subExtension;

		} else {
			return KernelString.rightSubString(filename, extension.length()) + subExtension + EXTENSION_SEPARATOR + extension;
		}
	}
}
