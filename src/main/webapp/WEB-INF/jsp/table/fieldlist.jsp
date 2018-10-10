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
    <table class="table table-bordered" >
        <tr>
            <th>Name</th>
            <th>JDBC Type</th>
            <th>JAVA Type</th>
            <th>Comment</th>
        </tr>
        <script  id="fieldlist" type="text/html" data-url="/TableController/FieldList?table={table}">
            {{each list as table}}
            <tr>
                <td>{{table.columnName}}</td>
                <td>{{table.columnType}}</td>
                <td>{{table.type}}</td>
                <td>{{table.remarks}}</td>
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
