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
	<div id="hd" class="ue-clear">
    	<div class="logo"></div>
        <div class="inputArea">
        	<input type="text" class="searchInput" />
            <input type="button" class="searchButton" />
            <a class="advanced" href="${pageContext.request.contextPath}/WEB-INF/pages/advanced.html">高级搜索</a>
        </div>
    </div>
    <div class="nav">
    	<ul class="searchList">
            <li class="searchItem current">资讯</li>
            <li class="searchItem">设计</li>
            <li class="searchItem">网页</li>
            <li class="searchItem">知道</li>
            <li class="searchItem">音乐</li>
            <li class="searchItem">图片</li>
            <li class="searchItem">视频</li>
            <li class="searchItem">地图</li>
            <li class="searchItem">百科</li>
            <li class="searchItem">文库</li>
        </ul>
        <a href="javascript:;" class="tips">什么是分类搜索</a>
    </div>
	<div id="bd" class="ue-clear">
        <div id="main">
        	<div class="sideBar">
            	<div class="subfield">设计分类</div>
                <ul class="subfieldContext">
                	<li>
                    	<span class="name">后台界面</span>
                        <span class="unit">(9862)</span>
                    </li>
                    <li>
                    	<span class="name">图标</span>
                        <span class="unit">(485)</span>
                    </li>
                    <li>
                    	<span class="name">自定义</span>
                        <span class="unit">(1564)</span>
                    </li>
                    <li>
                    	<span class="name">UI设计</span>
                        <span class="unit">(485)</span>
                    </li>
                    <li>
                    	<span class="name">游戏界面</span>
                        <span class="unit">(485)</span>
                    </li>
                    <li class="more">
                    	<a href="javascript:;">
                        	<span class="text">更多</span>
                        	<i class="moreIcon"></i>
                        </a>
                    </li>
                </ul>
                <div class="subfield">发布时间</div>
                <ul class="subfieldContext">
                	<li>
                    	<span class="name">2013年</span>
                        <span class="unit">(9862)</span>
                    </li>
                    <li>
                    	<span class="name">2012年</span>
                        <span class="unit">(485)</span>
                    </li>
                    <li>
                    	<span class="name">2011年</span>
                        <span class="unit">(1564)</span>
                    </li>
                    <li>
                    	<input type="text" />
                        <span class="divsion">-</span>
                        <input type="text" />
                    </li>
                    <li class="more define">
                    	<a href="javascript:;">
                        	<span class="text">自定义</span>
                        </a>
                    </li>
                    
                </ul>
                <div class="subfield">搜索引擎</div>
                <ul class="subfieldContext">
                	<li>
                    	<span class="name">百度搜索</span>
                    </li>
                    <li>
                    	<span class="name">Google</span>
                    </li>
                    <li>
                    	<span class="name">360搜索</span>
                    </li>
                    <li>
                    	<span class="name">搜搜</span>
                    </li>
                    <li class="more">
                    	<a href="javascript:;">
                        	<span class="text">更多</span>
                        	<i class="moreIcon"></i>
                        </a>
                    </li>
                </ul>
                
                <div class="subfield">您是不是要找</div>
                <ul class="subfieldContext">
                	<li>
                    	<span class="name">UI设计师</span>
                    </li>
                    <li>
                    	<span class="name">界面作品</span>
                    </li>
                </ul>
                
                <a href="javascript:;" class="reset">重置搜索选项</a>
                
                <div class="sideBarShowHide">
                	<a href="javascript:;" class="icon"></a>
                </div>
            </div>
            <div class="resultArea">
            	<p class="resultTotal">
                	<span class="info">找到约&nbsp;<span class="totalResult">2,608,495</span>&nbsp;条结果(用时<span class="time">0.36</span>秒)，共约<span class="totalPage">1900</span>页</span>
                    <span class="orderOpt">
                    	<a href="javascript:;" class="byTime">按时间排序</a>
                        <a href="javascript:;" class="byDependence">按相关度排序</a>
                    </span>
                </p>
                <div class="resultList">
                	<div class="resultItem">
                    	<div class="itemHead">
                        	<a href="http://www.uimaker.com"  target="_blank" class="title">Uimaker-专注<span class="keyWord">UI设计</span></a>
                            <span class="divsion">-</span>
                            <span class="fileType">
                            	<span class="label">设计分类：</span>
                                <span class="value">后台界面</span>
                            </span>
                            <span class="dependValue">
                            	<span class="label">相关度：</span>
                                <span class="value">91.78%</span>
                            </span>
                        </div>
                        <div class="itemBody">
                        	UI，我们还为<span class="keyWord">UI设计</span>师提供UI设计教、UI设计素材、ICON、图标设计、手机UI、ui设计师招聘、软件界面设计、后台界面、后台模版等相关内容;很多题目一问出来就知道怎么回答是高EQ 应该从一些具体的事出发 唉。。。 下次我出一个肯定比它好 欧阳筱筱 （何时才能遇见那个命中的克星呢？） 你的分数为28分，恭喜你有高EQ。EQ高的人，在自信心、人际关系、工作表现、婚姻...
                        </div>
                        <div class="itemFoot">
                        	<span class="info">
                            	<label>设计师：</label>
                                <span class="value">uimaker</span>
                            </span>
                            <span class="info">
                            	<label>搜索引擎：</label>
                                <span class="value">百度搜索</span>
                            </span>
                            <span class="info">
                            	<label>发布时间：</label>
                                <span class="value">2013-12-12 12:12</span>
                            </span>
                        </div>
                    </div>
                    <div class="resultItem">
                    	<div class="itemHead">
                        	<a href="http://www.uimaker.com"  target="_blank" class="title">学<span class="keyWord">ui设计</span>培训班多少钱-ui培训费用-ui设计培训哪里好</a>
                            <span class="divsion">-</span>
                            <span class="fileType">
                            	<span class="label">设计分类：</span>
                                <span class="value">UI培训</span>
                            </span>
                            <span class="dependValue">
                            	<span class="label">相关度：</span>
                                <span class="value">91.78%</span>
                            </span>
                        </div>
                        <div class="itemBody">
                        	不管这个<span class="keyWord">UI设计</span>洛可可工业设计荣获四项国际顶级设计大奖，多次受到国家领导人接见.服务过三星，诺基亚在内的众多国内外知名企业.是代表中国的世界顶级创新设计集团.TEL:010-6433洛可可工业设计荣获四项国际顶级设计大奖，多次受到国家领导人接见.服务过三星，诺基亚在内的众多国内外知名企业.是代表中国的世界顶级创新设计集团.TEL:010-6433
                        </div>
                        <div class="itemFoot">
                        	<span class="info">
                            	<label>设计师：</label>
                                <span class="value">Esion</span>
                            </span>
                            <span class="info">
                            	<label>搜索引擎：</label>
                                <span class="value">Google</span>
                            </span>
                            <span class="info">
                            	<label>发布时间：</label>
                                <span class="value">2013-12-12 12:12</span>
                            </span>
                        </div>
                    </div>
                    <div class="resultItem">
                    	<div class="itemHead">
                        	<a href="http://www.uimaker.com"  target="_blank" class="title"><span class="keyWord">UI设计</span>的微刊|微刊 - 悦读喜欢</a>
                            <span class="divsion">-</span>
                            <span class="fileType">
                            	<span class="label">设计分类：</span>
                                <span class="value">手机界面</span>
                            </span>
                            <span class="dependValue">
                            	<span class="label">相关度：</span>
                                <span class="value">91.78%</span>
                            </span>
                        </div>
                        <div class="itemBody">
                        	2012年7月13日... 移动互联网时代的到来，给我们带来便利的同时，也给众多移动网页<span class="keyWord">UI设计</span>师们提出了更大的挑战，因为相比于传统的电脑屏幕，移动设备幕要小很多，设计师2012年7月13日... 移动互联网时代的到来，给我们带来便利的同时，也给众多移动网页设计师们提出了更大的挑战，因为相比于传统的电脑屏幕，移动设备的屏幕要小很多，设计师...
                        </div>
                        <div class="itemFoot">
                        	<span class="info">
                            	<label>设计师：</label>
                                <span class="value">uimaker.com</span>
                            </span>
                            <span class="info">
                            	<label>搜索引擎：</label>
                                <span class="value">360搜索</span>
                            </span>
                            <span class="info">
                            	<label>发布时间：</label>
                                <span class="value">2013-12-13 14:50</span>
                            </span>
                        </div>
                    </div>
                    <div class="resultItem">
                    	<div class="itemHead">
                        	<a href="http://www.uimaker.com"  target="_blank" class="title">非常有用的免费<span class="keyWord">UI设计</span>工具和资源 - 博客园
</a>
                            <span class="divsion">-</span>
                            <span class="fileType">
                            	<span class="label">设计分类：</span>
                                <span class="value">游戏UI</span>
                            </span>
                            <span class="dependValue">
                            	<span class="label">相关度：</span>
                                <span class="value">91.78%</span>
                            </span>
                        </div>
                        <div class="itemBody">
                        	不管这个<span class="keyWord">UI设计</span>太不正规的 很多题目一问出来就知道怎么回答是高EQ 应该从一些具体的时触 很多题目一问出来就知道怎么回答是高EQ 应该从一些具体的时触发 很多题目一问出来就知道怎么回答是高EQ 应该从一些具体的时触发 很多题目一问出来就知道怎么回答是高EQ 应该从一些具体的时触发 很多题目一问出来就
                        </div>
                        <div class="itemFoot">
                        	<span class="info">
                            	<label>设计师：</label>
                                <span class="value">uimaker</span>
                            </span>
                            <span class="info">
                            	<label>搜索引擎：</label>
                                <span class="value">百度搜索</span>
                            </span>
                            <span class="info">
                            	<label>发布时间：</label>
                                <span class="value">2013-12-12 12:12</span>
                            </span>
                        </div>
                    </div>
                </div>
                <!-- 分页 -->
                <div class="pagination ue-clear"></div>
                <!-- 相关搜索 -->
                <div class="dependSearch ue-clear">
                    <h6>相关搜索</h6>
                    <div class="searchList">
                        <p>
                            <a href="javascript:;">UI设计</a>
                            <a href="javascript:;">UI设计师</a>
                            <a href="javascript:;">图标设计</a>
                            <a href="javascript:;">设计师</a>
                        </p>
                        <p>
                            <a href="javascript:;">后台界面</a>
                            <a href="javascript:;">管理系统界面</a>
                            <a href="javascript:;">界面欣赏</a>
                            <a href="javascript:;">交互设计</a>
                        </p>
                    </div>
                </div>
                
                <!--结果中搜索-->
                <div class="inputArea searchInResult">
                    <input type="text" class="searchInput" />
                    <input type="button" class="searchButton" />
                    <a class="inResult" javascript:;>在结果中搜索</a>
                </div>
            </div>
            <div class="historyArea">
            	<div class="hotSearch">
                	<h6>热门搜索</h6>
                    <ul class="historyList">
                    	<li><a href="javascript:;">UI设计</a></li>
                        <li><a href="javascript:;">界面设计</a></li>
                        <li><a href="javascript:;">手机界面</a></li>
                        <li><a href="javascript:;">交互</a></li>
                        <li><a href="javascript:;">图标</a></li>
                        <li><a href="javascript:;">UI素材</a></li>
                        <li><a href="javascript:;">教程</a></li>
                    </ul>
                </div>
                <div class="mySearch">
                	<h6>我的搜索</h6>
                    <ul class="historyList">
                    	<li><a href="javascript:;">专注界面设计网站</a></li>
                        <li><a href="javascript:;">用户体验</a></li>
                        <li><a href="javascript:;">互联网</a></li>
                        <li><a href="javascript:;">UI设计师</a></li>
                    </ul>
                </div>
            </div>
        </div><!-- End of main -->
    </div><!--End of bd-->
</div>

<div id="foot">Copyright &copy;uimaker.com 版权所有  E-mail:admin@uimaker.com</div>
</body>
<script type="text/javascript" src=".${pageContext.request.contextPath}/resource/js/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/global.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/pagination.js"></script>
<script type="text/javascript">
	$('.searchList').on('click', '.searchItem', function(){
		$('.searchList .searchItem').removeClass('current');
		$(this).addClass('current');	
	});
	
	
	
	$.each($('.subfieldContext'), function(i, item){
		$(this).find('li:gt(2)').hide().end().find('li:last').show();		
	});
	
	$('.subfieldContext .more').click(function(e){
		var $more = $(this).parent('.subfieldContext').find('.more');
		if($more.hasClass('show')){
			
			if($(this).hasClass('define')){
				$(this).parent('.subfieldContext').find('.more').removeClass('show').find('.text').text('自定义');
			}else{
				$(this).parent('.subfieldContext').find('.more').removeClass('show').find('.text').text('更多');	
			}
			$(this).parent('.subfieldContext').find('li:gt(2)').hide().end().find('li:last').show();
	    }else{
			$(this).parent('.subfieldContext').find('.more').addClass('show').find('.text').text('收起');
			$(this).parent('.subfieldContext').find('li:gt(2)').show();	
		}
		
	});
	
	$('.sideBarShowHide a').click(function(e) {
		if($('#main').hasClass('sideBarHide')){
			$('#main').removeClass('sideBarHide');
			$('#container').removeClass('sideBarHide');
		}else{
			$('#main').addClass('sideBarHide');	
			$('#container').addClass('sideBarHide');
		}
        
    });
	
	//分页
	$(".pagination").pagination(500, {
		current_page :0, //当前页码
		items_per_page :9,
		display_msg :true,
		callback :pageselectCallback
	});
	function pageselectCallback(page_id, jq) {
		alert("当前页id(由0开始)：" + page_id + "，\n每页显示：" + this.items_per_page + "条数据");
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
</script>
</html>