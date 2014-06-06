<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<thead>
	<tr>
		<th width="22"><input type="checkbox" group="ids"
			class="checkboxCtrl"></th>
		<th class="id ${orderFieldMap.id}" orderfield="id">纪录编号</th>
<th class="category ${orderFieldMap.category}" orderfield="category">题目分类</th>
<th class="title ${orderFieldMap.title}" orderfield="title">标题</th>
<th class="difficult ${orderFieldMap.difficult}" orderfield="difficult">正确率</th>
<th class="minLevel ${orderFieldMap.minLevel}" orderfield="minLevel">最小等级</th>
<th class="maxLevel ${orderFieldMap.maxLevel}" orderfield="maxLevel">最大等级</th>
<th class="correct ${orderFieldMap.correct}" orderfield="correct">答案</th>
<th class="updateTime ${orderFieldMap.updateTime}" orderfield="updateTime">修改时间</th>
</tr>
</thead>
<tbody>
	<c:forEach items="${entities}" var="entity"><%pageContext.setAttribute("id", EntityStatics.getPrimary(pageContext.getAttribute("entity"), "id"));%><tr target="id" rel="${id}">
		<td><input name="ids"
		type="checkbox" value="${id}"></td><td class="id">${entity.id}</td>
<c:set value="${entity.category}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}.${value.name}" var="value"/>

<td class="category">${value}</td>
<td class="title">${entity.title}</td>
<td class="difficult">${entity.difficult}</td>
<td class="minLevel">${entity.minLevel}</td>
<td class="maxLevel">${entity.maxLevel}</td>
<td class="correct">${entity.correct}</td>
<c:set var="value" value="${entity.updateTime}" />
<td class="updateTime">
<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"))%></td>
</tr>
	</c:forEach></tbody>
