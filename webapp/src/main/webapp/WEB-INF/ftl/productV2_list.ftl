<#import "template/template.ftl" as frame>
<#global menu="车身套餐列表">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="车身套餐列表" js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js","js/layer-v1.9.3/layer/layer.js","js/layer.onload.js"] >

<script type="text/javascript">
    var table;

    function initTable () {
        table = $('#table').DataTable( {
             "dom": '<"#toolbar"><"top"i>rt<"bottom"flp><"clear"> ',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "scrollX": true,
             "aaSorting": [[3, "desc"]],
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                { "orderable": false, "targets": [0,1,4,5] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/product/ajax-productV2_list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
              { "data": "", "defaultContent": "" },
                { "data": "name", "defaultContent": "" },
                { "data": "price", "defaultContent": "", "render": $.fn.dataTable.render.number( ',', '.', 2, ' ')  },
                 { "data": "created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                { "data": "creater", "defaultContent": "" },
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                          var operations = '<a  onclick="showProV2Detail(\'${rc.contextPath}\','+data+');">查看详细</a>&nbsp;&nbsp;';
                          
                          
                     	 	 operations+= '<a  href="javascript:void(0);" onclick="buy('+data+')" >下单</a>&nbsp;&nbsp;';
                       <@security.authorize ifAnyGranted="bodyContractManager">
                          if(row.stats=='online'){
                      		operations +=	'<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/product/changeStats/' + data + '/offline">取消上架</a> &nbsp;'
                       	}else {
                       		operations +=	'<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/product/changeStats/' + data + '/online">上架</a> &nbsp;'
                       	}
                       	</@security.authorize>
                       return operations;
                        
                    }},
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
        } );
         $('#table').dataTable().fnNameOrdering();
        //table.fnNameOrdering("orderBy").fnNoColumnsParams();
        
        table.on( 'order.dt search.dt', function () {
	        table.column(0, {}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
	        } );
	    } ).draw();
       
    }

    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>套餐名称</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    </span>' +
                        '</div>'
        );

        $('#name').change(function() {
            table.fnDraw();
        });
    }

    function drawCallback() {
        $('.table-action').click(function() {
        var r = confirm("确定执行该操作吗")
        if(r==true){
       		$.post($(this).attr("url"), function() {
                table.fnDraw(true);
        	});
        }
        });
    }
function buy(pid){
		layer.confirm('确定下单吗？', {icon: 3}, function(index){
    		layer.close(index);
		    if(true){
		    	 $.ajax({
		    			url:"${rc.contextPath}/product/buyBodyPro/"+pid,
		    			type:"POST",
		    			async:false,
		    			dataType:"json",
		    			data:{},
		    			success:function(data){
		    				if (data.left == true) {
		    					layer.msg(data.right);
		    					 window.location.href="${rc.contextPath}/product/busOrderV2_list/my";
		    				} else {
		    					layer.msg(data.right,{icon: 5});
		    				}
		    			}
		       });  
		       }
		});		
    
        }
    $(document).ready(function() {
        initTable();
    } );
</script>

<div class="withdraw-wrap color-white-bg fn-clear">
		<div class="paging"></div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                     <th ></th>
                        <th >套餐名称</th>
                        <th orderBy="price">价格(元)</th>
                        <th orderBy="created">创建时间</th>
                        <th >创建人</th>
                        <th>管理</th>
                    </tr>
                    </thead>

                </table>
                <span>-</span>
</div>
</@frame.html>