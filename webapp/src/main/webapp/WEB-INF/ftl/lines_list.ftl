<#import "template/template.ftl" as frame>
<#global menu="线路管理">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="线路管理" js=["js/jquery-dateFormat.min.js","js/datepicker.js","js/jquery.datepicker.region.cn.js","js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js"] 
css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

<style type="text/css">
    .center {margin: auto;}
    .frame {width: 1000px;}
    .div {text-align:center; margin:25px;}
    div#toolbar {float: left;}
    .processed {color: limegreen;}
    .invalid {color: red;}
    .hl {background-color: #ffff00;}
</style>

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
                        "filter[name]" : $('#linename').val(),
                         "filter[level]" : $('#levelStr').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "name", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                    return '<a  target="_Blank" href="${rc.contextPath}/busselect/lineschedule/' + row.id + '" >'+data+'</a> &nbsp;';
                } },
                { "data": "levelStr", "defaultContent": ""},
                 { "data": "company.name", "defaultContent": ""},
                  { "data": "office", "defaultContent": ""},
                   { "data": "branch", "defaultContent": ""},
                    { "data": "_cars", "defaultContent": ""},
                    { "data": "linetype", "defaultContent": ""},
                    { "data": "description", "defaultContent": ""},
                     { "data": "routelocation", "defaultContent": ""},
                 { "data": "tolength", "defaultContent": ""},
                  { "data": "status", "defaultContent": ""},
                { "data": function( row, type, set, meta) {
                    return row.id;
                    },
                    "render": function(data, type, row, meta) {
                        var operations ='';
                         if(row.isdelete==0 || row.isdelete=='' ){
                         operations +='<a onclick="showLineDetail(\'${rc.contextPath}\','+data+')">编辑</a>';
                          operations += '&nbsp;&nbsp;<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/busselect/ajax-remove-line/0?id=' + data +'">删除</a>';
                       	}else {
                       		 operations += '&nbsp;&nbsp;<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/busselect/ajax-remove-line/1?id=' + data +'">恢复</a>';
                       	}
                        return operations;
                    }
                },
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
         				'</select>' +
                        '</div>'
        );

        $('#levelStr').change(function() {
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
               <span> 线路列表</span>
                <a class="block-btn" style="margin-left: 10px;" href="javascript:void(0);" onclick="addline('${rc.contextPath}')">添加线路</a>&nbsp;
                
									</div>
                <table id="table" class="display compact" cellspacing="0" width="100%">
                    <thead>
                    <tr style="height: 40px;">
                        <th >线路名</th>
                        <th >线路级别</th>
                          <th >所属营销中心</th>
                        <th >所属公司</th>
                        <th >所属分公司</th>
                        <th >车辆总数</th>
                        <th >线路类型</th>
                        <th >车辆详情</th>
                         <th >线路总里程</th>
                        <th >途径地点</th>
                        <th >状态</th>
                        <th>管理</th>
                    </tr>
                    </thead>

                </table>
                <input type="hidden" id = "address" value="">
                  <input type="hidden" id = "siteLine" value="">
</div>


 
</@frame.html>