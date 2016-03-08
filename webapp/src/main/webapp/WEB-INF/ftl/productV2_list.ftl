<#import "template/template.ftl" as frame> <#global menu="车身产品列表">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="车身产品列表"
js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js","js/layer-v1.9.3/layer/layer.js","js/layer.onload.js"]
>

<script type="text/javascript">
    var table;

    function initTable () {
        table = $('#table').DataTable( {
             "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "scrollX": true,
             "aaSorting": [[3, "desc"]],
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                { "orderable": false, "targets": [0,1,4] },
            ],
            "ajax": {
                type: "GET",
                url: "/product/ajax-productV2_list",
                
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val(),
                        "filter[stats]" : $('#stats').val()
                    } );
                },
                "dataSrc": "content",
                "complete": function(jqXHR, textStatus)
					      {
					       if(jqXHR.status==0){ alert("连接错误"); }
                           else if (jqXHR.status == 404) { alert("404错误"); }
                           else if (jqXHR.status == 500) {  alert("500错误,服务器暂时抽筋了");}
					},
            },
            "columns": [
                { "data": "jpaProductV2.name", "defaultContent": "" },
                { "data": "jpaProductV2.price", "defaultContent": "", "render": $.fn.dataTable.render.number( ',', '.', 2, ' ')  },
                { "data": "leval", "defaultContent": "", "render": function(data, type, row,meta) {
                           if(data=='S'){
                              return  '特级';
                            }else if(data=='APP'){
                               return 'A++';
                            }else if(data=='AP'){
                               return 'A+';
                            }else if(data=='A'){
                               return 'A';
                                 }else{
                                    return '经纬线';
                                 }
                               }},  
                { "data": "doubleDecker", "defaultContent": "", "render": function(data, type, row,meta) {
                           if(data==false){
                              return  ' 单层';
                            }else{
                               return ' 双层';
                                 }
                               }}, 
                { "data": "busNumber", "defaultContent": 0}, 
                { "data": "days", "defaultContent": 0}, 
                { "data": "jpaProductV2.stats", "defaultContent": "", "render": function(data) {
                    switch(data) {
                        case 'online':
                            return '<span class="processed">正常</span>';
                        default :
                            return '<span class="invalid">已下架</span>';
                    }
                } },
                 { "data": "jpaProductV2.created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                { "data": "jpaProductV2.creater", "defaultContent": "" },
                { "data": function( row, type, set, meta) {
                    return row.jpaProductV2.id;
                },
                    "render": function(data, type, row, meta) {
                          var operations = '';
                     	  operations+= '<a  href="javascript:void(0);" onclick="buy('+data+')" >下单</a>&nbsp;&nbsp;';
                        if(row.jpaProductV2.stats=='online'){
                      		operations +=	'<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/product/changeStats/' + data + '/offline">下架</a> &nbsp;'
                       	}else {
                       		operations +=	'<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/product/changeStats/' + data + '/online">上架</a> &nbsp;'
                       	}
                       	operations +='<a class="table-link operation" onclick="editBodyCombo('+row.id+')" href="javascript:void(0);">编辑</a> &nbsp;';
                       return operations;
                        
                    }},
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
            "fnDrawCallback": fnDrawCallback,
        } );
         $('#table').dataTable().fnNameOrdering();
        //table.fnNameOrdering("orderBy").fnNoColumnsParams();
        
    }
     function fnDrawCallback(){
    	// counter_columns(table,1);
    	 $('.table-action').click(function() {
        var r = confirm("确定执行该操作吗")
        if(r==true){
       		$.post($(this).attr("url"), function() {
                table.draw(true);
        	});
        }
        });
    }

    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>产品名称</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    </span>' +
                         '    &nbsp;&nbsp<span>状态</span>' +
                        '    <span><select id="stats">' +
                        '        <option value="online" selected="selected">正常</option>' +
                        '        <option value="offline">已下架</option>' +
                        '    </select></span>' +
                        '</div>'
        );

        $('#name,#stats').change(function() {
            table.draw();
        });
    }

    function drawCallback() {
    
    }
    function editBodyCombo(id){
        var url="${rc.contextPath}/product/getBodyProViewJson/"+id;
      $.post(url, function(data) {
               if(data!=""){
               window.location.href="${rc.contextPath}/product/BefEditBodyCombo?jsonStr="+data;
               }else{
                 layer.msg("操作异常");
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
	<div class="withdraw-title">车身产品列表</div>
	<div class="paging"></div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th>产品名称</th>
				<th orderBy="price">价格(元)</th>
				<th>线路级别</th>
				<th>车型</th>
				<th>车辆数</th>
				<th>刊期(天)</th>
				<th>状态</th>
				<th orderBy="created">创建时间</th>
				<th>创建人</th>
				<th>管理</th>
			</tr>
		</thead>

	</table>
	<span>-</span>
</div>
</@frame.html>
