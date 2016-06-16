<#import "template/template.ftl" as frame>
<#global menu="月发布统计">
<#assign security=JspTaglibs["/WEB-INF/tlds/security.tld"] />
<@frame.html title="月发布统计" js=["js/jquery-dateFormat.min.js","js/jquery-ui/jquery-ui.js", "js/datepicker.js","js/jquery.datepicker.region.cn.js"]
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
<style type="text/css">
.tdd{
    background-color: #3bafda;
    color: white;
    font-weight: normal;
    text-align: center;
    line-height: 30px;
    }
th, td { white-space: nowrap;border: 1px solid #C5C5C5;}
</style>
<script type="text/javascript">
	
    var table;
    function initTable () {
        table = $('#table').dataTable( {
            "dom": '<"#toolbar">rt',
            "searching": false,
            "ordering": false,
            "serverSide": false,
            "scrollX": true,
            "iDisplayLength" : 20,
            "aLengthMenu": [[20, 40, 100], [20, 40, 100]],
                          "aaSorting": [[1, "desc"]],
              "columnDefs": [
                 { "orderable": false, "targets": [0,2,3,4,5,6,7,8,9,10] },
            ],
            "ajax": {
                type: "GET",
                url: "${rc.contextPath}/report/ajax-publishCountM",
                data: function(d) {
                    return $.extend( {}, d, {
                    "filter[day]" : $("#day").val()
                    } );
                },
                "dataSrc": function(json) {return json;},
            },
            "columns": [
                { "data": "companyName", "defaultContent": ""},
                { "data": "modelName", "defaultContent": ""},
                { "data": "contractNum", "defaultContent": ""},
                { "data": "nor_Snum", "defaultContent": ""},
                { "data": "nor_APPnum", "defaultContent": ""},
                { "data": "nor_APnum", "defaultContent": ""},
                { "data": "nor_Anum", "defaultContent": ""},
                { "data": "nor_xiaoji", "defaultContent": ""},
                { "data": "xu_Snum", "defaultContent": ""},
                { "data": "xu_APPnum", "defaultContent": ""},
                { "data": "xu_APnum", "defaultContent": ""},
                { "data": "xu_Anum", "defaultContent": ""},
                { "data": "xu_xiaoji", "defaultContent": ""},
                { "data": "one_num", "defaultContent": ""},
                { "data": "three_num", "defaultContent": ""},
                { "data": "six_num", "defaultContent": ""},
                { "data": "twelve_num", "defaultContent": ""},
                { "data": "other_num", "defaultContent": ""},
                { "data": "days_xiaoji", "defaultContent": ""},
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
               ''
        );

    }

    function drawCallback() {
        $('.table-action').click(function() {
            $.post($(this).attr("url"), function() {
                table.fnDraw(true);
            })
        });
    }
function serch(){
 if($("#day").val()==""){
   layer.msg("请选择年月");
 }
  table.dataTable()._fnAjaxUpdate();
}
    $(document).ready(function() {
        initTable();
    } );
</script>
<div class="withdraw-wrap color-white-bg fn-clear">
            <div class="withdraw-title">
                     <input
                            class="ui-input ui-input-mini monthpicker" type="text" name="day"
                            id="day" data-is="isAmount isEnough"
                            autocomplete="off" disableautocomplete=""> <input type="button" class="block-btn" onclick="serch();" value="搜索"/>
									</div>
                <table id="table" class="cell-border compact nowrap display"  cellspacing="0">
                    <thead>
                    <tr>
                        <td rowspan=2 class="tdd">营销中心</td>
                        <td rowspan=2 class="tdd">车型</td>
                        <td rowspan=2 class="tdd">合同数</td>
                        <td colspan='5' class="tdd" align="center">上刊发布车辆数</td>
                        <td colspan='5' class="tdd" align="center">续刊车数</td>
                        <td colspan='6' class="tdd" align="center">刊期分类（车数）</td>
                    </tr>
                    <tr>
                        <th >特级</th>
                        <th >A++</th>
                        <th >A+</th>
                        <th >A&nbsp;&nbsp;&nbsp;</th>
                        <th >合计</th>
                        <th >特级</th>
                        <th >A++</th>
                        <th >A+</th>
                        <th >A&nbsp;&nbsp;&nbsp;</th>
                        <th >合计</th>
                        <th >1个月</th>
                        <th >3个月</th>
                        <th >6个月</th>
                        <th >12个月</th>
                        <th >其他期刊</th>
                        <th >合计</th>
                    </tr>
                    </thead>
                </table>
</div>
</@frame.html>