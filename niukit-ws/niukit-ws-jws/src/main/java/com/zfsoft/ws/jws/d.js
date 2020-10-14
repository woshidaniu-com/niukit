2.通过ajax+js+xml调用
<html>
    <head>
        <title>通过ajax调用WebService服务</title>
        <script>
            
            var xhr = new ActiveXObject("Microsoft.XMLHTTP");
            function sendMsg(){
                var name = document.getElementById('name').value;
                //服务的地址
                var wsUrl = 'http://192.168.1.100:6789/hello';
                
                //请求体
                var soap = '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:q0="http://ws.itcast.cn/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">' + 
                                     ' <soapenv:Body> <q0:sayHello><arg0>'+name+'</arg0>  </q0:sayHello> </soapenv:Body> </soapenv:Envelope>';
                                     
                //打开连接
                xhr.open('POST',wsUrl,true);
                
                //重新设置请求头
                xhr.setRequestHeader("Content-Type","text/xml;charset=UTF-8");
                
                //设置回调函数
                xhr.onreadystatechange = _back;
                
                //发送请求
                xhr.send(soap);
            }
            
            function _back(){
                if(xhr.readyState == 4){
                    if(xhr.status == 200){
                            //alert('调用Webservice成功了');
                            var ret = xhr.responseXML;
                            var msg = ret.getElementsByTagName('return')[0];
                            document.getElementById('showInfo').innerHTML = msg.text;
                            //alert(msg.text);
                        }
                }
            }
        </script>
    </head>
    <body>
            <input type="button" value="发送SOAP请求" onclick="sendMsg();">
            <input type="text" id="name">
            <div id="showInfo">
            </div>
    </body>
</html>