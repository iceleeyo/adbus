<#import "template/template_mobile.ftl" as frame>
<#import "template/orderDetail.ftl" as orderDetail/>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<#global menu="合同列表" >
<@frame.html title="合同列表"   js=["js/nano.js","js/jquery-dateFormat.js","js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js","js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]
>
<style type="text/css">
#table_length , #table_info{
	display: none;
}
body{
	max-width: none;
}
	.center {margin: auto;}
    .div {text-align:center;}
    div#toolbar {float: left;}
    .div2 {text-align:center;}
    .div5{margin: 5px;}
    div2#toolbar2 {float: left;}
    #toolbar2{  margin-bottom: 20px;}
    .processed {color: limegreen;}
    .invalid {color: red;}
    .hl {background-color: #ffff00;}
    #table {font-size: 20px;}
    #table td, #table2 td {position:relative;  line-height: 60px;}
    #table th, #table2 th {position:relative;  height: 50px;}
    #table td .per-middle {position:absolute;background-color: #ffad20;top:0;height:100%;z-index:1}
    #table td .per-first-or-last {position:absolute;background-color: #4acd48;border-left: 1px solid white;border-right: 1px solid white;top:0;height:100%;z-index:1;}
	#table th, #table2 th,#his_table th{background-color: #3bafda;color: white;  font-weight: normal;  text-align: left;  line-height: 30px;  padding: 2px 2px;}
	table.dataTable.compact tbody td { padding: 2px 2px;}
</style>

<script type="text/javascript">
var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": false,
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

				<table id="table" class="display" cellspacing="0">
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
