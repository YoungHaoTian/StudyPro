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
    <link rel="stylesheet" href="${APP_PATH}/resources1/css/wukong-ui.css">
    <link rel="stylesheet" href="${APP_PATH}/resources1/bootstrap/css/bootstrap-select.min.css">
    <script type="text/javascript" src="${APP_PATH}/resources1/js/jquery-3.1.1.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap-select.min.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap.min.js"></script>
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
            <li><a href="javascript:void(0);">大学生学习平台</a></li>
            <li><a href="javascript:void(0);">作业管理</a></li>
            <li><a href="javascript:void(0);">作业查询</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel wk-table-tools-panel">
            <div class="panel-heading">
                工具栏 Tools
            </div>
            <div style="position: absolute;top: 0px;right: 150px;">
                <form class="navbar-form navbar-right" role="search" action="" method="post">
                    <div class="form-group">
                        <button type="button" class="btn btn-primary viewOnlineTask" data-toggle="tooltip"
                                data-placement="left" style="margin-right: 20px"
                                title="查看线上作业">
                            <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                            线上作业
                        </button>
                    </div>
                    <div class="form-group">
                        <button type="button" class="btn btn-primary viewOfflineTask" data-toggle="tooltip"
                                data-placement="left"
                                title="查看线下作业">
                            <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                            线下作业
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default  wk-panel">
            <table class="table table-striped table-hover" style="table-layout:fixed;">
                <thead>
                <tr class="info">
                    <th>作业标题</th>
                    <th style="width: 200px">发布时间</th>
                    <th>完成状态</th>
                    <th style="width: 200px">完成时间</th>
                    <c:if test="${sessionScope.taskType.equals('online')}">
                        <th style="width: 200px">分数<span style="color:red;">/总分</span></th>

                    </c:if>
                    <c:if test="${sessionScope.taskType.equals('offline')}">
                        <th style="width: 200px">文件</th>

                    </c:if>
                    <td style="width: 100px">作业类型</td>
                    <th style="width: 200px">选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${sessionScope.taskType.equals('online')}">
                    <c:forEach items="${onlineTasks}" var="onlineTask">
                        <tr>
                            <td style="display: table-cell;vertical-align: middle"><span>${onlineTask.title}</span></td>
                            <td style="display: table-cell;vertical-align: middle;"><span
                                    style="color: red"><fmt:formatDate
                                    value="${onlineTask.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></span></td>
                            <c:if test="${onlineTasksMap.get(onlineTask.id)!=null}">
                                <td style="display: table-cell;vertical-align: middle"><span
                                        style="color: #0B58A8">已完成</span></td>
                                <td style="display: table-cell;vertical-align: middle"><span
                                        style="color: blue"><fmt:formatDate
                                        value="${onlineTasksMap.get(onlineTask.id).recordTime}"
                                        pattern="yyyy-MM-dd  HH:mm:ss"/></span>
                                </td>
                                <td><span style="color: #0B58A8">${onlineTasksMap.get(onlineTask.id).score}</span><span
                                        style="color: red">/${total.get(onlineTask.id)}</span></td>
                                <td style="display: table-cell;vertical-align: middle">线上作业</td>
                                <td style="display: table-cell;vertical-align: middle">
                                    <button type="button" class="btn btn-info btn-sm enterTask"
                                            data-toggle="tooltip" taskId="${onlineTask.id}"
                                            data-placement="left" title="完成作业">
                                        <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                        查看
                                    </button>
                                </td>
                            </c:if>
                            <c:if test="${onlineTasksMap.get(onlineTask.id)==null}">
                                <td style="display: table-cell;vertical-align: middle"><span
                                        style="color:red;">未完成</span></td>
                                <td></td>
                                <td></td>
                                <td style="display: table-cell;vertical-align: middle">线上作业</td>
                                <td style="display: table-cell;vertical-align: middle">
                                    <button type="button" class="btn btn-info btn-sm enterTask"
                                            data-toggle="tooltip" taskId="${onlineTask.id}"
                                            data-placement="left" title="完成作业">
                                        <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                        进入
                                    </button>
                                </td>
                            </c:if>

                        </tr>
                    </c:forEach>
                </c:if>
                <c:if test="${sessionScope.taskType.equals('offline')}">
                    <c:forEach items="${offlineTasks}" var="offlineTask">
                        <tr>
                            <td style="display: table-cell;vertical-align: middle">${offlineTask.title}</td>
                            <td style="display: table-cell;vertical-align: middle"><fmt:formatDate
                                    value="${offlineTask.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                            <c:if test="${offlineTasksMap.get(offlineTask.id)!=null}">
                                <td style="display: table-cell;vertical-align: middle"><span
                                        style="color: #0B58A8">已完成</span></td>
                                <td style="display: table-cell;vertical-align: middle"><span
                                        style="color: #0B58A8"><fmt:formatDate
                                        value="${offlineTasksMap.get(offlineTask.id).recordTime}"
                                        pattern="yyyy-MM-dd  HH:mm:ss"/></span>
                                </td>
                                <td style="display: table-cell;vertical-align: middle">
                                    <a class="btn btn-link" title="下载该文件"
                                       href="${APP_PATH}/student/downloadOfflineTaskFile/${offlineTasksMap.get(offlineTask.id).id}">
                                            ${offlineTasksMap.get(offlineTask.id).path.split("_",3)[2]}
                                    </a>
                                </td>
                                <td style="display: table-cell;vertical-align: middle">线下作业</td>
                                <td style="display: table-cell;vertical-align: middle">
                                    <button type="button" class="btn btn-success btn-sm reUploadOfflineTask"
                                            data-toggle="tooltip"
                                            studentTaskId="${offlineTasksMap.get(offlineTask.id).id}"
                                            taskId="${offlineTask.id}"
                                            data-placement="left" title="重新上传作业">
                                        <span class="glyphicon glyphicon-upload" aria-hidden="true"></span>
                                        重新上传作业
                                    </button>
                                </td>
                            </c:if>
                            <c:if test="${offlineTasksMap.get(offlineTask.id)==null}">
                                <td style="display: table-cell;vertical-align: middle"><span
                                        style="color:red;">未完成</span></td>
                                <td></td>
                                <td></td>
                                <td style="display: table-cell;vertical-align: middle">线下作业</td>
                                <td style="display: table-cell;vertical-align: middle">
                                    <button type="button" class="btn btn-success btn-sm uploadOfflineTask"
                                            data-toggle="tooltip" taskId="${offlineTask.id}"
                                            data-placement="left" title="上传作业">
                                        <span class="glyphicon glyphicon-upload" aria-hidden="true"></span>
                                        上传作业
                                    </button>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </c:if>
                </tbody>
            </table>
        </div>
        <div class="panel-footer wk-panel-footer">
            <button type="button" class="btn btn-info"
                    onclick="back()">
                返&nbsp;&nbsp;回
            </button>
        </div>
    </div>
</div>
<div class="modal fade" id="offlineTaskUpdateModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">作业文件上传</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="file" class="col-sm-2 control-label">选择文件:</label>
                        <div class="col-sm-9">
                            <input type="file" id="file" name="file" taskId="0" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="offlineTaskUpdateBtn">确定</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="offlineTaskReUpdateModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">作业文件重新上传</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="file1" class="col-sm-2 control-label">选择文件:</label>
                        <div class="col-sm-9">
                            <input type="file" id="file1" name="file1" studentTaskId="0" taskId="0"
                                   class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="offlineTaskReUpdateBtn">确定</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script type="text/javascript">
    $(".enterTask").on("click", function () {
        let taskId = $(this).attr("taskId");
        if (${pageNum!=null}) {
            window.location.href = "${APP_PATH}/student/enterTask/" + taskId + "?chapterId=${chapterId}&courseId=${courseId}&pageNum=${pageNum}";
        } else {
            window.location.href = "${APP_PATH}/student/enterTask/" + taskId + "?chapterId=${chapterId}&courseId=${courseId}";
        }
    });

    $(".viewOnlineTask").on("click", function () {
        if (${pageNum!=null}) {
            window.location.href = "${APP_PATH}/student/searchTaskInfo/${chapterId}?taskType=online&courseId=${courseId}&pageNum=${pageNum}";
        } else {
            window.location.href = "${APP_PATH}/student/searchTaskInfo/${chapterId}?taskType=online&courseId=${courseId}";
        }
    });

    $(".viewOfflineTask").on("click", function () {
        if (${pageNum!=null}) {
            window.location.href = "${APP_PATH}/student/searchTaskInfo/${chapterId}?taskType=offline&courseId=${courseId}&pageNum=${pageNum}";
        } else {
            window.location.href = "${APP_PATH}/student/searchTaskInfo/${chapterId}?taskType=offline&courseId=${courseId}";
        }

    });
    $(".uploadOfflineTask").on("click", function () {
        let taskId = $(this).attr("taskId");
        $("#file").attr("taskId", taskId);
        $("#offlineTaskUpdateModal").modal({
            backdrop: "static"
        });
    });
    $("#offlineTaskUpdateBtn").on("click", function () {
        let file = $("#file").val();
        let taskId = $("#file").attr("taskId");
        if (file === "") {
            layer.msg("请选择需要上传的文件", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //上传文件
        let formData = new FormData();
        let loadingIndex = layer.msg('处理中', {icon: 16});
        formData.append("file", $("#file")[0].files[0]);
        formData.append("chapterId", ${chapterId});
        formData.append("courseId", ${courseId});
        $.ajax({
            url: "${APP_PATH}/student/uploadOfflineTask/" + taskId,
            type: 'POST',
            dataType: 'json',
            crossDomain: true, // 如果用到跨域，需要后台开启CORS
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("作业文件上传成功", {time: 1000, icon: 1}, function () {
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
    $(".reUploadOfflineTask").on("click", function () {
        let studentTaskId = $(this).attr("studentTaskId");
        let taskId = $(this).attr("taskId");
        $("#file1").attr("studentTaskId", studentTaskId);
        $("#file1").attr("taskId", taskId);
        $("#offlineTaskReUpdateModal").modal({
            backdrop: "static"
        });
    });
    $("#offlineTaskReUpdateBtn").on("click", function () {
        let file = $("#file1").val();
        let studentTaskId = $("#file1").attr("studentTaskId");
        let taskId = $("#file1").attr("taskId");
        if (file === "") {
            layer.msg("请选择需要上传的文件", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //上传文件
        let formData = new FormData();
        let loadingIndex = layer.msg('处理中', {icon: 16});
        formData.append("file", $("#file1")[0].files[0]);
        formData.append("chapterId", ${chapterId});
        formData.append("courseId", ${courseId});
        formData.append("taskId", taskId);
        $.ajax({
            url: "${APP_PATH}/student/reUploadOfflineTask/" + studentTaskId,
            type: 'POST',
            dataType: 'json',
            crossDomain: true, // 如果用到跨域，需要后台开启CORS
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("作业文件更新成功", {time: 1000, icon: 1}, function () {
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

    function back() {
        if (${pageNum!=null}) {
            window.location.href = "${APP_PATH}/student/searchChapter?courseId=${courseId}&pageNum=${pageNum}";
        } else {
            window.location.href = "${APP_PATH}/student/searchCourseChapterInfo?courseId=${courseId}";
        }
        ${sessionScope.remove('taskType')}
    }
</script>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
