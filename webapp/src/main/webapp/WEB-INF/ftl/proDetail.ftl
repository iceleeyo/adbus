<#import "template/template.ftl" as frame> <#import
"template/proDetail.ftl" as proDetail> <@frame.html title="套餐详细">

<script type="text/javascript">
function go_back(){
	history.go(-1);
}
</script>
<@proDetail.proDetail prod buyLink=true> </@proDetail.proDetail>
</@frame.html>
