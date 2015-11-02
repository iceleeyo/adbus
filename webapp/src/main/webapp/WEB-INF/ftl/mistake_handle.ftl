<#import "template/template.ftl" as frame>
<#global menu="上下刊错误处理">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="车辆上下刊错误处理" js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

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
	function check(id){
		layer.confirm('确定下单吗？', {icon: 3}, function(index){
    		layer.close(index);
    		if(true){
        		window.location.href="${rc.contextPath}/bus/offlineContract/" + id +"/0";
    		}
		});
	}
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
                url: "${rc.contextPath}/bus/ajax-mistake_handle",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[plateNumber]" : $('#name').val(),
                        "filter[category]" : $('#category').val(),
                        "filter[levelStr]" : $('#levelStr').val(),
                        "filter[linename]" : $('#linename').val(),
                        "filter[company]" : $('#company').val(),
                        "filter[contractid]" : $('#cid').val()
                        
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "bus.plateNumber", "defaultContent": "","render": function(data, type, row, meta) {
                	return '<a  onclick="showbusOnline_history(\'${rc.contextPath}\','+row.bus.id+');">'+data+'</a>';
                }},
                { "data": "bus.serialNumber", "defaultContent": ""},
                { "data": "bus.oldSerialNumber", "defaultContent": ""},
                { "data": "model.name", "defaultContent": ""},
                { "data": "line.name", "defaultContent": ""},
                { "data": "line.levelStr", "defaultContent": ""},
                { "data": "busCategory", "defaultContent": ""},
                { "data": "company.name", "defaultContent": ""},
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
                 { "data": "bus.description", "defaultContent": ""},
                 { "data": "bus.branch", "defaultContent": ""},
	              { "data": "", "defaultContent": "","render": function(data, type, row, meta) {
	              
	              		var tString ='';
	              		if(row.busInfo.stats == 'now' || row.busInfo.stats == 'future'){
	              			tString = 	 '<a class="table-action" href="javascript:void(0);" onclick="check('+row.busInfo.busOnline.id+');" >撤销</a> &nbsp;'
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
var table2;
    function initTable2 () {
        table2 = $('#table2').dataTable( {
            "dom": '<"#toolbar2">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
              "aaSorting": [[12, "desc"]],
              "columnDefs": [
                 { "orderable": false, "targets": [0,1,2,3,4,5,6,7,8,9,10,11] },
            ],
            "scrollX": true,
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/busselect/ajax-publishLine_list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[contractid]" : $('#cid').val(),
                        "filter[linename]" : $('#linename').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "id", "defaultContent": ""},
                { "data": "offlineContract.contractCode", "defaultContent": ""},
                { "data": "line.name", "defaultContent": ""},
                { "data": "offlineContract.adcontent", "defaultContent": ""},
                { "data": "model", "defaultContent": "" , "render": function(data) {
               			 if(data.id ==0){
                                return "所有车型"
                            }else if(data.doubleDecker==false){
                              return data.name+ ' 单层';
                            }else{
                               return data.name+ '双层';
                                 }
                                 
                  }               
                },
                { "data": "lineDesc", "defaultContent": ""},
                { "data": "salesNumber", "defaultContent": ""},
                { "data": "remainNuber", "defaultContent": ""},
                { "data": "id", "defaultContent": "","render": function(data, type, row, meta) {
                return (Math.round(row.remainNuber / row.salesNumber * 100)  + "%");;
                }},
                { "data": "days", "defaultContent": ""},
                
                { "data": "jpaBusinessCompany.name", "defaultContent": ""},
                { "data": "startDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                { "data": "created", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
        } );
        table2.fnNameOrdering("orderBy").fnNoColumnsParams();
    }
    function initComplete() {
        $("div#toolbar").html(
                 '<div>' +
	                 '    <span>合同编号：</span>' +
	                 '    <span>' +
	                 '        <input id="contractid" value="">' +
	                 '    </span>&nbsp;&nbsp;' +
	                 '&nbsp;&nbsp;<span>线路：</span>'+
	                 '<span><input value="" id="linename" data-is="isAmount isEnough">'+
	                 '</span>&nbsp;&nbsp;'+
	                 
                        '   <br><br> <span>车牌号：</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>线路级别</span>&nbsp;&nbsp;' +
                       '<select class="ui-input ui-input-mini" name="levelStr" id="levelStr" style="width:125px" >' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	'<option value="S">特级</option>' +
                  	'<option value="APP">A++</option>' +
                  	'<option value="AP">A+</option>' +
                  	'<option value="A">A</option>' +
                  	'<option value="LATLONG">经纬线</option>' +
         			'</select>&nbsp;&nbsp;' +
                        '    <span>车辆类别</span>&nbsp;&nbsp;' +
                       '<select class="ui-input ui-input-mini" name="category" id="category" style="width:125px" >' +
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
                        '</div>'
        );
		
        
        $('#name,#category,#levelStr,#linename,#company').change(function() {
            table.fnDraw();
             table2.fnDraw();
        });
        $("#linename").autocomplete({
    		minLength: 0,
    			source : "${rc.contextPath}/busselect/autoComplete?tag=a",
    			change : function(event, ui) {
    			},
    			select : function(event, ui) {
    				$('#linename').val(ui.item.value);
    				table.fnDraw();
    				table2.fnDraw();
    			}
    		}).focus(function () {
           				 $(this).autocomplete("search");
       	 	});
       	 	
       	 	 $("#contractid").autocomplete({
    		    minLength: 0,
    			source : "${rc.contextPath}/busselect/contractAutoComplete?tag=a",
    			change : function(event, ui) {
    			},
    			select : function(event, ui) {
    			$('#cid').val(ui.item.dbId);
    				table.fnDraw();
    				table2.fnDraw();
    			}
    		}).focus(function () {
           				 $(this).autocomplete("search");
       	 	});
    }
		
    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }
    $(document).ready(function() {
        initTable();
        initTable2();
    
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title"> 上下刊错误处理</div>
	     	<input type="hidden" id="cid" />
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
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
                        <th>所属分公司</th>
                        <th>撤销上刊</th>
                    </tr>
                    </thead>
                </table>
</div>
<div class="withdraw-wrap color-white-bg fn-clear" style="margin-top: 10px;">
            <div class="withdraw-title">
                订单列表  	
									</div>
                <table id="table2" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
                        <th >订单编号</th>
                        <th >合同编号</th>
                        <th >线路</th>
                        <th>广告内容</th>
                        <th>车型</th>
                        <th>车型描述</th>
                        <th>订购数量</th>
                        <th>已上刊数量</th>
                        <th>在刊率</th>
                        <th>刊期</th>
                        <th>营销中心</th>
                        <th>预计上刊日期</th>
                        <th>订单记录时间</th>
                    </tr>
                    </thead>
                </table>
</div>
</@frame.html>