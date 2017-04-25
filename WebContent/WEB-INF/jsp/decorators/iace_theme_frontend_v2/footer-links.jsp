<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags"%>

<div class="footer_grey">
	<div class="container">
		<div class="row">
			<div class="col-sm-2 col-xs-6 text-center">
				<div style="margin: 12px 0;">
					<img src="<s:url value="/images/frontend-v2/footer_icon_01.png"/>" alt="" height="55" />
					<a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.researchPlan.ResearchPlan"/>" class="list_link_03">
					找技術
					</a>
				</div>
			</div>
			<div class="col-sm-2 col-xs-6 text-center">
				<div style="margin: 12px 0;">
					<img src="<s:url value="/images/frontend-v2/footer_icon_02.png"/>" alt="" height="55" />
					<s:url value="/f2/activity/init" var="urlTag" escapeAmp="false">
						<s:param name="searchCondition.category" value="%{'外部活動'}" />
					</s:url>	
					<a href="<s:property value="urlTag"/>" class="list_link_03">找活動</a>
				</div>
			</div>
			<div class="col-sm-2 col-xs-6 text-center">
				<div style="margin: 12px 0;">
					<img src="<s:url value="/images/frontend-v2/footer_icon_03.png"/>" alt="" height="55" />
					<s:url value="/f2/activity/init" var="urlTag" escapeAmp="false">
						<s:param name="searchCondition.category" value="%{'人培課程'}" />
					</s:url>
					<a href="<s:property value="urlTag"/>" class="list_link_03">找課程</a>
				</div>
			</div>
			<div class="col-sm-2 col-xs-6 text-center">
				<div style="margin: 12px 0;">
					<img src="<s:url value="/images/frontend-v2/footer_icon_04.png"/>" alt="" height="55" /><a href="<s:url value="/f2/consulting/create"/>" class="list_link_03">要諮詢</a>
				</div>
			</div>
			<div class="col-sm-2 col-xs-6 text-center">
				<div style="margin: 12px 0;">
					<img src="<s:url value="/images/frontend-v2/footer_icon_05.png"/>" alt="" height="55" /><a href="<s:url value="/f2/member/memberCenter"/>" class="list_link_03">會員</a>
				</div>
			</div>
			<div class="col-sm-2 col-xs-6 text-center">
				<s:url forceAddSchemeHostAndPort="true" includeParams="all" var="currentUrl" escapeAmp="false" />
				<div>
					<a href="javascript: void(window.open('http://www.facebook.com/share.php?u='.concat(encodeURIComponent(location.href)) ));" target="_blank"> <img
						src="<s:url value="/images/frontend-v2/icon_facebook.png"/>" alt="" height="40" />
					</a> &nbsp; <a href="javascript: void(window.open('http://twitter.com/home/?status='.concat(encodeURIComponent(document.title)) .concat(' ') .concat(encodeURIComponent(location.href))));"
						target="_blank"> <img src="<s:url value="/images/frontend-v2/icon_twitter.png"/>" alt="" height="40" />
					</a>
				</div>
				<div class="top5">
					<a href="javascript: void(window.open('https://plus.google.com/share?url='.concat(encodeURIComponent(location.href)) ));" target="_blank"> <img
						src="<s:url value="/images/frontend-v2/icon_google.png"/>" alt="" height="40" />
					</a> &nbsp; <a href="javascript: void(window.open('http://www.plurk.com/?qualifier=shares&status='.concat(encodeURIComponent(location.href)) ));" target="_blank"> <img
						src="<s:url value="/images/frontend-v2/icon_plurk_02.png"/>" alt="" height="40" />
					</a>
				</div>
			</div>
		</div>
	</div>
</div>