/**
 * Remove 'columns' params when sending Ajax request for order object.
 * 
 * **Note** - this plug-in currently only operates correctly with 
 * **server-side processing**.
 *
 *  @name fnNoColumnsParams
 *  @summary Remove 'columns' params when sending Ajax request for order object.
 *  @author Tony Liu
 *
 *  @example
 *    $('#example').dataTable().fnNoColumnsParams();
 */

jQuery.fn.dataTableExt.oApi.fnNoColumnsParams = function ( oSettings) {

	function removeColumnsParams (data) {
        data.columns = undefined;
	}

	oSettings.aoServerParams.push({ "fn": removeColumnsParams, "sName": "fnNoColumnsParams" });

	return this;
};
