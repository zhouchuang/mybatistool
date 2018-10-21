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
        background-color:#5cb85c!important;
    }
    .noexist{

    }
</style>
<body>

<!-- 模态框（Modal） -->
<div class="modal fade  bs-example-modal-sm" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    下载生成代码包
                </h4>
            </div>
            <div class="modal-body">

            </div>
            <div class="progress">
                <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">
                    0%
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" id="download" class="btn btn-default" >下载
                </button>
                <button type="button" class="btn btn-primary">取消</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<div class="modal fade " id="myField" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" >
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myTableName">

                </h4>
            </div>
            <div class="modal-body" id="myFieldContent">

            </div>
        </div>
    </div>
</div>


<div class="container">
    <div class="btn-toolbar" role="toolbar" >
        <div class="btn-group" role="group" id="charPanel" >
        </div>
    </div>
    <ul class="list-group" id="tablePanel">
        <script id="tablelist" type="text/html" data-url="/TableController/TableListDetail">
            {{each list as table index}}
                <li class="list-group-item">
                    <span data-table="{{table.name}}" class="badge general {{table.status==1?'success':'noexist' }}">{{table.status==0?'未生成':'已生成'}}</span>
                    <a>{{table.name}}</a>
                </li>
            {{/each}}
        </script>
    </ul>
    <%--<nav aria-label="Page navigation">
        <ul class="pagination">
            <li>
                <a href="#" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li><a href="#">1</a></li>
            <li><a href="#">2</a></li>
            <li><a href="#">3</a></li>
            <li><a href="#">4</a></li>
            <li><a href="#">5</a></li>
            <li>
                <a href="#" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
    </nav>--%>
</div>



<script id="field" type="text/html">
    {{each list as table}}
    <tr>
        <td>{{table.columnName}}</td>
        <td>{{table.columnType}}</td>
        <td>{{table.type}}</td>
        <td>{{table.remarks}}</td>
    </tr>
    {{/each}}
</script>
<script>

    function  init(){
        for(var i=0;i<26;i++){
            $("#charPanel").append("<button type=\"button\" class=\"btn btn-default\">"+String.fromCharCode(0x60+i+1)+"</button>");
        }
        $("#charPanel").on('click','button',function(){
            load($(this).text());
        });
        load('a');
    }
    init();

    function load(key){
        $("#tablelist").loadData({pageSize:100,currentPage:1,condition:{key:key}}).then(function(result){

        });
    }


    $("#tablePanel").on('click','a',function(){
        var _this = $(this);
        $.getJSON("/TableController/FieldList?table="+_this.text(),function(result){
            result.data.list.splice()
            var html = template("field", result.data);
            html="<table class=\"table table-bordered\" >\n" +
                "                                <tr>\n" +
                "                                    <th>Name</th>\n" +
                "                                    <th>JDBC Type</th>\n" +
                "                                    <th>JAVA Type</th>\n" +
                "                                    <th>Comment</th>\n" +
                "                                </tr>\n" +
                html +
                "                             </table>";
            $("#myFieldContent").html(html);
            $("#myTableName").text(_this.text());
            $("#myField").modal("show");
        });
    });

    $("#tablePanel").on('click','span',function(){
        var _this = $(this);
        var lefttable = {
            table:{
                tableName:_this.data("table")
            }
        }
        $(this).btPost("/TableController/generalDao",lefttable,function (result) {
            if(result.code==200){
                _this.addClass("success");
                var temp  = result.data.files;
                var files = "<ul class=\"list-group\">";
                for(var i in temp){
                    var t  = temp[i];
                    files +=  "<li class=\"list-group-item\"><span class=\"badge btn-primary\">"+t.extName+"</span>"+t.className+"</li>";
                }
                files+="</ul>";
                $(".modal-body").html(files);
                $("#myModal").modal("show");
                $("#download").unbind().click(function(){
                    window.location.href = "/TableController/download/"+result.data.key;
                    var i = 0;
                    var inter  =  setInterval(function(){
                        i+=5;
                        $(".progress-bar").css("width",i + "%").text(i + "%");
                        if(i==100){
                            clearInterval(inter);
                            $("#myModal").modal("hide");
                        }
                    },100);
                });
            }
        });
    });
</script>
</body>
</html>
