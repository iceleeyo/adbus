<div class="jack" style="height: 296px; top: 160.5px;">
        <ul class="icons">
        	<li class="up"><i></i></li>
            <li class="qq">
            	<i></i>
            </li>
            <li class="tel">
            	<i></i>
            </li>
            <li class="wechat">
            	<i></i>
            </li>
            <li class="down"><i></i></li>
        </ul>
        <a class="switch"></a>
</div>

<script type="text/javascript">
var iIndex = 0;	
	    $('.jack1 .icons .up').on('click', function(e) {
	        e.preventDefault();
	        if(iIndex > 0){
	            iIndex--;
	        } else if(iIndex == 0){
	            iIndex = 0;
	        }else if(opts.loop){
	            iIndex = arrElement.length-1;
	        }
	        $('.menu ul.list-line li').eq(iIndex).addClass('active').siblings('.active').removeClass('active');
	        scrollPage(arrElement[iIndex]);
	    });
	    
    
</script>
<style type="text/css">
   
</style>