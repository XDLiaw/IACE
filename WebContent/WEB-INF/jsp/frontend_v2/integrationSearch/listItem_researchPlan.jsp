<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags"%>

<div class="col-sm-12 col-xs-12">
	<div class="well well-sm">
		<table class="table content_03" style="margin-bottom: 0;">
			<tbody>
				<tr>
					<td width="10%" style="border: none;" class="category"><h4>
						<span class="label label-info">研發成果</span>
					</h4></td>
					<td width="10%" style="border: none;" class="date_01">計畫名稱</td>
					<td style="border: none;">
						<s:url value="/f2/researchPlan/showDetail" var="detailUrlTag" escapeAmp="false">
							<s:param name="id" value="researchPlan.id" />
						</s:url>
						<a href="<s:property value="%{#detailUrlTag}"/>" class="list_link_01" target="_blank">
							<s:property value="researchPlan.name" />
						</a>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td class="date_01">主持人</td>
					<td><s:property value="researchPlan.manager" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td class="date_01">年度</td>
					<td><s:property value="researchPlan.year" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td class="date_01">研究領域</td>
					<td>
						<s:if test="researchPlan.grbDomains != null">
							<s:iterator value="researchPlan.grbDomains" status="stat">
								<s:property value="name" />&nbsp;&nbsp;&nbsp;
							</s:iterator>
						</s:if>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td class="date_01">產業化潛力</td>
					<td><s:property value="%{researchPlan.trl.name}" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td class="date_01">技術名稱</td>
					<td>
						<table class="table content_03" style="margin-bottom: 0;">
							<s:iterator value="researchPlan.technologies" status="tec_stat">
								<tr>
									<td width="3%"><s:property value="%{#tec_stat.count}" /></td>
									<td><s:property value="name"/></td>
								</tr>
							</s:iterator>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
