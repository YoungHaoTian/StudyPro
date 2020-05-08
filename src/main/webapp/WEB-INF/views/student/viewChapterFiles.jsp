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
            <li><a href="javascript:void(0)">章节浏览</a></li>
            <li><a href="javascript:void(0)">课件浏览</a></li>
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
            <div style="position: absolute;top: -11px;left: 200px;">
                <form class="navbar-form navbar-right" role="search"
                      action="" method="post">
                    <div class="form-group">
                        <label for="minTime" class="control-label wk-filed-label" style="margin-top: 20px">上传时间:</label>
                        <input type="date" class="form-control" name="name" id="minTime"
                               value="${minTime}"/>
                        <label for="maxTime" class="control-label wk-filed-label" style="margin-top: 20px">到:</label>
                        <input type="date" class="form-control" name="name" id="maxTime"
                               value="${maxTime}"/>
                    </div>
                    <div class="form-group" style="margin-left: 20px">
                        <button type="button" id="search" class="btn btn-success search" data-toggle="tooltip"
                                data-placement="left" title="查询章节课件" style="margin-right: 20px">
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
        <div class="panel panel-default wk-panel">
            <table class="table table-striped table-hover">
                <thead>
                <tr class="info">
                    <th>文件名</th>
                    <th>上传时间</th>
                    <th>文件类型</th>
                    <th>选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${files}" var="file">
                    <tr>
                        <td>${file.path.substring(file.path.indexOf("_")+1)}</td>
                        <td><fmt:formatDate value="${file.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        <td>课件</td>
                        <td>
                            <button class="btn btn-success btn-sm downloadFile" data-toggle="tooltip"
                                    data-placement="left"
                                    title="下载当前文档" fileId="${file.id}" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>
                                下载文档
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <c:forEach items="${videos}" var="video">
                    <tr>
                        <td>${video.path.substring(video.path.indexOf("_")+1)}</td>
                        <td><fmt:formatDate value="${video.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        <td>视频</td>
                        <td>
                            <button class="btn btn-success btn-sm viewVideo" data-toggle="tooltip" data-placement="left"
                                    title="在线观看该视频" videoId="${video.id}" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                                在线观看
                            </button>
                            <button class="btn btn-success btn-sm downloadVideo" data-toggle="tooltip"
                                    data-placement="left"
                                    title="下载该视频" videoId="${video.id}">
                                <span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>
                                下载视频
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="panel-footer wk-panel-footer" style="margin-bottom:50px">
            <button type="button" class="btn btn-info" onclick="back()">返&nbsp;&nbsp;回</button>
        </div>
    </div>
</div>
</body>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">

    //下载文档
    $(".downloadFile").on("click", function () {
        let id = $(this).attr("fileId");
        window.location.href = "${APP_PATH}/student/downloadCourseFile/" + id;
    });

    //在线观看
    $(".viewVideo").on("click", function () {
        let id = $(this).attr("videoId");
        window.location.href = "${APP_PATH}/student/viewCourseVideo/" + id;
    });

    //查询按钮
    $("#search").on("click", function () {
        let minTime = $("#minTime").val().trim();
        let maxTime = $("#maxTime").val().trim();
        window.location.href = "${APP_PATH}/student/viewChapterFiles/${id}?courseId=${courseId}&minTime=" + minTime + "&maxTime=" + maxTime + "&pageNum=${pageNum}";
    });

    function back() {
        window.location.href = "${APP_PATH}/student/searchChapter?courseId=${courseId}&pageNum=${pageNum}";
    }

    $(".downloadVideo").on("click", function () {
        let id = $(this).attr("videoId");
        window.location.href = "${APP_PATH}/student/downloadCourseVideo/" + id;
    });
</script>