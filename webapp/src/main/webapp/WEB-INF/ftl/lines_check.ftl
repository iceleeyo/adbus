<#import "template/template.ftl" as frame>
<#global menu="线路核实">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="线路核实" js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js",
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
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": false,
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
                 
               
                  { "data": "office", "defaultContent": ""},
                  { "data": "branch", "defaultContent": ""},
                    { "data": "company.name", "defaultContent": ""},
                    { "data": "_cars", "defaultContent": ""},
                    
                    { "data": "_cars", "defaultContent": ""},
                    { "data": "_cars", "defaultContent": ""},
                     { "data": "_cars", "defaultContent": ""},
             
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
                        '<select class="ui-input ui-input-mini" name="levelStr" id="levelStr" style="width:105px">' +
                   		'<option value="defaultAll" selected="selected">所有</option>' +
                  		'<option value="S">特级</option>' +
                  		'<option value="APP">A++</option>' +
                  		'<option value="AP">A+</option>' +
                  		'<option value="A">A</option>' +
         				'</select>' +
         				' <span>所属公司</span>&nbsp;&nbsp;' +
                        '<select class="ui-input ui-input-mini" name="levelStr" id="levelStr" style="width:125px">  ' +
                   		'<option value="defaultAll" selected="selected">所有</option>' +
                  		'<option value="大公共公司">大公共公司</option>' +
                  		'<option value="八方达公司">八方达公司</option>' +
         				'</select>' +
         					' <span>所属分公司</span>&nbsp;&nbsp;' +
                        '<select class="ui-input ui-input-mini" name="levelStr" id="levelStr" style="width:125px">  ' +
                   		'<option value="defaultAll" selected="selected">所有</option>' +
                  		'<option value="客一">客一</option>' +
                  			'<option value="客二">客二</option>' +
                  				'<option value="客三">客三</option>' +
                  					'<option value="客四">客四</option>' +
                  						'<option value="客五">客五</option>' +
                  							'<option value="客六">客六</option>' +
                  								'<option value="客七">客七</option>' +
                  		'<option value="电运">电运</option>' +
                  		'<option value="延庆">延庆</option>' +
                  		'<option value="天桥">天桥</option>' +
                  		'<option value="怀柔">怀柔</option>' +
                  		'<option value="河滩">河滩</option>' +
                  		'<option value="通州">通州</option>' +
                  		'<option value="大兴">大兴</option>' +
                  		'<option value="东直门">东直门</option>' +
         				'</select>' +
         				'<br><br>' +
         				' <span>营销中心</span>&nbsp;&nbsp;' +
                        '<select class="ui-input ui-input-mini" name="levelStr" id="levelStr" style="width:125px">  ' +
                   		'<option value="defaultAll" selected="selected">所有</option>' +
                  		'<option value="七彩">七彩</option>' +
                  		'<option value="自营中心">自营中心</option>' +
                  		'<option value="白马">白马</option>' +
                  		'<option value="CBS">CBS</option>' +
                  		'<option value="待定">待定</option>' +
                  		'<option value="未定">未定</option>' +
         				'</select>' +
                        '</div>'
        );

        $('#linename,#levelStr').change(function() {
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
               <span> 线路核实</span>
									</div>
                <table id="table" class="display compact" cellspacing="0" width="100%">
                    <thead>
                    <tr style="height: 40px;">
                        <th >线路名称</th>
                        <th >线路级别</th>
                       
                        <th >所属公司</th>
                        <th >所属分公司</th>
                           <th >所属营销中心</th>
                           
                        <th >所属车辆数</th>
                        <th >所属运营车</th>
                        <th >白车数据</th>
                         <th >到期可用</th>
                       
                    </tr>
                    </thead>

                </table>
                <input type="hidden" id = "address" value="">
                  <input type="hidden" id = "siteLine" value="">
</div>


 
</@frame.html>