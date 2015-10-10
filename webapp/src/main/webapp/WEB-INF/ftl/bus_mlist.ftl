<#import "template/template.ftl" as frame>
<#global menu="车辆管理">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="车辆管理" js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

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
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
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
                        "filter[levelStr]" : $('#levelStr').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "jpaBus.serialNumber", "defaultContent": ""},
                { "data": "jpaBus.oldSerialNumber", "defaultContent": ""},
                { "data": "jpaBus.plateNumber", "defaultContent": ""},
                { "data": "jpaBus.model.name", "defaultContent": ""},
                { "data": "jpaBus.line.name", "defaultContent": ""},
                { "data": "jpaBus.line.levelStr", "defaultContent": ""},
                { "data": "jpaBus.categoryStr", "defaultContent": ""},
                { "data": "jpaBus.company.name", "defaultContent": ""},
                 { "data": "jpaBus.description", "defaultContent": ""},
                  { "data": "ishaveAd","defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="invalid">有</span>';
                        default :
                            return '<span class="processed">否</span>';
                    }
                } },
                     { "data": "jpaBus.office", "defaultContent": ""},
                      { "data": "jpaBus.branch", "defaultContent": ""},
                 { "data": function( row, type, set, meta) {
                    return row.jpaBus.id;
                    },
                    "render": function(data, type, row, meta) {
                        var operations = '<a  onclick="showBusDetail(\'${rc.contextPath}\',\'${rc.contextPath}/bus/ajaxdetail/\','+data+');"  >编辑</a>';
                        operations+='&nbsp;&nbsp;<a  onclick="showbusUpdate_history(\'${rc.contextPath}\','+row.jpaBus.id+');">查看变更历史</a>'
                        return operations;
                    }
                },
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
                '<div>' +
                        '    <span>车辆自编号：</span>' +
                        '    <span>' +
                        '        <input id="serinum" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>旧自编号：</span>' +
                        '    <span>' +
                        '        <input id="oldserinum" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>车牌号：</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>线路：</span>' +
                        '    <span>' +
                        '        <input id="linename" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>线路级别</span>&nbsp;&nbsp;' +
                       '<select class="ui-input ui-input-mini" name="levelStr" id="levelStr">' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	'<option value="S">特级</option>' +
                  	'<option value="APP">A++</option>' +
                  	'<option value="AP">A+</option>' +
                  	'<option value="A">A</option>' +
                  	'<option value="LATLONG">经纬线</option>' +
         			'</select><br><br>' +
                        '    <span>车辆类别</span>&nbsp;&nbsp;' +
                       '<select class="ui-input ui-input-mini" name="category" id="category">' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	'<option value="baoche">包车</option>' +
                  	'<option value="banche">班车</option>' +
                  	'<option value="jidongche">机动车</option>' +
                  	'<option value="yunyingche">运营车</option>' +
                  	'</select>'+
                  	'<span style="float:right;">&nbsp;&nbsp;<a class="block-btn" id="export_xls" href="javascript:void(0);">导出查询数据</a>'+
                  	'</div>'+
                  	
                    '<br>'
        );

        $('#serinum,#oldserinum,#name,#linename,#category,#levelStr').change(function() {
            table.fnDraw();
        });
        
        $("#export_xls").click(function(){
          var params =  "filter[plateNumber]=" + $('#name').val()
                        +"&filter[linename]=" + $('#linename').val()
                        +"&filter[category]=" + $('#category').val()
                        +"&filter[levelStr]=" + $('#levelStr').val();
                     //   alert(params);
                        var w = ''+ $('#name').val()+$('#linename').val() ;
                        if($('#name').val() =='' && $('#linename').val() ==''){
                         	jDialog.Alert("导出车辆信息,由于数据量较大执行较慢 正在优化开发随后几天将开放!请先缩小查询范围再导出");
                         	return ;
                        }
                        
            location.href='${rc.contextPath}/bus/ajax-list.xls?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1&'+params;
        });
    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }
    function initmodel(id) {
	$.ajax({
		url : "${rc.contextPath}/busselect/selectBusType",
		type : "POST",
		data : {
			"buslinId" : id
		},
		success : function(data) {
			$("#four").show();
			var v=' <option value="0" selected="selected">所有类型</option> ';
			$("#model_id").html(v);
			$.each(data, function(i, item) {
			var w="<option value="+item.gn1+">"
								+ item.gp2
								+ (item.gn2 == 0 ? "&nbsp;&nbsp; 单层"
										: "&nbsp;&nbsp; 双层")
								+ "&nbsp;&nbsp;[" + item.count + "]"
								+ "</option>";
				$("#model_id").append(w);
							
			});
		}
	}, "text");
}


			
    $(document).ready(function() {
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title">
                车辆管理  <a class="block-btn" onclick="addBus('${rc.contextPath}');" href="javascript:void(0);">添加车辆</a>
									</div>					
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
                        <th >车辆自编号</th>
                        <th>旧自编号</th>
                        <th >车牌号</th>
                        <th >车型</th>
                        <th >线路</th>
                        <th >线路级别</th>
                        <th >类别</th>
                        <th >营销中心</th>
                        <th>车型描述</th>
                        <th>是否有广告</th>
                        <th>公司名称</th>
                        <th>所属分公司</th>
                        <th>管理</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>