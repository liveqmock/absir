<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<thead>
	<tr>
		<th width="22"><input type="checkbox" group="ids"
			class="checkboxCtrl"></th>
		<th class="id ${orderFieldMap.id}" orderfield="id">订单号</th>
<th class="createTime ${orderFieldMap.createTime}" orderfield="createTime">创建时间</th>
<th class="uid ${orderFieldMap.uid}" orderfield="uid">用户名</th>
<th class="name ${orderFieldMap.name}" orderfield="name">商品类名</th>
<th class="nameData ${orderFieldMap.nameData}" orderfield="nameData">商品参数</th>
<th class="amount ${orderFieldMap.amount}" orderfield="amount">支付总金额</th>
<th class="status ${orderFieldMap.status}" orderfield="status">交易状态</th>
<th class="platform ${orderFieldMap.platform}" orderfield="platform">平台名称</th>
</tr>
</thead>
<tbody>
	<c:forEach items="${entities}" var="entity"><%pageContext.setAttribute("id", EntityStatics.getPrimary(pageContext.getAttribute("entity"), "id"));%><tr target="id" rel="${id}">
		<td><input name="ids"
		type="checkbox" value="${id}"></td><td class="id">${entity.id}</td>
<c:set var="value" value="${entity.createTime}" />
<td class="createTime">
<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"))%></td>
<td class="uid">${entity.uid}</td>
<td class="name">${entity.name}</td>
<td class="nameData">${entity.nameData}</td>
<td class="amount">${entity.amount}</td>
<c:set var="value">
 ${entity.status}
</c:set>
<td class="status">
<%=WebJsplUtils.value(((java.util.Map)EntityStatics.getSharedObject("com/absir/appserv/system/service/statics/EntityStatics/JPayTrade-status_SHARED", WebJsplUtils.getInput(request))).get(pageContext.getAttribute("value")))%></td>
<td class="platform">${entity.platform}</td>
</tr>
	</c:forEach></tbody>
