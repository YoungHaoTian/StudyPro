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
                    <th>发布时间</th>
                    <th>考试分数</th>
                    <th>完成时间</th>
                    <th>选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${tasks}" var="task">
                    <tr>
                        <td>${task.courseChapter.title}</td>
                        <td>${task.title}</td>
                        <td style="color: red"><fmt:formatDate value="${task.recordTime}"
                                                               pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        <td>${taskMap.get(task.id).score}</td>
                        <td style="color: blue"><fmt:formatDate value="${taskMap.get(task.id).recordTime}"
                                                                pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        <td>
                            <button type="button" class="btn btn-info enterTask"
                                    data-toggle="tooltip" taskId="${task.id}" chapterId="${task.chapterId}"
                                    data-placement="left" title="查看题目" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                进入
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
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
</script>