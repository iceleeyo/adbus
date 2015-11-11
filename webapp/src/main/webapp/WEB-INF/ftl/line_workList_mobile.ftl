<#import "template/template_mobile.ftl" as frame> <#import
"template/orderDetail.ftl" as orderDetail/> <#assign
security=JspTaglibs["/WEB-INF/tlds/security.tld"] /> <#global
menu="合同线路施工单" > <@frame.html title="线路施工单"
js=["js/nano.js","js/jquery-dateFormat.js","js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js","js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]
>

<style type="text/css">
.center {
	margin: auto;
}

.div {
	text-align: center;
}

div#toolbar {
	float: left;
}

.div2 {
	text-align: center;
}

.div5 {
	margin: 5px;
}

div2#toolbar2 {
	float: left;
}

#toolbar2 {
	margin-bottom: 20px;
}

.processed {
	color: limegreen;
}

.invalid {
	color: red;
}

.hl {
	background-color: #ffff00;
}

#table {
	font-size: 14px;
}

#table td,#table2 td {
	position: relative;
	line-height: 30px;
}

#table td .per-middle {
	position: absolute;
	background-color: #ffad20;
	top: 0;
	height: 100%;
	z-index: 1
}

#table td .per-first-or-last {
	position: absolute;
	background-color: #4acd48;
	border-left: 1px solid white;
	border-right: 1px solid white;
	top: 0;
	height: 100%;
	z-index: 1;
}

#table th,#table2 th,#his_table th {
	background-color: #3bafda;
	color: white;
	font-weight: normal;
	text-align: left;
	line-height: 30px;
	padding: 2px 2px;
}

table.dataTable.compact tbody td {
	padding: 2px 2px;
}
</style>

<script type="text/javascript">
	  function gotoSchedult(id,modelId){
	  window.open("${rc.contextPath}/busselect/lineschedule/"+id+"?modelId="+modelId);
	  }
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
	 function XYDetail(data){
	layer.open({
    		type: 1,
    		title: "施工照片",
    		skin: 'layui-layer-rim', 
    		area: ['100px', '79px'], 
    		content:''
			   +' '
			   +'<iframe  style="width:99%;height:99%" frameborder="no" src="${rc.contextPath}/supplies/XYDetail/'+data+'/workp"/>'
			});
}
	//上传施工照片
function uploadWorkP() {
$("#wpform").ajaxForm(function(data) {
	 $("#uploadWP").attr("disabled",true);
     $("#uploadWP").css("background-color","#85A2AD");
     $("#cc").trigger("click");
      layer.msg(data.right);
		table2.dataTable()._fnAjaxUpdate();
	    table.dataTable()._fnAjaxUpdate();
	}).submit();
}
	function to_confirm(bid,lid) {
	var url  = "${rc.contextPath}/busselect/toconfirm_bus/"+bid+"/"+lid;
	$.post(url,{},function(data){
    	 var tjson= {"staTime":$.format.date(data.buslock.startDate, "yyyy-MM-dd"),"creTime":$.format.date(data.busContract.created, "yyyy-MM-dd"),"endTime":$.format.date(data.buslock.endDate, "yyyy-MM-dd")};
		var innerHTMLString = nano2($("#ccc").html(),data,tjson);
			layer.open({
                type: 1,
                title: "确认实际上下刊日期",
                skin: 'layui-layer-rim', 
                area: ['450px', '600px'], 
                content: innerHTMLString+'<div class="withdrawInputs">'
	                        +' <div class="inputs"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/><div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">实际上刊日期:</label>'
							+'<input class="ui-input datepicker validate[required,custom[date],past[#endDate]]" type="text" name="startD" value="" id="startT" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
							+'</div><div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">实际下刊日期:</label>'
							+'<input class="ui-input datepicker validate[required,custom[date],past[#endDate]]" type="text" name="endD" value="" id="endT" data-is="isAmount isEnough" autocomplete="off" disableautocomplete="">'
							+'</div><div class="ui-form-item widthdrawBtBox"> <input type="button" id="uploadbutton" class="block-btn" onclick="confirm('+bid+','+lid+');" value="确认"> </div></div></div>'
            });
            var checkin = $('#startT').datepicker()
			.on('click', function (ev) {
			        $('.datepicker').css("z-index", "999999999");
			}).data('datepicker');
			
			var checkin = $('#endT').datepicker()
			.on('click', function (ev) {
			        $('.datepicker').css("z-index", "999999999");
			}).data('datepicker');
    });
	}
	function confirm(bid,lid) {
	var starT=$("#startT").val().trim();
	var endT=$("#endT").val().trim();
	if(starT==""){
	  layer.msg("请选择实际上刊日期");
	  return;
	}
	if(endT==""){
	  layer.msg("请选择实际下刊日期");
	  return;
	}
	if(starT>endT){
	  layer.msg("下刊日期不能小于上刊日期");
	  return;
	}
	   var url  = "${rc.contextPath}/busselect/confirm_bus/"+bid+"/"+lid;
	   var param={"startDate" : starT,
				   "endDate" : endT};
	  $.post(url,param,function(data){
	            layer.msg(data.right);
	            $("#cc").trigger("click");
			     table2.dataTable()._fnAjaxUpdate();
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
                "sEmptyTable": "表中无数据存在！",
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
                    var i=0;
                    <@security.authorize ifAnyGranted="bodyFinancialManager,bodyContractManager,bodyScheduleManager">
	                    i++;
	                     return '<a   onclick=" gotoSchedult(' + row.bus.lineId +","+(row.bus.modelId )+ ')" >'+data+'</a> &nbsp;';
                    </@security.authorize>
                    if(i==0){
                     	return data;
                    }
                    }
                } 
                 <@security.authorize ifAnyGranted="bodyFinancialManager,bodyContractManager,bodyScheduleManager">
                ,{
                    "data" : "busContract.startDate", "defaultContent": "", "render" : function(data, type, row, meta) {
                   var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                    }
                },{
                    "data" : "busContract.endDate", "defaultContent": "", "render" : function(data, type, row, meta) {
                    var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                    }
                } </@security.authorize>
                ,{
                    "data" : "busContract.created", "defaultContent": "", "render" : function(data, type, row, meta) {
                    var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                    }
                },{
                    "data" : "busContract.userid", "defaultContent": "", "render" : function(data, type, row, meta) {
                  
                	     return row.busContract.enable==false?'未确认':'已确认';
                    }
                },
                <@security.authorize ifAnyGranted="bodyScheduleManager">{
                    "data" : "line.name", "defaultContent": "", "render" : function(data, type, row, meta) {
                      var op='<a  onclick="XYDetail('+row.busContract.id+');">施工照片</a> &nbsp;';
                        if(row.busContract.enable==false){
	                      op+= '<a   onclick="to_confirm(' +(row.busContract.id ) +","+(row.line.id )+ ')" >确认</a> &nbsp;';
                        }else{
                        op+= '<a   onclick="to_confirm(' +(row.busContract.id ) +","+(row.line.id )+ ')" >修改</a> &nbsp;';
                       }
                         return op;
                    }
                } </@security.authorize>
                
 
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete2,
            "drawCallback": drawCallback2,
            "fnDrawCallback": fnDrawCallback2,
        } );
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
                "sEmptyTable": "表中无数据存在！",
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
                } ,   <@security.authorize ifNotGranted="bodyFinancialManager,bodyContractManager">  {
                    "data" : "line.name", "defaultContent": "", "render" : function(data, type, row, meta) {
	                    var w ='<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/online/${id}/'+row.bus.id+'">完成安装</a> &nbsp;';
	                      return '<a   onclick="uploadWorkPhoto(\'${rc.contextPath}\',' +${id} +","+(row.bus.id )+ ')" >安装操作</a> &nbsp;';
                    }
                }  </@security.authorize>
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
   

    
</script>




<H3 class="text-xl title-box">
	<A class="black" href="#">已安装车辆 <span id="recordsTotal2"
		style="background-color: red; font-size: 14px; border-radius: 4px;"></span></A>
</H3>
<div class="div5" style="overflow-x: auto;" id="div2">
	<table id="table2" class="cell-border compact display" cellspacing="0"
		width="100%">
		<thead>
			<tr>
				<th>自编号</th>
				<th>车牌号</th>
				<th>线路</th> <@security.authorize
				ifAnyGranted="bodyFinancialManager,bodyContractManager,bodyScheduleManager">
				<th>上刊时间</th>
				<th>下刊时间</th> </@security.authorize>
				<th>施工时间</th>
				<th>状态</th> <@security.authorize ifAnyGranted="bodyScheduleManager">
				<th>操作</th> </@security.authorize>
			</tr>
		</thead>
	</table>
</div>




<H3 class="text-xl title-box">
	<A class="black" href="#">可安装车辆列表 <span id="loading"><image
				src="${rc.contextPath}/imgs/load_.gif" /> </span> <span id="recordsTotal"
		style="background-color: red; font-size: 14px; border-radius: 4px;"></span></A>
</H3>
<div class="div5" style="overflow-x: auto;" id="div1">
	<table id="table" class="cell-border compact display" cellspacing="0"
		width="100%">
		<thead>
			<tr>
				<th>自编号</th>

				<th>车牌号</th>
				<th>线路</th> <@security.authorize
				ifNotGranted="bodyFinancialManager,bodyContractManager">
				<th>安装</th> </@security.authorize>

			</tr>
		</thead>
	</table>
</div>


<div class="wormB-tips">
	<div class="tips-title">
		<span class="icon"></span>排期说明：
		<ol>
			<li>1.列表显示的是该合同可以施工的所有车辆列表</li>
		</ol>
	</div>
</div>
<div id="ccc" style="display: none">
	<div class="withdrawInputs">
		<div class="inputs">
			<div class="ui-form-item">
				<label class="ui-labels mt10"> 自编号 </label> <input class="ui-input"
					type="text" readonly="readonly" autocomplete="off"
					value="{bus.serialNumber}">
			</div>
			<div class="ui-form-item">
				<label class="ui-labels mt10"> 车牌号 </label> <input class="ui-input"
					type="text" readonly="readonly" autocomplete="off"
					value="{bus.plateNumber}">
			</div>

			<div class="ui-form-item">
				<label class="ui-labels mt10"> 线路 </label> <input class="ui-input"
					type="text" readonly="readonly" autocomplete="off"
					value="{line.name}">
			</div>

			<div class="ui-form-item">
				<label class="ui-labels mt10"> 预上刊时间 </label> <input
					class="ui-input" type="text" readonly="readonly" autocomplete="off"
					value="{staTime}">
			</div>

			<div class="ui-form-item">
				<label class="ui-labels mt10"> 预下刊时间 </label> <input
					class="ui-input" type="text" readonly="readonly" autocomplete="off"
					value="{endTime}">
			</div>

			<div class="ui-form-item">
				<label class="ui-labels mt10"> 施工时间 </label> <input class="ui-input"
					type="text" readonly="readonly" autocomplete="off"
					value="{creTime}">
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
 $(document).ready(function() {
        initTable();
        initTable2();
    } );
    </script>
</@frame.html>
