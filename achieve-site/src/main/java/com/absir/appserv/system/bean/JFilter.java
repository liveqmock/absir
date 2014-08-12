/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月16日 上午11:52:33
 */
package com.absir.appserv.system.bean;

import javax.persistence.Entity;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbVerifier;

/**
 * @author absir
 *
 */
@MaEntity(parent = { @MaMenu("系统配置") }, name = "过滤字")
@Entity
public class JFilter extends JbVerifier {

}
