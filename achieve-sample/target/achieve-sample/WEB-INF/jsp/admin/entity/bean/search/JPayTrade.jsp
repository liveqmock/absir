<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<%EntityStatics.searchConditionMap(WebJsplUtils.getInput(request));%><p class="s_id"><label>订单号: </label> <input name="id" type="text" size="20" value="${searchConditionMap['id']}" /> </p>
<c:set var="value" value="${searchConditionMap['createTime >=']}" />
<p class="s_createTime"><label>创建时间: </label> 
<input name="createTime >=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> 
 <c:set var="value" value="${searchConditionMap['createTime <=']}" />
<input name="createTime <=" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="20" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), -1)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a></p>
<p class="s_uid"><label>用户名: </label> <input name="uid" type="text" size="20" value="${searchConditionMap['uid']}" /> </p>
<p class="s_name"><label>商品类名: </label> <input name="name" type="text" size="20" value="${searchConditionMap['name']}" /> </p>
<p class="s_nameData"><label>商品参数: </label> <input name="nameData" type="text" size="20" value="${searchConditionMap['nameData']}" /> </p>
<p class="s_amount"><label>支付总金额: </label> <input name="amount >=" type="text" size="10" value="${searchConditionMap['amount >=']}" /> <input name="amount <=" type="text" size="10" value="${searchConditionMap['amount <=']}" /></p>
<c:set value="${searchConditionMap['status']}" var="value"/>
<p class="s_status"><label>交易状态: </label> <select name="status" value="<%=WebJsplUtils.enumValue(pageContext.getAttribute("value"))%>" class=" combox"><option value="">请选择</option><option value="CLOSED">交易关闭</option> <option value="ERROR">交易错误</option> <option value="COMPLETE">交易完成</option> <option value="PAYED">付款完成</option></select> </p>
<p class="s_platform"><label>平台名称: </label> <input name="platform" type="text" size="20" value="${searchConditionMap['platform']}" /> </p>
