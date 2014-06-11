<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<thead>
	<tr>
		<th width="22"><input type="checkbox" group="ids"
			class="checkboxCtrl"></th>
		<th class="id ${orderFieldMap.id}" orderfield="id">纪录编号</th>
<th class="platform ${orderFieldMap.platform}" orderfield="platform">平台名称</th>
<th class="username ${orderFieldMap.username}" orderfield="username">用户名</th>
<th class="locked ${orderFieldMap.locked}" orderfield="locked">锁定用户</th>
<th class="serverId ${orderFieldMap.serverId}" orderfield="serverId">服务区</th>
<th class="playerId ${orderFieldMap.playerId}" orderfield="playerId">角色ID</th>
</tr>
</thead>
<tbody>
	<c:forEach items="${entities}" var="entity"><%pageContext.setAttribute("id", EntityStatics.getPrimary(pageContext.getAttribute("entity"), "id"));%><tr target="id" rel="${id}">
		<td><input name="ids"
		type="checkbox" value="${id}"></td><td class="id">${entity.id}</td>
<td class="platform">${entity.platform}</td>
<td class="username">${entity.username}</td>
<td class="locked">${entity.locked}</td>
<td class="serverId">${entity.serverId}</td>
<td class="playerId">${entity.playerId}</td>
</tr>
	</c:forEach></tbody>
