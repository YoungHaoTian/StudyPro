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
            <li><a href="javascript:void(0)">我的课程</a></li>
            <li><a href="javascript:void(0)">章节浏览</a></li>
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
        <div class="panel panel-default wk-panel">
            <table class="table table-striped table-hover" style="table-layout:fixed;">
                <thead>
                <tr class="info">
                    <th>章节标题</th>
                    <th>章节内容</th>
                    <th>所属课程</th>
                    <th style="width:200px">发布日期</th>
                    <th style="width:400px">选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${chapters}" var="chapter">
                    <tr>
                        <td>${chapter.title}</td>
                        <td>${chapter.content}</td>
                        <c:if test="${chapter.course!=null}">
                            <td>${chapter.course.name.trim()=="0"?"课程未设置名称":(chapter.course.name.trim()==""?"课程未设置名称":chapter.course.name) }</td>
                        </c:if>
                        <c:if test="${chapter.course==null}">
                            <td>未录入</td>
                        </c:if>
                        <c:if test="${chapter.recordTime!=null}">
                            <td><fmt:formatDate value="${chapter.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        </c:if>
                        <c:if test="${chapter.recordTime==null}">
                            <td>未录入</td>
                        </c:if>
                        <td>
                            <button type="button" class="btn btn-info btn-sm viewFiles"
                                    data-toggle="tooltip" chapterId="${chapter.id}"
                                    data-placement="left" title="查看该章节下所有的课件" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                                查看课件
                            </button>
                            <button type="button" class="btn btn-info btn-sm viewTask"
                                    data-toggle="tooltip" chapterId="${chapter.id}"
                                    data-placement="left" title="查看该章节下所有的作业" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-tasks" aria-hidden="true"></span>
                                查看作业
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="panel-footer wk-panel-footer" style="margin-bottom: 50px">
                <button type="button" class="btn btn-info"
                        onclick="window.location.href='${APP_PATH}/student/searchMyCourseInfo?pageNum=${pageNum}'"
                        style="margin-left: 20px">返&nbsp;&nbsp;回
                </button>
            </div>
    </div>
</div>
</body>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    let ids = "";
    $(".viewFiles").on("click", function () {
        let chapterId = $(this).attr("chapterId");
        console.log(chapterId);
        window.location.href = "${APP_PATH}/student/viewChapterFiles/" + chapterId + "?pageNum=${pageNum}&courseId=${courseId}";
    });
    $(".viewTask").on("click", function () {
        let chapterId = $(this).attr("chapterId");
        console.log(chapterId);
        window.location.href = "${APP_PATH}/student/searchTaskInfo/" + chapterId + "?pageNum=${pageNum}&courseId=${courseId}";
    });
</script>