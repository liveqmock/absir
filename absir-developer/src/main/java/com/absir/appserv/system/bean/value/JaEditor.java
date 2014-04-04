/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-11-22 上午10:32:37
 */
package com.absir.appserv.system.bean.value;

import com.absir.orm.value.JaClasses;
import com.absir.orm.value.JaNames;

/**
 * @author absir
 * 
 */
public @interface JaEditor {

	/**
	 * @return
	 */
	String name();

	/**
	 * @return
	 */
	JaEdit[] edit() default {};

	/**
	 * @return
	 */
	JaCrud[] crud() default {};

	/**
	 * @return
	 */
	JaClasses[] classes() default {};

	/**
	 * @return
	 */
	JaNames[] names() default {};
}
