<#import "template/template_Nleft.ftl" as frame> <#import
"template/orderDetail.ftl" as orderDetail/> <#if mediaType == 'screen'>
<#global menu="排期表"> </#if> <@frame.html title=menu>

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
                url: "${rc.contextPath}/schedule/ajax-schedule",
                data: function(d) {
                    return $.extend( {}, d, {
                        "orderId" : "${orderId!''}"
                    } );
                },
                "dataSrc": function(json) {return json;},
            },
            "columns": [
               
                    { "data" : "timeslot.name", "defaultContent": "1"},
                    { "data" : "timeslot.startTimeStr", "defaultContent": "1"},
                    { "data" : "timeslot.durationStr", "defaultContent": "1"},
    <#list dates as d>
                    { "data":  "", "defaultContent": "","render" : function(data, type, row, meta) {
                        var result = '';
                        if (row.map2['${d}'] ) {
                            result= row.map2['${d}'];
                            if(result.del==1){
                            return '<p><s><font color="red">'+result.num+'</font></p></s>'
                            }
                            return result.num;
                            }
                    return "";
                }},
    </#list>
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
     location.href='${rc.contextPath}/schedule/writeExcel/${orderId}';
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
					<th style="min-width: 60px;">包名</th>
					<th>时段</th>
					<th>时长</th>
					 <#list dates as d>
					<th style="min-width: 30px;">${d?substring(5)}</th>
					 </#list>
				</tr>
			</thead>

		</table>


	</div>

	<div class="worm-tips">
		<div class="tips-title">
			<span class="icon"></span>排期说明：
			<ol>
				<li>1.一个方格代表某一天的一个广告时段。</li>
				<li>2.彩条代表该广告的播出位置，绿色代表首播或者末播，橘色代表中间播。</li>
			</ol>
		</div>
	</div>
</div>

</@frame.html>
