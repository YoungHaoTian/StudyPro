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
    <style>
        .wk-accordion-list-group .current-list-group-item {
            border: none;
            background: #AAA;
            color: #00fff2;
        }
    </style>
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
            <li role="presentation"><a id="leftNav">&nbsp;管理员模块&nbsp;<span class="glyphicon glyphicon-menu-up"
                                                                           aria-hidden="true"></span></a></li>
            <li role="presentation"><a href="javascript:void(0);" class="wk-main-menu-item" id="createStudent">新增学生</a>
            </li>
            <li role="presentation"><a href="javascript:void(0);" class="wk-main-menu-item"
                                       id="searchStudent">学生信息查询</a></li>
            <li role="presentation"><a href="javascript:void(0);" class="wk-main-menu-item" id="createTeacher">新增教师</a>
            </li>
            <li role="presentation"><a href="javascript:void(0);" class="wk-main-menu-item"
                                       id="searchTeacher">教师信息查询</a></li>
            <li role="presentation"><a href="javascript:void(0);" class="wk-main-menu-item" id="createCollege">新增学院</a>
            </li>
            <li role="presentation"><a href="javascript:void(0);" class="wk-main-menu-item"
                                       id="searchCollege">学院信息管理</a></li>
            <li role="presentation"><a href="javascript:void(0);" class="wk-main-menu-item" id="createCourse">新增课程</a>
            </li>
            <li role="presentation"><a href="javascript:void(0);" class="wk-main-menu-item" id="searchCourse">课程信息查询</a>
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
                    <%-- <div class="panel panel-info wk-accordion-header">
                         <div class="panel-heading">
                             <img src="${APP_PATH}/head/img_0001.jpg" class="img-circle" alt="头像加载失败">
                         </div>

                     </div>--%>
                    <div class="panel panel-info wk-accordion-header">
                        <div class="panel-heading">
                            <a data-toggle="collapse" href="#AMenu" data-parent="#accordion"
                               class="click">学生信息管理&nbsp;<span
                                    class=" glyphicon glyphicon-chevron-down"
                                    aria-hidden="true"></span></a>
                        </div>
                        <div id="AMenu" class="panel-collapse collapse">
                            <div class="list-group wk-accordion-list-group">
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/createStudent">
                                    新增学生&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                    aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/createStudentBatch">
                                    批量新增学生&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                      aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/searchStudent">
                                    学生信息查询&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                      aria-hidden="true"></span>
                                </button>
                            </div>
                        </div>

                    </div>
                    <div class="panel panel-info wk-accordion-header">
                        <div class="panel-heading">
                            <a data-toggle="collapse" href="#BMenu" data-parent="#accordion"
                               class="click">教师信息管理&nbsp;<span
                                    class=" glyphicon glyphicon-chevron-down"
                                    aria-hidden="true"></span></a>
                        </div>
                        <div id="BMenu" class="panel-collapse collapse">
                            <div class="list-group wk-accordion-list-group">
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/createTeacher">
                                    新增教师&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                    aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/createTeacherBatch">
                                    批量新增教师&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                      aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/searchTeacher">
                                    教师信息查询&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                      aria-hidden="true"></span>
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-info wk-accordion-header">
                        <div class="panel-heading">
                            <a data-toggle="collapse" href="#CMenu" data-parent="#accordion"
                               class="click">学院信息管理&nbsp;<span
                                    class=" glyphicon glyphicon-chevron-down"
                                    aria-hidden="true"></span></a>
                        </div>

                        <div id="CMenu" class="panel-collapse collapse">
                            <div class="list-group wk-accordion-list-group">
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/createCollege">
                                    新增学院&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                    aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/createCollegeBatch">
                                    批量新增学院&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                      aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/searchCollege">
                                    学院信息查询&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                      aria-hidden="true"></span>
                                </button>
                            </div>
                        </div>

                    </div>

                    <div class="panel panel-info wk-accordion-header">
                        <div class="panel-heading">
                            <a data-toggle="collapse" href="#DMenu" data-parent="#accordion"
                               class="click">课程管理&nbsp;<span
                                    class=" glyphicon glyphicon-chevron-down"
                                    aria-hidden="true"></span></a>
                        </div>
                        <div id="DMenu" class="panel-collapse collapse">
                            <div class="list-group wk-accordion-list-group">
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/createCourse">
                                    新增课程&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                    aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/createCourseBatch">
                                    批量新增课程&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                      aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/searchCourse">
                                    课程信息查询&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                      aria-hidden="true"></span>
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-info wk-accordion-header">
                        <div class="panel-heading">
                            <a data-toggle="collapse" href="#EMenu" data-parent="#accordion"
                               class="click">论坛管理&nbsp;<span
                                    class=" glyphicon glyphicon-chevron-down"
                                    aria-hidden="true"></span></a>
                        </div>
                        <div id="EMenu" class="panel-collapse collapse">
                            <div class="list-group wk-accordion-list-group">
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/searchDiscuss">
                                    话题讨论&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                    aria-hidden="true"></span>
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-info wk-accordion-header">
                        <div class="panel-heading">
                            <a data-toggle="collapse" href="#FMenu" data-parent="#accordion"
                               class="click">公告管理&nbsp;<span
                                    class=" glyphicon glyphicon-chevron-down"
                                    aria-hidden="true"></span></a>
                        </div>
                        <div id="FMenu" class="panel-collapse collapse">
                            <div class="list-group wk-accordion-list-group">
                                <button type="button" class="list-group-item" href="${APP_PATH}/admin/createNotice">
                                    新增公告&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                    aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item" href="${APP_PATH}/admin/searchNotice">
                                    公告信息查询&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                      aria-hidden="true"></span>
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- 添加功能点 -->
                    <div class="panel panel-info wk-accordion-header">
                        <div class="panel-heading">
                            <a data-toggle="collapse" href="#GMenu" data-parent="#accordion"
                               class="click">个人信息管理&nbsp;<span
                                    class=" glyphicon glyphicon-chevron-down"
                                    aria-hidden="true"></span></a>
                        </div>
                        <div id="GMenu" class="panel-collapse collapse">
                            <div class="list-group wk-accordion-list-group">
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/viewAdminInfo">
                                    个人信息查看&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                      aria-hidden="true"></span>
                                </button>
                                <button type="button" class="list-group-item"
                                        href="${APP_PATH}/admin/editAdminInfo">
                                    个人信息修改&nbsp;<span class=" glyphicon glyphicon-hand-right"
                                                      aria-hidden="true"></span>
                                </button>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <div id="rightTable" class="col-lg-10" style="padding:0px;width: 85%">
        <iframe id="mainFrame" src="${APP_PATH}/index/welcome" width="100%" frameborder="0"
                onload="changeFrameHeight()">
        </iframe>
    </div>
</div>
<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">注销登录</h5>
            </div>
            <div class="modal-body" style="text-align: center">

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
        /*let previous = null;
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
        });*/
        $(".click").click(function () {
            if (pre != null && pre.text() !== $(this).text()) {
                if (pre.children(span).hasClass("glyphicon-chevron-up")) {
                    pre.children(span).removeClass("glyphicon-chevron-up");
                    pre.children(span).addClass("glyphicon-chevron-down");
                }
            }
            let children = $(this).children(span);
            if (children.hasClass("glyphicon-chevron-down")) {
                children.removeClass("glyphicon-chevron-down");
                children.addClass("glyphicon-chevron-up");
            } else if (children.hasClass("glyphicon-chevron-up")) {
                children.removeClass("glyphicon-chevron-up");
                children.addClass("glyphicon-chevron-down");
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
                $("#rightTable").css("width", "85%");
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

    };

    changeMenuPanelHeight();

    window.onresize = function () {
        changeMenuPanelHeight();
    };


    //为子菜单的按钮设置点击事件
    $(".list-group-item").each(function () {
        $(this).on("click", function () {
            if ($(".wk-main-menu-item").hasClass("current")) {
                $(".wk-main-menu-item").removeClass("current")
            }
            if ($(".list-group-item").hasClass("current-list-group-item")) {
                $(".list-group-item").removeClass("current-list-group-item")
            }
            $(this).addClass("current-list-group-item");
            let url = $(this).attr("href");
            $("#mainFrame").attr("src", url);
        });
    });
    //为主的按钮设置点击事件
    $(".wk-main-menu-item").each(function () {
        $(this).on("click", function () {
            if ($(".wk-main-menu-item").hasClass("current")) {
                $(".wk-main-menu-item").removeClass("current")
            }
            if ($(".list-group-item").hasClass("current-list-group-item")) {
                $(".list-group-item").removeClass("current-list-group-item")
            }
            $(this).addClass("current");
            let url = $(this).attr("id");
            $("#mainFrame").attr("src", "${APP_PATH}/admin/" + url);
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
            url: "${APP_PATH}/admin/logout",
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
                        window.location.href = "${APP_PATH}/index/login";
                    });
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