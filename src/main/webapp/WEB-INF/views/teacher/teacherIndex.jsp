<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>大学生学习平台</title>
    <link rel="shortcut icon" href="https://8.url.cn/edu/edu_modules/edu-ui/img/nohash/favicon.ico">
    <link rel="stylesheet" href="${APP_PATH}/resources1/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${APP_PATH}/resources1/bootstrap/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="${APP_PATH}/resources1/css/wukong-ui.css">
    <link rel="stylesheet" href="${APP_PATH}/resources1/bootstrap/css/bootstrap-select.min.css">
    <script type="text/javascript" src="${APP_PATH}/resources1/js/jquery-3.1.1.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap-select.min.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap.min.js"></script>
</head>

<body>
<div class="row">
    <div class="col-lg-12">
        <div class="banner" id="banner"></div>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <ul class="nav nav-tabs wk-nav-menu" id="wk-nav-menu">
            <li role="presentation"><a id="leftNav">教师模块&nbsp;&nbsp;<span class="glyphicon glyphicon-menu-up"
                                                                          aria-hidden="true"></span></a></li>
            <li role="presentation"><a href="javascript:void(0);" class="wk-main-menu-item" id="createCourseVideo">上传课程视频</a>
            </li>
            <li role="presentation"><a href="javascript:void(0);" class="wk-main-menu-item" id="createCourseFile ">上传课程文件</a>
            </li>
            <li role="presentation"><a href="javascript:void(0);" class="wk-main-menu-item" id="searchCourseVideo">课程视频查询</a>
            </li>
            <li role="presentation"><a href="javascript:void(0);" class="wk-main-menu-item"
                                       id="searchCourseFile">课程文件查询</a>
            </li>
            <li role="presentation"><a href="javascript:void(0);" class="wk-main-menu-item" id="searchTask">作业信息查询</a>
            </li>
            <li role="presentation"><a href="javascript:void(0);" id="logout">注销登录</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-2" id="wk-menu-panel" style="width: 15%;">

        <div class="row">
            <div class="col-lg-12">
                <div class="panel-group wk-accordion-panel-group" id="accordion">

                    <div class="panel panel-info wk-accordion-header">
                        <div class="panel-heading">
                            <a data-toggle="collapse" href="#BMenu" data-parent="#accordion" class="click">课程文件管理&nbsp;<span
                                    class="glyphicon glyphicon-hand-down" aria-hidden="true"></span></a>
                        </div>
                        <div id="BMenu" class="panel-collapse collapse">
                            <div class="list-group wk-accordion-list-group">
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/teacher/createCourseVideo">上传课程视频&nbsp;<span
                                        class=" glyphicon glyphicon-triangle-right"
                                        aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/teacher/searchCourseVideo">查询课程视频&nbsp;<span
                                        class=" glyphicon glyphicon-triangle-right"
                                        aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/teacher/createCourseFile">上传课程文档&nbsp;<span
                                        class=" glyphicon glyphicon-triangle-right"
                                        aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/teacher/searchCourseFile">查询课程文档&nbsp;<span
                                        class=" glyphicon glyphicon-triangle-right"
                                        aria-hidden="true"></span>
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-info wk-accordion-header">
                        <div class="panel-heading">
                            <a data-toggle="collapse" href="#CMenu" data-parent="#accordion" class="click">讨论管理&nbsp;<span
                                    class=" glyphicon glyphicon-hand-down" aria-hidden="true"></span></a>
                        </div>

                        <div id="CMenu" class="panel-collapse collapse">
                            <div class="list-group wk-accordion-list-group">
                                <button type="button" class="list-group-item" href="${APP_PATH}/teacher/createDiscuss">
                                    新增讨论&nbsp;<span class=" glyphicon glyphicon-triangle-right"
                                                    aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item" href="${APP_PATH}/teacher/searchDiscuss">
                                    讨论信息查询&nbsp;<span class=" glyphicon glyphicon-triangle-right"
                                                      aria-hidden="true"></span>
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-info wk-accordion-header">
                        <div class="panel-heading">
                            <a data-toggle="collapse" href="#DMenu" data-parent="#accordion" class="click">课程章节管理&nbsp;<span
                                    class=" glyphicon glyphicon-hand-down" aria-hidden="true"></span></a>
                        </div>
                        <div id="DMenu" class="panel-collapse collapse">
                            <div class="list-group wk-accordion-list-group">
                                <button type="button" class="list-group-item" href="${APP_PATH}/teacher/createChapter">
                                    新增章节&nbsp;<span class=" glyphicon glyphicon-triangle-right"
                                                    aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item" href="${APP_PATH}/teacher/searchChapter">
                                    章节查询&nbsp;<span class=" glyphicon glyphicon-triangle-right"
                                                      aria-hidden="true"></span>
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-info wk-accordion-header">
                        <div class="panel-heading">
                            <a data-toggle="collapse" href="#EMenu" data-parent="#accordion" class="click">作业管理&nbsp;<span
                                    class=" glyphicon glyphicon-hand-down" aria-hidden="true"></span></a>
                        </div>
                        <div id="EMenu" class="panel-collapse collapse">
                            <div class="list-group wk-accordion-list-group">
                                <button type="button" class="list-group-item" href="${APP_PATH}/teacher/createTask">发布作业&nbsp;<span
                                        class=" glyphicon glyphicon-triangle-right"
                                        aria-hidden="true"></span></button>
                                <button type="button" class="list-group-item" href="${APP_PATH}/teacher/searchTask">
                                    作业信息查询&nbsp;<span class=" glyphicon glyphicon-triangle-right"
                                                      aria-hidden="true"></span></button>
                                <!-- <button type="button" class="list-group-item" id="wk-menu-panel-item-searchTask"> >&nbsp;&nbsp;作业完成情况查询</button> -->
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-info wk-accordion-header">
                        <div class="panel-heading">
                            <a data-toggle="collapse" href="#FMenu" data-parent="#accordion" class="click">个人信息管理&nbsp;<span
                                    class=" glyphicon glyphicon-hand-down" aria-hidden="true"></span></a>
                        </div>
                        <div id="FMenu" class="panel-collapse collapse">
                            <div class="list-group wk-accordion-list-group">
                                 <button type="button" class="list-group-item"
                                        href="${APP_PATH}/teacher/viewTeacherInfo">
                                    个人信息查看&nbsp;<span class=" glyphicon glyphicon-triangle-right"
                                                      aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/teacher/editTeacherInfo">
                                    个人信息修改&nbsp;<span class=" glyphicon glyphicon-triangle-right"
                                                      aria-hidden="true"></span>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="rightTable" class="col-lg-10" style="padding:0px;">
        <iframe id="mainFrame" src="" width="100%" frameborder="0"
                onload="changeFrameHeight()">
        </iframe>
    </div>
</div>
<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title">注销登录</h5>
            </div>
            <div class="modal-body">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="logout_btn">确定</button>
            </div>
        </div>
    </div>
</div>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        let flag = true;
        let span = $("#leftNav span");
        let pre = null;
        let previous = null;
        $('#wk-nav-menu .wk-main-menu-item').on("click", function () {
            if (previous == null) {
                previous = $(this);
                $(this).addClass("current");
            } else {
                if (previous.text() !== $(this).text()) {
                    if (previous.hasClass("current")) {
                        previous.removeClass("current");
                    }
                    if (!$(this).hasClass("current")) {
                        $(this).addClass("current");
                    }
                    previous = $(this);
                }
            }
        });
        $(".click").click(function () {
            if (pre != null && pre.text() !== $(this).text()) {
                if (pre.children(span).hasClass("glyphicon-hand-up")) {
                    pre.children(span).removeClass("glyphicon-hand-up");
                    pre.children(span).addClass("glyphicon-hand-down");
                }
            }
            let children = $(this).children(span);
            if (children.hasClass("glyphicon-hand-down")) {
                children.removeClass("glyphicon-hand-down");
                children.addClass("glyphicon-hand-up");
            } else if (children.hasClass("glyphicon-hand-up")) {
                children.removeClass("glyphicon-hand-up");
                children.addClass("glyphicon-hand-down");
            }
            pre = $(this);
        });
        $("#leftNav").click(function () {
            if (span.hasClass("glyphicon-menu-down")) {
                span.removeClass("glyphicon-menu-down");
                span.addClass("glyphicon-menu-up");
            } else if (span.hasClass("glyphicon-menu-up")) {
                span.removeClass("glyphicon-menu-up");
                span.addClass("glyphicon-menu-down");
            }
            $("#wk-menu-panel").slideToggle();
            if (flag) {
                $("#rightTable").css("width", "100%");
                $("#rightTable").css("margin-left", "6px");
                flag = false;
            } else {
                $("#rightTable").css("width", "84%");
                $("#rightTable").css("margin-left", "0px");
                flag = true;
            }
        });
    });

    //计算子窗口高度
    function changeFrameHeight() {
        let ifm = document.getElementById("mainFrame");
        ifm.height = document.documentElement.clientHeight - $("#wk-nav-menu").height() - $("#banner").height() - 9;
    }

    //监听窗口或框架的大小改变事件，并执行函数
    window.onresize = function () {
        changeFrameHeight();
    };

    //计算子菜单高度
    function changeMenuPanelHeight() {
        let panel = $("#wk-menu-panel");
        panel.height(document.documentElement.clientHeight - $("#wk-nav-menu").height() - $("#banner").height() - 3);

    }

    changeMenuPanelHeight();

    window.onresize = function () {
        changeMenuPanelHeight();
    };

    //为子菜单的按钮设置点击事件
    $(".list-group-item").each(function () {
        $(this).on("click", function () {
            let url = $(this).attr("href");
            $("#mainFrame").attr("src", url);
        });
    });
    //为主的按钮设置点击事件
    $(".wk-main-menu-item").each(function () {
        $(this).on("click", function () {
            let url = $(this).attr("id");
            $("#mainFrame").attr("src", "${APP_PATH}/teacher/" + url);
        });
    });
    $("#logout").on("click", function () {
        $("#logoutModal .modal-body").text("你确定要注销登录吗？");
        $("#logoutModal").modal({
            backdrop: "static"
        });
    })
    $("#logout_btn").on("click", function () {
        let loadingIndex = layer.msg('注销中...', {icon: 16});
        //发送ajax请求删除
        $.ajax({
            url: "${APP_PATH}/teacher/logout",
            type: "POST",
            dataType: "json",
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("注销登录成功", {time: 1000, icon: 1}, function () {
                    });
                    window.location.href = "${APP_PATH}/index/login";
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });
</script>
</body>
</html>