<#import "template/template.ftl" as frame>
<#global menu="广告查询">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="广告查询" js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
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
</style>

<script type="text/javascript">
	
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar"><"top"il>rt<"bottom"p><"clear">',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "scrollX": true,
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-bus_offShelf",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[contracCode]" : $('#contractCode').val(),
                        "filter[linename]" : $('#linename').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "jpabus.plateNumber", "defaultContent": "","render": function(data, type, row, meta) {
                	return '<a  onclick="showbusOnline_history(\'${rc.contextPath}\','+row.jpabus.id+');">'+data+'</a>';
                }},
				{ "data": "offlineContract.contractCode", "defaultContent": ""},
                { "data": "jpabus.model.name", "defaultContent": ""},
                 { "data": "jpabus.description", "defaultContent": ""},
                { "data": "offlineContract.company", "defaultContent": ""},
                { "data": "jpabus.line.name", "defaultContent": ""},
                { "data": "startDate", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                { "data": "endDate", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                { "data": "jpabus.company.name", "defaultContent": ""},
                
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
        } );
        table.fnNameOrdering("orderBy").fnNoColumnsParams();
    }

    function initComplete() {
        $("div#toolbar").html(
                 '<div>' +
                        '    <span>合同编号:</span>' +
                        '    <span>' +
                        '        <input id="contractCode" value="">' +
                        '    </span>&nbsp;&nbsp;' +
                        '&nbsp;&nbsp;<span>线路：</span>'+
                        '<span><input value="" id="linename" data-is="isAmount isEnough">'+
                        '</span>&nbsp;&nbsp;'+
                        '</div>'
        );

        $('#contractCode,#linename').change(function() {
            table.fnDraw();
        });
        $("#linename").autocomplete({
    		minLength: 0,
    			source : "${rc.contextPath}/busselect/autoComplete?tag=a",
    			change : function(event, ui) {
    			},
    			select : function(event, ui) {
    				$('#linename').val(ui.item.value);
    				table.fnDraw();
    			}
    		}).focus(function () {
           				 $(this).autocomplete("search");
       				 });
    }
		
    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }
    $(document).ready(function() {
        initTable();
       $("#checkAll").click(function(){
         if($(this).attr("checked")=="checked"){
          $("input[name='checkone']:checkbox").attr("checked",true);
         }else{
          $("input[name='checkone']:checkbox").attr("checked",false);
         }
       });
    } );
    function batchOff(){
       var offday=$("#offday").val();
            if(offday==""){
            layer.msg("请选择下刊日期");
            return;
            }
        	var o = document.getElementsByName("checkone");
        	var dIds='';
        	for(var i=0;i<o.length;i++){
                if(o[i].checked)
                dIds+=o[i].value+',';   
           }
           if(dIds==""){
        	   layer.msg("请选择需要下刊的车辆");
        	   return false;
           }
   		var param={"ids":dIds,"offday":offday};
		layer.confirm('确定下刊吗？', {icon: 3}, function(index){
    		layer.close(index);
		    if(true){
		    	 $.ajax({
		    			url:"${rc.contextPath}/bus/batchOffline",
		    			type:"POST",
		    			async:false,
		    			dataType:"json",
		    			data:param,
		    			success:function(data){
		    				if (data.left == true) {
		    					layer.msg(data.right);
		    					 table.dataTable()._fnAjaxUpdate();
		    				} else {
		    					layer.msg(data.right,{icon: 5});
		    				}
		    			}
		       });  
		       }
		});		
    
        }
         
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title">
              广告查询
	     	</div>
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
                     <th > <input type="checkbox" name="checkAll" id="checkAll" /></th>
                        <th >车牌号</th>
                        <th>合同编号</th>
                        <th >车型</th>
                        <th>客户名称</th>
                        <th >线路</th>
                        <th>上刊日期</th>
                        <th>下刊日期</th>
                        <th>营销中心</th>
                    </tr>
                    </thead>
                </table>
</div>
</@frame.html>