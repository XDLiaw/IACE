<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags"%>

<!-- 學界研發成果 -->
<div style="background-image:url('<s:url value="/images/frontend-v2/banner_index_06.jpg"/>'); background-size:cover; padding-bottom:30px;">
	<div class="container">
		<div class="row">
			<div class="col-sm-6 col-xs-12 top50">
				<div>
					<div class="large_title_02">
						<i class="fa fa-lightbulb-o" aria-hidden="true" style="font-size: 20px; margin-right: 10px;"></i>學界研發成果
						<div class="pull-right">
							<a href="<s:url value="/f2/integrationSearch/init?searchCondition.className=iace.entity.researchPlan.ResearchPlan"/>">
								<img src="<s:url value="/images/frontend-v2/more_blue.png"/>" alt="" height="30" />
							</a>
						</div>
					</div>
					<div class="line_blue">&nbsp;</div>
					<div class="line_gray1px"></div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6 col-xs-12">
				<div class="top10">
					<table class="table" style="table-layout:fixed;">
						<tbody>
							<!-- 研發成果 -->
							<s:iterator value="technologyList" status="stat">
								<tr>
									<s:url value="/f2/researchPlan/showDetail" var="detailUrlTag" escapeAmp="false">
										<s:param name="id" value="researchPlan.id" />
									</s:url>
									<td style="border:none;" >
										<div class="single-line-truncate">
											<a href="<s:property value="%{#detailUrlTag}"/>" class="list_link_02" target="_blank">
												<span class="date_02"><s:property value="researchPlan.planNo"/></span>&nbsp;&nbsp;&nbsp;
												<s:property value="name"/>
											</a>
										</div>
									</td>
								</tr>
							</s:iterator>
							<!-- 專利 -->
							<s:iterator value="patentList" status="stat">
								<tr>
									<s:url value="/f2/patent/showDetail" var="detailUrlTag" escapeAmp="false">
										<s:param name="id" value="id" />
									</s:url>
									<td style="border:none;">
										<div class="single-line-truncate">
											<a href="<s:property value="%{#detailUrlTag}"/>" class="list_link_02" target="_blank">
												<span class="date_02"><s:property value="openNo"/></span>&nbsp;&nbsp;&nbsp;
												<s:property value="name"/>
											</a>
										</div>
									</td>
								</tr>
							</s:iterator>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>