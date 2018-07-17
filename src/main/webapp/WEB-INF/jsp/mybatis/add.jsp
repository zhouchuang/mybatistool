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
    .show{
        display: block;
    }
    .hide{
        display: none;
    }
</style>


<div class="container-fluid" style="padding-left: 0px;!important;" >
    <button type="button" id="add" class="btn btn-success" data-toggle="modal" data-target=".bs-example-modal-lg">新增</button>
    <button type="button" id="del" class="btn btn-danger">删除</button>
    <button type="button" id="gen" class="btn btn-primary" data-url="/TableController/generalDao">生成</button>
</div>

<div class="container" style="margin-top: 20px;height: 90%;overflow-y: auto">
    <ul id="tree" style="margin-left: 15px;">
    </ul>
</div>

<div id="tablepanel" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title" id="title">请选择表后再选择关联列</h3>
            </div>
            <div class="panel-body">
                <ul class="list-group col-md-6" id="tables">

                </ul>
                <ul class="list-group col-md-6" id="fields">

                </ul>
            </div>
        </div>

    </div>
</div>
<script>
    var tablelist = [];
    var tabletemplate = "<li class=\"tablename\" ><input type=\"checkbox\" value=\"@{name}\" data-id=\"@{id}\" data-ref=\"@{ref}\"  ><label >@{name}</label></li>";
    var fieldtemplate = "<li><input type=\"checkbox\" value=\"@{name}\" ><label style='color:#909090'>@{name}</label></li>";
    $('#add').click(function(){
        addTable();
    });

    function addTable(comp){
        var last = comp||$("#tree input:checked").last();
        var appendDom  = $("#tree") ;
        var id ;
        var refId;
        if(last==undefined || last.length==0){
//            $("#title").text("请选择关联id后点击添加");
            bttool.alert("请选择关联id后点击添加");
        }else{
            id = last.val();
            appendDom = last.closest("ul");
            $("#title").text("关联对象 "+last.closest("ul").prev().text()+"("+last.val()+")");
        }
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
                $.getJSON("/TableController/FieldList?table="+table,function (data) {
                    data = data.data;
                    var fieldhtml = "";
                    var html = "";
                    for(var key in data){
                        fieldhtml +=  "<li class=\"list-group-item\">"+data[key].Field+"</li>";
                        html  += fieldtemplate.replace("@{name}",data[key].Field).replace("@{name}",data[key].Field);
                    }
                    $("#fields").html(fieldhtml);
                    $("#fields").off("click");
                    $("#fields").on('click','li',function(){
                        refId = $(this).text();
                        var lihtml  =  tabletemplate.replace("@{name}",table).replace("@{name}",util.getFirstUp(table)).replace("@{id}",id).replace("@{ref}",refId);
                        appendDom.append(lihtml+"<ul>"+html+"</ul>");
                        initTree();
                    });
                });
            });
        });
    }
    function initTree(){
        $("#tablepanel").modal('hide');
        $("#tree input:checked").prop("checked",false);
    }

    $("#del").click(function(){
       $("#tree input:checked").each(function(i,item){
           $(this).parent().remove();
       });
    });
    
    $("#tree").on('click',"li[class='tablename']",function () {
        $(this).next().toggleClass("hide",1000);
    });

    $("#tree").on('change','input:checked',function(){
        addTable($(this));
    });
    
    $("#gen").click(function () {
        var firsttables =  $("#tree>li[class='tablename']");
        function generalLeftTable(tables){
            var lefttables = [];
            tables.each(function(index,item){
                var tableDom = $(item);
                var table = {
                    tableName:tableDom.children("input").val(),
                    fields:getFieldlist(tableDom)
                }
                var childs =  tableDom.next("ul").children("li[class='tablename']");
                if(childs.length>0){
                    table.leftTables =  generalLeftTable(childs);
                }
                var lefttable = {
                    refId:tableDom.children("input").data("id"),
                    revId:tableDom.children("input").data("ref"),
                    table:table
                }
                lefttables.push(lefttable);
            });
            return lefttables;
        }
        function getFieldlist(tableDom){
            var fields = [];
            tableDom.next("ul").children("li:not([class='tablename'])").each(function(index,item){
                fields.push({key:$(item).children("input").val()});
            });
            return fields;
        }
        var lefttable = generalLeftTable(firsttables);
        $(this).btPost(lefttable[0],function(result){

        })

    });
</script>
</body>
</html>
