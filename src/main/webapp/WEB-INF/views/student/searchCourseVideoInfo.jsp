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
            <li><a href="javascript:void(0)">大学生学习平台</a></li>
            <li><a href="javascript:void(0)">课程文件管理</a></li>
            <li><a href="javascript:void(0)">在线课程视频</a></li>
        </ul>
    </div>
</div>
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel wk-table-tools-panel">
            <div class="panel-heading">
                工具栏 Tools
            </div>
            <div style="position: absolute;top: -11px;left: 240px;">
                <form class="navbar-form navbar-right" role="search" action="" method="post">
                    <div class="form-group">
                        <label for="courseId" class="control-label wk-filed-label"
                               style="margin-top: 20px">所属课程:</label>
                        <select class="selectpicker" id="courseId" name="courseId">
                            <option value="0">请选择所属课程</option>
                            <c:forEach items="${course}" var="course">
                                <c:choose>
                                    <c:when test="${courseId==course.id}">
                                        <option value="${course.id}"
                                                selected="selected">${course.name}:${course.teacher.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${course.id}">${course.name}:${course.teacher.name}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group" style="margin-left: 20px">
                        <button type="button" id="search" class="btn btn-success search" data-toggle="tooltip"
                                data-placement="left" title="查询课程" style="margin-right: 20px">
                            <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                            查询
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
            <table class="table table-striped table-hover">
                <thead>
                <tr class="info">
                    <th>文件名</th>
                    <th>所属课程</th>
                    <th>所属章节</th>
                    <th>发布教师</th>
                    <th>上传时间</th>
                    <th>选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${courses}" var="course">
                    <c:forEach items="${course.chapters}" var="chapter">
                        <c:forEach items="${chapter.videos}" var="video">
                            <tr>
                                <td>${video.path.substring(video.path.indexOf("_")+1)}</td>
                                <td>${course.name}</td>
                                <td>${chapter.title}</td>
                                <td>${course.teacher.name}(${course.teacher.college.name})</td>
                                <td><fmt:formatDate value="${video.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                                <td>
                                    <button class="btn btn-success btn-sm viewVideo" data-toggle="tooltip"
                                            data-placement="left"
                                            title="在线观看该视频" videoId="${video.id}" style="margin-right: 20px">
                                        <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                                        在线观看
                                    </button>
                                    <button class="btn btn-success btn-sm downloadVideo" data-toggle="tooltip"
                                            data-placement="left"
                                            title="下载该视频" videoId="${video.id}" style="margin-right: 20px">
                                        <span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>
                                        下载
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:forEach>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    //在线观看
    $(".viewVideo").on("click", function () {
        let id = $(this).attr("videoId");
        window.location.href = "${APP_PATH}/student/viewCourseVideo/" + id;
    });

    $(".search").on("click", function () {
        let courseId = $("#courseId").val();
        window.location.href = "${APP_PATH}/student/searchCourseVideoInfo?courseId=" + courseId;
    });
    $(".downloadVideo").on("click", function () {
        let id = $(this).attr("videoId");
        window.location.href = "${APP_PATH}/student/downloadCourseVideo/" + id;
    });

</script>