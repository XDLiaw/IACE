<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags" %>

<div class="footer_blue">
	<div class="container">
		<div class="row">
			<div class="col-sm-2 col-xs-6">
				<div class="small_title_02">活動人培</div>
				<ul class="small_title_02">
					<s:iterator value="@iace.entity.activity.Activity@getCategoryList()" status="stat">
						<li>
							<s:url value="/f2/activity/init" var="urlTag" escapeAmp="false">
								<s:param name="searchCondition.category" value="code" />
							</s:url>
							<a href="<s:property value="urlTag"/>" class="list_link_04"><s:property value="name"/></a>
						</li>
					</s:iterator>
				</ul>
			</div>
			<div class="col-sm-2 col-xs-6">
				<div class="small_title_02">公告訊息</div>
				<ul class="small_title_02">
					<s:iterator value="@iace.entity.news.News@getCategoryList()" status="stat">
						<li>
							<s:url value="/f2/news/init" var="urlTag" escapeAmp="false">
								<s:param name="searchCondition.category" value="code" />
							</s:url>
							<a href="<s:property value="urlTag"/>" class="list_link_04"><s:property value="name"/></a>
						</li>
					</s:iterator>
				</ul>
			</div>
			<div class="col-sm-2 col-xs-6">
				<div class="small_title_02">產學合作案例</div>
				<ul class="small_title_02">
					<s:iterator value="@iace.entity.coopExample.CoopEx@getTypeList()" status="stat">
						<li>
							<s:url value="/f2/coopEx/init" var="urlTag" escapeAmp="false">
								<s:param name="searchCondition.type" value="code" />
							</s:url>
							<a href="<s:property value="urlTag"/>" class="list_link_04"><s:property value="name"/></a>
						</li>
					</s:iterator>
				</ul>
			</div>
			<div class="col-sm-2 col-xs-6">
				<div class="small_title_02">學界研發成果</div>
				<ul class="small_title_02">
					<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.researchPlan.ResearchPlan"/>" class="list_link_04">學研成果</a></li>
					<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.patent.Patent"/>" class="list_link_04">學界專利</a></li>
					<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.talentedPeople.TalentedPeople"/>" class="list_link_04">產學人才</a></li>
				</ul>
			</div>
			<div class="col-sm-2 col-xs-6">
				<div class="small_title_02">產業情報</div>
				<ul class="small_title_02">
					<s:iterator value="@iace.entity.industryInfo.IndustryInfo@getCategoryList()" status="stat">
						<li>
							<s:url value="/f2/industryInfo/init" var="urlTag" escapeAmp="false">
								<s:param name="searchCondition.category" value="code" />
							</s:url>
							<a href="<s:property value="urlTag"/>" class="list_link_04"><s:property value="name"/></a>
						</li>
					</s:iterator>
					<li>
						<s:url value="/f2/enterpriseRequireTech/init"  var="urlTag" escapeAmp="false">
						</s:url>
						<a href="<s:property value="urlTag"/>" class="list_link_04">企業需求</a>
					</li>					
				</ul>
			</div>
			<div class="col-sm-2 col-xs-6">
				<div class="small_title_02">媒合專區</div>
				<ul class="small_title_02">
					<li><a href="<s:url value="/f2/matchIntro/init"/>" class="list_link_04">產學媒合服務團簡介</a></li>
					<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.literature.Literature"/>" class="list_link_04">法規政策/文獻</a></li>
					<li><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.incubationCenter.IncubationCenter"/>" class="list_link_04">育成中心</a></li>
					<li><a href="#" class="list_link_04">問卷調查</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>