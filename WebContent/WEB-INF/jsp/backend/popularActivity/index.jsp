<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	$(document).ready(function () {			
		paggingSetting();
		
		$(".btn-confirm").click(function(){
			$("form#form-index").attr('action', '<s:url value="updateSubmit.action"/>');
			$("form#form-index").submit();
			$("form#form-index").attr('action', '<s:url value="index.action"/>'); // 要把action改為原本的，否則如果使用者按下瀏覽器的上一頁回到目前這個列表頁再去按搜尋就會跑到已經被改變的action所指定的那一頁
		});
		
		$("input[type='checkbox'][name='activityIds']").change(function(){
			if (this.checked == false) {
				var value = $(this).val();
				$("#hidden-template-value input[type=hidden][name='activityIds'][value='"+value+"']").remove();
			}
		});
	});
</script>
<script>	
	function paggingSetting() {
		$("ul.pagination > li > input").addClass("btn btn-default btn-sm");
		
		var pageIndex = parseInt('<s:property value="activityPagedList.pageIndex"/>');
		var pageNumber = parseInt('<s:property value="activityPagedList.pageNumber"/>');
		var pageCount = parseInt('<s:property value="activityPagedList.pageCount"/>');
		
		$("ul > li > input.btn-page").click(function() {
			$("#pageIndex").val($(this).attr("value") - 1);
			return true;
		});
		$("ul.pagination > li > input.btn-page[value='"+pageNumber+"']").addClass("active");

		//第一頁按鈕
		$("ul > li > input.btn-first-page").click(function() {
			$("#pageIndex").val(0);
		});
		
		//最後一頁按鈕
		$("ul > li > input.btn-last-page").click(function() {
			$("#pageIndex").val(pageCount-1);
		});
		
		//上一頁按鈕
		if (pageIndex == 0) {
			$("ul > li > input.btn-previous-page").addClass("disabled");
		}
		$("ul > li > input.btn-previous-page").click(function() {
			if (pageIndex > 0) {
				$("#pageIndex").val(pageIndex - 1);
				return true;
			} else {
				return false;
			}
		});
		
		//下一頁按鈕
		if (pageIndex == pageCount - 1) {
			$("ul > li > input.btn-next-page").addClass("disabled");
		}
		$("ul > li > input.btn-next-page").click(function() {
			if (pageIndex < (pageCount - 1)) {
				$("#pageIndex").val(pageIndex + 1);
				return true;
			} else {
				return false;
			}
		});
		
		// 每頁筆數
		$(".select-pageSize").change(function() {
			var pageSize = $(this).find(":checked").val();
			$("#pageSize").val(pageSize);
			$("#pageIndex").val(0);
			$("form").submit();
		});
		$(".select-pageSize").val($("#pageSize").val());
					
		// 注意: 在此頁面的搜尋按鈕記得要加上id
	    $("#btn-search").click(function(){
	        $("#pageIndex").val(0);
	        return true;
	    });
	 	// 注意: 在此頁面的重置按鈕記得要加上id
		$("#btn-reset").click(function(){
			$("input.form-control:text").val("");
			$("select").prop('selectedIndex', 0);
		});
	}
</script>
<meta name="funcPathText" content="設定 > HOT20 查詢 "/>
</head>
<body>
	<s:form action="index" method="post" validate="true" id="form-index">
		<s:include value="./create_hidden.jsp" />
		
		<div class="subForm">
			<div class="">
				<ul class="row">
					<li class="quarter">
						<s:select name="searchCondition.category" list="@iace.entity.activity.Activity@getCategoryList()" listKey="code" listValue="name" headerKey="" headerValue="--分類--" />
					</li>
					<li class="half">
						<s:textfield placeholder="請輸入標題" name="searchCondition.searchText"/>
					</li>
					<li class="quarter">
						<input type="submit" value="查詢" class="btn btn-primary redBtn" id="btn-search"/>
						<input type="button" value="清除" class="btn btn-warning grayBtn" id="btn-reset"/>
					</li>
				</ul>
			</div>
			<div class="clear"></div>
	
			<div class="">
				<table class="table table-striped table-hover table-bordered">
					<thead>
						<tr>
							<th nowrap width="3%">選中</th>
							<th nowrap width="3%">No.</th>
							<th nowrap width="8%">發布日期</th>
							<th nowrap width="8%">分類</th>
							<th nowrap width="">標題</th>
<!-- 							<th nowrap width="7%">瀏覽次數</th> -->
						</tr>
					</thead>
					<tbody>
						<s:if test="activityPagedList != null">
							<s:iterator value="activityPagedList.list" status="stat">
								<tr>
									<td>
										<s:if test="activityIds.contains(id)">
											<input type="checkbox" name="activityIds" value="<s:property value="id"/>" checked="checked"/>
										</s:if>
										<s:else>
											<input type="checkbox" name="activityIds" value="<s:property value="id"/>"/>
										</s:else>
									</td>
									<td>
										<s:property value="%{activityPagedList.itemStart + #stat.count -1}" />
									</td>
									<td><s:date name="postDate" format="yyyy/MM/dd"/></td>
									<td><s:property value="category" /></td>	
									<td><s:property value="title" /></td>
<%-- 									<td><s:property value="clickNum" /></td> --%>
								</tr>
							</s:iterator>
						</s:if>	
					</tbody>
				</table>
			</div>
			
			<div class="page">
				<s:hidden id="pageIndex" name="searchCondition.pageIndex" />
				<s:hidden id="pageSize" name="searchCondition.pageSize" />
							
				<s:set var="pgList" value="activityPagedList"/>
				<s:set var="pgIndex" value="searchCondition.pageIndex"/>
				<s:set var="pgCount" value="#pgList.pageCount"/>
				
				<ul class="pagination">
					<s:if test="#pgList != null && #pgCount > 0">					
						<li><input type="submit" value="First" class="btn-first-page" /></li>
						<li><input type="submit" value=&laquo; class="btn-previous-page" /></li>
						<s:if test="#pgIndex >= 5">
							<li>......</li>
						</s:if>
						<s:iterator value="#pgList.pageNumberList" status="stat" 
							begin="%{#pgIndex < 5 ? 0 : #pgIndex - 5 }"
							end="%{#pgIndex > #pgCount - 6 ? #pgCount -1 : #pgIndex +5 }">
							<li><input type="submit" value=<s:property/> class="btn-page" /></li>
						</s:iterator>
						<s:if test="#pgIndex <= #pgCount - 6">
							<li>......</li>
						</s:if>
						<li><input type="submit" value=&raquo;	class="btn-next-page" /></li>
						<li class="next"><input type="submit" value="Last" class="btn-last-page" /></li>					
					</s:if>
					<li>
						<p>共 <s:property value="#pgList.totatlItemCount"/> 筆資料</p>
					</li>
					<li>
						&nbsp;&nbsp;&nbsp;每頁筆數:
					</li>
					<li>
						<select class="select-pageSize">
							<option value="10">10</option>
							<option value="20">20</option>
							<option value="50">50</option>
						</select>
					</li>				
				</ul>
			</div>
			<div class="clear"></div>
		</div>
		
		<div class="bottom-btn-block">
			<input type="button" class="redBtn btn-confirm" value="確定所選" />
		</div>				
	</s:form>
</body>
</html>

