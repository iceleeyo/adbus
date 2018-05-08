<#import "template/template.ftl" as frame> <#global menu="上刊发布">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="上刊发布"
js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js","js/jquery-ui/jquery-ui.auto.complete.js",
"js/datepicker.js","js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

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
	
    var orderBusesTable;
    function initTable () {
        orderBusesTable = $('#table').dataTable( {
            "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": true,
            "serverSide": true,
              "aaSorting": [[12, "desc"]],
              "columnDefs": [
                 { "orderable": false, "targets": [0,1,2,3,4,5,6,7,8,9,10,11,13] },
            ],
            "scrollX": true,
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/busselect/ajax-publishLine_list",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[contractid]" : $('#cid').val(),
                        "filter[linename]" : $('#linename').val(),
                        "filter[model]" : $('#model').val(),
                        "filter[company]" : $('#company').val(),
                        "filter[bkTag]" : $('#bkTag').val()
                        
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "id", "defaultContent": ""},
                { "data": "offlineContract.contractCode", "defaultContent": ""},
                { "data": "line.name", "defaultContent": ""},
                { "data": "offlineContract.adcontent", "defaultContent": ""},
                { "data": "mediaType", "defaultContent": ""},
                { "data": "lineDesc", "defaultContent": ""},
                { "data": "salesNumber", "defaultContent": ""},
                { "data": "remainNuber", "defaultContent": "","render": function(data, type, row, meta) {
                	return '<a  onclick="showbusOnline_list(\'${rc.contextPath}\','+row.id+');">'+data+'</a>';
                }},
                { "data": "id", "defaultContent": "","render": function(data, type, row, meta) {
                return (Math.round(row.remainNuber / row.salesNumber * 100)  + "%");;
                 //return (Math.round(row.remainNuber / row.salesNumber * 10000) / 100.00 + "%");;
                }},
                { "data": "days", "defaultContent": ""},
                
                { "data": "jpaBusinessCompany.name", "defaultContent": ""},
                { "data": "startDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                { "data": "created", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                 { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                    var operations ='<a class="table-link" onclick="editPublishLine(\'${rc.contextPath}\','+data+');" href="javascript:void(0)"><font color="green"><B>修改</B></font></a>';
                    operations +='&nbsp;&nbsp;<a class="table-link" target="_blank" href="${rc.contextPath}/bus/findBusByLineid/'+data+'"><font color="green"><B>车辆上刊</B></font></a>';
                         return operations;
                    }}
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
        } );
        orderBusesTable.fnNameOrdering("orderBy").fnNoColumnsParams();
    }

    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>合同编号</span>' +
                        '    <span>' +
                        '        <input id="contractCode" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                         '    <span>线路</span>' +
                        '    <span>' +
                        '        <input id="linename" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                         '    <span>车型</span>' +
                        '    <span>' +
                        '        <input id="model" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                         '    <span>营销中心</span>' +
                       '<select class="ui-input ui-input-mini"  id="company">' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	                           '<option value="自营">自营</option>'+
                                            	'<option value="CBS">CBS</option>'+
                                            	'<option value="白马">白马</option>'+
                                            	'<option value="七彩">七彩</option>'+
                                            	'<option value="市场">市场</option>'+
                                            	'<option value="其他">其他</option>'+
         			'</select><br>' +
         			
         			  '&nbsp;&nbsp <select class="ui-input ui-input-mini"  id="bkTag">' +
                    '<option value="" selected="selected">所有</option>' +
                  	                           '<option value="0">正常合同订单</option>'+
                                            	'<option value="1">补刊订单[来自锁定线路菜单]</option>'+
         			'</select><br>' +
                        '</div>'
        );

        $('#contractCode,#linename,#model,#company,#bkTag').change(function() {
            orderBusesTable.fnDraw();
        });
        
           $("#linename").autocomplete({
		minLength: 0,
			source : "${rc.contextPath}/busselect/autoComplete?tag=a",
			change : function(event, ui) {
			},
			select : function(event, ui) {
				$('#linename').val(ui.item.value);
				orderBusesTable.fnDraw();
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   	 	});

		//---
		
		 $("#contractCode").autocomplete({
		minLength: 0,
			source : "${rc.contextPath}/busselect/contractAutoComplete?tag=a",
			change : function(event, ui) {
			},
			select : function(event, ui) {
			$('#cid').val(ui.item.dbId);
				orderBusesTable.fnDraw();
				$('#cid').val("");
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   	 	});
   	 	
   	 	//-----   	 	
    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                orderBusesTable.fnDraw(true);
            })
        });
    }

    $(document).ready(function() {
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title">上刊发布</div>
	<input type="hidden" id="cid" />
	<table id="table" class="display nowrap" cellspacing="0">
		<thead>
			<tr>
				<th>订单编号</th>
				<th>合同编号</th>
				<th>线路</th>
				<th>广告内容</th>
				<th>媒体类型</th>
				<th>发布形式</th>
				<th>订购数</th>
				<th>已上刊数</th>
				<th>在刊率</th>
				<th>刊期</th>
				<th>营销中心</th>
				<th>预计上刊日期</th>
				<th>订单记录时间</th>
				<th>操作</th>
			</tr>
		</thead>
	</table>
</div>
</@frame.html>
