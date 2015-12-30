 <#import "template/template.ftl" as frame> <#global menu="排条单">



<@frame.html title="线路列表"
js=["js/jquery-dateFormat.js","js/jquery-ui/jquery-ui.js",
"js/datepicker.js"
"js/jquery.datepicker.region.cn.js","js/jquery-ui/jquery-ui.auto.complete.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/jquery-dataTables-fnFakeRowspan.js"]
css=["js/jquery-ui/jquery-ui.css"]>

<!--  "js/tabletools/js/dataTables.tableTools.js"  "js/tabletools/css/dataTables.tableTools.min.css"-->

<style type="text/css">
#table.dataTable thead th:first-child,#table.dataTable thead td:first-child,#table.dataTable.cell-border tbody tr th:first-child,#table.dataTable.cell-border tbody tr td:first-child
	{
	display: none;
}

#table {
	font-size: 13px;
}

#table td {
	position: relative;
}

#table td .per-occupied {
	position: absolute;
	background-color: #ffad20;
	left: 0;
	top: 0;
	height: 4px;
}

#table td .per-free {
	position: absolute;
	background-color: #4acd48;
	right: 0;
	top: 0;
	height: 4px;
}

.report-toolbar {
	float: left !important;
	margin-top: 40px;
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

div#toolbar {
	float: left;
	margin-left: 77%;
	padding: 10px 0 10px 0;
	margin-top: -40px;
}
</style>
<script type="text/javascript">
    $(function(){
            $("#day").val(<#if day??>'${day}'<#else>$.format.date(new Date(), 'yyyy-MM-dd')</#if>);

        $("#day").change(function() {
            $(location).attr('href', "list?day=" + $("#day").val());
        });
    });
</script>


<script type="text/javascript">
 var sortTable;
    function initSortTable () {
        sortTable = $('#sortTable').dataTable( {
          /*  oLanguage: {
		        sProcessing: "<img src='${rc.contextPath}/imgs/load_.gif'>"
		    },*/
            "oLanguage": {
                "sSearch": "Search all columns:",
                "sLoadingRecords": "Please wait - loading...",
                "sProcessing": "正在加载中",
            },
		    processing : true,
            "dom": '<"#toolbar">rt',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "columnDefs": [
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/schedule/ajax-sortSolt",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[day]" : "${day}",
                         "filter[soltid]" : $("#hidSoleId").val(),
                    } );
                },
                "dataSrc": function(json) {return json;},
            },
            "columns": [
                { "data":  "suppliesId" ,"defaultContent": "","render" : function(data, type, row, meta) {
                        return row.supplieStr.seqNumber +" - "+row.supplieStr.name;
                }},
                {
                    "data" : "size", "defaultContent": ""
                },
            ],
            "language":{
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },"fnCreatedRow": function (nRow, aData, iDataIndex) {
                $(nRow).attr('id', aData.type+"_"+aData.id); // or whatever you choose to set as the id
            },
            "initComplete": initComplete2,
            "drawCallback": drawCallback2
        });
    }

    function initComplete2() {
    }
   

    function drawCallback2() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                sortTable.fnDraw(true);
            })
        });
    }
    
  



    var table;
    function initTable () {
        table = $('#table').dataTable( {
          /*  oLanguage: {
		        sProcessing: "<img src='${rc.contextPath}/imgs/load_.gif'>"
		    },*/
            "oLanguage": {
                "sSearch": "Search all columns:",
                "sLoadingRecords": "Please wait - loading...",
                "sProcessing": "正在加载中",
            },
		    processing : true,
            "dom": '<"#toolbar">rt',
/*            "tableTools": {
                "aButtons": [   {
                                "sExtends":     "xls",
                                "sButtonText": "导出Excel"
                                },
                                {
                                    "sExtends":     "pdf",
                                    "sButtonText": "导出PDF"
                                },
                                {
                                    "sExtends":     "print",
                                    "sButtonText": "打印预览"
                                }],
                "mColumns": [0, 1, 1, 1, 1, 1],
                "sSwfPath": "${rc.contextPath}/js/tabletools/swf/copy_csv_xls_pdf_2.swf"
            },*/
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "columnDefs": [
                { "width": "10%", "targets": 1 },
                { "width": "15%", "targets": 2 },
                { "width": "10%", "targets": 3 },
                { "sClass": "align-left", "targets": 4 },
                { "width": "10%", "targets": 5 }
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/schedule/box-detail-ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val(),
                        "filter[day]" : "${day}",
                        "filter[type]" : "${type}",
                          "filter[_loadBlack]" :  $('#_loadBlack').val(),
                    } );
                },
                "dataSrc": function(json) {return json;},
            },
            "columns": [
                { "data": "", "render" : function () { return "";}},
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
                    "data" : "slot.durationStr", "defaultContent": "", "render" : function(data, type, row, meta) {
                    var dayStr  = '${day}';
                  	  var  w = ' <a   onclick="fonfirm2(' +(dayStr) +","+(row.slot.id )+ ')" >修改</a> &nbsp;';
                        return data+w;
                }
                },
                { "data": "", "defaultContent": "",
                    "render" : function(data, type, row, meta) {
                        var box = row.boxes['${day}'];
                        return box && box.goods && box.goods.length ? (
                                box.goods[0].order.supplies.id?
                                '<a  href="javascript:void(0)" onclick="supDetail('
                                        + box.goods[0].order.supplies.id + ')" >'
                                        + box.goods[0].order.supplies.seqNumber + '-'
                                        + box.goods[0].order.supplies.name + '</a>' :
                                       box.goods[0].order.supplies.seqNumber +''+box.goods[0].order.supplies.name)
                                : '';
                }},
                { "data": "", "defaultContent": "",
                    "render" : function(data, type, row, meta) {
                        var box = row.boxes['${day}'];
                        return box && box.goods && box.goods.length ? box.goods[0].order.product.duration : '';
                }},
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
             "fnDrawCallback": fnDrawCallback
        });
        table.fnFakeRowspan(1, [1, 2, 3]).fnNameOrdering("orderBy").fnNoColumnsParams();
    }

    function initComplete() {
        $("div#toolbar").html(
                '<div style="float:right">' +
                        '    <span><a href="javascript:void()" id="export_xls" class="btn-sm " style="background: #ff9966">导出Excel</a>' +
                        '    <span><a href="javascript:void()" id="load_black" class="btn-sm btn-success">重新加载底版</a>' +
                        '    </span>' +
                        '</div>'
        );

        $("#export_xls").click(function(){
            location.href='${rc.contextPath}/schedule/list.xls?filter[day]=${day}';
        });
        
        $("#load_black").click(function(){
        $("#loading").show();
        $('#_loadBlack').val("Y")
            table.fnDraw();
        });

        $('#name').change(function() {
           $('#_loadBlack').val("N")
            table.fnDraw();
        });
    }
    //显示总条数 add by impanxh
    function fnDrawCallback(){
    	$("#loading").hide();
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
      //  initSortTable();
       //    sortTable.fnDraw();
    } );
    
    
//订单物料详情
    function supDetail(data){
	layer.open({
    		type: 1,
    		title: "物料及资质",
    		skin: 'layui-layer-rim', 
    		area: ['1000px', '529px'], 
    		content:''
			   +' '
			   +'<iframe frameborder="no" style="width:99%;height:99%" src="${rc.contextPath}/supplies/suppliesDetail/'+data+'"/>'
			});
}
	
  function fonfirm2(bid,lid){
    $('#hidSoleId').val(lid);
   		 var innerHTMLString = $("#ccc").html();
			layer.open({
                type: 1,
                title: "广告时段排序",
                skin: 'layui-layer-rim', 
                area: ['650px', '600px'], 
                content: '<br >'+'<div id ="ccc"  ><table id="sortTable" class="cell-border compact display" cellspacing="0" style="width: 100%; border-left-style: solid; border-left-width: 1px; border-left-color: rgb(221, 221, 221);">'
                +'<thead>'
                +'<tr>'
                +' <th>广告名称</th><th>时长</th>'
                +'</tr>'
                +'</thead>'
                +'</table>'
                +'</div>'
            });
          initSortTable();
          
   			 $(".layui-layer-close1").click(function(){
			    $("#loading").show();
			    $('#_loadBlack').val("N")
          		  table.fnDraw();
			});
			
			      
	  var fixHelperModified = function(e, tr) {
		    var $originals = tr.children();
		    var $helper = tr.clone();
		    $helper.children().each(function(index) {
		        $(this).width($originals.eq(index).width())
		    });
		    return $helper;
		},
		    updateIndex = function(e, ui) {
		        $('td.sorting_1', ui.item.parent()).each(function (i) {
		           // $(this).html(i + 1);
		        });
		         var _temp='';
		          $('#sortTable tbody tr').each(function(){
					_temp+=($(this).attr("id")) +",";
		        });
				    $.post("${rc.contextPath}/schedule/sortSolit",{
				        sortString: _temp,
				    },function(data){
						layer.msg('排序成功');    	
				    });		        
		    };
		$("#sortTable tbody").sortable({
		    helper: fixHelperModified,
		    stop: updateIndex
		}).disableSelection();
		          
	}
 
 
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title">
		<div class="tabs">
			<input type="hidden" id="_loadBlack" value="N" /> <input
				type="hidden" id="hidSoleId" value="0" /> <a id="tab1"
				class="active" href="${rc.contextPath}/schedule/list">视频广告排条单[${day}]</a>
			<a id="tab2" href="${rc.contextPath}/schedule/img-list">图片广告排条单</a> <a
				id="tab3" href="${rc.contextPath}/schedule/info-list">字幕广告排条单</a>
		</div>
		<div class="report-toolbar">
			<input class="ui-input ui-input-mini datepicker" type="text"
				name="day" id="day" data-is="isAmount isEnough" autocomplete="off"
				disableautocomplete=""> <span id="loading"><image
					src="${rc.contextPath}/imgs/load_.gif" /> </span>
		</div>
	</div>
	<table id="table" class="cell-border compact display" cellspacing="0"
		style="width: 100%; border-left-style: solid; border-left-width: 1px; border-left-color: rgb(221, 221, 221);">
		<thead>
			<tr>
				<th></th>
				<th>广告包名称</th>
				<th>播出时间</th>
				<th>包长</th>
				<th>广告名称</th>
				<th>时长</th>
			</tr>
		</thead>
	</table>
	</tbody>

</div>









</@frame.html>
