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
<%--<div class="btn-group-vertical" role="group" aria-label="...">
    <script id="tables" type="text/html" data-url="/TableController/TableList?database=tool">
        {{each list as table}}
        <div class="btn-group" role="group">
            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                {{table}}
            </button>
            <ul class="dropdown-menu">

            </ul>
        </div>
        {{/each}}
    </script>
</div>--%>
<div class="container-fluid">
    <div class="row">
        <button>事实上</button>
        <button>事实上</button>
        <button>事实上</button>

        <button>事实上</button><button>事实上</button>
        <button>事实上</button><button>事实上</button>
        <button>事实上</button><button>事实上</button>
        <button>事实上</button><button>事实上</button>
        <button>事实上</button><button>事实上</button>
        <button>事实上</button><button>事实上</button>
    </div>
</div>
<script>
    /*$("#tables").loadData().then(function(result){
        $(".btn-group-vertical").on('click','button',function(){
            var _this = $(this);
            if(_this.next().children().length==0){
                $.getJSON('/TableController/FieldList?table='+_this.text(),function(data){
                    var html  = "";
                    $.each(data.data,function(i,item){
                        html += "<li><a href=\"javascript:void(0)\">"+item.Field+"</a></li>";
                    });
                    _this.next().html(html);
                });
            }
        });
    });*/
</script>
</body>
</html>
