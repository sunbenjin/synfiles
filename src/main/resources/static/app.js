var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#iframe").contents().find("#notice").html("");
}

var i = 0;
function showContent(body) {

    $("#iframe").contents().find("#notice").after("<li id='ranks"+i+"'>行号：" + i++ + "运行内容:" + body.content + "运行时间："+new Date(body.time).toLocaleString()+"</li>");
    // $("#iframe").contents().find("#dw").focus();

    if(i>=1000){
        $("#iframe").contents().find("#ranks"+(i-1000)).remove();
    }

}


function showFtpServerContent(body) {
    $("#dqzt").html(body.content);
}


function setMonitorBool(connected) {
    $("#kqjk").prop("disabled", connected);
    $("#gbjk").prop("disabled", !connected);
}


/**
 * 开启监控
 */
function startMonitor(){

    $.ajax({
        type: "get",
        url: "/ftpServer/openMonitor",
        data: {rootDirs:$("#rootDir").val()},
        dataType: "json",
        success: function(data){
            setMonitorBool(true);
        }
    });

}

/**
 * 关闭监控
 */
function stopMonitor(){
    $.ajax({
        type: "get",
        url: "/ftpServer/closeMonitor",
        data: {rootDirs:$("#rootDir").val()},
        dataType: "json",
        success: function(data){
            setMonitorBool(false);
        }
    });
}



/**
 <button id="kqfw" class="btn btn-default" type="submit">开启服务</button>
 <button id="gbfw" class="btn btn-default" type="submit">关闭服务</button>
 */
function startFtpServer(){
    $.ajax({
        type: "get",
        url: "/ftpServer/start",
        data: {ftpAddress:$("#ftpAddress").val()},
        dataType: "json",
        success: function(data){
            if(data.code==200){
                alert(data.data);
                $("#dqzt").html(data.data);
                setFtpServerBool(true);
            }else{
                setFtpServerBool(false);
            }
            //console.log(data);
           /*$("#dqzt").html(data.data);
            setFtpServerBool(true);*/
        }
    });
}


function stopFtpServer(){
    setFtpServerBool(false)
    $.ajax({
        type: "get",
        url: "/ftpServer/close",
        data: {ftpAddress:$("#ftpAddress").val()},
        dataType: "json",
        success: function(data){
            $("#dqzt").html(data.data);
        }
    });
}




/**
 <button id="kqfw" class="btn btn-default" type="button">开启服务</button>
 <button id="gbfw" class="btn btn-default" type="button">关闭服务</button>
 * @param connected
 */
function setFtpServerBool(connected) {
    $("#kqfw").prop("disabled", connected);
    $("#gbfw").prop("disabled", !connected);
    $("#dqzt").html("");
}




$(function () {
    debugger;
    var socket = new SockJS('/endpoint-websocket'); //连接上端点(基站)
    stompClient = Stomp.over(socket);			//用stom进行包装，规范协议
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/callback', function (result) {
            showContent(JSON.parse(result.body));
        });
        stompClient.subscribe('/topic/ftpserver', function (result) {
            showFtpServerContent(JSON.parse(result.body));
        });

    });



    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    // $( "#connect" ).click(function() { connect(); });
    // $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });

    $( "#kqfw" ).click(function() { startFtpServer(); });
    $( "#gbfw" ).click(function() { stopFtpServer(); });
    /**
     <button id="kqjk" class="btn btn-default" type="button">开启监控</button>
     <button id="gbjk" class="btn btn-default" type="button">关闭监控</button>
     */
    $( "#kqjk" ).click(function() { startMonitor(); });
    $( "#gbjk" ).click(function() { stopMonitor(); });

    //setConnected(false);
    setFtpServerBool(false);
    setMonitorBool(false);

});



