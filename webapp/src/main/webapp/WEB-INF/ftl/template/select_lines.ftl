<#macro select_lines title="选取车辆" buyLink=false>
<script type="text/javascript">
	function gotoSchedult(id,modelId){
	  window.open("${rc.contextPath}/busselect/lineschedule/"+id+"?modelId="+modelId);
	 }
	
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
										"seriaNum" : '${seriaNum}'
									});
								},
								 "dataSrc": function(json) {return json;},
							},
							"columns" : [
			    { "data": "line.name", "defaultContent": "", "render": function(data, type, row, meta) {
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
											operations += '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/busselect/ajax-remove-buslock?seriaNum=${seriaNum}&id=' + data +'">删除</a>';
											return operations;

										}
									}, ],
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
	}
	
	function initComplete() {
		$("div#toolbar").attr("style", "width: 100%;")
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

		<div class="p20bs mt10 color-white-bg border-ec">
				<div class="withdraw-title">
					<span>选取车辆</span>
					<input type="hidden" name="seriaNum" id="seriaNum" value="${seriaNum}"/>
				<a class="block-btn" style="margin-top: -5px;" href="javascript:void(0);" onclick="selctLine('${rc.contextPath}',${seriaNum})">增加选择</a>
					
				</div>
			
			<div id="orderedBuses">
				<table id="table" class="display compact"
					cellspacing="0" width="100%">
					<thead>
						<tr class="tableTr">
					<th>线路</th>
                    <th>数量（辆）</th>
                    <th width="180px">车型</th>
                     <th>上刊时间</th>
                    <th>下刊时间</th>
                    <th>操作</th>
						</tr>
					</thead>

				</table>
			</div>

			<div id="orderBusesPopup" title="选择车辆"></div>
		</div>
</#macro>
