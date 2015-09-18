<#import "template/template.ftl" as frame>
<#global menu="上下刊错误处理">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="车辆上下刊错误处理" js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
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
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-mistake_handle",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[plateNumber]" : $('#name').val(),
                        "filter[category]" : $('#category').val(),
                        "filter[levelStr]" : $('#levelStr').val(),
                        "filter[linename]" : $('#linename').val(),
                        "filter[company]" : $('#company').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "jpaBus.plateNumber", "defaultContent": "","render": function(data, type, row, meta) {
                	return '<a  onclick="showbusOnline_history(\'${rc.contextPath}\','+row.jpaBus.id+');">'+data+'</a>';
                }},
                { "data": "jpaBus.serialNumber", "defaultContent": ""},
                { "data": "jpaBus.oldSerialNumber", "defaultContent": ""},
                { "data": "jpaBus.model.name", "defaultContent": ""},
                { "data": "jpaBus.line.name", "defaultContent": ""},
                { "data": "jpaBus.line.levelStr", "defaultContent": ""},
                { "data": "jpaBus.categoryStr", "defaultContent": ""},
                { "data": "jpaBus.company.name", "defaultContent": ""},
                { "data": "busInfo.contractCode", "defaultContent": ""},
                { "data": "busInfo.startD", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                { "data": "busInfo.endD", "defaultContent": "","render": function(data, type, row, meta) {
              	  var d= $.format.date(data, "yyyy-MM-dd");
              	  if(d!=null){
	               	  var start=new Date(d.replace("-", "/").replace("-", "/")); 
	              		  if(start >= new Date()){
	                		d="<font color='red'>"+d+"</font>";
	                	}else {
	                		d="<font color='green'>"+d+"</font>";
	                	}
                	}
                	return d;
                }},
                 { "data": "jpaBus.description", "defaultContent": ""},
                 { "data": "jpaBus.branch", "defaultContent": ""},
	              { "data": "", "defaultContent": "","render": function(data, type, row, meta) {
	              
	              		var tString ='';
	              		if(row.busInfo.stats == 'now' || row.busInfo.stats == 'future'){
	              			tString = 	 '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/bus/offlineContract/' + (row.busInfo.busOnline.id) + '">撤销</a> &nbsp;'
	              		}
	                	return tString;
	                }
                }
                
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
                        '    <span>车牌号</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>线路级别</span>&nbsp;&nbsp;' +
                       '<select class="ui-input ui-input-mini" name="levelStr" id="levelStr" style="width:125px" >' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	'<option value="S">特级</option>' +
                  	'<option value="APP">A++</option>' +
                  	'<option value="AP">A+</option>' +
                  	'<option value="A">A</option>' +
                  	'<option value="LATLONG">经纬线</option>' +
         			'</select>&nbsp;&nbsp;' +
                        '    <span>车辆类别</span>&nbsp;&nbsp;' +
                       '<select class="ui-input ui-input-mini" name="category" id="category" style="width:125px" >' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	'<option value="baoche">包车</option>' +
                  	'<option value="banche">班车</option>' +
                  	'<option value="jidongche">机动车</option>' +
                  	'<option value="yunyingche">运营车</option>' +
         			'</select>' +
         			'    <span>营销中心</span>&nbsp;&nbsp;' +
                    '<select class="ui-input ui-input-mini" name="company" id="company">' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	    <#list companys as c>
					'<option value="${c.id}">${c.name}</option>'+
					    </#list>
         			'</select>' +
                        '</div>'
        );

        $('#name,#category,#levelStr,#linename,#company').change(function() {
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
    $(document).ready(function() {
        initTable();
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
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title">
                车辆上下刊处理
                              &nbsp; &nbsp; &nbsp; &nbsp; 线路：<input class="ui-input" value="" id="linename" data-is="isAmount isEnough">
	     	</div>
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
                        <th >车牌号</th>
                        <th >新车号</th>
                        <th >旧车号</th>
                        <th >车型</th>
                        <th orderBy="line.name">线路</th>
                        <th orderBy="line.level">线路级别</th>
                        <th orderBy="category">类别</th>
                        <th orderBy="company">营销中心</th>
                        <th>合同编号</th>
                        <th>上刊日期</th>
                        <th>下刊日期</th>
                        <th>车辆描述</th>
                        <th>客户名称</th>
                        <th>撤销上刊</th>
                    </tr>
                    </thead>
                </table>
</div>
</@frame.html>