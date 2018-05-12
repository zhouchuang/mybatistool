/**
 * Created by zhouchuang on 2018/2/12.
 */

document.write("<style>.close-upimg {\n" +
    "    display: none;\n" +
    "    position: absolute;\n" +
    "    top: 6px;\n" +
    "    right: 8px;\n" +
    "    z-index: 10;\n" +
    "}\n" +
    ".up-span {\n" +
    "    display: none;\n" +
    "    width: 100%;\n" +
    "    height: 100%;\n" +
    "    position: absolute;\n" +
    "    top: 0px;\n" +
    "    left: 0px;\n" +
    "    z-index: 9;\n" +
    "    background: rgba(0, 0, 0, 0.5);\n" +
    "}</style>")
var attachment  = {
    mask:"<div class=\"loading\">\n" +
    "        <div class=\"spinner\">\n" +
    "            <div class=\"spinner-container container1\">\n" +
    "                <div class=\"circle1\"></div>\n" +
    "                <div class=\"circle2\"></div>\n" +
    "                <div class=\"circle3\"></div>\n" +
    "                <div class=\"circle4\"></div>\n" +
    "            </div>\n" +
    "            <div class=\"spinner-container container2\">\n" +
    "                <div class=\"circle1\"></div>\n" +
    "                <div class=\"circle2\"></div>\n" +
    "                <div class=\"circle3\"></div>\n" +
    "                <div class=\"circle4\"></div>\n" +
    "            </div>\n" +
    "            <div class=\"spinner-container container3\">\n" +
    "                <div class=\"circle1\"></div>\n" +
    "                <div class=\"circle2\"></div>\n" +
    "                <div class=\"circle3\"></div>\n" +
    "                <div class=\"circle4\"></div>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>",
    form:{},
    Type:{
        'Head':'1','Certificate':'2','Photos':'3'
    },
    Path:{
        'Head':'iJob/images/head','Certificate':'iJob/images/certi','Photos':'iJob/images/photos'
    },
    Able:["upload","delete","change"],
    clickhandler:function(event){
        event.preventDefault();
        $(event.target).next().click();
    },
    deleteImage:function(event){
        $(event.target).parent().parent().trigger('deleteEvent');
        $(event.target).parent().remove();

    },
    selectImage:function(name,refid,type,editable){
        form = new FormData();//通过HTML表单创建FormData对象
        var files = document.getElementById('pic_'+name).files;
        if(files.length == 0){
            return;
        }
        var file = files[0];
        //把上传的图片显示出来
        var reader = new FileReader();
        // 将文件以Data URL形式进行读入页面
        reader.readAsBinaryString(file);
        reader.onload = function(f){
            var src = "data:" + file.type + ";base64," + window.btoa(this.result);
            //result.innerHTML = '<img src ="'+src+'"/>';
            $("[data-name='"+name+"']:first").attr('src',src);
            $("body").before(attachment.mask);
        }
        form.append('file',file);
        // document.getElementById("submitBtn").removeAttribute("hidden");
        attachment.uploadFile(name,refid,type);
        //如果是可编辑 ，则可以让删除
        if(editable=="true"){
            $("[data-name='"+name+"']:first").unbind();
            $("[data-name='"+name+"']").parent().on('click',function(){
                var displayCss = "block";
                if($(this).find(".up-span:first").css("display")=="block"){
                    displayCss  = "none";
                }
                $(this).find(".up-span:first").css("display",displayCss);
                $(this).find(".close-upimg:first").css("display",displayCss);
            });
        }

    },
    uploadFile:function(name,refid,type){
        $("[data-name='"+name+"']:first").parent().find("div[name='resultDiv']").remove();
        if(type=="Head"){
            form.append("type",attachment.Type.Head);
            form.append("path",attachment.Path.Head)
        }else if(type=="Certificate"){
            form.append("type",attachment.Type.Certificate);
            form.append("path",attachment.Path.Certificate)
        }else if(type=="Photos"){
            form.append("type",attachment.Type.Photos);
            form.append("path",attachment.Path.Photos)
        }
        form.append("id",refid);
        $.ajax({
            url: '/fileUpload/uploadImage',
            type: "post",
            data: form,
            processData: false,
            contentType: false,
            success: function (data) {
                mui.toast("上传完成");
                var temp = "<input name='${name}' value='${value}' type='hidden'  />";
                var attaHtml  = "";
                for( key in data){
                    attaHtml  += temp.replace("${name}",name+"."+key).replace("${value}",data[key]);
                }
                $("input[id='pic_"+name+"']").after("<div name='resultDiv'>"+attaHtml+"</div>");
            },
            beforeSend: function(){
                $("[data-name='"+name+"']:first").trigger('uploadStartEvent');
            },
            complete: function(){
                $("[data-name='"+name+"']:first").trigger('uploadCompletionEvent');
                $("body").prev().remove();
            }
        });
    }
};


(function($){
    $.fn.attachment  = function(){
        var _this = $(this);
        if(_this.hasClass("attachment")){
            render(0,_this);
        }else{
            _this.find(".attachment").each(function(i,item){
                render(i,item);
            });
        }
        function render(i,item){
            if($(item).get(0).tagName == 'IMG'){
                var name = $(item).data("name");
                var refid = $(item).data("id");
                var type = $(item).data("type");
                var capture=$(item).data("capture");
                var editable = $(item).data("editable");
                $(item).on('click',attachment.clickhandler);//绑定事件 <button onclick = 'uploadFile()' hidden='hidden' id='submitBtn'>提交</button>
                $(item).before("<span class=\"up-span\"></span><img class=\"close-upimg\" onclick='attachment.deleteImage(event)' src=\"/static/images/a7.png\">");
                if(capture==true){
                    $(item).after("<input id='pic_"+name+"' type='file' name = 'pic' accept = 'image/*' capture='camera' onchange = \"attachment.selectImage(\'"+name+"\',\'"+refid+"\',\'"+type+"\',\'"+editable+"\')\" hidden='hidden'/>");
                }else{
                    $(item).after("<input id='pic_"+name+"' type='file' name = 'pic' accept = 'image/*' onchange = \"attachment.selectImage(\'"+name+"\',\'"+refid+"\',\'"+type+"\',\'"+editable+"\')\" hidden='hidden'/>");
                }

            }else if($(item).get(0).tagName == 'INPUT'){

            }
        }
    }
})(jQuery);

    /*$(".attachment").each(function(i,item){
        if($(item).get(0).tagName == 'IMG'){
            var name = $(item).data("name");
            var refid = $(item).data("id");
            var type = $(item).data("type");
            $(item).on('click',attachment.clickhandler);//绑定事件 <button onclick = 'uploadFile()' hidden='hidden' id='submitBtn'>提交</button>
            $(item).after("<input id='pic_"+name+"' type='file' name = 'pic' accept = 'image/!*' onchange = \"attachment.selectImage(\'"+name+"\',\'"+refid+"\',\'"+type+"\')\" hidden='hidden'/>");
        }else if($(item).get(0).tagName == 'INPUT'){

        }
    });*/

