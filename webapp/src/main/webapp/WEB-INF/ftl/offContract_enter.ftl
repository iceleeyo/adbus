<#import "template/template.ftl" as frame> 
<#global menu="创建合同">
<@frame.html title="创建合同"
js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

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
							{ "data": "model", "defaultContent": "", "render": function(data) {
                           if(data.id ==0){
                                return "所有车型"
                            }else if(data.doubleDecker==false){
                              return '单层';
                            }else{
                               return '双层';
                                 }
                               }},
			    { "data": "line.name", "defaultContent": "", "render": function(data, type, row, meta) {
                      return '<a   onclick=" gotoSchedult(' + row.line.id +","+(row.model.id )+ ')" >'+data+'</a> &nbsp;';
                }},
                { "data": "line.levelStr", "defaultContent": ""}, 
                { "data": "model.name", "defaultContent": ""}, 
                { "data": "salesNumber", "defaultContent": ""}, 
                { "data": "remainNuber", "defaultContent": ""}, 
                { "data": "days", "defaultContent": 0}, 
                { "data": "startDate","render" : function(data, type, row,meta) {
					return  $.format.date(data, "yyyy-MM-dd");
										}
									},
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
											operations +='&nbsp;&nbsp;<a class="table-link" onclick="publishAmount(\'${rc.contextPath}\','+data+');" href="javascript:void(0)">发布费详情</a>';
											if(row.offlineContract.id>0){
											operations +='&nbsp;&nbsp;<a class="table-link" target="_blank" href="${rc.contextPath}/bus/findBusByLineid/'+data+'">车辆上刊</a>';
											}
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
	if($("#busNumber").val()==0){
	 layer.msg("数量要大于0");
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
	
	//编辑保存
	function editLine() {
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
			$('#publishform01').ajaxForm(function(data) {
			if(data.left){
			     layer.msg("编辑成功");
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

	function addPublishLine(url,seriaNum) {
		layer.open({
					type : 1,
					title : "选择车辆",
					skin : 'layui-layer-rim',
					area : [ '400px', '650px' ],
					content : ''
							+ '<form id="form01" action='+url+'/busselect/savePublishLine?seriaNum='+seriaNum+'>'
							+ '<div class="inputs" style="margin-top: 40px;margin-left: -30px;">'
							+ '<div class="ui-form-item"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/>'
							+ '<label class="ui-label mt10">选择线路：</label>'
							+ '<input class="ui-input"  id="line_id" data-is="isAmount isEnough">'
							+ '</div>'
							+ '<div id="four" style="display:none;"><div class="ui-form-item" id="model_Id">'
							+ '<label class="ui-label mt10">选择车型：</label>'
							+ '<select onchange="changeModel()" class="ui-input bus-model" name="modelId" id="model_id"> <option value="0" selected="selected" >所有类型</option> </select>'
							+ '</div>'
							
							+ '<div class="ui-form-item" id="model_Id2">'
							+ '<label class="ui-label mt10">车型描述：</label>'
							+ '<select  class="ui-input bus-model" name="lineDesc" id="desId"> <option value="0" selected="selected">所有类型</option> </select>'
							+ '</div>'
							
							
						    + ' <div class="ui-form-item" id="model_Id3">'
							+ '<label class="ui-label mt10">营销中心：</label>'
							+ '<select  class="ui-input bus-model" name="companyId" id="companyId"> <option value="0" selected="selected">所有类型</option> </select>'
							+ '</div>'
							
							
							+'<div class="ui-form-item"> <label class="ui-label mt10">选取数量：</label>'
							+'<input class="ui-input " type="text" value="0" name="salesNumber" onkeyup="value=value.replace(/[^\\d]/g,\'\')" '
							+'id="busNumber" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
           	 				+'</div>'
							+'<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">上刊日期:</label>'
							+'<input class="ui-input datepicker validate[required,custom[date],past[#endDate]]" type="text" name="startD" value="" id="startDate" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
							+'</div>'
							+'<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">下刊日期:</label>'
							+'<input class="ui-input datepicker validate[required,custom[date],past[#endDate]]" type="text" name="endD" value="" id="endDate" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
							+'</div>'
							+'<div class="ui-form-item"> <label class="ui-label mt10">批次：</label>'
							+'<input class="ui-input " type="text" value="0" name="batch"  '
							+'id="batch" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
           	 				+'</div>'
           	 				+'<div class="ui-form-item"> <label class="ui-label mt10">发布费单价：</label>'
							+'<input class="ui-input " type="text" value="0" name="unitPrice"  '
							+'id="unitPrice" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
           	 				+'</div>'
           	 				+'<div class="ui-form-item"> <label class="ui-label mt10">发布价值：</label>'
							+'<input class="ui-input " type="text" value="0" name="publishValue" '
							+'id="publishValue" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
           	 				+'</div>'
           	 				+'<div class="ui-form-item"> <label class="ui-label mt10">折扣率：</label>'
							+'<input class="ui-input " type="text" value="0" name="discountrate"  '
							+'id="discountrate" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
           	 				+'</div>'
           	 				+'<div class="ui-form-item"> <label class="ui-label mt10">优惠后金额：</label>'
							+'<input class="ui-input " type="text" value="0" name="discountPrice"  '
							+'id="discountPrice" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="" placeholder="">'
           	 				+'</div>'
							+ '</div></div>'
							+ '<div class="ui-form-item widthdrawBtBox" style="position: absolute; bottom: 10px;">'
							+ '<input type="button" onclick="sub()" class="block-btn" value="确认" ></div>'
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
				changeModel();
				
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   				 });
		
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
					<span>（一）发布线路信息</span>
					<input type="hidden" name="seriaNum" id="seriaNum" value="${seriaNum}"/>
				   <a class="block-btn" style="margin-top: -5px;" href="javascript:void(0);" onclick="addPublishLine('${rc.contextPath}',${seriaNum})">增加批次</a>
				</div>
			   <div id="orderedBuses">
				<table id="table" class="display compact"
					cellspacing="0" width="100%">
					<thead>
				<tr class="tableTr">
					<th>发布形式</th>
					<th>线路</th>
					<th>级别</th>
					<th>车型</th>
                    <th>订购数量</th>
                    <th>已上刊数量</th>
                    <th>刊期(天)</th>
                    <th>预计上刊时间</th>
                    <th>创建时间</th>
                    <th>操作</th>
				</tr>
					</thead>
				</table>
			 </div>
	    </div>
		<div id="relateSup">
		<div class="p20bs mt10 color-white-bg border-ec">
				<div class="withdraw-title fn-clear">
									（二）合同基本信息
								</div>
				<form data-name="withdraw" name="form02" id="form02"
					class="ui-form" method="post"
					action="${rc.contextPath}/busselect/saveOffContract"
					enctype="multipart/form-data">
					<input type="hidden" name="seriaNum" id="seriaNum" value="${seriaNum}"/>
					<div class="withdrawInputs">
						<div class="inputs">
						<#if offlinecontract??>
								  <input type="hidden" name="id" value="${(offlinecontract.id)!''}"/>
								</#if>
						<div class="ui-form-item">
                                            <label class="ui-label mt10">
											<span
                                                    class="ui-form-required">*
											</span>营销中心:
                                            </label>
                                            <select data-is="isAmount isEnough"  name="markcenter" id="markcenter"
												autocomplete="off" disableautocomplete="" style="width:173px; height: 38px;">
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
                                                    class="ui-form-required">*</span>签订日期:
															</label> <input
												class="ui-input datepicker validate[required,custom[date]]" 
												type="text" name="signDate1" value="${(offlinecontract.signDate?string("yyyy-MM-dd"))!''}"
												 data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10">
											<span class="ui-form-required">*</span>合同编号:
											</label> 
												<input class="ui-input validate[required]"
												type="text" name="contractCode"  value="${(offlinecontract.contractCode)!''}"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="" placeholder="请输入合同编号">
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>客户名称:</label>
												<input class="ui-input validate[required]"
												type="text" name="relateMan" value="${(offlinecontract.relateMan)!''}"
												id="name" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="请输入客户名称">
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>业务员:</label>
												<input class="ui-input validate[required]"
												type="text" name="salesman" value="${(offlinecontract.salesman)!''}"
												id="name" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="请输入业务员名称">
										</div>
										<div class="ui-form-item">
                                            <label class="ui-label mt10">
											<span
                                                    class="ui-form-required">*
											</span>广告形式:
                                            </label>
                                            <select data-is="isAmount isEnough"  name="adway" id="adway"
												autocomplete="off" disableautocomplete="" style="width:173px; height: 38px;">
                                            	<option value="单机">单机</option>
                                            	<option value="双层">双层</option>
                                            	<option value="其他">其他</option>
                                            </select>
                                 </div>
							<div class="ui-form-item">
								<label class="ui-label mt10"> <span
									class="ui-form-required">*</span>广告内容:
								</label> <input
									class="ui-input validate[required]"
									type="text" name="adcontent" value="${(offlinecontract.adcontent)!''}"
									data-is="isAmount isEnough" autocomplete="off"
									disableautocomplete="" placeholder="请输入广告内容">
							</div>
							<div class="ui-form-item">
                                            <label class="ui-label mt10 "><span
                                                    class="ui-form-required">*</span>发布线路:</label>
								<textarea rows="4" cols="40"  data-is="isAmount isEnough" style="resize: none;" name="linecontent">${(offlinecontract.linecontent)!''}</textarea>
                              </div>
							<div class="ui-form-item">
								<label class="ui-label mt10"> <span
									class="ui-form-required">*</span>合同金额:
								</label> <input
									class="ui-input validate[required]"
									type="text" name="amounts" value="${(offlinecontract.amounts)!''}"
									data-is="isAmount isEnough" autocomplete="off"
									disableautocomplete="" placeholder="输入合同金额">
							</div>
							<div class="ui-form-item">
								<label class="ui-label mt10"><span
									class="ui-form-required">*</span> 付款方式:
								</label> <input
									class="ui-input validate[required]"
									type="text" name="payway" value="${(offlinecontract.payway)!''}"
									data-is="isAmount isEnough" autocomplete="off"
									disableautocomplete="" placeholder="请输入付款方式">
							</div>
				</div>
				</div>
				</div>
		   <div id="relateSup">							
			<div class="p20bs mt10 color-white-bg border-ec">
				<div class="withdraw-title">
					<span>（三）合同付款方式</span>
				   <a class="block-btn" style="margin-top: -5px;" href="javascript:void(0);" onclick="addfenqi('${rc.contextPath}',${seriaNum})">添加分期</a>
				</div>
			   <div id="orderedBuses">
				<table id="table2" class="display compact"
					cellspacing="0" width="70%">
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
					<button type="button"  id="subutton" onclick="SupContract()"
						class="block-btn">保存合同</button>
					<br> <br />
				</p>
			</div>
		</div>
		<br>
	</div>
</div>

</@frame.html>






