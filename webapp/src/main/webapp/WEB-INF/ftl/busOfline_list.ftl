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
    .processed {color: limegreen;}
    .invalid {color: red;}
    .hl {background-color: #ffff00;}
</style>

<script type="text/javascript">
	
    var table;
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
    	var bln=window.confirm("确定上刊吗?");
        if(bln){
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
    				} else {
    					layer.msg(data.right);
    				}
    			}
       });  
       }
        }
    $(document).ready(function() {
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
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
</div>
</@frame.html>