<#import "template/template.ftl" as frame> <#global menu="底片管理">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="底片管理"
js=["js/jquery-dateFormat.js","js/layer-v1.9.3/layer/layer.js"]>


<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                { "orderable": false, "targets": [0] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/contract/blackAd-ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[seqNumber]" : $('#contractCode').val(),
                        "filter[adName]" : $('#contractName').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
            	{ "data": "adName", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        var filter = $('#contractName').val();
                        if (filter && filter != '') {
                            var regex = new RegExp(filter, "gi");
                            data = data.replace(regex, function(matched) {
                                return "<span class=\"hl\">" + matched + "</span>";
                            });
                        }
                    return data;
                } },
                { "data": "seqNumber", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        var filter = $('#contractCode').val();
                       	if (filter && filter != '') {
                            var regex = new RegExp(filter, "gi");
                            data = data.replace(regex, function(matched) {
                                return "<span class=\"hl\">" + matched + "</span>";
                            });
                        }
                        return data;
                }},
                { "data": "duration"},
                { "data": "sortNumber"},
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                         return '';
                    }},
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
        } );
        table.fnNameOrdering("orderBy").fnNoColumnsParams();
    }
function delContract(conid){
	var bln=window.confirm("确定删除该底片吗？");
    if(bln){
	 $.ajax({
			url:"${rc.contextPath}/contract/delContract/"+conid,
			type:"POST",
			async:false,
			dataType:"json",
			data:{},
			success:function(data){
				if (data.left == true) {
					jDialog.Alert(data.right);
				   var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/contract/blackAdlist"
			   	clearTimeout(uptime);
						},2000)
				} else {
					jDialog.Alert(data.right);
				}
			}
      });  
   }
	   
	}
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>底片名称</span>' +
                        '        <input id="contractName" value="">' +
                        '    <span>审核号</span>' +
                        '        <input id="contractCode" value="">' +
                        '</div>'
        );

        $('#contractCode, #contractName').change(function() {
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
	<div class="withdraw-title">
		<span>底片列表</span> <@security.authorize
		ifAnyGranted="ShibaOrderManager"> <a class="block-btn"
			href="${rc.contextPath}/contract/blackAdEnter">添加底片</a>
		</@security.authorize>
	</div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr class="tableTr">
				<th>底片名称</th>
				<th>审核号</th>
				<th>时长(秒)</th>
				<th>权重</th>
				<th>管理</th>
			</tr>
		</thead>
	</table>
</div>
</@frame.html>


