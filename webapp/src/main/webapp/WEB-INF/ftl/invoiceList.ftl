<#import "template/template.ftl" as frame>
<#global menu="发票列表">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="发票管理" js=["js/jquery-dateFormat.js"]>


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
                { "orderable": false, "targets": [5] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/user/ajax-invoiceList",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[title]" : $('#title').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "title"},
               { "data": "type", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data == 'special')
                            return '专用发票';
                        if (data == 'normal')
                            return '普通发票';
                        return '其他';
                    } },
                { "data": "taxrenum"},
                { "data": "bankname"},
                { "data": "updated", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                    var operations='<a target="_blank" class="table-link" href="${rc.contextPath}/user/invoice_edit/' + data +'">查看详细</a>&nbsp;';
                        operations +='<a class="table-link" href="javascript:delInvoice('+data+');" >删除</a>  &nbsp;';
                         return operations;
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
function delInvoice(conid){
	var bln=window.confirm("确定删除该发票吗？");
    if(bln){
	 $.ajax({
			url:"${rc.contextPath}/user/delInvoice/"+conid,
			type:"POST",
			async:false,
			dataType:"json",
			data:{},
			success:function(data){
				if (data.left == true) {
					jDialog.Alert(data.right);
				   var uptime = window.setTimeout(function(){
				window.location.href="${rc.contextPath}/user/invoiceList"
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
                        '    <span>发票抬头</span>' +
                        '        <input id="title" value="">' +
                        '</div>'
        );

        $('#title').change(function() {
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
            <div class="withdraw-title" style="padding-top: 0px;text-align:left;">
									发票列表
									</div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr class="tableTr">
                        <th orderBy="title">发票抬头</th>
                        <th orderBy="type">发票类型</th>
                        <th orderBy="taxrenum">税务登记证号</th>
                        <th orderBy="bankname">邮寄地址</th>
                        <th orderBy="updated">创建时间</th>
                        <th>管理</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>


