<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>

<!-- 網頁編輯器 -->
<script type="text/javascript" src="<s:url value="/scripts/tinymce/tinymce.min.js"/>"></script>
<script type="text/javascript" src="<s:url value="/scripts/tinymce/jquery.tinymce.min.js"/>"></script>
<script type="text/javascript" src="<s:url value="/scripts/tinymce/defaultEditorSetting.js"/>"></script>
<script>
	readonlyTinymceEditor('textArea[name="activity.content"]');
</script>

<script>
	$(document).ready(function(){
		addSearchConditionHiddenToForm();
		$("#btn-back").click(function(){				
			$("#form-backToIndex").submit();
		});
	});
</script>
<script>
	function addSearchConditionHiddenToForm() {
		$("#form-backToIndex input[type=hidden]").each(function(index){
			$("#form-delete").append($(this).clone());
		});
	}
</script>
<style>
textarea[disabled] { width:100%; resize:none; border:none; background-color:#ffffff; color:#000000;}
</style>
<meta name="funcPathText" content="編輯管理  > 刪除"/>
</head>
<body>
	<s:form action="deleteSubmit" method="post" validate="true" id="form-delete">
		<s:hidden name="id" />
		<ul>						
			<li class="all">
				<b>標題</b>
				<div class="border-text">
					<s:property value="activity.title"/>
				</div>
			</li>
			<li class="half">
				<b>來源</b>
				<div class="border-text">
					<s:property value="activity.source"/>
				</div>
			</li>
			<li class="half">
				<b>發布日期</b>
				<div class="border-text">
					<s:date name="activity.postDate" format="yyyy/MM/dd"/>
				</div>
			</li>
			<div class="clear"></div>
			<li class="half">
				<b>分類</b>
				<div class="border-text">
					<s:property value="activity.category"/>
				</div>
			</li>
			<li class="half">
				<b>活動日期</b>
				<s:textfield name="activity.actDate" maxlength="200"/>			
			</li>
			<div class="clear"></div>
			<li class="all">
				<b>活動地點</b>
				<s:textfield name="activity.actAddress" maxlength="200"/>
			</li>			
			<li class="all">
				<b>主辦單位</b>
				<s:textfield name="activity.organizer" maxlength="200"/>
			</li>
			<li class="all">
				<b>指導單位</b>
				<s:textfield name="activity.advisor" maxlength="200"/>
			</li>			
			<li class="all">
				<b>聯絡窗口</b>
				<s:textfield name="activity.contact" maxlength="200"/>
			</li>
			<li class="half">
				<b>報名人數</b>
				<s:textfield name="activity.signUpNum" maxlength="200"/>
			</li>
			<li class="half">
				<b>報名起迄</b>
				<s:textfield name="activity.signUpPeriod" maxlength="200"/>			
			</li>
			<li class="all">
				<b>報名網址</b>
				<s:textfield name="activity.signUpLink" maxlength="200"/>
			</li>
			<li class="all">
				<b>內容</b>
				<s:textarea name="activity.content"/>	
			</li>		
		</ul>
		<div class="clear"></div>
		
		<!-- 附檔 -->
		<table id="table-attach" class="table-files">
			<thead>
				<tr><th colspan="3">附檔</th></tr>
			</thead>
			<tbody>
				<s:iterator value="activity.attachList" status="stat">
					<tr>						
						<td>						
							<label class="label-fileName">
								<s:url namespace="/file" action="downloadFile" escapeAmp="false" var="downloadAttachUrl">
									<s:param name="folderConfigKey" value="%{'activityAttachFolder'}" />
									<s:param name="downloadFileSubPath" value="fileSubPath" />
								</s:url>
								<a href="<s:property value="downloadAttachUrl" />" >
									<s:property value="uploadFileName"/>
								</a>
							</label>
						</td>
						<td>
							<s:select class="fileType" name="%{'activity.attachList['+#stat.index+'].fileType'}" list="fileTypeList" listKey="code" listValue="name" disabled="true"/>
						</td>
						<td>
							<s:property value="fileTitle"/>						
						</td>						
					</tr>
				</s:iterator>
			</tbody>
		</table>	
		<div class="clear"></div>
		
		<!-- 影片 -->
		<table id="table-video" class="">
			<thead>
				<tr>
					<th width="3%">No.</th>
					<th>活動影片名稱</th>
					<th>影片</th>
					<th>瀏覽次數</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="activity.videoList" status="stat">
					<tr>
						<td class="td-No"><s:property value="%{#stat.index+1}" /></td>
						<td class="td-title"><s:property value="title"/></td>
						<td class="td-videoUrl"><s:property value="videoUrl" escapeHtml="false"/></td>
						<td class="td-clickNum"><s:property value="clickNum"/></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
		<div class="clear"></div>
			
		<h2>SEO區(網站優化加註，幫助訊息更易搜尋到)</h2>
		<ul class="common-meta-fields">
			<li class="all">
				<b>Meta Title</b>
				<div class="border-text">
					<s:property value="activity.metaTitle"/>
				</div>				
			</li>
			<li class="all">
				<b>Meta Description</b>
				<div class="border-text">
					<s:property value="activity.metaDes"/>
				</div>				
			</li>
			<li class="all">
				<b>Meta KeyWord</b>
				<div class="border-text">
					<s:property value="activity.metaKeyword"/>
				</div>				
			</li>
		</ul>
		<div class="clear"></div>
		
		<h2>狀態設定區</h2>
		<ul class="common-linkiac-fields">
			<li class="quarter">
				<b>瀏覽次數</b>
				<div class="border-text">
					<s:property value="activity.clickNum"/>
				</div>				
			</li>
			<li class="quarter">
				<b>排序(數字愈大排愈前面)</b>
				<div class="border-text">
					<s:property value="activity.sort"/>
				</div>				
			</li>
			<li class="quarter">
				<b>顯示狀態</b>
				<s:radio name="activity.displayStatus" list="#{'true':'開啟', 'false':'關閉'}" class="horizontalList" disabled="true"/>
			</li>
			<li class="quarter">
				<b>首頁顯示</b>
				<s:radio name="activity.homeDisplayStatus" list="#{'true':'開啟', 'false':'關閉'}" class="horizontalList" disabled="true"/>
			</li>			
		</ul>		
		<div class="clear"></div>	
		
		<div class="bottom-btn-block">
			<s:submit cssClass="btn btn-info redBtn" value="確定" />
			<input type="button" class="grayBtn" id="btn-back" value="回列表頁"/>
		</div>
	</s:form>
				
	<s:include value="./form-backToIndex.jsp" />
</body>
</html>