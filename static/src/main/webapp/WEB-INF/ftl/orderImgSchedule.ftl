<#import "template/template_Nleft.ftl" as frame> <#import
"template/orderDetail.ftl" as orderDetail/> 
<#global menu="排期表">  <@frame.html title=menu css=["js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css","css/layer.css"]
js=["js/layer.min.js","js/jquery-ui/jquery-ui.auto.complete.js","js/jquery-dateFormat.js","js/layer-v1.9.3/layer-site.js"]>

<style type="text/css">
.center {
	margin: auto;
}

.frame {
	width: 1000px;
}

.div {
	text-align: center;
	margin: 10px;
}

div#toolbar {
	float: left;
}

.processed {
	color: limegreen;
}

.invalid {
	color: red;
}

.hl {
	background-color: #ffff00;
}

#table {
	font-size: 14px;
}

#table td {
	position: relative;
}

#table td .per-middle {
	position: absolute;
	background-color: #ffad20;
	border-left: 1px solid white;
	border-right: 1px solid white;
	top: 0;
	height: 100%;
	z-index: 1
}

#table td .per-first-or-last {
	position: absolute;
	background-color: #4acd48;
	border-left: 1px solid white;
	border-right: 1px solid white;
	top: 0;
	height: 100%;
	z-index: 1
}
.ls-10{
	margin-left: 7%;
}
</style>
<script type="text/javascript">
    var table;
    function initTable () {
        $('#metatable').dataTable({
            "dom": 'rt',
            "searching": false,
            "ordering": false,
            "serverSide": false,
        });

        table = $('#table').dataTable( {
            "dom": '<"#toolbar">rt',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "ajax": {
                type: "POST",
                url: "${rc.contextPath}/schedule/orderSchedule/image",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[orderId]" : ${orderId!''}
                    } );
                },
                "dataSrc": function(json) {return json;},
            },
            "columns": [
               
                   {
                    "data" : "date", "defaultContent": "","render": function(data, type, row, meta) {
                       var d= $.format.date(data, "yyyy-MM-dd");
                      return d;
                }},
                {
                    "data" : "order.supplies.seqNumber", "defaultContent": ""
                },
                {
                    "data" : "order.supplies.name", "defaultContent": ""
                },
                { "data": "", "defaultContent": "",
                    "render" : function(data, type, row, meta) {
                        return '新';
                }},
                {
                    "data" : "attachment.name", "defaultContent": ""
                },
                {
                    "data" : "duration", "defaultContent": ""
                },
                {
                    "data" : "deleted", "defaultContent": "","render": function(data, type, row, meta) {
                      if(data){
                        return '<font color="red">已失效</font>';
                      }
                      return '<font color="green">正常</font>';
                }},
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
        } );
    }

    function initComplete() {
    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }
   function export_xls(){
   
          $.ajax({
					    			url:'${rc.contextPath}/schedule/exportOrderSchedule/image',
					    			type:"GET",
					    			dataType:"json",
					    			data:{"filter[orderId]" : ${orderId!''}},
					    			success:function(data){
                                        location.href='../'+data;
					    			}
					       });  
   }
    $(document).ready(function() {
        initTable();
    } );
</script>
 
<div class="p20bs mt10 withdraw-wrap color-white-bg fn-clear">
	<H3 class="text-xl title-box">
		<A class="black" href="#">排期表【排期内容很多，请上下左右拖拽，可以看到全部排期。】</A>
	</H3>
	<div class="div" style="overflow-x: auto;">
	<div style="float:left">
<a href="javascript:void()" onclick="export_xls();" class="btn-sm btn-success">导出Excel</a>
</div>
		<table id="table" class="cell-border compact display" cellspacing="0"
			width="100%">
			<thead>
				<tr>
				<th>日期</th>
				<th>审批合格号</th> 
				<th>图片包名称</th>
				<th>性质</th>
				<th>图片名称</th>
				<th>时长(秒)</th>
				<th>状态</th>
				</tr>
			</thead>

		</table>


	</div>

</div>

</@frame.html>
