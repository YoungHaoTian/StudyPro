<%--
  User: Mr.Young
  Date: 2020/3/25
  Time: 21:22
  Email: no.bugs@foxmail.com
  Description: 
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:forward page="/admin/adminIndex"/>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>测试BootStrap</title>
    <!--引入jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
    <script type="text/javascript" src="${APP_PATH}/resources1/lib/jquery-3.1.1.js"></script>
    <!--加载layer组件-->
    <script src="resources/layer/layer.js"></script>
    <!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
<%--    <script src="${APP_PATH}/resources1/bootstrap/js/bootstrap.min.js"></script>
    <!-- Bootstrap 样式-->
    <link href="${APP_PATH}/resources1/bootstrap/css/bootstrap.min.css" rel="stylesheet">--%>
</head>
<body>
<!-- 16:9 aspect ratio -->

<div class="embed-responsive embed-responsive-16by9">

    <video width="320" height="240" controls>

        <source src="${APP_PATH}/video/movie.mp4" type="video/mp4">

    </video>

</div>

${APP_PATH}
</body>
</html>
<script type="text/javascript">
    $(function () {
        layer.msg("提示信息", {time: 1000, icon: 5, shift: 6});
    })
</script>