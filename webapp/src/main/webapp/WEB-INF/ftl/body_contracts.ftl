<#import "template/template_mobile.ftl" as frame>
<#global menu="${orderMenu}">
<@frame.html title="合同列表" css=["js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"] js=["js/jquery-ui/jquery-ui.auto.complete.js","js/jquery-dateFormat.js"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<script type="text/javascript">
var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "aaSorting": [[3, "desc"]],
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                { "orderable": false, "targets": [1,2,3,4] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/busselect/public_ajax-bodycontracts",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[companyname]" : $('#companyname').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
            	{ "data": "jpaBodyContract.contractCode", "defaultContent": ""},
            	{ "data": "jpaBodyContract.company", "defaultContent": ""},
            	{ "data": "need_cars", "defaultContent": ""},
            	{ "data": "done_cars", "defaultContent": ""},
                { "data": "jpaBodyContract.id", "defaultContent": "","render": function(data, type, row, meta) {
                	 var tr= "<a target='_blank' href='${rc.contextPath}/busselect/workList/"+ data+"'>查看</a>";
                	return tr;
                }},
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
            "fnDrawCallback": fnDrawCallback,
        } );
		table.fnNameOrdering("orderBy").fnNoColumnsParams();
    }
    
       	 function initComplete() {
       
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
		var record_count = (this.fnSettings().fnRecordsTotal() );
		if(record_count>0){
	 	  $("#recordsTotal").html("&nbsp;"+record_count+"&nbsp;");
		  }
    }

    $(document).ready(function() {
        initTable();
    } );
</script>

				<table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>合同编号</th>
                        <th>签约公司</th>
                        <th>车辆总数</th>
                         <th>已安装</th>
                         <th>施工单</th>
                    </tr>
                    </thead>

                </table>
                
</@frame.html>
<!-- 针对tab的js -->
