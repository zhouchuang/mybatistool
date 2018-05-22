/**
 * Created by zhouchuang on 2018/5/22.
 */
var bttool = {
    alert:function (msg) {
        $("#alertPanel span").text(msg);
        $("#alertPanel").show(1000);
        var num = setTimeout(function(){
            $("#alertPanel").hide(1000);
        },3000);
    }
}