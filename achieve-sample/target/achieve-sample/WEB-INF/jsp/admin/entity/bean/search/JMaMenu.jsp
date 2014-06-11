<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>
<%EntityStatics.searchConditionMap(WebJsplUtils.getInput(request));%><p class="s_id"><label>实体标识: </label> <input name="id" type="text" size="20" value="${searchConditionMap['id']}" /> </p>
<p class="s_caption"><label>实体名称: </label> <input name="caption" type="text" size="20" value="${searchConditionMap['caption']}" /> </p>
