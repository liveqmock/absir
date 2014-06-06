<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<thead>
	<tr>
		<th width="22"><input type="checkbox" group="ids"
			class="checkboxCtrl"></th>
		<th class="id ${orderFieldMap.id}" orderfield="id">纪录编号</th>
<th class="serverId ${orderFieldMap.serverId}" orderfield="serverId">服务区</th>
<th class="userId ${orderFieldMap.userId}" orderfield="userId">玩家ID</th>
<th class="name ${orderFieldMap.name}" orderfield="name">角色名称</th>
<th class="sex ${orderFieldMap.sex}" orderfield="sex">角色性别</th>
<th class="sign ${orderFieldMap.sign}" orderfield="sign">用户签名</th>
<th class="level ${orderFieldMap.level}" orderfield="level">角色等级</th>
<th class="cardNumber ${orderFieldMap.cardNumber}" orderfield="cardNumber">卡牌数量</th>
<th class="friendNumber ${orderFieldMap.friendNumber}" orderfield="friendNumber">好友数量</th>
</tr>
</thead>
<tbody>
	<c:forEach items="${entities}" var="entity"><%pageContext.setAttribute("id", EntityStatics.getPrimary(pageContext.getAttribute("entity"), "id"));%><tr target="id" rel="${id}">
		<td><input name="ids"
		type="checkbox" value="${id}"></td><td class="id">${entity.id}</td>
<td class="serverId">${entity.serverId}</td>
<td class="userId">${entity.userId}</td>
<td class="name">${entity.name}</td>
<td class="sex">${entity.sex}</td>
<td class="sign">${entity.sign}</td>
<td class="level">${entity.level}</td>
<td class="cardNumber">${entity.cardNumber}</td>
<td class="friendNumber">${entity.friendNumber}</td>
</tr>
	</c:forEach></tbody>
