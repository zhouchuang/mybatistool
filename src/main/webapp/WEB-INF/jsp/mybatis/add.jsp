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
    .success{
        background-color:#5cb85c;
    }
    label{
        margin-right: 5px;
    }
    .xdirection{
        display:inline-block;
    }
    .xdirection ul li{
        float: left;
        margin-left: 10px;
    }
    .code{
        width: 100%;
        height: 100px;
    }

</style>


<div class="container-fluid" style="padding-left: 0px;!important;" >
<%--
    <button type="button" id="add" class="btn btn-success" data-toggle="modal" data-target=".bs-example-modal-lg">新增</button>
--%>
    <button type="button" id="del" class="btn btn-danger">删除</button>
    <button type="button" id="gen" class="btn btn-primary" data-url="/TableController/generalDao">生成</button>
</div>

<div class="container" style="margin-top: 20px;height: 90%;overflow-y: auto">
    <ul id="tree" style="margin-left: 15px;">
    </ul>
    <div id="sql"style="background-color: #3c3c3c;color:black;height: auto;width: 80%" class="row">
        <textarea class="code" id="select">select </textarea>
        <textarea class="code"  id="from"></textarea>
        <textarea class="code"  id="where">where 1=1 </textarea>
    </div>
</div>

<div id="tablepanel" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title" id="title">请选择表后再选择关联列</h3>
            </div>
            <div class="btn-group" role="group" id="charPanel" >
            </div>
            <div class="panel-body"  style="height: 80%">

                <div class="list-group col-md-6">
                    <ul id="tableUl"  style="height:100%;overflow:scroll">
                        <script  id="tables"  type="text/html"  data-url="/TableController/TableListDetail">
                            {{each list as table index}}
                            <li class="list-group-item" data-table="{{table.name}}" data-file="{{table.status==true?1:0}}">
                                <span  class="badge general {{table.status==1?'success':'noexist' }}">{{table.status==0?'未生成':'已生成'}}</span>
                                <span >{{table.name}}</span>
                            </li>
                            {{/each}}
                        </script>
                    </ul>
                </div>

                <div class="list-group col-md-6"  >
                    <ul id="fields"  style="height:100%;overflow:scroll">

                    </ul>
                </div>

            </div>
        </div>

    </div>
</div>


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
<script>


    var selectmap =  {};
    var tabletemplate = "<li class=\"tablename\" ><input type=\"checkbox\" value=\"@{name}\" data-id=\"@{id}\" data-status=\"@{status}\" data-ref=\"@{ref}\"  ><label >@{name}</label><span class=\"badge\">@{file}</span></li>";
    var fieldtemplate = "<li><input type=\"checkbox\" value=\"@{name}\" ><label style='color:#909090'>@{name}</label></li>";
    var html = "";
    var table = "";
    var file;
    var id = "";
    var tableName = "";
    var refId = "";
    var appendDom;
    var comp;
    function  init(){



        for(var i=0;i<26;i++){
            $("#charPanel").append("<button type=\"button\" class=\"btn btn-default\">"+String.fromCharCode(0x60+i+1)+"</button>");
        }
        $("#charPanel").on('click','button',function(){
            $("#fields").empty();
            load($(this).text());
        });
        $("#tableUl").on('click','li',function () {

            $("#tableUl li").removeClass("active");
            $(this).addClass("active");
            var _this = $(this);
            table = $(this).data("table");
            file = $(this).data("file")==1?true:false;
            $.getJSON("/TableController/FieldList?table="+table,function (result) {
                var data = result.data.list;
                var status = result.data.status;
                var fieldhtml = "";
                html = "";
                for(var i in data){
                    var obj = data[i];
                    obj.remarks = obj.remarks||"";
                    obj.remarks = obj.remarks.substring(0,Math.min(10,obj.remarks.length));
                    fieldhtml +=  "<li class=\"list-group-item\"><span class=\"refID\">"+obj.columnName+"</span><span class='badge'>"+obj.remarks+"</span></li>";
                    html  += fieldtemplate.replace("@{name}",obj.columnName).replace("@{name}",obj.columnName);
                }
                $("#fields").html(fieldhtml);
                $("#fields").off("click");
                $("#fields").on('click','li',function(){
                    refId = $(this).children(".refID").text();
                    var sql ;
                    if(!tableName){
                        sql  = "from  "+table +" "+table;
                    }else{
                        sql  = "left join "+table+" on "+tableName+"."+id+" = "+table+"."+refId;
                    }
                    var lihtml  =  tabletemplate.replace("@{name}",table)
                        .replace("@{name}",util.getFirstUp(table))
                        .replace("@{id}",id)
                        .replace("@{ref}",refId)
                        .replace("@{status}",status)
//                        .replace("@{file}",file==true?"已生成":"未生成")
                        .replace("@{file}",sql);
//                        .replace("@{filestatus}",file==true?"success":"");
                    appendDom.append(lihtml+"<li class=\"fieldsNode\"><ul class=\"appendNode\"><li class=\"xdirection\"><ul>"+html+"</ul></li></ul></li>");
                    initTree();
                    appendSql("#from",sql);
                });
            });
        });

      /*  $("#fields").on('click','li',function(){
            refId = $(this).text();
            var lihtml  =  tabletemplate.replace("@{name}",table)
            //.replace("@{name}",(util.getFirstUp(table)+(status==true?"<span class=\"glyphicon glyphicon-th-list\" aria-hidden=\"true\"></span>":"")))
                .replace("@{name}",util.getFirstUp(table))
                .replace("@{id}",id)
                .replace("@{ref}",refId)
                .replace("@{status}",status)
                .replace("@{file}",file==true?"已生成":"未生成")
                .replace("@{filestatus}",file==true?"success":"");
            appendDom.append(lihtml+"<ul>"+html+"</ul>");
            initTree();
        });
*/

        load('a');
    }
    //加载行
    init();



    var tablelist = [];
    $('#add').click(function(){
        //load('a');
    });

    function load(key){
        initSelectTable();
        $("#tables").loadData({pageSize:100,currentPage:1,condition:{key:key}}).then(function(result){

        });
    }




  /*  function addTable(comp){
        var last = comp||$("#tree input:checked").last();
        var appendDom  = $("#tree") ;
        var id ;
        var refId;
        if(last==undefined || last.length==0){
            bttool.alert("请选择关联id后点击添加");
        }else{
            id = last.val();
            appendDom = last.closest("ul");
            $("#title").text("关联对象 "+last.closest("ul").prev().text()+"("+last.val()+")");
        }
        new Promise(function(callback){
            if(tablelist.length==0) {
                $.getJSON("/TableController/TableListDetail",function(data){
                    tablelist = [].concat(data.data);
                    callback(tablelist);
                });
                load();
            }else{
                callback(tablelist);
            }
        }).then(function(result){
            var html = "";
            for(var i in result){
                var obj = result[i];
                for(var key in obj){
                    html += "<li data-name=\""+key+"\" data-file=\""+obj[key]+"\" class=\"list-group-item\"><span class=\"badge"+(obj[key]==true?" success":"")+"\" >"+(obj[key]==true?"已生成":"未生成")+"</span>"+key+"</li>";
                }
            }
            $("#tables").html(html);
        }).then(function () {
            $("#tables").on('click','li',function () {
                var _this = $(this);
                var table = $(this).data("name");
                var file = $(this).data("file");
                $.getJSON("/TableController/FieldList?table="+table,function (result) {
                    var data = result.data.list;
                    var status = result.data.status;
                    var fieldhtml = "";
                    var html = "";
                    for(var i in data){
                        var obj = data[i];
                        fieldhtml +=  "<li class=\"list-group-item\">"+obj.columnName+"</li>";
                        html  += fieldtemplate.replace("@{name}",obj.columnName).replace("@{name}",obj.columnName);
                    }
                    $("#fields").html(fieldhtml);
                    $("#fields").off("click");
                    $("#fields").on('click','li',function(){
                        refId = $(this).text();
                        var lihtml  =  tabletemplate.replace("@{name}",table)
                            //.replace("@{name}",(util.getFirstUp(table)+(status==true?"<span class=\"glyphicon glyphicon-th-list\" aria-hidden=\"true\"></span>":"")))
                            .replace("@{name}",util.getFirstUp(table))
                            .replace("@{id}",id)
                            .replace("@{ref}",refId)
                            .replace("@{status}",status)
                            .replace("@{file}",file==true?"已生成":"未生成")
                            .replace("@{filestatus}",file==true?"success":"");
                        appendDom.append(lihtml+"<ul>"+html+"</ul>");
                        initTree();
                    });
                });
            });
        });
    }*/

    function  appendSql(eleID,sql){
        var panel = $(eleID);
        panel.html(panel.html()+" "+ sql);
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
        if($(this).next().find(".xdirection").css("display")!="none"){
            $(this).next().find(".xdirection:first").hide();
        }else{
            $(this).next().find(".xdirection:first").show();
        }
    });

    $("#tree").on('change','input',function(){
        var ctn = $(this).closest(".fieldsNode").prev().find("input").val();
        var ct = selectmap[ctn]||[];
        if($(this).is(":checked")) {
            ct.push($(this).val());
        }else{
            //删除
        }
        selectmap[ctn] = ct;
    });

    $("#tree").on('dblclick','label',function(){
        comp = $(this).prev();
        load('a');
    })

    function initSelectTable(){
        $(".bs-example-modal-lg").modal("show");
        var last = comp||$("#tree input:checked").last();
        appendDom  = $("#tree") ;
        if(last==undefined || last.length==0){
            //bttool.alert("请选择关联id后点击添加");
        }else{
            id = last.val();
            appendDom = last.closest(".fieldsNode");
            tableName = appendDom.prev().find("input").val();
            appendDom = appendDom.find(".appendNode");
            $("#title").text("关联对象 "+last.closest("ul").prev().text()+"("+last.val()+")");
        }

    }
    
    $("#gen").click(function () {
        var firsttables =  $("#tree>li[class='tablename']");
        function generalLeftTable(tables){
            var lefttables = [];
            tables.each(function(index,item){
                var tableDom = $(item);
                var table = {
                    tableName:tableDom.children("input").val(),
                    status:tableDom.children("input").data("status"),
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
            if(result.code==200){
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

        })

    });
</script>
</body>
</html>
