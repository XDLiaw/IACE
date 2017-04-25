<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags"%>

<div class="well">
		<div class="row">
			<div class="col-sm-3 col-xs-12">
				<s:textfield placeholder="專利名稱" name="searchCondition.patentSearchModel.name" maxlength="300" />
			</div>
			<div class="col-sm-3 col-xs-12">
				<s:textfield placeholder="專利權人" name="searchCondition.patentSearchModel.assignee" maxlength="500" />
			</div>
			<div class="col-sm-3 col-xs-12">
				<s:textfield placeholder="申請號" name="searchCondition.patentSearchModel.appliactionNo" maxlength="100" />
			</div>
			
			<div class="col-sm-4 col-xs-12">
			    <div style="float: left; width: 48%;">
				    <s:textfield placeholder="申請日(起)" name="searchCondition.patentSearchModel.applicationDateS" maxlength="10" cssClass="calendarBox" />
			    </div>
			    <div style="float: right; width: 48%;">
				    <s:textfield placeholder="申請日(迄)" name="searchCondition.patentSearchModel.applicationDateE" maxlength="10" cssClass="calendarBox" />
			    </div>
            </div>
            <div class="col-sm-2 col-xs-12">
					<s:select name="searchCondition.patentSearchModel.countryCode" list="searchCondition.optionCountryList" listKey="code" listValue="%{code+' - '+name}" headerKey="" headerValue="請選擇申請國" />
			</div>
            <div class="col-sm-2 col-xs-12">
					<s:select name="searchCondition.patentSearchModel.techFieldId" list="searchCondition.techFieldList" listKey="id" listValue="name" headerKey="-1" headerValue="請選擇專利技術領域" />
			</div>
    
		</div>
</div>