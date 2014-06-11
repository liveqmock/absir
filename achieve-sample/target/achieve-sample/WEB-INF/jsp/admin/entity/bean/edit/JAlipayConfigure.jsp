<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<div class="pageFormContent" layoutH="56">
	<p> <label>支付宝帐号:</label> <input name="username" type="text" size="30" value="${entity.username}" /> </p>
<p> <label>合作者ID:</label> <input name="partner" type="text" size="30" value="${entity.partner}" /> </p>
<p> <label>商户私钥:</label> <input name="key" type="text" size="30" value="${entity.key}" /> </p><div class="divider"></div>
<div class=" p"> 
 <label>支付宝公钥:</label> 
 <textarea name="publicKey" type="text" cols="87" rows="7">${entity.publicKey}</textarea> 
</div>
<div class="divider"></div>
	</div>