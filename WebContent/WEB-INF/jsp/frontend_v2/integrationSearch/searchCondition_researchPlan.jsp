<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags"%>

<div class="well">
		<div class="row">
			<div class="col-sm-3 col-xs-12">
				<s:textfield name="searchCondition.researchPlanSearchModel.planName"
					class="form-control" placeholder="計畫名稱" style="font-size:18px" />
			</div>
			<div class="col-sm-3 col-xs-12">
				<s:textfield name="searchCondition.researchPlanSearchModel.planNo"
					class="form-control" placeholder="計畫編號" style="font-size:18px" />
			</div>
			<div class="col-sm-3 col-xs-12">
				<s:textfield name="searchCondition.researchPlanSearchModel.keyword"
					class="form-control" placeholder="計畫關鍵詞" style="font-size:18px" />
			</div>
			<div class="col-sm-3 col-xs-12">
				<s:textfield name="searchCondition.researchPlanSearchModel.manager"
					class="form-control" placeholder="計畫主持人" style="font-size:18px" />
			</div>
			<div class="col-sm-3 col-xs-12">
				<s:textfield name="searchCondition.researchPlanSearchModel.technologyName"
					class="form-control" placeholder="技術名稱" style="font-size:18px" />
			</div>
			
			<div class="col-sm-3 col-xs-12">
			    <div style="float: left; width: 48%;">
				    <s:select name="searchCondition.researchPlanSearchModel.yearStart" list="searchCondition.yearList"
					listKey="code" listValue="name" headerKey="" headerValue="計畫年度(起)" />
			    </div>
			    <div style="float: right; width: 48%;">
				    <s:select name="searchCondition.researchPlanSearchModel.yearEnd" list="searchCondition.yearList"
					listKey="code" listValue="name" headerKey="" headerValue="計畫年度(訖)" />
			    </div>
            </div>
            <div class="col-sm-3 col-xs-12">
				<s:select name="searchCondition.researchPlanSearchModel.trlId" list="searchCondition.optionTrlList" listKey="id" listValue="%{code +'-'+ name}" headerKey="-1" headerValue="全部計畫發展階段"/>
			</div>
            <div class="col-sm-3 col-xs-12">
				<s:select name="searchCondition.researchPlanSearchModel.grbDomainId" list="searchCondition.optionGrbDomainList" listKey="id" listValue="%{code +'-'+ name}" headerKey="-1" headerValue="全部研究領域"/>
			</div>
            <div class="col-sm-3 col-xs-12">
				<s:select name="searchCondition.researchPlanSearchModel.technologyTrlId" list="searchCondition.optionTrlList" listKey="id" listValue="%{code +'-'+ name}" headerKey="-1" headerValue="全部技術發展階段"/>
			</div>
		</div>
</div>