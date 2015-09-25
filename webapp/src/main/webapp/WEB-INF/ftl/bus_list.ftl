<#import "template/template.ftl" as frame>
<#global menu="巴士列表">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="巴士列表" js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
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
                        "filter[plateNumber]" : $('#name').val(),
                        "filter[linename]" : $('#linename').val(),
                        "filter[category]" : $('#category').val(),
                        "filter[levelStr]" : $('#levelStr').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "jpaBus.plateNumber"},
                { "data": "jpaBus.serialNumber"},
                { "data": "jpaBus.oldSerialNumber"},
                { "data": "jpaBus.model.name"},
                { "data": "jpaBus.line.name"},
                { "data": "jpaBus.line.levelStr"},
                { "data": "jpaBus.categoryStr"},
                { "data": "jpaBus.company.name"},
                { "data": "busInfo.contractCode", "defaultContent": ""},
                { "data": "busInfo.startD", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                { "data": "busInfo.endD", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                 { "data": "jpaBus.description", "defaultContent": ""},
                     { "data": "jpaBus.office", "defaultContent": ""},
                      { "data": "jpaBus.branch", "defaultContent": ""},
                { "data": "jpaBus.enabled", "defaultContent": "", "render": function(data) {
                    switch(data) {
                        case true:
                            return '<span class="processed">正常</span>';
                        default :
                            return '<span class="invalid">禁用</span>';
                    }
                } },
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
         			'</select><br>' +
                        '</div>'
        );

        $('#name,#linename,#category,#levelStr').change(function() {
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

//车辆信息修改
function showBusDetail(pathUrl,tourl,id){

	$.ajax({
		url : tourl  + id,
		type : "POST",
		data : {
		},
		success : function(data) {
			layer.open({
				type: 1,
				title: "车辆信息修改",
				skin: 'layui-layer-rim', 
				area: ['450px', '650px'], 
				content: ''
				+ '<form id="publishform01" action='+pathUrl+'/bus/saveBus>'
				+ '<div class="inputs" style="margin-top: 40px;margin-left: -30px;"><input type="hidden" name="id" value="'+data.id+'"/>'
				+'<div class="ui-form-item"> <label class="ui-label mt10">车牌号: </label><input class="ui-input-d"'
				+'type="text" name="plateNumber" id="plateNumber" value="'+data.plateNumber+'" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10">车辆自编号:</label><input  class="ui-input-d"'
				+'type="text" name="serialNumber" value="'+data.serialNumber+'"  data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10">旧自编号:</label><input  class="ui-input-d"'
				+'type="text" name="oldSerialNumber" value="'+data.oldSerialNumber+'" id="oldSerialNumber" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> <p class="ui-term-placeholder"></p> </div>'
				+ '<div class="ui-form-item"><input type="hidden" id ="cc" class="layui-layer-ico layui-layer-close layui-layer-close1"/>'
				+ '<label class="ui-label mt10">选择线路：</label>'
				+ '<input class="ui-input" value="'+data.line.name+'"  id="line_id" data-is="isAmount isEnough">'
				+ '</div>'
				+ '<div id="four"><div class="ui-form-item" id="model_Id">'
				+ '<label class="ui-label mt10">选择车型：</label>'
				+ '<select  class="ui-input bus-model" name="modelId" id="model_id"> <option value="'+data.model.id+'" selected="selected">'+data.model.name+'</option><option value="0">所有类型</option> </select>'
				+ '</div>'
				+'<div class="ui-form-item toggle bodyToggle"> <label class="ui-label mt10">车辆类别:</label>'
				+'<select class="ui-input ui-input-mini" name="category" id="category">' 
              	+'<option value="0">包车</option>' 
              	+'<option value="1">班车</option>' 
              	+'<option value="2">机动车</option>' 
              	+'<option value="3">运营车</option>' 
     			+'</select>'
				+'</div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="toggle bodyToggle">营销中心:</span> </label>'
				+ '<select  class="ui-input bus-model" name="companyId" id="companyId"> <option value="'+data.company.id+'" selected="selected">'+data.company.name+'</option> </select>'
				+'</div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="toggle bodyToggle">公司名称:</span> </label>'
				+'<input class="ui-input-d"  value="'+data.office+'" name="office" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10"> <span class="toggle bodyToggle">客户:</span> </label>'
				+'<input class="ui-input-d"  value="'+data.branch+'" id="branch" name="branch" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> </div>'
				+'<div class="ui-form-item"> <label class="ui-label mt10" style="width: 145px;">车辆情况:</label><textarea rows="4" name="description" cols="30"  style="resize: none;margin-left: -20px;" >'+data.description+'</textarea> </div></div>'
				+ '<div class="ui-form-item widthdrawBtBox" style="margin-left:40px;">'
				+ '<input type="button" onclick="saveBus()" class="block-btn" value="确认" ></div></div>'
				+ '<input type="hidden" value="'+data.line.id+'" name="lineId" id="db_id"></form>'
			});
			var companyN=data.company.name;
					    $.ajax({
		       url : pathUrl + "/bus/findAllCompany",
		       type : "GET",
		       success : function(data) {
		      $.each(data, function(i, item) {
		      if(companyN!=item.name){
				$("#companyId").append(
						$("<option value="+item.id+">" + item.name
								+ "</option>"));
								}
		         });
	    }}, "text");
			
			$("#line_id").autocomplete({
				minLength: 0,
					source : "${rc.contextPath}/busselect/autoComplete",
					change : function(event, ui) {
					},
					select : function(event, ui) {
						$('#line_id').val(ui.item.value);
						initmodel(ui.item.dbId);
						$("#db_id").val(ui.item.dbId);
					}
				}).focus(function () {
		       				 $(this).autocomplete("search");
		   				 });
		}
		
	}, "text");
	
	
}
//编辑保存
	function saveBus() {
		var lineid=$("#db_id").val();
		if(lineid==0){
		   layer.msg("请选择线路");
		   return;
		}
			$('#publishform01').ajaxForm(function(data) {
			if(data.left){
			     layer.msg("编辑成功");
			       table.dataTable()._fnAjaxUpdate();
			       $("#cc").trigger("click");
			     }else{
			     layer.msg(data.right);
			     }
			}).submit();
			}
			
    $(document).ready(function() {
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title">
                巴士列表
									</div>
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
                        <th >车牌号</th>
                        <th >车辆自编号</th>
                        <th>旧自编号</th>
                        <th >车型</th>
                        <th >线路</th>
                        <th >线路级别</th>
                        <th >类别</th>
                        <th >营销中心</th>
                        <th>合同编号</th>
                        <th>上刊日期</th>
                        <th>下刊日期</th>
                        <th>车辆描述</th>
                        <th>公司名称</th>
                        <th>所属分公司</th>
                        <th orderBy="enabled">状态</th>
                        <th>管理</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>