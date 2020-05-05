<%--
  User: Mr.Young
  Date: 2020/4/5
  Time: 0:15
  Email: no.bugs@foxmail.com
  Description: 
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>大学生学习平台</title>
    <link rel="shortcut icon" href="https://8.url.cn/edu/edu_modules/edu-ui/img/nohash/favicon.ico">
    <link rel="stylesheet" href="${APP_PATH}/resources1/bootstrap/css/bootstrap.min.css">
</head>
<body>
<div style="text-align: center;margin-top: 10%">
    <img src="${APP_PATH}/images/error.jpg" width="100px">
</div>
<div style="text-align: center">
    <h3 style="color: red">出错啦!${exception.message}</h3>
    <button type="button" class="btn btn-link" onclick="window.history.back()"><h4>返回</h4></button>
</div>
</body>
</html>
