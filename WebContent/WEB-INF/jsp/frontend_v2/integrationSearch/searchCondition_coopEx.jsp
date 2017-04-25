<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags"%>

<div class="well">
		<div class="row">
			<div class="col-sm-3 col-xs-12">
				<s:select name="searchCondition.coopExSearchModel.year" list="#{'':'請選擇年度', '2014':'2014', '2015':'2015', '2016':'2016', '2017':'2017' }" />

			</div>
			<div class="col-sm-3 col-xs-12">
				<s:select name="searchCondition.coopExSearchModel.type" list="searchCondition.typeList" listKey="code" listValue="name" headerKey="" headerValue="--類別--"/>
			</div>
			<div class="col-sm-3 col-xs-12">
				<s:textfield name="searchCondition.coopExSearchModel.searchText" placeholder="關鍵字"/>
			</div>
			<div class="col-sm-3 col-xs-12">
				<s:textfield name="searchCondition.coopExSearchModel.rdTeam" placeholder="研發團隊"/>
			</div>
			<div class="col-sm-3 col-xs-12">
					<s:textfield name="searchCondition.coopExSearchModel.assisTeam" placeholder="輔導團隊"/>
			</div>

		</div>
</div>