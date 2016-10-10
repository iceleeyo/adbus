<#import "template/template.ftl" as frame> <#global menu="销售员客户管理 ">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="销售员客户管理 "
js=["js/layer-v1.9.3/layer/layer.js","js/layer.onload.js","js/jquery-dateFormat.js","js/jquery-ui/jquery-ui.auto.complete.js"]
css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css","css/uploadprogess.css","css/liselect/pkg-generator.css$ver=1431443489.css"]>

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
            "ordering": false,
            "serverSide": true,
              "scrollX": true,
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
                url: "${rc.contextPath}/user/ajax-clientList",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[company]" : $('#company').val(),
                        "filter[relateMan]" : $('#relateMan').val(),
                        "filter[salesMan]" : $('#salesMan').val()
                        
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "company", "defaultContent": ""},
                { "data": "companyAddr", "defaultContent": ""},
                { "data": "createBySales", "defaultContent": ""},
                { "data": "legalman", "defaultContent": ""},
                { "data": "relateman", "defaultContent": ""},
                { "data": "phone", "defaultContent": ""},
                { "data": "created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
               
                
                { "data": function( row, type, set, meta) {
                    return row.username;
                },
                    "render": function(data, type, row, meta) {
                    
                        var op=''
                        if(row.createBySales == '<@security.authentication property="principal.user.id"/>'){
                         op+='<a class="operation" href="javascript:void(0);" onclick="deleteClinent(\''+data+'\');" >删除</a> &nbsp;'
                        }
                         op+='<a class="operation" href="${rc.contextPath}/user/editClient/' + data + '" >编辑</a> &nbsp;'
                         op+='<a class="operation" href="${rc.contextPath}/user/customerHistory/' + row.id + '" >修改历史</a> &nbsp;'
                         op+='<a class="operation" href="${rc.contextPath}/user/clientUser_invoice/' + data + '" >发票信息</a> &nbsp;'
                       return op; 
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
                        '    <span>公司名称</span>' +
                        '    <span>' +
                        '        <input style="width: 440px;" id="company" value="">' +
                        '    </span>' +
                        '    &nbsp;&nbsp;<span>联系人</span>' +
                        '    <span>' +
                        '        <input id="relateMan" value="">' +
                        '    </span>' +
                        <@security.authorize ifAnyGranted="salesManager">
                        '    &nbsp;&nbsp;<span>业务员</span>' +
                        '    <span>' +
                        '        <input id="salesMan" value="">' +
                        '    </span>' +
                         </@security.authorize>
                        '</div>'
        );

        $('#company,#relateMan,#salesMan').change(function() {
            table.fnDraw();
        });
          <@security.authorize ifAnyGranted="salesManager">
      			 initSalesAutocomplete(table);
	   	  </@security.authorize>
        bindLayerMouseOver();
    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }
    function deleteClinent(username){
      layer.confirm('确定删除吗？', {icon: 3}, function(index){
    		layer.close(index);
		    if(true){
		    	 $.ajax({
		    			url:"${rc.contextPath}/user/deleteClinent/"+username,
		    			type:"POST",
		    			async:false,
		    			dataType:"json",
		    			success:function(data){
		    				if (data.left) {
		    					layer.msg(data.right);
		    					table.fnDraw(true);
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
         bindLayerMouseOver();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title">
		客户列表 <a class="block-btn" href="${rc.contextPath}/user/addClientUser">添加客户</a>
	</div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th>公司名称</th>
				<th>公司地址</th>
				<th>所属业务员</th>
				<th>法定代表人</th>
				<th>联系人</th>
				<th>联系电话</th>
				<th>创建时间</th>
				<th>管理</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
