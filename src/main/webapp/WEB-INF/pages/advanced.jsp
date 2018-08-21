<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=emulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>农产品追溯检索系统</title>
<link href="${pageContext.request.contextPath}/resource/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/resource/css/advanced.css" rel="stylesheet" type="text/css" />
    <script src="${pageContext.request.contextPath}/resource/js/jquery-1.12.3.min.js"></script>
</head>
<body>
<div id="container">
	<div id="hd" class="ue-clear">
    	<div class="logo"></div>
        <div class="inputArea">
        	<input type="text" class="searchInput" id="keyword"/>
            <input type="button" class="searchButton" onclick="searchKeyword($('#keyword').val());"/>
            <a class="back" href="/index.html">返回主页</a>
        </div>
    </div>
    <div class="divsion"></div>
	<div id="bd">
        <div id="main">
        	<div class="subfield">高级搜索</div>
            <div class="subfieldContent">
            	<!--搜索范围-->
                <dl class="ue-clear advanceItem">
                	<dd>
                    	<label>主体信息</label>
                        <span>选择要查询的机构</span>
                    </dd>
                    <dt class="fillInArea">
                    	<span class="choose">
                            <input name="scope" type="checkbox" checked="checked">
                            <span class="text">监管机构</span>
                        </span>
                        <span class="choose">
                        <input type="checkbox" name="scope">
                            <span class="text">检测机构</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="scope">
                            <span class="text">执法机构</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="scope">
                            <span class="text">生产经营商</span>
                        </span>
                    </dt>
                </dl>
                
                <!--监管信息-->
                <dl class="ue-clear advanceItem">
                	<dd>
                    	<label>监管信息</label>
                        <span>指定的文件类型</span>
                    </dd>
                    <dt class="fillInArea">
                    	<span class="choose">
                            <input name="type" type="checkbox" checked="checked">
                            <span class="text">农产品追溯</span>
                        </span>
                        <span class="choose">
                        <input type="checkbox" name="type">
                            <span class="text">例行监测</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">专项监测</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">监督抽查</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">复检任务</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">基地巡查</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">考核任务</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">执法检查</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">执法人员</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="type">
                            <span class="text">主体交易</span>
                        </span>
                    </dt>
                </dl>
                
                <!--检测信息-->
                <dl class="ue-clear advanceItem part">
                	<dd>
                    	<label>检测信息</label>
                        <span>指定检测信息条韩</span>
                    </dd>
                    <dt class="fillInArea">
                    	<span class="choose">
                            <input name="scope" type="checkbox" checked="checked">
                            <span class="text">检测任务</span>
                        </span>
                        <span class="choose">
                        <input type="checkbox" name="scope">
                            <span class="text">检测项目</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="scope">
                            <span class="text">受检单位</span>
                        </span>
                    </dt>
                </dl>
                
                <!--执法信息-->
                <dl class="ue-clear advanceItem time">
                	<dd>
                    	<label>执法信息</label>
                        <span>指定执法信息条件</span>
                    </dd>
                    <dt class="fillInArea">
                    	<span class="choose">
                            <input type="checkbox" name="time">
                            <span class="text">监督抽查</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="time">
                            <span class="text">委托任务</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="time">
                            <span class="text">日常执法</span>
                        </span>
                        <span class="choose">
                            <input type="checkbox" name="time">
                            <span class="text">行政处罚</span>
                        </span>
                    </dt>
                </dl>

                <!--搜索关键字-->
                <dl class="ue-clear advanceItem keyWords">
                    <dd>
                        <label>关键字及时间</label>
                        <div class="tips">
                            <p class="tip">包含以下<span class="impInfo">全部</span>的关键</p>
                            <p class="tip">包含以下<span class="impInfo">任意一个</span>关键词</p>
                            <p class="tip"><span class="impInfo">指定</span>时间范围</p>
                        </div>
                    </dd>
                    <dt class="fillInArea">
                        <p><input type="text" /></p>
                        <p><input type="text" /></p>
                    </dt>
                    <dt class="fillInArea">
                    	<span class="choose">
                            <input type="radio" name="time">
                            <span class="text">全部</span>
                        </span>
                        <span class="choose">
                            <input type="radio" name="time">
                            <span class="text">近一天</span>
                        </span>
                        <span class="choose">
                            <input type="radio" name="time">
                            <span class="text">近一周</span>
                        </span>
                        <span class="choose">
                            <input type="radio" name="time">
                            <span class="text">近一月</span>
                        </span>
                        <span class="choose">
                            <input type="radio" name="time">
                            <span class="text">近一年</span>
                        </span>
                    </dt>
                </dl>

                <div class="button">
                	<input type="button" class="search" value="立刻搜索" onclick="javascript:window.location='/result'" />
                </div>
            </div>
        </div><!-- End of main -->
    </div><!--End of bd-->
</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/global.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/search.js"></script>
<script type="text/javascript">
	$('.defineRadio input[type=radio]').click(function(e) {
        if($(this).prop('checked')){
			$('.define').show();
		}
    });
	
	$('.time input[type=radio]').click(function(){
		if(!$(this).parent().hasClass('defineRadio')){
			$('.define').hide();
		}	
	});

	
	setHeight();
	$(window).resize(function(){
		setHeight();	
	});
	
	function setHeight(){
		if($('#container').outerHeight() < $(window).height()){
			$('#container').height($(window).height()-33);
		}	
	}
</script>
</html>