<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p>
		<label>纪录编号:</label> <input name="id"
			type="text" size="30" readonly="readonly"
			value="${entity.id}">
	</p>
	<p> <label>角色名称:</label> <input name="rolename" type="text" size="30" value="${entity.rolename}" class=" required" minlength="2" maxlength="12" /> </p>
<div class="divider"></div>
	</div>