<#import "template/template.ftl" as frame>
<#global menu="${orderMenu}">
<@frame.html title="进行中的订单" css=["js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"] js=["js/jquery-ui/jquery-ui.auto.complete.js","js/jquery-dateFormat.js"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<script type="text/javascript">
var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "scrollX": true,
              "aaSorting": [[4, "desc"]],
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                { "orderable": false, "targets": [0,1,2,3,5,6,7] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/busselect/ajax-body-runningAjax",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[companyname]" : $('#companyname').val()
                        ,"filter[taskKey]" : $('#taskKey').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
            	{ "data": "jpaBodyContract.creator", "defaultContent": ""},
            	{ "data": "jpaBodyContract.company", "defaultContent": ""},
            	{ "data": "need_cars", "defaultContent": ""},
            	{ "data": "done_cars", "defaultContent": ""},
                { "data": "jpaBodyContract.created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm");
                	return d;
                }},
                 { "data": "task_name", "defaultContent": "","render": function(data, type, row, meta) {
		                 	var tew=  "<a target='_blank' href='${rc.contextPath}/workflow/view/"+row.executionId+"/page/"+row.processInstanceId+"'>"+data+"</a>";
	                 	 	return tew;
	                   
                  	 
                    }},
                   { "data": "task_assignee", "defaultContent": ""
                   
                   },
                   { "data": "jpaBodyContract.id", "defaultContent": "","render": function(data, type, row, meta) {
                	 var tr= "<a target='_blank' href='${rc.contextPath}/busselect/detail/"+ data+"?taskid=" +(row.task_id)+  "'>查看详情</a>";
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
    
    	<@security.authorize ifAnyGranted="bodyContractManager,bodyFinancialManager,bodyScheduleManager">
	    	 function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>公司名称</span>' +
                        '    <span>' +
                        '        <input id="companyname" value="">' +
                        '    </span>' +
                         '<select class="ui-input ui-input-mini" name="taskKey" id="taskKey">' +
                    '<option value="defaultAll" selected="selected">所有事项</option>' +
                  	'<option value="be_contractEnable">待合同生效</option>' +
                  	'<option value="payment">待支付</option>' +
                    '<option value="be_workcomple">待施工完成</option>' +
         			'</select>' +
                    '</div>'
        );

        $('#companyname, #taskKey').change(function() {
            table.fnDraw();
        });
    }
   </@security.authorize>

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

             <div class="withdraw-wrap color-white-bg fn-clear">
                <div class="tabs">
                <@security.authorize ifAnyGranted="bodyContractManager,bodyFinancialManager,bodyScheduleManager">
					<a id="tab2" class="active" href="${rc.contextPath}/busselect/body_allRuningOrders">进行中的订单<span id="recordsTotal" style="background-color:#ff9966;font-size: 14px;border-radius: 4px;"></span></a>
					</@security.authorize>	
					<a id="tab3" href="${rc.contextPath}/busselect/finished">已完成的订单</a>
					<@security.authorize ifAnyGranted="bodyContractManager,bodyFinancialManager,bodyScheduleManager">
					<a id="tab4" href="${rc.contextPath}/busselect/join/1">我参与的订单</a>
					</@security.authorize>
				
				</div>
				
				<table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>下单用户</th>
                        <th>签约公司</th>
                        <th>车辆总数</th>
                         <th>已安装</th>
                        <th orderBy="created">创建时间</th>
                        <th>待办事项</th>
                        <th>当前处理人</th>
                         <th>订单详情</th>
                    </tr>
                    </thead>

                </table>
                
</div>
</@frame.html>
<!-- 针对tab的js -->
