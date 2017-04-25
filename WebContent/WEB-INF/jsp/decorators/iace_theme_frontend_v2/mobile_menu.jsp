<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags" %>

<!-- Menu 手機版 -->
<nav class="mobile_menu" style="border:none; background-color:#FFF; width:100%; margin-bottom:0px;">
	<div class="container-fluid nopadding">
		<div class="pull-left">
			<a href="<s:url value="/index"/>">
				<img src="<s:url value="/images/frontend-v2/logo.jpg"/>" alt="" style="height:60px; margin-left:10px;"/>
			</a>
		</div>
		<div class="pull-right" style="padding:15px 10px;">
			<a href="#" onclick="$('#menu_dropdown').slideToggle('fast');"><i class="fa fa-bars fa-2x" style="color:#1fb5da;" aria-hidden="true"></i></a>
		</div>
		<div class="pull-right" style="padding:15px 10px;">
			<a href="<s:url value="/f2/integrationSearch/init"/>"><i class="fa fa-search fa-2x" style="color:#1fb5da;"></i></a>
		</div>
		<div class="clear-fix"></div>
		<div id="menu_dropdown">
			<ul style="background-color:#F8F8F8; margin-bottom:0;">
				<s:if test="#session.member!= null">
					<li style="border-bottom:#DDD 1px solid" role="presentation" >
						<a href="<s:url value="/f2/member/logout"/>" class="menu_link_mobile right5">
							<i class="fa fa-sign-out" aria-hidden="true"></i> 登出
						</a>
					</li>
				</s:if>
				<li style="border-bottom:#DDD 1px solid" role="presentation" >
					<a href="<s:url value="/f2/videosArea/init"/>" class="menu_link_mobile right5">
						影音專區
					</a>
				</li>
				<li style="border-bottom:#DDD 1px solid" role="presentation" class="dropdown">
					<a href="#" class="dropdown-toggle menu_link_mobile right5" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
						活動/人培<span class="caret"></span>
					</a> 
					<ul class="dropdown-menu" style="margin-left:20px; padding-left:10px;">
						<s:iterator value="@iace.entity.activity.Activity@getCategoryList()" status="stat">
							<li>
								<s:url value="/f2/activity/init" var="urlTag" escapeAmp="false">
									<s:param name="searchCondition.category" value="code" />
								</s:url>
								<a href="<s:property value="urlTag"/>" class="menu_link_mobile"><s:property value="name"/></a>
							</li>
						</s:iterator>
					</ul>
				</li>
				<li style="border-bottom:#DDD 1px solid" role="presentation" class="dropdown">
					<a href="#" class="dropdown-toggle menu_link_mobile right5" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
						公告訊息<span class="caret"></span>
					</a> 
					<ul class="dropdown-menu" style="margin-left:20px; padding-left:10px;">
						<s:iterator value="@iace.entity.news.News@getCategoryList()" status="stat">
							<li>
								<s:url value="/f2/news/init" var="urlTag" escapeAmp="false">
									<s:param name="searchCondition.category" value="code" />
								</s:url>
								<a href="<s:property value="urlTag"/>" class="menu_link_mobile"><s:property value="name"/></a>
							</li>
						</s:iterator>
					</ul>
				</li>
				<li style="border-bottom:#DDD 1px solid" role="presentation" class="dropdown">
					<a href="#" class="dropdown-toggle menu_link_mobile right5" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
						產學合作案例<span class="caret"></span>
					</a> 
					<ul class="dropdown-menu" style="margin-left:20px; padding-left:10px;">
						<s:iterator value="@iace.entity.coopExample.CoopEx@getTypeList()" status="stat">
							<li>
								<s:url value="/f2/coopEx/init" var="urlTag" escapeAmp="false">
									<s:param name="searchCondition.type" value="code" />
								</s:url>
								<a href="<s:property value="urlTag"/>" class="menu_link_mobile"><s:property value="name"/></a>
							</li>
						</s:iterator>
					</ul>
				</li>
				<li style="border-bottom:#DDD 1px solid" role="presentation" class="dropdown">
					<a href="#" class="dropdown-toggle menu_link_mobile right5" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
						學界研發成果<span class="caret"></span>
					</a> 
					<ul class="dropdown-menu" style="margin-left:20px; padding-left:10px;">
						<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.researchPlan.ResearchPlan"/>" class="menu_link_mobile">學研成果</a></li>
						<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.patent.Patent"/>" class="menu_link_mobile">學界專利</a></li>
						<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.talentedPeople.TalentedPeople"/>" class="menu_link_mobile">產學人才</a></li>
					</ul>
				</li>
				<li style="border-bottom:#DDD 1px solid" role="presentation" class="dropdown">
					<a href="#" class="dropdown-toggle menu_link_mobile right5" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
						研發焦點<span class="caret"></span>
					</a>
					<ul class="dropdown-menu" style="margin-left:20px; padding-left:10px;">
						<s:iterator value="@iace.entity.rdFocus.RdFocus@getCategoryList()" status="stat">
							<li>
								<s:url value="/f2/rdFocus/init" var="urlTag" escapeAmp="false">
									<s:param name="searchCondition.category" value="code" />
								</s:url>
								<a href="<s:property value="urlTag"/>" class="menu_link_mobile"><s:property value="name"/></a>
							</li>
						</s:iterator>
					</ul>
				</li>
				<li style="border-bottom:#DDD 1px solid" role="presentation" class="dropdown">
					<a href="#" class="dropdown-toggle menu_link_mobile right5" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
						產業情報<span class="caret"></span>
					</a> 
					<ul class="dropdown-menu" style="margin-left:20px; padding-left:10px;">
						<s:iterator value="@iace.entity.industryInfo.IndustryInfo@getCategoryList()" status="stat">
							<li>
								<s:url value="/f2/industryInfo/init" var="urlTag" escapeAmp="false">
									<s:param name="searchCondition.category" value="code" />
								</s:url>
								<a href="<s:property value="urlTag"/>" class="menu_link_mobile"><s:property value="name"/></a>
							</li>
						</s:iterator>
						<li>
							<s:url value="/f2/enterpriseRequireTech/init"  var="urlTag" escapeAmp="false">
							</s:url>
							<a href="<s:property value="urlTag"/>" class="menu_link_mobile">企業需求</a>
						</li>
					</ul>
				</li>
				<li style="border-bottom:#DDD 1px solid" role="presentation" class="dropdown">
					<a href="#" class="dropdown-toggle menu_link_mobile right5" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
						媒合專區<span class="caret"></span>
					</a> 
					<ul class="dropdown-menu" style="margin-left:20px; padding-left:10px;">
						<li><a href="<s:url value="/f2/matchIntro/init"/>" class="menu_link_mobile">產學媒合服務團簡介</a></li>
						<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.literature.Literature"/>" class="menu_link_mobile">法規政策/文獻</a></li>
						<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.incubationCenter.IncubationCenter"/>" class="menu_link_mobile">育成中心</a></li>
						<li><a href="#" class="menu_link_mobile">問卷調查</a></li>
					</ul>
				</li>
			</ul>
			<!-- Blue menu 手機版 -->
			<div style="background-color:#1fb5da; box-shadow:5px 5px 12px -1px rgba(20%,20%,40%,0.5);">
				<div class="container">
					<div class="row">
						<div class="col-xs-6 text-center" style="padding:15px; border-bottom:1px solid #FFF; border-right:1px solid #FFF;">
							<a href="<s:url value="/index"/>" class="blue_menu_link">
								<i class="fa fa-home right5" aria-hidden="true"></i>首頁
							</a>
						</div>
						<div class="col-xs-6 text-center" style="padding:15px; border-bottom:1px solid #FFF;">
							<a href="<s:url value="/f2/about/init"/>" class="blue_menu_link">
								<i class="fa fa-pencil right5" aria-hidden="true"></i>產學合作計畫
							</a>
						</div>
						<div class="col-xs-6 text-center" style="padding:15px; border-bottom:1px solid #FFF; border-right:1px solid #FFF;">
							<a href="<s:url value="/f2/consulting/create"/>" class="blue_menu_link">
								<i class="fa fa-phone right5" aria-hidden="true"></i>我要諮詢
							</a>
						</div>
						<div class="col-xs-6 text-center" style="padding:15px; border-bottom:1px solid #FFF;">
							<s:url value="/f2/faq/init" var="urlTag" escapeAmp="false">
								<s:param name="searchCondition.category" value="@iace.entity.faq.Faq@getCategoryList()[0].code" />
							</s:url>
							<a href="<s:property value="%{#urlTag}"/>" class="blue_menu_link">
								<i class="fa fa-book right5" aria-hidden="true"></i>常問集
							</a>
						</div>
						<div class="col-xs-6 text-center" style="padding:15px; border-bottom:1px solid #FFF; border-right:1px solid #FFF;">
							<a href="<s:url value="/f2/member/memberCenter"/>" class="blue_menu_link">
								<i class="fa fa-user-circle-o right5" aria-hidden="true"></i>會員中心
							</a>
						</div>
						<div class="col-xs-6 text-center" style="padding:15px; border-bottom:1px solid #FFF;">
							<a href="<s:url value="/f2/ePaper/init"/>" class="blue_menu_link">
								<i class="fa fa-envelope-open right5" aria-hidden="true"></i>電子報
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- /.navbar-collapse --> 
	</div>
</nav>