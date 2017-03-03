<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta name="funcPathText" content="編輯管理  > 編輯"/>

<script>
$(document).ready(function() {
	addSearchConditionHiddenToForm();
	$("#btn-back").click(function(){				
		$("#form-backToIndex").submit();
	});
});
</script>
<script>
	function addSearchConditionHiddenToForm() {
		$("#form-backToIndex input[type=hidden]").each(function(index){
			$("#form-update").append($(this).clone());
		});
	}
</script>
</head>
<body>
	<s:form action="updateSubmit" method="post" validate="true" id="form-update">
		<s:hidden name="about.id"/>
		<s:hidden name="about.isValid"/>
		<s:hidden name="about.createTime"/>
		<s:hidden name="about.createUser"/>
		<s:hidden name="about.updateTime"/>
		<s:hidden name="about.updateUser"/>
		<s:hidden name="about.ver"/>
		<ul>						
			<li class="all">
				<b>標題</b>
				<s:textfield name="about.title" maxlength="200"/>
			</li>
			<li class="all">
				<b>內容</b>
				<s:textarea name="about.content" />
				<div>
					<!-- 網頁編輯器 -->
					<s:include value="/WEB-INF/jsp/ckEditor.jsp" />
					<script type="text/javascript">
					window.onload = function() {
						CKEDITOR.replace('about.content'); // 此處參數 'about.content' 為需要套用ckeditor 的 textarea 的 name
					};
					</script>
				</div>
			</li>
		</ul>
		<div class="clear"></div>
			
		<h2>SEO區(網站優化加註，幫助訊息更易搜尋到)</h2>
		<ul class="common-meta-fields">
			<li class="all">
				<b>Meta Title</b>
				<s:textfield name="about.metaTitle" maxlength="200"/>
			</li>
			<li class="all">
				<b>Meta Description</b>
				<s:textfield name="about.metaDes" />
			</li>
			<li class="all">
				<b>Meta KeyWord</b>
				<s:textfield name="about.metaKeyword" />
			</li>
		</ul>
		<div class="clear"></div>
		
		<h2>狀態設定區</h2>
		<ul class="common-linkiac-fields">
			<li class="quarter">
				<b>瀏覽次數</b>
				<s:textfield name="about.clickNum" type="number" min="0"/>
			</li>
			<li class="quarter">
				<b>排序(數字愈大排愈前面)</b>
				<s:textfield name="about.sort" type="number"/>
			</li>
			<li class="quarter">
				<b>顯示狀態</b>
				<s:radio name="about.displayStatus" list="#{'true':'開啟', 'false':'關閉'}" class="horizontalList"/>
			</li>
			<li class="quarter">
				<b>首頁顯示</b>
				<s:radio name="about.homeDisplayStatus" list="#{'true':'開啟', 'false':'關閉'}" class="horizontalList"/>
			</li>			
		</ul>		
		<div class="clear"></div>
		
		<div style="width: 80%; text-align: center; margin: 20px auto 40px auto;">
			<s:submit cssClass="redBtn" value="儲存" />
			<input type="button" class="grayBtn" id="btn-back" value="回列表頁"/>
		</div>		
	</s:form>
	
	<s:include value="./form-backToIndex.jsp" />
</body>
</html>