<#import "template/template.ftl" as frame>
<#global menu="车辆下刊">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="车辆下刊" js=["js/jquery-ui/jquery-ui.js","js/jquery-dateFormat.js",
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
            "dom": '<"#toolbar">lrtip',
            "searching": false,
            "ordering": true,
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
             { "data": function( row, type, set, meta) {
                                                  return row.id;
                                              },
										"render" : function(data, type, row,
												meta) {
											var operations = '<input type="checkbox" name="checkone" value="'+data+'" />';
											return operations;
										}
									},
				{ "data": "offlineContract.contractCode", "defaultContent": ""},
                { "data": "jpabus.plateNumber", "defaultContent": "","render": function(data, type, row, meta) {
                	return '<a  onclick="showbusOnline_history(\'${rc.contextPath}\','+row.jpabus.id+');">'+data+'</a>';
                }},
                { "data": "jpabus.serialNumber", "defaultContent": ""},
                { "data": "jpabus.oldSerialNumber", "defaultContent": ""},
                { "data": "jpabus.model.name", "defaultContent": ""},
                { "data": "jpabus.line.name", "defaultContent": ""},
                { "data": "jpabus.line.levelStr", "defaultContent": ""},
                { "data": "jpabus.categoryStr", "defaultContent": ""},
                { "data": "jpabus.company.name", "defaultContent": ""},
                { "data": "startDate", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                { "data": "endDate", "defaultContent": "","render": function(data, type, row, meta) {
                	var d= $.format.date(data, "yyyy-MM-dd");
                	return d;
                }},
                { "data": "real_endDate", "defaultContent": "","render": function(data, type, row, meta) {
              	  var d= $.format.date(data, "yyyy-MM-dd");
              	  if(d!=null){
	               	  var start=new Date(d.replace("-", "/").replace("-", "/")); 
	              		  if(start >= new Date()){
	                		d="<font color='red'>"+d+"</font>";
	                	}else {
	                		d="<font color='green'>"+d+"</font>";
	                	}
                	}
                	return d;
                }},
                 { "data": "jpabus.description", "defaultContent": ""},
                 { "data": "offlineContract.relateMan", "defaultContent": ""},
                
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
                车辆下刊
	     	</div>
	     	<div>
	     	<span>下刊日期：</span>
            <span>
                  <input  class="datepicker" type="text" name="offday" id="offday" data-is="isAmount isEnough" autocomplete="off" disableautocomplete=""> 
            </span>&nbsp;&nbsp;<input type="button" class="button_kind" style="width: 85px;height: 30px;" value="批量下刊" onclick="batchOff()"/>
            </div>    
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
                     <th > <input type="checkbox" name="checkAll" /></th>
                        <th>合同编号</th>
                        <th >车牌号</th>
                        <th >新车号</th>
                        <th >旧车号</th>
                        <th >车型</th>
                        <th >线路</th>
                        <th >线路级别</th>
                        <th >类别</th>
                        <th >营销中心</th>
                        <th>上刊日期</th>
                        <th>预计下刊日期</th>
                        <th>实际下刊日期</th>
                        <th>车辆描述</th>
                        <th>客户名称</th>
                    </tr>
                    </thead>
                </table>
</div>
</@frame.html>