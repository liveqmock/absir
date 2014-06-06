<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p>
		<label>纪录编号:</label> <input name="id"
			type="text" size="30" readonly="readonly"
			value="${entity.id}">
	</p>
	
<div class="divider"></div>
	<c:set value="${entity}" var="entityContext"/><div class="tabs">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li id="activityRewards"><a href="javascript:;"><span>关联题库</span></a></li></ul>
			</div>
		</div>
		<div class="tabsContent">
			<c:set value="${entity.activityRewards}" var="entities"/><div id="activityRewards" class="pageContent"><input name="!subtables" type="hidden" value="activityRewards" />
			<table class="list nowrap itemDetail" width="100%"
				addButton="添加纪录">
				<thead>
					<tr>
						<th>内容</th>
						<th class="option" width="60">操作</th>
						</tr>
					<tr class="archetype">
						<td><input name="activityRewards[#index#]" type="text" style="width: 80%" value="" /></td>
<td><a href="javascript:void(0)" class="btnDel">删除</a></td></tr>
				</thead>
				<tbody>
				<c:forEach items="${entities}" var="entity" varStatus="i"><tr>
					<td><input name="activityRewards[${i.index}]" type="text" style="width: 80%" value="${entity}" /></td>
<td><a href="javascript:void(0)" class="btnDel">删除</a></td></tr>
				</c:forEach></tbody>
			</table>
			</div>
</div>
		<div class="tabsFooter">
			<div class="tabsFooterContent"></div>
		</div>
	</div>
	</div>