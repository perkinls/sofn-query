<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>搜索详情页</title>
    <script type="text/javascript">
        var jsonArray = ${currMap.records};
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