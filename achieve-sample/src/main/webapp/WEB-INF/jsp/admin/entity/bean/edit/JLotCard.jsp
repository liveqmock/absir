<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p>
		<label>纪录编号:</label> <input name="id"
			type="text" size="30" readonly="readonly"
			value="${entity.id}">
	</p>
	<p> <label>名称:</label> <input name="name" type="text" size="30" value="${entity.name}" /> </p><div class="divider"></div>
<div class=" p"> 
 <label>抽奖卡牌:</label> 
 <c:set var="value" value="${entity.cardIds}" />
 <textarea name="cardIds" type="text" cols="87" rows="7"><%=WebJsplUtils.paramsValue(pageContext.getAttribute("value"))%></textarea> 
</div>
<c:set var="value" value="${entity.beginTime}" />
<p> <label>开始时间:</label> 
<input name="beginTime" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="30" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), 0)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> </p>
<c:set var="value" value="${entity.passTime}" />
<p> <label>过期时间:</label> 
<input name="passTime" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="30" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), 0)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> </p>
<div class="divider"></div>
	</div>