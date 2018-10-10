<%--
  Created by IntelliJ IDEA.
  User: zhouchuang
  Date: 2018/5/12
  Time: 15:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<style>
    .success{
        background-color:#5cb85c;
    }
</style>
<body>
<ul class="list-group">
    <script id="tablelist" type="text/html" data-url="/TableController/TableListDetail">
        {{each list as table}}
        <li class="list-group-item" onclick="util.loadPage({path:'/table/fieldlist?table={{table.name}}'})"><span class="badge {{table.status==1?'success':'' }}">{{table.status==0?'未生成':'已生成'}}</span>{{table.name}}</li>
        {{/each}}
    </script>
</ul>
<script>
    $("#tablelist").loadData();
</script>
</body>
</html>
