<%--
  User: Mr.Young
  Date: 2020/3/25
  Time: 21:22
  Email: no.bugs@foxmail.com
  Description: 
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>测试BootStrap</title>
    <!--引入jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
    <script type="text/javascript" src="static/js/jquery-3.4.1/jquery-3.4.1.js"></script>
    <!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
    <script src="static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <!-- Bootstrap 样式-->
    <link href="static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<!-- Standard button -->
<button type="button" class="btn btn-default">（默认样式）Default</button>
<br><br>

<!-- Provides extra visual weight and identifies the primary action in a set of buttons -->
<button type="button" class="btn btn-primary">（首选项）Primary</button>
<br><br>

<!-- Indicates a successful or positive action -->
<button type="button" class="btn btn-success">（成功）Success</button>
<br><br>

<!-- Contextual button for informational alert messages -->
<button type="button" class="btn btn-info">（一般信息）Info</button>
<br><br>

<!-- Indicates caution should be taken with this action -->
<button type="button" class="btn btn-warning">（警告）Warning</button>
<br><br>

<!-- Indicates a dangerous or potentially negative action -->
<button type="button" class="btn btn-danger">（危险）Danger</button>
<br><br>

<!-- Deemphasize a button by making it look like a link while maintaining button behavior -->
<button type="button" class="btn btn-link">（链接）Link</button>
<br><br>

<!-- 4:3 aspect ratio -->

<div class="embed-responsive embed-responsive-4by3">

    <video width="100" height="100" controls>

        <source src="video/movie.mp4" type="video/mp4">

    </video>

</div>

</body>
</html>
