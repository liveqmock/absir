/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-25 上午10:34:19
 */
package com.absir.appserv.resource;

import java.io.File;

/**
 * @author absir
 * 
 */
public interface ResourceProcessor {

	/**
	 * @param scanner
	 * @return
	 */
	public boolean supports(ResourceScanner scanner);

	/**
	 * @param directoryFile
	 * @param scanner
	 * @return
	 */
	public boolean supportsDirectory(File directoryFile, ResourceScanner scanner);

	/**
	 * @param resourceFile
	 * @param scanner
	 */
	public void doProcessor(File resourceFile, ResourceScanner scanner);

}
