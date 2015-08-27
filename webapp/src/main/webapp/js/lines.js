 	var table;
 	var chost;
	function checkLocation(tourl){
	tourl+="?address="+$('#location').val();
	layer.open({
		type: 1,
		title: "查询位置确认",
		skin: 'layui-layer-rim', 
		area: ['600px', '450px'], 
		content:''
			+' '
			+'<iframe style="width:99%;height:98%" src="'+tourl+'"/>'
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
   
    function initTable (cp,roleIndex) {
    	chost=cp;
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "columnDefs": [
                { "orderable": false, "targets": [3] },
            ],
            "iDisplayLength" : 50,
            "aLengthMenu": [[50, 100], [50, 100]],
            "ajax": {
                type: "GET",
                url: cp+"/api/public-lines",
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
               
                    if(roleIndex==0){
                    	return data;
                   }else{
                	     return '<a  target="_Blank" href="'+(cp)+'/busselect/lineschedule/' + row.id + '" >'+data+'</a> &nbsp;';   
                   }
                    
                } },
                { "data": "levelStr", "defaultContent": ""}, { "data": "_cars", "defaultContent": ""},
                { "data": "_persons", "defaultContent": ""},
                 { "data": "line.levelStr", "defaultContent": "","render": function(data, type, row, meta) {
                     var _t='';
                     if(row._sim>0){
                     _t="["+row._sim+"]";
                     }
                        return '<a href="javascript:;" onclick="showSite(\'/api/public_lineMap?lineName='+row.name+'\');">线路情况'+_t+'</a>&nbsp;';
                    
                    }},
                    
                 
            ],
            "language": {
                "url": cp+"/js/jquery.dataTables.lang.cn.json"
            },
            "fnCreatedRow": function (nRow, aData, iDataIndex) {
                $(nRow).attr('id', aData.id); // or whatever you choose to set as the id
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
  
  	function searchSite(){
	 var w=$('#siteName').val();
	 $('#siteLine').val(w);
	 $('#address').val('');
	  var oSettings = table.fnSettings();
        oSettings._iDisplayLength = 50;
	  redrawWithNewCount(table, 50);
	 //table.dataTable().fnSetDisplayLength = 100;
	 //table.dataTable()._fnAjaxUpdate();
	 // table.fnDraw();
	}
	function searchLine(){
	 var w=$('#location').val();
	 $('#address').val(w);
	 $('#siteLine').val('');
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
                        '    <span>' +
                        '        <input id="location" value="" placeholder="请输入地标,比如:北京市天池餐厅" style="width:300px">&nbsp;' +
                        '<a href="javascript:;" style="margin-left:10px;" onclick="checkLocation('+chost+ "\'/api/public_simple\'" +');">位置确认</a>&nbsp;'+
                          '  <a href="javascript:;" onclick="searchLine();">查附近线路</a>&nbsp;<br><br>'+
                           '      <input id="siteName" placeholder="请输入线路名称" value=""> ' +
                        '    &nbsp;&nbsp;' +
                               '  <a href="javascript:;" onclick="searchSite();">线路相似匹配</a>&nbsp;'+
                          ' <br> <br>   <span>线路</span>' +
                        '    <span>' +
                        '        <input id="name" value="" style="margin-right:10px;" ' +
                        '    <span>线路级别</span>&nbsp;&nbsp;' +
                        '<select class="ui-input ui-input-mini" name="levelStr" id="levelStr">' +
                   		'<option value="defaultAll" selected="selected">所有</option>' +
                  		'<option value="S">特级</option>' +
                  		'<option value="APP">A++</option>' +
                  		'<option value="AP">A+</option>' +
                  		'<option value="A">A</option>' +
                  		'<option value="LATLONG">经纬线</option>' +
         				'</select>' +
                        '</div>'
        );

        $('#name,#levelStr').change(function() {
        	$('#address').val("");
        	$('#location').val("");
        	$('#siteLine').val("");
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

    
