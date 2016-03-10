<#import "template/template.ftl" as frame> <#global menu="网上订单">
<@frame.html title="车身网上订单"
css=["js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css","css/layer.css"]
js=["js/layer.min.js","js/jquery-ui/jquery-ui.auto.complete.js","js/jquery-dateFormat.js","js/layer-v1.9.3/layer-site.js"]>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<script type="text/javascript">
 	function gotoContract(_seriaNum,helpid){
 	var Purl="${bodyOnlineUrl}/busselect/ajax-onlineContractInfo?_seriaNum="+_seriaNum+"&"+"${Md5!''}";
        
         $.ajax({
            url:Purl,
            type:'get',
            success:function(data,status,jqXHR){
            if(jqXHR.status==200){
                if(data!=null && typeof(data)!="undefined"){
               var str = JSON.stringify(data);  
               window.location = "${rc.contextPath}/bodyClient/be_createOnlineContract?_seriaNum="+_seriaNum+"&helpid="+helpid+"&jsonStr="+str;
               }else{
                 layer.msg("操作异常");
               }
               }
               else{
                alertCompleteMsg(jqXHR.status);
               }
            },
            complete:function(jqXHR, textStatus)
           
					      {
					      alertCompleteMsg(jqXHR.status);
					      
            },
            error:function(jqXHR, textStatus){
               alertCompleteMsg(jqXHR.status);
            }
           
        })
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
                        "filter[orderid]" : $('#orderid').val()
                        
                    } );
                },
                "dataSrc": "content",
                 "complete": function(jqXHR, textStatus)
					      {
					      alertCompleteMsg(jqXHR.status);
					},
            },
            
            "columns": [
              <@security.authorize ifAnyGranted="bodyOnlineManager">
                       	{ "data": "r.userid", "defaultContent": ""},
                  </@security.authorize>
            
            	{ "data": "r.newBodySeriaNum", "defaultContent": "","render": function(data, type, row, meta) {
                	var option= "W"+data;
                	return option;
                }},
                <#--
                { "data": "media_type", "defaultContent": "","render": function(data, type, row, meta) {
                   if(data==0){
                   	 return '移动视频';
                   }else if(data ==1){
                  	 return '车身广告';
                   }else {
                		return "";
                	}
                }},-->
            	{ "data": "r.totalMoney", "defaultContent": ""},
            	<#--
            	{ "data": "r.isdivid", "defaultContent": "","render": function(data, type, row, meta) {
            	   if(data==0){
            	      return '否';
            	   }else{
            	      return '是';
            	   }
            	}},
            	{ "data": "r.fengqi", "defaultContent": "","render": function(data, type, row, meta) {
            	   if(row.r.isdivid==0){
            	      return '';
            	   }else{
            	      return data;
            	   }
            	}},-->
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
                	else if(data =='offline'){
                		t='线下支付';
                	}
                	return t;
                }},
                { "data": "r.created", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd HH:mm");
                	return d;
                }},
                { "data": "r.stats", "defaultContent": "","render": function(data, type, row, meta) {
                	var t='';
                	if(data =='init'){
                		t='待审核';
                	}else if(data =='pass'){
                		t='<font color="greeen">审核通过</font>';
                	}else if(data =='refu'){
                		t='<font color="red">订单已拒绝</font>';
                	}else if(data =='contractd'){
                		t='<font color="greeen">合同已创建</font>';
                	}
                	return t;
                }},
                { "data": "r.remarks", "defaultContent": ""},
                
                { "data": function( row, type, set, meta) {  return row.id;},
							"render" : function(data, type, row,meta) {
					var operations = '';
					if(row.r.stats=='init'){
						operations +='<a class="table-link" onclick="queryCarBoxBody(\'${rc.contextPath}\','+row.r.id+');" href="javascript:void(0)">详情</a>';
						operations +='&nbsp;&nbsp;<a class="table-link" onclick="handlebodyorder(\'${rc.contextPath}\','+row.r.id+",'"+"<@security.authentication property="principal.user.id"/>"+'\');" href="javascript:void(0)">处理</a>';
					}else if(row.r.stats=='pass'){
						operations +='&nbsp;&nbsp;<a class="table-link" onclick="gotoContract('+row.r.newBodySeriaNum+","+row.r.id+');" href="javascript:void(0)">查看合同</a>';
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
  		table.fnNameOrdering("orderBy").fnNoColumnsParams();
  		
  		 
    }
     function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                          '    <span>订单号</span>' +
                        '    <span>' +
                        '        <input id="orderid" value="">' +
                        '    </span>' +
                        '</div>'
        );
        
        $('#orderid').change(function() {
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
     //显示总条数 add by impanxh
    function fnDrawCallback(){
		var record_count = (this.fnSettings().fnRecordsTotal() );
		if(record_count>0){
	 	  $("#recordsTotal").html("&nbsp;"+record_count+"&nbsp;");
		  }
    }
 		
    $(document).ready(function() {
        initTable();
    } );
</script>


<div class="withdraw-wrap color-white-bg fn-clear">
	<div class="withdraw-title" style="padding-top: 0px; text-align: left;">
		网上订单 <span id="recordsTotal"
			style="background-color: #ff9966; color: #fff; font-size: 14px; border-radius: 4px;"></span>
	</div>
	<table id="table" class="display" cellspacing="0" width="100%">
		<thead>
			<tr>
				<@security.authorize
				ifAnyGranted="bodyOnlineManager">
				<th>下单用户</th> 
				</@security.authorize>
				<th>订单号</th>
				<!--<th>媒体类型</th>-->
				<th>订单总价</th>
				<#--<th>是否分期</th>
				<th>分期数</th>-->
				<th>相关产品个数</th>
				<th>支付方式</th>
				<th orderBy="created">下单时间</th>
				<th>状态</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
		</thead>

	</table>
</div>
</@frame.html>
