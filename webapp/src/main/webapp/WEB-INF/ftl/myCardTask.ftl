<#import "template/template.ftl" as frame>
<#global menu="网上订单">
<@frame.html title="网上订单" css=["js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css","css/layer.css"] js=["js/layer.min.js","js/jquery-ui/jquery-ui.auto.complete.js","js/jquery-dateFormat.js","js/layer-v1.9.3/layer-site.js"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<script type="text/javascript">

	function closeOrder22(mainPath,orderid,taskid){
		$.ajax({
			url : mainPath+"/order/closeOrder/"+taskid+"?orderid="+orderid,
			type : "POST",
			success : function(data) {
				//jDialog.Alert(data.right);
				 jDialog.Alert(data.right,function(){
		        var uptime = window.setTimeout(function(){
				$(location).attr('href', mainPath+"/order/myTask/1");
		         clearTimeout(uptime);
		       },2000);});
				//location.reload([true]);
			}
		}, "text");
	}

	function claim(orderid,taskid){
 	$.ajax({
			url : "${rc.contextPath}/order/claim?orderid="+orderid+"&taskid="+taskid,
			type : "POST",
			success : function(data) {
				//jDialog.Alert(data.right);
				 jDialog.Alert(data.right,function(){
		        var uptime = window.setTimeout(function(){
				$(location).attr('href', "${rc.contextPath}/busselect/body_handleView?orderid=" +(orderid)+ "&taskid="+taskid);
		         clearTimeout(uptime);
		       },1000);});
				//location.reload([true]);
			}
		}, "text");
	  
	}
	
	
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "scrollX": true,
              <@security.authorize ifAnyGranted="bodyContractManager,contract_list,ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
            	"aaSorting": [[6, "desc"]],
              </@security.authorize>
              
               <@security.authorize ifAnyGranted="advertiser">
            	"aaSorting": [[5, "desc"]],
              </@security.authorize>
              
              
            "columnDefs": [
                { "sClass": "align-left", "targets": [0] },
                 { "orderable": false, "targets": [0,1,2,3,4,5] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/carbox/ajax-myCards",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[orderid]" : $('#orderid').val(),
                         <@security.authorize ifAnyGranted="advertiser">
                        "filter[media_type]" : $('#media_type').val()
                             </@security.authorize>
                        
                    } );
                },
                "dataSrc": "content",
            },
            
            "columns": [
              <@security.authorize ifAnyGranted="bodyContractManager,contract_list,ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
                       	{ "data": "r.userid", "defaultContent": ""},
                  </@security.authorize>
            
            	{ "data": "r.seriaNum", "defaultContent": "","render": function(data, type, row, meta) {
                	var option= "W"+data;
                	if(row.r.mediaType=='body'){
                	   return '<a  onclick="queryCarBoxBody(\'${rc.contextPath}\','+row.r.id+');">'+option+'</a>';
                	}else{
                	   return '<a  onclick="queryCarBoxMedia(\'${rc.contextPath}\','+row.r.id+');">'+option+'</a>';
                	}
                }},
                { "data": "media_type", "defaultContent": "","render": function(data, type, row, meta) {
                   if(data==0){
                   	 return '移动视频';
                   }else if(data ==1){
                  	 return '车身广告';
                   }else {
                		return "";
                	}
                }},
            	{ "data": "r.totalMoney", "defaultContent": ""},
            	{ "data": "r.fengqi", "defaultContent": ""},
            	{ "data": "r.productCount", "defaultContent": ""},
            	{ "data": "r.payType", "defaultContent": "","render": function(data, type, row, meta) {
                	var t='';
                	if(data =='online'){
                		t='网上支付';
                	}else if(data =='check'){
                		t='支票';
                	}else if(data =='cash'){
                		t='现金';
                	}else if(data =='remit'){
                		t='汇款';
                	}
                	return t;
                }},
                { "data": "r.created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm");
                	return d;
                }}
                
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
            "fnDrawCallback": fnDrawCallback,
        } );
  		table.fnNameOrdering("orderBy").fnNoColumnsParams();
  		
  		 
    }
    <@security.authorize ifAnyGranted="advertiser,ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
     function initComplete() {
     
        $("div#toolbar").html(
                '<div>' +
                          '    <span>订单号</span>' +
                        '    <span>' +
                        '        <input id="orderid" value="">' +
                        '    </span>' +
                          '<select class="ui-input ui-input-mini" name="media_type" id="media_type">' +
	                    '<option value="defaultAll" selected="selected">所有媒体</option>' +
	                    '<option value="screen">移动视频</option>' +
	                    '<option value="body">车身广告</option>' +
	         			'</select>' +
                        '</div>'
        );
        
        $('#orderid,#media_type').change(function() {
            table.fnDraw();
        });
        
     }
     </@security.authorize>
    
    
	<@security.authorize ifAnyGranted="bodyFinancialManager,bodyContractManager,bodyScheduleManager">
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                          '    <span>公司名称</span>' +
                        '    <span>' +
                        '        <input id="companyname" value="">' +
                        '    </span>' +
                        '</div>'
        );

        $('#companyname').change(function() {
            table.fnDraw();
        });
        //author:impanxh 2015-05-20 22:36 自动补全功能
        $( "#autocomplete" ).autocomplete({
  			source: "${rc.contextPath}/user/autoComplete",
  			change: function( event, ui ) { 
  				/*if(ui.item!=null){alert(ui.item.value);}*/
  				table.fnDraw();
  			 },
  			 select: function(event,ui) {
  			 $('#autocomplete').val(ui.item.value);
  				table.fnDraw();
  			 }
		});
		bindLayerMouseOver();
    }
    </@security.authorize>
    
    <@security.authorize ifAnyGranted="bodysales">
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '    <span>流水号</span>' +
                        '    <span>' +
                        '        <input id="seriaNum" value="">' +
                        '    </span>'+
                    '</div>'
        );

        $('#seriaNum').change(function() {
            table.fnDraw();
        });
        bindLayerMouseOver();
    }
    </@security.authorize>

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }
     //显示总条数 add by impanxh
    function fnDrawCallback(){
		var record_count = (this.fnSettings().fnRecordsTotal() );
		if(record_count>0){
	 	  $("#recordsTotal").html("&nbsp;"+record_count+"&nbsp;");
		  }
    }
 		
    $(document).ready(function() {
        initTable();
       // setInterval("bindLayerMouseOver()",1500);
    } );
</script>


<div class="withdraw-wrap color-white-bg fn-clear">
              <div class="withdraw-title" style="padding-top: 0px; text-align:left; ">
									网上订单 <span id="recordsTotal"  style="background-color:#ff9966;color: #fff;font-size: 14px;border-radius: 4px;"></span>
									</div>
                <table id="table" class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <@security.authorize ifAnyGranted="bodyContractManager,contract_list,ShibaSuppliesManager,ShibaOrderManager,ShibaFinancialManager,BeiguangScheduleManager,BeiguangMaterialManager">
                        <th>下单用户</th>
                          </@security.authorize>
                        <th >订单号</th>
                        <th>媒体类型</th>
                        <th>订单总价</th>
                        <th  >分期数</th>
                        <th  >相关产品个数</th>
                          <th  >支付方式</th>
                          <th orderBy="created" >下单时间</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>