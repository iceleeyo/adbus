/**
 * Send column 'name' along with column index in Ajax request for order object, and also
 * flatten the order object to map, for example, order = [{"name":"asc"},...]
 * 
 * **Note** - this plug-in currently only operates correctly with 
 * **server-side processing**.
 *
 *  @name fnNameOrdering
 *  @summary Send column 'name' along with column index in Ajax request for order object
 *  @author Tony Liu
 *
 *  @param {nameSelector} class for column name in thead.th, default to "orderBy"
 *
 *  @example
 *    $('#example').dataTable().fnNameOrdering();
 */

jQuery.fn.dataTableExt.oApi.fnNameOrdering = function ( oSettings, nameSelector ) {

    nameSelector = nameSelector || "orderBy";

	function nameOrdering (data) {
        data.columns = null;
        var header = oSettings.aoHeader[0];

        var orders = [];
        for (i in data.order) {
            var orderByName = $(header[data.order[i].column].cell).attr("orderBy");

            if (orderByName) {
                var dir = data.order[i]["dir"];
                var order = {};
                order[orderByName] = dir;
                orders.push(order);
            }
        }
        data.order = orders;
    }

	oSettings.aoServerParams.push({ "fn": nameOrdering, "sName": "fnNameOrdering" });

	return this;
};
