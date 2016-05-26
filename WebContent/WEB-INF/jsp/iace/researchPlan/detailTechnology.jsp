<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<body>
	<h4>研發成果明細</h4>

	<div class="container-fluid" >
		<s:hidden name="id"/>
		<s:hidden name="technologyId"/>
		
		<s:textfield label="技術名稱" name="technology.name" readonly="true" cssClass="form-control" labelposition="left"/>				
		<s:textarea label="技術簡述" name="technology.descriptoin" readonly="true" cssClass="form-control"  />
		<s:select label="技術發展階段" name="technology.optionTrlCodes" list="optionTrlList" listKey="code" listValue="%{code +' ' +name}"  disabled="true" multiple="true"/>
		<s:textarea label="技術發展階段說明" name="technology.trlDesc" readonly="true" cssClass="form-control"/>	
	</div>
	
	<s:url value="update.action" var="updateUrlTag">
		<s:param name="id" value="id" />
	</s:url>
	<input type="button" class="btn btn-default" value="回上一頁"
		onclick="window.location.href='<s:property value="#updateUrlTag" />'" />

</body>
</html>