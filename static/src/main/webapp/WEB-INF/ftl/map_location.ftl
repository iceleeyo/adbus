<#global menu="位置确认:${address}">
<META name=GENERATOR content="MSHTML 10.00.9200.16576">
<style type="text/css">
#allmap {
	width: 100%;
	height: 100%;
	overflow: hidden;
	margin: 0;
	font-family: "微软雅黑";
}
</style>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/jquery.form.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/layer-v1.9.3/layer/layer.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/layer.onload.js"></script>
<script type="text/javascript" language="javascript"
	src="${rc.contextPath}/js/layer-v1.9.3/layer-site.js"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=Ok6Nri8q5UjAa0anpoGv7R3o"></script>
<#if locationPair??>
<div id="allmap" width="100%" height="100%"></div>
<script type="text/javascript">
	// 百度地图API功能
	var map = new BMap.Map("allmap");
	var point = new BMap.Point(${locationPair.left},${locationPair.right});
	map.centerAndZoom(point,12);
	// 创建地址解析器实例
	var myGeo = new BMap.Geocoder();
	// 将地址解析结果显示在地图上,并调整地图视野
	myGeo.getPoint("${address}", function(point){
		if (point) {
			map.centerAndZoom(point, 16);
			var myIcon = new BMap.Icon("/imgs/2.png", new BMap.Size(28,39));
			var marker1=new BMap.Marker(point,{icon:myIcon});
			map.addOverlay(marker1);
			marker1.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
			var infoWindow1 = new BMap.InfoWindow("<p style='font-size:14px;'><#if BEIBA_COMPANY_NAME??>${BEIBA_COMPANY_NAME}<#else>${address}</#if></p>");
			marker1.addEventListener("click", function(){this.openInfoWindow(infoWindow1);});
			<#if BEIBA_COMPANY_NAME??>layer.msg('您选择地址没有解析到结果!');</#if>
		}else{
			  layer.msg("您选择地址没有解析到结果!");
		}
	}, "${city}");
</script>
<#else>
<style type="text/css">
body {
	TEXT-ALIGN: center;
}

#center {
	MARGIN-RIGHT: auto;
	MARGIN-LEFT: auto;
}
</style>
layer.msg("查询不到相应的查询信息,请确认查询地址"); </#if>
