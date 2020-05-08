<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>大学生学习平台</title>
    <link rel="shortcut icon" href="https://8.url.cn/edu/edu_modules/edu-ui/img/nohash/favicon.ico">
    <link rel="stylesheet" href="${APP_PATH}/resources1/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${APP_PATH}/resources1/bootstrap/css/bootstrap-theme.min.css">
    <script type="text/javascript" src="${APP_PATH}/resources1/js/jquery-3.1.1.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${APP_PATH}/resources1/css/wukong-ui.css">
    <link rel="stylesheet" href="${APP_PATH}/resources1/bootstrap/css/bootstrap-select.min.css">
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap-select.min.js"></script>
    <style>
        th, td {
            text-align: center;
            height: 30px;
            border: #CCCCCC 1px solid;
        }

        td {
            word-wrap: break-word;
        }
    </style>
</head>

<body>
<div class="row">
    <div class="col-lg-12">
        <ul class="breadcrumb wk-breadcrumb">
            <li><a href="javascript:void(0)">大学生学习平台</a></li>
            <li><a href="javascript:void(0)">课程章节管理</a></li>
            <li><a href="javascript:void(0)">章节作业查询</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel wk-table-tools-panel">
            <div class="panel-heading">
                工具栏 Tools
            </div>
            <!-- 搜索 start -->
            <div style="position: absolute;top: -2px;right: 150px;">
                <form class="navbar-form navbar-right" role="search" action="" method="post">
                    <div class="form-group">
                        <button type="button" class="btn btn-info createOnlineTask" data-toggle="tooltip"
                                data-placement="left"
                                title="新增线上作业">
                            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                            新增线上作业
                        </button>
                    </div>
                    <div class="form-group" style="margin-left: 20px">
                        <button type="button" class="btn btn-info createOfflineTask" data-toggle="tooltip"
                                data-placement="left"
                                title="新增线下作业">
                            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                            新增线下作业
                        </button>
                    </div>
                </form>
            </div>
            <!-- 搜索 end -->
        </div>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default  wk-panel">
            <table class="table table-striped table-hover" style="table-layout:fixed;">
                <thead>
                <tr class="info">
                    <th style="width:80px">
                        序号
                    </th>
                    <th>作业标题</th>
                    <th style="width:200px">作业类型</th>
                    <th style="width:200px">发布时间</th>
                    <th style="width:600px">选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:set var="index" value="1"/>
                <c:forEach items="${onlineTasks}" var="onlineTask">
                    <tr>
                        <td>${index}</td>
                        <td>${onlineTask.title}</td>
                        <td><span style="color:green;">线上作业</span></td>
                        <td><fmt:formatDate value="${onlineTask.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        <td>
                            <button type="button" class="btn btn-danger btn-sm deleteOnlineBtn"
                                    data-toggle="tooltip" onlineTaskId="${onlineTask.id}"
                                    data-placement="left" title="删除该作业" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                                删除
                            </button>
                            <button type="button" class="btn btn-info btn-sm updateOnlineBtn"
                                    data-toggle="tooltip" onlineTaskId="${onlineTask.id}"
                                    data-placement="left" title="编辑该作业" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                编辑
                            </button>
                            <button type="button" class="btn btn-info btn-sm searchQuestionBtn"
                                    data-toggle="tooltip" onlineTaskId="${onlineTask.id}"
                                    data-placement="left" title="查看该作业的题目" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                                查看题目
                            </button>
                            <button type="button" class="btn btn-info btn-sm createQuestionBtn"
                                    data-toggle="tooltip" onlineTaskId="${onlineTask.id}"
                                    data-placement="left" title="录入该作业的题目" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
                                录入题目
                            </button>
                            <button type="button" class="btn btn-success btn-sm viewTaskFinishBtn"
                                    data-toggle="tooltip" onlineTaskId="${onlineTask.id}" taskType="online"
                                    data-placement="left" title="查看作业完成情况">
                                <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
                                完成情况
                            </button>
                        </td>
                    </tr>
                    <c:set var="index" value="${index+1}"/>
                </c:forEach>
                <c:forEach items="${offlineTasks}" var="offlineTask">
                    <tr>
                        <td>${index}</td>
                        <td>${offlineTask.title}</td>
                        <td><span style="color:red;">线下作业</span></td>
                        <td><fmt:formatDate value="${offlineTask.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        <td>
                            <button type="button" class="btn btn-danger btn-sm deleteOfflineBtn"
                                    data-toggle="tooltip" offlineTaskId="${offlineTask.id}"
                                    data-placement="left" title="删除该作业" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                                删除
                            </button>
                            <button type="button" class="btn btn-info btn-sm updateOfflineBtn"
                                    data-toggle="tooltip" offlineTaskId="${offlineTask.id}"
                                    data-placement="left" title="编辑该作业" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                编辑
                            </button>
                            <button type="button" class="btn btn-success btn-sm viewTaskFinishBtn"
                                    data-toggle="tooltip" offlineTaskId="${offlineTask.id}" taskType="offline"
                                    data-placement="left" title="查看作业完成情况">
                                <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
                                完成情况
                            </button>
                        </td>
                    </tr>
                    <c:set var="index" value="${index+1}"/>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="panel-footer wk-panel-footer">
            <button type="button" class="btn btn-info"
                    onclick="window.location.href='${APP_PATH}/teacher/searchChapter?courseId=${courseId}&pageNum=${pageNum}'"
                    style="margin-left: 20px">返&nbsp;&nbsp;回
            </button>
        </div>
    </div>
</div>

<div class="modal fade" id="onlineTaskDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">线上作业删除</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <span>删除当前作业将删除对应的题目，你确定这么做吗？</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="onlineTask_delete_btn">确定</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="offlineTaskDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">线下作业删除</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <span>删除当前作业将删除对应的文件，你确定这么做吗？</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="offlineTask_delete_btn">确定</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="createOnlineTaskModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">线上作业添加</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="title" class="col-sm-2 control-label">作业标题：</label>
                        <div class="col-sm-9">
                            <textarea type="text" name="title" class="form-control" id="title"
                                      placeholder="请输入作业标题"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="createOnlineTaskBtn">添加</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="createOfflineTaskModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">线下作业添加</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="title1" class="col-sm-2 control-label">作业标题：</label>
                        <div class="col-sm-9">
                            <textarea type="text" name="title1" class="form-control" id="title1"
                                      rows="4"
                                      placeholder="请输入作业标题"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer" style="margin-bottom: 50px">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="createOfflineTaskBtn">添加</button>
            </div>
        </div>
    </div>
</div>

<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    let id = "";
    //删除线上作业
    $(".deleteOnlineBtn").on("click", function () {
        id = $(this).attr("onlineTaskId");
        console.log(id);
        //删除课程时弹出确认框
        $("#onlineTaskDeleteModal").modal({
                backdrop: "static"
            }
        );
    });
    $("#onlineTask_delete_btn").on("click", function () {
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //确认，发送ajax请求删除即可
        $.ajax({
            url: "${APP_PATH}/teacher/deleteOnlineTask",
            type: "POST",
            dataType: "json",
            data: {
                "id": id
            },
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("删除成功", {time: 1000, icon: 1}, function () {
                        window.location.reload();
                    });
                    <%--window.location.href = "${APP_PATH}/teacher/searchChapterTask/${chapterId}?pageNum=${pageNum}&courseId=${chapterId}";--%>
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        })
    });

    //删除线下作业
    $(".deleteOfflineBtn").on("click", function () {
        id = $(this).attr("offlineTaskId");
        console.log(id);
        //删除课程时弹出确认框
        $("#offlineTaskDeleteModal").modal({
                backdrop: "static"
            }
        );
    });
    $("#offlineTask_delete_btn").on("click", function () {
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //确认，发送ajax请求删除即可
        $.ajax({
            url: "${APP_PATH}/teacher/deleteOfflineTask",
            type: "POST",
            dataType: "json",
            data: {
                "id": id,
                "courseId":${courseId},
                "chapterId":${chapterId}
            },
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("删除成功", {time: 1000, icon: 1}, function () {
                        window.location.reload();
                    });
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        })
    });

    $(".updateOnlineBtn").on("click", function () {
        let id = $(this).attr("onlineTaskId");
        window.location.href = "${APP_PATH}/teacher/editTask/" + id + "?pageNum=0&pageNumber=${pageNum}&courseId=${courseId}&chapterId=${chapterId}&type=online";
    });

    $(".updateOfflineBtn").on("click", function () {
        let id = $(this).attr("offlineTaskId");
        window.location.href = "${APP_PATH}/teacher/editTask/" + id + "?pageNum=0&pageNumber=${pageNum}&courseId=${courseId}&chapterId=${chapterId}&type=offline";
    });

    $(".createQuestionBtn").on("click", function () {
        let id = $(this).attr("onlineTaskId");
        window.location.href = "${APP_PATH}/teacher/createOnlineTaskQuestion/" + id;
    });

    $(".searchQuestionBtn").on("click", function () {
        let id = $(this).attr("onlineTaskId");
        window.location.href = "${APP_PATH}/teacher/searchOnlineTaskQuestion/" + id + "?page=${pageNum}&courseId=${courseId}&chapterId=${chapterId}";
    });
    $(".viewTaskFinishBtn").on("click", function () {
        let taskType = $(this).attr("taskType");
        let id;
        if (taskType === "online") {
            id = $(this).attr("onlineTaskId");
        } else {
            id = $(this).attr("offlineTaskId");
        }
        window.location.href = "${APP_PATH}/teacher/viewTaskFinish/" + id + "?type=" + taskType + "&courseId=${courseId}&pageNum=${pageNum}&chapterId=${chapterId}";
    });

    //增加线上作业
    $(".createOnlineTask").on("click", function () {
        $("#createOnlineTaskModal").modal({
                backdrop: "static"
            }
        );
    });
    $("#createOnlineTaskBtn").on("click", function () {
        let title = $("#title").val();
        let chapterId = ${chapterId};
        if (title.trim() === "") {
            layer.msg("作业标题不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        let loadingIndex = layer.msg('处理中', {icon: 16});
        $.ajax({
            url: "${APP_PATH}/teacher/saveOnlineTask",
            type: "POST",
            // contentType: "application/json",//不使用contentType: “application/json”则data可以是对象,使用contentType: “application/json”则data只能是json字符串
            dataType: "json",
            data: {
                "chapterId": chapterId,
                "title": title.trim()
            },
            success: function (result) {
                layer.close(loadingIndex);
                console.log(result);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    console.log("success");
                    layer.msg("作业添加成功", {time: 1500, icon: 6}, function () {
                        window.location.reload();
                    });
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });

    //增加线下作业
    $(".createOfflineTask").on("click", function () {
        $("#createOfflineTaskModal").modal({
                backdrop: "static"
            }
        );
    });
    $("#createOfflineTaskBtn").on("click", function () {
        let title = $("#title1").val();
        let chapterId = ${chapterId};
        if (title.trim() === "") {
            layer.msg("作业标题不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        let loadingIndex = layer.msg('处理中', {icon: 16});
        $.ajax({
            url: "${APP_PATH}/teacher/saveOfflineTask",
            type: "POST",
            // contentType: "application/json",//不使用contentType: “application/json”则data可以是对象,使用contentType: “application/json”则data只能是json字符串
            dataType: "json",
            data: {
                "chapterId": chapterId,
                "title": title.trim()
            },
            success: function (result) {
                layer.close(loadingIndex);
                console.log(result);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    console.log("success");
                    layer.msg("作业添加成功", {time: 1500, icon: 6}, function () {
                        window.location.reload();
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