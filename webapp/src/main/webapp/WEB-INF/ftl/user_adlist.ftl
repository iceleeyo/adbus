<#import "template/template.ftl" as frame> <#global menu="广告主用户列表">
<@frame.html title="广告主用户列表"
js=["js/layer-v1.9.3/layer/layer.js","js/layer.onload.js","js/jquery-dateFormat.js"]
css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/uploadprogess.css","css/liselect/pkg-generator.css$ver=1431443489.css"]>

<style type="text/css">
.operation
{
	color: #E0296C;
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
             "aaSorting": [[3, "desc"]],
               "iDisplayLength" : 50,
            "aLengthMenu": [[20, 50, 100], [20, 50, 100]],
            "columnDefs": [
                {
                    "sClass": "align-left", "targets": [0,1] ,
                    "orderable": false, "targets": [1, 2,4,5,6]
                },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/user/ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val(),
                        "filter[ustats]" : $('#ustats').val(),
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
                    { "data": "company", "defaultContent": ""},
                     { "data": "created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm");
                	return d;
                }},
                    
             
                { "data": "enabled", "defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="processed">正常</span>';
                        default :
                            return '<span class="invalid">禁用</span>';
                    }
                } },
                 { "data": "ustats", "defaultContent": "", "render": function(data) {
                    if (data == 'init')
                            return '<span class="processed layer-tips" tip="已认证的用户可以参与竞价!">未上传资质</span>'; 
                        if (data == 'authentication')
                    return '<span class="invalid">认证通过</span>'; 
                        if (data == 'upload')
                    return '<span class="invalid">待认证</span>';
                        if (data == 'unauthentication')
                    return '<span class="invalid">资质不合格</span>';
                     
                } },
                
                { "data": function( row, type, set, meta) {
                    return row.username;
                },
                    "render": function(data, type, row, meta) {
                        return (row.enabled ? '<a class="table-action operation" href="javascript:void(0);" url="${rc.contextPath}/user/' + data + '/disable">禁用</a> &nbsp;'
                                :'<a class="table-action operation " href="javascript:void(0);" url="${rc.contextPath}/user/' + data + '/enable">启用</a> &nbsp;')
                        + '<a class="operation" href="${rc.contextPath}/user/editAdUser/' + data + '" >编辑</a> &nbsp;'
                        +(row.ustats=='init' ? '':'<a href="javascript:void(0)" class="operation" onclick="UserQualifi(\'${rc.contextPath}\',\'' + row.username + '\');" >查看资质</a>&nbsp;'
                        +(row.ustats!='authentication' ? '<a class="table-action operation" href="javascript:void(0);" url="${rc.contextPath}/user/ustats/' + data + '/authentication">认证通过</a> &nbsp;'
                                :'<a class="table-action operation" href="javascript:void(0);" url="${rc.contextPath}/user/ustats/' + data + '/unauthentication">撤销认证</a> &nbsp;'))
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
        $("div#toolbar").html(
                '<div>' +
                        '    <span>用户名</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    </span>' +
                          '    <span>' +
                        '      认证状态' +
                        '    </span>' +
	                         '<select class="ui-input ui-input-mini" name="ustats" id="ustats">' +
	                    '<option value="defaultAll" selected="selected">所有状态</option>' +
	                     	'<option value="upload">待认证</option>' +
	                  	'<option value="authentication">认证通过</option>' +
	                    '<option value="unauthentication">资质不合格</option>' +
	                    '<option value="init">未上传资质</option>' +
	         			'</select>' +
                        '</div>'
        );

        $('#name,#ustats').change(function() {
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
    
     

    $(document).ready(function() {
        initTable();
         bindLayerMouseOver();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title">
	广告主用户列表 <a class="block-btn" href="${rc.contextPath}/register">添加用户</a>
	</div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th orderBy="username">用户名</th>
				<th>所属组</th>
				<th>所属公司</th>
				<th orderBy="created">注册时间</th>
				<th>状态</th>
				<th>认证状态</th>
				<th>管理</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
