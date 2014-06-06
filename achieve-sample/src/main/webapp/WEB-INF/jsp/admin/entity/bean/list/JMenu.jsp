<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<thead>
	<tr>
		<th width="22"><input type="checkbox" group="ids"
			class="checkboxCtrl"></th>
		<th class="id ${orderFieldMap.id}" orderfield="id">纪录编号</th>
<th class="parent ${orderFieldMap.parent}" orderfield="parent">父级菜单</th>
<th class="name ${orderFieldMap.name}" orderfield="name">菜单名称</th>
<th class="type ${orderFieldMap.type}" orderfield="type">菜单类型</th>
<th class="ordinal ${orderFieldMap.ordinal}" orderfield="ordinal">菜单排序</th>
<th class="url ${orderFieldMap.url}" orderfield="url">菜单地址</th>
</tr>
</thead>
<tbody>
	<c:forEach items="${entities}" var="entity"><%pageContext.setAttribute("id", EntityStatics.getPrimary(pageContext.getAttribute("entity"), "id"));%><tr target="id" rel="${id}">
		<td><input name="ids"
		type="checkbox" value="${id}"></td><td class="id">${entity.id}</td>
<c:set value="${entity.parent}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}.${value.name}" var="value"/>

<td class="parent">${value}</td>
<td class="name">${entity.name}</td>
<td class="type">${entity.type}</td>
<td class="ordinal">${entity.ordinal}</td>
<td class="url">${entity.url}</td>
</tr>
	</c:forEach></tbody>
