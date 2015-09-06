<#import "template/template.ftl" as frame> 
<#import "template/select_lines.ftl" as select_lines>
<@frame.html title=""
js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

<script type="text/javascript">
	$(function() {
	//显示当前节点对应的表单信息
	$('.${activityId!'' }').css("display","inline");
});

//锁定库存
function LockStore() {
        var LockDate = $("#LockDate").val();
        var d = new Date(LockDate.replace(/-/g,"/")); 
        date = new Date();
        var str  = date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate();
        var d2 = new Date(str.replace(/-/g,"/")); 
        if(LockDate==""){
         jDialog.Alert('请填写预留截止日期');
         return;
         }
           var days=Math.floor((d-d2)/(24*3600*1000));
        if(days<1) {
            jDialog.Alert('预留截止日期必须选择今天以后的日期');
            return;
         } 
    var isEnough="";
    var orderid=$("#orderid").val();
    var taskid=$("#taskid").val();
    var contractid=$("#contractCode  option:selected").val();
    var temp=document.getElementsByName("canSchedule");
	      for(var i=0;i<temp.length;i++)
         {
           if(temp[i].checked)
            isEnough = temp[i].value;
         }
	        if(isEnough=="true"){
	          if(contractid==""){
	              jDialog.Alert("请选择合同");
	              return;
	            }
	        }
    var canSchedule=$('#usertask1 :radio[name=canSchedule]:checked').val();
	var url="${rc.contextPath}/busselect/LockStore";
	// 发送任务完成请求
    $.post(url,{
        canSchedule: canSchedule,
        contractid: contractid,
        orderid: orderid,
        taskid:taskid,
        LockDate:LockDate
    },function(data){
    	jDialog.Alert(data.left==true? data.right :"执行失败!");
    	var uptime = window.setTimeout(function(){
			var a = document.createElement('a');
    		a.href='${rc.contextPath}/busselect/myTask/1';
    		document.body.appendChild(a);
    		a.click();
			clearTimeout(uptime);
		},2000)
    	
    });
}
//回执处理
function isreceveCon() {
var ReceComments=$('#ReceComments').val();
	complete('${taskid!''}',[
		{
			key: 'ReceComments',
			value: ReceComments,
			type: 'S'
		}
	]);
}
//录入小样
function uploadXY() {
          if($("#Sfile").val()==""){
            layer.msg("请选择小样");
            return;
          }
$("#xyform").ajaxForm(function(data) {
	 $("#xybutton").attr("disabled",true);
     $("#xybutton").css("background-color","#85A2AD");
     jDialog.Alert(data.left==true? data.right :"执行失败!");
		var uptime = window.setTimeout(function(){
		var a = document.createElement('a');
    		a.href='${rc.contextPath}/busselect/myTask/1';
    		document.body.appendChild(a);
    		a.click();
		clearTimeout(uptime);
		},2000)
	}).submit();
}
//施工确认
function shigong() {
var shigongComments=$('#shigongComments').val();
	complete('${taskid!''}',[
		{
			key: 'shigongComments',
			value: shigongComments,
			type: 'S'
		}
	]);
}
//财务确认
function financialCheck() {
 var orderid=$("#orderid").val();
    var taskid=$("#taskid").val();
  var paymentResult=$('#financialCheck :radio[name=paymentResult]:checked').val();
  var financialcomment=$('#financialcomment').val();
  var url="${rc.contextPath}/busselect/financialCheck";
	// 发送任务完成请求
    $.post(url,{
        paymentResult: paymentResult,
        financialcomment: financialcomment,
        orderid: orderid,
        taskid:taskid
    },function(data){
    	jDialog.Alert(data.left==true? data.right :"执行失败!");
    	var uptime = window.setTimeout(function(){
			var a = document.createElement('a');
    		a.href='${rc.contextPath}/busselect/myTask/1';
    		document.body.appendChild(a);
    		a.click();
			clearTimeout(uptime);
		},2000)
    	
    });
}
function complete(taskId, variables) {
	// 转换JSON为字符串
    var keys = "", values = "", types = "";
	if (variables) {
		$.each(variables, function() {
			if (keys != "") {
				keys += ",";
				values += ",";
				types += ",";
			}
			keys += this.key;
			values += this.value;
			types += this.type;
		});
	}
	
var url="${rc.contextPath}/order/"+taskId+"/complete";
	// 发送任务完成请求
    $.post(url,{
        keys: keys,
        values: values,
        types: types
    },function(data){
    	jDialog.Alert(data.left==true? data.right :"执行失败!");
    	var uptime = window.setTimeout(function(){
			var a = document.createElement('a');
    		a.href='${rc.contextPath}/busselect/myTask/1';
    		document.body.appendChild(a);
    		a.click();
			clearTimeout(uptime);
		},2000)
    	
    });
    
}

    i = 2;
	j = 2;
	$(document).ready(function() {
			$("#btn_add2").live("click",function() {
					$("#newUpload2").append('<div id="div_'+j+'"><input type="file" name="file'+j+'"  style="margin-top:10px;" class="validate[required]"/><input class="btn-sm btn-wrong" type="button" value="删除" style="margin-top:10px;" onclick="del_2('+ j+ ')"/></div>');
											j = j + 1;
										});
					});
	function del_2(o) {
		document.getElementById("newUpload2").removeChild(
		document.getElementById("div_" + o));
	}
</script>
<input type="hidden" id="orderid" value="${bodycontract.id!''}" />
<input type="hidden" id="taskid" value="${taskid!''}" />
<#if activityId == "usertask1">
<!-- 锁定线路并生效合同 -->
<div id="usertask1" class="usertask1" style="display: none;">

	<script type="text/javascript">
	function gotoSchedult(id,modelId){
	  window.open("${rc.contextPath}/busselect/lineschedule/"+id+"?modelId="+modelId);
	  }
	function worklist(seriaNum,id){
	  window.open("${rc.contextPath}/busselect/workList/"+seriaNum+"/"+id);
	  }  
	  
	var orderBusesTable;
	function refreshOrderedBuses() {
		orderBusesTable = $('#table')
				.dataTable(
						{
							"dom" : '<"#toolbar">t',
							"searching" : false,
							"ordering" : false,
							"serverSide" : false,
							"ajax" : {
								type : "GET",
								url : "${rc.contextPath}/busselect/ajax-buslock",
								data : function(d) {
									return $.extend({}, d, {
										"seriaNum" : '${bodycontract.seriaNum!''}'
									});
								},
								 "dataSrc": function(json) {return json;},
							},
							"columns" : [
			    { "data": "line.name", "defaultContent": "","render": function(data, type, row, meta) {
                      return '<a   onclick=" gotoSchedult(' + row.line.id +","+(row.model.id )+ ')" >'+data+'</a> &nbsp;';
                }},
                { "data": "remainNuber", "defaultContent": ""}, 
                { "data": "model", "defaultContent": "", "render": function(data) {
                if(data.id ==0){
                 	return "所有车型"
                }else if(data.doubleDecker==false){
                  return data.name+'(单层)';
                 }else{
                    return data.name+'(双层)';
                 }
                }},
                { "data": "startDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                { "data": "endDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                } },
				 { "data": function( row, type, set, meta) {
                    return row.id;
                },
										"render" : function(data, type, row,
												meta) {
											var operations = '';
											operations += '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/busselect/ajax-remove-buslock?seriaNum=${bodycontract.seriaNum!''}&id=' + data +'">删除</a>&nbsp;&nbsp;';
											operations += '<a class="table-action2" href="javascript:void(0);" url="${rc.contextPath}/busselect/checkStock?seriaNum=${bodycontract.seriaNum!''}&buslockid=' + data +'">检验库存</a>&nbsp;&nbsp;';
											return operations;
										}
									},],
							"language" : {
								"url" : "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
							},
							"initComplete" : initComplete,
							"drawCallback" : drawCallback,
						});
		orderBusesTable.fnNameOrdering("orderBy").fnNoColumnsParams();
	}

	function drawCallback() {
		$('.table-action').click(function() {
			$.post($(this).attr("url"), function(data) {
			if(data){
				 orderBusesTable.dataTable()._fnAjaxUpdate();
				 }else{
				 alert("非法操作");
				 }
			})
		});
		$('.table-action2').click(function() {
			$.post($(this).attr("url"), function(data) {
			layer.msg(data.right);
			})
		});
	}
	function initComplete() {
		$("div#toolbar").attr("style", "width: 100%;")
		$("div#toolbar")
				.html('');
						
	}

	function sub() {
	var lineid=$("#db_id").val();
	var startd=$("#startDate").val();
	var endDate=$("#endDate").val();
	if(lineid==0){
	   alert("请选择线路");
	   return;
	}
	if(startd==""){
	 alert("请选上刊日期");
	   return;
	   }
	if(endDate==""){
	 alert("请选择下刊日期");
	   return;
	   }
	if($("#busNumber").val()==0){
	 alert("数量要大于0");
	   return;
	   }
	var bb=false;
	if(endDate<startd){
	layer.msg("下刊时间不能小于上刊开始时间");
		return;
	}
	$.ajax({
			url : "${rc.contextPath}/busselect/lineReaminCheck",
			type : "POST",
			async:false,
			dataType:"json",
			data : {
				"buslinId" : $("#db_id").val(),
				"start" : $("#startDate").val(),
				"end" : $("#endDate").val(),
				"modelId" : $("#model_Id  option:selected").val()
			},
			success : function(data) {  
				if($("#busNumber").val()>data){
					$("#worm-tips").empty(); 
					var tip='<div  class="tips-title" id="tip" style="padding-left: 13%;">[抱歉，所选线路库存量：<font color="red">'+data+'&nbsp;</font>少于选取数量]</div>'
					$("#worm-tips").append(tip);
					$("#worm-tips").show();
					return;
				}else {
					bb=true;
				}
			}
		});
	if(bb==true){
		$('#form01').ajaxForm(function(data) {
		if(data.left){
		     layer.msg("添加成功");
		       orderBusesTable.dataTable()._fnAjaxUpdate();
		       $("#cc").trigger("click");
		     }else{
		     layer.msg(data.right);
		     }
		}).submit();
		}
	}
	function initProvince(id) {
		$.ajax({
			url : "${rc.contextPath}/busselect/selectBusType",
			type : "POST",
			data : {
				"buslinId" : id
			},
			success : function(data) {
				$("#four").show();
				var v=' <option value="0" selected="selected">所有类型</option> ';
				$("#model_id").html(v);
				$.each(data, function(i, item) {
				
				var w="<option value="+item.gn1+">"
									+ item.gp2
									+ (item.gn2 == 0 ? "&nbsp;&nbsp; 单层"
											: "&nbsp;&nbsp; 双层")
									+ "&nbsp;&nbsp;[" + item.count + "]"
									+ "</option>";
					$("#model_id").append(w);
								
				});
			}
		}, "text");
	}

	function selctLine(url,seriaNum) {
		layer.open({
					type : 1,
					title : "选择车辆",
					skin : 'layui-layer-rim',
					area : [ '400px', '480px' ],
					content : ''
							+ '<form id="form01" action='+url+'/busselect/saveBusLock?seriaNum='+seriaNum+'>'
							+ '<div class="inputs" style="margin-top: 40px;margin-left: -30px;">'
							+ '<div class="ui-form-item"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/>'
							+ '<label class="ui-label mt10">选择线路：</label>'
							+ '<input class="ui-input"  id="line_id" data-is="isAmount isEnough">'
							+ '</div>'
							+ '<div id="four" style="display:none;"><div class="ui-form-item" id="model_Id">'
							+ '<label class="ui-label mt10">选择车型：</label>'
							+ '<select  class="ui-input bus-model" name="modelId" id="model_id"> <option value="0" selected="selected">所有类型</option> </select>'
							+ '</div>'
							+'<div class="ui-form-item"> <label class="ui-label mt10">选取数量：</label>'
							+'<input class="ui-input validate[required,integer,min[1],max[2000]]" type="number" value="0" name="remainNuber"'
							+'id="busNumber" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
           	 				+'</div>'
							+'<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">上刊日期:</label>'
							+'<input class="ui-input datepicker validate[required,custom[date],past[#endDate]]" type="text" name="startD" value="" id="startDate" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
							+'</div>'
							+'<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">下刊日期:</label>'
							+'<input class="ui-input datepicker validate[required,custom[date],past[#endDate]]" type="text" name="endD" value="" id="endDate" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
							+'</div></div>'
							+ '</div>'
							+ '<div class="ui-form-item widthdrawBtBox" style="position: absolute; bottom: 10px;">'
							+ '<input type="button" onclick="sub()" class="block-btn" value="确认选车" ></div>'
							+ '<input type="hidden" value="0" name="lineId" id="db_id"></form>'
							+'<div id="worm-tips" class="worm-tips" style="width:350px;display:none;"></div>'
				});
			var checkin = $('#startDate').datepicker()
			.on('click', function (ev) {
			        $('.datepicker').css("z-index", "999999999");
			}).data('datepicker');
			
			var checkin = $('#endDate').datepicker()
			.on('click', function (ev) {
			        $('.datepicker').css("z-index", "999999999");
			}).data('datepicker');
			
			
		$("#line_id").autocomplete({
		minLength: 0,
			source : "${rc.contextPath}/busselect/autoComplete",
			change : function(event, ui) {
				/*if(ui.item!=null){alert(ui.item.value);}*/
				//table.fnDraw();
			},
			select : function(event, ui) {
				$('#line_id').val(ui.item.value);
				//table.fnDraw();
				initProvince(ui.item.dbId);
				$("#db_id").val(ui.item.dbId);
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   				 });
		
	}

</script>
<script type="text/javascript">
	$(document).ready(function() {
         refreshOrderedBuses();
         $("#form002").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
            showOneMessage: true,
            maxErrorsPerField: 1,
        });
	});
	function SupContract(){
	if (!$("#form02").validationEngine('validateBeforeSubmit'))
            return;
	   $('#form02').ajaxForm(function(data) {
		  if(data.left){
		      document.getElementById('subutton').setAttribute('disabled',true);
		       $("#subutton").css("background-color","#85A2AD");
		      alert(data.right);
		      var uptime = window.setTimeout(function(){
				window.location.reload()
			   	clearTimeout(uptime);
						},2000)
		     }else{
		     alert(data.right);
		     }
		}).submit();
	}
</script>
<div class="color-white-bg fn-clear">

	<div id="relateSup">
		<div class="p20bs mt10 color-white-bg border-ec">
		<div class="withdraw-title">
			<span>选取车辆</span>
			<input type="hidden" name="seriaNum" id="seriaNum" value="${bodycontract.seriaNum}"/>
			<a class="block-btn" style="margin-top: -5px;" href="javascript:void(0);" onclick="selctLine('${rc.contextPath}',${bodycontract.seriaNum})">增加选择</a>
			<#if bodycontract.isSchedule()==true> 
				<a class="block-btn" style="margin-top: -5px;" href="javascript:void(0);" onclick="worklist(${bodycontract.seriaNum},${bodycontract.id})">施工单
			</#if>
			</a>
		</div>
			<div id="orderedBuses">
				<table id="table" class="display compact"
					cellspacing="0" width="100%">
					<thead>
						<tr class="tableTr">
					<th>线路</th>
                    <th>数量（辆）</th>
                    <th width="180px">车型</th>
                     <th>上刊日期</th>
                    <th>下刊日期</th>
                     <th>操作</th>
						</tr>
					</thead>

				</table>
			</div>

			<div id="orderBusesPopup" title="选择车辆"></div>
		</div>
</div>
<#include "template/body_contratDetail.ftl" />	
<div class="p20bs mt10 color-white-bg border-ec">
<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR style="height: 45px;">
					<TH style="width: 22%;">锁定库存</TH>
					<TD colspan=3><input name="canSchedule" type="radio"
						value="true" checked="checked" style="padding: 5px 15px;" />有库存 <input
						name="canSchedule" type="radio" value="false"
						style="padding: 5px 15px;" />库存不足</TD>
				</TR>
				<TR style="height: 45px;">
					<TH>预留截止日期</TH>
					<TD colspan=3>
							<input	class="ui-input datepicker validate[required,custom[date],past[#endDate]]" 
							type="text" value=""
							id="LockDate" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">
						<em style="color: #ccc;margin-left: 10px;">注：预留截止时间不得早于当前时间</em>						
					</TD>
					
				</TR>
				<TR style="height: 45px;">
				<TD width="20%" style="text-align: right">合同选择</TD>
					<TD>	<select class="ui-input" name="contractCode" id="contractCode">
							<option value="" selected="selected">请选择合同</option> <#if
							contracts?exists> <#list contracts as c>
							<option value="${c.id}">${c.contractName!''}</option> </#list>
							</#if>
						</select>
				</TD>
			</TR>
		</TABLE>
        <div style="text-align: center; margin-top: 10px;">
			<button type="button"  id="subutton" onclick="LockStore()" class="block-btn">确定</button>
		</div>
       </div>
       
       
	</div>
</div>
</#if>
<#if activityId == "usertask2">
<!-- 合同回执确认 -->
<@select_lines.select_lines seriaNum=bodycontract.seriaNum bodycontract=bodycontract activityId =activityId/>
<#include "template/body_contratDetail.ftl" />
<div id="usertask2" class="usertask2" style="display: none;">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">合同回执确认</A>
		</H3>
		<BR>
		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
					<TH width="20%">签收时间</TH>
					<TD colspan=2 style="border-radius: 0 0 0"><#setting
						date_format="yyyy-MM-dd HH:mm:ss"> ${claimTime!''}</TD>
				</TR>
				<TR>
					<TH>意见</TH>
					<TD colspan=2><textarea name="ReceComments" id="ReceComments"
							style="margin: 5px 0; width: 400px; margin-top: 5px;">已收到回执</textarea></TD>
				</TR>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<button onclick="isreceveCon();" class="block-btn">提交确认结果</button>
		</div>

	</div>
</div>


</#if> 
<#if activityId == "financialCheck">
<@select_lines.select_lines seriaNum=bodycontract.seriaNum activityId =activityId/>
<#include "template/body_contratDetail.ftl" />
<!--财务确认 -->

<div id="financialCheck" class="financialCheck" style="display: none;">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">财务确认</A>
		</H3>
		<BR>
		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
					<TH width="20%">签收时间</TH>
					<TD colspan=2 style="border-radius: 0 0 0"><#setting
						date_format="yyyy-MM-dd HH:mm:ss"> ${claimTime!''}</TD>
				</TR>
				<TR style="height: 45px;">
					<TH>支付确认</TH>
					<TD colspan=3><input name="paymentResult" type="radio"
						value="true" checked="checked" style="padding: 5px 15px;" />支付OK <input
						name="paymentResult" type="radio" value="false"
						style="padding: 5px 15px;" />逾期未支付</TD>
				</TR>
				<TR>
					<TH>意见</TH>
					<TD colspan=2><textarea name="financialcomment" id="financialcomment"
							style="margin: 5px 0; width: 400px; margin-top: 5px;">已收到Money</textarea></TD>
				</TR>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<button onclick="financialCheck();" class="block-btn">提交确认结果</button>
		</div>

	</div>
</div>
</#if> 
<#if activityId == "approve2">
<@select_lines.select_lines seriaNum=bodycontract.seriaNum activityId =activityId/>
<#include "template/body_contratDetail.ftl" />
<!--录入小样 -->
<div id="approve2" class="approve2" style="display: none;">
		<form name="xyform" id="xyform" action="${rc.contextPath}/busselect/uploadXiaoY?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1" method="post" enctype="multipart/form-data">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">录入小样</A>
		</H3>
		<BR>
		<TABLE class="ui-table ui-table-gray">
		<input type="hidden" name="mainid" value="${bodycontract.id!''}" />
        <input type="hidden" name="taskid" value="${taskid!''}" />
			<TBODY>
				<TR>
					<TH width="20%">签收时间</TH>
					<TD colspan=2 style="border-radius: 0 0 0"><#setting
						date_format="yyyy-MM-dd HH:mm:ss"> ${claimTime!''}</TD>
				</TR>
				<TR style="height: 45px;">
					<TH>上传小样</TH>
					<TD colspan=3>
					                      <div id="newUpload2">
												<div id="div_1">
													<input type="file" name="file01" id="Sfile" class="validate[required]"/>
												</div>
											</div>
											<input class="btn-sm btn-success" type="button" id="btn_add2" value="增加一行"
												style="margin-top: 10px;" ><br>
					</TD>
				</TR>
				<TR>
					<TH>意见</TH>
					<TD colspan=2><textarea name="approve2Comments" id="approve2Comments"
							style="margin: 5px 0; width: 400px; margin-top: 5px;">小样已上传</textarea></TD>
				</TR>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<input type="button" id="xybutton" value="上传小样" onclick="uploadXY();" class="block-btn"/>
		</div>
	</div>
		</form>
</div>
</#if> 

<#if activityId == "usertask4">
<@select_lines.select_lines seriaNum=bodycontract.seriaNum activityId =activityId/>
<#include "template/body_contratDetail.ftl" />
<!--施工确认 -->
<div id="usertask4" class="usertask4" style="display: none;">
	<div class="p20bs mt10 color-white-bg border-ec">
		<H3 class="text-xl title-box">
			<A class="black" href="#">施工确认</A>
		</H3>
		<BR>
		<TABLE class="ui-table ui-table-gray">
			<TBODY>
				<TR>
					<TH width="20%">签收时间</TH>
					<TD colspan=2 style="border-radius: 0 0 0"><#setting
						date_format="yyyy-MM-dd HH:mm:ss"> ${claimTime!''}</TD>
				</TR>
				<TR>
					<TH>意见</TH>
					<TD colspan=2><textarea name="shigongComments" id="shigongComments"
							style="margin: 5px 0; width: 400px; margin-top: 5px;">已按施工单要求施工完毕</textarea></TD>
				</TR>
		</TABLE>
		<div style="margin: 10px 0 0; text-align: center;">
			<button onclick="shigong();" class="block-btn">提交确认结果</button>
		</div>

	</div>
</div>
</#if> 
<#include "template/hisDetail.ftl" />
</@frame.html>