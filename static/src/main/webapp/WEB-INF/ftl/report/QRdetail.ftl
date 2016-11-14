<#import "../template/template.ftl" as frame> <#global menu="二维码列表 ">
<@frame.html title="详情 " js=["js/jquery-ui/jquery-ui.js",
"js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/highcharts/highcharts.js","js/highcharts/exporting.js"]
css=["js/jquery-ui/jquery-ui.css"]>


<script type="text/javascript">

    $(document).ready(function() {
        showData();
        getCharData();
        setInterval('getCharData()',5000);
    });
    function showChar(data1,data2){

         $('#container').highcharts({
        title: {
            text: 'pv 统计',
            x: -20 //center
        },
        subtitle: {
            text: '',
            x: -20
        },
        xAxis: {
            categories: data1
        },
          plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: false
            }
        },
        yAxis: {
            title: {
                text: '扫码次数 (次)'
            },
            
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: '次'
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: [{
            name: '扫码量',
            data:data2
        }]
    });
    }
    function showData(){
    var para='${para!''}';
     var url="http://60.205.168.48:9009/url/getByid/"+para;
     
     $.get(url, function(data) {
               $("#sourceUrl_s").append(data.sourceUrl_s);
               $("#description_s").append(data.description_s);
               $("#img").attr("src",data.img);
            })
            
            
    }
    function getCharData(){
    
    
     var para='${para!''}';
     var url="http://60.205.168.48:9009/url/daycount/"+para;
     $.get(url, function(data) {
     console.log(data.days+"_"+data.number);
           showChar(data.days,data.number) 
            })
    }
    
    
</script>



<div class="withdraw-wrap color-white-bg fn-clear">

	<form data-name="withdraw" name="userForm2" id="userForm2"
		class="ui-form" method="post" action="http://60.205.168.48:9009/url/save"
		enctype="multipart/form-data">
		<div class="withdrawInputs">
			<div class="inputs">
				<div class="ui-form-item">
					<label class="ui-label mt10">url:</label>
						<span id="sourceUrl_s"></span>
				</div>
				<div class="ui-form-item">
					<label class="ui-label mt10">描述:</label>
					<span id="description_s"></span>
				</div>
				
            <div >
                     <img src="" id="img" width="300" height="300" style="margin-left:200px" border="1px solid #d0d0d0;"/>
			</div>
		</div>
	
</div>

<div id="container" style="min-width:400px;height:400px"></div>
</@frame.html>
