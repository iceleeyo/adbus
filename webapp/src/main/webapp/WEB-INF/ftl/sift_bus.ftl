<#import "template/template_intro.ftl" as frame> <#global menu="">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="商品sift" left=false nav=false
js=["js/jquery-dateFormat.min.js","js/sift_bus.js","js/jquery-ui/jquery-ui.js",
"js/datepicker.js", "js/jquery.datepicker.region.cn.js"]
css=["css/sift.css","css/account.css","js/jquery-ui/jquery-ui.css"]>

<style type="text/css">
#busNumber {
	height: 30px;
	float: left;
}

#startDate1 {
	float: left;
}

th {
	border-top: none !important;
}
</style>
<head>

</head>
<body>


	<div class="pg-container">
		<div class="pg-container-main">
			<div class="container-12">
				<ul class="breadcrumb ml10 m11 s-clear">
					<li class="s-left fsize-16 bread-homep"><a class="gray-text"
						href="/">首页</a></li>
					<li class="s-left breadcrumb-right"></li>
					<li class="s-left bread-child"><a class="gray-text" href="#">车身广告</a>
					</li>
				</ul>
			</div>

			<#if (siftList?size>0) >
			<div class="container">

				<div class="module1 withdraw-wrap" style="width: 1000px;">
					<div class="title s-clear ">
						<span> 线路推荐 </span>
					</div>
					<div class="fn-clear pg-uplan-product-list text-middle mt15 mb10">
						<#list siftList as item>
						<div class="fn-left pg-uplan-product-item mr15">
							<a href="javascript:;" target="_Blank"
								onclick="goV2Detail('/order/ibus/${item.id}');">
								<dl class="bg-color-white">
									<dt class="ub" style="height: 310px;">
										<div>
											<span class="mr10" style="font-size: 16px;">套餐价格 <font
												color="#2ea7e0">${item.price!''}</font>
											</span>
										</div>
										<div style="margin: -15px 2px 0px 0px; height: 80px;">
											<span class="mr10" style="font-size: 12px;">套餐描述:
												${item.remarks!''} </span>
										</div>
										<div class="img_box" style="width: 100%; height: 170px;">
											<img class="img_size" src="${rc.contextPath}/imgs/t8.jpg"
												style="width: 100%; height: 100%;">
										</div>
									</dt>
								</dl>
							</a>
						</div>
						</#list>
					</div>
				</div>
			</div>
			</#if>


			<div class="container-12 mt10 s-clear">
				<div class="sift-box">


					<div class="sift-item s-clear">
						<span>车型类型：</span>
						<div class="sift-list" qt="dc">
							<a class="item" href="javascript:;" sort="-1" qc="Y"> 双层<i>×</i></a>
							<a class="item" href="javascript:;" sort="-1" qc="N">单层<i>×</i></a>
						</div>
					</div>
					<div class="sift-item s-clear">
						<span>线路级别：</span>
						<div class="sift-list" qt="lev">
							<a class="item" href="javascript:;" sort="-1" qc="S">特级<i>×</i></a>
							<a class="item" href="javascript:;" sort="-1" qc="APP">A++<i>×</i></a>
							<a class="item" href="javascript:;" sort="-1" qc="AP">A+<i>×</i></a>
							<a class="item" href="javascript:;" sort="-1" qc="A">A<i>×</i></a>
						</div>
					</div>
					<div class="sift-item s-clear">
						<span>展示周期：</span>
						<div class="sift-list" qt="d">
							<a class="item active" href="javascript:;" sort="-1" qc="30">30<i>×</i></a>
							<a class="item" href="javascript:;" sort="-1" qc="60">60<i>×</i></a>
							<a class="item" href="javascript:;" sort="-1" qc="90">90<i>×</i></a>
							<a class="item" href="javascript:;" sort="-1" qc="180">180<i>×</i></a>
							<a class="item" href="javascript:;" sort="-1" qc="360">360<i>×</i></a>
						</div>
					</div>

					<#if city.id==2>
					<div class="sift-item s-clear">
						<span>商圈：</span>
						<div class="sift-list" qt="B">
							<a class="item" href="javascript:;" sort="-1">王府井<i>×</i></a> <a
								class="item" href="javascript:;" sort="-1">西单<i>×</i></a> <a
								class="item" href="javascript:;" sort="-1">国贸<i>×</i></a> <a
								class="item" href="javascript:;" sort="-1">公主坟<i>×</i></a> <a
								class="item" href="javascript:;" sort="-1">中关村<i>×</i></a> <a
								class="item" href="javascript:;" sort="-1">东直门<i>×</i></a>
						</div>
					</div>

					<div class="sift-item s-clear">
						<span>城区：</span>
						<div class="sift-list" qt="C">
							<a class="item" href="javascript:;" sort="-1">海淀<i>×</i></a> <a
								class="item" href="javascript:;" sort="-1">朝阳<i>×</i></a> <a
								class="item" href="javascript:;" sort="-1">丰台<i>×</i></a> <a
								class="item" href="javascript:;" sort="-1">东城<i>×</i></a> <a
								class="item" href="javascript:;" sort="-1">西城<i>×</i></a> <a
								class="item" href="javascript:;" sort="-1">石景山<i>×</i></a> <a
								class="item" href="javascript:;" sort="-1">通州<i>×</i></a> <a
								class="item" href="javascript:;" sort="-1">大兴<i>×</i></a> <a
								class="item" href="javascript:;" sort="-1">密云<i>×</i></a>
						</div>
					</div>
					</#if>
					<div class="sift-item s-clear">
						<span>车辆数量：</span> <input id="busNumber"
							onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^\d.]/g,'')}else{this.value=this.value.replace(/[^\d.]/g,'')}"
							type="text" placeholder="请输入车辆数量"> <span>开始时间：</span> <input
							class="ui-input datepicker validate[required,custom[date] "
							type="text" name="startDate1" id="startDate1"> <input
							type="hidden" id="sn" name="sn" value='${seriaNum}'> <span
							id="pd"> </span> <span id="btn"> <a href="javascript:;"
							onclick="addPlan('${rc.contextPath}');">增加计划</a></span>
						<@security.authorize access="isAuthenticated()"> <input
							type="hidden" id="lc" value="1" /> </@security.authorize>
						<@security.authorize access="! isAuthenticated()"> <input
							type="hidden" id="lc" value="0" /> </@security.authorize>
					</div>


				</div>
			</div>
		</div>


		<div class="withdraw-wrap color-white-bg fn-clear"
			style="margin-top: 30px;">
			<div class="withdraw-title fn-clear">投放计划列表</div>
			<table id="table" class="display" cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>单双层</th>
						<th>线路级别</th>
						<th>展示周期</th>
						<th>预购车辆</th>
						<th>支付金额</th>
						<th>展示时间</th>
						<th>管理</th>
					</tr>
				</thead>
				<@security.authorize access="isAuthenticated()">
				<tfoot>
					<tr>
						<th id="sum" colspan="7"
							style="text-align: center; height: 50px; background-color: navajowhite;"><font
							color="#000">合计:</font></th>
					</tr>

				</tfoot>
				</@security.authorize>
			</table>
		</div>


	</div>
	<@security.authorize access="isAuthenticated()">
	<input type="hidden" id="lc" value="1" /> </@security.authorize>
	<@security.authorize access="! isAuthenticated()">
	<input type="hidden" id="lc" value="0" /> </@security.authorize>



	</div>

	<script type="text/javascript">
      </script>
	<input type="hidden" id="sh" value="" />

	<script type="text/javascript">
   var orderBusesTable;
	function initTable() {
		orderBusesTable = $('#table')
				.dataTable(
						{
							"dom" : '<"#toolbar">t',
							"searching" : false,
							"ordering" : false,
							"serverSide" : false,
							"ajax" : {
								type : "GET",
								url : "${rc.contextPath}/product/sift_orderdetailV2",//ajax-orderdetailV2
								data : function(d) {
									return $.extend({}, d, {
										"seriaNum" : '${seriaNum}'
									});
								},
								 "dataSrc": function(json) {return json;},
							},
							"columns" : [
			    { "data": "doubleDecker", "defaultContent": "", "render": function(data, type, row, meta) {
                      return data?"双层":"单层";
                }},
                { "data": "leval", "defaultContent": "", "render": function(data, type, row, meta) {
                		var r='';
                		if(data ==0){
                			r="特级";
                		}else if(data ==1){
                			r="A++";
                		}else if(data ==2){
                			r="A+";
                		}else if(data ==3){
                			r="A+";
                		}
                      return r;
                }}, 
                { "data": "days", "defaultContent": ""
                },
                { "data": "busNumber", "defaultContent": ""},
                 { "data": "price", "defaultContent": ""},
                
                { "data": "startTime", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                	}
                 }, 
                { "data": "", "defaultContent": "", "render": function(data, type, row, meta) {
                  var operations ='<a class="table-link del" onclick="delPlan('+row.id+');" href="javascript:void(0)">删除</a>';
                         return operations;
                } }
                    ],
							"language" : {
								"url" : "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
							},
							"initComplete" : initComplete,
							"drawCallback" : drawCallback,
							<@security.authorize access="isAuthenticated()">
							"footerCallback": function ( row, data, start, end, display ) {
							
									if(end==0){
										return ;
									}
					            var api = this.api(), data;
					 
					            // Remove the formatting to get integer data for summation
					            var intVal = function ( i ) {
					                return typeof i === 'string' ?
					                    i.replace(/[\$,]/g, '')*1 :
					                    typeof i === 'number' ?
					                        i : 0;
					            };
					 
					            // Total over all pages
					            total = api
					                .column( 4 )
					                .data()
					                .reduce( function (a, b) {
					                    return intVal(a) + intVal(b);
					                } );
					            // Total over this page
					 		   var operations ='<a style="margin-left:30px;" onclick="submitPlan();" id="sendToServer" href="javascript:void(0)"><font color="red">支付并提交订单</font></a>';
					            // Update footer
					            $( api.column( 3 ).footer() ).html(
					            '<strong class="swift-bprice" id="jd-price">￥'+('合计:'+total+operations )+'</strong>'
					            );
					        }
					         </@security.authorize>
						});
		orderBusesTable.fnNameOrdering("orderBy").fnNoColumnsParams();
	}

	function drawCallback() {
		$('.table-action').click(function() {
			$.post($(this).attr("url"), function(data) {
			if(data){
				 orderBusesTable.dataTable()._fnAjaxUpdate();
				 }else{
				 alert("非法操作");
				 }
			})
		});
	}
	function initComplete() {
		$("div#toolbar").attr("style", "width: 100%;")
		$("div#toolbar")
				.html('');
				
		$('#busNumber').change(function() {
		 queryPrice();
        });
						
	}

    $(document).ready(function() {
        initTable();
        initSwift2(orderBusesTable)
    } );
    
   
</script>






</body>
</@frame.html>
