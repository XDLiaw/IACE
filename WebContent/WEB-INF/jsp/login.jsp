<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title></title>
</head>
<body>
    <h2 class="itemTitle">登入後台管理系統</h2>
    <s:form action="loginSubmit" method="post" validate="true" >
		<ul>
			<li class="half">
				<b>帳號</b>
				<s:textfield name="sysUser.account" autocomplete="off"/>
			</li>
			<li class="half">
				<b>密碼</b>
				<s:password name="sysUser.password" autocomplete="off"/>
			</li>			
		</ul>
    
    	<div class="clear"></div>
    
    	<s:submit cssClass="btn btn-default redBtn" value="LOGIN" />	
    </s:form>
</body>
</html>