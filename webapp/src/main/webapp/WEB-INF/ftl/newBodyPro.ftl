<#import "template/template.ftl" as frame>
<#global menu="车身套餐定义">
<@frame.html title="车身套餐定义" js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<script type="text/javascript">
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
								url : "${rc.contextPath}/product/ajax-BusOrderDetailV2",
								data : function(d) {
									return $.extend({}, d, {
										"seriaNum" : '${seriaNum}'
									});
								},
								 "dataSrc": "content",
							},
							"columns" : [
                { "data": "leval", "defaultContent": "", "render": function(data, type, row,meta) {
                           if(data=='S'){
                              return  '特级';
                            }else if(data=='APP'){
                               return 'A++';
                            }else if(data=='AP'){
                               return 'A+';
                            }else if(data=='A'){
                               return 'A';
                                 }else{
                                    return '经纬线';
                                 }
                               }},  
                { "data": "doubleDecker", "defaultContent": "", "render": function(data, type, row,meta) {
                           if(data==false){
                              return  ' 单层';
                            }else{
                               return ' 双层';
                                 }
                               }}, 
                { "data": "busNumber", "defaultContent": 0}, 
                { "data": "days", "defaultContent": 0}, 
                { "data": "price", "defaultContent": 0}, 
				{ "data": function( row, type, set, meta) {
                                                  return row.id;
                                              },
										"render" : function(data, type, row,
												meta) {
											var operations = '';
											operations += '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/product/ajax-remove-busOrderDetail?id=' + data +'">删除</a>';
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

</script>
<script type="text/javascript">
  function sub(){
	if (!$("#form2").validationEngine('validateBeforeSubmit'))
            return;
	   $('#form2').ajaxForm(function(data) {
		  if(data.left){
		      document.getElementById('subutton').setAttribute('disabled',true);
		       $("#subutton").css("background-color","#85A2AD");
		      layer.msg(data.right);
		      var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/product/productV2_list";
			   	clearTimeout(uptime);
						},2000)
		     }else{
		     layer.msg(data.right);
		     }
		}).submit();
	}
  function addBusOrderDetail() {
      if (!$("#form1").validationEngine('validateBeforeSubmit'))
            return;
		$('#form1').ajaxForm(function(data) {
		if(data.left){
		     layer.msg("添加成功");
		       orderBusesTable.dataTable()._fnAjaxUpdate();
		       $("#sumprice").val(data.right);
		     }else{
		     layer.msg("操作失败");
		     }
		}).submit();
	}
    $(document).ready(function() {
      refreshOrderedBuses();
        $("#form1").validationEngine({
            validationEventTrigger:"blur",  //触发的事件  validationEventTriggers:"keyup blur",
            inlineValidation: true,//是否即时验证，false为提交表单时验证,默认true
            success :  false,//为true时即使有不符合的也提交表单,false表示只有全部通过验证了才能提交表单,默认false
            promptPosition: "centerRight",//提示所在的位置，topLeft, topRight, bottomLeft,  centerRight, bottomRight
            showOneMessage: true,
            maxErrorsPerField: 1,
            //failure : function() { alert("验证失败，请检查。");  }//验证失败时调用的函数
            //success : function() { callSuccessFunction() },//验证通过时调用的函数
        });
    });
</script>
<div id="relateSup">
		<div class="p20bs mt10 color-white-bg border-ec">
				<div class="withdraw-title fn-clear">
									添加套餐方案
								</div>
				<form data-name="withdraw" name="form1" id="form1"
					class="ui-form" method="post"
					action="${rc.contextPath}/product/saveBusOrderDetail"
					enctype="multipart/form-data">
					<input type="hidden" name="seriaNum" id="seriaNum" value="${seriaNum}"/>
					<div class="withdrawInputs">
						<div class="inputs">
						<div class="ui-form-item ">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>线路级别：</label>
                                            <select class="ui-input" name="leval" id="leval">
                                                <#if lineLevels??>
                                                <#list lineLevels as level>
                                                    <option value="${level.name()}">${level.nameStr}</option>
                                                </#list>
                                                </#if>
                                            </select>
                                        </div>
                                        <div class="ui-form-item ">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>车型:</label>
                                           <select class="ui-input" name="doubleDecker" id="lineLevel">
                                                    <option value="false">单层</option>
                                                    <option value="true">双层</option>
                                            </select>
                                        </div>
                                        <div class="ui-form-item">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>巴士数量:</label>
                                            <input
                                                    class="ui-input validate[required,number,min[1]"
                                                    type="number" value="" name="busNumber"
                                                    id="busNumber" data-is="isAmount isEnough"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
                                        <div class="ui-form-item toggle bodyToggle">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>刊期:</label>
                                            <select class="ui-input" name="days" >
                                                    <option value="30">30</option>
                                                    <option value="60">60</option>
                                                    <option value="90">90</option>
                                                    <option value="180">180</option>
                                                    <option value="360">360</option>
                                            </select>
                                        </div>
									</div>
									<div class="ui-form-item widthdrawBtBox">
										<input type="button" id="submit" class="block-btn"
											onclick="addBusOrderDetail();" value="添加">
									</div>
									</form>
				</div>
				</div>
				</div>
			<div class="p20bs mt10 color-white-bg border-ec">
				<div class="withdraw-title">
					<span>套餐详细</span>
				</div>
			   <div id="orderedBuses">
				<table id="table" class="display compact"
					cellspacing="0" width="70%">
					<thead>
				<tr class="tableTr">
					<th>线路级别</th>
					<th>车型</th>
					<th>车辆数</th>
					<th>刊期</th>
					<th>金额</th>
					<th>操作</th>
				</tr>
					</thead>
				</table>
						</div>
						</div>
						<div class="withdraw-wrap color-white-bg fn-clear" style="margin-top: 10px;">
			<form data-name="withdraw" name="form2" id="form2"
								class="ui-form" method="post" action="saveProductV2"
								enctype="multipart/form-data">
								<div class="withdrawInputs">
								    <div class="ui-form-item toggle bodyToggle">
                                            <label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>套餐价格:</label>
                                           
                                                  <input  type="text" value="0" name="price"
                                                    id="sumprice" data-is="isAmount isEnough" readonly="readonly"
                                                    autocomplete="off" disableautocomplete="">
                                        </div>
                                        <input type="hidden" name="seriaNum" id="seriaNum" value="${seriaNum}"/>
                                        <div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>套餐名称：</label> <input
												class="ui-input validate[required]"
                                                type="text" value="" name="name"
												id="name" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="" placeholder="2-20个字符">
										</div>
                                       <div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>套餐描述：</label> 
											<textarea rows="4" class=" validate[required]" cols="40" style="resize: none;" name="remarks"></textarea>
										</div>
									</div>
									
									</div>
									<div class="ui-form-item widthdrawBtBox" style="padding: 20px 0px 0px 0px; text-align: center;">
										<input type="button" id="subutton" class="block-btn" 
											onclick="sub();" value="生成套餐">
									</div>
								</div>
							</form>
</div>

</@frame.html>
