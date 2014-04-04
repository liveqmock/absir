/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-6 下午12:44:50
 */
package com.absir.appserv.system.menu.value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.absir.appserv.feature.menu.value.MaFactory;

/**
 * @author absir
 * 
 */
@Target(value = { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@MaFactory("crudMenuFactory")
public @interface MaCrudMenu {

}
