<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	$(document).ready(function(){
		
	});
</script>
<script type="text/javascript">
	$(document).ready(function(){
		$(".btn-dataSetting").click(function(){
			var url = $(this).siblings(".dataSettingUrl").val();
			$("form#form-create").attr('action', url);
			$("form#form-create").submit();
			$("form#form-create").attr('action', '<s:url value="/popularActivity/init"/>'); // 要把action改為原本的，否則如果使用者按下瀏覽器的上一頁回到目前這個列表頁再去按搜尋就會跑到已經被改變的action所指定的那一頁
		});
		$(".btn-del").click(function(){
// 			$(this).parents("tr").remove();
			var delete1 = $(this).val(); 
			var url = $(this).parents("tr").find(".deleteUrl").val()+delete1;
			$("form").attr('action', url);
			$("form").submit();
			$("form").attr('action', '<s:url value="/popularActivity/init"/>'); // 要把action改為原本的，否則如果使用者按下瀏覽器的上一頁回到目前這個列表頁再去按搜尋就會跑到已經被改變的action所指定的那一頁
		});
		$(".btn-prioritysSetting").click(function(){
			var url = $(this).siblings(".prioritysSetting").val();
			$("form#form-create").attr('action', url);
			$("form#form-create").submit();
			$("form#form-create").attr('action', '<s:url value="/popularActivity/init"/>'); // 要把action改為原本的，否則如果使用者按下瀏覽器的上一頁回到目前這個列表頁再去按搜尋就會跑到已經被改變的action所指定的那一頁
		});
		$("input[type=number]").change(function() {
			var priority1 = $(this).val(); 
			var url = $(this).parents("tr").find(".updatePriorityUrl").val() + "&priority="+priority1;
			$("form").attr('action', url);
			$("form").submit();
			$("form").attr('action', '<s:url value="/popularActivity/init"/>'); // 要把action改為原本的，否則如果使用者按下瀏覽器的上一頁回到目前這個列表頁再去按搜尋就會跑到已經被改變的action所指定的那一頁
		});
	});
</script>


<meta name="funcPathText" content="設定"/>
</head>
<body>
	<s:form action="init" method="post" validate="true" id="form-create">
		<!-- 活動人培 -->
		<div>
			<input type="hidden" class="dataSettingUrl" value="<s:url value="/popularActivity/index" />"/>
			<input type="button" value="HOT20更改" class="btn-dataSetting"/>
			<table>
				<thead>
					<tr>
						<th>標題</th>
						<th>順序</th>
						<th width="8%">功能</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="activityList" status="status">
						<tr>
							<s:hidden name="activityIds" value="%{id}"/>
							<td><s:property value="title"/></td>
							<td>
								<s:url value="update.action" var="updatePriorityTag" >
									<s:param name="id" value="%{popularActivityList[#status.index].id}" />
								</s:url>
								<s:hidden value="%{#updatePriorityTag}" class="updatePriorityUrl"/>	
							    <s:if test='%{#session.sysUser.hasAuth(namespace, "update")}'>								
					                <s:textfield type="number" value="%{popularActivityList[#status.index].priority}" />	
								</s:if>										        								
							</td>
							<td>
							    <s:url value="delete.action" var="deleteTag" >
									<s:param name="id" value="%{popularActivityList[#status.index].id}" />
								</s:url>
								<s:hidden value="%{#deleteTag}" class="deleteUrl"/>	
							    <input type="button" class="btn-info btn-func btn-del" />刪除							    
							</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
		<div class="clear"></div>
	</s:form>
</body>
</html>