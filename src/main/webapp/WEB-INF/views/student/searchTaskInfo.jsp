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
                    <th style="width: 200px">完成状态</th>
                    <th style="width: 200px">完成时间</th>
                    <th style="width: 200px">分数</th>
                    <th style="width: 200px">选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${tasks}" var="task">
                    <tr>
                        <td>${task.title}</td>
                        <td><fmt:formatDate value="${task.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        <c:if test="${taskMap.get(task.id)!=null}">
                            <td><span style="color: #0B58A8">已完成</span></td>

                            <td><span style="color: #0B58A8"><fmt:formatDate value="${taskMap.get(task.id).recordTime}"
                                                                             pattern="yyyy-MM-dd  HH:mm:ss"/></span>
                            </td>
                            <td><span style="color: #0B58A8">${taskMap.get(task.id).score}</span></td>
                        </c:if>
                        <c:if test="${taskMap.get(task.id)==null}">
                            <td><span style="color:red;">未完成</span></td>
                            <td></td>
                            <td></td>
                        </c:if>
                        <td>
                            <button type="button" class="btn btn-info enterTask"
                                    data-toggle="tooltip" taskId="${task.id}"
                                    data-placement="left" title="完成作业" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                进入
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="panel-footer wk-panel-footer" style="margin-top: 50px">
                <button type="button" class="btn btn-info"
                        onclick="window.location.href='${APP_PATH}/student/searchCourseChapterInfo?courseId=${courseId}'">
                    返&nbsp;&nbsp;回
                </button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script type="text/javascript">
    $(".enterTask").on("click", function () {
        let taskId = $(this).attr("taskId");
        window.location.href = "${APP_PATH}/student/enterTask/" + taskId + "?chapterId=${chapterId}&courseId=${courseId}";
    });
</script>
