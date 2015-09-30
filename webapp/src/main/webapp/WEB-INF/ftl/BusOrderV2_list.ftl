<#import "template/template.ftl" as frame>
<#global menu="${currMenu}">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="车身订单列表" js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js","js/layer-v1.9.3/layer/layer.js","js/layer.onload.js"] >

<script type="text/javascript">
    var table;

    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "scrollX": true,
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                { "orderable": false, "targets": [0,1,3] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/product/ajax-busOrderV2_list/${type}",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "jpaProductV2.name", "defaultContent": "" ,"render": function(data, type, row, meta) {
                    if(data==null || data==''){
                	  return '<a  onclick="showProV2DetailByOrderID(\'${rc.contextPath}\','+row.id+');">自选下单</a>';
                    }else{
                          return '<a  onclick="showProV2Detail(\'${rc.contextPath}\','+row.jpaProductV2.id+');">'+data+'</a>';
                    }
                }},
                { "data": "productPrice", "defaultContent": "", "render": $.fn.dataTable.render.number( ',', '.', 2, ' ')  },
                 { "data": "created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                <@security.authorize ifAnyGranted="bodyContractManager,sale_packageList">
                { "data": "creater", "defaultContent": "" ,
                    "render": function(data, type, row, meta) {
                    return '<a href="${rc.contextPath}/user/u/' + data + '" >'+data+'</a> ';
                } },
                  </@security.authorize >
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                          var operations = '';
                     	 	 operations+= '<a  href="javascript:void(0);" >标记为已处理</a>';
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
		    					 table.dataTable()._fnAjaxUpdate();
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
		
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th >套餐名称</th>
                        <th >价格(元)</th>
                        <th  orderBy="created">下单时间</th>
                         <@security.authorize ifAnyGranted="bodyContractManager,sale_packageList">
                        <th >下单人</th>
                          </@security.authorize >
                        <th>管理</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>