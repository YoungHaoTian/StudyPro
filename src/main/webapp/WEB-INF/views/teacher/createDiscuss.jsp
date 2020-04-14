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
    <link rel="stylesheet" href="${APP_PATH}/resources1/css/wukong-ui.css">
    <link rel="stylesheet" href="${APP_PATH}/resources1/bootstrap/css/bootstrap-select.min.css">
    <script type="text/javascript" src="${APP_PATH}/resources1/js/jquery-3.1.1.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap-select.min.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap.min.js"></script>
</head>

<body>
<div class="row">
    <div class="col-lg-12">
        <ul class="breadcrumb wk-breadcrumb">
            <li><a href="javascript:void(0)">大学生学习平台</a></li>
            <li><a href="javascript:void(0)">讨论管理</a></li>
            <li><a href="javascript:void(0)">新增讨论</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">
                新增讨论 Create Data
            </div>
            <form id="discussData" action="" method="POST">
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="courseId" class="control-label wk-filed-label">所属课程: </label>
                                <select id="courseId" class="selectpicker" name="courseId">
                                <option value="0">请选择所属课程</option>
                                <c:forEach items="${courses}" var="course">
                                    <option value="${course.id}">${course.name}(${course.college.name})</option>
                                </c:forEach>
                            </select>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="title" class="control-label wk-filed-label">讨论标题: </label>
                                <div class="input-group">
                                    <textarea required="required" name="title" type="text"
                                              class="form-control wk-long-2col-input" id="title" placeholder="请输入公告标题"></textarea>
                                </div>
                            </div>
                        </div>

                        <div class="form-inline">
                            <div class="form-group">
                                <label for="content" class="control-label wk-filed-label">讨论内容: </label>
                                <div class="input-group">
                                    <textarea required="required" id="content" name="content" type="text"
                                              class="form-control wk-long-2col-input"
                                              placeholder="请输入公告内容"></textarea>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="panel-footer wk-panel-footer">
                    <button type="button" class="btn btn-info" onclick="createDiscuss();">提&nbsp;&nbsp;交</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    function createDiscuss() {
        let courseId = $("#courseId").val();
        let title = $("#title").val();
        let content = $("#content").val();
        if (courseId === "0") {
            layer.msg("请选择所属课程", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (title.trim() === "") {
            layer.msg("请输入讨论标题", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (title.indexOf(" ") === 0) {
            layer.msg("讨论标题不能以空格开头，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (content.trim() === "") {
            layer.msg("请输入讨论内容", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        let loadingIndex = layer.msg('处理中', {icon: 16});
        let data = $("#discussData").serialize();
        data = decodeURIComponent(data);
        $.ajax({
            url: "${APP_PATH}/teacher/saveDiscuss",
            type: "POST",
            // contentType: "application/json",//不使用contentType: “application/json”则data可以是对象,使用contentType: “application/json”则data只能是json字符串
            dataType: "json",
            data: data,
            success: function (result) {
                layer.close(loadingIndex);
                console.log(result);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    console.log("success");
                    layer.msg("讨论添加成功", {time: 1500, icon: 6}, function () {
                    });
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    }
</script>