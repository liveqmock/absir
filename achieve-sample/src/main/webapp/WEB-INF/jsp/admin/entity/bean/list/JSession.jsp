<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<thead>
	<tr>
		<th width="22"><input type="checkbox" group="ids"
			class="checkboxCtrl"></th>
		<th class="id ${orderFieldMap.id}" orderfield="id">验证主键</th>
<th class="passTime ${orderFieldMap.passTime}" orderfield="passTime">过期时间</th>
<th class="userId ${orderFieldMap.userId}" orderfield="userId">用户ID</th>
<th class="username ${orderFieldMap.username}" orderfield="username">用户名</th>
<th class="lastTime ${orderFieldMap.lastTime}" orderfield="lastTime">最后登录</th>
</tr>
</thead>
<tbody>
	<c:forEach items="${entities}" var="entity"><%pageContext.setAttribute("id", EntityStatics.getPrimary(pageContext.getAttribute("entity"), "id"));%><tr target="id" rel="${id}">
		<td><input name="ids"
		type="checkbox" value="${id}"></td><td class="id">${entity.id}</td>
<c:set var="value" value="${entity.passTime}" />
<td class="passTime">
<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"))%></td>
<td class="userId">${entity.userId}</td>
<td class="username">${entity.username}</td>
<c:set var="value" value="${entity.lastTime}" />
<td class="lastTime">
<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"))%></td>
</tr>
	</c:forEach></tbody>
