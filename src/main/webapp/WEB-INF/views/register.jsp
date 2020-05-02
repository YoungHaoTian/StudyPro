<%--
  User: Mr.Young
  Date: 2020/3/31
  Time: 18:18
  Email: no.bugs@foxmail.com
  Description: 
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${APP_PATH}/resources/css/bootstrap.min.css" type="text/css" rel="stylesheet">
    <link href="${APP_PATH}/resources/font/css/font-awesome.min.css" type="text/javascript" rel="stylesheet">
    <link href="${APP_PATH}/resources/css/bootsnav.css" type="text/css" rel="stylesheet">
    <link href="${APP_PATH}/resources/css/normalize.css" type="text/css" rel="stylesheet">
    <link href="${APP_PATH}/resources/css/css.css" rel="stylesheet" type="text/css">
    <link rel="shortcut icon" href="https://8.url.cn/edu/edu_modules/edu-ui/img/nohash/favicon.ico">
    <script src="${APP_PATH}/resources/js/jquery-1.11.0.min.js" type="text/javascript"></script>
    <script src="${APP_PATH}/resources/js/jquery.step.js"></script>
    <script src="${APP_PATH}/resources/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${APP_PATH}/resources/js/bootsnav.js" type="text/javascript"></script>
    <script src="${APP_PATH}/resources/js/jquery.js" type="text/javascript"></script>
    <!--[if IE]>
    <script src="${APP_PATH}/resources/js/html5.js"></script><![endif]-->
    <title>好学习 注册</title>
</head>

<body class="logobg_style">
<div id="large-header" class="large-header login-page">
    <canvas id="demo-canvas" width="1590" height="711"></canvas>
    <div class="Retrieve_style">
        <div class="Retrieve-content step-body" id="myStep">
            <h1 class="title_name">用户注册</h1>
            <div class="step-header">
                <ul>
                    <li><p>输入电话</p></li>
                    <li><p>确认密码</p></li>
                    <li><p>注册成功</p></li>
                </ul>
            </div>
            <div class="step-content">
                <div class="step-list login_padding">
                    <form role="form" id="form_login" class="">
                        <div class="form-group clearfix">
                            <div class="input-group col-lg-8 col-md-8 col-xs-8">
                                <div class="input-group-addon"><i class="icon_phone"></i></div>
                                <input type="text" oninput="value=value.replace(/[^\d]/g,'')" maxlength="11"
                                       class="form-control text_phone" name="phone"
                                       id="phone"
                                       placeholder="你的手机号" autocomplete="off">
                            </div>
                            <div class="col-lg-4 col-md-4 col-xs-4 fl"><input type="button" id="btn" class="btn_mfyzm"
                                                                              value="获取验证码" onclick="Sendpwd(this)"/>
                            </div>
                        </div>
                        <div class="form-group clearfix">
                            <div class="input-group">
                                <div class="input-group-addon"><i class="icon_yanzhen"></i></div>
                                <input type="text" maxlength="6" class="form-control" name="Verification"
                                       id="Verification"
                                       placeholder="短信验证码" autocomplete="off">
                            </div>
                        </div>
                        <div class="tishi"></div>
                    </form>
                    <div class="form-group">
                        <button class="btn btn-danger btn-step btn-login back " style="float: left">返回登录</button>
                        <button class="btn btn-danger btn-step btn-login" id="applyBtn" style="float: right">下一步
                        </button>
                    </div>

                </div>
                <div class="step-list">
                    <form method="post" role="form" id="" class="login_padding">
                        <div class="form-group clearfix">
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <i class="icon_password"></i>
                                </div>
                                <input type="password" class="form-control" name="password" id="password"
                                       placeholder="注册密码（长度需大于6）" autocomplete="off">
                            </div>
                        </div>
                        <div class="form-group clearfix">
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <i class="icon_password"></i>
                                </div>
                                <input type="password" class="form-control" name="confirmpwd" id="confirmpwd"
                                       placeholder="再次密码" autocomplete="off">
                            </div>
                        </div>
                        <div class="form-group">
                            <a href="javascript:void(0);" type="submit" class="btn btn-danger btn-step btn-login"
                               style="float: left"
                               id="preBtn">上一步</a>

                            <a href="javascript:void(0);" type="submit" class="btn btn-danger btn-step btn-login"
                               style="float: right;"
                               id="submitBtn">下一步</a>
                        </div>
                    </form>
                </div>
                <div class="step-list">
                    <div class="ok_style center">
                        <h2><img src="${APP_PATH}/resources/images/ok.png"></h2>
                        <h5 class="color2 mtb20">你已成功注册<b id="sec">5</b>秒后跳转到登录页面</h5>
                        <a href="javaScript:void(0);" class="btn btn-danger back" id="back" style="margin-right: 20px">返回登录</a>
                        <a href="javaScript:void(0);" class="btn btn-primary" id="goBtn">重新注册</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${APP_PATH}/resources/js/TweenLite/TweenLite.min.js"></script>
<script src="${APP_PATH}/resources/js/TweenLite/EasePack.min.js"></script>
<script src="${APP_PATH}/resources/js/TweenLite/rAF.js"></script>
<script src="${APP_PATH}/resources/js/TweenLite/demo-1.js"></script>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
</body>
</html>
<script type="text/javascript">
    let validCode = true;
    let phone = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\d{8}$/;
    let passwords = /^[a-zA-Z0-9]{6,18}$/;

    //发送验证码
    function Sendpwd(sender) {
        //60s倒计时
        let time = 60;
        let phones = $.trim($("#phone").val());
        if ($.trim(phones) === "") {
            layer.msg("请输入你的手机号", {time: 1500, icon: 5, shift: 6}, function () {
            });
            $("#phone").focus();
            return;
        }
        if (!phone.test(phones)) {
            layer.msg("手机号格式不正确", {time: 1500, icon: 5, shift: 6}, function () {
            });
            $("#phone").focus();
            return;
        }
        let data = {"phone": phones, "type": "register"};
        let loadingIndex = layer.msg('处理中', {icon: 16});
        $.ajax({
            url: "${APP_PATH}/index/code",
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
                    let code = $(sender);
                    if (validCode) {
                        validCode = false;
                        code.addClass("msgs1").attr("disabled", true);
                        let t = setInterval(function () {
                            time--;
                            code.val(time + "秒");
                            if (time === 0) {
                                clearInterval(t);
                                code.val("重新获取");
                                validCode = true;
                                code.removeClass("msgs1").attr("disabled", false);
                            }
                        }, 1000);
                    }
                    layer.msg("获取成功，请注意查收", {time: 1000, icon: 6}, function () {
                    });
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });

    }

    function lazyGo() {
        let sec = $("#sec").text();
        $("#sec").text(--sec);
        if (sec > 0) {
            setTimeout("lazyGo()", 1000);
        } else {
            window.location.href = "${APP_PATH}/index/login";
        }
    }

    $(function () {
        let request_phone = '';
        let request_code = '';
        let request_password = '';
        let step = $("#myStep").step({
            animate: true,
            initStep: 1,
            speed: 1000
        });
        $("#preBtn").click(function (event) {
            let yes = step.preStep();
        });
        $("#applyBtn").click(function (event) {

            let code = $.trim($("#Verification").val());
            let phone = /^1[345789]\d{9}$/;

            let phones = $.trim($("#phone").val());
            if ($.trim(phones) === "") {
                layer.msg("请填写手机号码", {time: 1500, icon: 5, shift: 6}, function () {
                });
                $("#phone").focus();
                return;
            }
            if (!phone.test(phones)) {
                layer.msg("手机输入格式不正确,请从新输入", {time: 1500, icon: 5, shift: 6}, function () {
                });
                $("#phones").focus();
                return;
            }
            if ($.trim(code) === "") {
                layer.msg("动态验证码未填写", {time: 1500, icon: 5, shift: 6}, function () {
                });
                $("#Verification").focus();
                return;
            }
            request_code = code;
            request_phone = phones;
            let yes = step.nextStep();
            return;
        });
        $("#submitBtn").click(function (event) {
            let txtconfirm = $.trim($("#confirmpwd").val());
            let txtPwd = $("#password").val();
            if ($.trim(txtPwd) === "") {
                layer.msg("请输入你的密码", {time: 1500, icon: 5, shift: 6}, function () {
                });
                $("#txtPwd").focus();
                return;
            }
            if ($.trim(txtconfirm) === "") {
                layer.msg("请再次输入你的密码", {time: 1500, icon: 5, shift: 6}, function () {
                });
                $("#txtconfirm").focus();
                return;
            }
            if ($.trim(txtconfirm) != $.trim(txtPwd)) {
                layer.msg("你输入的密码不匹配，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
                });
                $("#txtconfirm").focus();
                return;

            }
            if (txtPwd.indexOf(" ") !== -1) {
                layer.msg("密码不能包含空格", {time: 1500, icon: 5, shift: 6}, function () {
                });
                $("#txtconfirm").focus();
                return;
            }
            if (!passwords.test(txtPwd)) {
                layer.msg("登录密码只能由字母和数字组成，长度6-18位，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
                });
                $("#txtconfirm").focus();
                return;
            }
            request_password = txtPwd;
            let data = {"phone": request_phone, "code": request_code, "password": request_password};
            let loadingIndex = layer.msg('处理中', {icon: 16});
            $.ajax({
                url: "${APP_PATH}/student/register",
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
                        /*layer.msg("注册成功", {time: 1000, icon: 6}, function () {
                        });*/
                        let yes = step.nextStep();
                        $(function () {
                            setTimeout("lazyGo()", 1000);
                        });
                    }
                },
                error: function () {
                    layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
            });
        });

        $("#goBtn").click(function (event) {
            let yes = step.goStep(1);
            $("#sec").text(5);
        });
        $(".back").click(function (event) {
            window.location.href = "${APP_PATH}/index/login";
        });
    });
</script>


