<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags"%>
<script type="text/javascript">
	$(document).ready(function () {
		dropDownBoxSetting();
	});
</script>
<script>
	function dropDownBoxSetting() {
		$(".dropDownBox").mouseleave(function(){
			toggleDropDownBox();
			displaySelectedGrbDomains();
		});
	}

	var expanded = false;
	function toggleDropDownBox() {
		if (!expanded) {
			$(".dropDownBox").css("display","block")
			expanded = true;
		} else {
			$(".dropDownBox").css("display","none")
			expanded = false;
			displaySelectedGrbDomains();
		}
	}
	
	function displaySelectedGrbDomains() {
		var selectedGrbDomains = "";
		$("[name='searchCondition.grbDomainIdList']:checked").each(function(index){
			selectedGrbDomains += $(this).parents("li.third").find(".grbDomainName").val()+"; \r\n";
		});
		var selectCount = $("[name='searchCondition.grbDomainIdList']:checked").length;
		if (selectCount > 0) {
			$(".selectBox select option").html(selectedGrbDomains);
			$(".selectBox").attr("title", selectedGrbDomains);
		} else {
			$(".selectBox select option").html("選擇領域");
			$(".selectBox").removeAttr("title");
		}
	}
</script>
<style>
.selectBox { position: relative; width: 200%;}
.overSelect { position: absolute; left: 0; right: 13px; top: 0; bottom: 0; }
.dropDownBox { width:60%; background-color:rgba(255, 255, 255, 1.0) ; display:none; border:#e6eff5 1px solid; position:absolute; z-index:9999;}
.dropDownBox label { display: block; font-size: 0.7em; }
.dropDownBox label:hover { background-color: #1e90ff; color:#ffffff; }
.dropDownBox li { margin:0; padding:0;}
</style>
<div class="well">
		<div class="row">
			<div class="col-sm-3 col-xs-12">
				<s:textfield placeholder="姓名" name="searchCondition.talentedPeopleSearchModel.name"
					maxlength="100" />
			</div>
			<div class="col-sm-3 col-xs-12">
				<s:select name="searchCondition.talentedPeopleSearchModel.gender" list="#{ '男':'男', '女':'女' }"
					headerKey="" headerValue="選擇性別" />
			</div>
			<div class="col-sm-3 col-xs-12">
				<s:textfield placeholder="現職單位" name="searchCondition.talentedPeopleSearchModel.workOrg"
					maxlength="100" />
			</div>

			<div class="col-sm-4 col-xs-12">
				<div style="float: left; width: 48%;">
					<s:textfield placeholder="產學經驗(起)" name="searchCondition.talentedPeopleSearchModel.expYearS"
						maxlength="3" />
				</div>
				<div style="float: right; width: 48%;">
					<s:textfield placeholder="產學經驗(迄)" name="searchCondition.talentedPeopleSearchModel.expYearE"
						maxlength="3" />
				</div>
			</div>
			<div class="col-sm-3 col-xs-12">
				<ul>
					<div>
						<div class="selectBox" onclick="toggleDropDownBox()">
							<select><option>選擇領域</option></select>
							<div class="overSelect"></div>
						</div>
						<div class="dropDownBox">
							<ul style="width: 200px; height: 300px; overflow-x:hidden;overflow-y:auto">
								<s:iterator value="searchCondition.mainDomainList" status="stat">
									<li class="all"><b><s:property value="name"/></b></li>
									
									<s:iterator value="subDomainList" status="stat">
									<li class="third">
										<s:checkbox id="%{'chkbox_'+id}" label="%{name}" name="searchCondition.talentedPeopleSearchModel.grbDomainIdList" value="%{searchCondition.talentedPeopleSearchModel.grbDomainIdList.contains(id)}" fieldValue="%{id}"/>  
										<input type="hidden" class="grbDomainName" value="<s:property value="name"/>" />
									</li>
									</s:iterator>
								</s:iterator>
							</ul>
						</div>
					</div>				
			</ul>
			</div>
			<div class="col-sm-2 col-xs-12">
				<s:textfield placeholder="合作專長" name="searchCondition.talentedPeopleSearchModel.specialty" maxlength="1000" />
			</div>

		</div>
</div>