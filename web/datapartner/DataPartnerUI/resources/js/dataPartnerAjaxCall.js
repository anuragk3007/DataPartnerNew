$(document).ready(function () {
    //Stops the submit request
    $("#formSubmission").submit(function (e) {
        e.preventDefault();
    });

    //checks for the button click event
    $("#requestSubmit").click(function (e) {
        //get the form data and then serialize that
        dataString = $("#formSubmission").serialize();

        dataString = "requestType=query&";
        //get the form data using another method
        var dataPartner = $("input#dataPartner").val();
        dataString = dataString + "dataPartner=" + dataPartner;

        var requestIds = $("textarea#requestIdList").val();
        dataString = dataString + "&requestIdList=" + requestIds;

        /*make the AJAX request, dataType is set to json
         meaning we are expecting JSON data in response from the server*/
        $.ajax({
            type: "POST",
            url: "RequestHandler",
            data: dataString,
            dataType: "json",

            //if received a response from the server
            success: function (data, textStatus, jqXHR) {

                if (data.success) {
                    var tableBody = createResultDivElements("resultDiv");
                    var dataList = data.resultList.myArrayList;
                    for (var i = 0; i < dataList.length; i++) {
                        var dataPartnerData = JSON.parse(dataList[i]);
                        var requestId = document.createTextNode(dataPartnerData.requestId);
                        var remark = document.createTextNode(dataPartnerData.status);
                        var behaviorList = document.createTextNode(dataPartnerData.behaviorList);

                        var tr = document.createElement("tr");
                        var td = document.createElement("td");
                        td.appendChild(requestId);
                        tr.appendChild(td);
                        td = document.createElement("td");
                        td.appendChild(remark);
                        tr.appendChild(td);
                        td = document.createElement("td");
                        td.appendChild(behaviorList);
                        tr.appendChild(td);
                        tableBody.appendChild(tr);
                    }
                } else {
                    $("#resultDiv").html("<div class='col-xs-12' align='center'>" +
                        "<Strong>Data not available</Strong></div>");
                }
            }
        });
    });
});

function createResultDivElements(resultDivId) {
    var resultDiv = document.getElementById(resultDivId);
    //Emptying the div
    while (resultDiv.firstChild) {
        resultDiv.removeChild(resultDiv.firstChild);
    }
    resultDiv.style.visibility = "visible";
    //creating margin div
    var marginDiv = document.createElement("div");
    marginDiv.className = "col-xs-12";
    marginDiv.style.height = "10px";

    //Summary Ddiv
    var summaryDiv = document.createElement("div");
    summaryDiv.className = "col-xs-7";
    summaryDiv.align = "right";
    var text = document.createTextNode("Summary");
    text.value = "bold";
    summaryDiv.appendChild(text);

    // download div
    var downloadDiv = document.createElement("div");
    downloadDiv.className = "col-xs-5";
    downloadDiv.align = "right";
    downloadDiv.style.float = "right";
    var downloadForm = document.createElement("form");
    downloadForm.id = "downloadForm";
    downloadForm.name = "downloadForm";
    downloadForm.method = "post";
    // download link
    /*var link = document.createElement("a");
    link.href = "dataPartnerUI/data/RequestFinderResult.csv";
    link.id = "downloadLink";
    link.name ="downloadLink";
    link.onclick="return downloadSummary();"
    link.download = "requestFinderResult.csv";
    text = document.createTextNode("Download");
    link.appendChild(text);
    downloadDiv.appendChild(link);*/

    // download button
    var button = document.createElement("input");
    button.type = "submit";
    button.className = "btn btn-info btn-xs";
    button.id = "downloadLink";
    button.name = "downloadLink";
    button.value = "Download";
    var text = document.createTextNode("Download");
    button.appendChild(text);
    downloadForm.appendChild(button)
    downloadDiv.appendChild(downloadForm);
    //summaryDiv.appendChild(downloadDiv);

    // table to show result
    var table = document.createElement("table");
    table.className = "table table-bordered";
    var thead = document.createElement("thead");
    var tr = document.createElement("tr");
    var th = document.createElement("th");
    th.appendChild(document.createTextNode("Request Id"));
    tr.appendChild(th);
    th = document.createElement("th");
    th.appendChild(document.createTextNode("Remarks"));
    tr.appendChild(th);
    th = document.createElement("th");
    th.appendChild(document.createTextNode("Behavior List"));
    tr.appendChild(th);
    thead.appendChild(tr);
    table.appendChild(thead);
    var tbody = document.createElement("tbody");
    tbody.id = "resultantRowValue";
    tbody.name = "resultantRowValue";
    table.appendChild(tbody);

    var tableHolder = document.createElement("div");
    tableHolder.className = "col-xs-12";
    tableHolder.align = "center";
    tableHolder.appendChild(table);
    resultDiv.appendChild(marginDiv);
    resultDiv.appendChild(summaryDiv);
    resultDiv.appendChild(downloadDiv);
    resultDiv.appendChild(marginDiv);
    resultDiv.appendChild(tableHolder);

    return tbody;
}

