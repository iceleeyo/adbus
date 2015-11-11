<#import "template/template.ftl" as frame> <#global menu="线路变更历史">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="线路变更历史"
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

div#toolbar {
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
               "aaSorting": [[16, "desc"]],
            "columnDefs": [
                { "orderable": false, "targets": [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,17] },
            ],
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-lineUpLog_list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[linename]" : $('#linename').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "newLine.name", "defaultContent": ""},
                { "data": "oldLine2.name", "defaultContent": ""},
               { "data": "levleString", "defaultContent": "","render": function(data, type, row,meta) {
                  if(row.lineUpLog.change_fileds!='' && row.lineUpLog.change_fileds.indexOf("level")>=0){
                	  return '<b><span style="color:#B70909">'+data+'</span></b>';
                  } else {return data;} }},
               { "data": "oldlevleString", "defaultContent": ""},
                  { "data": "newLine.office", "defaultContent": "","render": function(data, type, row,meta) {
                  if(row.lineUpLog.change_fileds!='' && row.lineUpLog.change_fileds.indexOf("office")>=0){
                	  return '<b><span style="color:#B70909">'+data+'</span></b>';
                  } else {return data;} }},
                  { "data": "oldLine2.office", "defaultContent": ""},
                   { "data": "newLine.branch", "defaultContent": "","render": function(data, type, row,meta) {
                  if(row.lineUpLog.change_fileds!='' && row.lineUpLog.change_fileds.indexOf("branch")>=0){
                	  return '<b><span style="color:#B70909">'+data+'</span></b>';
                  } else {return data;} }},
                   { "data": "oldLine2.branch", "defaultContent": ""},
                 { "data": "company.name", "defaultContent": ""},
                 { "data": "oldcompany.name", "defaultContent": ""},
                    { "data": "newLine.cars", "defaultContent": "","render": function(data, type, row,meta) {
                  if(row.lineUpLog.change_fileds!='' && row.lineUpLog.change_fileds.indexOf("cars")>=0){
                	  return '<b><span style="color:#B70909">'+data+'</span></b>';
                  } else {return data;} }},
                    { "data": "oldLine2.cars", "defaultContent": ""},
                 { "data": "newLine.tolength", "defaultContent": "","render": function(data, type, row,meta) {
                  if(row.lineUpLog.change_fileds!='' && row.lineUpLog.change_fileds.indexOf("tolength")>=0){
                	  return '<b><span style="color:#B70909">'+data+'</span></b>';
                  } else {return data;} }},
                 { "data": "oldLine2.tolength", "defaultContent": ""},
                     { "data": "newLine.routelocation", "defaultContent": "","render": function(data, type, row,meta) {
                  if(row.lineUpLog.change_fileds!='' && row.lineUpLog.change_fileds.indexOf("routelocation")>=0){
                	  return '<b><span style="color:#B70909">'+data+'</span></b>';
                  } else {return data;} }},
                     { "data": "oldLine2.routelocation", "defaultContent": ""},
                { "data": "lineUpLog.updated", "defaultContent": "","render" : function(data, type, row,meta) {
					return  $.format.date(data, "yyyy-MM-dd");
										}
									},
                { "data": "lineUpLog.updator", "defaultContent": ""},
              
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
                        '   <span>线路：</span>' +
                        '    <span>' +
                        '        <input id="linename" value="" ' +
                        '    </span>&nbsp;&nbsp;' +
                        '</div>');
                        
        $('#linename').change(function() {
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
	<div class="withdraw-title">线路变更信息</div>
	<table id="table" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				<th>线路名称</th>
				<th>原线路名称</th>
				<th>线路级别</th>
				<th>原线路级别</th>
				<th>所属大公司</th>
				<th>原所属大公司</th>
				<th>所属分公司</th>
				<th>原所属分公司</th>
				<th>营销中心</th>
				<th>原营销中心</th>
				<th>车辆总数</th>
				<th>原车辆总数</th>
				<th>线路总里程</th>
				<th>原线路总里程</th>
				<th>途径地点</th>
				<th>原途径地点</th>
				<th orderBy="updated">最后更新时间</th>
				<th>操作人</th>
			</tr>
		</thead>
	</table>
</div>
</@frame.html>
