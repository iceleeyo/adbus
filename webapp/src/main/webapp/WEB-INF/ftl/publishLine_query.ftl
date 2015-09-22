<#import "template/template.ftl" as frame>
<#global menu="订单列表">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="订单列表" js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js", "js/datepicker.js","js/jquery.datepicker.region.cn.js"]
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
              "aaSorting": [[12, "desc"]],
              "columnDefs": [
                 { "orderable": false, "targets": [0,1,2,3,4,5,6,7,8,9,10,11,13] },
            ],
            "scrollX": true,
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/busselect/ajax-publishLine_list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[contractCode]" : $('#contractCode').val(),
                        "filter[linename]" : $('#linename').val(),
                        "filter[model]" : $('#model').val(),
                        "filter[company]" : $('#company').val()
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
                 //return (Math.round(row.remainNuber / row.salesNumber * 10000) / 100.00 + "%");;
                }},
                { "data": "days", "defaultContent": ""},
                
                { "data": "jpaBusinessCompany.name", "defaultContent": ""},
                { "data": "startDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                { "data": "created", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                 { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                    var operations ='<a class="table-link" onclick="editPublishLine(\'${rc.contextPath}\','+data+');" href="javascript:void(0)">修改</a>';
                    operations +='&nbsp;&nbsp;<a class="table-link" target="_blank" href="${rc.contextPath}/bus/findBusByLineid/'+data+'">车辆上刊</a>';
                         return operations;
                    }}
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
                        '    <span>合同编号</span>' +
                        '    <span>' +
                        '        <input id="contractCode" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                         '    <span>线路</span>' +
                        '    <span>' +
                        '        <input id="linename" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                         '    <span>车型</span>' +
                        '    <span>' +
                        '        <input id="model" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                         '    <span>营销中心</span>&nbsp;&nbsp;' +
                       '<select class="ui-input ui-input-mini"  id="company">' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	                           '<option value="自营">自营</option>'+
                                            	'<option value="CBS">CBS</option>'+
                                            	'<option value="白马">白马</option>'+
                                            	'<option value="七彩">七彩</option>'+
                                            	'<option value="市场">市场</option>'+
                                            	'<option value="其他">其他</option>'+
         			'</select><br>' +
                        '</div>'
        );

        $('#contractCode,#linename,#model,#company').change(function() {
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
 var table2;
    function initTable2 () {
        table2 = $('#table2').dataTable( {
            "dom": '<"#toolbar2">lrtip',
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
                        "filter[company]" : $('#company').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "jpaBus.plateNumber", "defaultContent": "","render": function(data, type, row, meta) {
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
	              			tString = 	 '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/bus/offlineContract/' + (row.busInfo.busOnline.id) +"/0"+ '">撤销</a> &nbsp;'
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
        table2.fnNameOrdering("orderBy").fnNoColumnsParams();
    }
    $(document).ready(function() {
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title">
                订单列表
									</div>
                <table id="table" class="display nowrap" cellspacing="0">
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
                        <th>操作</th>
                    </tr>
                    </thead>
                </table>
</div>
<div class="withdraw-wrap color-white-bg fn-clear">
                <table id="table2" class="display nowrap" cellspacing="0">
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
                        <th>客户名称</th>
                        <th>撤销上刊</th>
                    </tr>
                    </thead>
                </table>
</div>
</@frame.html>