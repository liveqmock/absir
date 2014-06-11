<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p> <label>验证主键:</label> <input name="id" type="text" size="30" value="${entity.id}" ${create ? "" : " readonly"}="true" /> </p>
<c:set var="value" value="${entity.passTime}" />
<p> <label>过期时间:</label> 
<input name="passTime" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="30" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), 0)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> </p>
<p> <label>用户名:</label> <input name="username" type="text" size="30" value="${entity.username}" /> </p>
<c:set var="value" value="${entity.lastTime}" />
<p> <label>最后登录:</label> 
<input name="lastTime" class="dateTime" value="${value}" type="hidden" size="30"/><input type="text" size="30" value="<%=WebJsplUtils.dateValue(pageContext.getAttribute("value"), 0)%>" class=" date" datefmt="yyyy-MM-dd HH:mm:ss" /><a class="inputDateButton" href="javascript:;">选择</a> </p>
<p> <label>请求地址:</label> <input name="addrest" type="text" size="30" value="${entity.addrest}" /> </p>
<p> <label>请求路径:</label> <input name="url" type="text" size="30" value="${entity.url}" /> </p>
<p> <label>请求来源:</label> <input name="agent" type="text" size="30" value="${entity.agent}" /> </p>
<p> <label>关联用户:</label> <input name="user" type="text" size="30" value="${entity.user}" readonly="readonly" /> </p>
<div class="divider"></div>
	</div>