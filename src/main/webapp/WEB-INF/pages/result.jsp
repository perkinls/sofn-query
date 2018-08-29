<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=emulateIE7"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>国家农产品信息库</title>
    <link href="${pageContext.request.contextPath}/resource/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resource/css/result.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resource/css/lyz.calendar.css" rel="stylesheet" type="text/css"/>
    <style>
        .black_overlay {
            display: none;
            position: absolute;
            top: 0%;
            left: 0%;
            width: 100%;
            height: 100%;
            background-color: black;
            z-index: 1001;
            -moz-opacity: 0.8;
            opacity: .80;
            filter: alpha(opacity=80);
        }

        .white_content {
            display: none;
            position: absolute;
            top: 10%;
            left: 10%;
            width: 80%;
            height: 80%;
            border: 16px solid lightblue;
            background-color: white;
            z-index: 1002;
            overflow: auto;
        }
    </style>
</head>
<body>
<div id="container">
    <div id="hd" class="ue-clear" st>
        <div class="logo"></div>
        <div class="inputArea">
            <input type="text" class="searchInput" id="keyword" value="${result.keyword}"/>
            <div class="datesearch">
                日期范围：<input id="txtBeginDate" style="width:120px;border:1px solid #ccc;" value="${result.startDate}"/>
                -至- <input id="txtEndDate" style="width:120px;border:1px solid #ccc;" value="${result.endDate}"/>
                <button id="reset" type="reset" onclick="reset()">重置</button>
            </div>
            <input type="button" id="searchES" class="searchButton"
                   onclick="searchKeyword($('#keyword').val(),$('#txtBeginDate').val(),$('#txtEndDate').val());"/>
            <%--<a class="advanced" href="/advanced.html">高级搜索</a>--%>
        </div>
    </div>
    <div id="bd" class="ue-clear">
        <div id="main">
            <div class="resultArea">
                <p class="resultTotal">
                    <span class="info">共匹配到&nbsp;<span
                            class="totalResult">${result.indexCount}</span>&nbsp;张表&nbsp;</span>
                    <span class="info">总共有&nbsp;<span class="totalResult">${result.recordCount}</span>&nbsp;条记录</span>
                    <a id="exportXls" onclick="exportXls();">&nbsp;&nbsp;&nbsp;&nbsp;导出到excel</a>
                </p>
                <div class="resultList">
                    <c:forEach items="${result.list }" varStatus="status" var="hit">
                        <div class="resultItem">
                            <div class="itemHead">
                                <a href="${pageContext.request.contextPath}/detail?startDate=${result.startDate}&endDate=${result.endDate}&keyword=${result.keyword}&index=${status.index}&pageIndex=${result.pageIndex}"
                                   target="_blank" class="title"><span>${hit.tableName}</span></a>
                                <span class="divsion">-</span>
                                <span class="fileType">
                            	    <span class="label">索引库：</span>
                                    <span class="value"> ${hit.indexName}, 共${hit.oneIndexCount}条记录</span>
                                </span>
                                <input type="checkbox" id="${hit.indexName}"
                                       onclick='checkExport("${hit.indexName}",JSON.stringify(${hit.selectFields}));'/>
                                <label for="${hit.indexName}">导出</label>
                            </div>
                            <div class="itemBody">
                                <c:forEach items="${hit.showRecords }" var="record">
                                    <p>${record}</p>
                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                    <!-- 分页 -->
                    <div class="pagination ue-clear"></div>
                </div>
            </div>
        </div><!-- End of main -->
    </div><!--End of bd-->
</div>

<div id="fade" class="black_overlay">
</div>
<div id="MyDiv" class="white_content">
    <div id="selectFields">

    </div>
    <button type="button" onclick="selectFields();">确定</button>
    <button type="button" onclick="CloseDiv('MyDiv','fade')">取消</button>
</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/jquery-1.5.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/global.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/pagination.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/lyz.calendar.min.js" type="text/javascript"></script>
<script type="text/javascript">
    var exportMap={};
    var currIndex="";

    $.each($('.subfieldContext'), function (i, item) {
        $(this).find('li:gt(2)').hide().end().find('li:last').show();
    });

    //分页
    $(".pagination").pagination(${result.indexCount}, {
        current_page: ${result.pageIndex} ? ${result.pageIndex} : 0, //当前页码
        items_per_page: 10,
        display_msg: true,
        callback: pageselectCallback
    });

    function pageselectCallback(page_id, jq) {
        window.location.href = "${pageContext.request.contextPath}/result?startDate=${result.startDate}&endDate=${result.endDate}&keyword=${result.keyword}&pageIndex=" + page_id;
        // alert("当前页id(由0开始)：" + page_id + "，\n每页显示：" + this.items_per_page + "条数据");
    }

    setHeight();
    $(window).resize(function () {
        setHeight();
    });

    function setHeight() {
        if ($('#container').outerHeight() < $(window).height()) {
            $('#container').height($(window).height() - 33);
        }
    }

    $("body").keydown(function () {
        if (event.keyCode == "13") {//keyCode=13是回车键
            $('#searchES').click();
        }
    });

    $(function () {
        $("#txtBeginDate").calendar({
            controlId: "divDate",                                 // 弹出的日期控件ID，默认: $(this).attr("id") + "Calendar"
            speed: 200,                                           // 三种预定速度之一的字符串("slow", "normal", or "fast")或表示动画时长的毫秒数值(如：1000),默认：200
            complement: true,                                     // 是否显示日期或年空白处的前后月的补充,默认：true
            readonly: true,                                       // 目标对象是否设为只读，默认：true
            upperLimit: new Date(),                               // 日期上限，默认：NaN(不限制)
            lowerLimit: new Date("2011/01/01"),                   // 日期下限，默认：NaN(不限制)
            // callback: function () {                               // 点击选择日期后的回调函数
            //     alert("您选择的日期是：" + $("#txtBeginDate").val());
            // }
        });
        $("#txtEndDate").calendar();
    });

    function reset() {
        //TODO
        $("#txtBeginDate").val("");
        $("#txtEndDate").val("")
        $("#keyword").val("");

    }

    $("body").keydown(function () {
        if (event.keyCode == "13") {//keyCode=13是回车键
            $('#searchES').click();
        }
    });

    function searchKeyword(keyword, startDate, endDate) {
        if ("" == keyword || "" == keyword.trim()) {
            return;
        }
        if (!startDate) {
            startDate = "";
        }
        if (!endDate) {
            endDate = "";
        }
        window.location.href = "${pageContext.request.contextPath}/result?keyword=" + keyword + "&startDate=" + startDate + "&endDate=" + endDate+"&pageIndex="+0;
    }

    //弹出隐藏层
    function ShowDiv(show_div, bg_div) {
        document.getElementById(show_div).style.display = 'block';
        document.getElementById(bg_div).style.display = 'block';
        var bgdiv = document.getElementById(bg_div);
        bgdiv.style.width = document.body.scrollWidth;
// bgdiv.style.height = $(document).height();
        $("#" + bg_div).height($(document).height());
    };

    //取消
    function CloseDiv(show_div, bg_div) {
        document.getElementById(show_div).style.display = 'none';
        document.getElementById(bg_div).style.display = 'none';
        var checkBoxObj = document.getElementById(currIndex);
        checkBoxObj.checked=false;
        delete exportMap[currIndex];
    }

    //提交需要导出的字段
    function selectFields() {
        let str = "";
        $("#selectFields input:checkbox").each(function () {
            if ($(this).is(':checked')) {
                str=str+$(this).val()+",";
            }
        });
        exportMap[currIndex]=str.endsWith(",")?str.substring(0,str.length-1):str;
        document.getElementById("MyDiv").style.display = 'none';
        document.getElementById("fade").style.display = 'none';
    }

    function checkExport(indexName, map) {
        // TODO  添加需要导入的索引
        currIndex = indexName;
        var checkBoxObj = document.getElementById(indexName);
        map = JSON.parse(map);
        if (checkBoxObj.checked) {
            //获得前台的div
            var insertDiv = document.getElementById("selectFields");
            //定义向前台插入的内容为空
            insertDiv.innerHTML = "";
            var chkinfo;
            var chkDIV;
            //var txtinfo;
            //解析从服务器获得的数据,循环添加复选框
            for (var key in map) {
                //为每一个复选框创建一个DIV
                chkDIV = document.createElement("div");
                //每一个复选框用input创建，类型为checkBox
                chkinfo = document.createElement("input");
                chkinfo.type = "checkbox";
                chkinfo.checked = true;
                //将每一个chinesename为复选框赋值
                chkinfo.value = key;
                //将复选框添加到Div中
                chkDIV.appendChild(chkinfo);
                //为Div设置样式
                chkDIV.style.height = "50px";
                chkDIV.style.width = "150px";
                chkDIV.style.float = "left";
                chkDIV.align = "left";
                chkDIV.appendChild(document.createTextNode(map[key]));
                //将创建的div添加到前台预留的DIV下
                insertDiv.appendChild(chkDIV);
            }
            ShowDiv('MyDiv','fade');
        }
        // 移除不需要导出的索引
        else {
            delete exportMap[currIndex];
        }
    }

    function exportXls() {
        $.ajax({
            type: "post",
            url: "${pageContext.request.contextPath}/export",
            data:
                {
                    'exportItems': JSON.stringify(exportMap),
                    "pageIndex":${result.pageIndex},
                    "startDate": "${result.startDate}",
                    "endDate": "${result.endDate}",
                    "keyword": "${result.keyword}"
                },
            success: function(data){
                location.href=data.filePath;
            },
            error: function(xhr,status,errMsg){
                alert("操作失败!");
            }
        });
    }
</script>
</html>