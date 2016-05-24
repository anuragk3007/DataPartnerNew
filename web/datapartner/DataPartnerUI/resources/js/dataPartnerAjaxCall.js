$(document).ready(function () {
    //Stops the submit request
    $("#formSubmission").submit(function (e) {
        e.preventDefault();
    });

    //checks for the button click event
    $("#requestSubmit").click(function (e) {
        checkForEmptyRequestList();
        preProcessResultDiv();
        //get the form data and then serialize that
        dataString = $("#formSubmission").serialize();
        dataString = "requestType=query&" + dataString;

        /*make the AJAX request, dataType is set to json
         meaning we are expecting JSON data in response from the server*/
        $.ajax({
            type: "POST",
            url: "RequestHandler",
            data: dataString,
            dataType: "json",

            //if received a response from the server
            success: function (data) {

                if (data.success) {
                    document.getElementById("downloadLink").href = data.resultFile;
                    var dataList = data.resultList.myArrayList;
                    var tableBody = document.getElementById("resultTableBody");
                    for (var i = 0; i < dataList.length; i++) {
                        var dataPartnerData = JSON.parse(dataList[i]);
                        var date = dataPartnerData.logDate;
                        var dataPartnerName = dataPartnerData.dataPartnerName;
                        var requestId = dataPartnerData.requestId;
                        var remarks = dataPartnerData.remarks;
                        var behaviorList = dataPartnerData.behaviorList;
                        var noOfBeh = behaviorList.length;
                        if (noOfBeh != 0) {
                            for (var j = 0; j < noOfBeh; j++) {
                                var behaviorId = behaviorList[j].behaviorId;
                                var topicPath = behaviorList[j].topicPath;
                                var ucUS = behaviorList[j].userCount.usCount;
                                var ucUK = behaviorList[j].userCount.ukCount;
                                var td;
                                var text;
                                $(document).ready(function () {
                                    var tr = document.createElement("tr");
                                    tr.appendChild(getTableColumnForData(date));
                                    tr.appendChild(getTableColumnForData(dataPartnerName));
                                    tr.appendChild(getTableColumnForData(requestId));
                                    tr.appendChild(getTableColumnForData(remarks));
                                    tr.appendChild(getTableColumnForData(noOfBeh));
                                    var td = getTableColumnForData(topicPath);
                                    td.style.whiteSpace = "normal";
                                    td.align = "justify";
                                    tr.appendChild(td);
                                    tr.appendChild(getTableColumnForData(ucUS));
                                    tr.appendChild(getTableColumnForData(ucUK));
                                    tableBody.appendChild(tr);
                                })
                            }
                        } else {
                            var tr = document.createElement("tr");
                            tr.appendChild(getTableColumnForData(date));
                            tr.appendChild(getTableColumnForData(dataPartnerName));
                            tr.appendChild(getTableColumnForData(requestId));
                            tr.appendChild(getTableColumnForData(remarks));
                            tr.appendChild(getTableColumnForData(noOfBeh));
                            tr.appendChild(getTableColumnForData(""));
                            tr.appendChild(getTableColumnForData(""));
                            tr.appendChild(getTableColumnForData(""));
                            tableBody.appendChild(tr);
                        }
                    }
                    document.getElementById("resultDiv").style.visibility = "visible";
                    $(body).style.height = "inherit"
                }
            }
        });
    });
});

function preProcessResultDiv() {
    document.getElementById("resultDiv").style.visibility = "hidden";
    var tableBody = document.getElementById("resultTableBody");
    while (tableBody.firstChild) {
        tableBody.removeChild(tableBody.firstChild);
    }
}
function getTableColumnForData(text) {
    var td = document.createElement("td");
    td.style.whiteSpace="nowrap";
    td.align = "center";
    text = document.createTextNode(text);
    td.appendChild(text);
    return td;
}

function checkForEmptyRequestList() {
    var text = $("#requestIdList").val();
    if (text == "") {
        $("#formSubmission").submit(function (e) {
            e.preventDefault();
        });
    }
    text = $("#dataPartner").val();
    if (text == "default") {
        alert("Select a DataPartner");
        $("#formSubmission").submit(function (e) {
            e.preventDefault();
        });
    }
}