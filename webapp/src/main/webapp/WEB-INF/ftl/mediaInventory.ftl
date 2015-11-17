<#import "template/template.ftl" as frame>
<#global menu="媒体库存查询">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="媒体库存查询" js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js", "js/datepicker.js","js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css"]>

<style type="text/css">
    .center {margin: auto;}
    .frame {width: 1000px;}
    .div {text-align:center; margin:25px;}
    div#toolbar {float: left;}
    .processed {color: limegreen;}
    .invalid {color: red;}
    .hl {background-color: #ffff00;}
</style>
<style type="text/css">
.tdd{
    background-color: #3bafda;
    color: white;
    font-weight: normal;
    text-align: center;
    line-height: 30px;
    }
th, td { white-space: nowrap;border: 1px solid #C5C5C5;}
</style>
<script type="text/javascript">
	
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">rt',
            "searching": false,
            "ordering": false,
            "serverSide": false,
            "scrollX": true,
             "columnDefs": [
                 { "orderable": false, "targets": [0,1,2,3] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/schedule/ajax-mediaInventory",
                data: function(d) {
                    return $.extend( {}, d, {
                    "filter[day]" : $("#day").val()
                    } );
                },
                "dataSrc": function(json) {return json;},
            },
            "columns": [
                { "data": "bname", "defaultContent": ""},
                { "data": "bsize", "defaultContent": ""},
                { "data": "normalremain", "defaultContent": ""},
                { "data": "fremain", "defaultContent": ""},
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

    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }
function serch(){
 if($("#day").val()==""){
   layer.msg("请选择日期");
   return;
 }
  table.dataTable()._fnAjaxUpdate();
}
    $(document).ready(function() {
     $("#day").val(<#if day??>'${day}'<#else>$.format.date(new Date(), 'yyyy-MM-dd')</#if>);
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title">
                     <input
                            class="ui-input ui-input-mini datepicker" type="text" name="day"
                            id="day" data-is="isAmount isEnough"
                            autocomplete="off" disableautocomplete=""> <input type="button" class="block-btn" onclick="serch();" value="搜索"/>
									</div>
          <table id="table" class="cell-border compact display" cellspacing="0"
		style="width: 70%; border-left-style: solid; border-left-width: 1px; border-left-color: rgb(221, 221, 221);">
		<thead>
			<tr>
				<th>广告包名称</th>
				<th>包长(秒)</th>
				<th>非首播剩余时长(秒)</th>
				<th>首播剩余时长(秒)</th>
			</tr>
		</thead>
	</table>
</div>
</@frame.html>