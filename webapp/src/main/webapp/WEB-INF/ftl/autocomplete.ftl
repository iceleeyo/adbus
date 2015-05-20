<#import "template/template.ftl" as frame>
<#global menu="进行中订单">
<@frame.html title="进行中的订单"  css=["js/jquery-ui/jquery-ui.auto.complete.css","css/autocomplete.css"] js=["js/jquery-ui/jquery-ui.auto.complete.js","js/jquery-dateFormat.js"]>
<label for="autocomplete">Select a programming language: </label>
<input id="autocomplete">
<script>
$( "#autocomplete" ).autocomplete({
  source: "/webapp/dts/autoComplete"
});
</script>
</@frame.html>