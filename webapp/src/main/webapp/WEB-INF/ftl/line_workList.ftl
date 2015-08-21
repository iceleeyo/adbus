<#import "template/template_blank.ftl" as frame>
<#import "template/orderDetail.ftl" as orderDetail/>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<#global menu="合同线路施工单" >
<@frame.html title="合同线路施工单"   js=["js/jquery-dateFormat.js"]>

<style type="text/css">
    .center {margin: auto;}
    .frame {width: 1000px;}
    .div {text-align:center; margin:25px;}
    div#toolbar {float: left;}
    
       .div2 {text-align:center; margin:25px;}
    div2#toolbar2 {float: left;}
    
    .processed {color: limegreen;}
    .invalid {color: red;}
    .hl {background-color: #ffff00;}
    #table {font-size: 12px;}
    #table td {position:relative;}
    #table td .per-middle {position:absolute;background-color: #ffad20;top:0;height:100%;z-index:1}
    #table td .per-first-or-last {position:absolute;background-color: #4acd48;border-left: 1px solid white;border-right: 1px solid white;top:0;height:100%;z-index:1}
	#table th, #his_table th{background-color: #3bafda;color: white;  font-weight: normal;  text-align: left;  line-height: 20px;  padding: 2px 2px;}
	table.dataTable.compact tbody td { padding: 2px 2px;}
</style>
 
<script type="text/javascript">

	var table;
	var table2;
	
	function setBusOnline(bid,busid) {
		layer.confirm('请确认此车辆已安装？', {
	    btn: ['确认','取消'], //按钮
	    shade: false //不显示遮罩
		}, function(){
		   $.ajax({
				url : "${rc.contextPath}/busselect/work_online/"+bid+"/"+busid,
				type : "POST",
				data : {
				},
				success : function(data) {
			     layer.msg(data.right);
			     table2.dataTable()._fnAjaxUpdate();
			     table.dataTable()._fnAjaxUpdate();
				}
			}, "text");
		}, function(){
		});
	}
	 function initTable2 () {
        $('#metatable').dataTable({
            "dom": 'rt',
            "searching": false,
            "ordering": false,
            "serverSide": false,
        });

        table2 = $('#table2').dataTable( {
		   /*  oLanguage: {
		        sProcessing: "<img src='${rc.contextPath}/imgs/load_.gif'>"
		    },*/
            "oLanguage": {
                "sSearch": "Search all columns:",
                "sLoadingRecords": "Please wait - loading...",
                "sProcessing": "正在加载中",
            },
		    processing : true,
            "dom": '<"#toolbar2">rt',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "ajax": {
                type: "POST",
                url: "${rc.contextPath}/busselect/work_done",
                data: function(d) {
              	  var _modelId=  $('#taskKey2').val();
              	  var _lineId=   $('#taskKey2').find("option:selected").attr("lid");
              	  if (typeof(_lineId) == "undefined"){
	                  _lineId = ${lineId};
                  }
	              if (typeof(_modelId) == "undefined"){
	                  _modelId = ${modelId};
                  }
                    return $.extend( {}, d, {
                        "filter[lineId]" : _lineId,
                        "filter[modelId]" : _modelId,
                        "filter[bodycontract_id]" : "${id}",
                        
                    } );
                },
                    "dataSrc": "content",
            },
            "columns": [
                {
                    "data" : "serialNumber", "defaultContent": "", "render" : function(data, type, row, meta) {
                    return data;
                    }
                },
                {
                    "data" : "bus.plateNumber", "defaultContent": "", "render" : function(data, type, row, meta) {
                    return data;
                    }
                },{
                    "data" : "line.name", "defaultContent": "", "render" : function(data, type, row, meta) {
                    return data;
                    }
                } 
                 <@security.authorize ifAnyGranted="bodyFinancialManager,bodyContractManager,bodyScheduleManager">
                ,{
                    "data" : "busContract.startDate", "defaultContent": "", "render" : function(data, type, row, meta) {
                   var d= $.format.date(data, "yyyy-MM-dd HH:mm");
                	return d;
                    }
                },{
                    "data" : "busContract.endDate", "defaultContent": "", "render" : function(data, type, row, meta) {
                    var d= $.format.date(data, "yyyy-MM-dd HH:mm");
                	return d;
                    }
                } </@security.authorize>
                ,{
                    "data" : "busContract.created", "defaultContent": "", "render" : function(data, type, row, meta) {
                    var d= $.format.date(data, "yyyy-MM-dd HH:mm");
                	return d;
                    }
                    
                }
                ,{
                    "data" : "line.name", "defaultContent": "", "render" : function(data, type, row, meta) {
	                   return '<a   onclick="confirmDate(' +${id} +","+(row.line.id )+ ')" >确认</a> &nbsp;';
                 }
                }
 
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete2,
            "drawCallback": drawCallback2,
            "fnDrawCallback": fnDrawCallback2,
        } );
    }


//提交施工单
function subForm() {
    if (!$("#userForm1").validationEngine('validateBeforeSubmit'))
        return;
	
	$('#userForm1').ajaxForm(function(data) {
	//	jDialog.Alert();
	 $("#uploadbutton").attr("disabled",true);
     $("#uploadbutton").css("background-color","#85A2AD");
       layer.msg(data.right);
		var uptime = window.setTimeout(function(){
		$("#cc").trigger("click");
		clearTimeout(uptime);
		},3000)
	}).submit();
	 
}

    function initComplete2() {
       $("div#toolbar2").html(
	                '<div>' +
	                         '<select class="ui-input ui-input-mini" style="width:250px" name="taskKey2" id="taskKey2">' +
	                     <#list lockList as item>
	                  		'<option value="${item.model.id}" lid="${item.line.id}" <#if item.line.id==lineId>selected="selected"</#if>  >${item.line.name}'+
	                  		'<#if item.model.id==0> 所有车型 <#else>[${item.model.name}<#if item.model.doubleDecker>双层<#else>单层</#if>] </#if>'+
	                  		'</option>' +
	                  	 </#list>
	         			'</select>' +
	                    '</div>'
	        );
	        $('#taskKey2').change(function() {
	            table2.fnDraw();
	        });
    }

    function drawCallback2() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table2.fnDraw(true);
            })
        });
    }
      //显示总条数 add by impanxh
    function fnDrawCallback2(){
    	$("#loading").hide();
    	 bindLayerMouseOver();
		var record_count = (this.fnSettings().fnRecordsTotal() );
		if(record_count>0){
	 	  $("#recordsTotal2").html("&nbsp;"+record_count+"&nbsp;");
		  }
    }
	
	
    
    function initTable () {
        $('#metatable').dataTable({
            "dom": 'rt',
            "searching": false,
            "ordering": false,
            "serverSide": false,
        });

        table = $('#table').dataTable( {
		   /*  oLanguage: {
		        sProcessing: "<img src='${rc.contextPath}/imgs/load_.gif'>"
		    },*/
            "oLanguage": {
                "sSearch": "Search all columns:",
                "sLoadingRecords": "Please wait - loading...",
                "sProcessing": "正在加载中",
            },
		    processing : true,
            "dom": '<"#toolbar1">rt',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "ajax": {
                type: "POST",
                url: "${rc.contextPath}/busselect/work_note",
                data: function(d) {
              	  var _modelId=  $('#taskKey').val();
              	  var _lineId=   $('#taskKey').find("option:selected").attr("lid");
              	  if (typeof(_lineId) == "undefined"){
	                  _lineId = ${lineId};
                  }
	              if (typeof(_modelId) == "undefined"){
	                  _modelId = ${modelId};
                  }
                    return $.extend( {}, d, {
                        "filter[lineId]" : _lineId,
                        "filter[modelId]" : _modelId,
                        "filter[bodycontract_id]" : "${id}",
                        
                    } );
                },
                    "dataSrc": "content",
            },
            "columns": [
                {
                    "data" : "serialNumber", "defaultContent": "", "render" : function(data, type, row, meta) {
                    return data;
                    }
                },{
                    "data" : "bus.plateNumber", "defaultContent": "", "render" : function(data, type, row, meta) {
                    return data;
                    }
                },{
                    "data" : "line.name", "defaultContent": "", "render" : function(data, type, row, meta) {
                    return data;
                    }
                } ,{
                    "data" : "line.name", "defaultContent": "", "render" : function(data, type, row, meta) {
	                    var w ='<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/online/${id}/'+row.bus.id+'">完成安装</a> &nbsp;';
	                      return '<a   onclick="setBusOnline(' +${id} +","+(row.bus.id )+ ')" >完成安装</a> &nbsp;';
                    }
                } 
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
            "fnDrawCallback": fnDrawCallback,
        } );
    }

    function initComplete() {
       $("div#toolbar1").html(
	                '<div>' +
	                         '<select class="ui-input ui-input-mini" style="width:250px"  name="taskKey" id="taskKey">' +
	                     <#list lockList as item>
	                  		'<option value="${item.model.id}" lid="${item.line.id}" <#if item.line.id==lineId>selected="selected"</#if>  >${item.line.name}'+
	                  		'<#if item.model.id==0> 所有车型 <#else>[${item.model.name}<#if item.model.doubleDecker>双层<#else>单层</#if>] </#if>'+
	                  		'(${item.salesNumber})</option>' +
	                  	 </#list>
	         			'</select>' +
	                    '</div><br>'
	        );
	        $('#taskKey').change(function() {
	            table.fnDraw();
	        });
    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }
      //显示总条数 add by impanxh
    function fnDrawCallback(){
    	$("#loading").hide();
    	 bindLayerMouseOver();
		var record_count = (this.fnSettings().fnRecordsTotal() );
		if(record_count>0){
	 	  $("#recordsTotal").html("&nbsp;"+record_count+"&nbsp;");
		  }
    }
   
function confirmDate(bid,lid){
alert(lid);
	$.ajax({
		url :  "${rc.contextPath}/busselect/toconfirm_bus/"+bid+"/"+lid,
		type : "POST",
		data : {
		},
		success : function(data) {
		     alert(data.bus);
		  }
		}, "text");
}
    
</script>



<div class="p20bs mt10 withdraw-wrap color-white-bg fn-clear" style="margin-left: -150px;margin-right: 30px">
    <H3 class="text-xl title-box"><A class="black" href="#">已安装车辆
    <span id="recordsTotal2" style="background-color:#ff9966;font-size: 14px;border-radius: 4px;"></span></A></H3>
             <div class="div2" style="overflow-x:auto;" id = "div2">
                <table id="table2" class="cell-border compact display" cellspacing="0" width="60%">
                    <thead>
                    <tr>
                        <th style="min-width:110px;">车辆自编号</th>
                         <th style="min-width:110px;">车牌号</th>
                          <th style="min-width:110px;">线路名称</th>
                             <@security.authorize ifAnyGranted="bodyFinancialManager,bodyContractManager,bodyScheduleManager">
                          <th style="min-width:110px;">上刊时间</th>
                          <th style="min-width:110px;">下刊时间</th>
                            </@security.authorize>
                           <th style="min-width:110px;">施工时间</th>
                           <th style="min-width:60px;">实际时间</th>
                    </tr>
                    </thead>
                </table>
            </div>
            
</div>



<div class="p20bs mt10 withdraw-wrap color-white-bg fn-clear" style="margin-left: -150px;margin-right: 30px">
    <H3 class="text-xl title-box"><A class="black" href="#">可安装车辆列表
    <span id="loading"><image src="${rc.contextPath}/imgs/load_.gif"/> </span>
    <span id="recordsTotal" style="background-color:#ff9966;font-size: 14px;border-radius: 4px;"></span></A></H3>
            <div class="div" style="overflow-x:auto;" id = "div1">
                <table id="table" class="cell-border compact display" cellspacing="0" width="60%">
                    <thead>
                    <tr>
                        <th style="min-width:110px;">车辆自编号</th>
                        
                         <th style="min-width:110px;">车牌号</th>
                          <th style="min-width:110px;">线路名称</th>
                           <th style="min-width:110px;">安装完成</th>
                       
                    </tr>
                    </thead>
                </table>
            </div>
            
            
            <div class="worm-tips">
		<div class="tips-title">
			<span class="icon"></span>排期说明：
			<ol>
			<li>1.列表显示的是该合同可以施工的所有车辆列表</li>
		</ol>
		</div>
		</div>
<div id ="ccc" style="display:none" >

<form id="userForm1" name="userForm1" action="${rc.contextPath}/busselect/confirm_bus/bid/lid" enctype="multipart/form-data" method="post"">
<br/><br/>
		<input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/>
<div class="withdrawInputs">
	<div class="inputs">
		<div class="ui-form-item"> 
			<label class="ui-labels mt10">
				车辆自编号
			</label>
				<input class="ui-input" type="text" name="duration" id="duration" readonly="readonly"  autocomplete="off" value="{serialNumber}">
		</div>
		
		<div class="ui-form-item">
			<label class="ui-labels mt10">
				车牌号
			</label>
				<input class="ui-input" type="text" name="duration" id="duration" readonly="readonly"  autocomplete="off" value="{bus.plateNumber}">
		</div>

		<div class="ui-form-item">
			<label class="ui-labels mt10">
				线路名称
			</label>
				<input class="ui-input" type="text" name="duration" id="duration" readonly="readonly"  autocomplete="off" value="{line.name}">
		</div>
		
		<div class="ui-form-item">
			<label class="ui-labels mt10">
				预上刊时间
			</label>
				<input class="ui-input" type="text" name="duration" id="duration" readonly="readonly"  autocomplete="off" value="{staTime}">
		</div>
		
		<div class="ui-form-item">
			<label class="ui-labels mt10">
				预下刊时间
			</label>
				<input class="ui-input" type="text" name="duration" id="duration" readonly="readonly"  autocomplete="off" value="{endTime}">
		</div>
		
		<div class="ui-form-item">
			<label class="ui-labels mt10">
				施工时间
			</label>
				<input class="ui-input" type="text" name="duration" id="duration" readonly="readonly"  autocomplete="off" value="busContract.created">
		</div>
		
		<div class="ui-form-item">
			<label class="ui-labels mt10">
				实际上刊时间
			</label>
				<input class="ui-input datepicker validate[required,custom[date] layer-tips"  type="text" name="startDate"
               	id="startDate" data-is="isAmount isEnough"
                autocomplete="off" disableautocomplete="">
		</div>
		
		<div class="ui-form-item">
			<label class="ui-labels mt10">
				实际下刊时间
			</label>
				<input class="ui-input datepicker validate[required,custom[date] layer-tips"  type="text" name="endDate"
               	id="endDate" data-is="isAmount isEnough"
                autocomplete="off" disableautocomplete="">
		</div>
		 <div class="ui-form-item widthdrawBtBox"> <input type="button" id="uploadbutton" class="block-btn" onclick="subForm();" value="确认"> </div>
	</div>
</div>
</form>

</div>
</div>
<script type="text/javascript">
 $(document).ready(function() {
        initTable();
        initTable2();
    } );
    </script>
</@frame.html>