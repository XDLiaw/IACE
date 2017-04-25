<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags"%>

<div class="well">
		<div class="row">
			 <div class="col-sm-3 col-xs-12">
				<s:select name="searchCondition.literatureSearchModel.category" list="#{'文獻':'文獻', '法規政策':'法規政策' }"/>
			</div>
			<div class="col-sm-3 col-xs-12">
				<s:textfield name="searchCondition.literatureSearchModel.searchText"
					class="form-control" placeholder="標題名" style="font-size:18px" />
			</div>
			<div class="col-sm-3 col-xs-12">
				<s:textfield name="searchCondition.literatureSearchModel.language"
					class="form-control" placeholder="語文" style="font-size:18px" />
			</div>
			<div class="col-sm-3 col-xs-12">
				<s:textfield name="searchCondition.literatureSearchModel.publishYear"
					class="form-control" placeholder="出版年份" style="font-size:18px" />
			</div>
			
		</div>
</div>