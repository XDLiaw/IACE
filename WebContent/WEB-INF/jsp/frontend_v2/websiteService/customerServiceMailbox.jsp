<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<script type="text/javascript">
	$(document).ready(function() {
		$('ul.categoryList li:contains("客服信箱")').addClass("active");
		
		$("#form-sendMessage").submit(function () {
			if ($("#readPolicyCheck").prop("checked")) {
				return true;
			} else {
				alert("請選擇同意接受「個人資料蒐集、處理及利用之告知暨同意條款」");
				return false;
			}
		});
	});
</script>
</head>
<body>
	<!-- Banner -->
	<s:include value="./banner.jsp" />

	<!-- Main -->
	<div class="container top50">
		<div class="row">
			<div class="col-sm-12 col-xs-12">
				<div>
					<div class="large_title_01">
						<i class="fa fa-user-circle-o" aria-hidden="true" style="font-size: 18px; margin-right: 5px;"></i>網站服務
					</div>
					<div class="line_blue">&nbsp;</div>
					<div class="line_gray1px bottom20"></div>
				</div>
			</div>
		</div>	
		<div class="row">
			<!-- 左選單 -->
			<div class="col-sm-3 col-xs-12 bottom20">
				<s:include value="./left_menu.jsp" />
			</div>
			<!-- 右列表 -->
			<div class="col-sm-9 col-xs-12">
				<div class="large_title_03">客服信箱</div>
				<div class="line_solid"></div>
				<div class="content_01 top10 text-center">
					如果有任何問題，歡迎您留下訊息，我們將儘速回答您的問題， 並竭誠歡迎您的建言，謝謝！<br> (以下有標 * 的欄位請務必填寫，以便我們能針對您的意見適時給予答覆！)
				</div>
				<div class="content_01 well top10">
					<s:form action="createSubmit" method="post" validate="true" class="form-horizontal" id="form-sendMessage">
						<div class="form-group">
							<label for="" class="col-sm-2 control-label">公司名稱</label>
							<div class="col-sm-10">
								<s:textfield name="contactUs.companyName" maxlength="200" class="form-control"/>
							</div>
						</div>
						<div class="form-group form-inline">
							<label for="" class="col-sm-2 control-label">聯絡人姓名<span style="color: #F00;">*</span></label>
							<div class="col-sm-10">
								<s:textfield name="contactUs.name" maxlength="50" class="form-control"/>
								<label class="radio-inline"> 
									<input type="radio" name="contactUs.gender" id="" value="true" checked="checked">先生
								</label> 
								<label class="radio-inline"> 
									<input type="radio" name="contactUs.gender" id="" value="false">小姐
								</label>
							</div>
						</div>
						<div class="form-group">
							<label for="" class="col-sm-2 control-label">聯絡電話<span style="color: #F00;">*</span></label>
							<div class="col-sm-10">
								<s:textfield name="contactUs.phone" maxlength="30" class="form-control"/>
							</div>
						</div>
						<div class="form-group">
							<label for="" class="col-sm-2 control-label">E-MAIL<span style="color: #F00;">*</span></label>
							<div class="col-sm-10">
								<s:textfield name="contactUs.email" maxlength="200" type="email" class="form-control"/>
							</div>
						</div>
						<div class="form-group">
							<label for="" class="col-sm-2 control-label">意見內容<span style="color: #F00;">*</span></label>
							<div class="col-sm-10">
								<s:textarea name="contactUs.message" class="form-control" rows="3"/>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<div class="checkbox">
									<label> 
										<input type="checkbox" id="readPolicyCheck">
										同意【<a href="#">個人資料蒐集、處理及利用之告知暨同意條款</a>】
									</label>
								</div>
							</div>
						</div>
						<div class="form-group form-inline">
							<label for="" class="col-sm-2 control-label">驗證碼<span style="color: #F00;">*</span></label>
							<div class="col-sm-10">
								<s:include value="/WEB-INF/jsp/captcha.jsp" />
							</div>
						</div>
						<div class="line_gray1px bottom10 top30"></div>
						<div class="form-group">
							<div class="col-sm-12 text-center">
								<button type="submit" class="btn btn-primary">確定送出</button>
								<button type="button" class="btn btn-default" onclick="window.location.href='<s:url value="/f2/customerServiceMailbox/create"/>'">清除重填</button>
							</div>
						</div>
					</s:form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>		