<#import "../template/template.ftl" as frame>
<#global menu="车身角色管理">
<@frame.html title="角色管理" js=["js/layer-v1.9.3/layer/layer.js","js/layer.onload.js"] css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/uploadprogess.css","css/liselect/pkg-generator.css$ver=1431443489.css"]>
<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom" : '<"#toolbar">td',
							"searching" : false,
							"ordering" : false,
							"serverSide" : false,
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/user/ajax-roleList",
                data: function(d) {
                },
               "dataSrc": function(json) {return json;},
            },
            "columns": [
                { "data": "actIdGroup.name", "defaultContent": ""},
                { "data": "groupId", "defaultContent": ""},
                { "data": "functions", "defaultContent": ""},
                { "data": function( row, type, set, meta) {
                    return row.actIdGroup.id ;
                },
                    "render": function(data, type, row, meta) {
                        var operations= '<a  href="${rc.contextPath}/user/to_editRole/' + data + '" >修改</a> ';
                         operations +='&nbsp;&nbsp;<a class="table-link" href="javascript:delContract(\''+data+'\');" >删除</a>  &nbsp;';
                         return operations;
                        ;
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
        $("div#toolbar").html('');
    }
function delContract(id){
	layer.confirm('确定删除吗？', {icon: 3}, function(index){
    		layer.close(index);
    		if(1==1){
				 $.ajax({
						url:"${rc.contextPath}/user/deleRole/"+id,
						type:"POST",
						async:false,
						dataType:"json",
						data:{},
						success:function(data){
							if (data.left == true) {
								layer.msg(data.right);
							   var uptime = window.setTimeout(function(){
							window.location.href="${rc.contextPath}/user/role_list";
						   	clearTimeout(uptime);
									},2000)
							} else {
								layer.msg(data.right);
							}
						}
			      }); 
			      
			     } 
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
									角色列表
									<a class="block-btn" href="${rc.contextPath}/user/addRole">添加角色</a>
									</div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>角色名称</th>
                        <th>角色简码</th>
                        <th>权限</th>
                        <th>操作</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>