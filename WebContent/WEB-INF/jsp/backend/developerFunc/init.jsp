<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	$(document).ready(function() {

	});
</script>
<meta name="funcPathText" content=""/>
</head>
<body>
	<div>
		<a href="<s:url value="/developerFunc/batchResizeTalentedPeopleHeadshot"/>">批次將產學人才大頭照做縮圖處理</a>
		<label>(p.s.由於資料較多，可能需要花費數分鐘到數十分鐘的時間，請勿重複點擊)</label>
	</div>
	<div class="clear"></div>
	<div>
		<a href="<s:url value="/developerFunc/rebuildLuceneIndex"/>">重建索引</a>
		<label>(p.s.由於資料較多，可能需要花費數分鐘到數十分鐘的時間，請勿重複點擊)</label>
	</div>
	<div class="clear"></div>
	<div>
		<a href="<s:url value="/developerFunc/syncIndustryInfoData"/>">同步產業情報資料</a>
		<label>(p.s.由於資料較多，可能需要花費數分鐘到數十分鐘的時間，請勿重複點擊)</label>
	</div>
	<div>
		<a href="<s:url value="/developerFunc/reflashMonthlyPopularActivity"/>">ReflashHot20</a>
	</div>	
</body>
</html>