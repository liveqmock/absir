<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p>
		<label>纪录编号:</label> <input name="id"
			type="text" size="30" readonly="readonly"
			value="${entity.id}">
	</p>
	<p> <label>活动名称:</label> <input name="activityBase.name" type="text" size="30" value="${entity.activityBase.name}" /> </p>
<p> <label>市场奖励:</label> <input name="activityBase.marketReward" type="text" size="30" value="${entity.activityBase.marketReward}" /> </p>
<c:set value="${entity.repetition}" var="value"/>
<p> <label>重复类型:</label> <select name="repetition" value="<%=WebJsplUtils.enumValue(pageContext.getAttribute("value"))%>" class=" combox"><option value="">请选择</option><option value="TEST">测试</option> <option value="WEEKLY">每周</option> <option value="DAYLY">每天</option> <option value="MOTHLY">每月</option> <option value="YEARLY">每年</option></select> </p>
<c:set var="value" value="${entity.beginTime}" />
<p> <label>开始时间:</label> 
<input name="beginTime" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="30" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), 0)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> </p>
<c:set var="value" value="${entity.passTime}" />
<p> <label>过期时间:</label> 
<input name="passTime" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="30" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), 0)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> </p>
<p> <label>活动背景:</label> <input name="backgroud" type="text" size="30" value="${entity.backgroud}" /> </p>
<p> <input type="file" name="backgroud_file" /> </p>
<c:set value="" var="values"/>
<c:set value="" var="ids"/>
<c:forEach items='${entity.questions}' var="value" varStatus="status">
<c:if test="${status.index > 0}">
<c:set value="${ids}," var="ids"/>
<c:set value="${values}," var="values"/>
</c:if>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}.${value.title}" var="value"/>
<c:set value="${ids}${id}" var="ids"/>
<c:set value="${values}${value}" var="values"/>
</c:forEach>
<%session.setAttribute("SUGGEST@JQuestion", true);%>
<p> <label>关联题库:</label> <input name="questions$suggest" type="text" size="30" value="${values}" /><a class="btnLook" href="/achieve-dtyx/admin/entity/lookup/JQuestion?suggest=questions" lookupgroup="">查找带回</a> <input type="hidden" name="questions" value="${ids}" /></p>
<c:set var="value" value="${entity.updateTime}" />
<p> <label>修改时间:</label> 
<input name="updateTime" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="30" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), 0)%>" readonly="readonly" /> </p>
<div class="divider"></div>
	<c:set value="${entity}" var="entityContext"/><div class="tabs">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li id="activityBase.activityRewards"><a href="javascript:;"><span>奖励梯度</span></a></li></ul>
			</div>
		</div>
		<div class="tabsContent">
			<c:set value="${entity.activityBase.activityRewards}" var="entities"/><div id="activityBase.activityRewards" class="pageContent"><input name="!subtables" type="hidden" value="activityBase.activityRewards" />
			<table class="list nowrap itemDetail" width="100%"
				addButton="添加纪录">
				<thead>
					<tr>
						<th>金钱奖励</th>
						<th>宝石奖励</th>
						<th>卡牌奖励</th>
						<th>道具奖励</th>
						<th>排名</th>
						<th class="option" width="60">操作</th>
						</tr>
					<tr class="archetype">
						<td><input name="activityBase.activityRewards[#index#].money" type="text" style="width: 80%" value="" /></td>
<td><input name="activityBase.activityRewards[#index#].diamond" type="text" style="width: 80%" value="" /></td>
<td><input name="activityBase.activityRewards[#index#].cardRewards" type="text" style="width: 80%" value="" /></td>
<td><input name="activityBase.activityRewards[#index#].propRewards" type="text" style="width: 80%" value="" /></td>
<td><input name="activityBase.activityRewards[#index#].ranking" type="text" style="width: 80%" value="" /></td>
<td><a href="javascript:void(0)" class="btnDel">删除</a></td></tr>
				</thead>
				<tbody>
				<c:forEach items="${entities}" var="entity" varStatus="i"><tr>
					<td><input name="activityBase.activityRewards[${i.index}].money" type="text" style="width: 80%" value="${entity.money}" /></td>
<td><input name="activityBase.activityRewards[${i.index}].diamond" type="text" style="width: 80%" value="${entity.diamond}" /></td>
<td><input name="activityBase.activityRewards[${i.index}].cardRewards" type="text" style="width: 80%" value="${entity.cardRewards}" /></td>
<td><input name="activityBase.activityRewards[${i.index}].propRewards" type="text" style="width: 80%" value="${entity.propRewards}" /></td>
<td><input name="activityBase.activityRewards[${i.index}].ranking" type="text" style="width: 80%" value="${entity.ranking}" /></td>
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