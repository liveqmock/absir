<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p> <label>商品ID:</label> <input name="id" type="text" size="30" value="${entity.id}" ${create ? "" : " readonly"}="true" class=" required" /> </p>
<p> <label>宝石数量:</label> <input name="diamond" type="text" size="30" value="${entity.diamond}" /> </p>
<p> <label>价格:</label> <input name="price" type="text" size="30" value="${entity.price}" /> </p>
<div class="divider"></div>
	</div>