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


    <title>登陆</title>
    <style>
        .loginContent{
            position: absolute;
            top: 50%;
            height: 240px;
            margin-top: -120px; /* negative half of the height */
        }
        .line{
            padding-top: 5px;
        }
    </style>
</head>
<body>
<div class="row loginContent" >
    <div class="col-md-4"></div>
    <div class="col-md-4">
        <form action="/toLogin">
            <div class="input-group input-group-lg line">
                <span class="input-group-addon" id="sizing-addon1">@</span>
                <input type="text" name="accountNo" class="form-control" placeholder="Username" value="admin" aria-describedby="sizing-addon1">
            </div>
            <div class="input-group input-group-lg line">
                <span class="input-group-addon" id="sizing-addon2">@</span>
                <input type="password"  name="password" class="form-control" placeholder="Password" value="123456" aria-describedby="sizing-addon2">
            </div>
            <div class="input-group input-group-lg line">
                <button type="submit" class="btn btn-default" id="submit">登录</button>
            </div>
        </form>
    </div>
    <div class="col-md-4"></div>
</div>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="/static/js/jquery-2.2.3.js"></script>
<script src="/bootstrap/js/bootstrap.min.js" ></script>
<script>
    $("#submit").click();
</script>
</body>
</html>
