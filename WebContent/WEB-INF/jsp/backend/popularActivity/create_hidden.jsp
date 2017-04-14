<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="hidden-template-value">
	<s:hidden name="title"/>
	
	<s:iterator value="activityIds" status="stat">
		<input type="hidden" name="activityIds" value="<s:property/>"/>
	</s:iterator>
</div>