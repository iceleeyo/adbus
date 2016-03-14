<#import "template/template.ftl" as frame> <#global menu="创建合同">
<@frame.html title="创建合同"
js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["css/sift.css","js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
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
								url : "${bodyOnlineUrl}/busselect/ajax-publishLine",
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
                 { "data": "startDate","defaultContent": "","render" : function(data, type, row,meta) {
					return  $.format.date(data, "yyyy-MM-dd");
										}
									},
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
								url : "${bodyOnlineUrl}/busselect/ajax-getDividPay",
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
	  function updateStateToRemote(){
	  var param={"id":${helpid} , stat : "contractd","creater":"<@security.authentication property="principal.user.id"/>"};
		$.ajax({
			url : "${rc.contextPath}/carbox/editCarHelper",
			type : "GET",
			data:param,
			success : function(data) {
			},complete: function(XMLHttpRequest, textStatus) {
				if(XMLHttpRequest.status!=200){
					//layer.msg("连接电商平台网络错误,加载广告主信息失败!", {icon: 5});
				}
  		    }		
		}, "text");
	}
	
	function SupContract(){
	if (!$("#form02").validationEngine('validateBeforeSubmit'))
            return;
	   $('#form02').ajaxForm(function(data) {
		  if(data.left){
		  	  updateStateToRemote();
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
<!---begin order --->


<script type="text/javascript">
	
    var orderTable;
    function initOrderTable () {
        orderTable = $('#orderTable').dataTable( {
            "dom" : '<"#toolbar">t',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "scrollX": true,
               "aaSorting": [[5, "desc"]],
            "columnDefs": [
                { "orderable": false, "targets": [0,1,2,3] },
            ],
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/carbox/ajax-queryCarBoxBody",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[helpid]" : ${helpid}
                    } );
                },
                "dataSrc": "content",
                 "complete": function(jqXHR, textStatus)
					      {
					      alertCompleteMsg(jqXHR.status);
					},
            },
            "columns": [
                { "data": "product.jpaProductV2.name", "defaultContent": ""},
                { "data": "product.leval", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data == 'S')
                            return '特级';
                        if (data == 'APP')
                            return 'A++';
                        if (data == 'AP')
                            return 'A+';
                        if (data == 'A')
                            return 'A';
                        return '其它';
                    } },
                { "data": " product.doubleDecker", "defaultContent": "","render": function(data, type, row, meta) {
                	if(data==true){
                	 return '双层';
                	}else{
                	   return '单层';
                	}
                }},
                 { "data": "needCount", "defaultContent": ""},
                 { "data": "days", "defaultContent": ""},
                 { "data": "totalprice", "defaultContent": ""},
              
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initOrderComplete,
            "drawCallback": drawOrderCallback,
        } );
       // table.fnNameOrdering("orderBy").fnNoColumnsParams();
    }

    function initOrderComplete() {
        //$("div#toolbar").html('');
    }

    function drawOrderCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                orderTable.fnDraw(true);
            })
        });
    }
    
    function loadUserInfo(){
		$.ajax({
			url : "${rc.contextPath}/carbox/queryCarHelperyByid/${helpid}",
			type : "GET",
			success : function(data) {
				 if (typeof(data.userJson) != "undefined" && data.userJson!='' ) {
					 var dataset = $.parseJSON(data.userJson );
					 $("#phone").val(dataset.phone);
					 $("#relateman").val(dataset.relateman);
					 $("#company").val(dataset.company);
					 $("#companyAddr").val(dataset.companyAddr);
					 $("#zipcode").val(dataset.zipCode);
				 }
			},complete: function(XMLHttpRequest, textStatus) {
				if(XMLHttpRequest.status!=200){
					//layer.msg("连接电商平台网络错误,加载广告主信息失败!", {icon: 5});
				}
  		    }		
		}, "text");
	}
    $(document).ready(function() {
        initOrderTable();
        loadUserInfo();
    } );
</script>

<script type="text/javascript">
	
    var historyTable;
    function initHistoryTable () {
        historyTable = $('#history').dataTable( {
            "dom" : '<"#toolbar">t',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "scrollX": true,
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "POST",
                url: "${rc.contextPath}/carbox/ajax-bodyOrderLog",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[helpId]" : ${helpid}
                    } );
                },
                "dataSrc": function(json) {return json;},
                 "complete": function(jqXHR, textStatus)
					      {
					    //  alertCompleteMsg(jqXHR.status);
					},
            },
            "columns": [
                  { "data": "created","defaultContent": "","render" : function(data, type, row,meta) {
					return  $.format.date(data, "yyyy-MM-dd");
										}
									},
                 { "data": "userid", "defaultContent": ""},
                 { "data": "stats", "defaultContent": ""},
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
        } );
    }

     

    
     
    $(document).ready(function() {
        initHistoryTable();
    } );
</script>

<div id="relateSup">

<div class="p20bs mt10 color-white-bg border-ec">
		<div class="withdraw-title">
			<span>（一）订单产品信息</span>  
				<a class="block-btn"
				style="margin-top: -5px; margin-left: 5px;"
				href="javascript:void(0);"
				onclick="lastPage()">返回</a> 
				  <#--<a
				class="block-btn" style="margin-top: -5px;"
				href="javascript:void(0);"
				onclick="addPublishLine('${rc.contextPath}',${seriaNum})">发布线路</a>-->
		</div>
		<div id="orderFromSite">
			<table id="orderTable" class="display compact" cellspacing="0" width="100%">
				<thead>
					<tr class="tableTr">
						<th>产品名称</th>
						<th>级别</th>
						<th>单双层</th>
						<th>车辆数</th>
						<th>刊期(天)</th>
						<th>价格</th>
					</tr>
				</thead>
		</table>
		</div>
	</div>
	<div id="relateSup">
		<div class="p20bs mt10 color-white-bg border-ec">
			<div class="withdraw-title fn-clear">（二）下单用户信息</div>
			 
				
				<div class="withdrawInputs">
					<div class="inputs">
						<div class="ui-form-item">
							<label class="ui-label mt10">广告主: </label> <input
								class="ui-input"
								type="text" name="signDate1" id="relateman"
								value="" />
						</div>
						<div class="ui-form-item">
							<label class="ui-label mt10"> 相关公司:
							</label> <input class="ui-input" type="text"
								name="contractCode" value="" id="company"
							 />
						</div>
						<div class="ui-form-item">
							<label class="ui-label mt10">公司地址:</label> <input
								class="ui-input" type="text" name="companyAddr"
								value="" id="companyAddr"
								 />
						</div>
						<div class="ui-form-item">
							<label class="ui-label mt10">联系电话:</label> <input
								class="ui-input" type="text" name="companyAddr"
								value="" id="phone"
								 />
						</div>
						<div class="ui-form-item">
							<label class="ui-label mt10">区号:</label> <input
								class="ui-input validate[required]" type="text" name="company"
								value="" id="zipcode"
								 />
						</div>
					  
					</div>
				</div>
		</div>
	<!-- over 1-->
	<div class="p20bs mt10 color-white-bg border-ec">
		<div class="withdraw-title">
			<span>（三）发布线路信息</span>  
				  
		</div>
		<div id="orderedBuses">
			<table id="table" class="display compact" cellspacing="0" width="100%">
				<thead>
					<tr class="tableTr">
						<th>批次</th>
						<th>线路</th>
						<th>级别</th>
						<th>媒体类型</th>
						<th>刊期（天）</th>
						<th>预计上刊</th>
						<th>发布形式</th>
						<th>订购数量</th>
						<th>已上刊数量</th>
						<th>备注</th>
						<th>创建时间</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="relateSup">
		<div class="p20bs mt10 color-white-bg border-ec">
			<div class="withdraw-title fn-clear">（四）合同基本信息</div>
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
								style="width: 220px; height: 35px;">
								<option value="自营">自营</option>
								<option value="CBS" selected="selected">CBS</option>
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
								value="${(offlinecontract.signDate?string("yyyy-MM-dd"))!''}"
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
								style="width: 220px; height: 35px;">
								<option value="810">810</option>
								<option value="910">910</option>
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
					<span>（五）合同付款方式</span> 
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
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
		<div id="relateSup">
			<div class="p20bs mt10 color-white-bg border-ec">
				<div class="withdraw-title">
					<span>（六）订单历史</span>  
				</div>
				<div >
					<table id="history" class="display compact" cellspacing="0"
						>
						<thead>
							<tr class="tableTr">
								<th>日期</th>
								<th>用户</th>
								<th>状态</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
		</form>
		
	</div>
	<br>
</div>
</div>
</@frame.html>






