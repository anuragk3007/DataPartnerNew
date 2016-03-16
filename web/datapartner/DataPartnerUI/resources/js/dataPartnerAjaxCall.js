$(document).ready(function () {
    //Stops the submit request
    $("#formSubmission").submit(function (e) {
        e.preventDefault();
    });

    //checks for the button click event
    $("#requestSubmit").click(function (e) {
        //get the form data and then serialize that
        dataString = $("#formSubmission").serialize();

        //get the form data using another method
        var dataPartner = $("input#dataPartner").val();
        dataString = "dataPartner=" + dataPartner;

        var requestIds = $("textarea#requestIdList").val();
        dataString = "requestIdList=" + requestIds;

        /*make the AJAX request, dataType is set to json
         meaning we are expecting JSON data in response from the server*/
        $.ajax({
            type: "POST",
            url: "RequestHandler",
            data: dataString,
            dataType: "json",

            //if received a response from the server
            success: function (data, textStatus, jqXHR) {

                var jsonData = JSON.parse(data);
                if (jsonData.success) {
                    var tableBody = createResultDivElements("resultDiv")
                    for(var i=0; i<jsonData.resultList.length; i++) {
                        var dataPartnerData = jsonData.resultList[i];
                        var requestId = document.createTextNode(dataPartnerData.getRequestid);
                        var remark = document.createTextNode("Request not found");
                        var behaviorList = document.createTextNode("");
                        if(dataPartnerData.isFound) {
                            if(dataPartnerData.isMatched) {
                                remark = document.createTextNode("Request Found and Matched");
                                behaviorList = document.createTextNode(dataPartnerData.getBehaviorList().toString()
                                    .replaceAll("[\\[\\]]*", "").replaceAll(", ", ";"));
                            } else {
                                remark = document.createTextNode("Request Found but not Matched");
                            }
                        }

                        var tr = document.createElement("tr");
                        var td = document.createElement("td").appendChild(requestId);
                        tr.appendChild(td);
                        td = document.createElement("td").appendChild(remark);
                        tr.appendChild(td);
                        td = document.createElement("td").appendChild(behaviorList);
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
    resultDiv.style.visibility = "visible";
    //creating margin div
    var marginDiv = document.createElement("div");
    marginDiv.class = "col-xs-12";
    marginDiv.style.height = "10px";

    //Summary Ddiv
    var summaryDiv = document.createElement("div");
    summaryDiv.class = "col-xs-12";
    summaryDiv.align = "center";
    var strong = document.createElament("Strong");
    var text = document.createTextNode("Summary");
    strong.appendChild(text);
    summaryDiv.appendChild(strong);

    // download div
    var downloadDiv = document.createElement("div");
    downloadDiv.class = "col-xs-4";
    downloadDiv.align = "center";
    downloadDiv.style.float = "right";
    var link = document.createElement("a");
    link.href = "data/requestFinderResult.csv";
    link.download = "requestFinderResult.csv";
    text = document.createTextNode("Download");
    link.appendChild(text);
    downloadDiv.appendChild(link);
    summaryDiv.appendChild(downloadDiv);

    // table to show result
    var table = document.createElement("table");
    table.class = "table table-bordered";
    var thead = document.createElement("thaead");
    var tr = document.createElement("tr");
    var th = document.createElement("th").appendChild(document.createTextNode("Request Id"));
    tr.appendChild(th);
    th = document.createElement("th").appendChild(document.createTextNode("Remarks"));
    tr.appendChild(th);
    th = document.createElement("th").appendChild(document.createTextNode("Behavior List"));
    tr.appendChild(th);
    thead.appendChild(tr);
    table.appendChild(thead);
    var tbody = document.createElement("tbody");
    tbody.id = "resultantRowValue";
    tbody.name = "resultantRowValue";
    table.appendChild(tbody);

    resultDiv.appendChild(marginDiv);
    resultDiv.appendChild(summaryDiv);
    resultDiv.appendChild(table);

    return tbody;
}