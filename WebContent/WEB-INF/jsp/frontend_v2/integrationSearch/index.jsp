<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	var count=0;
	$(document).ready(function() {
	 	// 注意: 在此頁面的重置按鈕記得要加上id
		$("#btn-reset").click(function(){
			$("input[type=text]").val("");// 因為會把 pc_menu 的input text className清空 造成難以判斷 所以把pc_menu裡的改成hidden
			$("select").prop('selectedIndex', 0);
			$("input[type=checkbox]").prop("checked", false);
			$("select").prop('selectedIndex', 0);
		});
	
		highlightKeyword();
	
		$(".list-item").click(function(){
			$(this).find("a")[0].click();
		});
		$(".list-item a").click(function(e) {
			e.stopPropagation(); //避免將事件傳遞給parent element導致觸發兩次連結的點擊事件
		});
		$(".pc_menu .classNameBtn").click(function(){
			count=count+1;
			if(count>1){
				//防止案分頁時 換了搜索模組 可能會出錯 但本身刷新分頁時就會改變一次 所以先給一次 submit後自動歸零
				$("#div-network-diagram").hide();
				$(".row").find(".pagination").hide();
				$("div.list-item").hide();
			}
			$(".search-active").removeClass("search-active").hide();
			if(($(this).find(".classNameValue").val()=="iace.entity.researchPlan.ResearchPlan")){
				$("#searchCondition_researchPlan").addClass("search-active").show();
			}
			else if($(this).find(".classNameValue").val()=="iace.entity.patent.Patent"){
				$("#searchCondition_patent").addClass("search-active").show();
			}
			else if($(this).find(".classNameValue").val()=="iace.entity.talentedPeople.TalentedPeople"){
				$("#searchCondition_talentedPeople").addClass("search-active").show();
			}
			else if($(this).find(".classNameValue").val()=="iace.entity.coopExample.CoopEx"){
				$("#searchCondition_coopEx").addClass("search-active").show();
			}
			else if($(this).find(".classNameValue").val()=="iace.entity.literature.Literature"){
				$("#searchCondition_literature").addClass("search-active").show();
			}
			else if($(this).find(".classNameValue").val()=="iace.entity.incubationCenter.IncubationCenter"){
				$("#searchCondition_incubationCenter").addClass("search-active").show();
			}
			else {
				 $("#searchCondition_base").addClass("search-active").show();
			}
		});
	});
</script>
<script>
	function highlightKeyword() {
		var searchText = '<s:property value="searchCondition.searchText"/>';
		var keywords = searchText.trim().split(" ");
		for (var i=0; i<keywords.length; i++) {
			var keyword = keywords[i].toLowerCase().trim();
			if (keyword != "and" && keyword != "or") {
				$("#search-result-list .list-item td").not(".category, .date_01").highlight(keyword);
			}
		}
	}
</script>
<style>
#search-result-list .highlight {background-color: #FFFF00}
.list-item div.well:hover { 
	border:#000000 1px solid;
	cursor:pointer;
}
</style>
</head>
<body>
	<s:include value="./banner.jsp" />

	<s:form action="index" method="post" validate="true">
		<s:hidden name="searchCondition.className"/>
<%-- 		<s:textfield name="searchCondition.className"/> --%>
	
		<!-- 分類 -->
		<div class="container top50">
			<div class="row">
				<s:include value="./index_searchSubTitle.jsp" />
			</div>
			<div class="row">
				<!-- 手機版 -->
				<s:include value="./mobile_menu.jsp" />
				<!-- PC版 -->
				<s:include value="./pc_menu.jsp" />
			
				<!-- 搜尋輸入區塊 -->
				<div class="col-sm-12 col-xs-12" style="display:none" id="searchCondition_base">
					<s:include value="./searchCondition_base.jsp" />
				</div>
				<div class="col-sm-12 col-xs-12" style="display:none" id="searchCondition_researchPlan">
					<s:include value="./searchCondition_researchPlan.jsp" />
				</div>
				<div class="col-sm-12 col-xs-12" style="display:none" id="searchCondition_patent">
					<s:include value="./searchCondition_patent.jsp" />
				</div>
				<div class="col-sm-12 col-xs-12" style="display:none" id="searchCondition_talentedPeople">
					<s:include value="./searchCondition_talentedPeople.jsp" />
				</div>
				<div class="col-sm-12 col-xs-12" style="display:none" id="searchCondition_coopEx">
					<s:include value="./searchCondition_coopEx.jsp" />
				</div>
				<div class="col-sm-12 col-xs-12" style="display:none" id="searchCondition_literature">
					<s:include value="./searchCondition_literature.jsp" />
				</div>
				<div class="col-sm-12 col-xs-12" style="display:none" id="searchCondition_incubationCenter">
					<s:include value="./searchCondition_incubationCenter.jsp" />
				</div>
				<div class="col-sm-4 col-xs-12">
					<button type="submit" class="btn btn-primary" id="btn-search"><i class="fa fa-search-plus right5" aria-hidden="true"></i><span style="font-size:18px">搜尋</span></button>&nbsp;
					<button type="button" class="btn btn-default" id="btn-reset"><span style="font-size:18px">清除</span></button>
				</div>				
			</div>
		</div>

		<!-- 搜尋結果 -->
		<div class="container">
			<div class="row">
				<s:include value="./index_resultSubTitle.jsp" />
			</div>
			<s:if test="searchCondition.className == 'iace.entity.researchPlan.ResearchPlan'">
				<s:include value="./researchPlanManagerGraph.jsp" />
			</s:if>
			<div class="row">
				<s:include value="./pagination_top.jsp" />
			</div>
			<div class="row" id="search-result-list">
				<s:if test="pagedList.list.size == 0">
					<label style="font-size:24px; color:#FF0033;">查無資料，請重新查詢!</label>
				</s:if>
				<s:else>
					<s:iterator value="pagedList.list" status="stat">
						<div class="list-item">
							<!-- 研發成果 -->
							<s:if test="researchPlan != null">
								<s:include value="./listItem_researchPlan.jsp" />
							</s:if>
							<s:if test="technology != null">
								<s:include value="./listItem_technology.jsp" /> <!-- 已棄用!若出現則代表有錯 -->
							</s:if>
							<!-- 專利資料 -->
							<s:if test="patent != null">
								<s:include value="./listItem_patent.jsp" />
							</s:if>
							<!-- 合作案例 -->
							<s:if test="coopEx != null">
								<s:include value="./listItem_coopEx.jsp" />
							</s:if>
							<!-- 產學人才 -->
							<s:if test="talentedPeople != null">
								<s:include value="./listItem_talentedPeople.jsp" />
							</s:if>
							<!-- 育成中心 -->
							<s:if test="incubationCenter != null">
								<s:include value="./listItem_incubationCenter.jsp" />
							</s:if>
							<!-- 法規政策 -->
							<s:if test='literature != null'>
								<s:include value="./listItem_policyAndLiterature.jsp" />
							</s:if>
							<!-- 活動/人培 -->
							<s:if test='activity != null'>
								<s:include value="./listItem_activity.jsp" />
							</s:if>
							<!-- 產業情報 -->
							<s:if test='industryInfo != null'>
								<s:include value="./listItem_industryInfo.jsp" />
							</s:if>
							<!-- 公告訊息 -->
							<s:if test='news != null'>
								<s:include value="./listItem_news.jsp" />
							</s:if>
						</div>
					</s:iterator>
				</s:else>
			</div>
			<div class="row">
				<s:include value="./pagination.jsp" />
			</div>
		</div>
	</s:form>
	<br>
</body>