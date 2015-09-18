<#import "template/template_blank.ftl" as frame>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="巴士上刊历史" js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js", "js/datepicker.js","js/jquery.datepicker.region.cn.js"]
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
               "aaSorting": [[7, "desc"]],
            "columnDefs": [
                { "orderable": false, "targets": [0,1,2,3,4,5,6,8] },
            ],
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-busOnline_history",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[busid]" : ${busid}
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "jpabus.plateNumber", "defaultContent": ""},
                { "data": "offlineContract.contractCode", "defaultContent": ""},
                { "data": "startDate", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                { "data": "endDate", "defaultContent": "","render": function(data, type, row, meta) {
            	    var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                 { "data": "days", "defaultContent": ""},
                 { "data": "created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                
                 { "data": "userid", "defaultContent": ""},
                  { "data": "enable", "defaultContent": "","render": function(data, type, row, meta) {
                	switch(data) {
                        case true:
                            return '<span class="processed">上刊正常</span>';
                        default :
                            return '<span class="invalid">上刑错误撤销</span>';
                    }
                }},
	 				{ "data": "updated", "defaultContent": "","render": function(data, type, row, meta) {
	                	var d= $.format.date(data, "yyyy-MM-dd");
	                	return d;
	                }
	                },
	                 { "data": "editor", "defaultContent": ""},
              
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
        $("div#toolbar").html('');
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
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
                        <th >车牌号</th>
                        <th>合同编号</th>
                        <th>上刊日期</th>
                        <th>下刊日期</th>
                        <th>刊期(天)</th>
                        <th>操作日期</th>
                        <th>操作人</th>
                         <th>状态</th>
                        <th orderBy="updated">最后更新时间</th>
                        <th >撤销操作人</th>
                       
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>