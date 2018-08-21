<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title></title>
    <script type="text/javascript">
        var jsonArray = [{"编号":"001","名称":"小苹果","描述":"现代神曲，大妈的最爱"},{"编号":"002","名称":"mou宝","描述":"想怎么玩就怎么完"}];
        var headArray = [];
        function parseHead(oneRow) {
            for ( var i in oneRow) {
                headArray[headArray.length] = i;
            }
        }
        function appendTable() {
            parseHead(jsonArray[0]);
            var div = document.getElementById("div1");
            var table = document.createElement("table");
            var thead = document.createElement("tr");
            for ( var count = 0; count < headArray.length; count++) {
                var td = document.createElement("th");
                td.innerHTML = headArray[count];
                thead.appendChild(td);
            }
            table.appendChild(thead);
            for ( var tableRowNo = 0; tableRowNo < jsonArray.length; tableRowNo++) {
                var tr = document.createElement("tr");
                for ( var headCount = 0; headCount < headArray.length; headCount++) {
                    var cell = document.createElement("td");
                    cell.innerHTML = jsonArray[tableRowNo][headArray[headCount]];
                    tr.appendChild(cell);
                }
                table.appendChild(tr);
            }
            div.appendChild(table);
        }
    </script>
    <style>
        table {
            width: 600px;
            padding: 0 ;
            margin: 100px;
            border-collapse: collapse;
        }
        td,th {
            border: 1px solid #ddd;
            padding: 6px 6px 6px 12px;
            color: #4f6b72;
            text-align: center;
        }
        th {
            background: #d3d3d3;

        }
    </style>
</head>
<body onload="appendTable(jsonArray);">
<div id="div1"></div>
</body>
</html>