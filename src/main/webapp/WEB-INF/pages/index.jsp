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
            	<input type="text" class="searchInput" />
                <input type="button" class="searchButton" onclick="javascript:window.location='/result'" />
                <a class="advanced" href="/advanced">高级搜索</a>
                <ul class="dataList">
                	<li>追溯码查询</li>
                    <li>界面设计</li>
                    <li>xxxxxx</li>
                    <li>设计师学习</li>
                    <li>哪里有好的网站</li>
                </ul>
            </div>
        </div><!-- End of main -->
    </div><!--End of bd-->
    
    <div class="foot">
    	<div class="wrap">
            <div class="copyright">Copyright &copy;xxxxxxx.com 版权所有  E-mail:admin@uimaker.com</div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/global.js"></script>
<script type="text/javascript">
	$('.searchList').on('click', '.searchItem', function(){
		$('.searchList .searchItem').removeClass('current');
		$(this).addClass('current');	
	});
	
	// 联想下拉显示隐藏
	$('.searchInput').on('focus', function(){
		$('.dataList').show()
    });
	
	// 联想下拉点击
	$('.dataList').on('click', 'li', function(){
		var text = $(this).text();
		$('.searchInput').val(text);
		$('.dataList').hide()
	});
	
	hideElement($('.dataList'), $('.searchInput'));
</script>
</html>