<#import "template/template_blank.ftl" as frame>
<#global menu="巴士列表">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="巴士列表" js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js", "js/datepicker.js","js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css"]>

<style type="text/css">
    .center {margin: auto;}
    .frame {width: 1000px;}
    .div {text-align:center; margin:25px;}
    div#toolbar {float: left;}
    div#toolbar3 {float: left;}
    .processed {color: limegreen;}
    .invalid {color: red;}
    .hl {background-color: #ffff00;}
</style>

<script type="text/javascript">
	
    var table;
    var orderBusesTable;
    
    function refreshOrderedBuses() {
		orderBusesTable = $('#lineTable')
				.dataTable(
						{
							"dom" : '<"#toolbar2">t',
							"searching" : false,
							"ordering" : false,
							"serverSide" : false,
							"ajax" : {
								type : "GET",
								url : "${rc.contextPath}/busselect/ajax-publishLinebyId",
								data : function(d) {
									return $.extend({}, d, {
										"seriaNum" : '${seriaNum}',
										"plid":'${plid}'
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
                	{ "data": "batch", "defaultContent": ""}, 
                { "data": "salesNumber", "defaultContent": ""}, 
                 { "data": "remainNuber", "defaultContent": "","render": function(data, type, row, meta) {
                	 
                	return "<font color='red'>"+data+"</font>";
                }},
                { "data": "days", "defaultContent": 0}, 
                { "data": function( row, type, set, meta) {
                    return row.id;
                },"render" : function(data, type, row,meta) {
                if(null!=row.startDate && ""!=row.endDate ){
					return  $.format.date(row.startDate, "yyyy-MM-dd");
                }else{
                   return '';
                }
										}
									},
								
									 ],
							"language" : {
								"url" : "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
							},
							"initComplete" : initComplete2,
							"drawCallback" : drawCallback2,
						});
		orderBusesTable.fnNameOrdering("orderBy").fnNoColumnsParams();
	}
	
	
	function drawCallback2() {
		$('.table-action').click(function() {
			$.post($(this).attr("url"), function(data) {
			if(data){
				 orderBusesTable.dataTable()._fnAjaxUpdate();
				 }else{
				 alert("操作失败");
				 }
			})
		});
	}
	function initComplete2() {
		$("div#toolbar").attr("style", "width: 100%;")
		$("div#toolbar").html('');
	}
	
	
	
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "scrollX": true,
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-findBusByLineid",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[plateNumber]" : $('#name').val(),
                        "filter[category]" : $('#category').val(),
                        "filter[levelStr]" : $('#levelStr').val(),
                          "filter[company]" : $('#company').val(),
                        "filter[lineid]" : ${jpaPublishLine.line.id}
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": function( row, type, set, meta) {
                                                  return row.id;
                                              },
										"render" : function(data, type, row,
												meta) {
											var operations = '<input type="checkbox" name="checkone" value="'+row.jpaBus.id+'" />';
											return operations;
										}
									},
                { "data": "jpaBus.plateNumber", "defaultContent": "","render": function(data, type, row, meta) {
                	//return '<a class="table-link" href="${rc.contextPath}/bus/busOnline_history/' + row.jpaBus.id +'" >'+data+'</a>';
                	return '<a  onclick="showbusOnline_history(\'${rc.contextPath}\','+row.jpaBus.id+');">'+data+'</a>';
                }},
                { "data": "jpaBus.serialNumber", "defaultContent": ""},
                { "data": "jpaBus.oldSerialNumber", "defaultContent": ""},
                { "data": "jpaBus.model.name", "defaultContent": ""},
                { "data": "jpaBus.line.name", "defaultContent": ""},
                { "data": "jpaBus.line.levelStr", "defaultContent": ""},
                { "data": "jpaBus.categoryStr", "defaultContent": ""},
                { "data": "jpaBus.company.name", "defaultContent": ""},
                { "data": "busInfo.contractCode", "defaultContent": ""},
                { "data": "busInfo.startD", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                { "data": "busInfo.endD", "defaultContent": "","render": function(data, type, row, meta) {
              	  var d= $.format.date(data, "yyyy-MM-dd");
              	  if(d!=null){
	               	  var start=new Date(d.replace("-", "/").replace("-", "/")); 
	              		  if(start >= new Date()){
	                		d="<font color='red'>"+d+"</font>";
	                	}else {
	                		d="<font color='green'>"+d+"</font>";
	                	}
                	}
                	return d;
                }},
                 { "data": "jpaBus.description", "defaultContent": ""},
                 { "data": "jpaBus.branch", "defaultContent": ""},
	              { "data": "", "defaultContent": "","render": function(data, type, row, meta) {
	              
	              		var tString ='';
	              		if(row.busInfo.stats == 'now' || row.busInfo.stats == 'future'){
	              			tString = 	 '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/bus/offlineContract/' + (row.busInfo.busOnline.id) +"/"+($("#plid").val())+ '">撤销</a> &nbsp;'
	              		}
	                	return tString;
	                }
                }
                
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
          //  "fnDrawCallback": fnDrawCallback,
             
        } );
        table.fnNameOrdering("orderBy").fnNoColumnsParams();
    }

    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>车牌号</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>线路级别</span>&nbsp;&nbsp;' +
                       '<select class="ui-input ui-input-mini" name="levelStr" id="levelStr" style="width:125px">' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	'<option value="S">特级</option>' +
                  	'<option value="APP">A++</option>' +
                  	'<option value="AP">A+</option>' +
                  	'<option value="A">A</option>' +
                  	'<option value="LATLONG">经纬线</option>' +
         			'</select>&nbsp;&nbsp;' +
                        '    <span>车辆类别</span>&nbsp;&nbsp;' +
                       '<select class="ui-input ui-input-mini" name="category" id="category" style="width:125px">' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	'<option value="baoche">包车</option>' +
                  	'<option value="banche">班车</option>' +
                  	'<option value="jidongche">机动车</option>' +
                  	'<option value="yunyingche">运营车</option>' +
         			'</select>' +
         			'    <span>营销中心</span>&nbsp;&nbsp;' +
                    '<select class="ui-input ui-input-mini" name="company" id="company">' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	    <#list companys as c>
					'<option value="${c.id}">${c.name}</option>'+
					    </#list>
         			'</select>' +
                    '<br></div>'
        );//companys

        $('#name,#category,#levelStr,#company').change(function() {
            table.fnDraw();
        });
    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
                orderBusesTable.dataTable()._fnAjaxUpdate();
            })
        });
    }
     //显示总条数 add by impanxh
    function fnDrawCallback(){
		var record_count = (this.fnSettings().fnRecordsTotal() );
		
		
		//alert(record_count);
		//alert($(this).attr('numberOfElements'));
    }
     var buslogtable;
    function initTable2 () {
        buslogtable = $('#busUpHis').dataTable( {
            "dom": '<"#toolbar3">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "scrollX": true,
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-busUpdate_history",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[plid]" : ${plid}
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "bus.plateNumber", "defaultContent": ""},
                { "data": "bus.serialNumber", "defaultContent": ""},
                { "data": "bus.oldSerialNumber", "defaultContent": ""},
                { "data": "model.name", "defaultContent": ""},
                { "data": "line.name", "defaultContent": ""},
                { "data": "line.levelStr", "defaultContent": ""},
                { "data": "busCategory", "defaultContent": ""},
                { "data": "company.name", "defaultContent": ""},
                { "data": "bus.description", "defaultContent": ""},
                { "data": "bus.office", "defaultContent": ""},
                { "data": "bus.branch", "defaultContent": ""},
                { "data": "busUpLog.updated", "defaultContent": "","render" : function(data, type, row,meta) {
					return  $.format.date(data, "yyyy-MM-dd");
										}
									},
                { "data": "busUpLog.updator", "defaultContent": ""},
              
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete3,
            "drawCallback": drawCallback3,
        } );
        table.fnNameOrdering("orderBy").fnNoColumnsParams();
    }

    function initComplete3() {
        //$("div#toolbar").html('');
    }

    function drawCallback3() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }
 function sub(){
            var stday=$("#stday").val();
            var days=$("#days").val();
            var contractid=${jpaPublishLine.contractId};
            if(stday==""){
            layer.msg("请选择上刊日期");
            return;
            }
            if(days==""){
            layer.msg("请输入刊期");
            return;
            }
        	var o = document.getElementsByName("checkone");
        	var dIds='';
        	for(var i=0;i<o.length;i++){
                if(o[i].checked)
                dIds+=o[i].value+',';   
           }
           if(dIds==""){
        	   layer.msg("请选择需要上刊的车辆");
        	   return false;
           }
   		var param={"ids":dIds,"days":days,"stday":stday,"contractid":contractid,"plid":$("#plid").val()};
    	
    	 

		layer.confirm('确定上刊吗？', {icon: 3}, function(index){
    		layer.close(index);
    		
		    if(true){
		    	 $.ajax({
		    			url:"${rc.contextPath}/bus/batchOnline",
		    			type:"POST",
		    			async:false,
		    			dataType:"json",
		    			data:param,
		    			success:function(data){
		    				if (data.left == true) {
		    					layer.msg(data.right);
		    					 table.dataTable()._fnAjaxUpdate();
		    					  orderBusesTable.dataTable()._fnAjaxUpdate();
		    					  buslogtable.dataTable()._fnAjaxUpdate();
		    				} else {
		    					layer.msg(data.right,{icon: 5});
		    				}
		    			}
		       });  
		       }
		});		

    
        }
        
    $(document).ready(function() {
  	   refreshOrderedBuses();
        initTable();
        initTable2();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
  			 <div id="orderedBuses">
				<table id="lineTable" class="display compact"
					cellspacing="0" width="100%">
					<thead>
				<tr class="tableTr">
					<th>发布形式</th>
					<th>线路</th>
					<th>级别</th>
					   <th>批次</th>
                    <th>数量（辆）</th>
                    <th>已装数量</th>
                    <th>刊期(天)</th>
                    <th>发布时间</th>
				</tr>
					</thead>
				</table>
			 </div>
			  </div><br><br>
<div class="withdraw-wrap color-white-bg fn-clear">

			<!--over-->
            <div class="withdraw-title">
                 <div class="report-toolbar">
                 <input type="hidden" id ="plid" value="${plid}"/>
                           上刊日期：<input  class="ui-input ui-input-mini datepicker" type="text" name="stday"
                            id="stday" data-is="isAmount isEnough"
                            autocomplete="off" disableautocomplete=""> 
                           &nbsp;&nbsp; 刊期(天)：<input  class="ui-input"  type="text" 
                            id="days" data-is="isAmount isEnough"  onkeyup="value=value.replace(/[^\d]/g,'')"
                            autocomplete="off" disableautocomplete=""> 
                           &nbsp;&nbsp; <input type="button" class="button_kind" style="width: 85px;height: 30px;"
			                    value="批量上刊" onclick="sub()"/>
                </div>
	     	</div>
	     	
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
                        <th > <input type="checkbox" name="checkAll" /></th>
                        <th >车牌号</th>
                        <th >新车号</th>
                        <th >旧车号</th>
                        <th >车型</th>
                        <th orderBy="line.name">线路</th>
                        <th orderBy="line.level">线路级别</th>
                        <th orderBy="category">类别</th>
                        <th orderBy="company">营销中心</th>
                        <th>合同编号</th>
                        <th>上刊日期</th>
                        <th>下刊日期</th>
                        <th>车辆描述</th>
                        <th>客户名称</th>
                        <th>撤销上刊</th>
                    </tr>
                    </thead>
                </table>
                <div><span id="trw2">ss</span></div>
                
</div>
<br>
<div class="withdraw-wrap color-white-bg fn-clear">
 <div class="withdraw-title">
             已上刊车辆历史变更信息
                </div>
                <table id="busUpHis" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
                        <th >车牌号</th>
                        <th >车辆自编号</th>
                        <th>旧自编号</th>
                        <th >车型</th>
                        <th >线路</th>
                        <th >线路级别</th>
                        <th >类别</th>
                        <th >营销中心</th>
                         <th>车辆描述</th>
                        <th>公司名称</th>
                        <th>客户名称</th>
                        <th>最后更新时间</th>
                        <th>操作人</th>
                       
                    </tr>
                    </thead>

                </table>
</div>
<script type="text/javascript">
		function queryTotalInfo(){
        	var param={"publish_line_id":$("#plid").val()};
      		   $.ajax({
		    			url:"${rc.contextPath}/bus/ajax-daysNumber",
		    			type:"POST",
		    			async:false,
		    			dataType:"json",
		    			data:param,
		    			success:function(data){
		    				if (data != null) {
		    				var t="线路车辆总数: "+data.totalBus +" 当日已上刊总数:"+data.dayOnlieBus;
		    					$("#trw").html(t);
		    					$("#trw2").html(t);
		    				}  
		    			}
		       });  
        }
 $(document).ready(function() {
        queryTotalInfo();
    } );
</script>
</@frame.html>