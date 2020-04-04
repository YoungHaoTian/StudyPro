<%--
  User: Mr.Young
  Date: 2020/3/31
  Time: 11:12
  Email: no.bugs@foxmail.com
  Description: 
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${APP_PATH}/resources/css/bootstrap.min.css" type="text/css" rel="stylesheet">
    <link href="${APP_PATH}/resources/css/font-awesome.min.css" type="text/javascript" rel="stylesheet">
    <link href="${APP_PATH}/resources/css/bootsnav.css" type="text/css" rel="stylesheet">
    <link href="${APP_PATH}/resources/css/normalize.css" type="text/css" rel="stylesheet">
    <link href="${APP_PATH}/resources/css/css.css" rel="stylesheet" type="text/css">
    <link rel="shortcut icon" href="https://8.url.cn/edu/edu_modules/edu-ui/img/nohash/favicon.ico">
    <script src="${APP_PATH}/resources/js/jquery-1.9.1.min.js" type="text/javascript"></script>
    <script src="${APP_PATH}/resources/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${APP_PATH}/resources/js/bootsnav.js" type="text/javascript"></script>
    <script src="${APP_PATH}/resources/js/jquery.js" type="text/javascript"></script>
    <!--[if IE]>
    <script src="js/html5.js"></script><![endif]-->
    <title>好学习 登录</title>
</head>

<body class="logobg_style">
<div id="large-header" class="large-header login-page">
    <canvas id="demo-canvas" width="1590" height="711"></canvas>
    <div class="login-form">
        <div class="login-content">
            <h1 class="title_name">用户登录</h1>
            <form method="post" role="form" id="form_login" class="login_padding">
                <div class="form-group clearfix">
                    <div class="input-group-addon">
                        <span>登录方式</span>
                    </div>
                    <br>
                    <div>
                        <label class="radio-inline">
                            <input type="radio" value="email" name="type"><span style="color: white;">邮箱登录</span>
                        </label>
                        <label class="radio-inline">
                            <input type="radio" value="phone" name="type" checked="checked"><span
                                style="color: white;">手机号登录</span>
                        </label>
                        <label class="radio-inline">
                            <input type="radio" value="account" name="type"><span style="color: white;">账号登录</span>
                        </label>
                    </div>
                </div>


                <div class="form-group clearfix">

                    <div class="input-group">
                        <div class="input-group-addon">
                            <i class="icon_user"></i>
                        </div>

                        <input type="text" class="form-control" maxlength="30" name="username" id="username"
                               placeholder="邮箱\手机号\账号"
                               autocomplete="off">
                    </div>

                </div>

                <div class="form-group clearfix">

                    <div class="input-group">
                        <div class="input-group-addon">
                            <i class="icon_password"></i>
                        </div>

                        <input type="password" class="form-control" name="password" id="password" placeholder="密码"
                               autocomplete="off">
                    </div>

                </div>
                <div class="form-group clearfix">
                    <div class="input-group-addon" style="margin-bottom: 5px">
                        <span>登录角色</span>
                    </div>
                    <br>
                    <div>
                        <label class="radio-inline">
                            <input type="radio" value="admin" name="role"><span style="color: white;">管理员</span>
                        </label>
                        <label class="radio-inline">
                            <input type="radio" value="student" name="role" checked="checked"><span
                                style="color: white;">学生</span>
                        </label>
                        <label class="radio-inline">
                            <input type="radio" value="teacher" name="role"><span style="color: white;">老师</span>
                        </label>
                    </div>
                </div>
                <div class="textright"><a href="javascript:void(0); " onClick="cliForget()" class="forget">忘记密码?</a>
                </div>
                <div class="tishi"></div>
                <div class="form-group">
                    <a href="javascript:;" type="submit" class="btn btn-danger btn-block btn-login"
                       onClick="cliLogin()">
                        <i class="fa fa-sign-in"></i>
                        登录
                    </a>
                </div>
                <div class=" textright"><a href="javascript:void(0);" onClick="cliRegister()" class="forget">立即注册</a>
                </div>
                <!-- Implemented in v1.1.4 -->
                <div class="form-group">

                </div>


            </form>
        </div>

    </div>
</div>

<script src="${APP_PATH}/resources/js/TweenLite/TweenLite.min.js"></script>
<script src="${APP_PATH}/resources/js/TweenLite/EasePack.min.js"></script>
<script src="${APP_PATH}/resources/js/TweenLite/rAF.js"></script>
<script src="${APP_PATH}/resources/js/TweenLite/demo-1.js"></script>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script src="${APP_PATH}/resources/js/jquery.md5.js"></script>
</body>
</html>
<script type="text/javascript">
    //登录操作
    let phone = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\d{8}$/;
    let email = /^(\w+\.)*\w+\@+[0-9a-zA-Z]+\.(com|com.cn|edu|hk|cn|net)$/;

    function cliLogin() {
        let txtUser = $.trim($("#username").val());
        let txtPwd = $("#password").val();
        let type = $("input[name='type']:checked").val();
        let role = $("input[name='role']:checked").val();
        if (type === "phone") {
            if ($.trim(txtUser) == "") {
                layer.msg("请输入你的手机号", {time: 1500, icon: 5, shift: 6}, function () {
                });
                $("#txtUser").focus();
                return;

            }
            if (!phone.test(txtUser)) {
                layer.msg("手机输入格式不正确，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
                });
                $("#txtUser").focus();
                return;
            }
        } else if (type === "email") {
            if ($.trim(txtUser) == "") {
                layer.msg("请输入你的邮箱", {time: 1500, icon: 5, shift: 6}, function () {
                });
                $("#txtUser").focus();
                return;
            }
            if (!email.test(txtUser)) {
                layer.msg("邮箱格式不正确，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
                });
                $("#txtUser").focus();
                return;
            }
        } else {
            if ($.trim(txtUser) == "") {
                layer.msg("请输入你的账号", {time: 1500, icon: 5, shift: 6}, function () {
                });
                $("#txtUser").focus();
                return;
            }
        }
        if ($.trim(txtPwd) == "") {
            layer.msg("请输入密码！", {time: 1500, icon: 5, shift: 6}, function () {
            });
            $("#Userpwd").focus();
            return;
        }
        console.log("admin");
        txtPwd = $.md5(txtPwd);
        let data = {"type": type, "username": txtUser, "password": txtPwd};
        let loadingIndex = layer.msg('处理中', {icon: 16});

        $.ajax({
            url: "${APP_PATH}/" + role + "/login",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (result) {
                layer.close(loadingIndex);
                console.log(result);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("登录成功，即将跳转", {time: 1000, icon: 6}, function () {
                    });
                    window.location.href = "${APP_PATH}/" + role + "/" + role + "Index";
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    }

    function cliRegister() {
        window.location.href = "${APP_PATH}/index/register";
    }

    function cliForget() {
        window.location.href = "${APP_PATH}/index/forget";
    }
</script>
