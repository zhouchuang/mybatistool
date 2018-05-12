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
<div class="panel panel-default">
    <!-- Default panel contents -->
    <div class="panel-heading" id="tableName">Panel heading</div>

    <!-- Table -->
    <table class="table" >
        <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Comment</th>
            <th>Null</th>
            <th>Privileges</th>
        </tr>
        <script  id="fieldlist" type="text/html" data-url="/TableController/FieldList?table={table}">
            {{each list as table}}
            <tr>
                <td>{{table.Field}}</td>
                <td>{{table.Type}}</td>
                <td>{{table.Comment}}</td>
                <td>{{table.Null}}</td>
                <td>{{table.Privileges}}</td>
            </tr>
            {{/each}}
        </script>

    </table>
</div>
<script>
    $("#tableName").text(util.storage.get("table"));
    $("#fieldlist").loadData();
</script>
</body>
</html>
