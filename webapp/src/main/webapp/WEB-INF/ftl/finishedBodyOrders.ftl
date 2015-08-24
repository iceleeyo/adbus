<#import "template/template.ftl" as frame>
<#global menu="已完成订单">
<@frame.html title="已完成的订单" css=["js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"] js=["js/jquery-ui/jquery-ui.auto.complete.js","js/jquery-dateFormat.js"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />

<script type="text/javascript">

	
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
             "aaSorting": [[5, "desc"]],
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                { "orderable": false, "targets": [0,1,2,3,4] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/busselect/ajax-finishedOrders",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[companyname]" : $('#companyname').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "jpaBodyContract.seriaNum", "defaultContent": ""},
            	{ "data": "jpaBodyContract.creator", "defaultContent": ""},
            	{ "data": "jpaBodyContract.company", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                    return data;
                } },
                { "data": "need_cars", "defaultContent": ""},
            	{ "data": "done_cars", "defaultContent": ""},
                { "data": "startTime", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm:ss");
                	return d;
                }},
                { "data": "endTime", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm:ss");
                	return d;
                }},
                  { "data": "jpaBodyContract.created", "defaultContent": "","render": function(data, type, row, meta) {
                	 var tr= "<a target='_blank' href='${rc.contextPath}/busselect/detail/" +(row.jpaBodyContract.id)+ "?pid="+(row.processInstanceId) +  "'>查看详情</a>";
                	return tr;
                }},{ "data": "finishedState", "defaultContent": "" },
                
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
                    '</div>'
        );

        $('#companyname').change(function() {
            table.fnDraw();
        });
    }
 </@security.authorize>
 
<@security.authorize ifAnyGranted="bodysales">
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>订单号</span>' +
                        '    <span>' +
                        '        <input id="longOrderId" value="">' +
                        '    </span>' +
                        '<select class="ui-input ui-input-mini" name="stateKey" id="stateKey">' +
	                    '<option value="defaultAll" selected="selected">所有状态</option>' +
    	              	'<option value="finished">已完成</option>' +
        	          	'<option value="closed">已关闭</option>' +
         				'</select>' +
                        '</div>'
        );
        $('#stateKey').change(function() {
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
<#--            
			<!-- <div class="div" style="margin-top:25px">
                <caption><h2>待办事项</h2></caption>
            </div> -->
            <div class="tabs">
              		<@security.authorize ifAnyGranted="bodysales">
					<a id="tab1" href="${rc.contextPath}/busselect/myOrders/1">我的订单</a>
					</@security.authorize>
					<@security.authorize ifAnyGranted="bodyContractManager,bodyFinancialManager,bodyScheduleManager">
					<a id="tab2" href="${rc.contextPath}/busselect/body_allRuningOrders">进行中的订单</a>
					</@security.authorize>
					<a id="tab3" href="${rc.contextPath}/busselect/finished" class="active">已完成的订单<span id="recordsTotal" style="background-color:#ff9966;font-size: 14px;border-radius: 4px;"></span></a>
					<@security.authorize ifAnyGranted="bodyContractManager,bodyFinancialManager,bodyScheduleManager">
					<a id="tab4" href="${rc.contextPath}/busselect/join/1">我参与的订单</a>
					</@security.authorize>
				</div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                    <th>流水号</th>
                        <th>下单用户</th>
                        <th>套餐名称</th>
                           <th>车辆总数</th>
                            <th>安装车数</th>
                       <!-- <th>素材号</th>-->
                        <th orderBy="startTime">创建时间</th>
                           <th orderBy="endTime">结束时间</th>
                         <th>订单详情</th>
                           <th>最终状态</th>
                       
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>