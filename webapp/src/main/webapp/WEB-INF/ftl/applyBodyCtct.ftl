<#import "template/template.ftl" as frame> <@frame.html title=""
js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

<script type="text/javascript">
	var orderBusesTable;
	function refreshOrderedBuses() {
		orderBusesTable = $('#orderedBusesTable')
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
			    { "data": "line.name", "defaultContent": ""},
                { "data": "remainNuber", "defaultContent": ""}, 
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

											operations += '<a class="table-action" href="javascript:void(0);">删除</a>';

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
				orderBusesTable.fnDraw(true);
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
				alert("库存量为"+data+",请重新输入车辆数量");
				  return;
				}else{
				  bb=true;
				}
			}
		});
		alert(bb);
	if(bb==true){
		$('#form01').ajaxForm(function(data) {
		if(data.left){
		     alert("添加成功");
		       refreshOrderedBuses();
		     }else{
		     alert(data.right);
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
							+ '<div class="ui-form-item">'
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
	});
</script>
<div class="color-white-bg fn-clear">

	<div id="relateSup">
		<div class="p20bs mt10 color-white-bg border-ec">
			<H3 class=".withdraw-title text-xl title-box">
				<A class="black" href="#">选取车辆</A>
				 <input type="hidden" name="seriaNum" id="seriaNum" value="${seriaNum}"/>
			</H3>
			<span><input type="button" onclick="selctLine('${rc.contextPath}',${seriaNum})"
				class="block-btn" value="增加选择"
				style="float: right; margin: 10px 20px 0px 20px;"></span> <br>
			<div id="orderedBuses">
				<table id="orderedBusesTable" class="display compact"
					cellspacing="0" width="100%">
					<thead>
						<tr>
					<th>线路</th>
                    <th>数量（辆）</th>
                     <th>上刊时间</th>
                    <th>下刊时间</th>
                    <th>操作</th>
						</tr>
					</thead>

				</table>
			</div>

			<div id="orderBusesPopup" title="选择车辆"></div>
		</div>

		<div class="p20bs mt10 color-white-bg border-ec">
		
				<div class="withdraw-title fn-clear">
									合同详情录入
								</div>

		

		
				<form data-name="withdraw" name="userForm2" id="userForm2"
					class="ui-form" method="post"
					action="${rc.contextPath}/contract/saveContract?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1"
					enctype="multipart/form-data">
					<div class="withdrawInputs">
						<div class="inputs">
							<div class="ui-form-item">
								<label class="ui-label mt10"> <span
									class="ui-form-required">*</span>合同编号:
								</label> <input
									class="ui-input validate[required,custom[noSpecialContratNum],minSize[5],maxSize[120]]"
									type="text" name="contractCode" id="code"
									value="${(contractView.mainView.contractCode)!''}"
									data-is="isAmount isEnough" autocomplete="off"
									disableautocomplete="" placeholder="中英文、数字、下划线">
							</div>
							<div class="ui-form-item">
								<label class="ui-label mt10"><span
									class="ui-form-required">*</span>合同名称:</label> <input
									class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
									type="text" name="contractName"
									value="${(contractView.mainView.contractName)!''}" id="name"
									data-is="isAmount isEnough" autocomplete="off"
									disableautocomplete="">
								<p class="ui-term-placeholder" placeholder="中英文、数字、下划线"></p>

							</div>
						</div>
					</div>

				</form>
		
			
			<div id="tb2">

				<p style="text-align: center; margin-top: 10px;">
					<button type="button" id="subsupbutton2" onclick="relatSup()"
						class="block-btn">确认</button>
					<br> <br />
				</p>
			</div>
		</div>
		<br>
	</div>
</div>

</@frame.html>






