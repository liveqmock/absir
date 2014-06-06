package com.absir.appserv.system.bean;

import javax.persistence.Entity;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbUserRole;

@MaEntity(parent = { @MaMenu("用户管理") }, name = "角色")
@Entity
public class JUserRole extends JbUserRole {

}
