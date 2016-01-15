<#import "template/template.ftl" as frame> <#global menu="空媒体查询">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="空媒体查询"
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
	
	
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": false,
            "serverSide": true,
              "scrollX": true,
            "columnDefs": [
                { "orderable": false, "targets": [7] },
            ],
            "iDisplayLength" : 50,
            "aLengthMenu": [[50, 100], [50, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/api/ajax-use",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#linename').val(),
                        "filter[company]" : $('#company').val(),
                         "filter[level]" : $('#levelStr').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                
                { "data": "line.name", "defaultContent": ""},
                  { "data": "levelString", "defaultContent": ""},
                   { "data": "line.office", "defaultContent": ""},
                    { "data": "line.company.name", "defaultContent": ""},
                    
                    
                    { "data": "view.cars", "defaultContent": ""},
                    { "data": "view.online", "defaultContent": ""},
                     { "data": "view.orders", "defaultContent": ""},
                 { "data": "view.nowCanUseCar", "defaultContent": ""},
                  { "data": "view.nextMonthEndCount", "defaultContent": ""},
                  
                   { "data": "view.nextMonthCanUseCar", "defaultContent": ""},
                    { "data": "view.onlineBl", "defaultContent": ""},
                     { "data": "view.useBl", "defaultContent": ""},
                      { "data": "view.notDown", "defaultContent": ""}
            
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
                          '   <span>线路：</span>' +
                        '    <span>' +
                        '        <input id="linename" value="" ' +
                        '    <span>线路级别</span>&nbsp;&nbsp;' +
                        '<select class="ui-input ui-input-mini" name="levelStr" id="levelStr">' +
                   		'<option value="defaultAll" selected="selected">所有</option>' +
                  		'<option value="S">特级</option>' +
                  		'<option value="APP">A++</option>' +
                  		'<option value="AP">A+</option>' +
                  		'<option value="A">A</option>' +
                  		'<option value="LATLONG">无</option>' +
         				'</select>&nbsp;&nbsp;' +
         				'    <span>营销中心</span>' +
                    '<select class="ui-input ui-input-mini" name="company" id="company">' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	    <#list companys as c>
					'<option value="${c.id}">${c.name}</option>'+
					    </#list>
         			'</select>' +
                        '</div>'
        );

        $('#levelStr,#company').change(function() {
            table.fnDraw();
        });
        $('#linename').change(function() {
            ishaveline($("#linename").val());
            table.fnDraw();
        });
        
   			$("#linename").autocomplete({
		minLength: 0,
			source : "${rc.contextPath}/busselect/autoComplete?tag=a",
			change : function(event, ui) {
			},
			select : function(event, ui) {
				$('#linename').val(ui.item.value);
				table.fnDraw();
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   	 	});	 
   				 
    }
    function ishaveline(linename){
     $.ajax({
				url :  "${rc.contextPath}/busselect/ishaveline/"+linename,
				type : "POST",
				success : function(data) {
				if(!data.left){
				    layer.msg(data.right);
				  }
				 }
			}, "text");
    }
	function fnDrawCallback(){
	  $('.table-action').click(function() {
			$.post($(this).attr("url"), function(data) {
			if(data.left){
			    layer.msg(data.right);
				 table.dataTable()._fnAjaxUpdate();
				 }else{
				  layer.msg(data.right);
				 }
			});
		});
    }
    function drawCallback() {
    }

    $(document).ready(function() {
        initTable();
    } );
</script>


<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title">
		<span> 空媒体查询</span>
	</div>
	<table id="table" class="display compact" cellspacing="0" width="100%">
		<thead>
			<tr style="height: 40px;">
				<th>线路名</th>
				<th>线路级别</th>
				<th>所属公司</th>
				<th>营销中心</th>
				<th>配车数</th>
				<th>广告在刊</th>
				<th>合同预订</th>
				<th>本月可用数</th>
				<th>次月下刊数</th>
				<th>次月可用数</th>
				<th>媒介在刊率</th>
				<th>媒介占有率</th>
				<th>到期未下刊</th>
			</tr>
		</thead>

	</table>
	<input type="hidden" id="address" value=""> <input
		type="hidden" id="siteLine" value=""> <span>1:<b>广告在刊</b>
		当天广告在刊的车辆总数<br> 2:<b>合同预订</b> 线路预订的数量减广告在刊,如产生负数表示多上刊了车辆<br>
		3:<b>本月可用数</b> 配车数-广告在刊-合同预订<br> 4:<b>次月下刊数</b> 下刊车辆在下个月任一天的车辆总数<br>
		5:<b>次月可用数</b> 本月可用数+次月下刊数<br> 6:<b>媒介在刊率</b> 在刊广告车数/车辆配车<br>
		7:<b>媒体占有率</b> (在刊广告车数+预定车数)/车辆配车<br>
	</span>
</div>



</@frame.html>
