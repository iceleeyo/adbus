$(document).ready(function(){
	
    jQuery.fn.anim_progressbar = function (aOptions) {
        // def values
        var iCms = 1000;
        var iMms = 60 * iCms;
        var iHms = 3600 * iCms;
        var iDms = 24 * 3600 * iCms;

        // def options
        var aDefOpts = {
            start: new Date(), // now
            finish: new Date().setTime(new Date().getTime() + 60 * iCms), // now + 60 sec
            interval: 400,
            upath:"/upload/process"
        }
        var aOpts = jQuery.extend(aDefOpts, aOptions);
        var vPb = this;

        // each progress bar
        return this.each(
            function() {
                var iDuration = aOpts.finish - aOpts.start;
                // calling original progressbar
                $(vPb).children('.pbar').progressbar();

                // looping process
                var vInterval = setInterval(
                    function(){
                    	var iPerc = 0;
                    	var showString = "";
                    	 $.ajax({  
                             type: 'GET',  
                             url: aOpts.upath,  
                             data: {},  
                             dataType: 'json',  
                             async: false,//这里要设置为同步 不然就显示不了了
                             success : function(data){
                            	 //alert(data.rate+ " "+date.show+" "+data.totalSize);
                            	 iPerc = data.rate;
                            	 showString = data.show;
                             }});
                    	 
                    	   var iLeftMs = aOpts.finish - new Date(); // left time in MS
                           var iElapsedMs = new Date() - aOpts.start, // elapsed time in MS
                               iDays = parseInt(iLeftMs / iDms), // elapsed days
                               iHours = parseInt((iLeftMs - (iDays * iDms)) / iHms), // elapsed hours
                               iMin = parseInt((iLeftMs - (iDays * iDms) - (iHours * iHms)) / iMms), // elapsed minutes
                               iSec = parseInt((iLeftMs - (iDays * iDms) - (iMin * iMms) - (iHours * iHms)) / iCms); // elapsed seconds
                              // iPerc = (iElapsedMs > 0) ? iElapsedMs / iDuration * 100 : 0; // percentages
                           $(vPb).children('.percent').html('<b>'+(iPerc)+'%</b>');
                        // display current positions and progress
                        // $(vPb).children('.percent').html('<b>'+iPerc.toFixed(1)+'%</b>');
                        $(vPb).children('.elapsed').html(showString+'</b>');
                        $(vPb).children('.pbar').children('.ui-progressbar-value').css('width', (iPerc)+'%');

                        // in case of Finish
                        if (iPerc >= 100) {
                            clearInterval(vInterval);
                            $(vPb).children('.percent').html('<b>100%</b>');
                            $(vPb).children('.elapsed').html('上传完成');
                        }
                    } ,aOpts.interval
                );
            }
        );
    }

    // default mode
    // 小潘
    //$('#progress1').anim_progressbar();
});