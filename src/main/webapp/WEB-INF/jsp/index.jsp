<%--
  Created by IntelliJ IDEA.
  User: zhouchuang
  Date: 2018/5/12
  Time: 10:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="/bootstrap/css/bootstrap.min.css" >
    <link rel="stylesheet" href="/bootstrap/css/bootstrap-theme.min.css" >
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="/static/js/jquery-2.2.3.js"></script>
    <script src="/bootstrap/js/bootstrap.min.js" ></script>

    <title>欢迎界面</title>
    <style>
        #panel{
            padding-top: 5px;
        }
    </style>
    <script>
        $(document).ready(function(e) {
            var counter = 0;
            if (window.history && window.history.pushState) {
                $(window).on('popstate', function () {
                    window.history.pushState('forward', null, '#');
                    window.history.forward(1);
                    util.url.history.back($("#panel"));
                });
            }
             window.history.pushState('forward', null, '#'); //在IE中必须得有这两行
             window.history.forward(1);

        });
    </script>
</head>
<body >
<div class="row" style="width: 90%;margin:10px auto;">
    <div class="col-md-3">
        <jsp:include page="menu.jsp"/>
    </div>
    <div class="col-md-9">
        <div id="panel">

        </div>
    </div>
</div>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->

<script src="/static/js/template.js"></script>
<script src="/static/js/templateUtils.js"></script>
<script src="/static/js/util.js"></script>
<script>

    $(".list-group").on('click','a',function () {
        var url  = $(this).data("url");
        $("#panel").loadPage({path:url});
    })

    $(".list-group a:first").click();

</script>
</body>
</html>
