<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p> <label>订单号:</label> <input name="id" type="text" size="30" value="${entity.id}" ${create ? "" : " readonly"}="true" /> </p>
<c:set var="value" value="${entity.createTime}" />
<p> <label>创建时间:</label> 
<input name="createTime" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="30" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), 0)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> </p>
<p> <label>用户名:</label> <input name="uid" type="text" size="30" value="${entity.uid}" /> </p>
<p> <label>商品类名:</label> <input name="name" type="text" size="30" value="${entity.name}" /> </p>
<p> <label>商品参数:</label> <input name="nameData" type="text" size="30" value="${entity.nameData}" /> </p>
<p> <label>支付总金额:</label> <input name="amount" type="text" size="30" value="${entity.amount}" /> </p>
<c:set value="${entity.status}" var="value"/>
<p> <label>交易状态:</label> <select name="status" value="<%=WebJsplUtils.enumValue(pageContext.getAttribute("value"))%>" class=" combox"><option value="">请选择</option><option value="CLOSED">交易关闭</option> <option value="ERROR">交易错误</option> <option value="COMPLETE">交易完成</option> <option value="PAYED">付款完成</option></select> </p>
<p> <label>支付订单号:</label> <input name="tradeNo" type="text" size="30" value="${entity.tradeNo}" /> </p>
<p> <label>平台名称:</label> <input name="platform" type="text" size="30" value="${entity.platform}" /> </p>
<p> <label>平台支付参数:</label> <input name="platformData" type="text" size="30" value="${entity.platformData}" /> </p>
<div class="divider"></div>
	</div>