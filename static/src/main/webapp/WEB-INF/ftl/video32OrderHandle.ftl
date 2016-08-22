<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<#import "template/template.ftl" as frame> <#import
"template/orderDetail.ftl" as orderDetail/> <#import
"template/pickBuses.ftl" as pickBuses> <@frame.html title="订单办理"
js=["js/nano.js","js/highslide/highslide-full.js",
"js/video-js/video.js", "js/video-js/lang/zh-CN.js",
"js/jquery-ui/jquery-ui.min.js","index_js/sift_common.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/layer-v1.9.3/layer/layer.js","js/progressbar.js","js/jquery-dateFormat.js"
,"js/ajax-pushlet-client.js"
]
css=["js/highslide/highslide.css",
"js/video-js/video-js.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","css/liselect/pkg-generator.css$ver=1431443489.css"]>
<#include "template/preview.ftl" />
<style type="text/css">
.textareaBox {
	border: 1px solid #3BAFDA;
}

.title-box{
    border-bottom: 0px;
}

.withdraw-wrap{
	border-top:0;
}

.ui-table-gray tr{
	background: none;
}

.ui-table-gray tr th{
	//color: white;
}

.banknameformError .parentFormuserForm3 .formError {
	opacity: 0.87;
	position: absolute;
	top: 0px;
	left: 340px !important;
	
}
</style>

<script type="text/javascript">
	$(function() {
	//显示当前节点对应的表单信息
	$('.${activityId!'' }').css("display","inline");
	$("#otherpay").hide();
	$("#contractCode").hide();
    $("#pingzhengTab").hide();
	initOrderDetailTable('${rc.contextPath}',${video32OrderStatus.order.id});
});


function go_back() {
		history.go(-1);
}

function uploadImaget(formId) { 
    var image_name=$("#fileMaterial").val();
    if(image_name != ''){
    var imgs=image_name.split(".");
    var img_subfier= imgs[imgs.length-1].toLocaleLowerCase();
    var img_parr = ["jpg", "jpeg", "gif","png"]; 
    
    if(image_name !=''){
        if($.inArray(img_subfier, img_parr) ==-1){
            jDialog.Alert("请上传['jpg','gif','png','jpeg']格式的图片!");
            return false;
        }
    }
    var options = { 
            url : "${rc.contextPath}/upload/savePayvoucher/${video32OrderStatus.order.id!''}",
            type : "POST",
            data : {"filetype":"payvouOf32"},
            dataType : "json",
            success : function(data) {
             if(data !=null && data!=""){
                  $("#showImg").attr("src","${rc.contextPath}/upload_temp/"+data);
                   } 
                 }
        }; 
        $("#" +formId+"").ajaxSubmit(options);
        document.getElementById(formId).reset();
        }
}
function pay(tp) {
	    var contractid=-1;
	     var payType="";
	     var invoiceid=0;
	     var contents="";
	     var receway="";
	     var orderid = $("#orderid").val();
	     var temp=document.getElementsByName("payType");
	       var payWayTemp=document.getElementsByName("payWay");
	       var isinvoice=0;
	      for(var i=0;i<temp.length;i++)
         {
           if(temp[i].checked)
            payType = temp[i].value;
         }
            if(payType==""){
            	 jDialog.Alert("请选择支付方式");
	         	 return;
            }
	         else if(payType=="others"){
	             var otp=$("#otherpay  option:selected").val();
	              if(otp==""){
	              layer.msg("请选择支付方式");
	              return;
	               }else{
	               payType=$("#otherpay option:selected").val();
	                }
	         }else{
	            contractid=-1;
	         }
		
		$.ajax({
			url : "${rc.contextPath}/order/payment",
			type : "POST",
			data : {
				"orderid" :orderid,
				"payType":payType
				
			},
			success : function(data) {
				layer.msg(data.right);
				var uptime = window.setTimeout(function(){
				var a = document.createElement('a');
    	        a.href='${rc.contextPath}/order/to32OrderList/1';
            	document.body.appendChild(a);
             	a.click();
			   	clearTimeout(uptime);
						},2000)
				
			}
		}, "text");
	}

		function showContract(){
	     $("#contractCode").show();
	     $("#otherpay").hide();
	     $("#pingzhengTab").hide();
	}
	function hideContract(){
	     $("#contractCode").hide();
	     $("#otherpay").show();
	     $("#pingzhengTab").hide();
	}
	function hideboth(){
		
	     $("#contractCode").hide();
	     $("#otherpay").hide();
	     $("#pingzhengTab").show();
	}
	function hideall(){
		
	     $("#contractCode").hide();
	     $("#otherpay").hide();
	     $("#pingzhengTab").hide();
	}

</script>
<input type="hidden" id="orderid" value="${video32OrderStatus.order.id!''}" />
<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">订单详情</A>
		</H3>
		<BR>
		 
	<table id="orderDetailTable" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				                <th>订单号</th>
								<th>线路</th>
								<th>开播时间</th>
								<th>结束时间</th>
							
			</tr>
		</thead>

	</table>
</div>
    <#if activityId == "paid">
<!-- 支付-->
<div id="paid" class="paid relateSup" style="display: none;">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 style="border-bottom:0;" class="text-xl title-box">
			<p style="text-align: left">
				<A class="black" href="#">支付订单</A>
			</p>
		</H3>
		<BR>
		<TABLE class="ui-table ui-table-gray">
		<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">订单价格</TD>
				<TD>
					<SPAN></SPAN><SPAN class="con">${video32OrderStatus.order.price!''}</SPAN>
				</TD>
			</TR>
			<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">支付方式</TD>
				<TD>
					<input type="radio" name="payType" value="remit">汇款支付 
					<input type="radio"	name="payType" value="others">其他支付
				</TD>
				<TD>
					<div id="otherpay">
						<select class="ui-input" name="otherpay" id="otherpay">
							<option value="" selected="selected">请选择支付方式</option>
							<option value="check">支票</option>
							<option value="cash">现金</option>
						</select>
					</div>
				</TD>
			</TR>
			<tbody id="pingzhengTab">
			<TR style="height: 45px;">
				<TD style="text-align: right">上传有效银行支付凭证（可选）</TD>
				<TD colspan=3>
				  <form id="form_img" method="post" enctype="multipart/form-data"> 
                     <img src="" id="showImg" width="200" height="100" border="1px solid #d0d0d0;"/>
                     <input id ="fileMaterial" name="fileMaterial" type="file" onchange="uploadImaget('form_img');"/>
                 </form>
				</TD>
			</TR>
			</tbody>
		
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<button type="button" onclick="pay('')" class="block-btn">确认支付</button>
		</div>
		
		<div class="worm-tips">
		<div class="tips-title">
			<span class="icon"></span>
			<font color="orange"><B>温馨提示</B></font><br>
			三方一致：合同甲方公司名称-付款方银行开户名称-开具发票抬头名称<br>
			线下付款的账户信息：<br>
			&nbsp;&nbsp;&nbsp;&nbsp;开户行：<B>工行知春路支行</B><br>
			&nbsp;&nbsp;&nbsp;&nbsp;账户名称：<b>北京世巴传媒有限公司</b><br>
			&nbsp;&nbsp;&nbsp;&nbsp;收款方账号：<b>0200207909200097152</b><br>
			&nbsp;&nbsp;&nbsp;&nbsp;公司地址：北京市海淀区紫竹院路32号15号平房	电话：68427368<br>
		</div>
		</div>

	</div>
</div>
</#if>
    
</@frame.html>
