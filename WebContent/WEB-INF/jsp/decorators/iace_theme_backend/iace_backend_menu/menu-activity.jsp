<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags" %>

<s:if test='%{#session.sysUser.hasNamespaceStartWith("/activity")}'>
	<li><a href="#">活動人培</a>
		<ul>
			<s:if test='%{#session.sysUser.hasAuth("/activity", "init")}'>
				<li><a href="<s:url value="/activity/init"/>">編輯管理</a></li>
			</s:if>
			<s:if test='%{#session.sysUser.hasAuth("/activity", "create")}'>
				<li><a href="<s:url value="/activity/create"/>">新增</a></li>
			</s:if>	
			<s:if test='%{#session.sysUser.hasAuth("/popularActivity", "update")}'>
				<li><a href="<s:url value="/popularActivity/init"/>">hot20</a></li>
			</s:if>	
		</ul>
	</li>
</s:if>
