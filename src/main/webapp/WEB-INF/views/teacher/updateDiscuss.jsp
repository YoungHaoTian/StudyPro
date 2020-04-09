<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
</head>

<body>
<div class="row">
    <div class="col-lg-12">
        <ul class="breadcrumb wk-breadcrumb">
            <li><a href="javascript:void(0)">大学生学习平台</a></li>
            <li><a href="javascript:void(0)">讨论管理</a></li>
            <li><a href="javascript:void(0)">编辑讨论</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">
                编辑讨论 Create Data
            </div>
            <form action="" method="POST">
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="courseId" class="control-label wk-filed-label">所属课程:</label>
                                <select class="selectpicker" id="courseId" name="courseId">
                                    <c:forEach items="${courses}" var="course">
                                        <c:choose>
                                            <c:when test="${course.id == course.courseId}">
                                                <option value="${course.id}" selected="selected">${course.name}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${course.id}">${course.name}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="title" class="control-label wk-filed-label">讨论标题: </label>
                                <div class="input-group">
                                    <input required="required" id="title" name="title" type="text"
                                           class="form-control wk-long-2col-input" value="${discuss.title}"
                                           placeholder="请输入讨论标题"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-inline">
                            <div class="form-group">
                                <label for="content" class="control-label wk-filed-label">讨论内容: </label>
                                <div class="input-group">
                                    <textarea required="required" id="content" name="content" type="text"
                                           class="form-control wk-long-2col-input" style="height:300px;"
                                              placeholder="请输入讨论内容">${discuss.content}</textarea>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>

                <div class="panel-footer wk-panel-footer">
                    <button type="button" class="btn btn-info">提&nbsp;&nbsp;交</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>