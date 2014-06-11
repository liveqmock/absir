<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p> <label>订单号:</label> <input name="id" type="text" size="30" value="${entity.id}" ${create ? "" : " readonly"}="true" /> </p>
<p> <label>支付订单号:</label> <input name="tradeNo" type="text" size="30" value="${entity.tradeNo}" /> </p>
<c:set var="value" value="${entity.createTime}" />
<p> <label>创建时间:</label> 
<input name="createTime" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="30" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), 0)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> </p>
<div class="divider"></div>
	</div>