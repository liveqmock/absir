<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<%EntityStatics.searchConditionMap(WebJsplUtils.getInput(request));%><p class="s_id"><label>商品ID: </label> <input name="id" type="text" size="20" value="${searchConditionMap['id']}" /> </p>
<p class="s_diamond"><label>宝石数量: </label> <input name="diamond >=" type="text" size="10" value="${searchConditionMap['diamond >=']}" /> <input name="diamond <=" type="text" size="10" value="${searchConditionMap['diamond <=']}" /></p>
<p class="s_price"><label>价格: </label> <input name="price >=" type="text" size="10" value="${searchConditionMap['price >=']}" /> <input name="price <=" type="text" size="10" value="${searchConditionMap['price <=']}" /></p>
