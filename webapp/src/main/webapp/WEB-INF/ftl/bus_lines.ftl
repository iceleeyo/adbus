<#import "template/template.ftl" as frame>
<#global menu="线路列表">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="线路列表" js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js",
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
    <@security.authorize ifAnyGranted="BodyOrderManager">
                { "orderable": false, "targets": [2] },
    </@security.authorize>
            ],
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/bus/ajax-all-lines",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[name]" : $('#name').val(),
                        "filter[level]" : $('#levelStr').val()
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "name", "defaultContent": "",
                    "render": function(data, type, row, meta) {
                    return '<a  target="_Blank" href="${rc.contextPath}/busselect/lineschedule/' + row.id + '" >'+data+'</a> &nbsp;';
                } },
                { "data": "levelStr", "defaultContent": ""},
                <@security.authorize ifAnyGranted="BodyOrderManager">
                { "data": function( row, type, set, meta) {
                    return row.id;
                    },
                    "render": function(data, type, row, meta) {
                        var operations = '';

                            operations+= (row.enabled ? '<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/bus/line/' + data + '/disable">禁用</a> &nbsp;'
                                    :'<a class="table-action" href="javascript:void(0);" url="${rc.contextPath}/bus/line/' + data + '/enable">启用</a> &nbsp;')
                            operations +='<a class="table-link" href="${rc.contextPath}/bus/line/' + data +'">编辑</a>&nbsp;';

                        return operations;

                    }
                },
                </@security.authorize>
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
                        '    <span>线路名</span>' +
                        '    <span>' +
                        '        <input id="name" value="">' +
                        '    <span>线路级别</span>&nbsp;&nbsp;' +
                        '<select class="ui-input ui-input-mini" name="levelStr" id="levelStr">' +
                   		'<option value="defaultAll" selected="selected">所有</option>' +
                  		'<option value="S">特级</option>' +
                  		'<option value="APP">A++</option>' +
                  		'<option value="AP">A+</option>' +
                  		'<option value="A">A</option>' +
                  		'<option value="LATLONG">经纬线</option>' +
         				'</select>' +
                        ' <a class="block-btn" target="_Blank" style="margin-left:30px;" href="${rc.contextPath}/busselect/applyBodyCtct">申请合同</a>'+
                        '</div>'
        );

        $('#name,#levelStr').change(function() {
            table.fnDraw();
        });
        
        $("#name").autocomplete({
			minLength: 0,
			source : "${rc.contextPath}/busselect/autoComplete",
			change : function(event, ui) {
			},
			select : function(event, ui) {
				$('#line_id').val(ui.item.value);
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
                线路列表
									</div>
                <table id="table" class="display compact" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th orderBy="name">线路名</th>
                        <th orderBy="level">线路级别</th>
    <@security.authorize ifAnyGranted="BodyOrderManager">
                        <th>管理</th>
    </@security.authorize>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>