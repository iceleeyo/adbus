<#import "template/template.ftl" as frame>
<#global menu="上刊发布统计">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="上刊发布统计" js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
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
            "ordering": false,
            "serverSide": true,
            "scrollX": true,
               "aaSorting": [[1, "desc"]],
            "columnDefs": [
                { "orderable": false, "targets": [0,2,5,7,8] },
            ],
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-bus_orders",
                data: function(d) {
                    return $.extend( {}, d, {
                      //  "filter[contractCode]" : $('#contractCode').val(),
                        "filter[contractid]" : $('#cid').val(),
                        "filter[sktype]" : $('#sktype').val(),
                        "filter[becompany]" : $('#becompany').val(),
                        "filter[adcontent]" : $('#adcontent').val(),
                        "filter[box]" : $("#showAll").val()  
                        
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
           		{ "data": "", "defaultContent": ""},//1
             	{ "data": "orders.id", "defaultContent": ""},
             		{ "data": "orders.offlineContract.contractCode", "defaultContent": ""},
             	{ "data": "orders.offlineContract.adcontent", "defaultContent": ""},
             	{ "data": "adtype", "defaultContent": ""},
             	{ "data": "one.startDate", "defaultContent": "","render": function(data, type, row, meta) {
                	return  $.format.date(data, "yyyy-MM-dd");
                }},
             	{ "data": "orders.days", "defaultContent": ""},//刊期
             	{ "data": "orders.remainNuber", "defaultContent": ""},
             	{ "data": "orders.line.name", "defaultContent": ""},
             	{ "data": "sb", "defaultContent": ""},
             	{ "data": "print", "defaultContent": ""},
             	{ "data": "orders.jpaBusinessCompany.name", "defaultContent": ""},
                { "data": "sktype", "defaultContent": ""},
               
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
                		
                        '    <span>合同编号</span>'+
                        '    <span>' +
                        '        <input id="contractCode" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '    <span>广告内容</span>'+
                        '    <span>' +
                        '        <input id="adcontent" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                      '  <span>原营销中心</span>&nbsp;&nbsp;' +
                    '<select class="ui-input ui-input-mini" name="becompany" id="becompany" style="width:135px">' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	    <#list companys as c>
					'<option value="${c.id}">${c.name}</option>'+
					    </#list>
         			'</select>' +
                           '    <span>上刊类型：</span>' +
                        '    <span>' +
                       
					 '<select class="ui-input ui-input-mini" name="sktype" id="sktype" style="width:135px">' +
					  '<option value="defaultAll" selected="selected">所有</option>' +
                    '<option value="normal" >正常上刊</option>' +
                    '<option value="fill" >补刊</option>' +
                    '<option value="contin"> 续刊</option>' +
         			'</select>' +
         			 '    <span>过滤零装车：</span>'+ 
         			'<input type="checkbox" name="box1" id="box1" value="1"  />'+
                       
                  	'</div>'+
                  	
                    '<br>'
        );
 
        $('#contractCode,#sktype,#becompany,#adcontent').change(function() {
            table.draw();
        });
           $("#contractCode").autocomplete({
		minLength: 0,
			source : "${rc.contextPath}/busselect/contractAutoComplete?tag=a",
			change : function(event, ui) {
				if(ui.item==null){
					$('#cid').val("");
				}
			},
			select : function(event, ui) {
			$('#cid').val(ui.item.dbId);
				 table.draw();
		    //$('#cid').val("");
			}
		}).focus(function () {
       				 $(this).autocomplete("search");
   	 	});
        $("#box1").click(function(){  
			 var w = $("#showAll").val();
			 if(w==0){
			   $("#showAll").val(1);
			 }else {
			 	$("#showAll").val(0);
			 }  
			  table.draw(); 
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


			
    $(document).ready(function() {
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title">
									</div>
									<input type="hidden" id ="showAll" value ="0" >	
									<input type="hidden" id="cid" />
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>   
                    	<th ></th>
                     	<th>订单编号</th>
                     	<th>合同编号</th>
                        <th>广告内容</th>
                        <th>形式</th>
                        <th>实际上刊时间</th>
                        <th>刊期</th>
                        <th>数量</th>
                        <th>线路</th>
                        <th>车辆自编号</th>
                        <th>印制</th>
                        <th>营销中心</th>
                        <th>上刊类型</th>
                    </tr>
                    </thead>

                </table>
                <span>订单的 形式,实际上刊时间,印制,上刊类型 以最后一辆上刊时的信息为准</span>
</div>
</@frame.html>