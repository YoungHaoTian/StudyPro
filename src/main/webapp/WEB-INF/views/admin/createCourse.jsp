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
            <li><a href="javascript:void(0)">课程信息管理</a></li>
            <li><a href="javascript:void(0)">新增课程</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">新增课程 Create Data</div>
            <form action="" method="POST">
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="name" class="control-label wk-filed-label">课程名称:
                                </label>
                                <div class="input-group">
                                    <input required="required" id="name" name="name" type="text"
                                           class="form-control wk-normal-input"
                                           placeholder="请输入课程名称"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="number" class="control-label wk-filed-label">课程编号:
                                </label>
                                <div class="input-group">
                                    <input required="required" id="number" name="number" type="text"
                                           class="form-control wk-normal-input"
                                           placeholder="请输入课程编号"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="collegeId" class="control-label wk-filed-label">所属学院:
                                </label> <select class="selectpicker" id="collegeId" name="collegeId">
                                <c:forEach items="${colleges}" var="college">
                                    <option value="${college.id}">${college.name}</option>
                                </c:forEach>
                            </select>
                            </div>
                        </div>

                        <div class="form-inline">
                            <div class="form-group">
                                <label for="intro" class="control-label wk-filed-label">课程介绍:</label>
                                <div class="input-group">
                                    <textarea required="required" id="intro" name="intro" type="text"
                                           class="form-control wk-long-2col-input"
                                              placeholder="请输入课程介绍"></textarea>
                                </div>
                            </div>

                        </div>

                    </div>
                </div>

                <div class="panel-footer wk-panel-footer">
                    <button type="button" class="btn btn-info" onclick="createCourse()">提&nbsp;&nbsp;交</button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">

    function createCourse() {

    }
</script>