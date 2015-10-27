 

	/**
	 * 
	 * 设置序号
	 * @param tableObj table对象
	 * @param cellId 第几列显示序号
	 */

    function counter_columns(tableObj,cellId){
    	   table.column(cellId, {}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
	       } );
    }