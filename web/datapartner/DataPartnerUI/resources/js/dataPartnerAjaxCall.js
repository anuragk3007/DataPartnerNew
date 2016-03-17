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
                //console.log("Before Parse: "+data.toString());
                //var jsonData = JSON.parse(data);
                //console.log("Response Data: "+jsonData.toString());

                if (data.success) {
                    console.log("inside success");
                    var tableBody = createResultDivElements("resultDiv");
                    console.log(data.resultList);
                    for(var i=0; i<data.resultList.myArrayList.length; i++) {
                        var dataPartnerData = data.resultList.myArrayList[i];
                        dataPartnerData = JSON.parse(dataPartnerData);
                        //console.log("Object "+i+" request Id: "+dataPartnerData.requestId);
                        //console.log("Object "+i+" status: "+dataPartnerData.status);
                        //console.log("Object "+i+" behavior list: "+dataPartnerData.behaviorList);
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
                    console.log(tableBody);
                    console.log("********************************");
                    console.log(document.getElementById("resultDiv"));
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
    marginDiv.className = "col-xs-12";
    marginDiv.style.height = "10px";

    //Summary Ddiv
    var summaryDiv = document.createElement("div");
    summaryDiv.class = "col-xs-12";
    summaryDiv.align = "center";
    var text = document.createTextNode("Summary");
    text.value = "bold";
    summaryDiv.appendChild(text);

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
    downloadDiv.className = "col-xs-12";
    downloadDiv.align = "center";
    tableHolder.appendChild(table);
    resultDiv.appendChild(marginDiv);
    resultDiv.appendChild(summaryDiv);
    resultDiv.appendChild(tableHolder);

    return tbody;
}