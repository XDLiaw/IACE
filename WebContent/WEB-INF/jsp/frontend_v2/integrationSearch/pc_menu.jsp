<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags"%>

<script type="text/javascript">
//因為index 的btn-reset會把 pc_menu 的input text className清空 造成難以判斷 所以把pc_menu裡的改成hidden
	$(document).ready(function() {
		$(".pc_menu .classNameBtn").click(function(){
			$(".pc_menu .search_icon_active").removeClass("search_icon_active").addClass("search_icon");
			$(this).addClass("search_icon_active");
			var className = $(this).find(".classNameValue").val();
			$("input[name='searchCondition.className']").val(className);	
		});
		
		var className = $("input[name='searchCondition.className']").val();
		$(".pc_menu .classNameValue[value='"+className+"']").parents(".classNameBtn").trigger("click");
	});
</script>


<div class="col-sm-12 col-xs-12 pc_menu bottom20">
	<table class="table">
		<tbody>
			<tr>
				<td width="11%" style="border: none;">
					<div class="search_icon text-center classNameBtn" >
<!-- 					因為index 的btn-reset會把 pc_menu 的input text className清空 造成難以判斷 所以把pc_menu裡的改成hidden -->
						<input type="hidden" class="classNameValue" style="display:none;" value=""/>
						<div>
							<img src="<s:url value="/images/frontend-v2/icon_01.png"/>" alt="" height="40" />
						</div>
						<div class="top10 tag">全部</div>
					</div>
				</td>
				<td width="13%" style="border: none;">
					<div class="search_icon text-center classNameBtn">
						<input type="hidden" class="classNameValue" style="display:none;" value="iace.entity.researchPlan.ResearchPlan"/>
						<div>
							<img src="<s:url value="/images/frontend-v2/icon_02.png"/>" alt="" height="40" />
						</div>
						<div class="top10 tag">研發成果</div>
					</div>
				</td>
				<td width="13%" style="border: none;">
					<div class="search_icon text-center classNameBtn">
						<input type="hidden" class="classNameValue" style="display:none;" value="iace.entity.patent.Patent"/>
						<div>
							<img src="<s:url value="/images/frontend-v2/icon_03.png"/>" alt="" height="40" />
						</div>
						<div class="top10 tag">專利資料</div>
					</div>
				</td>
				<td width="13%" style="border: none;">
					<div class="search_icon text-center classNameBtn">
						<input type="hidden" class="classNameValue" style="display:none;" value="iace.entity.talentedPeople.TalentedPeople"/>
						<div>
							<img src="<s:url value="/images/frontend-v2/icon_04.png"/>" alt="" height="40" />
						</div>
						<div class="top10 tag">產學人才</div>
					</div>
				</td>
				<td width="13%" style="border: none;">
					<div class="search_icon text-center classNameBtn">
						<input type="hidden" class="classNameValue" style="display:none;" value="iace.entity.coopExample.CoopEx"/>
						<div>
							<img src="<s:url value="/images/frontend-v2/icon_05.png"/>" alt="" height="40" />
						</div>
						<div class="top10 tag">合作案例</div>
					</div>
				</td>
				<td width="13%" style="border: none;">
					<div class="search_icon text-center classNameBtn">
						<input type="hidden" class="classNameValue" style="display:none;" value="iace.entity.literature.Literature"/>
						<div>
							<img src="<s:url value="/images/frontend-v2/icon_06.png"/>" alt="" height="40" />
						</div>
						<div class="top10 tag">法規/文獻</div>
					</div>
				</td>
				<td width="13%" style="border: none;">
					<div class="search_icon text-center classNameBtn">
						<input type="hidden" class="classNameValue" style="display:none;" value="iace.entity.incubationCenter.IncubationCenter"/>
						<div>
							<img src="<s:url value="/images/frontend-v2/icon_07.png"/>" alt="" height="40" />
						</div>
						<div class="top10 tag">育成中心</div>
					</div>
				</td>
				<td width="11%" style="border: none;">
					<div class="search_icon text-center classNameBtn">
						<input type="hidden" class="classNameValue" style="display:none;" value="OTHER"/>
						<div>
							<img src="<s:url value="/images/frontend-v2/icon_08.png"/>" alt="" height="40" />
						</div>
						<div class="top10 tag">其它</div>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
