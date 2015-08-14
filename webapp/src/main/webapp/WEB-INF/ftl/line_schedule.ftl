<#import "template/template.ftl" as frame>
<#import "template/orderDetail.ftl" as orderDetail/>
    <#global menu="上刊巴士列表">
<@frame.html title=menu>

<style type="text/css">
    .center {margin: auto;}
    .frame {width: 1000px;}
    .div {text-align:center; margin:25px;}
    div#toolbar {float: left;}
    .processed {color: limegreen;}
    .invalid {color: red;}
    .hl {background-color: #ffff00;}
    #table {font-size: 14px;}
    #table td {position:relative;}
    #table td .per-middle {position:absolute;background-color: #ffad20;border-left: 1px solid white;border-right: 1px solid white;top:0;height:100%;z-index:1}
    #table td .per-first-or-last {position:absolute;background-color: #4acd48;border-left: 1px solid white;border-right: 1px solid white;top:0;height:100%;z-index:1}
</style>
 
<script type="text/javascript">
    var table;
    function initTable () {
        $('#metatable').dataTable({
            "dom": 'rt',
            "searching": false,
            "ordering": false,
            "serverSide": false,
        });

        table = $('#table').dataTable( {
            "dom": '<"#toolbar">rt',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "ajax": {
                type: "POST",
                url: "${rc.contextPath}/busselect/order-body-ajax-list2",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[lineId]" : "${lineId}", "filter[modelId]" : "${modelId}"
                        
                    } );
                },
                    "dataSrc": "content",
            },
            "columns": [
                {
                    "data" : "serialNumber", "defaultContent": "", "render" : function(data, type, row, meta) {
                    return data+row.bus.plateNumber;
                    }
                },
                
    <#list dates as d>
                { "data": "", "defaultContent": "",
                    "render" : function(data, type, row, meta) {
                    var v = row.map['${d}'];
                    
                    if (typeof(v) == "undefined"){
                    	result ='<span class="per-middle" style="left:0%;width:70%;background-color:green"></span>';
                    }else {
                     	var  result = '<span class="per-middle layer-tips" tip="'+(v.company)+'" style="left:0%;width:70%;background-color:red"></span>';
                    }
                        return result;
                }},
    </#list>
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
            "fnDrawCallback": fnDrawCallback,
        } );

    }

    function initComplete() {
      bindLayerMouseOver();
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


<div class="p20bs mt10 withdraw-wrap color-white-bg fn-clear">
    <H3 class="text-xl title-box"><A class="black" href="#">线路车辆排期表<span id="recordsTotal" style="background-color:#ff9966;font-size: 14px;border-radius: 4px;"></span></A></H3>
            <div class="div" style="overflow-x:auto;">
                      
                <table id="table" class="cell-border compact display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th style="min-width:130px;">车辆自编号</th>
                        <#list dates as d>
                            <th style="min-width:10px;">${d?substring(5)}</th>
                        </#list>
                    </tr>
                    </thead>

                </table>
                
               
            </div>
            
            <div class="worm-tips">
		<div class="tips-title">
			<span class="icon"></span>排期说明：
			<ol>
			<li>1.一个方格代表某一天的一个广告时段。</li>
			<li>2.彩条代表该广告的播出位置，绿色代表首播或者末播，橘色代表中间播。</li>
		</ol>
		</div>
		</div>
</div>
 
</@frame.html>