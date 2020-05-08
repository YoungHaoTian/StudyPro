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
    <link rel="stylesheet" href="${APP_PATH}/resources1/ztree/zTreeStyle.css">
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
            <li><a href="javascript:void(0);">已完成作业</a></li>
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
            <div style="position: absolute;top: -11px;left: 240px;">
                <form class="navbar-form navbar-right" role="search" action="" method="post">
                    <div class="form-group">
                        <label for="courseId" class="control-label wk-filed-label"
                               style="margin-top: 20px">所属课程:</label>
                        <select class="selectpicker" id="courseId" name="courseId">
                            <option value="0">请选择所属课程</option>
                            <c:forEach items="${courses}" var="course">
                                <c:choose>
                                    <c:when test="${courseId==course.id}">
                                        <option value="${course.id}"
                                                selected="selected">${course.name}(${course.teacher.name})
                                        </option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${course.id}">${course.name}(${course.teacher.name})</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group" style="margin-left: 20px">
                        <button type="button" id="search" class="btn btn-success search" data-toggle="tooltip"
                                data-placement="left" title="点击查询" style="margin-right: 20px">
                            <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                            查询
                        </button>
                    </div>
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
                    <th>所属章节</th>
                    <th>作业标题</th>
                    <th style="width: 200px">发布时间</th>
                    <c:if test="${sessionScope.taskType.equals('online')}">
                        <th style="width: 200px">分数<span style="color:red;">/总分</span></th>
                    </c:if>
                    <c:if test="${sessionScope.taskType.equals('offline')}">
                        <th style="width: 300px">文件</th>
                    </c:if>
                    <th style="width: 200px">完成时间</th>
                    <th style="width: 100px">作业类型</th>
                    <th style="width: 200px">选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${sessionScope.taskType.equals('online')}">
                    <c:forEach items="${onlineTasks}" var="onlineTask">
                        <tr>
                            <td style="display: table-cell;vertical-align: middle">${onlineTask.courseChapter.title}</td>
                            <td style="display: table-cell;vertical-align: middle">${onlineTask.title}</td>
                            <td style="display: table-cell;vertical-align: middle"><span
                                    style="color: red"><fmt:formatDate value="${onlineTask.recordTime}"
                                                                       pattern="yyyy-MM-dd  HH:mm:ss"/></span></td>
                            <td style="display: table-cell;vertical-align: middle">
                                <span style="color: #0B58A8">${onlineTasksMap.get(onlineTask.id).score}</span>
                                <span style="color: red">/${totalMap.get(onlineTask.id)}</span>
                            </td>
                            <td style="display: table-cell;vertical-align: middle"><span
                                    style="color: blue"><fmt:formatDate
                                    value="${onlineTasksMap.get(onlineTask.id).recordTime}"
                                    pattern="yyyy-MM-dd  HH:mm:ss"/></span></td>
                            <td style="display: table-cell;vertical-align: middle">线上作业</td>
                            <td>
                                <button type="button" class="btn btn-info  btn-sm enterTask"
                                        data-toggle="tooltip" taskId="${onlineTask.id}"
                                        chapterId="${onlineTask.chapterId}"
                                        data-placement="left" title="查看题目">
                                    <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                    查看
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                <c:if test="${sessionScope.taskType.equals('offline')}">
                    <c:forEach items="${offlineTasks}" var="offlineTask">
                        <tr>
                            <td style="display: table-cell;vertical-align: middle">${offlineTask.courseChapter.title}</td>
                            <td style="display: table-cell;vertical-align: middle">${offlineTask.title}</td>
                            <td style="display: table-cell;vertical-align: middle"><span
                                    style="color: red"><fmt:formatDate value="${offlineTask.recordTime}"
                                                                       pattern="yyyy-MM-dd  HH:mm:ss"/></span></td>
                            <td>
                                <a class="btn btn-link"
                                   href="${APP_PATH}/student/downloadOfflineTaskFile/${offlineTasksMap.get(offlineTask.id).id}">
                                        ${offlineTasksMap.get(offlineTask.id).path.split("_",3)[2]}
                                </a>
                            </td>
                            <td style="display: table-cell;vertical-align: middle"><span
                                    style="color: blue"><fmt:formatDate
                                    value="${offlineTasksMap.get(offlineTask.id).recordTime}"
                                    pattern="yyyy-MM-dd  HH:mm:ss"/></span></td>
                            <td style="display: table-cell;vertical-align: middle">线下作业</td>
                            <td>
                                <button type="button" class="btn btn-info btn-sm reUploadOfflineTask"
                                        data-toggle="tooltip" studentTaskId="${offlineTasksMap.get(offlineTask.id).id}"
                                        taskId="${offlineTask.id}" chapterId="${offlineTask.courseChapter.id}"
                                        data-placement="left" title="重新上传作业">
                                    <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                    重新上传作业
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                </tbody>
            </table>
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
                            <input type="file" id="file1" name="file1" studentTaskId="0" taskId="0" chapterId="0"
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
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    $(".enterTask").on("click", function () {
        let taskId = $(this).attr("taskId");
        let chapterId = $(this).attr("chapterId");
        window.location.href = "${APP_PATH}/student/enterTask/" + taskId + "?courseId=${courseId}&chapterId=" + chapterId + "&flag=1";
    });

    $("#search").on("click", function () {
        let courseId = $("#courseId").val();
        window.location.href = "${APP_PATH}/student/searchStudentTaskInfo?courseId=" + courseId;
    });

    $(".viewOnlineTask").on("click", function () {
        window.location.href = "${APP_PATH}/student/searchStudentTaskInfo?taskType=online&courseId=${courseId}";
    });
    $(".viewOfflineTask").on("click", function () {
        window.location.href = "${APP_PATH}/student/searchStudentTaskInfo?taskType=offline&courseId=${courseId}";
    });
    $(".reUploadOfflineTask").on("click", function () {
        let studentTaskId = $(this).attr("studentTaskId");
        let taskId = $(this).attr("taskId");
        let chapterId = $(this).attr("chapterId");
        $("#file1").attr("studentTaskId", studentTaskId);
        $("#file1").attr("taskId", taskId);
        $("#file1").attr("chapterId", chapterId);
        $("#offlineTaskReUpdateModal").modal({
            backdrop: "static"
        });
    });
    $("#offlineTaskReUpdateBtn").on("click", function () {
        let file = $("#file1").val();
        let studentTaskId = $("#file1").attr("studentTaskId");
        let taskId = $("#file1").attr("taskId");
        let chapterId = $("#file1").attr("chapterId");
        if (file === "") {
            layer.msg("请选择需要上传的文件", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //上传文件
        let formData = new FormData();
        let loadingIndex = layer.msg('处理中', {icon: 16});
        formData.append("file", $("#file1")[0].files[0]);
        formData.append("chapterId", chapterId);
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
</script>