<%@ include file="/WEB-INF/jsp/common.jsp"%>
<%@ page import="com.absir.core.kernel.*"%>
<%@ page import="com.absir.appserv.system.service.statics.*"%>

<%-- G_BEGAN <%importClass> --%>
<%@page import="com.absir.appserv.system.bean.JMaMenu"%>
<%@page import="com.absir.appserv.system.bean.JPermission"%>
<%@page import="com.absir.appserv.system.bean.JUserRole"%>
<%@page import="com.absir.appserv.system.bean.value.JeVote"%>
<%@page import="com.absir.appserv.system.bean.value.JeVotePermission"%>
<%@page import="com.absir.appserv.system.service.BeanService"%>
<%@page import="java.util.List"%>
<%-- G_END --%>

<div class="pageFormContent" layoutH="56">
	<p> <label>实体标识:</label> <input name="id" type="text" size="30" value="${entity.id}" ${create ? "" : " readonly"}="true" /> </p>
<p> <label>实体名称:</label> <input name="caption" type="text" size="30" value="${entity.caption}" /> </p>
<div class="divider"></div>
	<c:set value="${entity}" var="entityContext"/><div class="tabs">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li id="permissions"><a href="javascript:;"><span>授权信息</span></a></li></ul>
			</div>
		</div>
		<div class="tabsContent">
			
			<%-- G_BEGAN --%>
			<c:if test="${!create}">
				<div id="permissions" class="pageContent">
					<input name="!subtables" type="hidden" value="permissions" />
					<table class="list nowrap itemDetail" width="100%" addButton="">
						<thead>
							<tr>
								<th>角色</th>
								<th>查看</th>
								<th>编辑</th>
								<th>创建</th>
								<th>删除</th>
								<th>授权字段</th>
								<th>禁止字段</th>
								<th class="option" width="60">操作</th>
							</tr>
						</thead>
						<%
							JMaMenu maMenu = (JMaMenu) pageContext.findAttribute("entity");
								List<JUserRole> userRoles = BeanService.ME.findAll(JUserRole.class);
								JUserRole defaultUserRole = new JUserRole();
								defaultUserRole.setId(0L);
								defaultUserRole.setRolename("全部用户");
								userRoles.add(0, defaultUserRole);
								defaultUserRole = new JUserRole();
								defaultUserRole.setId(-1L);
								defaultUserRole.setRolename("匿名用户");
								userRoles.add(0, defaultUserRole);
								for (JUserRole userRole : userRoles) {
									Long roleId = userRole.getId();
									JPermission permission = maMenu.getPermissions().get(roleId);
						%>
						<tr>
							<td><%=userRole.getRolename()%></td>
							<td>
								<%
									JeVote jeVote = permission == null ? null : permission.getSelectable();
								%> <input name="permissions['<%=roleId%>'].selectable"
								type="radio" value="NONE"
								<%if (jeVote == null || jeVote == JeVote.NONE)
						out.print(" checked=\"checked\"");%> />默认
								<input name="permissions['<%=roleId%>'].selectable" type="radio"
								value="ALLOW"
								<%if (jeVote == JeVote.ALLOW)
						out.print(" checked=\"checked\"");%> />允许
								<input name="permissions['<%=roleId%>'].selectable" type="radio"
								value="FORBID"
								<%if (jeVote == JeVote.FORBID)
						out.print(" checked=\"checked\"");%> />禁止
							</td>
							<td>
								<%
									jeVote = permission == null ? null : permission.getUpdatable();
								%> <input name="permissions['<%=roleId%>'].updatable"
								type="radio" value="NONE"
								<%if (jeVote == null || jeVote == JeVote.NONE)
						out.print(" checked=\"checked\"");%> />默认
								<input name="permissions['<%=roleId%>'].updatable" type="radio"
								value="ALLOW"
								<%if (jeVote == JeVote.ALLOW)
						out.print(" checked=\"checked\"");%> />允许
								<input name="permissions['<%=roleId%>'].updatable" type="radio"
								value="FORBID"
								<%if (jeVote == JeVote.FORBID)
						out.print(" checked=\"checked\"");%> />禁止
							</td>
							<td>
								<%
									jeVote = permission == null ? null : permission.getInsertable();
								%> <input name="permissions['<%=roleId%>'].insertable"
								type="radio" value="NONE"
								<%if (jeVote == null || jeVote == JeVote.NONE)
						out.print(" checked=\"checked\"");%> />默认
								<input name="permissions['<%=roleId%>'].insertable" type="radio"
								value="ALLOW"
								<%if (jeVote == JeVote.ALLOW)
						out.print(" checked=\"checked\"");%> />允许
								<input name="permissions['<%=roleId%>'].insertable" type="radio"
								value="FORBID"
								<%if (jeVote == JeVote.FORBID)
						out.print(" checked=\"checked\"");%> />禁止
							</td>
							<td>
								<%
									jeVote = permission == null ? null : permission.getDeletable();
								%> <input name="permissions['<%=roleId%>'].deletable"
								type="radio" value="NONE"
								<%if (jeVote == null || jeVote == JeVote.NONE)
						out.print(" checked=\"checked\"");%> />默认
								<input name="permissions['<%=roleId%>'].deletable" type="radio"
								value="ALLOW"
								<%if (jeVote == JeVote.ALLOW)
						out.print(" checked=\"checked\"");%> />允许
								<input name="permissions['<%=roleId%>'].deletable" type="radio"
								value="FORBID"
								<%if (jeVote == JeVote.FORBID)
						out.print(" checked=\"checked\"");%> />禁止
							</td>
							<td><input
								name="permissions['<%=userRole.getId()%>'].allows" type="text"
								style="width: 80%"
								value="<%=permission == null ? "" : WebJsplUtils.paramsValue(permission.getAllows())%>" /></td>
							<td><input
								name="permissions['<%=userRole.getId()%>'].forbiddens"
								type="text" style="width: 80%"
								value="<%=permission == null ? "" : WebJsplUtils.paramsValue(permission.getForbiddens())%>" /></td>
							<td></td>
						</tr>
						<%
							}
						%>
						</tbody>
					</table>
				</div>
			</c:if>
			<%-- G_END --%>

</div>
		<div class="tabsFooter">
			<div class="tabsFooterContent"></div>
		</div>
	</div>
	</div>