
/**
 *  author:pxh
 * @param element_id 元素id
 * @param templete 格式化样式 
 */
function countdown(element_id,dateTo,template){
	$("#"+element_id).countdown({
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
        template: template
        
		 });
}

function countDate (element_id,dateTo){
	var default_templete='%d天  <input type="text" id="time_h" value="%h">时<input type="text" id="time_m" value="%i">分<input type="text" id="time_s" value="%s">秒 ';
	return countdown(element_id,dateTo,default_templete);
}