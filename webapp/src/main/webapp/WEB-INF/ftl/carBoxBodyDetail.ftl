<#import "template/template_blank.ftl" as frame>
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="订单详情" js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js", "js/datepicker.js","js/jquery.datepicker.region.cn.js"]
css=["js/jquery-ui/jquery-ui.css"]>

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
               "aaSorting": [[5, "desc"]],
            "columnDefs": [
                { "orderable": false, "targets": [0,1,2,3] },
            ],
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/carbox/ajax-queryCarBoxBody",
                data: function(d) {
                    return $.extend( {}, d, {
                        "filter[helpid]" : ${helpid}
                    } );
                },
                "dataSrc": "content",
            },
            "columns": [
                { "data": "product.jpaProductV2.name", "defaultContent": ""},
                { "data": "product.leval", "defaultContent": ""},
                { "data": " product.doubleDecker", "defaultContent": "","render": function(data, type, row, meta) {
                	if(data==true){
                	 return '双层';
                	}else{
                	   return '单层';
                	}
                }},
                 { "data": "needCount", "defaultContent": ""},
                 { "data": "days", "defaultContent": ""},
                 { "data": "totalprice", "defaultContent": ""},
              
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
        $("div#toolbar").html('');
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
                <table id="table" class="display nowrap" cellspacing="0">
                    <thead>
                    <tr>
                        <th >线路</th>
                        <th>级别</th>
                        <th>单双层</th>
                        <th>车辆数</th>
                        <th>刊期(天)</th>
                        <th>价格</th>
                    </tr>
                    </thead>

                </table>
</div>
</@frame.html>