<#import "template/template_blank.ftl" as frame>
<#global menu="车辆上刊">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="车辆上刊" js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js", "js/datepicker.js","js/jquery.datepicker.region.cn.js"]
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
		    				var t="线路车辆总数: <font color='red'>[&nbsp;&nbsp;"+data.totalBus +"&nbsp;&nbsp;]</font>当日已上刊总数:<font color='red'>[&nbsp;&nbsp;"+data.dayOnlieBus+"&nbsp;&nbsp;]</font>";
		    					$("#trw").html(t);
		    					$("#trw2").html(t);
		    				}  
		    			}
		       });  
        }
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
			    { "data": "line.name", "defaultContent": "", "render": function(data, type, row, meta) {
                      return '<a   onclick=" gotoSchedult(' + row.line.id +","+(row.model.id )+ ')" >'+data+'</a> &nbsp;';
                }},
                { "data": "line.levelStr", "defaultContent": ""}, 
                { "data": "mediaType", "defaultContent": ""},
                { "data": "lineDesc", "defaultContent": ""},
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
            "ordering": false,
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
											var operations = '<input type="checkbox" name="checkone" zbm="'+row.jpaBus.serialNumber+'" value="'+row.jpaBus.id+'" />';
											return operations;
										}
									},
									  { "data": "jpaBus.line.name", "defaultContent": ""},
                { "data": "jpaBus.line.levelStr", "defaultContent": ""},
                { "data": "jpaBus.serialNumber", "defaultContent": "","render": function(data, type, row, meta) {
                	return '<a  onclick="showbusOnline_history(\'${rc.contextPath}\','+row.jpaBus.id+');">'+data+'</a>';
                }},
                
                { "data": "jpaBus.oldSerialNumber", "defaultContent": ""},
                { "data": "jpaBus.plateNumber", "defaultContent": "","render": function(data, type, row, meta) {
                if($.format.date(row.jpaBus.updated, "yyyy-MM-dd")==$.format.date(new Date(), "yyyy-MM-dd")){
                 return '<font color="red">'+data+'</font>';
                }else{
                   return data;;
                }
                }},
                { "data": "jpaBus.model.name", "defaultContent": ""},
                { "data": "jpaBus.description", "defaultContent": ""},
                { "data": "busInfo.contractCode", "defaultContent": ""},
                        { "data": "busInfo.offlinecontract.adcontent", "defaultContent": ""},
                            { "data": "busInfo._adtype", "defaultContent": ""},
                    { "data": "jpaBus.categoryStr", "defaultContent": ""},
                { "data": "jpaBus.company.name", "defaultContent": ""},
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
                 { "data": "busInfo.busOnline.reserveDate", "defaultContent": "","render": function(data, type, row, meta) {
              	  var d= $.format.date(data, "yyyy-MM-dd");
              	  return d;
              	  }},
                 { "data": "busInfo.busOnline.realEndDate", "defaultContent": "","render": function(data, type, row, meta) {
              	  var d= $.format.date(data, "yyyy-MM-dd");
              	  return d;
              	  }},
                 { "data": "busInfo.offlinecontract.relateMan", "defaultContent": ""},
	              { "data": "", "defaultContent": "","render": function(data, type, row, meta) {
	              
	              		var tString ='';
	              		if(row.busInfo.stats == 'now' || row.busInfo.stats == 'future'){
	              			tString = 	 '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/bus/offlineContract/' + (row.busInfo.busOnline.id) +"/"+($("#plid").val())+ '">撤销</a> &nbsp;'
	              		}
	              		if(typeof(row.busInfo.busOnline)!="undefined" ){
	              		 if( $.format.date(row.busInfo.busOnline.realEndDate, "yyyy-MM-dd") <= $.format.date(new Date(), "yyyy-MM-dd")){
                           tString ='';
                         }
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
                        '        <input id="name" value="" style="width:125px">' +
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
                    '<select class="ui-input ui-input-mini" name="company" id="company" style="width:125px">' +
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
    
    
  function  checkFree(){
  
   var stday=$("#stday").val();
            var days=$("#days").val();
            if(stday==""){
            layer.msg("请选择上刊日期",{icon: 5});
            return;
            }
            if(days==""){
            layer.msg("请输入刊期",{icon: 5});
            return;
            }
        	 
   		var param={"days":days,"stday":stday,"plid":$("#plid").val()};
		    	 $.ajax({
		    			url:"${rc.contextPath}/bus/ajax-checkFree",
		    			type:"POST",
		    			async:false,
		    			dataType:"json",
		    			data:param,
		    			success:function(data){
		    				if (data.left == true) {
		    					layer.msg("有["+data.right+"]辆车可以进行上刊", {icon: 1});
		    				} else {
		    					layer.msg(data.right,{icon: 5});
		    				}
		    			}
		       });  
  }
  
  
  function selectAll(){ 
  	 var t =0;
     $("input[name='checkAll']:checkbox:checked").each(function(){
	    		 	t++; 
	}) 
    if(t==1){
		  $("input[name='checkone']").attr("checked", 'true');
	}else {
	  $("input[name='checkone']").removeAttr("checked");
	}
} 

  
  function checkSelect(){
     
    		 var aa='';
    		 var c=0;
	        $("input[name='checkone']:checkbox:checked").each(function(){
	    		 	c++; 
					aa+=$(this).attr("zbm") +","
			}) 
			if(c==0){
			  layer.msg("请选择需要上刊的车辆");
			} else {
			   var t=aa.substring(0,aa.length-1);
		   	   layer.alert('您选择的自编号有：'+t) ;
			}
    }
 function sub(){
            var stday=$("#stday").val();
            var days=$("#days").val();
            var fdays=$("#fdays").val();
            var contractid=${jpaPublishLine.offlineContract.id};
            if(stday==""){
            layer.msg("请选择上刊日期");
            return;
            }
            if(fdays==""){
            layer.msg("请填写下刊预留天数");
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
   		var param={"ids":dIds,
   		"days":days,"stday":stday,
   		"contractid":contractid,
   		"plid":$("#plid").val(),
   		"fday":fdays,
   		"adtype":$("#adtype  option:selected").val(),
   		"print":$("#print  option:selected").val(),
   		"sktype":$("#sktype  option:selected").val()
   		};
    	
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
        queryTotalInfo();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
<div class="withdraw-title">
               已上刊发布信息
									</div>
  			 <div id="orderedBuses">
				<table id="lineTable" class="display compact"
					cellspacing="0" width="100%">
					<thead>
				<tr class="tableTr">
					<th>线路</th>
					<th>级别</th>
					<th>媒体类型</th>
					<th>发布形式</th>
					<th>批次</th>
                    <th>数量（辆）</th>
                    <th>已装数量</th>
                    <th>刊期(天)</th>
                    <th>发布时间</th>
				</tr>
					</thead>
				</table>
			 </div>
			  </div><br>
<div class="withdraw-wrap color-white-bg fn-clear">

			<!--over-->
			<div class="withdraw-title">
               上刊发布
			</div>
            <div style="padding-top:10px;" >
                 <div >
                 <input type="hidden" id ="plid" value="${plid}"/>
                           上刊日期：<input  class="ui-input ui-input-mini datepicker" type="text" name="stday"
                            id="stday" data-is="isAmount isEnough"
                            autocomplete="off" disableautocomplete="" style="width:90px"> 
                           &nbsp;&nbsp; 刊期(天)：<input  class="ui-input"  type="text" 
                            id="days" data-is="isAmount isEnough"  value="${jpaPublishLine.days}" readonly="readonly"  onkeyup="value=value.replace(/[^\d]/g,'')"
                            autocomplete="off" disableautocomplete="" style="width:45px"> 
                                  <span>广告形式</span>&nbsp;&nbsp;
                       <select class="ui-input ui-input-mini"  id="adtype" style="width:100px">
                    <option value="tiaofu" selected="selected">条幅式</option>
                  	<option value="cheshen">车身彩贴</option>
                  	<option value="quanche">全车彩贴</option>
                  	</select>
                  	       <span>印制</span>&nbsp;&nbsp;
                       <select class="ui-input ui-input-mini" name="print" id="print" style="width:100px">
                    <option value="center" selected="selected">中心</option>
                  	<option value="out">外部</option>
                  	</select>
                  	  <span>上刊类型</span>&nbsp;&nbsp;
                      <select class="ui-input ui-input-mini" name="sktype" id="sktype" style="width:100px">
                    <option value="normal" selected="selected">正常上刊</option>
                  	<option value="fill">补刊</option>
                  	<option value="contin">续刊</option>
                  	</select>
                  <span>下刊预留天数</span>&nbsp;&nbsp; <input id="fdays"  value="" onkeyup="value=value.replace(/[^\\d]/g,\'\')" style="width:75px;height: 30px;border-radius: 2px;border: 1px solid #C5C5C5;">
                       &nbsp;&nbsp;<input type="button" class="button_kind" style="width: 85px;"
			                value="验证提醒" onclick="checkSelect()"/> 
			                <!--    <input type="button" class="button_kind" style="width: 85px;height: 30px;" value="库存检查" onclick="checkFree()"/>-->
			                   
                           &nbsp;&nbsp; <input type="button" class="button_kind" style="width: 85px;"
			                    value="批量上刊" onclick="sub()"/>
                </div>
	     	</div>
	     	
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
                        <th > <input type="checkbox" id="checkAll" name="checkAll" onclick="selectAll()" /></th>
                           <th orderBy="line.name">线路</th>
                        <th orderBy="line.level">线路级别</th>
                        <th orderBy="serialNumber" >车辆自编号</th>
                        <th >旧自编号</th>
                        <th >车牌号</th>
                        <th >车型</th>
                         <th>车辆描述</th>
                        <th>合同编号</th>
                        <th>广告内容</th>
                        <th>广告类型</th>
                        <th>车辆情况</th>
                        <th orderBy="company">营销中心</th>
                        <th>实际上刊日期</th>
                        <th>预计下刊日期</th>
                        <th>下刊预留日期</th>
                        <th>实际下刊日期</th>
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
                </div><br>
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

</@frame.html>