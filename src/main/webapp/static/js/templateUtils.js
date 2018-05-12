template.data={
    date:{
        date:1,
        time:2,
        scope:3
    },
}
template.ifNull = function (data,replaceValue,key) {
    if(data&&key) {
        try{
            data = eval("data."+key);
        }catch (e){
            data = undefined;
        }

    }
    return data||replaceValue||'';
};

template.getTypeColor = function(data){
    var temInt = parseInt(data);
    if (temInt < 6){
        return "#ff943e";
    }else if (temInt < 11){
        return "#108ee9";
    }else if (temInt < 16){
        return "#e8541e";
    }else{
        return "#4ccca0";
    }
}

template.absolutelyPath = function (data,key) {
    data  = template.ifNull(data,'',key);
    if(data)
        return (data.path+"/"+data.datestr+"/"+data.name);
    else
        return "#";
}



template.helper('enums', function (data,type,key) {
    if(data=='0')data = '00';
    if(data||data==false){
        try{
            if(key){
                data = eval("enums."+key);
            }
            return  (data&&enums[type][data])||'';
        }catch (e){
            return ''
        }
    }else{
        return '';
    }
});



template.helper('timeRange',function(data,key){
    if(data){
        if(key)data  = eval("data."+key);
        console.log(data);
        if(data&&data!="{}"){
            var arr =  ijob.getDateList(data);

            var start = arr[0];
            var end = arr[arr.length-1];
            return start+" - "+end;
        }else{
            return '';
        }
    }else{
        return '';
    }
});

template.helper('dateFormatByMinute',function(num){
    function fullNum(number){
        number = Math.floor(number);
        return (number<10?"0":"")+number;
    }
    return fullNum(num/60)+":"+fullNum(num%60);
});

//如果不传，默认自动 m分 h时 d天
template.helper('subTime',function(date,type){
    console.log(new Date().getTime());
    console.log(date);
    if(date){
        var time = new Date().getTime()-date;
        if(time>3600*24*1000){
            type = "d";
        }else if( time > 3600*1000){
            type = "h";
        }else{
            type = "m";
        }
        if(type=='h'){
            return Math.round(time/3600000)+"小时";
        }else if(type =='m'){
            return Math.round(time/60000)+"分钟";
        }else if(type =='d') {
            return Math.round(time/(3600000*24))+"天";
        }
    }else{
        return "0分钟";
    }
});




template.helper('dateFormat',function(date,format,type){
    if(date&&type){
        date = eval("date."+type);
    }
    if(!date)return '0';
    var week = ["日","一","二","三","四","五","六"];
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
        "S": date.getMilliseconds(), //毫秒
        "W": week[date.getDay()],
        "A": new Date().getFullYear()-date.getFullYear()
    };


    format = format.replace(/([yMdhmsqSWA])+/g, function(all, t){
        var v = map[t];
        if(v !== undefined){
            if(all.length > 1){
                v = '0' + v;
                v = v.substr(v.length-2);
            }
            return v;
        }
        else if(t === 'y'){
            return (date.getFullYear() + '').substr(4 - all.length);
        }
        return all;
    });
    return format;
});



template.helper('absolutelyPath', template.absolutelyPath);

template.helper('userHead',function(data){
    if(data){
        if(data.attahement){
            return ("/upload/"+data.attahement.path+"/"+data.attahement.datestr+"/"+data.attahement.name);
        }else{
            return  data.weixin.headimgurl;
        }
    }
    return '';
});

template.helper('ifNull', template.ifNull);

template.helper("getTypeColor", template.getTypeColor);

template.helper("formatBoard",function(data){
    if(data){
        if(data === '0000'){
            return "不管饭";
        }
        var str = "早,中,晚,宵";
        var result = "";
        var ss = data.split("");
        for(var i in ss){
            if(ss[i]=="1"){
                result += str.split(",")[i];
            }
        }
        return result;
    }
    return '';

});

template.helper("signState",function(data){
    if(data){
        if (data.signinList[0].state == false || data.signinList[0].state == "false" ){
            return "state7"
        }else {
            if(data.dismiss==true){
                return 'state6';
            }else{
                return "state"+data.state;
            }
        }
    }
    return '';

});

template.helper("scale",function(data){
    if(data){
        return Math.round(data*100)/100;
    }else{
        return '';
    }
});

template.helper("jobStatus",function(data){
    if(data){
        var state = data.state;
        var dismiss = data.dismiss;
        return (enums['BeenStatus'][state]+(dismiss==true?'(被辞退)':(dismiss==false?'(主动退出)':'')));
    }else{
        return '';
    }
});

template.helper("money",function(data,len){
    len = len||2;
    if(data){
        data  = new String(data);
        if(data.indexOf('.')>-1){
            data = (data+"00");
        }else{
            data = data+".00";
        }
        data = data.substring(0,(data.indexOf(".")+len+1));
        return data;
    }else {
        return '';
    }
});

template.substr = function(data,len){
    if(data){
        len = Math.min(data.length,len||4);
        return data.substring(0,len)+(data.length>len?"..":"");
    }else{
        return '';
    }
}
template.helper("substr",template.substr);