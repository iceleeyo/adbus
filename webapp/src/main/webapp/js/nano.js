/* Nano Templates - https://github.com/trix/nano */

function nano(template, data) {
  return template.replace(/\{([\w\.]*)\}/g, function(str, key) {
    var keys = key.split("."), v = data[keys.shift()];
    for (var i = 0, l = keys.length; i < l; i++) v = v[keys[i]];
    return (typeof v !== "undefined" && v !== null) ? v : "";
  });
}




/**
 * 第一个参数是html串
 * 第二个参数是服务器返回的json对象
 * 第三个参数是补助的json对象 之所以有这个对象是服务器端返回的json可能需要进行一次数据转换 比如video翻译成 视频
 * 当一个要解析的{}在第二个json找不着时 尝试第三个json里找
 * @param template
 * @param data
 * @param exactdata
 * @returns
 */
function nano2(template, data, exactdata) {
	  return template.replace(/\{([\w\.]*)\}/g, function(str, key) {
		var keys = key.split(".");
		var _shift=keys.shift();
		var  v = data[_shift],e = exactdata[_shift];
	    for (var i = 0, l = keys.length; i < l; i++){
	    	 v = v[keys[i]];
	    } 
		if (typeof v !== "undefined" && v !== null) 
 	    	return v;
		else {
			for (var i = 0, l = keys.length; i < l; i++)e = e[keys[i]];
			return (typeof e !== "undefined" && e !== null) ? e : "";
	  	}
	  });
	}
