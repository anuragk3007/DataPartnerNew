function getDataPartnerList(file) {
    var table = [];
    var rawFile = new XMLHttpRequest();
    rawFile.open("GET", file, false);
    rawFile.onreadystatechange = function () {
        if(rawFile.readyState === 4) {
            if(rawFile.status === 200 || rawFile.status == 0) {
                var allText = rawFile.responseText;
                table = allText.split('\n');
            }
        }
    }
    rawFile.send(null);
    return table;
};

$(document).ready(
    function(){
    var table = getDataPartnerList("../data/requestFinderResult.csv");
        var list = document.getElementById("resultantRowValue");
        for (var i = 1; i < table.length; i++) {
            var row = table[i];
            var tr = document.createElement("tr");
            var col = row.trim().split(",");
            for(var j=0; j<col.length; j++) {
                var text = document.createTextNode(col[j]);
                var td = document.createElement("td");
                td.appendChild(text);
                tr.appendChild(td);
            }
            list.appendChild(tr);
        }
    });

