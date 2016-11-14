<#import "../template/QRtemplate.ftl" as frame> <#global menu="二维码列表 ">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="二维码列表 "
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
            "iDisplayLength" : 20,
            "aLengthMenu": [[5,20, 40, 100], [5,20, 40, 100]],
                          "aaSorting": [[1, "desc"]],
              "columnDefs": [
                 { "orderable": false, "targets": [0] },
            ],
           
            "ajax": {
                type: "GET",
                url: "http://60.205.168.48:9009/url/list",
                data: function(d) {
                    return $.extend( {}, d, {
                        
                    } );
                },
                 "dataSrc": function (json) { 
                    json.recordsTotal=json.totalElements;
                    json.recordsFiltered=json.totalElements;
                    console.log(json);
                   console.log(json.totalElements);
                    
                    return json.content;
                }
            },
            "columns": [
                { "data": "sourceUrl_s", "defaultContent": "","render": function(data, type, row, meta) {
                      var d= '<a href="'+data+'">'+data.substr(0,50)+'</a>' ;
                      return d;
                }},
                { "data": "description_s", "defaultContent": ""},
                { "data": "count_i", "defaultContent": "0"},
               
                { "data": "created_dt", "defaultContent": "","render": function(data, type, row, meta) {
                  var d= $.format.date(data, "yyyy-MM-dd");
                      return d;
                }},
                { "data": function( row, type, set, meta) {
                    return row.img;
                },
                
                    "render": function(data, type, row, meta) {
                    
                        var op=''
                         op+='<a class="operation"  onclick="showQRlayer(\''+data+'\');" >查看二维码</a> &nbsp;'
                         op+='<a class="operation"  href="${rc.contextPath}/report/QRdetail/'+row.targetUrl_s+'" >详情</a> &nbsp;'
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
                ''
        );

        $('#company,#relateMan,#salesMan').change(function() {
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
    
    function showQRlayer(data){
	layer.open({
		type: 1,
		title: "二维码",
		skin: 'layui-layer-rim', 
		area: ['400px', '400px'], 
		content:''
			+'<img src='+data+'/> '
	});
	
}
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title">
		二维码列表 <a class="block-btn" href="${rc.contextPath}/report/addQR">制作二维码</a>
	
	</div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th  >url</th>
				<th>说明</th>
				<th>扫码总量</th>
				<th>创建时间</th>
				<th>操作</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
