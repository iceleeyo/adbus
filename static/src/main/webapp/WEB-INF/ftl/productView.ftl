<#import "template/template.ftl" as frame> <#import "spring.ftl" as
spring /> <#import "template/proDetail.ftl" as proDetail> <#global
menu="产品定义"> <@frame.html title="套餐详情"> <@proDetail.proDetail prod=prod
buyLink=true/> </@frame.html>
