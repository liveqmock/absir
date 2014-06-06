<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p>
		<label>纪录编号:</label> <input name="id"
			type="text" size="30" readonly="readonly"
			value="${entity.id}">
	</p>
	<p> <label>服务区:</label> <input name="serverId" type="text" size="30" value="${entity.serverId}" /> </p>
<p> <label>玩家ID:</label> <input name="userId" type="text" size="30" value="${entity.userId}" /> </p>
<p> <label>角色名称:</label> <input name="name" type="text" size="30" value="${entity.name}" /> </p>
<c:set var="value" value="${entity.sex}" />
<p> <label>角色性别:</label> <input name="sex" type="checkbox" value="true" ${value ? "checked" : ""}="true" /> </p>
<input type="hidden" name="sex" value="false" />
<p> <label>用户签名:</label> <input name="sign" type="text" size="30" value="${entity.sign}" /> </p>
<p> <label>金钱:</label> <input name="money" type="text" size="30" value="${entity.money}" /> </p>
<p> <label>宝石数量:</label> <input name="diamond" type="text" size="30" value="${entity.diamond}" /> </p>
<p> <label>角色等级:</label> <input name="level" type="text" size="30" value="${entity.level}" /> </p>
<p> <label>经验:</label> <input name="exp" type="text" size="30" value="${entity.exp}" /> </p>
<p> <label>升级经验:</label> <input name="maxExp" type="text" size="30" value="${entity.maxExp}" /> </p>
<p> <label>行动力:</label> <input name="ep" type="text" size="30" value="${entity.ep}" /> </p>
<p> <label>最大行动力:</label> <input name="maxEp" type="text" size="30" value="${entity.maxEp}" /> </p>
<p> <label>队伍领导力:</label> <input name="cost" type="text" size="30" value="${entity.cost}" /> </p>
<p> <label>最大领导力:</label> <input name="maxCost" type="text" size="30" value="${entity.maxCost}" /> </p>
<p> <label>卡牌数量:</label> <input name="cardNumber" type="text" size="30" value="${entity.cardNumber}" /> </p>
<p> <label>最大卡牌数量:</label> <input name="maxCardNumber" type="text" size="30" value="${entity.maxCardNumber}" /> </p>
<p> <label>好友数量:</label> <input name="friendNumber" type="text" size="30" value="${entity.friendNumber}" /> </p>
<p> <label>最大好友数量:</label> <input name="maxFriendNumber" type="text" size="30" value="${entity.maxFriendNumber}" /> </p>
<p> <label>友情点数:</label> <input name="friendshipNumber" type="text" size="30" value="${entity.friendshipNumber}" /> </p>
<%pageContext.setAttribute("selects", EntityStatics.suggest("JCard", WebJsplUtils.getInput(request)));%><c:set var="options"><option value="">请选择</option><c:forEach items="${selects}" var="select" varStatus="status"><c:set value="${select}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}" var="value"/>
<option value="${id}">${value}</option></c:forEach></c:set><c:set value="${entity.card}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}" var="value"/>
<p> <label>队长卡牌:</label> <select name="card" value="${id}" class=" combox">${options}</select> </p>
<%pageContext.setAttribute("selects", EntityStatics.suggest("JCard", WebJsplUtils.getInput(request)));%><c:set var="options"><option value="">请选择</option><c:forEach items="${selects}" var="select" varStatus="status"><c:set value="${select}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}" var="value"/>
<option value="${id}">${value}</option></c:forEach></c:set><c:set value="${entity.card0}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}" var="value"/>
<p> <label>卡牌0号位:</label> <select name="card0" value="${id}" class=" combox">${options}</select> </p>
<%pageContext.setAttribute("selects", EntityStatics.suggest("JCard", WebJsplUtils.getInput(request)));%><c:set var="options"><option value="">请选择</option><c:forEach items="${selects}" var="select" varStatus="status"><c:set value="${select}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}" var="value"/>
<option value="${id}">${value}</option></c:forEach></c:set><c:set value="${entity.card1}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}" var="value"/>
<p> <label>卡牌1号位:</label> <select name="card1" value="${id}" class=" combox">${options}</select> </p>
<%pageContext.setAttribute("selects", EntityStatics.suggest("JCard", WebJsplUtils.getInput(request)));%><c:set var="options"><option value="">请选择</option><c:forEach items="${selects}" var="select" varStatus="status"><c:set value="${select}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}" var="value"/>
<option value="${id}">${value}</option></c:forEach></c:set><c:set value="${entity.card2}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}" var="value"/>
<p> <label>卡牌2号位:</label> <select name="card2" value="${id}" class=" combox">${options}</select> </p>
<%pageContext.setAttribute("selects", EntityStatics.suggest("JCard", WebJsplUtils.getInput(request)));%><c:set var="options"><option value="">请选择</option><c:forEach items="${selects}" var="select" varStatus="status"><c:set value="${select}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}" var="value"/>
<option value="${id}">${value}</option></c:forEach></c:set><c:set value="${entity.card3}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}" var="value"/>
<p> <label>卡牌3号位:</label> <select name="card3" value="${id}" class=" combox">${options}</select> </p>
<%pageContext.setAttribute("selects", EntityStatics.suggest("JCard", WebJsplUtils.getInput(request)));%><c:set var="options"><option value="">请选择</option><c:forEach items="${selects}" var="select" varStatus="status"><c:set value="${select}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}" var="value"/>
<option value="${id}">${value}</option></c:forEach></c:set><c:set value="${entity.card4}" var="value"/>
<c:set value="${value.id}" var="id"/>
<c:set value="${value.id}" var="value"/>
<p> <label>卡牌4号位:</label> <select name="card4" value="${id}" class=" combox">${options}</select> </p>
<p> <label>冠军次数:</label> <input name="championNumber" type="text" size="30" value="${entity.championNumber}" /> </p>
<p> <label>亚军次数:</label> <input name="secondNumber" type="text" size="30" value="${entity.secondNumber}" /> </p>
<p> <label>季军次数:</label> <input name="thirdNumber" type="text" size="30" value="${entity.thirdNumber}" /> </p>
<div class="divider"></div>
	<c:set value="${entity}" var="entityContext"/><div class="tabs">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li id="answers"><a href="javascript:;"><span>答题情况</span></a></li></ul>
			</div>
		</div>
		<div class="tabsContent">
			<c:set value="${entity.answers}" var="entities"/><div id="answers" class="pageContent"><input name="!subtables" type="hidden" value="answers" />
			<table class="list nowrap itemDetail" width="100%"
				addButton="">
				<thead>
					<tr>
						<th>键值</th>
						<th>答题总数</th>
						<th>答题正确数</th>
						<th>答题总时间</th>
						<th class="option" width="60">操作</th>
						</tr>
					<tr class="archetype">
						<td><input class="itemKey answers" type="text" readonly="readonly" value="" /></td>
<td><input name="answers[''].answerCount" type="text" style="width: 80%" value="" /></td>
<td><input name="answers[''].answerCorrect" type="text" style="width: 80%" value="" /></td>
<td><input name="answers[''].answerTime" type="text" style="width: 80%" value="" /></td>
<td><a href="javascript:void(0)" class="btnDel">删除</a></td></tr>
				</thead>
				<tbody>
				<c:forEach items="${entities}" var="entity" varStatus="i"><tr>
					<td><input class="itemKey answers" type="text" readonly="readonly" value="${entity.key}" /></td>
<td><input name="answers['${entity.key}'].answerCount" type="text" style="width: 80%" value="${entity.value.answerCount}" /></td>
<td><input name="answers['${entity.key}'].answerCorrect" type="text" style="width: 80%" value="${entity.value.answerCorrect}" /></td>
<td><input name="answers['${entity.key}'].answerTime" type="text" style="width: 80%" value="${entity.value.answerTime}" /></td>
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