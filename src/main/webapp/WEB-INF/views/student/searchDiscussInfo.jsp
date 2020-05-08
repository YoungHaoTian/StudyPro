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
            <li><a href="javascript:void(0)">讨论管理</a></li>
            <li><a href="javascript:void(0)">讨论查询</a></li>
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
                <form class="navbar-form navbar-right" role="search"
                      action="" method="post">
                    <div class="form-group">
                        <label for="courseId" class="control-label wk-filed-label"
                               style="margin-top: 20px">所属课程:</label>
                        <select class="selectpicker" id="courseId" name="courseId">
                            <option value="0">请选择所属课程</option>
                            <c:forEach items="${courses}" var="course">
                                <c:choose>
                                    <c:when test="${course.id == courseId}">
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
                                data-placement="left" title="查询讨论" style="margin-right: 20px">
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
                    <th>讨论标题</th>
                    <th>讨论内容</th>
                    <th>所属课程(<span style="color:green;">课程所属学院)</span></th>
                    <th style="width:300px">发布教师(<span style="color:green;">教师所属学院)</span></th>
                    <th style="width:200px">发布日期</th>
                    <th style="width:200px">选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${discusses}" var="discuss">
                    <tr>
                        <td>${discuss.title}</td>
                        <td>${discuss.content}</td>
                        <td>${discuss.course.name}(<span style="color: green">${discuss.course.college.name}</span>)</td>
                        <td>${discuss.course.teacher.name}(<span style="color: green">${discuss.course.teacher.college.name}</span>)</td>
                        <td><fmt:formatDate value="${discuss.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        <td>
                            <button type="button" class="btn btn-info btn-sm viewReplyBtn"
                                    data-toggle="tooltip" discussId="${discuss.id}"
                                    data-placement="left" title="查看该讨论的所有回复" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                                查看
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
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    $(".search").on("click", function () {
        let courseId = $("#courseId").val();
        window.location.href = "${APP_PATH}/student/searchDiscussInfo?courseId=" + courseId;
    });
    $(".viewReplyBtn").on("click", function () {
        let id = $(this).attr("discussId");
        window.location.href = "${APP_PATH}/student/searchDiscussReply/" + id + "?courseId=${courseId}";
    });
</script>