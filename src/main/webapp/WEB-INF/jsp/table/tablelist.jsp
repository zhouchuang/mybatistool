<%--
  Created by IntelliJ IDEA.
  User: zhouchuang
  Date: 2018/5/12
  Time: 15:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<ul class="list-group">
    <script id="tablelist" type="text/html" data-url="/TableController/TableList?database=tool">
        {{each list as table}}
        <li class="list-group-item"><a href="javascript:void(0)" onclick="util.loadPage({path:'/table/fieldlist?table={{table}}'})">{{table}}</a></li>
        {{/each}}
    </script>
</ul>
<script>
    $("#tablelist").loadData();
</script>
</body>
</html>
