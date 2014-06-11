<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<thead>
	<tr>
		<th width="22"><input type="checkbox" group="ids"
			class="checkboxCtrl"></th>
		<th class="id ${orderFieldMap.id}" orderfield="id">纪录编号</th>
<th class="rolename ${orderFieldMap.rolename}" orderfield="rolename">角色名称</th>
</tr>
</thead>
<tbody>
	<c:forEach items="${entities}" var="entity"><%pageContext.setAttribute("id", EntityStatics.getPrimary(pageContext.getAttribute("entity"), "id"));%><tr target="id" rel="${id}">
		<td><input name="ids"<c:set value="${entity}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.rolename}" var="value"/>

		type="checkbox" value="{${suggest}:'${id}',
		${suggest}$suggest:'${value}'}">
		</td>
		<td class="id">${entity.id}</td>
<td class="rolename">${entity.rolename}</td>
</tr>
	</c:forEach></tbody>