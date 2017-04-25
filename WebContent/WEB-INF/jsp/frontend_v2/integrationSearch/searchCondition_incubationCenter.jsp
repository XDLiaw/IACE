<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags"%>

	<div class="well">
		<div class="row">
			<div class="col-sm-3 col-xs-12">
				<s:textfield placeholder="學校/單位名稱" name="searchCondition.incubationCenterSearchModel.name" maxlength="500" />
			</div>
			<div class="col-sm-3 col-xs-12">
				<s:select name="searchCondition.incubationCenterSearchModel.attribute" list="searchCondition.attributeList" listKey="code" listValue="%{name}" headerKey="" headerValue="請選擇屬性" />
			</div>
		</div>
	</div>