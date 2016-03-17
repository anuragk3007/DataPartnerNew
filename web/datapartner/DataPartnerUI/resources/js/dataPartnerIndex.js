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
        var table = getDataPartnerList("dataPartnerUI/data/dataPartnerList.txt");
        var list = document.getElementById("dataPartnerSelectorDropdown");
        for (var i = 0; i < table.length; i++) {
            var opt = table[i];
            var li = document.createElement("li");
            var link = document.createElement("a");
            var text = document.createTextNode(opt);
            link.appendChild(text);
            //link.href = "";
            link.id = opt;
            link.name = opt;
            link.value = opt;
            li.appendChild(link);
            list.appendChild(li);
        }
    });

    $(function(){
        $(".dropdown-menu li a").click(function(){
              $("#dataPartnerListButton").text($(this).text());
              $("#dataPartnerListButton").val($(this).text());
              $("#dataPartner").val($(this).text());
       });
    });