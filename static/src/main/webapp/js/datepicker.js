$(function() {
    var beforeDatePickerShow = function(input, inst) {
        if ($(input).hasClass("datepicker")) {
            $('#ui-datepicker-div').removeClass('only-month');
        } else if ($(input).hasClass("monthpicker")){
            $('#ui-datepicker-div').addClass('only-month');
        }
    };

    $(".datepicker").datepicker({
        "dateFormat" : ($(this).attr("date-format") || "yy-mm-dd"),
        changeMonth: true,
        changeYear: true,
        beforeShow:beforeDatePickerShow,
    });
    $(".monthpicker").datepicker({
        "dateFormat" : ($(this).attr("date-format") || "yy-mm"),
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true,
        beforeShow: beforeDatePickerShow,
        onClose: function(dateText, inst) {
            var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
            var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
            $(this).datepicker('setDate', new Date(year, month, 1));
        }
    });
});
