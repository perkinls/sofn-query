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
                            <input name="es_index" type="checkbox"  value="subj_supervise_detail">
                            <span class="text">监管机构</span>

                        </span>
                        <span class="choose">
                           <input name="es_index" type="checkbox" value="subj_detection_detail">
                            <span class="text">检测机构</span>
                        </span>
                        <span class="choose">
                            <input name="es_index" type="checkbox" value="subj_enforce_law_detail">
                            <span class="text">执法机构</span>
                        </span>
                        <span class="choose">
                            <input name="es_index" type="checkbox" value="subj_enterprise_detail">
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
                            <input name="es_index" type="checkbox"
                                   value="tts_scltxxcj_product_v2,tts_scltxxcj_xsdj_v2,tts_scltxxcj_scgl_v2,tts_scltxxcj_xsdjjl_v2,tts_scltxxcj_cppchc_v2,tts_scltxxcj_cggl_v2,tts_scltxxcj_xsth_v2,tts_scltxxcj_sla_record_v2">
                            <span class="text">产品追溯</span>
                        </span>
                        <span class="choose">
                            <input name="es_index" type="checkbox" value="business_asms_routine_monitor">
                            <span class="text">例行监测</span>
                        </span>
                        <span class="choose">
                            <input name="es_index" type="checkbox" value="business_asms_special_monitor">
                            <span class="text">专项监测</span>
                        </span>
                        <span class="choose">
                            <input name="es_index" type="checkbox" value="business_asms_check_task">
                            <span class="text">监督抽查</span>
                        </span>
                        <span class="choose">
                            <input name="es_index" type="checkbox" value="business_asms_recheck_task">
                            <span class="text">复检任务</span>
                        </span>
                        <span class="choose">
                            <input name="es_index" type="checkbox" value="business_asms_base_inspection">
                            <span class="text">基地巡查</span>
                        </span>
                        <span class="choose">
                            <input name="es_index" type="checkbox" value="business_asms_inspection_assess">
                            <span class="text">考核任务</span>
                        </span>
                        <%--<span class="choose">--%>
                            <%--<input name="es_index" type="checkbox"--%>
                                   <%--value="ales_task_sample,ales_wt_object_criterion,ales_entrust_detection,ales_wt_task_enterprise">--%>
                            <%--<span class="text">执法检查</span>--%>
                        <%--</span>--%>
                        <%--<span class="choose">--%>
                            <%--<input name="es_index" type="checkbox" value="">--%>
                            <%--<span class="text">主体交易</span>--%>
                        <%--</span>--%>
                    </dt>
                </dl>
                
                <!--检测信息-->
                <dl class="ue-clear advanceItem part">
                	<dd>
                    	<label>检测信息</label>
                        <span>指定检测信息条件</span>
                    </dd>
                    <dt class="fillInArea">
                    	<span class="choose">
                            <input name="es_index" type="checkbox" value="ads_monitor_task">
                            <span class="text">检测任务</span>
                        </span>
                        <span class="choose">
                            <input name="es_index" type="checkbox" value="ads_info_project">
                            <span class="text">检测项目</span>
                        </span>
                        <%--<span class="choose">--%>
                            <%--<input name="es_index" type="checkbox" value="">--%>
                            <%--<span class="text">受检单位</span>--%>
                        <%--</span>--%>
                    </dt>
                </dl>
                
                <!--执法信息-->
                <dl class="ue-clear advanceItem time">
                	<dd>
                    	<label>执法信息</label>
                        <span>指定执法信息条件</span>
                    </dd>
                    <dt class="fillInArea">
                    	<%--<span class="choose">--%>
                            <%--<input name="es_index" type="checkbox" value="">--%>
                            <%--<span class="text">监督抽查</span>--%>
                        <%--</span>--%>
                        <span class="choose">
                            <input name="es_index" type="checkbox" value="ales_wt_task_monitor">
                            <span class="text">委托任务</span>
                        </span>
                        <span class="choose">
                            <input name="es_index" type="checkbox" value="ales_daily_enforce_law">
                            <span class="text">日常执法</span>
                        </span>
                        <span class="choose">
                            <input name="es_index" type="checkbox" value="ales_produce_admin_punish">
                            <span class="text">行政处罚</span>
                        </span>
                    </dt>
                </dl>

                <!--时间信息-->
                <dl class="ue-clear advanceItem time">
                    <dd>
                        <label>时间</label>
                        <span>指定时间范围</span>
                    </dd>
                    <dt class="fillInArea">
                    	<span class="choose">
                            <input type="radio" name="time" value="all" checked="checked">
                            <span class="text">全部</span>
                        </span>
                        <span class="choose">
                            <input type="radio" name="time" value="day">
                            <span class="text">近一天</span>
                        </span>
                        <span class="choose">
                            <input type="radio" name="time" value="week">
                            <span class="text">近一周</span>
                        </span>
                        <span class="choose">
                            <input type="radio" name="time" value="month">
                            <span class="text">近一月</span>
                        </span>
                        <span class="choose">
                            <input type="radio" name="time" value="year">
                            <span class="text">近一年</span>
                        </span>
                    </dt>
                </dl>

                <!--搜索关键字-->
                <dl class="ue-clear advanceItem keyWords">
                    <dd>
                        <label>关键字</label>
                        <div class="tips">
                            <p class="tip">包含以下<span class="impInfo">全部</span>的关键</p>
                            <p class="tip">包含以下<span class="impInfo">任意一个</span>关键词</p>
                        </div>
                    </dd>
                    <dt class="fillInArea">
                        <p><input type="text" id="all_keywords"/> <input type="button"  value="立刻搜索" onclick="advancedSearchAll();" /></p>
                        <p><input type="text" id="any_keywords"/> <input type="button"  value="立刻搜索" onclick="advancedSearchAny();" /></p>
                    </dt>

                </dl>

                <%--<div class="button">--%>
                	<%--<input type="button" class="search" value="立刻搜索" onclick="advancedSearch();" />--%>
                <%--</div>--%>
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
    function advancedSearchAll() {
        let all_keywords = $('#all_keywords').val();
        let timeInfo = $("input[name='time']:checked").val();
        let indices = "";
        $.each($("input[name='es_index']:checked"),function(){
            indices = indices + $(this).val() + ",";
        });

        $.post(
            "/result",
            {
                "all_keywords":all_keywords,
                "timeInfo":timeInfo,
                "indices":indices
            },
            function(data) {
                // 等待2S，关闭提示信息
                setTimeout(function(){
                    alert("请求超时！");
                }, 2000);
            }
        );
        // window.location.href = "/result?keyword="+keyword;
    }
	function advancedSearchAny() {
        let any_keywords = $('#any_keywords').val();
        let timeInfo = $("input[name='time']:checked").val();
        var indices = "";
        $.each($("input[name='es_index']:checked"),function(){
            indices = indices + $(this).val() + ",";
        });

        $.post(
            "/adv",
            {
                "any_keywords":any_keywords,
                "timeInfo":timeInfo,
                "indices":indices
            },
            function(data) {
                // 等待2S，关闭提示信息
                setTimeout(function(){
                    alert("请求超时！");
                }, 2000);
            }
        );
    }
	
</script>
</html>