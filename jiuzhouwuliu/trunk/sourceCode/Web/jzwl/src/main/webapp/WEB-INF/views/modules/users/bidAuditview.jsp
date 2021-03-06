<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>高估值货品审核</title>
<meta name="decorator" content="default" />
<!-- 引入layer插件 -->
<link rel="stylesheet" href="${ctxStatic}/layer-v2.0/layer/skin/layer.css">
<link rel="stylesheet" href="${ctxStatic}/image-viewer/css/viewer.min.css">
<script src="${ctxStatic}/layer-v2.0/layer/layer.js"></script>
<script src="${ctxStatic}/layer-v2.0/layer/laydate/laydate.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/cms/front/themes/weixin/lyb/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/uploadfile/ajaxfileupload.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/jquery.form.js"></script>
<script type="text/javascript">
	//回调函数，在编辑和保存动作时，供openDialog调用提交表单
	var htmlstatus = false;
	function doSubmit(status) {
		if (htmlstatus == true || htmlstatus == "true") {
			return false;
		}
		var checkRemark = $("#checkRemark").val();
		if (status == '2') {
			if (checkRemark == null || checkRemark.trim() == "") {
				layer.alert('请输入审核意见', {
					icon : 3
				});
				return;
			}
		}
		$.ajax({
			type : 'POST',
			url : "${ctx}/users/bidAudit/checkResult",
			data : {
				id : $("#id").val(),
				isCheckPass : status,
				checkRemark : checkRemark
			},
			dataType : "json",
			success : function(data) {
				htmlstatus = data.status;
				layer.alert(data.msg, {
					icon : 1,
					shade : 0
				}, function(index) {
					parent.location.reload(); // 父页面刷新  
				});
			},
			error : function(e) {
				layer.alert("操作失败", {
					icon : 1,
					shade : 0
				});

			}
		});
	}
	
</script>
<style type="text/css">
/* select, textarea, input[type="text"], input[type="password"], input[type="datetime"], input[type="datetime-local"], input[type="date"], input[type="month"], input[type="time"], input[type="week"], input[type="number"], input[type="email"], input[type="url"], input[type="search"], input[type="tel"], input[type="color"], .uneditable-input{
			height: 30px;
		} */
.form-search .ul-form li label {
	width: 120px;
	text-align: right;
}
</style>
</head>

<body>
	<div style="text-align: center">
		<!-- Tab panes -->
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="home">
				<form id="infoForm" method='post' class="breadcrumb form-search" style="background-color: white;" enctype="multipart/form-data">
					<input type="text" id="id" name="id" value="${goods.id}" style="display:none" />
					<ul id="dataShowUl" class="ul-form">
						<li style="margin-left: 65px;">
							<h5>货物信息</h5>
						</li>
						<li class="clearfix"></li>
						<li><label>货主名称：</label> <input type="text" id="shipperName" name="shipperName" value="${goods.userName }" maxlength="100"
							class="input-medium" style="width:163px" readonly="readonly" /></li>
						<li><label>货主手机号码：</label> <input id="shipperTel" name="shipperTel" type="text" value="${goods.userPhone }" class="input-medium"
							style="width:163px" readonly="readonly" /></li>
						<li class="clearfix"></li>
						<li><label>货物类型：</label> <input id="goodsType" name="goodsType" type="text" value="${goods.goodsType }" class="input-medium"
							style="width:163px" readonly="readonly" /></li>
						<li><label>发货地详细地址：</label> <input id="shipperAreaDetail" name="shipperAreaDetail" type="text" value="${goods.shipperAreaDetail }"
							class="input-medium" style="width:163px" readonly="readonly" /></li>
						<li class="clearfix"></li>
						<li><label>收货地详细地址：</label> <input type="text" id="reciverAreaDetail" name="reciverAreaDetail" value="${goods.reciverAreaDetail }"
							class="input-medium" style="width:163px" readonly="readonly" /></li>
						<li><label>收货手机号码：</label> <input type="text" id="reciverTel" name="reciverTel" value="${goods.reciverTel }" class="input-medium"
							style="width:163px" readonly="readonly" /></li>
						<li class="clearfix"></li>
						<li><label>类型（保险）：</label> <input id="insuranceType" name="insuranceType" type="text" value="${goods.insuranceType }" class="input-medium"
							style="width:163px" readonly="readonly" /></li>
						<li><label>需求车型：</label> <input type="text" id="needTruckType" name="needTruckType" value="v" class="input-medium" style="width:163px"
							readonly="readonly" /></li>
						<li class="clearfix"></li>
						<li><label>货物单位：</label> <input type="text" id="goodsUnit" name="goodsNum" value="${goods.goodsUnit }" class="input-medium"
							style="width:163px" readonly="readonly" /></li>
						<%-- <li><label>是否交保险：</label> <input type="text" id="isHasInsurance" name="isHasInsurance" value="${(goods.isHasInsurance==0)? '否':'是'}"
							class="input-medium" style="width:163px" readonly="readonly" /></li>
						<li class="clearfix"></li>
						<li><label>是否开票：</label> <input type="text" id="isHasBill" name="isHasBill" value="${(goods.isHasBill==0)? '否':'是'}" class="input-medium"
							style="width:163px" readonly="readonly" /></li> --%>
						<c:choose>
							<c:when test="${fn:length(imgList)==0}">
								<li class="clearfix" style="margin-bottom:5px;"></li>
							</c:when>
							<c:when test="${fn:length(imgList)<=4}">
								<li class="clearfix"></li>
								<li style="height:110px;margin-top:8px;"><label>货物图片：</label> <c:forEach items="${imgList}" var="i">
										<img src="${pageContext.request.contextPath}/${i}" data-original="${pageContext.request.contextPath}/${i}" style="width:120px; height:100px;cursor:pointer"
											alt="${i == null || i == '' ? '暂无图片' : ''}">
									</c:forEach></li>
							</c:when>
							<c:otherwise>
								<li style="height:110px;margin-top:8px;"><label>货物图片：</label> <c:forEach items="${imgList}" var="i" end="3">
										<img src="${pageContext.request.contextPath}/${i}" data-original="${pageContext.request.contextPath}/${i}" style="width:120px; height:100px;cursor:pointer"
											alt="${i == null || i == '' ? '暂无图片' : ''}">
									</c:forEach></li>
								<li style="height:110px;"><label></label> <c:forEach items="${imgList}" var="i" begin="4">
										<img src="${pageContext.request.contextPath}/${i}" data-original="${pageContext.request.contextPath}/${i}" style="width:120px; height:100px;cursor:pointer"
											alt="${i == null || i == '' ? '暂无图片' : ''}">
									</c:forEach></li>
							</c:otherwise>
						</c:choose>
						<li style="height:100px;width:100%; "><label style="float:left">审核意见：</label> <textarea cols='5' name="checkRemark"
								id="checkRemark" readonly="readonly" style="width:480px;height:50px;float:left">${goods.checkRemark }</textarea></li>
					</ul>
				</form>
			</div>

		</div>
	</div>
<script type="text/javascript" src="${ctxStatic}/image-viewer/js/viewer.min.js"></script>
<script>
var viewer = new Viewer(document.getElementById('dataShowUl'), {
	url: 'data-original'
});
</script>

</body>
</html>
