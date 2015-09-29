<#import "template/template_blank.ftl" as frame>
<@frame.html title="套餐详情" js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
"js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js",
"js/jquery.datepicker.region.cn.js","js/progressbar.js"]
css=["js/jquery-ui/jquery-ui.css","css/uploadprogess.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>
<meta http-equiv="X-Frame-Options" content="SAMEORIGIN"> 
<script type="text/javascript">
     var orderBusesTable;
	function refreshOrderedBuses() {
		orderBusesTable = $('#table')
				.dataTable(
						{
							"dom" : '<"#toolbar">t',
							"searching" : false,
							"ordering" : false,
							"serverSide" : false,
							"ajax" : {
								type : "GET",
								url : "${rc.contextPath}/product/ajax-BusOrderDetailV2",
								data : function(d) {
									return $.extend({}, d, {
										"pid" : '${pid!''}'
									});
								},
								 "dataSrc": "content",
							},
							"columns" : [
                { "data": "leval", "defaultContent": "", "render": function(data, type, row,meta) {
                           if(data=='S'){
                              return  '特级';
                            }else if(data=='APP'){
                               return 'A++';
                            }else if(data=='AP'){
                               return 'A+';
                            }else if(data=='A'){
                               return 'A';
                                 }else{
                                    return '经纬线';
                                 }
                               }},  
                { "data": "doubleDecker", "defaultContent": "", "render": function(data, type, row,meta) {
                           if(data==false){
                              return  ' 单层';
                            }else{
                               return ' 双层';
                                 }
                               }}, 
                { "data": "busNumber", "defaultContent": 0}, 
                { "data": "days", "defaultContent": 0}, 
                { "data": "price", "defaultContent": 0}, 
									 ],
							"language" : {
								"url" : "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
							},
							"initComplete" : initComplete,
							"drawCallback" : drawCallback,
						});
		orderBusesTable.fnNameOrdering("orderBy").fnNoColumnsParams();
	}
	function drawCallback() {
		$('.table-action').click(function() {
			$.post($(this).attr("url"), function(data) {
			if(data.left){
			    layer.msg(data.right);
				 orderBusesTable.dataTable()._fnAjaxUpdate();
				 }else{
				  layer.msg(data.right);
				 }
			})
		});
	}
	function initComplete() {
		$("div#toolbar").attr("style", "width: 100%;")
		$("div#toolbar").html('');
	}

</script>
<script type="text/javascript">
    $(document).ready(function() {
      refreshOrderedBuses();
    });
</script>
<div id="relateSup">
			<div class="p20bs mt10 color-white-bg border-ec">
			   <div id="orderedBuses">
				<table id="table" class="display compact"
					cellspacing="0" width="100%">
					<thead>
				<tr class="tableTr">
					<th>线路级别</th>
					<th>车型</th>
					<th>车辆数</th>
					<th>刊期</th>
					<th>金额</th>
				</tr>
					</thead>
				</table>
						</div>
						</div>
						

</@frame.html>
