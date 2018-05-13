<%--
  Created by IntelliJ IDEA.
  User: zhouchuang
  Date: 2018/5/12
  Time: 19:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<style>
    ul{
        list-style:none;
    }
</style>


<div class="container-fluid">
    <button type="button" id="add" class="btn btn-success" data-toggle="modal" data-target=".bs-example-modal-sm">新增</button>
    <button type="button" id="del" class="btn btn-danger">删除</button>
</div>

<div class="container" style="margin-top: 20px;">
    <ul id="tree" style="margin-left: 15px;">
    </ul>
</div>

<div id="tablepanel" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content" >
            <ul class="list-group" id="tables">

            </ul>
        </div>
    </div>
</div>
<script>
    var tablelist = [];
    var template = "<li><input type=\"checkbox\" value=\"@{name}\" ><label>@{name}</label></li>";
    $('#add').click(function(){
        new Promise(function(callback){
            if(tablelist.length==0) {
                $.getJSON("/TableController/TableList?database=tool",function(data){
                    tablelist = [].concat(data.data);
                    callback(tablelist);
                });
            }else{
                callback(tablelist);
            }
        }).then(function(result){
            var html = "";
            for(var key in result){
                html += "<li class=\"list-group-item\">"+result[key]+"</li>";
            }
            $("#tables").html(html);
        }).then(function () {
            $("#tables").on('click','li',function () {
                var _this = $(this);
                var table = $(this).text();
                var lihtml  =  template.replace("@{name}",table).replace("@{name}",table);
                $("#tree").append(lihtml);
                $.getJSON("/TableController/FieldList?table="+table,function (data) {
                    data = data.data;
                    var html = "";
                    for(var key in data){
                        html  += template.replace("@{name}",data[key].Field).replace("@{name}",data[key].Field);
                    }
                    $("#tree li:last").append("<ul>"+html+"</ul>");

                });
                $("#tablepanel").modal('hide');
            });
        });
    });
</script>
</body>
</html>
