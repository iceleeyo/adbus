<#import "template/template.ftl" as frame> <#global menu="媒体定购">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="线路列表"
js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js"]
css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

<link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/basecss.css">

<script type="text/javascript">
 	var table;
	function checkLocation(tourl){
	tourl+="?address="+$('#location').val();
	layer.open({
		type: 1,
		title: "查询位置确认",
		skin: 'layui-layer-rim', 
		area: ['600px', '450px'], 
		content:''
			+' '
			+'<iframe style="width:99%;height:98%" frameborder="no" src="'+tourl+'"/>'
	});
	}
	
	
	function showSite(tourl){
	layer.open({
		type: 1,
		title: "线路站点",
		skin: 'layui-layer-rim', 
		area: ['650px', '660px'], 
		content:''
			+' '
			+'<iframe style="width:99%;height:98%" src="'+tourl+'"/>'
	});
	}
   
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "columnDefs": [
                { "orderable": false, "targets": [7] },
            ],
            "iDisplayLength" : 50,
            "aLengthMenu": [[50, 100], [50, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/api/ajax-all-lines",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val(),
                        "filter[level]" : $('#levelStr').val(),
                        "filter[address]" : $('#address').val(),
                        "filter[siteLine]" : $('#siteLine').val(), 
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "name", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                    return '<a  target="_Blank" href="${rc.contextPath}/busselect/lineschedule/' + row.id + '" >'+data+'</a> &nbsp;';
                } },
                { "data": "levelStr", "defaultContent": ""}, { "data": "_cars", "defaultContent": ""},
                { "data": "_persons", "defaultContent": ""},
                 { "data": "_today", "defaultContent": ""},
                  { "data": "_month1day", "defaultContent": ""},
                   { "data": "_month2day", "defaultContent": ""},
                    { "data": "_month3day", "defaultContent": ""},
                 { "data": "line.levelStr", "defaultContent": "","render": function(data, type, row, meta) {
                     var _t='';
                     if(row._sim>0){
                     _t="["+row._sim+"]";
                     }
                        return '<a href="javascript:;" onclick="showSite('+ "\'${rc.contextPath}/api/lineMap?lineName="+row.name+"\' " +');">线路情况'+_t+'</a>&nbsp;';
                    }},
                    
                <@security.authorize ifAnyGranted="BodyOrderManager">
                { "data": function( row, type, set, meta) {
                    return row.id;
                    },
                    "render": function(data, type, row, meta) {
                        var operations = '';

                            operations+= (row.enabled ? '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/bus/line/' + data + '/disable">禁用</a> &nbsp;'
                                    :'<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/bus/line/' + data + '/enable">启用</a> &nbsp;')
                            operations +='<a class="table-link" href="${rc.contextPath}/bus/line/' + data +'">编辑</a>&nbsp;';

                        return operations;

                    }
                },
                </@security.authorize>
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
             "fnDrawCallback": fnDrawCallback,
        } );
        table.fnNameOrdering("orderBy").fnNoColumnsParams();
    }
    
    function redrawWithNewCount(t, row_count)
  {
    //Lifted more or less right out of the DataTables source
    var oSettings = t.fnSettings();
 
    oSettings._iDisplayLength = parseInt(row_count, 10);
    t._fnCalculateEnd( oSettings );
     
    /* If we have space to show extra rows (backing up from the end point - then do so */
    if ( oSettings.fnDisplayEnd() == oSettings.fnRecordsDisplay() )
    {
      oSettings._iDisplayStart = oSettings.fnDisplayEnd() - oSettings._iDisplayLength;
      if ( oSettings._iDisplayStart < 0 )
      {
        oSettings._iDisplayStart = 0;
      }
    }
     
    if ( oSettings._iDisplayLength == -1 )
    {
      oSettings._iDisplayStart = 0;
    }
     
    t.fnDraw( oSettings );
 
    return t;
  }
  
  
	function searchLine(){
	 var w=$('#location').val();
	 $('#address').val(w);
	 
	  var oSettings = table.fnSettings();
        oSettings._iDisplayLength = 50;
	  redrawWithNewCount(table, 50);
	 //table.dataTable().fnSetDisplayLength = 100;
	 //table.dataTable()._fnAjaxUpdate();
	 // table.fnDraw();
	}
	
	function searchSite(){
	 var w=$('#siteName').val();
	 $('#siteLine').val(w);
	 
	  var oSettings = table.fnSettings();
        oSettings._iDisplayLength = 50;
	  redrawWithNewCount(table, 50);
	 //table.dataTable().fnSetDisplayLength = 100;
	 //table.dataTable()._fnAjaxUpdate();
	 // table.fnDraw();
	}
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>附近线路：</span>' +
                        '    <span>' +
                        '        <input id="location" value="" style="width:300px">' +
                        '&nbsp;&nbsp;<a href="javascript:;" onclick="checkLocation('+ "\'${rc.contextPath}/api/simple\'" +');">位置确认</a>&nbsp;'+
                          '  <a href="javascript:;" onclick="searchLine();">查附近线路</a>&nbsp;'+
                          '    <span>线路：</span>    <input id="siteName" value=""> ' +
                        '    &nbsp;&nbsp;' +
                               '  <a href="javascript:;" onclick="searchSite();">线路相似匹配</a>&nbsp;'+
                          ' <br> <br>   <span>线路：</span>' +
                        '    <span>' +
                        '        <input id="name" value="" ' +
                        '    <span>&nbsp;&nbsp;线路级别</span>&nbsp;&nbsp;' +
                        '<select class="ui-input ui-input-mini" name="levelStr" id="levelStr">' +
                   		'<option value="defaultAll" selected="selected">所有</option>' +
                  		'<option value="S">特级</option>' +
                  		'<option value="APP">A++</option>' +
                  		'<option value="AP">A+</option>' +
                  		'<option value="A">A</option>' +
                  		'<option value="LATLONG">经纬线</option>' +
         				'</select>' +
                        ''+
                        '</div>'
        );

        $('#name,#levelStr').change(function() {
        	$('#address').val("");
        	$('#location').val("");
            table.fnDraw();
        });
        
        /*$("#name").autocomplete({
			minLength: 0,
			source : "${rc.contextPath}/busselect/autoComplete",
			change : function(event, ui) {
			},
			select : function(event, ui) {
				$('#line_id').val(ui.item.value);
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   				 });*/
   				 
   				 
    }
	function fnDrawCallback(){
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
		<span> 线路列表</span> <a class="block-btn" target="_Blank"
			href="${rc.contextPath}/busselect/applyBodyCtct">申请合同</a>
	</div>
	<table id="table" class="display compact" cellspacing="0" width="100%">
		<thead>
			<tr style="height: 40px;">
				<th orderBy="name">线路名</th>
				<th orderBy="level">线路级别</th>
				<th orderBy="_cars">自营车辆</th>
				<th orderBy="_persons">人车流量</th>
				<th orderBy="_today">当天上刊数</th>
				<th orderBy="_month1day">未来1月</th>
				<th orderBy="_month2day">未来2月</th>
				<th orderBy="_month3day">未来3月</th>
				<th>查看站点</th> <@security.authorize ifAnyGranted="BodyOrderManager">
				<th>管理</th> </@security.authorize>
			</tr>
		</thead>

	</table>
	<input type="hidden" id="address" value=""> <input
		type="hidden" id="siteLine" value="">
</div>



</@frame.html>
