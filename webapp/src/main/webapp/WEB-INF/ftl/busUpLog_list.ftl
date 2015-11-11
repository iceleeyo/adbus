<#import "template/template.ftl" as frame> <#global menu="车辆变更历史">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="车辆变更历史"
js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js",
"js/datepicker.js","js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css"]>

<style type="text/css">
.center {
	margin: auto;
}

.frame {
	width: 1000px;
}

.div {
	text-align: center;
	margin: 25px;
}

.div#toolbar {
	float: left;
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
</style>

<script type="text/javascript">
	
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "scrollX": true,
               "aaSorting": [[21, "desc"]],
            "columnDefs": [
                { "orderable": false, "targets": [0,1,2,3,4,5,6,8,9,10,11,12,13,14,15,16,17,18,19,20] },
            ],
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-busUpLog_list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[serinum]" : $('#serinum').val(),
                        "filter[pname]" : $('#pname').val(),
                        "filter[companyId]" : $('#companyId').val()
                        
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "bus.serialNumber", "defaultContent": "", "render": function(data, type, row,meta) { 
               	   if(row.busUpLog.change_fileds!='' && row.busUpLog.change_fileds.indexOf("serialNumber")>=0){
                	  return '<b><span style="color:#B70909">'+data+'</span></b>';
                  } else {return data;} 
                }},
                { "data": "bus.oldSerialNumber", "defaultContent": "", "render": function(data, type, row,meta) { 
                
                 if(row.busUpLog.change_fileds!='' && row.busUpLog.change_fileds.indexOf("oldSerialNumber")>=0){
                	  return '<b><span style="color:#B70909">'+data+'</span></b>';
                  } else {return data;} 
                }},
                { "data": "bus.plateNumber", "defaultContent": "","render": function(data, type, row,meta) {
                  if(row.busUpLog.change_fileds!='' && row.busUpLog.change_fileds.indexOf("plateNumber")>=0){
                	  return '<b><span style="color:#B70909">'+data+'</span></b>';
                  } else {return data;} }},
                { "data": "oldbus.plateNumber", "defaultContent": ""},
                
                
                
                { "data": "model.name", "defaultContent": ""},
                { "data": "oldmodel.name", "defaultContent": ""},
                { "data": "line.name", "defaultContent": ""},
                { "data": "oldline.name", "defaultContent": ""},
                { "data": "line.levelStr", "defaultContent": ""},
                { "data": "oldline.levelStr", "defaultContent": ""},
                { "data": "ishaveAd","defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="invalid">有</span>';
                        default :
                            return '<span class="processed">否</span>';
                    }
                } },
                { "data": "busCategory", "defaultContent": "","render": function(data, type, row,meta) {
                  if(row.busUpLog.change_fileds!='' && row.busUpLog.change_fileds.indexOf("category")>=0){
                	  return '<b><span style="color:#B70909">'+data+'</span></b>';
                  } else {return data;} }},
                { "data": "oldbusCategory", "defaultContent": ""},
                { "data": "company.name", "defaultContent": "","render": function(data, type, row,meta) {
                  if(row.busUpLog.change_fileds!='' && row.busUpLog.change_fileds.indexOf("companyId")>=0){
                	  return '<b><span style="color:#B70909">'+data+'</span></b>';
                  } else {return data;} }
                  },
                { "data": "oldcompany.name", "defaultContent": ""},
                { "data": "bus.description", "defaultContent": "","render": function(data, type, row,meta) {
                  if(row.busUpLog.change_fileds!='' && row.busUpLog.change_fileds.indexOf("description")>=0){
                	  return '<b><span style="color:#B70909">'+data+'</span></b>';
                  } else {return data;} }},
                { "data": "oldbus.description", "defaultContent": ""},
                { "data": "bus.office", "defaultContent": "","render": function(data, type, row,meta) {
                  if(row.busUpLog.change_fileds!='' && row.busUpLog.change_fileds.indexOf("office")>=0){
                	  return '<b><span style="color:#B70909">'+data+'</span></b>';
                  } else {return data;} }},
                { "data": "oldbus.office", "defaultContent": ""
                
                	
                },
                { "data": "bus.branch", "defaultContent": "","render": function(data, type, row,meta) {
                  if(row.busUpLog.change_fileds!='' && row.busUpLog.change_fileds.indexOf("branch")>=0){
                	  return '<b><span style="color:#B70909">'+data+'</span></b>';
                  } else {return data;} }},
                { "data": "oldbus.branch", "defaultContent": ""},
                { "data": "busUpLog.updated", "defaultContent": "","render" : function(data, type, row,meta) {
					return  $.format.date(data, "yyyy-MM-dd");
										}
									},
                { "data": "busUpLog.updator", "defaultContent": ""},
              
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
        $("div#toolbar").html( '<div>' +
                        '    <span>车辆自编号：</span>' +
                        '    <span>' +
                        '        <input id="serinum" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '     &nbsp;&nbsp;'+ 
                           '    <span>原营销中心：</span>' +
                         '<select class="ui-input ui-input-mini" name="companyId" id="companyId">' +
                   		'<option value="-1" selected="selected">所有</option>' +
         				'</select>' +
         				
                        '</div>');
                        
                        $.ajax({
				url :   "/bus/findAllCompany",
				type : "GET",
				success : function(data) {
					$.each(data, function(i, item) {
							$("#companyId").append(
									$("<option value="+item.id+">" + item.name
											+ "</option>"));
					});
				}}, "text");
                        
        $('#serinum,#pname,#companyId').change(function() {
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
    $(document).ready(function() {
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title">车辆变更查询</div>
	<table id="table" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				<th>车辆自编号</th>
				<th>旧自编号</th>
				<th>车牌号</th>
				<th>原车牌号</th>
				<th>车型</th>
				<th>原车型</th>
				<th>线路</th>
				<th>原线路</th>
				<th>线路级别</th>
				<th>原线路级别</th>
				<th>是否有广告</th>
				<th>类别</th>
				<th>原类别</th>
				<th>营销中心</th>
				<th>原营销中心</th>
				<th>车辆描述</th>
				<th>原车型描述</th>
				<th>公司名称</th>
				<th>原公司名称</th>
				<th>客户名称</th>
				<th>原客户名称</th>
				<th orderBy="updated">最后更新时间</th>
				<th>操作人</th>

			</tr>
		</thead>

	</table>
</div>
</@frame.html>
