<#import "template/template.ftl" as frame> <#global menu="视频用户列表">
<@frame.html title="视频用户列表"
js=["js/layer-v1.9.3/layer/layer.js","js/layer.onload.js"]
css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/uploadprogess.css","css/liselect/pkg-generator.css$ver=1431443489.css"]>

<style type="text/css">
.operation
{
	color: #E0296C;
    font-weight: 800;
}
.operation-ok
{
	color: #29E039;
    font-weight: 800;
}
.operation-cancel
{
	color: #E0B229;
    font-weight: 800;
}
</style>

<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": true,
            "serverSide": true,
               "iDisplayLength" : 50,
            "aLengthMenu": [[20, 50, 100], [20, 50, 100]],
            "columnDefs": [
                {
                    "sClass": "align-left", "targets": [0,1] ,
                    "orderable": false, "targets": [1,2,4]
                },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/user/ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val(),
                        "filter[utype]" : '${usertype!''}'
                        
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "username", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        var filter = $('#name').val();
                        if (filter && filter != '') {
                            var regex = new RegExp(filter, "gi");
                            data = data.replace(regex, function(matched) {
                                return "<span class=\"hl\">" + matched + "</span>";
                            });
                        }
                        
                        
                    return '<a href="${rc.contextPath}/user/u/' + row.username + '" >'+data+'</a> &nbsp;';
                } },
                { "data": "groups", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        if (data.length > 1) {
                            var g = '<select class="ui-input" name="groups">';
                            $.each(data, function(i) {
                                g += '<option value="' + data[i].name + '" >' + data[i].name + '</option>';
                            });
                            g += '</select>'
                            return g;
                        } else {
                            return data.length ? data[0].name : "";
                        }
                    } },  
                    { "data": "utype", "defaultContent": "", "render": function(data) {
		                   if(data=='screen'){
                           		 return '移动电视';
                            }  else if(data=='body'){
                          		 	 return '车身广告';
                            }else {
                            	return '注册广告主'; 
                            }
                    }
                },
                { "data": "enabled", "defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="processed">正常</span>';
                        default :
                            return '<span class="invalid">禁用</span>';
                    }
                } },
             
                
                { "data": function( row, type, set, meta) {
                    return row.username;
                },
                    "render": function(data, type, row, meta) {
                    var opt='';
                    if(row.enabled){
                    opt+='<a class="table-action operation" href="javascript:void(0);" url="${rc.contextPath}/user/' + data + '/disable">禁用</a> &nbsp;'
                    }else{
                    opt+='<a class="table-action operation " href="javascript:void(0);" url="${rc.contextPath}/user/' + data + '/enable">启用</a> &nbsp;'
                    }
                      opt+= '<a class="operation" href="${rc.contextPath}/user/u_edit/' + data + '" >编辑</a> &nbsp;'
                        if(row.isActivate==0){
                         opt+=  '<a class="operation" href="javascript:void(0)" onclick="delUser(\''+data+'\')" >删除</a> &nbsp;'
                        }
                         opt+=  '<a class="operation" href="javascript:void(0)" onclick="resetPW(\''+data+'\')" >重置密码</a> &nbsp;'
                        
                        return opt;
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
                        '    <span>用户名</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    </span>' +
                        '</div>'
        );

        $('#name').change(function() {
            table.fnDraw();
        });
        bindLayerMouseOver();
    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }
    
     function delUser(username){
	var bln=window.confirm("确定删除该用户吗？");
    if(bln){
	 $.ajax({
			url:"${rc.contextPath}/user/delUser/"+username,
			type:"POST",
			async:false,
			dataType:"json",
			data:{},
			success:function(data){
				if (data.left == true) {
					layer.msg(data.right);
				   var uptime = window.setTimeout(function(){
				window.location.reload();
			   	clearTimeout(uptime);
						},2000)
				} else {
					layer.msg(data.right);
				}
			}
      });  
   }
	   
	}

    $(document).ready(function() {
        initTable();
         bindLayerMouseOver();
    } );
    
    function resetPW(userName){
    layer.confirm('确定重置密码为123456吗？', {icon: 3}, function(index){
    		layer.close(index);
		    if(true){
		    	 $.ajax({
		    			url:"${rc.contextPath}/user/resetPW",
		    			type:"POST",
		    			async:false,
		    			dataType:"json",
		    			data:{"userName":userName},
		    			success:function(data){
		    				if (data.left) {
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
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title">
		管理员列表 <a class="block-btn" href="${rc.contextPath}/user/enter">添加用户</a>
	</div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th orderBy="username">用户名</th>
				<th>所属组</th>
				<th>类型</th>
				<th orderBy="enabled">状态</th>
				<th>管理</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
