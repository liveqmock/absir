<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<thead>
	<tr>
		<th width="22"><input type="checkbox" group="ids"
			class="checkboxCtrl"></th>
		<th class="id ${orderFieldMap.id}" orderfield="id">纪录编号</th>
<th class="name ${orderFieldMap.name}" orderfield="name">名称</th>
<th class="beginTime ${orderFieldMap.beginTime}" orderfield="beginTime">开始时间</th>
<th class="passTime ${orderFieldMap.passTime}" orderfield="passTime">过期时间</th>
</tr>
</thead>
<tbody>
	<c:forEach items="${entities}" var="entity"><%pageContext.setAttribute("id", EntityStatics.getPrimary(pageContext.getAttribute("entity"), "id"));%><tr target="id" rel="${id}">
		<td><input name="ids"
		type="checkbox" value="${id}"></td><td class="id">${entity.id}</td>
<td class="name">${entity.name}</td>
<c:set var="value" value="${entity.beginTime}" />
<td class="beginTime">
<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"))%></td>
<c:set var="value" value="${entity.passTime}" />
<td class="passTime">
<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"))%></td>
</tr>
	</c:forEach></tbody>
