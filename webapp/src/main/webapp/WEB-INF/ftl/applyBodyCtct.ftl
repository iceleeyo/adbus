<#import "template/template.ftl" as frame>
<@frame.html title="未绑定物料订单" js=["js/jquery-ui/jquery-ui.js","js/nano.js","js/jquery-dateFormat", "js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/layer-v1.9.3/layer/layer.js","js/progressbar.js","js/layer.onload.js"] css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/uploadprogess.css","css/liselect/pkg-generator.css$ver=1431443489.css"]>

<script type="text/javascript">
    var orderBusesTable;
    function refreshOrderedBuses () {
        orderBusesTable = $('#orderedBusesTable').dataTable( {
            "dom": '<"#toolbar">t',
            "searching": false,
            "ordering": false,
            "serverSide": true,
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/busselect/ajax-buslock",
                data: function(d) {
                     return $.extend( {}, d, {
                        "seriaNum" : ${seriaNum}
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "line.name", "defaultContent": ""},
                { "data": "remainNuber", "defaultContent": "", "render" : function(data) {
                    return '<span style="color: rgb(245, 135, 8);">' + data + '</span>';
                }},
                { "data": "startDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                }},
                { "data": "endDate", "defaultContent": "", "render": function(data) {
                    return data == null ? "" : $.format.date(data, "yyyy-MM-dd");
                } },
                { "data": function( row, type, set, meta) {
                    return row.id;
                },
                    "render": function(data, type, row, meta) {
                        var operations = '';
                        operations+= '<a class="table-action" href="javascript:void(0);">删除</a>';
                        return operations;
                    }
                },
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
        } );
        orderBusesTable.fnNameOrdering("orderBy").fnNoColumnsParams();
    }
    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function(data) {
                orderBusesTable.fnDraw(true);
                //updateTotalOrdered(data);
            })
        });
    }
    function initComplete() {
        $("div#toolbar").attr("style", "width: 100%;")
         $("div#toolbar").html(
                '<div style="width:100%">' +
                        '<span><input type="button" onclick="openPopup()" class="block-btn" value="增加选择" style="float:right" ></span>' +
                '</div>'
        );
	}
	
	function openPopup() {
        //$("#orderBusesPopup").dialog("open");
         layer.open({
                type: 1,
                title: "test layer",
                skin: 'layui-layer-rim', 
                area: ['450px', '650px'], 
                content: $("#orderBusesPopup").html(),
                     });
    }
    
    $(document).ready(function() {
     refreshOrderedBuses();
        $( "#orderBusesPopup" ).dialog({
            autoOpen: false,
            height: 490,
            width: 450,
            modal: true,
            buttons: {
            },
            close: function() {
                $(".bus-line, .bus-model, .bus-company").empty();
                $("#busNumber").val("0");
            }
        });
		        $( "#lineid" ).autocomplete({
		  			source: "${rc.contextPath}/busselect/autoComplete",
		  			change: function( event, ui ) {
		  			 },
		  			 select: function(event,ui) {
		  			    alert(ui.item.dbId);
		  			 }
				});
    });
    
function serchModel(){
//busselect/selectBusType?buslinId=187

//alert($('#lineid').val());
}
    function sub() {
        $('#pickBusesForm').ajaxForm(
                function(data) {
                    jDialog.Alert(data.errorMessage);
                    if (data.error) {
                        //updateTotalOrdered(data);
                        //refreshOrderedBuses();
                        orderBusesTable.dataTable()._fnAjaxUpdate();
                        $("#orderBusesPopup").dialog("close");
                    }
                }
        ).submit();
    }
</script>


<div class="color-white-bg fn-clear">

<div id="relateSup" >
    <div class="p20bs mt10 color-white-bg border-ec">
        <H3 class=".withdraw-title text-xl title-box"><A class="black" href="#">选取车辆</A></H3>
        <br>
        <div id="orderedBuses">
            <table id="orderedBusesTable" class="display compact" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th>线路</th>
                    <th>数量（辆）</th>
                     <th>上刊时间</th>
                    <th>下刊时间</th>
                    <th>操作</th>
                </tr>
                </thead>

            </table>
        </div>

        <div id="orderBusesPopup"  title="选择车辆">
        <form data-name="withdraw" name="pickBusesForm" id="pickBusesForm"
              class="ui-form" method="post" action="${rc.contextPath}/order/ajax-order-buses"
              enctype="multipart/form-data">
              <input type="hidden" name="seriaNum" id="seriaNum" value="${seriaNum}"/>
              <input type="hidden" name="line.id" id="line.id" value=""/>
        <div class="inputs">
            <div class="ui-form-item" >
                <label class="ui-label mt10">选择线路：</label>
               <input id="lineid" name="lineid" value="" onblur="serchModel();"
                         						 class="ui-input validate[required,custom[noSpecialLetterChinese]]" placeholder="请选择线路" />
            </div>
            <div class="ui-form-item" >
                <label class="ui-label mt10">选择车型：</label>
                <select class="ui-input bus-model"
                        name="model.id" id="model.id">
                    <option value="" selected="selected"></option>
                </select>
            </div>
            <div class="ui-form-item">
                <label class="ui-label mt10">选取数量：</label>
                <input class="ui-input validate[required,integer,min[1],max[2000]]"
                        type="number" value="0" name="busNumber"
                        id="busNumber" data-is="isAmount isEnough"
                        autocomplete="off" disableautocomplete="" placeholder="">
            </div>
            <div class="ui-form-item">
							<label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>上刊日期:
															</label> <input
												class="ui-input datepicker validate[required,custom[date],past[#endDate]]" 
												type="text" name="startDate1" value=""
												id="startDate" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
		     </div>

										<div class="ui-form-item">
											<label class="ui-label mt10"><span
                                                    class="ui-form-required">*</span>下刊日期:</label> <input
												class="ui-input datepicker validate[required,custom[date],future[#startDate]"
												type="text" name="endDate1"
												id="endDate" data-is="isAmount isEnough"  value=""
												autocomplete="off" disableautocomplete="">
										</div>
        </div>
        <div class="ui-form-item widthdrawBtBox" style="position: absolute; bottom: 10px;">
            <input type="button" onclick="sub()" class="block-btn"
                   value="确认选车" >
        </div>
        </form>
        </div>
    </div>

              <div class="p20bs mt10 color-white-bg border-ec">
                 <H3 class="text-xl title-box" style="text-align: left;">
                 </H3>
                 
                 <div class="withdraw-wrap color-white-bg fn-clear">
				<form data-name="withdraw" name="userForm2" id="userForm2"
					  class="ui-form" method="post" action="${rc.contextPath}/contract/saveContract?dos_authorize_token=b157f4ea25e968b0e3d646ef10ff6624&t=v1"
					  enctype="multipart/form-data">
				<div class="withdrawInputs">
					<div class="inputs">
						<div class="ui-form-item">
											<label class="ui-label mt10">
											<span class="ui-form-required">*</span>合同编号:
											</label> 
												<input class="ui-input validate[required,custom[noSpecialContratNum],minSize[5],maxSize[120]]"
												type="text" name="contractCode" id="code"  value=""
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="" placeholder="中英文、数字、下划线">
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>合同名称:</label>
												<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
												type="text" name="contractName" value=""
												id="name" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
											<p class="ui-term-placeholder" placeholder="中英文、数字、下划线"></p>

										</div>			
					</div>
				</div>
									  
				</form>
				</div>	
             <div id="tb2">
                 		
								<p style="text-align: center;margin-top: 10px;">
									<button type="button" id="subsupbutton2" onclick="relatSup()" class="block-btn" >确认</button><br>
								<br/>
								</p>
				  </div>
				  </div>
				<br>
             </div>	
</div>

</@frame.html>






