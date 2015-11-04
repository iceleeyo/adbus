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
    
    td.highlight {
        font-weight: bold;
        color: blue;
    }
    
    
</style>

<script type="text/javascript">
	
    var table;
    function initTable () {
        table = $('#table').DataTable( {
            //"dom": '<"#toolbar">lrtip',
             "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": true,
            "serverSide": true,
            "scrollX": true,
               "aaSorting": [[1, "desc"]],
            "columnDefs": [
                { "orderable": false, "targets": [0,2,5,7,8,9,10,11,12,13,14,15,16,17,18,19,20] },
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
                         "filter[becompany]" : $('#becompany').val(),
                          "filter[afcompany]" : $('#afcompany').val(),
                             "filter[box]" : $("#showAll").val() 
                          
                        
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
            { "data": "", "defaultContent": ""},
            { "data": "log.updated", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd hh:mm");
                	return d;
                }},
                  { "data": "log.nowline.branch", "defaultContent": ""},
               
                { "data": "log.oldlineName", "defaultContent": ""},
                { "data": "log.nowLineName", "defaultContent": ""},
                   { "data": "log.serialNumber", "defaultContent": ""},
                   
                 { "data": "log.jpabus.serialNumber", "defaultContent": ""},
                  { "data": "log.jpabus.plateNumber", "defaultContent": ""},
                    { "data": "log.oldCompanyId.name", "defaultContent": ""},
                        { "data": "log.nowCompanyId.name", "defaultContent": ""},
                       { "data": "log.jpabus.categoryStr", "defaultContent": ""},
                       { "data": "ishaveAd", "defaultContent": "", "render": function(data, type, row, meta) {
                    switch(data) {
                        case true:
                            return '<span class="invalid">有</span>';
                        default :
                            return '<span class="processed">否</span>';
                    }
                } },
                
                   { "data": "busInfo.contractCode", "defaultContent": ""},
                { "data": "busInfo.startD", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                { "data": "busInfo.endD", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                  { "data": "busInfo.offlinecontract.adcontent", "defaultContent": ""},
                
                       
                { "data": "oldBusLevel", "defaultContent": ""},
                { "data": "log.oldline.branch", "defaultContent": ""},
                     { "data": "log.oldline.office", "defaultContent": ""},
                      { "data": "log.nowline.office", "defaultContent": ""},
               { "data": "log.updator", "defaultContent": ""}, 
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
            "fnDrawCallback": fnDrawCallback,
            "createdRow": function ( row, data, index ) {
	            if (data.ishaveAd) {
	            // $('td', row).eq(5).addClass('highlight');
	              	$('td', row).css('background-color', 'yellow');
	            }
        }
        } );
         $('#table').dataTable().fnNameOrdering();
       // table.fnNameOrdering("orderBy").fnNoColumnsParams();
    }
	function fnDrawCallback(){
    	 counter_columns(table,0);
    }
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                		
                        '    <span>车辆自编号</span>' +'[历史<input type="checkbox" style=" height: 15px;  width: 15px;" name="box1" id="box1" value="1"  />]：'+
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
                      ' <br>  <br>   <span>原营销中心</span>&nbsp;&nbsp;' +
                    '<select class="ui-input ui-input-mini" name="becompany" id="becompany" style="width:135px">' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	    <#list companys as c>
					'<option value="${c.id}">${c.name}</option>'+
					    </#list>
         			'</select>' +
                           '    <span>修改后营销中心</span>&nbsp;&nbsp;' +
                    '<select class="ui-input ui-input-mini" name="afcompany" id="afcompany" style="width:135px">' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	    <#list companys as c>
					'<option value="${c.id}">${c.name}</option>'+
					    </#list>
         			'</select>' +
                  	'</div>'
        );
	$("#box1").click(function(){  
			 var w = $("#showAll").val();
			 if(w==0){
			   $("#showAll").val(1);
			 }else {
			 	$("#showAll").val(0);
			 }  
			  table.draw(); 
	    }); 
        $('#serinum,#newLinename,#oldLinename,#becompany,#afcompany').change(function() {
        
        	if($('#newLinename').val() ==''){
        		  $('#newLineId').val("");
        	}
        	if($('#oldLinename').val() ==''){
        		  $('#oldLineId').val("");
        	}
            table.draw();
        });
          
        
            
   	 	
   	 	  $("#newLinename").autocomplete({
		minLength: 0,
			source : "${rc.contextPath}/busselect/autoComplete?tag=a",
			change : function(event, ui) {
			},
			select : function(event, ui) {
				$('#linename').val(ui.item.value);
			   //$('#newLineId').val(ui.item.dbId);
			   $('#newLineId').val(ui.item.value);
				table.draw();
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
				//$('#oldLineId').val(ui.item.dbId);
				$('#oldLineId').val(ui.item.value);
				table.draw();
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   	 	});
         
     }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.draw();
            })
        });
        $("#table_length").hide(); 
         $(".dataTables_length").hide();
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
            <div class="withdraw-title">调车统计
									</div>
									<input type="hidden" id ="oldLineId" value ="" >			
									<input type="hidden" id ="newLineId" value ="" >		
									<input type="hidden" id ="showAll" value ="0" >	
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>   
                     <th ></th>
                     	 <th orderBy="updated">车辆变更日期</th>
                     	 <th>变更后分公司</th>
                      
                        <th orderBy="oldline.name">原线路</th>
                        <th orderBy="nowline.name">变更后线路</th>
                            <th>调整时自编号</th>
                         <th orderBy="jpabus.serialNumber">当前新自编号</th>
                           <th >车牌号</th>
                             <th >原营销中心</th>
                                <th>调整后营销中心</th>
                                <th >车辆状态</th>
                                <th>车身广告状态</th>
                                  <th>合同编号</th>
                        <th>实际上刊日期</th>
                        <th>预计下刊日期</th>
                        <th>广告内容</th>
                        <th>原线路级别</th>
                        <th >原分公司</th>
                        <th>原总公司</th>
                        <th>变更后总公司</th>
                            <th>记录人</th>
                    </tr>
                    </thead>

                </table>
                
                <div class="worm-tips">
		<div class="tips-title">
			<span class="icon"></span> 温馨提示
		</div>
		<ol>
			<li>1.记录为黄色表示改调整车辆有相关联的在刊广告需要处理。</li>
			
		</ol>
	</div>
</div>
</@frame.html>