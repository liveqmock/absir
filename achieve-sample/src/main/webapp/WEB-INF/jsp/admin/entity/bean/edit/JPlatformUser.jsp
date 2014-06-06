<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p>
		<label>纪录编号:</label> <input name="id"
			type="text" size="30" readonly="readonly"
			value="${entity.id}">
	</p>
	<p> <label>平台名称:</label> <input name="platform" type="text" size="30" value="${entity.platform}" /> </p>
<p> <label>用户名:</label> <input name="username" type="text" size="30" value="${entity.username}" /> </p>
<c:set var="value" value="${entity.locked}" />
<p> <label>锁定用户:</label> <input name="locked" type="checkbox" value="true" ${value ? "checked" : ""}="true" /> </p>
<input type="hidden" name="locked" value="false" />
<p> <label>服务区:</label> <input name="serverId" type="text" size="30" value="${entity.serverId}" /> </p>
<p> <label>角色ID:</label> <input name="playerId" type="text" size="30" value="${entity.playerId}" /> </p>
<p> <label>服务区纪录:</label> 
 <c:set var="value" value="${entity.serverIds}" /><input name="serverIds" type="text" size="30" value="<%=WebJsplUtils.paramsValue(pageContext.getAttribute("value"))%>" /> </p>
<div class="divider"></div>
	</div>