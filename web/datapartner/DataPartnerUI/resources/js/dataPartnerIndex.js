$(document).ready(
    function () {
        dataString = "requestType=reportDataQuery";
        $.ajax({
            type: "POST",
            url: "RequestHandler",
            data: dataString,
            dataType: "json",
            async: false,

            success: function (data) {
                var text = document.createTextNode("Date not fetched");
                if (data.success) {
                    populateDataPartnerList(data.dataPartnerNameList.myArrayList);
                    populateDate(data.dateList.myArrayList);
                }
            }
        });
    }
);

function populateDate(dateList) {
    var startDateDropdown = document.getElementById("startDateDropdown");
    var endDateDropdown = document.getElementById("endDateDropdown");

    for (var index = 0; index < dateList.length; index++) {
        var opt = dateList[index];
        startDateDropdown.appendChild(getLinkElementForOpt(opt));
        endDateDropdown.appendChild(getLinkElementForOpt(opt));
        if (index == 0) {
            $("#startDate").val(opt)
        }
        if (index == dateList.length - 1) {
            $("#endDate").val(opt);
        }
    }
}

function populateDataPartnerList(dataPartnerList) {
    var dataPartnerDropDown = document.getElementById("dataPartnerSelectorDropdown");
    for (var index = 0; index < dataPartnerList.length; index++) {
        var opt = dataPartnerList[index];
        dataPartnerDropDown.appendChild(getLinkElementForOpt(opt));
    }
}
function getLinkElementForOpt(opt) {
    var li = document.createElement("li");
    var link = document.createElement("a");
    var text = document.createTextNode(opt);
    link.appendChild(text);
    link.href = "#";
    link.id = opt;
    link.name = opt;
    link.value = opt;
    li.appendChild(link);
    return li;
}

$(function () {
    $("#dataPartnerSelectorDropdown").find("li a").click(function () {
        $("#dataPartnerListButton").text($(this).text());
        $("#dataPartner").val($(this).text());
    });
});

$(function () {
    $("#startDateDropdown").find("li a").click(function () {
        $("#startDateButton").text($(this).text());
        $("#startDate").val($(this).text());
        var fine = isEndDateFine();
        if (!fine) {
            alert("Start Date should be same or before end date");
            $("#startDateButton").text($("#endDate").val());
            $("#startDate").val($("#endDate").val());
        }
    });
});

$(function () {
    $("#endDateDropdown").find("li a").click(function () {
        $("#endDateButton").text($(this).text());
        $("#endDate").val($(this).text());
        var fine = isEndDateFine();
        if (!fine) {
            alert("End Date should be same or after start date");
            $("#endDateButton").text($("#startDate").val());
            $("#endDate").val($("#startDate").val());
        }
    });
});

function isEndDateFine() {
    var startDateString = document.getElementById("startDate").value.split("-");
    var endDateString = document.getElementById("endDate").value.split("-");
    var startDate = new Date(startDateString[2], getMonthFromString(startDateString[0]), startDateString[1]);
    var endDate = new Date(endDateString[2], getMonthFromString(endDateString[0]), endDateString[1]);
    return startDate <= endDate;
}

function getMonthFromString(monthString) {
    var monthList = "Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec".split(",");
    var month = 0;
    for (; month < 12; month++) {
        if (monthList[month] === monthString) {
            break;
        }
    }
    return month;
}