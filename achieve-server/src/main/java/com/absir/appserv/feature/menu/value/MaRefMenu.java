/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月19日 下午5:48:46
 */
package com.absir.appserv.feature.menu.value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.absir.appserv.feature.menu.factory.RefMenuFactory;

/**
 * @author absir
 *
 */
@MaFactory(factory = RefMenuFactory.class)
@Target(value = { ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaRefMenu {

	/**
	 * @return
	 */
	String value();

}
