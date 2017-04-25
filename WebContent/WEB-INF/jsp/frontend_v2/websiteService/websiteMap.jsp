<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<html>
<head>
<script type="text/javascript">
	$(document).ready(function() {
		$('ul.categoryList li:contains("網站地圖")').addClass("active");
	});
</script>
</head>
<body>
	<!-- Banner -->
	<s:include value="./banner.jsp" />

	<!-- Main -->
	<div class="container top50">
		<div class="row">
			<div class="col-sm-12 col-xs-12">
				<div>
					<div class="large_title_01">
						<i class="fa fa-calendar-check-o" aria-hidden="true" style="font-size:18px; margin-right:5px;"></i>網站服務
					</div>
					<div class="line_blue">&nbsp;</div>
					<div class="line_gray1px bottom20"></div>
				</div>
			</div>
		</div>	
		<div class="row">
			<!-- 左選單 -->
			<div class="col-sm-3 col-xs-12 bottom20">
				<s:include value="./left_menu.jsp" />
			</div>
			<!-- 右列表 -->
			<div class="col-sm-9 col-xs-12 bottom20">
				<div class="large_title_03">網站地圖</div>
				<div class="content_01 top10">
					<div class="row">
						<div class="col-sm-3 col-xs-12 bottom20">
							<table>
								<tr><th>影音專區</th></tr>
								<tr><td><a href="<s:url value="/f2/videosArea/init"/>">影音專區</a></td></tr>
							</table>
						</div>
						<div class="col-sm-3 col-xs-12 bottom20">
							<table>
								<tr><th>活動/人培</th></tr>
								<s:iterator value="@iace.entity.activity.Activity@getCategoryList()" status="stat">
									<tr>
										<td>
											<s:url value="/f2/activity/init" var="urlTag" escapeAmp="false">
												<s:param name="searchCondition.category" value="code" />
											</s:url>
											<a href="<s:property value="urlTag"/>"><s:property value="name"/></a>
										</td>
									</tr>
								</s:iterator>
							</table>
						</div>
						<div class="col-sm-3 col-xs-12 bottom20">
							<table>
								<tr><th>公告訊息</th></tr>
								<s:iterator value="@iace.entity.news.News@getCategoryList()" status="stat">
									<tr>
										<td>
											<s:url value="/f2/news/init" var="urlTag" escapeAmp="false">
												<s:param name="searchCondition.category" value="code" />
											</s:url>
											<a href="<s:property value="urlTag"/>"><s:property value="name"/></a>	
										</td>
									</tr>
								</s:iterator>
							</table>
						</div>
						<div class="col-sm-3 col-xs-12 bottom20">
							<table>
								<tr><th>產學合作案例</th></tr>
								<s:iterator value="@iace.entity.coopExample.CoopEx@getTypeList()" status="stat">
									<tr>
										<td>
											<s:url value="/f2/coopEx/init" var="urlTag" escapeAmp="false">
												<s:param name="searchCondition.type" value="code" />
											</s:url>
											<a href="<s:property value="urlTag"/>"><s:property value="name"/></a>
										</td>
									</tr>
								</s:iterator>
							</table>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-3 col-xs-12 bottom20">
							<table>
								<tr><th>學界研發成果</th></tr>
								<tr><td><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.researchPlan.ResearchPlan"/>">學研成果</a></td></tr>
								<tr><td><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.patent.Patent"/>">學界專利</a></td></tr>
								<tr><td><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.talentedPeople.TalentedPeople"/>">產學人才</a></td></tr>
							</table>
						</div>
						<div class="col-sm-3 col-xs-12 bottom20">
							<table>
								<tr><th>研發焦點</th></tr>
								<s:iterator value="@iace.entity.rdFocus.RdFocus@getCategoryList()" status="stat">
									<tr>
										<td>
											<s:url value="/f2/rdFocus/init" var="urlTag" escapeAmp="false">
												<s:param name="searchCondition.category" value="code" />
											</s:url>
											<a href="<s:property value="urlTag"/>"><s:property value="name"/></a>
										</td>
									</tr>
								</s:iterator>
							</table>
						</div>		
						<div class="col-sm-3 col-xs-12 bottom20">
							<table>
								<tr><th>產業情報</th></tr>
								<s:iterator value="@iace.entity.industryInfo.IndustryInfo@getCategoryList()" status="stat">
									<tr>
										<td>
											<s:url value="/f2/industryInfo/init" var="urlTag" escapeAmp="false">
												<s:param name="searchCondition.category" value="code" />
											</s:url>
											<a href="<s:property value="urlTag"/>"><s:property value="name"/></a>
										</td>
									</tr>
								</s:iterator>
								<tr>
									<td>
										<s:url value="/f2/enterpriseRequireTech/init"  var="urlTag" escapeAmp="false">
										</s:url>
										<a href="<s:property value="urlTag"/>">企業需求</a>
									</td>
								</tr>
							</table>
						</div>
						<div class="col-sm-3 col-xs-12 bottom20">
							<table>
								<tr><th>媒合專區</th></tr>
								<tr><td><a href="<s:url value="/f2/matchIntro/init"/>">產學媒合服務團簡介</a></td></tr>
								<tr><td><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.literature.Literature"/>">法規政策/文獻</a></td></tr>
								<tr><td><a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.incubationCenter.IncubationCenter"/>">育成中心</a></td></tr>
								<tr><td><a href="#">問卷調查</a></td></tr>
							</table>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-3 col-xs-12 bottom20">
							<table>
								<tr><th>產學合作計畫</th></tr>
								<tr><td><a href="<s:url value="/f2/about/init"/>">產學合作計畫</a></td></tr>
							</table>
						</div>
						<div class="col-sm-3 col-xs-12 bottom20">
							<table>
								<tr><th>我要諮詢</th></tr>
								<tr><td><a href="<s:url value="/f2/consulting/create"/>">我要諮詢</a></td></tr>
							</table>
						</div>
						<div class="col-sm-3 col-xs-12 bottom20">
							<table>
								<tr><th>常問集</th></tr>
								<s:iterator value="@iace.entity.faq.Faq@getCategoryList()" status="stat">
									<tr>
										<td>
											<s:url value="/f2/faq/init" var="urlTag" escapeAmp="false">
												<s:param name="searchCondition.category" value="code" />
											</s:url>
											<a href="<s:property value="urlTag"/>"><s:property value="name"/></a>
										</td>
									</tr>
								</s:iterator>
							</table>
						</div>
						<div class="col-sm-3 col-xs-12 bottom20">
							<table>
								<tr><th>會員中心</th></tr>
								<s:if test="#session.member == null">
									<tr><td><a href="<s:url value="/f2/member/login"/>">會員登入</a></td></tr>
									<tr><td><a href="<s:url value="/f2/member/register"/>">加入會員</a></td></tr>
									<tr><td><a href="<s:url value="/f2/member/forgetPassword"/>">忘記密碼</a></td></tr>
								</s:if>
								<s:else>
									<tr><td><a href="<s:url value="/f2/member/memberCenter"/>">會員中心</a></td></tr>
									<tr><td><a href="<s:url value="/f2/member/logout"/>">會員登出</a></td></tr>
								</s:else>
							</table>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-3 col-xs-12 bottom20">
							<table>
								<tr><th>電子報</th></tr>
								<tr><td><a href="<s:url value="/f2/ePaper/init"/>">電子報歷史區</a></td></tr>
								<tr><td><a href="<s:url value="/f2/ePaper/subscribe"/>">電子報訂退閱</a></td></tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>	
	</div>
</body>
</html>