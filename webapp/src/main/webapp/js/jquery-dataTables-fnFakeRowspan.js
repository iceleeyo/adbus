/**
 * Creates `rowspan` cells for columns based on a column when there are two
 * or more cells in a row with the same content, effectively grouping them
 * together visually.
 * 
 * **Note** - this plug-in currently only operates correctly with 
 * **server-side processing**.
 *
 * **Note** - this plug-in is modified to work differently than the original
 * version, please read the above description carefully. Also cell comparison
 * is modified to be based on cell text content intead of backend data from
 * server. --Tony Liu
 *
 *  @name fnFakeRowspan
 *  @summary Create a rowspan for cells which share data
 *  @author Fredrik Wendel, Tony Liu
 *
 *  @param {interger} iColumn Column index to have row span
 *  @param {boolean} [bCaseSensitive=true] If the data check should be case
 *    sensitive or not.
 *  @returns {jQuery} jQuery instance
 *
 *  @example
 *    $('#example').dataTable().fnFakeRowspan(3);
 */

jQuery.fn.dataTableExt.oApi.fnFakeRowspan = function ( oSettings, iColumn, iColumns, bCaseSensitive ) {
	/* Fail silently on missing/errorenous parameter data. */
	if (isNaN(iColumn) || !iColumns.length) {
		return false;
	}

	if (iColumn < 0 || iColumn > oSettings.aoColumns.length-1) {
		alert ('Invalid column number choosen as base, must be between 0 and ' + (oSettings.aoColumns.length-1));
		return false;
	}

    for (i in iColumns) {
        if (iColumns[i] < 0 || iColumns[i] > oSettings.aoColumns.length-1) {
            alert ('Invalid column number choosen as rowspan column, must be between 0 and ' + (oSettings.aoColumns.length-1));
            return false;
        }
    }

	bCaseSensitive = (typeof(bCaseSensitive) != 'boolean' ? true : bCaseSensitive);

	function fakeRowspan () {
		var firstOccurance = null,
			value = null,
			rowspan = 0;
		jQuery.each(oSettings.aoData, function (i, oData) {
            //tliu
/*			var val = oData._aData[iColumn],
				cell = oData.nTr.childNodes[iColumn];*/
            var cell = oData.nTr.childNodes[iColumn],
                val = cell.textContent;
			/* Use lowercase comparison if not case-sensitive. */
			if (!bCaseSensitive) {
				val = val.toLowerCase();
			}
			/* Reset values on new cell data. */
			if (val != value) {
				value = val;
				firstOccurance = oData.nTr;
				rowspan = 0;
			}

			if (val == value) {
				rowspan++;
			}

			if (firstOccurance !== null && val == value && rowspan > 1) {
                var cellsToRemove = [];
                for (i in iColumns) {
                    cellsToRemove.push(oData.nTr.childNodes[iColumns[i]]);
                }
                for (i in cellsToRemove) {
                    oData.nTr.removeChild(cellsToRemove[i]);
                    firstOccurance.childNodes[iColumns[i]].rowSpan = rowspan;
                }
			}
		});
	}

	oSettings.aoDrawCallback.push({ "fn": fakeRowspan, "sName": "fnFakeRowspan" });

	return this;
};
