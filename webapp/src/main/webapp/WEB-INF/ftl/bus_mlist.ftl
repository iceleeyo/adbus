<#import "template/template.ftl" as frame> <#global menu="车辆管理">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="车辆管理"
js=["js/bus_mlist.js","js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["css/sift.css","css/account.css","js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

<link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/basecss.css">

<script type="text/javascript">
	
    var table;
    function initTable () {
        table = $('#table').DataTable( {
             "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "scrollX": true,
            "aaSorting": [[2, "asc"]],
            "columnDefs": [
                { "orderable": false, "targets": [0,1,3,5,8,9,10,11,12,13] },
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
                          "filter[sh]" : $('#sh').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
             { "data": function( row, type, set, meta) {
                                                  return row.jpaBus.id;
                                              },
										"render" : function(data, type, row,
												meta) {
											var operations = '<input type="checkbox" name="checkone" value="'+data+'" />';
											return operations;
										}
									},
									  { "data": "", "defaultContent": ""},
                { "data": "jpaBus.serialNumber", "defaultContent": ""},
                { "data": "jpaBus.oldSerialNumber", "defaultContent": ""},
                { "data": "jpaBus.plateNumber", "defaultContent": "","render" : function(data, type, row,
												meta) {
											return data.replace("/","");
										}},
                { "data": "jpaBus.model.name", "defaultContent": ""},
                { "data": "jpaBus.line.name", "defaultContent": ""},
                { "data": "jpaBus.line.levelStr", "defaultContent": ""},
                { "data": "jpaBus.categoryStr", "defaultContent": "", "render": function(data) {
                  if(data=="机动车"){
                     return '<span class="invalid">'+data+'</span>';
                  }else{
                     return data;
                  }
                } },
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
                 { "data": function( row, type, set, meta) {
                    return row.jpaBus.id;
                    },
                    "render": function(data, type, row, meta) {
                         var operations ='';
                         if(row.jpaBus.enabled==true){
                         operations += '<a  onclick="showBusDetail(\'${rc.contextPath}\',\'${rc.contextPath}/bus/ajaxdetail/\','+data+');"  ><font color="green"><B>编辑</B></font></a>';
                         operations += '&nbsp;&nbsp;<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/busselect/ajax-remove-bus/0?id=' + data +'"><font color="green"><B>删除</B></font></a>'
                        operations+='&nbsp;&nbsp;<a  onclick="showbusUpdate_history(\'${rc.contextPath}\','+row.jpaBus.serialNumber+');"><font color="green"><B>查看变更历史</B></font></a>'
                       	}else {
                       		 operations += '&nbsp;&nbsp;<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/busselect/ajax-remove-bus/1?id=' + data +'"><font color="green"><B>恢复</B></font></a>';
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
        $('#table').dataTable().fnNameOrdering();
      //  table.fnNameOrdering("orderBy").fnNoColumnsParams();
    }
    
    function fnDrawCallback(){
    	 counter_columns(table,1);
    	  $('.table-action').click(function() {
			$.post($(this).attr("url"), function(data) {
			if(data.left){
			    layer.msg(data.right);
				table.draw();
				 }else{
				  layer.msg(data.right);
				 }
			});
		});
    }
    
      function drawCallback() {
    }

    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>车辆自编号</span>' +
                        '    <span>' +
                        '        <input id="serinum" value="" placeholder="可以输入多个，用逗号分隔。" style="width:300px;">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>旧自编号</span>' +
                        '    <span>' +
                        '        <input id="oldserinum" value="" placeholder="可以输入多个，用逗号分隔。" style="width:300px;">' +
                        '    </span>&nbsp;&nbsp;<br><br>' +
                        '    <span>车牌号</span>' +
                        '    <span>' +
                        '        <input id="name" value="" placeholder="可以输入多个，用逗号分隔。" style="width:300px;">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>线路</span>' +
                        '    <span>' +
                        '        <input id="linename" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                  	'<span>&nbsp;&nbsp;<a class="block-btn" id="export_xls" href="javascript:void(0);">导出查询数据</a>'+
                  	   '&nbsp;&nbsp;&nbsp;&nbsp; <br><br> <span>调整到线路</span>  <span>' +
                        '        <input id="newLine" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '&nbsp;&nbsp;<a class="block-btn" id="change_line" href="javascript:void(0);">确定调车</a>'+
                  	'</div>'
        );

        $('#serinum,#oldserinum,#name').change(function() {
            table.draw();
        });
          $('#linename').change(function() {
            ishaveline($("#linename").val());
           table.draw();
        });
        
            $("#newLine").autocomplete({
			minLength: 0,
				source : "${rc.contextPath}/busselect/autoComplete?tag=a",
				change : function(event, ui) {
				},
				select : function(event, ui) {
					$('#newLine').val(ui.item.value);
					$('#newLineId').val(ui.item.dbId);
				}
			}).focus(function () {
	       	  $(this).autocomplete("search");
	   	 	});
   	 	//---------
         $("#linename").autocomplete({
		minLength: 0,
			source : "${rc.contextPath}/busselect/autoComplete?tag=a",
			change : function(event, ui) {
			},
			select : function(event, ui) {
				$('#linename').val(ui.item.value);
				table.draw();
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   	 	});
        $("#export_xls").click(function(){
          var params =  "filter[plateNumber]=" + $('#name').val()
                        +"&filter[linename]=" + $('#linename').val();
                     //   +"&filter[category]=" + $('#category').val()
                      //  +"&filter[levelStr]=" + $('#levelStr').val();
                     //   alert(params);
                        var w = ''+ $('#name').val()+$('#linename').val() ;
                        if($('#name').val() =='' && $('#linename').val() ==''){
                         	jDialog.Alert("导出车辆信息,由于数据量较大执行较慢 正在优化开发随后几天将开放!请先缩小查询范围再导出");
                         	return ;
                        }
                        
            location.href='${rc.contextPath}/bus/ajax-list.xls?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1&'+params;
        });
        
         $("#change_line").click(function(){
           
			  	 	var o = document.getElementsByName("checkone");
			        	var dIds='';
			        	for(var i=0;i<o.length;i++){
			                if(o[i].checked)
			                dIds+=o[i].value+',';   
			           }
			           if(dIds==""){
			        	   layer.msg("请选择需要调车的车辆");
			        	   return false;
			           }
			           var newLineId = $('#newLineId').val();
			           if(newLineId<=0){
			               layer.msg("请选择要调到的线路");
			        	   return false;
			           }
			           
			   		var param={"ids":dIds,"newLineId":newLineId};
					layer.confirm('确定调车吗？', {icon: 3}, function(index){
			    		layer.close(index);
					    	 $.ajax({
					    			url:"${rc.contextPath}/bus/changeLine",
					    			type:"POST",
					    			async:false,
					    			dataType:"json",
					    			data:param,
					    			success:function(data){
					    				if (data.left == true) {
					    					layer.msg("调车操作成功");
					    					 table.draw();
					    				} else {
					    					layer.msg(data.right,{icon: 5});
					    				}
					    			}
					       });  
					});	
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
    $("#sh").val("stats_1");
        initTable();
         initSwift(table);
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title">
		<input type="hidden" id="sh" value="" /> 车辆管理 <a class="block-btn"
			onclick="addBus('${rc.contextPath}');" href="javascript:void(0);">添加车辆</a>

		<span> &nbsp;&nbsp &nbsp;&nbsp <a style="margin-right: 25px"
			class="block-btn" onclick="addBusBatch('${rc.contextPath}');"
			href="javascript:void(0);">批量修改车辆</a><span>
	</div>
	<div class="container-12 s-clear" style="width: 100%;">
		<div class="sift-box">
			<div class="sift-item s-clear">
				<span>线路级别：</span>
				<div class="sift-list" qt="lev">
					<a class="item active" href="#" sort="-1" qc="all">所有</a>
					<a class="item" href="#" qc="S">特级<i>×</i></a> 
					<a class="item" href="#" qc="APP">A++<i>×</i></a> 
					<a class="item" href="#" qc="AP">A+<i>×</i></a> 
					<a class="item" href="#" qc="A">A<i>×</i></a>
					<a class="item" href="#" qc="LATLONG">其他<i>×</i></a>
				</div>
			</div>
			<div class="sift-item s-clear">
				<span>营销中心：</span>
				<div class="sift-list" qt="com">
					<a class="item active" href="#" sort="-1" qc="all">所有</a> 
					<a class="item" href="#" qc="10">自营<i>×</i></a> 
					<a class="item" href="#" qc="13">CBS<i>×</i></a> 
					<a class="item" href="#" qc="12">白马<i>×</i></a>
					<a class="item" href="#" qc="8">七彩<i>×</i></a>
					<a class="item" href="#" qc="11">其他<i>×</i></a>
				</div>
			</div>
			<div class="sift-item s-clear">
				<span>车辆类型：</span>
				<div class="sift-list" qt="gor">
					<a class="item active" href="#" sort="-1" qc="all">所有</a>
					<a class="item" href="#" qc="baoche">包车<i>×</i></a> 
					<a class="item" href="#" qc="banche">班车<i>×</i></a> 
					<a class="item" href="#" qc="jidongche">机动车<i>×</i></a> 
					<a class="item" href="#" qc="yunyingche">运营车<i>×</i></a>
				</div>
			</div>
			<div class="sift-item s-clear">
				<span>公司名称：</span>
				<div class="sift-list" qt="company">
					<a class="item active" href="#" sort="-1" qc="all">所有</a> <a
						class="item" href="#" qc="1">大公共公司<i>×</i></a> <a class="item"
						href="#" qc="2"> 八方达公司<i>×</i></a>
				</div>
			</div>
			<div class="sift-item s-clear">
				<span>车辆状态：</span>
				<div class="sift-list" qt="stats">
					<a class="item " href="#" sort="-1" qc="all">所有</a> <a
						class="item active" href="#" qc="1">正常车辆<i>×</i></a> <a
						class="item" href="#" qc="2"> 回收站车辆<i>×</i></a>
				</div>

			</div>
		</div>
	</div>

	<input type="hidden" id="newLineId" value="0">
	<table id="table" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				<th><input type="checkbox" name="checkAll" /></th>
				<th>序号</th>
				<th orderBy="serialNumber">车辆自编号</th>
				<th>旧自编号</th>
				<th orderBy="plateNumber">车牌号</th>
				<th>车型</th>
				<th orderBy="line.id">线路</th>
				<th orderBy="line.level">级别</th>
				<th>类别</th>
				<th>营销中心</th>
				<th>车型描述</th>
				<th>是否有广告</th>
				<th>公司名称</th>
				<th>管理</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
