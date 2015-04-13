$(function() {
    $(".datepicker").datepicker({
        "dateFormat" : ($(this).attr("date-format") || "yy-mm-dd"),
        changeMonth: true,
        changeYear: true
    });
});
