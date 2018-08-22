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
<link href="${pageContext.request.contextPath}/resource/css/index.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div id="container">
	<div id="bd">
        <div id="main">
        	<h1 class="title">
            	<div class="logo large"></div>
            </h1>

            <div class="inputArea">
            	<input type="text" class="searchInput" id="keyword"/>
                <%--<input type="button" class="searchButton" onclick="javascript:window.location='/result.html'" />--%>
                <input type="button" class="searchButton" onclick="searchKeyword($('#keyword').val());" />
                <%--<a class="advanced" href="/advanced.html">高级搜索</a>--%>
            </div>
        </div><!-- End of main -->
    </div><!--End of bd-->

</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/global.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/search.js"></script>
<script type="text/javascript">
	$('.searchList').on('click', '.searchItem', function(){
		$('.searchList .searchItem').removeClass('current');
		$(this).addClass('current');	
	});


</script>
</html>