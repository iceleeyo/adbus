<#import "template/template.ftl" as frame>
<#global menu="空媒体统计">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="空媒体统计" js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js",
"js/jquery-ui/jquery-ui.auto.complete.js"] 
css=["js/jquery-ui/jquery-ui.css","css/jquery-ui-1.8.16.custom.css","js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"]>

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
            "columnDefs": [
                { "orderable": false, "targets": [7] },
            ],
            "iDisplayLength" : 50,
            "aLengthMenu": [[50, 100], [50, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/api/ajax-all-lines",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#linename').val(),
                         "filter[level]" : $('#levelStr').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "name", "defaultContent": "" },
                { "data": "null", "defaultContent": "单层"},
                { "data": "levelStr", "defaultContent": ""},
                  { "data": "office", "defaultContent": ""},
                 { "data": "company.name", "defaultContent": ""},
                    { "data": "_cars", "defaultContent": ""},
                   { "data": "null", "defaultContent": "0"},
                   { "data": "null", "defaultContent": "0"},
                   { "data": "null", "defaultContent": "0"},
                   { "data": "null", "defaultContent": "0"},
                   { "data": "null", "defaultContent": "0"},
                   { "data": "null", "defaultContent": "0"},
                   { "data": "null", "defaultContent": "0"},
            ],
            "language": {
                "url": "${rc.contextPath}/js/jquery.dataTables.lang.cn.json"
            },
            "initComplete": initComplete,
            "drawCallback": drawCallback,
             "fnDrawCallback": fnDrawCallback,
        } );
        table.fnNameOrdering("orderBy").fnNoColumnsParams();
    }
    
    function initComplete() {
        $("div#toolbar").html(
                '<div>' +
                        '   <span>线路名称：</span>' +
                        '    <span>' +
                        '        <input id="linename" value="" />' +
                        '    &nbsp;&nbsp;</span><span>线路级别:</span>&nbsp;&nbsp;' +
                        '<select class="ui-input ui-input-mini" name="levelStr" id="levelStr">' +
                   		'<option value="defaultAll" selected="selected">所有</option>' +
                  		'<option value="S">特级</option>' +
                  		'<option value="APP">A++</option>' +
                  		'<option value="AP">A+</option>' +
                  		'<option value="A">A</option>' +
         				'</select>' +
         				'   &nbsp;&nbsp;  <span>营销中心</span>&nbsp;&nbsp;' +
                    '<select class="ui-input ui-input-mini" name="company" id="company">' +
                    '<option value="defaultAll" selected="selected">所有</option>' +
                  	    <#list companys as c>
					'<option value="${c.id}">${c.name}</option>'+
					    </#list>
         			'</select>' +
         				'&nbsp;&nbsp;&nbsp;&nbsp;<span style="float:right;"><a class="block-btn" id="export_xls" href="javascript:void(0);">导出Excel</a></span>'+
                        '</div>'
        );

        $('#linename,#levelStr').change(function() {
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
	function fnDrawCallback(){
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
</script>


<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title">
               <span> 空媒体统计</span>
									</div>
                <table id="table" class="display compact" cellspacing="0" width="100%">
                    <thead>
                    <tr style="height: 40px;">
                        <th >线路名</th>
                        <th >单双层</th>
                        <th >线路级别</th>
                        <th >所属公司</th>
                          <th >营销中心</th>
                        <th >配车数</th>
                        <th >广告在刊</th>
                        <th >合同预订</th>
                        <th >到期未下刊车数</th>
                         <th >次月下刊数</th>
                        <th >次月可使用车数</th>
                        <th >媒介在刊率</th>
                        <th >车身媒体占有率</th>
                    </tr>
                    </thead>

                </table>
</div>


 
</@frame.html>