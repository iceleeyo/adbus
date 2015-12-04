<#import "template/template.ftl" as frame> <#global menu="剩余时段">
<@frame.html title="剩余时段" js=["js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.js", "js/datepicker.js",
"js/jquery.datepicker.region.cn.js"] css=["js/jquery-ui/jquery-ui.css"]>

<style type="text/css">
#table {
	font-size: 13px;
}

#table td {
	position: relative;
}

.per-occupied {
	position: absolute;
	background-color: #ffad20;
	left: 0;
	top: 0;
	height: 4px;
}

.per-free {
	position: absolute;
	background-color: #4acd48;
	right: 0;
	top: 0;
	height: 4px;
}
</style>
<style type="text/css">
.ui-datepicker-calendar.only-month {
	display: none;
}

.report-toolbar {
	float: right;
}

.report-toolbar .ui-label-mini {
	font-size: 12px;
	line-height: 35px;
}
</style>
<script type="text/javascript">

function getDateDiff(startDate,endDate){  
	var startTime = new Date(startDate);     
	var endTime = new Date(endDate);     
	var days = (startTime - endTime)/(1000*60*60*24);     
	return  days;    
}


function checkEndTime(){  
    var startTime=$("#day").val();  
    var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
    var endTime=$("#end").val();  
    var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
    if(end<start){  
    	 layer.msg("结束时间必须晚于开始时间！");
        return false;  
    }  
    var t=getDateDiff(end,start);
    if(t>60){
   		layer.msg("查询范围过大,暂限定60天！");
        return false;
    }
    return true;  
}  
    $(function(){
            $("#day").val(<#if from??>'${from}'<#else>$.format.date(new Date(), 'yyyy-MM-dd')</#if>);
            var endD = new Date();
            $("#end").val(<#if end??>'${end}'<#else>$.format.date((d.getTime() + 10*24*60*60*1000), 'yyyy-MM-dd')</#if>);
        $("#day").change(function() {
        	if(!checkEndTime()){
        		 return;
        	}else{
            	$(location).attr('href', "report?from=" + $("#day").val() +"&end="+ $("#end").val()      );
        	}
	    });
        
        $("#end").change(function() {
        	if(!checkEndTime()){
        		return;
        	}else{
                $(location).attr('href',  "report?from=" + $("#day").val() +"&end="+ $("#end").val()   );
        	}
        });
    });

</script>
<script type="text/javascript">
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">rt',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "ajax": {
                type: "POST",
                url: "${rc.contextPath}/schedule/box-ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val(),
                        "filter[from]" : "${from}",
                         "filter[days]" : "${days}",
                        "filter[end]" : "${end}",
                        "filter[type]" : "${type}"
                    } );
                },
                "dataSrc": function(json) {return json;},
            },
            "columns": [
                { "data": "slot.name", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                        var filter = $('#name').val();
                        if (filter && filter != '') {
                            var regex = new RegExp(filter, "gi");
                            data = data.replace(regex, function(matched) {
                                return "<span class=\"hl\">" + matched + "</span>";
                            });
                        }
                    return data;
                } },
                {
                    "data" : "slot.startTimeStr", "defaultContent": ""
                },
                {
                    "data" : "slot.durationStr", "defaultContent": ""
                },
    <#list dates as d>
                { "data": "", "defaultContent": "",
                    "render" : function(data, type, row, meta) {
                        var freePer = row.boxes['${d}'] ? (row.boxes['${d}'].remain * 100 / row.boxes['${d}'].size) : 100;
                        return (row.boxes['${d}'] ? row.boxes['${d}'].remainStr : row.slot.durationStr) +
                                '<span class="per-free" style="width:' + (100-freePer) + '%"></span>' +
                                '<span class="per-occupied" style="width:' + freePer + '%"></span>';
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
        $("div#toolbar").html(
                '<div>' +
                        '    <span>广告包名称：</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    </span>' +'    <span>[备注：黄色代表占用时段，绿色代表剩余时段。]</span>' +
                        '</div>'
        );

        $('#name').change(function() {
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

    $(document).ready(function() {
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<#--
	<div class="div" style="margin-top: 25px">
		<caption>
			<h2>产品套餐列表</h2>
		</caption>
	</div>
	<div class="div">
		<hr />
	</div>
	-->
	<div class="withdraw-title">
		剩余时段
		
		<div class="report-toolbar">
		<span>
		结束时间：
		</span>
			<input class="ui-input ui-input-mini datepicker" type="text"
				name="end" id="end" data-is="isAmount isEnough" autocomplete="off"
				disableautocomplete="">
		</div>
		<div class="report-toolbar">
		<span>
		开始时间：
		</span>
			<input class="ui-input ui-input-mini datepicker" type="text"
				name="day" id="day" data-is="isAmount isEnough" autocomplete="off"
				disableautocomplete="">
		</div>
		

	</div>

	<table id="table" class="cell-border compact display" cellspacing="0"
		width="100%">
		<thead>
			<tr>
				<th>广告包名称</th>
				<th>播出时间</th>
				<th>包长</th> <#list dates as d>
				<th>${d?substring(5)}</th> </#list>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
