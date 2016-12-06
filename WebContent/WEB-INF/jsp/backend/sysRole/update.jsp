<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<script>
	$(document).ready(function(){
		$("#select-all").change(function(){
			if ($(this).is(':checked')) {
				$("input[type=checkbox]").prop('checked', true);
			}
			else {
				$("input[type=checkbox]").prop('checked', false);
			}
		});		
	});
</script>
</head>
<body>
	<h2 class="itemTitle">編輯</h2>
	<s:form action="updateSubmit" method="post" validate="true" >
		<s:hidden name="id"/>
		<s:hidden name="sysRole.id"/>
		<s:hidden name="sysRole.isValid"/>
		<s:hidden name="sysRole.createTime"/>
		<s:hidden name="sysRole.createUser"/>
		<s:hidden name="sysRole.updateTime"/>
		<s:hidden name="sysRole.updateUser"/>		
		<s:hidden name="sysRole.ver"/>		
		<ul>
			<li class="all">
				<b>腳色名稱</b>
				<s:textfield name="sysRole.name"/>
			</li>
		</ul>
		<div class="clear"></div>
		
		<table>
			<tr>
				<th width="15%">權限設定</th>
				<th></th>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.qnrCoopWay.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.qnrCoopWay.opView.displayName}" name="sysRole.sysAuth.qnrCoopWay.opView.auth" fieldValue="true"/>
				</td>	
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.researchPlan.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.researchPlan.opView.displayName}" name="sysRole.sysAuth.researchPlan.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.researchPlan.opCreate.displayName}" name="sysRole.sysAuth.researchPlan.opCreate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.researchPlan.opUpdate.displayName}" name="sysRole.sysAuth.researchPlan.opUpdate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.researchPlan.opCreateTechnology.displayName}" name="sysRole.sysAuth.researchPlan.opCreateTechnology.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.researchPlan.opUpdateTechnology.displayName}" name="sysRole.sysAuth.researchPlan.opUpdateTechnology.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.researchPlan.opDeleteTechnology.displayName}" name="sysRole.sysAuth.researchPlan.opDeleteTechnology.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.researchPlan.opBatchImport.displayName}" name="sysRole.sysAuth.researchPlan.opBatchImport.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.patent.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.patent.opView.displayName}" name="sysRole.sysAuth.patent.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.patent.opCreate.displayName}" name="sysRole.sysAuth.patent.opCreate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.patent.opUpdate.displayName}" name="sysRole.sysAuth.patent.opUpdate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.patent.opDelete.displayName}" name="sysRole.sysAuth.patent.opDelete.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.patent.opBatchImport.displayName}" name="sysRole.sysAuth.patent.opBatchImport.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.consulting.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.consulting.opView.displayName}" name="sysRole.sysAuth.consulting.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.consulting.opCreate.displayName}" name="sysRole.sysAuth.consulting.opCreate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.consulting.opUpdate.displayName}" name="sysRole.sysAuth.consulting.opUpdate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.consulting.opDelete.displayName}" name="sysRole.sysAuth.consulting.opDelete.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.enterpriseNeed.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.enterpriseNeed.opView.displayName}" name="sysRole.sysAuth.enterpriseNeed.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.enterpriseNeed.opCreate.displayName}" name="sysRole.sysAuth.enterpriseNeed.opCreate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.enterpriseNeed.opUpdate.displayName}" name="sysRole.sysAuth.enterpriseNeed.opUpdate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.enterpriseNeed.opDelete.displayName}" name="sysRole.sysAuth.enterpriseNeed.opDelete.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.coopEx.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.coopEx.opView.displayName}" name="sysRole.sysAuth.coopEx.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.coopEx.opCreate.displayName}" name="sysRole.sysAuth.coopEx.opCreate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.coopEx.opUpdate.displayName}" name="sysRole.sysAuth.coopEx.opUpdate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.coopEx.opDelete.displayName}" name="sysRole.sysAuth.coopEx.opDelete.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.talentedPeople.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.talentedPeople.opView.displayName}" name="sysRole.sysAuth.talentedPeople.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.talentedPeople.opUpdate.displayName}" name="sysRole.sysAuth.talentedPeople.opUpdate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.talentedPeople.opDelete.displayName}" name="sysRole.sysAuth.talentedPeople.opDelete.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.talentedPeople.opBatchImport.displayName}" name="sysRole.sysAuth.talentedPeople.opBatchImport.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.talentedPeople.opExportEmail.displayName}" name="sysRole.sysAuth.talentedPeople.opExportEmail.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.incubationCenter.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.incubationCenter.opView.displayName}" name="sysRole.sysAuth.incubationCenter.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.incubationCenter.opBatchImport.displayName}" name="sysRole.sysAuth.incubationCenter.opBatchImport.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.literature.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.literature.opBatchImport.displayName}" name="sysRole.sysAuth.literature.opBatchImport.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.optionManage.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.optionManage.opView.displayName}" name="sysRole.sysAuth.optionManage.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.optionManage.opCreate.displayName}" name="sysRole.sysAuth.optionManage.opCreate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.optionManage.opUpdate.displayName}" name="sysRole.sysAuth.optionManage.opUpdate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.optionManage.opDelete.displayName}" name="sysRole.sysAuth.optionManage.opDelete.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.optionManage.opBatchImport.displayName}" name="sysRole.sysAuth.optionManage.opBatchImport.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.sysLog.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.sysLog.opView.displayName}" name="sysRole.sysAuth.sysLog.opView.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.sysUser.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.sysUser.opView.displayName}" name="sysRole.sysAuth.sysUser.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.sysUser.opCreate.displayName}" name="sysRole.sysAuth.sysUser.opCreate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.sysUser.opUpdate.displayName}" name="sysRole.sysAuth.sysUser.opUpdate.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.sysRole.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.sysRole.opView.displayName}" name="sysRole.sysAuth.sysRole.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.sysRole.opCreate.displayName}" name="sysRole.sysAuth.sysRole.opCreate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.sysRole.opUpdate.displayName}" name="sysRole.sysAuth.sysRole.opUpdate.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.about.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.about.opView.displayName}" name="sysRole.sysAuth.about.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.about.opCreate.displayName}" name="sysRole.sysAuth.about.opCreate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.about.opUpdate.displayName}" name="sysRole.sysAuth.about.opUpdate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.about.opDelete.displayName}" name="sysRole.sysAuth.about.opDelete.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.faq.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.faq.opView.displayName}" name="sysRole.sysAuth.faq.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.faq.opCreate.displayName}" name="sysRole.sysAuth.faq.opCreate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.faq.opUpdate.displayName}" name="sysRole.sysAuth.faq.opUpdate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.faq.opDelete.displayName}" name="sysRole.sysAuth.faq.opDelete.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.news.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.news.opView.displayName}" name="sysRole.sysAuth.news.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.news.opCreate.displayName}" name="sysRole.sysAuth.news.opCreate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.news.opUpdate.displayName}" name="sysRole.sysAuth.news.opUpdate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.news.opDelete.displayName}" name="sysRole.sysAuth.news.opDelete.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.activity.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.activity.opView.displayName}" name="sysRole.sysAuth.activity.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.activity.opCreate.displayName}" name="sysRole.sysAuth.activity.opCreate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.activity.opUpdate.displayName}" name="sysRole.sysAuth.activity.opUpdate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.activity.opDelete.displayName}" name="sysRole.sysAuth.activity.opDelete.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.member.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.member.opView.displayName}" name="sysRole.sysAuth.member.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.member.opCreate.displayName}" name="sysRole.sysAuth.member.opCreate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.member.opUpdate.displayName}" name="sysRole.sysAuth.member.opUpdate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.member.opDelete.displayName}" name="sysRole.sysAuth.member.opDelete.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.contactUs.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.contactUs.opView.displayName}" name="sysRole.sysAuth.contactUs.opView.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.contactUs.opCreate.displayName}" name="sysRole.sysAuth.contactUs.opCreate.auth" fieldValue="true"/>
					<s:checkbox label="%{sysRole.sysAuth.contactUs.opUpdate.displayName}" name="sysRole.sysAuth.contactUs.opUpdate.auth" fieldValue="true"/>
				</td>
			</tr>
			<tr>
				<td><b><s:property value="sysRole.sysAuth.batchSendEmail.displayName"/></b></td>
				<td class="horizontalList">
					<s:checkbox label="%{sysRole.sysAuth.batchSendEmail.opBatchSendEmail.displayName}" name="sysRole.sysAuth.batchSendEmail.opBatchSendEmail.auth" fieldValue="true"/>
				</td>
			</tr>
		</table>
		
		<div class="clear"></div>
		<s:submit cssClass="btn btn-default redBtn" value="儲存" />
		<input type="button" class="grayBtn" value="回上一頁" onclick="window.location.href='<s:url value="index"/>'" />
	</s:form>
</body>
</html>