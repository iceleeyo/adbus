<#import "template/template.ftl" as frame>
<#import "template/pickBusesLine.ftl" as pickBusesLine>
<@frame.html title="未绑定物料订单" js=["js/jquery-ui/jquery-ui.js", "js/jquery-ui/jquery-ui.auto.complete.js","js/datepicker.js", "js/jquery.datepicker.region.cn.js","js/layer-v1.9.3/layer/layer.js","js/progressbar.js","js/layer.onload.js"] css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/uploadprogess.css","css/liselect/pkg-generator.css$ver=1431443489.css"]>

<script type="text/javascript">
    $(document).ready(function() {
         $("#otherpay").hide(); 
     });
</script>


<script type="text/javascript">


function go_back(){
	history.go(-1);
	
}

</script>

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
                url: "${rc.contextPath}/order/ajax-orderedbuses",
                data: function(d) {
                    return $.extend( {}, d, {
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "categoryStr", "defaultContent": ""},
                { "data": "company.name", "defaultContent": ""},
                { "data": "line.name", "defaultContent": ""},
                { "data": "model.name", "defaultContent": ""},
                { "data": "busNumber", "defaultContent": "", "render" : function(data) {
                    return '<span style="color: rgb(245, 135, 8);">' + data + '</span>';
                }},
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
                updateTotalOrdered(data);
            })
        });
    }
    function initComplete() {
        $("div#toolbar").attr("style", "width: 100%;")
        $("div#toolbar").html(
                '<div style="width:100%">' +
                        '<span style="font-size:18px; font-weight:500">已选车辆：</span>'+
                        '<span class="totalOrdered" style="color: rgb(245, 135, 8);"></span>/辆，剩余车辆将机动分配。' +
                '</div>'
        );
	}
	
	function openPopup() {
        $("#orderBusesPopup").dialog("open");
    }
    

    function openPopup() {
        $("#orderBusesPopup").dialog("open");
    }

    $(document).ready(function() {
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

        function refreshCompanies() {
            $(".bus-line, .bus-model, .bus-company").closest("div").hide();
            $(".bus-line, .bus-model, .bus-company").empty();

            
            var cate = $(".bus-category").val();
            if (cate == "")
                return;
            $.ajax({
                url : "${rc.contextPath}/bus/ajax-bus-companies",
                type : "POST",
                data : {
                    "category" : cate
                },
                success : function(data) {
                    //update bus company options
                    $(".bus-company").empty();
                    $(".bus-company").append('<option value="" selected="selected"></option>');
                    for (var i in data) {
                        $(".bus-company").append('<option value="'+ data[i].id +'">'+ data[i].name + ' (共 ' + data[i].busCount + ' 辆)' +'</option>');
                    }
                    $(".bus-company").closest("div").show();
                    $(".bus-line, .bus-model").closest("div").hide();
                    $(".bus-line, .bus-model").empty();
                }
            }, "text");
        }
        function refreshLines() {
            $(".bus-line, .bus-model").closest("div").hide();
            $(".bus-line, .bus-model").empty();

            var cate = $(".bus-category").val();
            var company = $(".bus-company").val();
            if (cate == "" || company == "")
                return;
            $.ajax({
                url : "${rc.contextPath}/bus/ajax-bus-lines",
                type : "POST",
                data : {
                    "level" : level,
                    "category" : cate,
                    "companyId" : company
                },
                success : function(data) {
                    //update bus model options
                    $(".bus-line").empty();
                    $(".bus-line").append('<option value="" selected="selected"></option>');
                    for (var i in data) {
                        $(".bus-line").append('<option value="'+ data[i].id +'">'+ data[i].name + ' (共 ' + data[i].busCount + ' 辆)' +'</option>');
                    }
                    $(".bus-line, .bus-model").closest("div").show();
                    $(".bus-model").closest("div").hide();
                    $(".bus-model").empty();
                }
            }, "text");
        }

        function refreshModels() {
            $(".bus-model").closest("div").hide();
            $(".bus-model").empty();
            var cate = $(".bus-category").val();
            var company = $(".bus-company").val();
            var line = $(".bus-line").val();
            if (cate == "" || company == "" || line == "")
            return;

            $.ajax({
                url : "${rc.contextPath}/bus/ajax-bus-models",
                type : "POST",
                data : {
                    "level" : level,
                    "category" : cate,
                    "companyId" : company,
                    "lineId" : line
                },
                success : function(data) {
                    //update bus model options
                    $(".bus-model").empty();
                    $(".bus-model").append('<option value="" selected="selected"></option>');
                    for (var i in data) {
                        $(".bus-model").append('<option value="'+ data[i].id +'">'+ data[i].name + ' (共 ' + data[i].busCount + ' 辆)' +'</option>');
                    }
                    $(".bus-model").closest("div").show();
                }
            }, "text");
        }

        $(".bus-category").change(function() {
            refreshCompanies();
        });
        $(".bus-company").change(function() {
            refreshLines();
        });
        $(".bus-line").change(function() {
            refreshModels();
        });

        refreshOrderedBuses();
        
        
        //author:pxh 2015-05-20 22:36
		        $( "#line.id" ).autocomplete({
		  			source: "${rc.contextPath}/busselect/autoCompleteByName",
		  			change: function( event, ui ) { 
		  				/*if(ui.item!=null){alert(ui.item.value);}*/
		  				//table.fnDraw();
		  			 },
		  			 select: function(event,ui) {
		  			 $('#line.id').val(ui.item.value);
		  				//table.fnDraw();
		  			 }
				});
    });
    

    function sub() {
        $('#pickBusesForm').ajaxForm(
                function(data) {
                    jDialog.Alert(data.errorMessage);
                    if (data.error) {
                        updateTotalOrdered(data);
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
        <span><input type="button" onclick="openPopup()" class="block-btn" value="增加选择" style="float:right; margin:10px 20px 0px 20px;" ></span>
        <br>
        <div id="orderedBuses">
            <table id="orderedBusesTable" class="display compact" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th>类别</th>
                    <th>运营公司</th>
                    <th>线路</th>
                    <th>车型</th>
                    <th>数量（辆）</th>
                    <th>删除</th>
                </tr>
                </thead>

            </table>
        </div>

        <div id="orderBusesPopup"  title="选择车辆">
        <form data-name="withdraw" name="pickBusesForm" id="pickBusesForm"
              class="ui-form" method="post" action="${rc.contextPath}/order/ajax-order-buses"
              enctype="multipart/form-data">
        <div class="inputs">

            <div class="ui-form-item" style="display:none">
                <label class="ui-label mt10">选择线路：</label>
                <input class="ui-input  bus-line validate[required,integer,min[1],max[2000]]"
                        type="number" value="0" name="line.id"
                        id="line.id" data-is="isAmount isEnough"
                >
            </div>
            <div class="ui-form-item" style="display:none">
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
                    <input type="button" id="paybutton1" onclick="showtb1()" class="tab block-btn" value="添加合同">
                  
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
												type="text" name="contractCode" id="code"  value="${(contractView.mainView.contractCode)!''}"
												data-is="isAmount isEnough" autocomplete="off"
												disableautocomplete="" placeholder="中英文、数字、下划线">
										</div>
										<div class="ui-form-item">
											<label class="ui-label mt10"><span
												class="ui-form-required">*</span>合同名称:</label>
												<input class="ui-input validate[required,custom[noSpecialLetterChinese],minSize[5],maxSize[120]]"
												type="text" name="contractName" value="${(contractView.mainView.contractName)!''}"
												id="name" data-is="isAmount isEnough"
												autocomplete="off" disableautocomplete="">
											<p class="ui-term-placeholder" placeholder="中英文、数字、下划线"></p>

										</div>			
					</div>
				</div>
									  
				</form>
				</div>	
                 <div class="worm-tips">
		<div class="tips-title">
			<span class="icon"></span> [温馨提示：您可以选择先支付订单，后绑定物料；也可以选择先绑定物料，后支付订单。]
		</div>
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






