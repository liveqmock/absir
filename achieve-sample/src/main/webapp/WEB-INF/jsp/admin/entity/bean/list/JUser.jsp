<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<thead>
	<tr>
		<th width="22"><input type="checkbox" group="ids"
			class="checkboxCtrl"></th>
		<th class="id ${orderFieldMap.id}" orderfield="id">纪录编号</th>
<th class="enable ${orderFieldMap.enable}" orderfield="enable">激活用户</th>
<th class="expired ${orderFieldMap.expired}" orderfield="expired">过期用户</th>
<th class="locked ${orderFieldMap.locked}" orderfield="locked">锁定用户</th>
<th class="lastLogin ${orderFieldMap.lastLogin}" orderfield="lastLogin">最后登录</th>
<th class="errorLogin ${orderFieldMap.errorLogin}" orderfield="errorLogin">错误登录</th>
<th class="lastErrorLogin ${orderFieldMap.lastErrorLogin}" orderfield="lastErrorLogin">最后错误登录</th>
<th class="username ${orderFieldMap.username}" orderfield="username">用户名</th>
<th class="userType ${orderFieldMap.userType}" orderfield="userType">用户类型</th>
<th class="userRoles">用户角色</th>
</tr>
</thead>
<tbody>
	<c:forEach items="${entities}" var="entity"><%pageContext.setAttribute("id", EntityStatics.getPrimary(pageContext.getAttribute("entity"), "id"));%><tr target="id" rel="${id}">
		<td><input name="ids"
		type="checkbox" value="${id}"></td><td class="id">${entity.id}</td>
<td class="enable">${entity.enable}</td>
<td class="expired">${entity.expired}</td>
<td class="locked">${entity.locked}</td>
<c:set var="value" value="${entity.lastLogin}" />
<td class="lastLogin">
<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"))%></td>
<td class="errorLogin">${entity.errorLogin}</td>
<c:set var="value" value="${entity.lastErrorLogin}" />
<td class="lastErrorLogin">
<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"))%></td>
<td class="username">${entity.username}</td>
<c:set var="value">
 ${entity.userType}
</c:set>
<td class="userType">
<%=WebJsplUtils.value(((java.util.Map)EntityStatics.getSharedObject("com/absir/appserv/system/service/statics/EntityStatics/JUser-userType_SHARED", WebJsplUtils.getInput(request))).get(pageContext.getAttribute("value")))%></td>
<c:set value="" var="values"/>
<c:set value="" var="ids"/>
<c:forEach items='${entity.userRoles}' var="value" varStatus="status">
<c:if test="${status.index > 0}">
<c:set value="${ids}," var="ids"/>
<c:set value="${values}," var="values"/>
</c:if>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}.${value.rolename}" var="value"/>
<c:set value="${ids}${id}" var="ids"/>
<c:set value="${values}${value}" var="values"/>
</c:forEach>
<td class="userRoles">${values}</td>
</tr>
	</c:forEach></tbody>
