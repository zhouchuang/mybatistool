var util = {

    mask: "<div id='myLoading' class=\"modal-backdrop fade in\" style='text-align: center;'><img style=\"position: absolute;top: 50%;left: 50%;\" src=\"/static/images/loading.gif\"></div>",
    isNull: function (str) {
        if (typeof(str) == "string"){
            var reg = /^\s*$/g;
            if (str == "" || reg.test(str)) {
                return true;
            }
            return false;
        }else if(typeof(str) == "object"){
            var flag = true;
            for(var key in str){
                if(str[key]){
                    flag = false;
                    break;
                }
            }
            return flag;
        }

    },
    getFirstUp:function(str){
        return str.substring(0,1).toLocaleUpperCase()+str.substring(1,str.length);
    },
    isArray:function(o){
        return Object.prototype.toString.call(o)=='[object Array]';
    },
    isNotNull: function (str) {
        return !this.isNull(str);
    },
    getDateList: function (datetime) {
        var arr = [];
        if (datetime) {
            var datetime = JSON.parse(datetime);
            for (var key in datetime) {
                var year = datetime[key];
                for (var mkey in year) {
                    var month = year[mkey];
                    for (var dkey in month) {
                        arr.push(key + "-" + mkey + "-" + dkey);
                    }

                }
            }
        }
        return arr;
    },
    getStrFromList: function (arr) {
        var datejson = {};
        for (i in arr) {
            var date = arr[i];
            var year = date.split('-')[0];
            var month = date.split('-')[1];
            var day = date.split('-')[2];
            if (!datejson.hasOwnProperty(year)) {
                datejson[year] = {};
            }
            if (!datejson[year].hasOwnProperty(month)) {
                datejson[year][month] = {};
            }
            datejson[year][month][day] = true;
        }
        return datejson;
    },
    getDateFromString: function (date) {
        var year = date.toString().split("-")[0];
        var month = date.toString().split("-")[1];
        var day = date.toString().split("-")[2];
        return new Date(year, month, day);
    },
    setCookie: function (name, value, days) {    //封装一个设置cookie的函数
        var oDate = new Date();
        oDate.setDate(oDate.getDate() + days);   //days为保存时间长度
        document.cookie = name + '=' + encodeURIComponent(value) + ';expires=' + oDate;
    },
    getCookie: function (name) {
        var arr = document.cookie.split(';');
        for (var i = 0; i < arr.length; i++) {
            var arr2 = arr[i].split('=');
            if (arr2[0] == name) {
                return decodeURIComponent(arr2[1]);  //找到所需要的信息返回出来
            }
        }
        return '';        //找不到就返回空字符串
    },
    getBaseConfig: function (name) {
        //读取
        str = sessionStorage.obj;
        //如果不存在，则主动请求获取
        if (str == undefined) {
            htmlobj = $.ajax({url: "/getBaseConfig", async: false});
            this.setBaseConfig(htmlobj.data);
            str = sessionStorage.obj;
        }
        //重新转换为对象
        obj = JSON.parse(str);
        return obj[name];

    },
    setBaseConfig: function (obj) {
        var str = JSON.stringify(obj);
        //存入
        sessionStorage.obj = str;
    },
    dateFormat: function (date, format) {
        if(!date)return '';
        if (typeof date === "string") {
            var mts = date.match(/(\/Date(\d+)\/)/);
            if (mts && mts.length >= 3) {
                date = parseInt(mts[2]);
            }
        }
        date = new Date(date);
        if (!date || date.toUTCString() == "Invalid Date") {
            return "";
        }

        var map = {
            "M": date.getMonth() + 1, //月份
            "d": date.getDate(), //日
            "h": date.getHours(), //小时
            "m": date.getMinutes(), //分
            "s": date.getSeconds(), //秒
            "q": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds() //毫秒
        };


        format = format.replace(/([yMdhmsqS])+/g, function (all, t) {
            var v = map[t];
            if (v !== undefined) {
                if (all.length > 1) {
                    v = '0' + v;
                    v = v.substr(v.length - 2);
                }
                return v;
            }
            else if (t === 'y') {
                return (date.getFullYear() + '').substr(4 - all.length);
            }
            return all;
        });
        return format;
    },
    goPreAndRefresh: function (msg) {
        mui.toast(msg || "操作成功");
        setTimeout(function () {
            self.location = document.referrer;
        }, 1500);
    },
    storage: {
        private: {
            write: function (data) {
                data = data || {data: {}};
                sessionStorage.setItem("param", JSON.stringify(data));
            },
            read: function () {
                var objs = sessionStorage.getItem("param") || JSON.stringify({data: {}});
                return JSON.parse(objs);
            },
            setObjByDepth: function (param, keys, value) {
                function setObj(obj, key) {
                    if (!obj.hasOwnProperty(key)) {
                        obj [key] = {};
                    }
                }

                if (keys.length == 1) {
                    param.data[keys[0]] = value;
                }
                if (keys.length == 2) {
                    setObj(param.data, keys[0]);
                    param.data[keys[0]][keys[1]] = value;
                }
                if (keys.length == 3) {
                    setObj(param.data, keys[0]);
                    setObj(param.data[keys[0]], keys[1]);
                    param.data[keys[0]][keys[1]][keys[2]] = value;
                }
            }
        },
        merge: function (data) {
            if (!data.hasOwnProperty("data")) {
                data = {data: data};
            }
            var obj = util.storage.private.read();
            if (!(obj && obj.hasOwnProperty("data"))) {
                obj = {data: {}};
            }
            var temp = $.extend(obj.data, data.data);
            obj.data = temp;
            if (data.path) obj.path = data.path;
            if (data.url) obj.url = data.url;
            util.storage.private.write(obj);
        },
        set: function (key, value) {
            if (key.indexOf("data") == 0) {
                key = key.replace("data.", "");
            }
            var obj = util.storage.private.read();
            var keys = key.split(".");
            util.storage.private.setObjByDepth(obj, keys, value);
            sessionStorage.setItem('param', JSON.stringify(obj));
        },
        get: function (key) {
            var index = -1;
            if(key.indexOf("[")!=-1){
                var indexstr = key.substring(key.indexOf("["),key.indexOf("]")+1);
                key = key.replace(indexstr,"");
                index = parseInt(indexstr.substring(1,indexstr.length-1));
            }
            if (key.indexOf("data") == 0) {
                key = key.replace("data.", "");
            }
            var keys = key.split(".");
            var obj = util.storage.private.read().data;
            for (var i in keys) {
                if (obj.hasOwnProperty(keys[i])) {
                    obj = obj[keys[i]];
                } else {
                    return null;
                }
            }
            if(index>-1){
                return obj[index];
            }else{
                return obj;
            }

        },
        data: function () {
            return util.storage.private.read();
        },
        clear: function () {
            var mylocaltion = util.storage.get("mylocaltion");
            var menu = util.storage.get("menu");
            sessionStorage.clear();
            util.storage.set("mylocaltion",mylocaltion);
            util.storage.set("menu",menu);
        }

    },
    url: {
        private: {
            gotoPage: function (param, div, callback) {
                if (param.path) {
                    if (div) {
                        util.url.history.record(param.path);
                        div.load("/forward?path=" + param.path, callback);
                    } else {
                        window.location.href = "/forward?path=" + param.path;
                    }
                } else if (param.url) {
                    if (div) {
                        util.url.history.record(param.url);
                        div.load(param.url, callback);
                    } else {
                        window.location.href = param.url;
                    }
                }
            }
        },
        next: {
            call: function (id) {
                var url = util.storage.private.read().next+"/"+id;
                if (url) {
                    util.url.next.clear();
                    $.getJSON(url, function (data) {
                        util.goPreAndRefresh();
                    });
                }
            },
            set: function (url) {
                var obj = util.storage.private.read();
                obj.next = url;
                util.storage.private.write(obj);
            },
            clear: function () {
                util.url.next.set("");
            }
        },
        to: function (param) {
            var url = param.url || param.path;
            if (url.indexOf("{") != -1) {
                url = util.url.from(url);
            }
            if (url.indexOf("?") != -1) {
                var paramstr = url.substring(url.indexOf("?") + 1, url.length);
                var paramarr = paramstr.split("&");
                for (var i in paramarr) {
                    var obj = paramarr[i].split("=");
                    util.storage.set(obj[0], obj[1]);
                }
                if (param.url) {
                    param.url = url.split("?")[0];
                } else {
                    param.path = url.split("?")[0];
                }
            }
            if (!param.hasOwnProperty("data")) {
                param.data = {};
            }
            util.storage.merge(param);
            return param;
        },
        from: function (url) {
            try{
                while (url.indexOf("{") != -1) {
                    var keystr = url.substring(url.indexOf("{") + 1, url.indexOf("}"));
                    var flag = false;
                    if (keystr.indexOf("data") == 0) {
                        flag = true;
                        keystr = keystr.replace("data.", "");
                    }
                    var value = util.storage.get(keystr);
                    url = url.replace((flag ? "{data." : "{") + keystr + "}", value);
                }
            }catch (err){
                console.log(""+url);
            }

            return url;
        },
        history:{
            urls:[],
            href:'',
            back:function(panel){
                panel.loadPage({path:this.next()})
            },
            record:function (url) {
                this.href = url;
                this.urls.push(url);
            },
            next:function(){
                var url  = this.urls.pop();
                if(url==this.href){
                    return this.next();
                }else{
                    return url;
                }
            }
        }
    },
    location:{
        set:function(latlng){
            var localtion = {
                lng: latlng.lng,
                lat: latlng.lat,
                addr: latlng.addr,
                cityID: site.region.id(latlng.district || latlng.city),
                cityname: latlng.district || latlng.city,
                district:latlng.district,
                city:latlng.city,
                mycity:latlng.city,
                mycityID:site.region.id(latlng.district || latlng.city),
                realCityID:site.region.id(latlng.city)
            };
            util.storage.set("mylocaltion",localtion);
        },
        get:function(){
            return util.storage.get("mylocaltion");
        }
    },
    gotoPage: function (param, event) {
        if (event) {
            event.preventDefault();
        }
        util.url.private.gotoPage(util.url.to(param));

    },
    loadPage:function(param,event){
        $("#panel").loadPage(param);
    },
    delayGotoPage: function (param, msg,time) {
        time = time || 2;
        var shows  = msg||(time + "秒后跳转页面");
        var intervalnum = setInterval(function () {
            --time;
            shows  = msg||(time + "秒后跳转页面");
            if (time <= 0) {
                clearInterval(intervalnum);
                util.gotoPage(param);
            }
        }, 1000);
    },
    back:function(){
        window.history.back();
    },
    reback:function(msg){
        setTimeout(function () {
            self.location=document.referrer;
        }, 1000);
    },
    parseFromFormObject:function(obj,keys){
        for(i in keys){
            var key = keys[i];
            obj[key] = [];
            for(objkey in obj){
                var reg  = new RegExp("^"+ key + "\\d$");
                if(reg.test(objkey)){
                    obj[key].push(obj[objkey]);
                    delete obj[objkey];
                }
            }
        }
        return obj;

    },
    showProcess:function(id,arr,title){
        var canvas=document.getElementById(id);
        canvas.width = 750;
        canvas.height = 200;
        var ctx=canvas.getContext('2d');
        ctx.scale(2,2);
        var width = $("#"+id).width()/2;
        var height = $("#"+id).height()/2;
        var k = 1.6;
        var top = 10;
        if(title){
            ctx.fillStyle="#000";
            ctx.textAlign="left";
            ctx.font  = "12px Arial";
            ctx.fillText(title,10,top+10);
        }
        var r = 12;
        ctx.lineWidth=5;
        for(var i=1;i<arr.length;i++){
            var obj = arr[i];
            if(obj.state==1)
                ctx.strokeStyle="#108EE9";
            else
                ctx.strokeStyle="#999";
            ctx.beginPath();
            ctx.moveTo(width*0.1+width*0.8/2*(i-1),height/5*k+top);
            ctx.lineTo(width*0.1+width*0.8/2*(i),height/5*k+top);
            ctx.stroke();
        }
        for(var  i  in  arr){
            var obj = arr[i];
            if(obj.state==1)
                ctx.fillStyle="#108EE9";
            else
                ctx.fillStyle="#999";
            ctx.beginPath();
            ctx.arc(width*0.1+width*0.8/2*i,height*1/5*k+top,r,0*Math.PI,2*Math.PI);
            ctx.fill();
            ctx.textAlign="center";
            ctx.font = "10px Arial";
            ctx.fillText(obj.name,width*0.1+width*0.8/2*i,height*2/5*k+top);
            if(obj.state==1)ctx.fillText(obj.time,width*0.1+width*0.8/2*i,height/2*k+top);
        }

    },
    showPoint:function(workPlace){
        var center = workPlace.latitude+","+workPlace.longitude;
        var addr = workPlace.detailedAddress;
        window.location.href='http://apis.map.qq.com/tools/poimarker?type=0&marker=coord:'+center+';title:工作地址;addr:'+addr+'&key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77&referer=myapp';
    }
};


//util的一些插件实现
(function ($) {
    //优化多选框  超过10个隐藏起来
    $.fn.showSelect = function () {

        var _this = $(this);
        if (_this.get(0).tagName != "UL") {
            _this = _this.find("ul:first");
        }

        //监听点击事件，如果遇到全部，则选择全部，否则取消全部
        _this.on('click', 'li', function () {
            $(this).toggleClass('active');
            if ($(this).data("id") == "") {
                if ($(this).hasClass("active")) {
                    _this.find("li").addClass("active");
                } else {
                    _this.find("li").removeClass("active");
                }
            } else {
                if (!$(this).hasClass("active")) {
                    _this.find("li").eq(0).removeClass("active");
                }
            }
        });

        var len = _this.find("li").length;
        if (len > 9) {
            _this.find("li:nth-child(9)").nextAll().each(function (i, item) {
                $(item).hide();
            });
            _this.on('showAllEvent', function () {
                _this.find("li:nth-child(9)").nextAll().each(function (i, item) {
                    $(item).show();
                });
            });
            _this.append("<li style='color: #108EE9;'>更多 ∨</li>");
            _this.on('click','li:last',function(){
                var text = $(this).text();
                if(text.indexOf('更多')==0){
                    $(this).remove();
                    _this.find("li:nth-child(9)").nextAll().each(function (i, item) {
                        $(item).show();
                    });
                    _this.append("<li style='color: #108EE9;'>收起 ∧</li>");
                }else{
                    $(this).remove();
                    _this.find("li:nth-child(9)").nextAll().each(function (i, item) {
                        $(item).hide();
                    });
                    _this.append("<li style='color: #108EE9;'>更多 ∨</li>");
                }
            });
        }


    }
    $.fn.serializeObjectJson = function () {
        var serializeObj = {};
        var currentObj = {};
        var seg = ".";
        $(this.serializeArray()).each(function () {
            var thisName = this.name;
            currentObj = serializeObj;//每次循环都定位到最开始位置
            while (thisName.indexOf(seg) != -1) { //如果有点 则需要分解这个对象
                var key = thisName.substring(0, thisName.indexOf(seg)); //获取这个key
                if (!currentObj.hasOwnProperty(key)) {  //然后判断当前对象中是否有这个key ，如果没有这个key 则加上这个属性
                    currentObj[key] = {};
                }
                currentObj = currentObj[key];  //赋值给当前对象
                thisName = thisName.substr(thisName.indexOf(seg) + 1); //当前的名字减少前面那节
            }
            currentObj[thisName] = this.value;
        });
        return serializeObj;
    };

    $.fn.appendData = function (config,utilRefresh,beforeCallback) {
        var temp = $(this);
        temp.before(util.mask);
        var result;
        var url = temp.data("url");
        var res = temp.data("res");
        // url = util.matchParamWithUrl(url);
        url = util.url.from(url);
        return new Promise(function (callback) {
            setTimeout(function () {
                callback(config, beforeCallback);
            }, 1);
        }).then(function (config, beforeCallback) {
            //如果第一个参数是方法，则赋给beforeCallback
            if (typeof config == 'function') {
                beforeCallback = config;
                config = {};
            }
            temp.hide();
            //提交方式 ，默认为 Post提交
            var submitType = temp.data("type") || "POST";
            // temp.nextAll().remove();
            var obj = $.extend({"pageSize": "10", "currentPage": "1"}, config);
            var param = {
                url: url,
                type: submitType,
                dataType: 'json',
                async: false,
                success: function (data) {
                    if(data.data){
                        var html = template(temp.attr("id"), data.data, beforeCallback);
                        temp.before(html);
                    }else{
                        // temp.after("<p style='text-align: center;margin-top: 6rem;'>没有查询到内容 <a href='#' onclick='util.back()'>点击返回</a></p>");
                    }
                    result = data;
                },
                error: function (e) {
                    alert("网络异常");
                },
                complete: function () {

                }
            };
            if (submitType == "POST") {
                param = $.extend(param, {
                    contentType: 'application/json',
                    data: JSON.stringify(obj)
                })
            }
            if (res && res.toUpperCase() == "LOCAL") {
                var data = {
                    data: {
                        list: []
                    }
                };
                data.data.list.push(util.storage.get(url));
                var html = template(temp.attr("id"), data.data, beforeCallback);
                temp.after(html);
                result = data;
            } else {
                $.ajax(param);
            }
            // temp.parent().find(".loading").remove();
            return result;
        }).then(function(result){
            if(result)
                utilRefresh.endPullupToRefresh(result.nextPage>result.totalPage);
            else{
                utilRefresh.endPullupToRefresh(true);
            }
            temp.prev("#myLoading").remove();
            return result||{nextPage:0};
        });
    };


    $.fn.loadData = function (config, beforeCallback) {
        var temp = $(this);
        temp.before(util.mask);
        var result;
        var url = temp.data("url");
        var res = temp.data("res");
        // url = util.matchParamWithUrl(url);
        url = util.url.from(url);
        return new Promise(function (callback) {
            setTimeout(function () {
                callback(config, beforeCallback);
            }, 1);
        }).then(function (config, beforeCallback) {
            //如果第一个参数是方法，则赋给beforeCallback
            if (typeof config == 'function') {
                beforeCallback = config;
                config = {};
            }
            temp.hide();
            //提交方式 ，默认为 Post提交
            var submitType = temp.data("type") || "POST";
            temp.nextAll().remove();
            var obj = $.extend({"pageSize": "10", "currentPage": "1"}, config);
            var param = {
                url: url,
                type: submitType,
                dataType: 'json',
                async: false,
                success: function (data) {
                    if(data.data){
                        if(util.isArray(data.data)){
                            var list = data.data;
                            data.data = {};
                            data.data.list = list;
                        }
                        var html = template(temp.attr("id"), data.data, beforeCallback);
                        temp.after(html);
                    }else{
                        temp.after("<p style='text-align: center;margin-top: 6rem;'>没有查询到内容 <a href='#' onclick='util.back()'>点击返回</a></p>");
                    }
                    result = data;
                },
                error: function (e) {
                    alert("网络异常");
                },
                complete: function () {

                }
            };
            if (submitType == "POST") {
                param = $.extend(param, {
                    contentType: 'application/json',
                    data: JSON.stringify(obj)
                })
            }
            if (res && res.toUpperCase() == "LOCAL") {
                var data = {
                    data: {
                        list: []
                    }
                };
                data.data.list.push(util.storage.get(url));
                var html = template(temp.attr("id"), data.data, beforeCallback);
                temp.after(html);
                result = data;
            } else {
                $.ajax(param);
            }
            temp.prev("#myLoading").remove();
            return result;
        });
    };

    $.fn.btPost = function (data, success, error) {
        var _this = $(this);
        var  url ;
        //如果是对象 则转出json字符串
        if (typeof data == "object") {
            data = JSON.stringify(data);
        }else if(typeof data == "string"){
            if(data.indexOf("/")==0){
                url = data;
                data = success;
                if (typeof data == "object") {
                    data = JSON.stringify(data);
                }
                success = error;
            }
        }
        _this.attr("disabled", "disabled");
        $("body").before(util.mask);
        $.ajax({
            url: url||$(this).data("url"),
            contentType: 'application/json',
            type: 'POST',
            data: data,
            dataType: 'json',
            success: function (data) {
                if (success) {
                    success(data);
                } else {
                    alert(data.msg);
                    setTimeout(function () {
                        window.location.reload();
                    }, 1500);
                }
            },
            error: function (e) {
                alert("网络异常");
            },
            complete: function () {
                _this.removeAttr("disabled");
                $("body").prev("#myLoading").remove();

            }
        })
    };

    $.fn.loadPage = function (param, callback) {
        var _this = $(this);
        util.url.private.gotoPage(util.url.to(param), _this, callback);
    };

        // 下拉刷新
    $.fn.pulldownRefresh = function () {
        mui.init({
            pullRefresh: {
                container: "#refreshContainer",//下拉刷新容器标识，querySelector能定位的css选择器均可，比如：id、.class等
                down: {
                    style: 'circle',//必选，下拉刷新样式，目前支持原生5+ ‘circle’ 样式
                    color: '#2BD009', //可选，默认“#2BD009” 下拉刷新控件颜色
                    height: '50px',//可选,默认50px.下拉刷新控件的高度,
                    range: '100px', //可选 默认100px,控件可下拉拖拽的范围
                    offset: '0px', //可选 默认0px,下拉刷新控件的起始位置
                    auto: true,//可选,默认false.首次加载自动上拉刷新一次
                    callback: pulldown //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
                }
            }
        });

        // 下拉刷新具体业务实现

        function pulldownRefresh() {
            setTimeout(function () {
                var table = document.body.querySelector('.mui-table-view');
                var cells = document.body.querySelectorAll('.mui-table-view-cell');
                for (var i = cells.length, len = i + 3; i < len; i++) {
                    var li = document.createElement('li');
                    li.className = 'mui-table-view-cell';
                    li.innerHTML = '<a class="mui-navigate-right">Item ' + (i + 1) + '</a>';
                    //下拉刷新，新纪录插到最前面；
                    table.insertBefore(li, table.firstChild);
                }
                mui('#pullrefresh').pullRefresh().endPulldownToRefresh(); //refresh completed
            }, 1500);
        };

    };




    // 上拉加载
    $.fn.pullupRefresh = function () {
        mui.init({
            pullRefresh: {
                container: ".refreshContainer",//待刷新区域标识，querySelector能定位的css选择器均可，比如：id、.class等
                up: {
                    height: 50,//可选.默认50.触发上拉加载拖动距离
                    auto: true,//可选,默认false.自动上拉加载一次
                    contentrefresh: "正在加载...",//可选，正在加载状态时，上拉加载控件上显示的标题内容
                    contentnomore: '没有更多数据了',//可选，请求完毕若没有更多数据时显示的提醒内容；
                    callback: pullup //必选，刷新函数，根据具体业务来编写，比如通过ajax从服务器获取新数据；
                }
            }
        });
        var count = 0;

        /**
         * 上拉加载具体业务实现
         */
        function pullupRefresh() {
            setTimeout(function () {
                mui('#pullrefresh').pullRefresh().endPullupToRefresh((++count > 2)); //参数为true代表没有更多数据了。
                var table = document.body.querySelector('.mui-table-view');
                var cells = document.body.querySelectorAll('.mui-table-view-cell');
                for (var i = cells.length, len = i + 20; i < len; i++) {
                    var li = document.createElement('li');
                    li.className = 'mui-table-view-cell';
                    li.innerHTML = '<a class="mui-navigate-right">Item ' + (i + 1) + '</a>';
                    table.appendChild(li);
                }
            }, 1500);
        };

        if (mui.os.plus) {
            mui.plusReady(function () {
                setTimeout(function () {
                    mui('#pullrefresh').pullRefresh().pullupLoading();
                }, 1000);

            });
        } else {
            mui.ready(function () {
                mui('#pullrefresh').pullRefresh().pullupLoading();
            });
        }

    };
})(jQuery);


