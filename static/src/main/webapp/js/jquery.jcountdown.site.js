/**
 * 
 * 倒计时api
 * 
 * 
 * @param element_id 页面元素id
 * @param dateTo 到期时间 
 * @param template 日期格式化格式 
 * @param event 额外的事件如onchange, onComplete
 */
function countdown(element_id,dateTo,template,event){
	var params={
			omitZero:true,
	        yearText: '年',
	        monthText: '月',
	        weekText: '周',
	        dayText: '天',
	        hourText: '时',
	        minText: '分',
	        secText: '秒',
	        yearSingularText: '年',
	        monthSingularText: '月',
	        weekSingularText: '周',
	        daySingularText: '天',
	        hourSingularText: '时',
	        minSingularText: '分',
	        secSingularText: '秒',
	        date: dateTo,
	        omitZero: false,
	        template: template,
	        onComplete:event,
	 };
	//$.extend(params, event); 
	$("#"+element_id).countdown(params);
}
function countDownReload (element_id,dateTo){
	var default_templete='%d 天%h 时%i 分%s 秒 ';
	return countdown(element_id,dateTo,default_templete,function (arg) {location.reload();} );
}
function countDate (element_id,dateTo){
	var default_templete='%d天  <input type="text" id="time_h" value="%h">时<input type="text" id="time_m" value="%i">分<input type="text" id="time_s" value="%s">秒 ';
	return countdown(element_id,dateTo,default_templete,function (arg) {location.reload();} );
}

function countDateSimple (element_id,dateTo){
	var default_templete='%d 天%h 时%i 分%s 秒 ';
	return countdown(element_id,dateTo,default_templete,null);
}

