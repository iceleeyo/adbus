<#import "template/template.ftl" as frame>
<#global menu="调车统计">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="调车统计" js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
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
               "aaSorting": [[1, "asc"]],
            "columnDefs": [
                { "orderable": false, "targets": [1,4,5,6,7,8,9,10,11,12] },
            ],
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-adJustLog",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[serinum]" : $('#serinum').val(),
                        "filter[oldLineId]" : $('#oldLineId').val(),
                        "filter[newLineId]" : $('#newLineId').val(),
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
           
                { "data": "log.jpabus.serialNumber", "defaultContent": ""},
                { "data": "log.oldline.name", "defaultContent": ""},
                { "data": "log.nowline.name", "defaultContent": ""},
                { "data": "oldBusLevel", "defaultContent": ""},
                { "data": "busLevel", "defaultContent": ""},
                
                { "data": "log.jpabus.oldSerialNumber", "defaultContent": ""},
                { "data": "log.jpabus.plateNumber", "defaultContent": ""},
                { "data": "log.jpabus.model.name", "defaultContent": ""},
                { "data": "log.jpabus.categoryStr", "defaultContent": ""},
                { "data": "log.jpabus.company.name", "defaultContent": ""},
                   { "data": "log.jpabus.description", "defaultContent": ""},
                     { "data": "log.jpabus.office", "defaultContent": ""},
                      { "data": "log.jpabus.branch", "defaultContent": ""},
                
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
                        
                        '    <span>原线路：</span>' +
                        '    <span>' +
                        '        <input id="oldLinename" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                           '    <span>现线路：</span>' +
                        '    <span>' +
                        '        <input id="newLinename" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        
                  	'</div>'+
                  	
                    '<br>'
        );

        $('#serinum,#newLinename,#oldLinename').change(function() {
        
        	if($('#newLinename').val() ==''){
        		  $('#newLineId').val(0);
        	}
        	if($('#oldLinename').val() ==''){
        		  $('#oldLineId').val(0);
        	}
            table.fnDraw();
        });
          
        
            
   	 	
   	 	  $("#newLinename").autocomplete({
		minLength: 0,
			source : "${rc.contextPath}/busselect/autoComplete?tag=a",
			change : function(event, ui) {
			},
			select : function(event, ui) {
				$('#linename').val(ui.item.value);
			   $('#newLineId').val(ui.item.dbId);
				table.fnDraw();
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   	 	});
         $("#oldLinename").autocomplete({
		minLength: 0,
			source : "${rc.contextPath}/busselect/autoComplete?tag=a",
			change : function(event, ui) {
			},
			select : function(event, ui) {
				$('#linename').val(ui.item.value);
				$('#oldLineId').val(ui.item.dbId);
				table.fnDraw();
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
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
									</div>
									<input type="hidden" id ="oldLineId" value ="0" >			
									<input type="hidden" id ="newLineId" value ="0" >			
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>   
                        <th orderBy="jpabus.serialNumber">车辆自编号</th>
                        <th orderBy="oldline.name">原线路</th>
                        <th orderBy="nowline.name">现线路</th>
                        
                        <th>原线路级别</th>
                        <th >现原线路级别</th>
                        
                           <th>旧自编号</th>
                        <th >车牌号</th>
                        <th >车型</th>
                        <th >类别</th>
                        <th >营销中心</th>
                        
                         <th>车辆描述</th>
                        <th>公司名称</th>
                        <th>所属分公司</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>