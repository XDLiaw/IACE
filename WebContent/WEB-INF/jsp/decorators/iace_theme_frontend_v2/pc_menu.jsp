<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags" %>

<style>
.menu_fixed {
	z-index: 9999;
	position: fixed;
	top: 0px;
	width: 100%;
}
</style>
<script type="text/javascript">
	//選單列黏住上方浮動
	$(window).bind('scroll', function() {
		if ($(window).scrollTop() > 110) {
			$('#main_menu').addClass('menu_fixed');
		} else {
			$('#main_menu').removeClass('menu_fixed');
		}
	});
</script>

<!-- Top 電腦版 -->
<div class="container pc_menu">
	<div class="row">
		<div class="col-md-3 col-sm-3">
			<div>
				<a href="<s:url value="/index"/>">
					<img src="<s:url value="/images/frontend-v2/logo.jpg"/>" alt="" style="max-height:100px;"/>
				</a>
			</div>
		</div>
		<div class="col-md-9 col-sm-9 text-right">
			<s:form namespace="/f2/integrationSearch" action="index" method="post" validate="true">
				<div class="input-group pull-right" style="width:300px; margin-top:15px;">
					<input type="text" name="searchCondition.searchText" class="form-control" placeholder="Search"> 
					<span class="input-group-btn">
						<button class="btn btn-info" type="submit" style="background-color: #1eb4da;">
							<i class="fa fa-search" aria-hidden="true"></i>
						</button>
						<s:if test="#session.member!= null">
							<button class="btn btn-warning" type="button" style="background-color: #ec971f; margin-left: 1px;" onclick="window.location.href='<s:url value="/f2/member/logout"/>';">
								<i class="fa fa-sign-out" aria-hidden="true"></i>登出
							</button>
						</s:if>
					</span>
				</div>
			</s:form>
			<div class="clearfix"></div>
			<div class="menu_list">
				<ul class="list-inline">
					<li class="dropdown left15">
						<a href="<s:url value="/f2/videosArea/init"/>" class=" menu_link">
							影音專區
						</a>
					</li>
					<li class="dropdown left15">
						<a href="#" class="dropdown-toggle menu_link" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" id="dropdownMenu1">
							活動/人培<span class="caret"></span>
						</a>
						<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
							<s:iterator value="@iace.entity.activity.Activity@getCategoryList()" status="stat">
								<li>
									<s:url value="/f2/activity/init" var="urlTag" escapeAmp="false">
										<s:param name="searchCondition.category" value="code" />
									</s:url>
									<a href="<s:property value="urlTag"/>"><s:property value="name"/></a>
								</li>
							</s:iterator>
						</ul>
					</li>
					<li class="dropdown left15">
						<a href="#" class="dropdown-toggle menu_link" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" id="dropdownMenu2">
							公告訊息<span class="caret"></span>
						</a>
						<ul class="dropdown-menu" aria-labelledby="dropdownMenu2">
							<s:iterator value="@iace.entity.news.News@getCategoryList()" status="stat">
								<li>
									<s:url value="/f2/news/init" var="urlTag" escapeAmp="false">
										<s:param name="searchCondition.category" value="code" />
									</s:url>
									<a href="<s:property value="urlTag"/>"><s:property value="name"/></a>
								</li>
							</s:iterator>
						</ul>
					</li>
					<li class="dropdown left15">
						<a href="#" class="dropdown-toggle menu_link" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" id="dropdownMenu3">
							產學合作案例<span class="caret"></span>
						</a>
						<ul class="dropdown-menu" aria-labelledby="dropdownMenu3">
							<s:iterator value="@iace.entity.coopExample.CoopEx@getTypeList()" status="stat">
								<li>
									<s:url value="/f2/coopEx/init" var="urlTag" escapeAmp="false">
										<s:param name="searchCondition.type" value="code" />
									</s:url>
									<a href="<s:property value="urlTag"/>"><s:property value="name"/></a>
								</li>
							</s:iterator>
						</ul>
					</li>
					<li class="dropdown left15">
						<a href="#" class="dropdown-toggle menu_link" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" id="dropdownMenu4">
							學界研發成果<span class="caret"></span>
						</a>
						<ul class="dropdown-menu" aria-labelledby="dropdownMenu4">
							<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.researchPlan.ResearchPlan"/>">學研成果</a></li>
							<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.patent.Patent"/>">學界專利</a></li>
							<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.talentedPeople.TalentedPeople"/>">產學人才</a></li>
						</ul>
					</li>
					<li class="dropdown left15">
						<a href="#" class="dropdown-toggle menu_link" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" id="dropdownMenu7">
							研發焦點<span class="caret"></span>
						</a>
						<ul class="dropdown-menu" aria-labelledby="dropdownMenu7">
							<s:iterator value="@iace.entity.rdFocus.RdFocus@getCategoryList()" status="stat">
								<li>
									<s:url value="/f2/rdFocus/init" var="urlTag" escapeAmp="false">
										<s:param name="searchCondition.category" value="code" />
									</s:url>
									<a href="<s:property value="urlTag"/>"><s:property value="name"/></a>
								</li>
							</s:iterator>
						</ul>
					</li>
					<li class="dropdown left15">
						<a href="#" class="dropdown-toggle menu_link" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" id="dropdownMenu5">
							產業情報<span class="caret"></span>
						</a>
						<ul class="dropdown-menu" aria-labelledby="dropdownMenu5">
							<s:iterator value="@iace.entity.industryInfo.IndustryInfo@getCategoryList()" status="stat">
								<li>
									<s:url value="/f2/industryInfo/init" var="urlTag" escapeAmp="false">
										<s:param name="searchCondition.category" value="code" />
									</s:url>
									<a href="<s:property value="urlTag"/>"><s:property value="name"/></a>
								</li>
							</s:iterator>
							<li>
								<s:url value="/f2/enterpriseRequireTech/init"  var="urlTag" escapeAmp="false">
								</s:url>
								<a href="<s:property value="urlTag"/>">企業需求</a>
							</li>
						</ul>
					</li>
					<li class="dropdown left15">
						<a href="#" class="dropdown-toggle menu_link" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" id="dropdownMenu6">
							媒合專區<span class="caret"></span>
						</a>
						<ul class="dropdown-menu" aria-labelledby="dropdownMenu6">
							<li><a href="<s:url value="/f2/matchIntro/init"/>">產學媒合服務團簡介</a></li>
							<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.literature.Literature"/>">法規政策/文獻</a></li>
							<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.incubationCenter.IncubationCenter"/>">育成中心</a></li>
							<li><a href="#">問卷調查</a></li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
	</div>
</div>
<div class="blue_menu pc_menu" id="main_menu">
	<div class="container">
		<div class="row">
			<div class="col-sm-2">
				<a href="<s:url value="/index"/>" class="blue_menu_link">
					<i class="fa fa-home right5" aria-hidden="true"></i>首頁
				</a>
			</div>
			<div class="col-sm-2">
				<a href="<s:url value="/f2/about/init"/>" class="blue_menu_link">
					<i class="fa fa-pencil right5" aria-hidden="true"></i>產學合作計畫
				</a>
			</div>
			<div class="col-sm-2 text-center">
				<a href="<s:url value="/f2/consulting/create"/>" class="blue_menu_link">
					<i class="fa fa-phone right5" aria-hidden="true"></i>我要諮詢
				</a>
			</div>
			<div class="col-sm-2 text-center">
				<s:url value="/f2/faq/init" var="urlTag" escapeAmp="false">
					<s:param name="searchCondition.category" value="@iace.entity.faq.Faq@getCategoryList()[0].code" />
				</s:url>
				<a href="<s:property value="%{#urlTag}"/>" class="blue_menu_link">
					<i class="fa fa-book right5" aria-hidden="true"></i>常問集
				</a>
			</div>
			<div class="col-sm-2 text-right">
				<a href="<s:url value="/f2/member/memberCenter"/>" class="blue_menu_link">
					<i class="fa fa-user-circle-o right5" aria-hidden="true"></i>會員中心
				</a>
			</div>
			<div class="col-sm-2 text-right">
				<a href="<s:url value="/f2/ePaper/init"/>" class="blue_menu_link">
					<i class="fa fa-envelope-open right5" aria-hidden="true"></i>電子報
				</a>
			</div>
		</div>
	</div>
</div>