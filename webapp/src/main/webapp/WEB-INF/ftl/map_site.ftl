<#global menu="站点">
<html>
<META name=GENERATOR content="MSHTML 10.00.9200.16576">
<style type="text/css">
body, html{width: 100%;height: 100%;margin:0;font-family:"微软雅黑";}
		#l-map{height:300px;width:100%;}
		#r-result {width:100%;}
</style>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=Ok6Nri8q5UjAa0anpoGv7R3o"></script>

<body>
	<div id="l-map"></div>
	<div id="r-result"></div>
</body>
</html>


<#if lineName??>
<script type="text/javascript">
	// 百度地图API功能
	var map = new BMap.Map("l-map");            // 创建Map实例
	map.centerAndZoom(new BMap.Point(116.404, 39.915), 12);

	var busline = new BMap.BusLineSearch(map,{
		renderOptions:{map:map,panel:"r-result"},
			onGetBusListComplete: function(result){
			   if(result) {
				 var fstLine = result.getBusListItem(0);//获取第一个公交列表显示到map上
				 busline.getBusLine(fstLine);
			   }
			}
	});
	function busSearch(){
		var busName = '${lineName}';
		busline.getBusList(busName);
	}
	setTimeout(function(){
		busSearch();
	},1500);
	
	
	
	<#if _mapLocationKey?? && _mapLocationKey.address??>
	setTimeout(function(){
	var point = new BMap.Point(${_mapLocationKey.locationPair.left},${_mapLocationKey.locationPair.right});
	var myGeo = new BMap.Geocoder();
	myGeo.getPoint("${_mapLocationKey.address}", function(point){
		if (point) {
          var marker1=new BMap.Marker(point);
			map.addOverlay(marker1);
          	var infoWindow1 = new BMap.InfoWindow("${_mapLocationKey.address}");
			marker1.addEventListener("click", function()		{this.openInfoWindow(infoWindow1);});
			 
		}else{
			alert("您选择地址没有解析到结果!");
		}
	}, "${_mapLocationKey.city}");
	},2800);
	</#if>
</script>
</#if>
