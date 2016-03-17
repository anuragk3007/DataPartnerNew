$(document).ready(function () {
    $("#downloadForm").submit(function (e) {
     e.preventDefault();
     });

    $("#downloadLink").click(function (e) {

        console.log("inside click download function");
        dataString = $("#downloadLink").serialize();
        dataString = "requestType=download";
        $.ajax({
            type: "POST",
            url: "RequestHandler",
            data: dataString,
            dataType: "text/csv",
            success: function (data, textStatus, jqXHR) {
                if (!data.success) {
                    console.log("Ajax call made and response success");
                }
            }
        });
    });
});