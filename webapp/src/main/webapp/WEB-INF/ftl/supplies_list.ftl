<#import "template/template.ftl" as frame>
<#global menu="物料列表">
<@frame.html title="物料列表">
    <script>
        function pages(pageNum) {
            var by = ($("#by").val());
            var name = ($("#name").val());
            var g2 = ($("#textpage").val());
            if (g2 == undefined) {
                g2 = 1;
            }
            if (!isNaN($("#textpage").val())) {
            } else {
                jDialog.Alert("请输入数字");
                return;
            }
            if (parseInt($("#textpage").val()) <= 0) {
                jDialog.Alert("请输入正整数");
                return;
            }
            if ($("#textpage").val() > pageNum) {
                jDialog.Alert("输入的页数超过最大页数");
                return;
            }
            window.location.href = "${rc.contextPath}/supplies/list/" + g2 + "?name="+ name;
        }

        function page(num) {
            var name = $("#name").val();
            var by = ($("#by").val());
            window.location.href = "${rc.contextPath}/supplies/list/" + num + "?name=" + name;
        }

        function sub(){
            var name = $("#name").val();
            window.location.href= "${rc.contextPath}/supplies/list/1"+"?name="+name
        }
    </script>

    <form id="base_form" action="/supplies/list/1" method="post"
    dataType="html" enctype="multipart/form-data" class="Page-Form">
        <div class="module s-clear u-lump-sum p19">
            <div class="u-sum-right">
                    <input class="ui-input" type="text" value="${name!''}" id="name" data-is="isAmount isEnough" autocomplete="off" disableautocomplete  placeholder="物料名称"/>
                    <input type="button" id="subWithdraw" class="block-btn" value="查询" onclick="sub();">
            </div>
        </div>
    </form>
    <form data-name="withdraw" name="userForm2" id="userForm2"
        class="ui-form" method="post" action="saveContract"
        enctype="multipart/form-data">
        <div class="mt20">
            <div class="u-tab">
                <ul class="u-tab-items s-clear">
                    <li class="u-tab-item u-tab-item-active"><a
                        class="u-item-a" href="#holding">展示中 <em class="baget">0</em>
                    </a></li>
                    <li class="u-tab-item"><a class="u-item-a"
                        href="#booking">预订中 <em class="baget">0</em>
                    </a></li>
                    <li class="u-tab-item"><a class="u-item-a"
                        href="#exiting">已结束 <em class="baget">0</em>
                    </a></li>

                </ul>
            </div>
            <div class="module p20" style="height: 423px;">
                <div class="tab-content">
                    <div class="tab-content-box s-clear" id="holding"
                        style="display: block;">
                        <div class="tab-plans-type">
                            <ul class="tab-plans">
                                <li class="tab-plan-item tab-plan-width"><span>全部</span>
                                </li>
                                <li class="tab-plan-item tab-plan-width"><span>已审核</span>
                                </li>
                                <li class="tab-plan-item tab-plan-width"><span>已提交</span>
                                </li>
                                <li class="tab-plan-item tab-plan-width"><span>已展示</span>
                                </li>

                            </ul>
                        </div>
                        <div class="uplan-table-box">
                            <table width="100%" class="uplan-table">
                                <tbody>
                                    <tr class="uplan-table-th">
                                        <td width="22%">
                                            <div class="th-head">物料名称</div>
                                        </td>
                                        <td width="15%">
                                            <div class="th-md">物料类型</div>
                                        </td>
                                        <td width="16%">
                                            <div class="th-tail">创建时间</div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            <#list list as item>
                            <li class="ui-list-item dark">
                                <div class="ui-list-item-row fn-clear">
                                    <span style="width: 306px; height: 35px; " class="ui-list-field text-center w80 fn-left" >
                                        <a href="../suppliesDetail?supplies_id=${item.id!''}">
                                        ${item.name}
                                        </a>
                                    </span>

                                     <span style="width: 208px; height: 35px; "
                                        class="ui-list-field text-center  fn-left">
                                        ${item.suppliesType}

                                     </span>
                                     <span
                                        style="width: 224px; height: 35px; "
                                        class="ui-list-field text-center w80 fn-left">
                                    <#setting
                                                date_format="yyyy-MM-dd HH:mm"> ${(item.created?date)!''}
                                     </span>
                                    </div>
                            </li>
                            </#list>
                            <!-- 分页 -->
                            <table class="pag_tbl"
                                style="width: 100%; border-width: 0px; margin-top: 10px;">
                                <tr>
                                    <td style="width: 70%; text-align: right;">
                                        <div id="numpage" style="float: right;">
                                        ${paginationHTML!''}
                                        </div>
                                    </td>
                                </tr>
                            </table>
                    </div>
                </div>
            </div>
        </div>
    </form>

</@frame.html>