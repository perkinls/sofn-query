<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=emulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Uimaker-专注UI设计</title>
<link href="${pageContext.request.contextPath}/resource/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resource/css/result.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="container">
	<div id="hd" class="ue-clear" st>
    	<div class="logo"></div>
        <div class="inputArea">
        	<input type="text" class="searchInput" id="keyword"/>
            <input type="button" id="searchES" class="searchButton" onclick="searchKeyword($('#keyword').val());"/>
            <%--<a class="advanced" href="/advanced.html">高级搜索</a>--%>
        </div>
    </div>
	<div id="bd" class="ue-clear">
        <div id="main">
            <div class="resultArea">
                <p class="resultTotal">
                    <span class="info">共匹配到&nbsp;<span class="totalResult">${result.indexCount}</span>&nbsp;张表&nbsp;</span>
                    <span class="info">总共有&nbsp;<span class="totalResult">${result.recordCount}</span>&nbsp;条记录</span>
                </p>
                <div class="resultList">
                    <c:forEach items="${result.list }" varStatus="status" var="hit">
                        <div class="resultItem">
                            <div class="itemHead">
                                <a href="/detail?keyword=${result.keyword}&index=${status.index}&pageIndex=${result.pageIndex}"  target="_blank" class="title"><span>${hit.tableName}</span></a>
                                <span class="divsion">-</span>
                                <span class="fileType">
                            	    <span class="label">索引库：</span>
                                    <span class="value"> ${hit.indexName}, 共${hit.oneIndexCount}条记录</span>
                                </span>
                                <input type="checkbox" id="${hit.indexName}"><label for="${hit.indexName}">导出</label>
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
</body>
<script type="text/javascript" src=".${pageContext.request.contextPath}/resource/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/global.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/pagination.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/search.js"></script>
<script type="text/javascript">

	
	
	
	$.each($('.subfieldContext'), function(i, item){
		$(this).find('li:gt(2)').hide().end().find('li:last').show();		
	});
	
	//分页
	$(".pagination").pagination(${result.indexCount}, {
		current_page :${result.pageIndex}?${result.pageIndex}:0, //当前页码
		items_per_page :10,
		display_msg :true,
		callback :pageselectCallback
	});
	function pageselectCallback(page_id, jq) {
        window.location.href = "/result?keyword=${result.keyword}&pageIndex="+page_id;
		// alert("当前页id(由0开始)：" + page_id + "，\n每页显示：" + this.items_per_page + "条数据");
	}
	
	setHeight();
	$(window).resize(function(){
		setHeight();	
	});
	
	function setHeight(){
		if($('#container').outerHeight() < $(window).height()){
			$('#container').height($(window).height()-33);
		}	
	}

    $("body").keydown(function() {
        if (event.keyCode == "13") {//keyCode=13是回车键
            $('#searchES').click();
        }
    });
</script>
</html>