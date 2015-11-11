<#import "template/template.ftl" as frame> <#global menu="配车查询">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="配车查询"
js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

<style type="text/css">
.center {
	margin: auto;
}

.frame {
	width: 1000px;
}

.div {
	text-align: center;
	margin: 25px;
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
</style>

<script type="text/javascript">
	
    var table;
    function initTable () {
        table = $('#table').DataTable( {
			 "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "scrollX": true,
            "columnDefs": [
                { "orderable": false, "targets": [7, 8,9,10] },
            ],
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[oldserinum]" : $('#oldserinum').val(),
                        "filter[serinum]" : $('#serinum').val(),
                        "filter[plateNumber]" : $('#name').val(),
                        "filter[linename]" : $('#linename').val(),
                        "filter[category]" : $('#category').val(),
                        "filter[levelStr]" : $('#levelStr').val(),
                         "filter[contractid]" : $('#cid').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
             { "data": "", "defaultContent": ""},
             { "data": "jpaBus.line.name", "defaultContent": ""},
                { "data": "jpaBus.line.levelStr", "defaultContent": ""},
                { "data": "jpaBus.serialNumber", "defaultContent": ""},
                { "data": "jpaBus.oldSerialNumber", "defaultContent": ""},
                 { "data": "jpaBus.company.name", "defaultContent": ""},
                  { "data": "jpaBus.model.name", "defaultContent": ""},
                { "data": "busInfo.contractCode", "defaultContent": ""},
                  { "data": "busInfo.offlinecontract.adcontent", "defaultContent": ""},
                { "data": "busInfo.startD", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                { "data": "busInfo.endD", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                  { "data": "busInfo.busOnline.realEndDate","defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                    { "data": "busInfo.busOnline.days", "defaultContent": ""},
                     { "data": "busInfo._adtype", "defaultContent": ""},
                       { "data": "ishaveAd","defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="invalid">有</span>';
                        default :
                            return '<span class="processed">无</span>';
                    }
                } },
                     { "data": "jpaBus.categoryStr", "defaultContent": ""},
                     { "data": "busInfo.offlinecontract.company", "defaultContent": ""},
                 { "data": "jpaBus.description", "defaultContent": ""},
                  { "data": "jpaBus.plateNumber", "defaultContent": ""},
                        { "data": "busInfo._sktype", "defaultContent": ""},
                       
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
              "fnDrawCallback": fnDrawCallback,
        } );

 		 $('#table').dataTable().fnNameOrdering();
      //  table.fnNameOrdering("orderBy").fnNoColumnsParams(); 
      
         }
      
      
       function fnDrawCallback(){
    	 counter_columns(table,0);
    }
    
    function reFreshTotalInfo(){
    		var param={ "filter[oldserinum]" : $('#oldserinum').val(),
                        "filter[serinum]" : $('#serinum').val(),
                        "filter[plateNumber]" : $('#name').val(),
                        "filter[linename]" : $('#linename').val(),
                        "filter[category]" : $('#category').val(),
                        "filter[levelStr]" : $('#levelStr').val(),
                         "filter[contractid]" : $('#cid').val()};
    			 $.ajax({
					    			url:"${rc.contextPath}/bus/ajax-countbus_list",
					    			type:"POST",
					    			async:false,
					    			dataType:"json",
					    			data:param,
					    			success:function(data){
					    			var w='';
					    			$("#modelGroupView").html(w);
					    				 $.each(data,function(index,value){
        								        w+='<span id="trw2">';
        								        w+=value.modelName=='-total-'?"该线路合计总数：":value.modelName;
        								        w+='&nbsp;&nbsp;&nbsp; &nbsp车辆总数：';
        								        w+=value.total;
        								        w+='&nbsp;&nbsp;&nbsp; &nbsp;占用车辆数：';
        								        w+=value.online;
        								        w+='&nbsp;&nbsp;&nbsp; &nbsp; 可用车辆数：';
        								        w+=value.free;
        								        w+='&nbsp;&nbsp;&nbsp; &nbsp; 到期未下刊数：';
        								        w+=value.nowDown;
        								        w+='</span><br>';
										})
										$("#modelGroupView").html(w);
					    			}
					       });  
    
    }

    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>车辆自编号：</span>' +
                        '    <span>' +
                        '        <input id="serinum" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>旧自编号：</span>' +
                        '    <span>' +
                        '        <input id="oldserinum" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>线路：</span>' +
                        '        <input id="linename" value="">' +
                        '    </span>&nbsp;&nbsp;<br><br>' +
                         '    <span>合同编号：</span>' +
                        '    <span>' +
                        '        <input value="" id="contractid">' +
                         '    </span>&nbsp;&nbsp;' +
                        '    <span>线路级别</span>&nbsp;&nbsp;' +
                       '<select class="ui-input ui-input-mini" name="levelStr" id="levelStr" style="width:120px"> ' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	'<option value="S">特级</option>' +
                  	'<option value="APP">A++</option>' +
                  	'<option value="AP">A+</option>' +
                  	'<option value="A">A</option>' +
         			'</select>' +
                        '    <span>车辆类别</span>&nbsp;&nbsp;' +
                       '<select class="ui-input ui-input-mini" name="category" id="category" style="width:120px">' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	'<option value="baoche">包车</option>' +
                  	'<option value="banche">班车</option>' +
                  	'<option value="jidongche">机动车</option>' +
                  	'<option value="yunyingche">运营车</option>' +
                  	'</select>'+
                  	'</div>'+
                    ''
        );

        $('#serinum,#oldserinum,#name,#linename,#category,#levelStr,#contractid').change(function() {
        	reFreshTotalInfo();
            table.draw();
        });
         $("#linename").autocomplete({
		minLength: 0,
			source : "${rc.contextPath}/busselect/autoComplete?tag=a",
			change : function(event, ui) {
			},
			select : function(event, ui) {
				$('#linename').val(ui.item.value);
				table.draw();
				reFreshTotalInfo();
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   	 	});
   	 	
   	 	
   	 	 $('#linename').change(function() {
	        if($('#linename').val()==""){
	            $('#linename').val("");
	            table.draw();
	        }
        });
        
   	 	 $('#contractid').change(function() {
	        if($('#contractid').val()==""){
	            $('#cid').val("");
	            table.draw();
	        }
        });
   	 	 $("#contractid").autocomplete({
		    minLength: 0,
			source : "${rc.contextPath}/busselect/contractAutoComplete?tag=a",
			change : function(event, ui) {
			},
			select : function(event, ui) {
			$('#cid').val(ui.item.dbId);
				table.draw();
				reFreshTotalInfo();
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   	 	});
    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
                reFreshTotalInfo();
            })
        });
    }
			
    $(document).ready(function() {
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title">
		配车查询 <input type="hidden" id="cid" />
	</div>
	<table id="table" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				<th></th>
				<th>线路</th>
				<th>线路级别</th>
				<th>车辆自编号</th>
				<th>旧自编号</th>
				<th>营销中心</th>
				<th>车型号</th>
				<th>合同编号</th>
				<th>广告内容</th>
				<th>实际上刊日期</th>
				<th>预计下刊日期</th>
				<th>实际下刊日期</th>
				<th>刊期</th>
				<th>广告类型</th>
				<th>车身广告状态</th>
				<th>类别</th>
				<th>客户名称</th>
				<th>车辆描述</th>
				<th>车牌号</th>
				<th>上刊类型</th>
			</tr>
		</thead>
	</table>
	<div id="modelGroupView"></div>
</div>

</@frame.html>
