<#import "template/template.ftl" as frame> <#global menu="创建合同">
<@frame.html title="创建合同"
js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<style type="text/css">
.tableTr {
	width: 100%;
	height: 40px;
}
</style>
<script type="text/javascript">
	function gotoSchedult(id,modelId){
	  window.open("${rc.contextPath}/busselect/lineschedule/"+id+"?modelId="+modelId);
	  }
	  
	var orderBusesTable;
	var orderBusesTable2;
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
								url : "${rc.contextPath}/busselect/ajax-publishLine",
								data : function(d) {
									return $.extend({}, d, {
										"seriaNum" : '${seriaNum}'
									});
								},
								 "dataSrc": function(json) {return json;},
							},
							"columns" : [
				{ "data": "batch", "defaultContent": ""},
			    { "data": "line.name", "defaultContent": "", "render": function(data, type, row, meta) {
                      return '<a   onclick=" gotoSchedult(' + row.line.id +","+(row.model.id )+ ')" >'+data+'</a> &nbsp;';
                }},
                { "data": "line.levelStr", "defaultContent": ""}, 
                { "data": "mediaType", "defaultContent": ""},
                { "data": "days", "defaultContent": 0}, 
                { "data": "lineDesc", "defaultContent": ""}, 
                { "data": "salesNumber", "defaultContent": ""}, 
                { "data": "remainNuber", "defaultContent": "","render": function(data, type, row, meta) {
                	 
                	return "<font color='red'>"+data+"</font>";
                }}, 
                { "data": "remarks", "defaultContent": ""}, 
                { "data": "created","render" : function(data, type, row,meta) {
					return  $.format.date(data, "yyyy-MM-dd");
										}
									},
									{ "data": function( row, type, set, meta) {
                                                  return row.id;
                                              },
										"render" : function(data, type, row,
												meta) {
											var operations = '';
											operations += '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/busselect/ajax-remove-publishLine?seriaNum=${seriaNum}&id=' + data +'">删除</a>';
											operations +='&nbsp;&nbsp;<a class="table-link" onclick="editPublishLine(\'${rc.contextPath}\','+data+');" href="javascript:void(0)">修改</a>';
											//operations +='&nbsp;&nbsp;<a class="table-link" onclick="publishAmount(\'${rc.contextPath}\','+data+');" href="javascript:void(0)">发布费详情</a>';
											//if(row.offlineContract!=null && row.offlineContract.id!=null && row.offlineContract.id>0){
										    //operations +='&nbsp;&nbsp;<a class="table-link" target="_blank" href="${rc.contextPath}/bus/findBusByLineid/'+data+'">车辆上刊</a>';
											//}
											return operations;
										}
									},
									 ],
							"language" : {
								"url" : "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
							},
							"initComplete" : initComplete,
							"drawCallback" : drawCallback,
						});
		orderBusesTable.fnNameOrdering("orderBy").fnNoColumnsParams();
	}
	function refreshOrderedBuses2() {
		orderBusesTable2 = $('#table2')
				.dataTable(
						{
							"dom" : '<"#toolbar">t',
							"searching" : false,
							"ordering" : false,
							"serverSide" : false,
							"ajax" : {
								type : "GET",
								url : "${rc.contextPath}/busselect/ajax-getDividPay",
								data : function(d) {
									return $.extend({}, d, {
										"seriaNum" : '${seriaNum}'
									});
								},
								 "dataSrc": function(json) {return json;},
							},
							"columns" : [
									{ "data": "name", "defaultContent": ""}, 
									{ "data": "amounts", "defaultContent": ""}, 
									{ "data": "payDate", "defaultContent": "", "render": function(data) {
                                              return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                                         } },
                                         { "data": "remarks", "defaultContent": ""}, 
                                         { "data": function( row, type, set, meta) {
                                                  return row.id;
                                              },
										"render" : function(data, type, row,
												meta) {
											var operations = '';
											operations += '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/busselect/ajax-remove-dividPay?seriaNum=${seriaNum}&id=' + data +'">删除</a>';
											operations +='&nbsp;&nbsp;<a class="table-link" onclick="editDividPay(\'${rc.contextPath}\','+data+');" href="javascript:void(0)">修改</a>';
											return operations;
										}
									},
										
									 ],
							"language" : {
								"url" : "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
							},
							"initComplete" : initComplete2,
							"drawCallback" : drawCallback2,
						});
		orderBusesTable2.fnNameOrdering("orderBy").fnNoColumnsParams();
	}

	function drawCallback() {
		$('.table-action').click(function() {
			$.post($(this).attr("url"), function(data) {
			if(data.left){
			    layer.msg(data.right);
				 orderBusesTable.dataTable()._fnAjaxUpdate();
				 }else{
				  layer.msg(data.right);
				 }
			})
		});
	}
	function initComplete() {
		$("div#toolbar").attr("style", "width: 100%;")
		$("div#toolbar").html('');
	}
	function drawCallback2() {
		$('.table-action').click(function() {
			$.post($(this).attr("url"), function(data) {
			if(data){
				 orderBusesTable2.dataTable()._fnAjaxUpdate();
				 }else{
				 alert("操作失败");
				 }
			})
		});
	}
	function initComplete2() {
		$("div#toolbar").attr("style", "width: 70%;")
		$("div#toolbar").html('');
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
	 layer.msg("请选上刊日期");
	   return;
	   }
	if(endDate==""){
	 layer.msg("请选择下刊日期");
	   return;
	   }
	if($("#busNumber").val()<0){
	 layer.msg("车辆数量要大于0");
	   return;
	   }
	if($("#days").val()<0){
	 layer.msg("刊期要大于0");
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
				"days" : $("#days").val(),
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
	
	function changeModel(){
	
		var t=$("#model_id").children('option:selected').val();
		$.ajax({
			url : "${rc.contextPath}/busselect/ajax-des",
			type : "POST",
			data : {
				"buslinId" : $("#db_id").val(),
				"modelid":t
			},
			success : function(data) {
				var v='';
				//$("#desId").html(v);
				$.each(data, function(i, item) {
				var w="<option value="+item+">"
									+ "&nbsp;&nbsp;" + item 
									+ "</option>";
									v+=w;
				});
				if(v==''){
					$("#desId").html('<option value="暂无描述" selected="selected">暂无描述</option>');
				}else {
					$("#desId").html(v);
				}
			}
		}, "text");
		
		
		$.ajax({
			url : "${rc.contextPath}/busselect/ajax-company",
			type : "POST",
			data : {
				"buslinId" : $("#db_id").val(),
				"modelid":t
			},
			success : function(data) {
				var v='';
				$.each(data, function(i, item) {
				var w="<option value="+item.id+">"
									+ "&nbsp;&nbsp;" + item.name
									+ "</option>";
					v+=(w);
				});
					$("#companyId").html(v);
			}
		}, "text");
	
	}
	function changeModel2(sot){
		$.ajax({
			url : "${rc.contextPath}/busselect/ajax-des",
			type : "POST",
			data : {
				"buslinId" : $("#db_id_"+sot).val(),
				"modelid":0
			},
			success : function(data) {
				var v='';
				$.each(data, function(i, item) {
				var w="<option value="+item+">"
									+ "&nbsp;&nbsp;" + item 
									+ "</option>";
									v+=w;
				});
				if(v==''){
					$("#desId_"+sot).html('<option value="暂无描述" selected="selected">暂无描述</option>');
				}else {
					$("#desId_"+sot).html(v);
				}
			}
		}, "text");
		
	}
	
	function addBatch(url,seriaNum) {
		layer.open({
					type : 1,
					title : "发布线路",
					skin : 'layui-layer-rim',
					area : [ '950px', '500px' ],
					content : ''
							+ '<br><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><table border="1px #ooo" id="tab" style="  width: 97%;margin-left:20px;" cellpadding="0"   cellspacing="0" width="90%">'
                            +' <tr align="center">'
                            +' <td style="width:10%"><input id="allCkb" type="checkbox"/></td><td >批次</td> <td >线路</td><td  >媒体类型</td> <td style="width:10%">刊期</td><td >发布形式</td><td >级别</td><td >数量</td><td >备注</td>'
                            +' </tr>'
                            +' <tr align="center">'
                            +' <td  width="40px"><input  type="checkbox" id="ckb" name="ckb" value="1"/></td>'
                            +' <td ><select  id="batch_1"> <option value="1" selected="selected">1</option> <option value="2" >2</option><option value="3" >3</option><option value="4" >4</option></select></td> '
                            +'<td ><input style="width:75px" class="ui-input"  id="line_id_1"  sot="1" data-is="isAmount isEnough"></td> '
                            +' <td ><select  id="isdouble_1"> <option value="单层" selected="selected">单层</option> <option value="单层市区" >单层市区</option><option value="单层郊区" >单层郊区</option><option value="双层" >双层</option></select></td> '
                            +' <td width="40px"><input class="ui-input " type="text" value="30" name="days" onkeyup="value=value.replace(/[^\\d]/g,\'\')" '
							+'id="days_1" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="" style="width:90px">'
                            +' </td>'
                            +' <td ><select  style="width:145px" class="ui-input bus-model" name="lineDesc" id="desId_1"> <option value="0" selected="selected">所有类型</option> </select></td>'
                            +'<td ><input class="ui-input"  id="levle_1" data-is="isAmount isEnough" style="width:65px"></td>'
                            +' <td ><input class="ui-input " type="text" value="0" name="salesNumber" onkeyup="value=value.replace(/[^\\d]/g,\'\')" '
							+'id="salesNumber_1" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder=""  style="width:45px">'
                            +' </td>'
                            +' <td ><input class="ui-input"  id="remarks_1" data-is="isAmount isEnough"  style="width:165px"></td>'
                            +' <input type="hidden" value="0" name="lineId" id="db_id_1"  style="width:90px"> </tr> '
                            +'</table>'
							+'<br> <input type="button" class="block-btn" style="margin-left:20px;" onclick="addTr2(\'tab\', -1)" value="添加一行">&nbsp;'
                            +'<input type="button" class="block-btn" onclick="delTr2()" value="删除"><p></p><p align="center"><input type="button" class="block-btn" onclick="batchOff()" value="确定"></p>'
				});
			
		$("#line_id_1").autocomplete({
		minLength: 0,
			source : "${rc.contextPath}/busselect/autoComplete?tag=reLevel",
			change : function(event, ui) {
			},
			select : function(event, ui) {
			   var sot=$(this).attr("sot");
				$('#line_id_'+sot).val(ui.item.value);
				$("#db_id_"+sot).val(ui.item.dbId);
				$("#levle_"+sot).val(ui.item.levelStr);
				changeModel2(sot);
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   				 });
   				 $("#allCkb").click(function(){
   				 if($(this).attr("checked") == "checked"){
   				  $("input[name='ckb']").attr("checked",true);
   				 }else{
   				  $("input[name='ckb']").attr("checked",false);
   				 }
              });
   				 
	}
	
	 function batchOff(){
        	var o = document.getElementsByName("ckb");
        	var dIds='';
        	for(var i=0;i<o.length;i++){
                if(o[i].checked)
                dIds+=o[i].value+',';   
           }
           if(dIds==""){
        	   layer.msg("请选择需要添加的数据");
        	   return false;
           }
        	for(var i=0;i<o.length;i++){
                if(o[i].checked){
                var obj=new Object(); 
                var sot=o[i].value;
                obj.batch=$("#batch_"+sot).val();
                obj.lineId=$("#db_id_"+sot).val();
                obj.mediaType=$("#isdouble_"+sot).val();
                obj.days=$("#days_"+sot).val();
                obj.lineDesc=$("#desId_"+sot).val();
                obj.salesNumber=$("#salesNumber_"+sot).val();
                obj.remarks=$("#remarks_"+sot).val();
                 obj.seriaNum=${seriaNum};
                if($("#db_id_"+sot).val()>0){
                  saveOne(obj);
                 }
                }
           }
             orderBusesTable.dataTable()._fnAjaxUpdate();
		       $("#cc").trigger("click");
        }
   function saveOne(obj){
     var str = JSON.stringify(obj);  
     var param={"obj":str}
      $.ajax({
		    			url:"${rc.contextPath}/busselect/savePublishLine2",
		    			type:"POST",
		    			async:false,
		    			dataType:"json",
		    			data:param,
		    			success:function(data){
		    			}
		       });  
   }
  function delTr(ckb){
     //获取选中的复选框，然后循环遍历删除
     var ckbs=$("input[name="+ckb+"]:checked");
     if(ckbs.size()==0){
        alert("要删除指定行，需选中要删除的行！");
        return;
     }
           ckbs.each(function(){
              $(this).parent().parent().remove();
           });
  }
  function allCheck(allCkb, items){
   $("#"+allCkb).click(function(){
      $('[name="ckb"]:checkbox').attr("checked", checked );
   });
  }
    $(function(){
  // allCheck("allCkb", "ckb");
  });
  var i=2;
  function addTr2(tab, row){
    var trHtml='<tr align="center"><td width="10%"><input type="checkbox" value="'+i+'" id="ckb" name="ckb"/></td>'
   +' <td ><select  id="batch_'+i+'"> <option value="1" selected="selected">1</option> <option value="2" >2</option><option value="3" >3</option><option value="4" >4</option></select></td> '
   +' <td ><input class="ui-input"  style="width:75px" sot="'+i+'" id="line_id_'+i+'"/></td>'
   +' <td ><select  id="isdouble_'+i+'"> <option value="单层" selected="selected">单层</option> <option value="单层市区" >单层市区</option><option value="单层郊区" >单层郊区</option><option value="双层" >双层</option></select></td> '
   +' <td ><input class="ui-input " style="width:90px" type="text" value="30" name="days" onkeyup="value=value.replace(/[^\\d]/g,\'\')" id="days_'+i+'"/></td>'
   +' <td ><select style="width:145px" class="ui-input bus-model" name="lineDesc" id="desId_'+i+'"> <option value="0" selected="selected">所有类型</option> </select></td>'
   +' <td ><input class="ui-input"  id="levle_'+i+'" data-is="isAmount isEnough" style="width:65px"></td>'
   +' <td ><input class="ui-input " type="text" value="0" style="width:45px" name="salesNumber" onkeyup="value=value.replace(/[^\\d]/g,\'\')" id="salesNumber_'+i+'"/></td>'
   +' <td ><input class="ui-input"  id="remarks_'+i+'"   style="width:165px" data-is="isAmount isEnough"></td><input type="hidden" style="width:90px" value="0" name="lineId" id="db_id_'+i+'"></tr>';
    addTr(tab, row, trHtml);
   i++;
  }
   function addTr(tab, row, trHtml){
     //获取table最后一行 $("#tab tr:last")
     //获取table第一行 $("#tab tr").eq(0)
     //获取table倒数第二行 $("#tab tr").eq(-2)
     var $tr=$("#"+tab+" tr").eq(row);
     if($tr.size()==0){
        alert("指定的table id或行数不存在！");
        return;
     }
     $tr.after(trHtml);
      $("#line_id_"+i).autocomplete({
		minLength: 0,
			source : "${rc.contextPath}/busselect/autoComplete?tag=reLevel",
			change : function(event, ui) {
			},
			select : function(event, ui) {
			    var sot=$(this).attr("sot");
				$('#line_id_'+sot).val(ui.item.value);
				$("#db_id_"+sot).val(ui.item.dbId);
				$("#levle_"+sot).val(ui.item.levelStr);
				changeModel2(sot);
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   				 });
  }
  function delTr2(){
     delTr('ckb');
  }
</script>
<script type="text/javascript">
	$(document).ready(function() {
	var adway='${(offlinecontract.adway)!''}';
    var markcenter='${(offlinecontract.markcenter)!''}';
    $("#adway option").each(function(){
     if($(this).val() == adway){
     $(this).attr("selected", "selected");  
      }
    });
    $("#markcenter option").each(function(){
      if($(this).val()==markcenter){
       $(this).attr("selected", "selected");  
      }
    });
         refreshOrderedBuses();
         refreshOrderedBuses2();
         $("#form02").validationEngine({
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
		      layer.msg(data.right);
		      var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/busselect/offContract_list";
			   	clearTimeout(uptime);
						},2000)
		     }else{
		     layer.msg(data.right);
		     }
		}).submit();
	}
</script>
<div id="relateSup">
	<div class="p20bs mt10 color-white-bg border-ec">
		<div class="withdraw-title">
			<span>（一）发布线路信息</span> <input type="hidden" name="seriaNum"
				id="seriaNum" value="${seriaNum}" /> <a class="block-btn"
				style="margin-top: -5px; margin-left: 5px;"
				href="javascript:void(0);"
				onclick="addBatch('${rc.contextPath}',${seriaNum})">增加批次</a> <#--<a
				class="block-btn" style="margin-top: -5px;"
				href="javascript:void(0);"
				onclick="addPublishLine('${rc.contextPath}',${seriaNum})">发布线路</a>-->

		</div>
		<div id="orderedBuses">
			<table id="table" class="display compact" cellspacing="0">
				<thead>
					<tr class="tableTr">
						<th>批次</th>
						<th>线路</th>
						<th>级别</th>
						<th>媒体类型</th>
						<th>刊期（天）</th>
						<th>发布形式</th>
						<th>订购数量</th>
						<th>已上刊数量</th>
						<th>备注</th>
						<th>创建时间</th>
						<th>操作</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="relateSup">
		<div class="p20bs mt10 color-white-bg border-ec">
			<div class="withdraw-title fn-clear">（二）合同基本信息</div>
			<form data-name="withdraw" name="form02" id="form02" class="ui-form"
				method="post" action="${rc.contextPath}/busselect/saveOffContract"
				enctype="multipart/form-data">
				<input type="hidden" name="seriaNum" id="seriaNum"
					value="${seriaNum}" />
				<div class="withdrawInputs">
					<div class="inputs">
						<#if offlinecontract??> <input type="hidden" name="id"
							value="${(offlinecontract.id)!''}" /> </#if>
						<div class="ui-form-item">
							<label class="ui-label mt10"> <span
								class="ui-form-required">* </span>营销中心:
							</label> <select data-is="isAmount isEnough" name="markcenter"
								id="markcenter" autocomplete="off" disableautocomplete=""
								style="width: 173px; height: 38px;">
								<option value="自营">自营</option>
								<option value="CBS">CBS</option>
								<option value="白马">白马</option>
								<option value="七彩">七彩</option>
								<option value="市场">市场</option>
								<option value="其他">其他</option>
							</select>
						</div>
						<div class="ui-form-item">
							<label class="ui-label mt10"><span
								class="ui-form-required">*</span>签订日期: </label> <input
								class="ui-input datepicker validate[required,custom[date]]"
								type="text" name="signDate1"
								value="${(offlinecontract.signDate?string("
								yyyy-MM-dd"))!''}"
												 data-is="isAmount isEnough"
								autocomplete="off" disableautocomplete="">
						</div>
						<div class="ui-form-item">
							<label class="ui-label mt10"> <span
								class="ui-form-required">*</span>合同编号:
							</label> <input class="ui-input validate[required]" type="text"
								name="contractCode" value="${(offlinecontract.contractCode)!''}"
								data-is="isAmount isEnough" autocomplete="off"
								disableautocomplete="" placeholder="请输入合同编号">
						</div>
						<div class="ui-form-item">
							<label class="ui-label mt10"><span
								class="ui-form-required">*</span>客户名称:</label> <input
								class="ui-input validate[required]" type="text" name="company"
								value="${(offlinecontract.company)!''}" id="name"
								data-is="isAmount isEnough" autocomplete="off"
								disableautocomplete="" placeholder="请输入客户名称">
						</div>
						<div class="ui-form-item">
							<label class="ui-label mt10"><span
								class="ui-form-required"></span>联系人:</label> <input
								class="ui-input" type="text" name="relateMan"
								value="${(offlinecontract.relateMan)!''}" id="name"
								data-is="isAmount isEnough" autocomplete="off"
								disableautocomplete="" placeholder="请输入联系人，可以不填">
						</div>
						<div class="ui-form-item">
							<label class="ui-label mt10"><span
								class="ui-form-required"></span>业务员:</label> <input
								class="ui-input" type="text" name="salesman"
								value="${(offlinecontract.salesman)!''}" id="name"
								data-is="isAmount isEnough" autocomplete="off"
								disableautocomplete="" placeholder="请输入业务员名称，可以不填">
						</div>
						<div class="ui-form-item">
							<label class="ui-label mt10"> <span
								class="ui-form-required">* </span>广告形式:
							</label> <select data-is="isAmount isEnough" name="adway" id="adway"
								autocomplete="off" disableautocomplete=""
								style="width: 173px; height: 38px;">
								<option value="条幅式">条幅式</option>
								<option value="车身彩贴">车身彩贴</option>
								<option value="全车彩贴">全车彩贴</option>
							</select>
						</div>
						<div class="ui-form-item">
							<label class="ui-label mt10"> <span
								class="ui-form-required">*</span>广告内容:
							</label> <input class="ui-input validate[required]" type="text"
								name="adcontent" value="${(offlinecontract.adcontent)!''}"
								data-is="isAmount isEnough" autocomplete="off"
								disableautocomplete="" placeholder="请输入广告内容">
						</div>
						<div class="ui-form-item">
							<label class="ui-label mt10 "><span
								class="ui-form-required">*</span>发布线路:</label>
							<textarea rows="6" cols="30" data-is="isAmount isEnough"
								style="resize: none;" name="linecontent">${(offlinecontract.linecontent)!''}</textarea>
						</div>
						<div class="ui-form-item">
							<label class="ui-label mt10"> <span
								class="ui-form-required">*</span>合同金额:
							</label> <input class="ui-input validate[required]" type="text"
								name="amounts" value="${(offlinecontract.amounts)!''}"
								data-is="isAmount isEnough" autocomplete="off"
								disableautocomplete="" placeholder="输入合同金额">
						</div>
						<div class="ui-form-item">
							<label class="ui-label mt10"><span
								class="ui-form-required"></span> 付款方式: </label> <input
								class="ui-input" type="text" name="payway"
								value="${(offlinecontract.payway)!''}"
								data-is="isAmount isEnough" autocomplete="off"
								disableautocomplete="" placeholder="请输入付款方式，可以不填">
						</div>
					</div>
				</div>
		</div>
		<div id="relateSup">
			<div class="p20bs mt10 color-white-bg border-ec">
				<div class="withdraw-title">
					<span>（三）合同付款方式</span> <a class="block-btn"
						style="margin-top: -5px;" href="javascript:void(0);"
						onclick="addfenqi('${rc.contextPath}',${seriaNum})">添加分期</a>
				</div>
				<div id="orderedBuses">
					<table id="table2" class="display compact" cellspacing="0"
						width="70%">
						<thead>
							<tr class="tableTr">
								<th>期数</th>
								<th>金额</th>
								<th>付款日期</th>
								<th>备注</th>
								<th>操作</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
		</form>
		<div id="tb2">
			<p style="text-align: center; margin-top: 10px;">
				<button type="button" id="subutton" onclick="SupContract()"
					class="block-btn">保存合同</button>
				<br> <br />
			</p>
		</div>
	</div>
	<br>
</div>
</div>

</@frame.html>






