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
            <form id="discussData" action="" method="POST">
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="courseId" class="control-label wk-filed-label">所属课程:</label>
                                <select class="selectpicker btn-group bootstrap-select" id="courseId" name="courseId"
                                        style="width:500px;">
                                    <option value="0">请选择所属课程</option>
                                    <c:forEach items="${courses}" var="course">
                                        <c:choose>
                                            <c:when test="${course.id == discuss.courseId}">
                                                <option value="${course.id}"
                                                        selected="selected">${course.name}(${course.college.name}):${course.teacher.name}(${course.teacher.college.name})
                                                </option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${course.id}">${course.name}(${course.college.name}):${course.teacher.name}(${course.teacher.college.name})</option>
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
                                    <%--<input required="required" id="title" name="title" type="text" maxlength="50"
                                           class="form-control wk-long-2col-input" value="${discuss.title}"
                                           placeholder="请输入讨论标题"/>--%>
                                    <textarea required="required" id="title" name="title" type="text"
                                              class="form-control wk-long-2col-input" rows="2"
                                              placeholder="请输入讨论内容">${discuss.title}</textarea>
                                </div>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="content" class="control-label wk-filed-label">讨论内容: </label>
                                <div class="input-group">
                                    <textarea required="required" id="content" name="content" type="text"
                                              class="form-control wk-long-2col-input" rows="5"
                                              placeholder="请输入讨论内容">${discuss.content}</textarea>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>

            </form>
        </div>
        <div class="panel-footer wk-panel-footer" style="margin-bottom: 50px">
            <button type="button" class="btn btn-info" onclick="editDiscuss()">提&nbsp;&nbsp;交</button>
            <button type="button" class="btn btn-info" onclick="back()" style="margin-left: 30px">
                返&nbsp;&nbsp;回
            </button>
            <button type="button" class="btn btn-info" onclick="$('#discussData')[0].reset();"
                    style="margin-left: 30px">
                重&nbsp;&nbsp;填
            </button>
        </div>
    </div>
</div>
</body>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    function editDiscuss() {
        let title = $("#title").val();
        let content = $("#content").val();
        let courseId = $("#courseId").val();
        if (courseId === "0") {
            layer.msg("请选择所属课程", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (title.trim() === "") {
            layer.msg("讨论标题不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (title.indexOf(" ") === 0) {
            layer.msg("讨论标题不能以空格开头，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (content.trim() === "") {
            layer.msg("讨论内容不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (content.indexOf(" ") === 0) {
            layer.msg("讨论内容不能以空格开头，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        let loadingIndex = layer.msg('处理中', {icon: 16});
        let data = $("#discussData").serialize();
        data = decodeURIComponent(data);
        $.ajax({
            url: "${APP_PATH}/admin/editDiscuss/${discuss.id}",
            type: "POST",
            // contentType: "application/json",//不使用contentType: “application/json”则data可以是对象,使用contentType: “application/json”则data只能是json字符串
            dataType: "json",
            data: data,
            success: function (result) {
                layer.close(loadingIndex);
                console.log(result);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 3000, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    console.log("success");
                    layer.msg("修改成功", {time: 1500, icon: 6}, function () {
                    });
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    }

    function back() {
        window.location.href = "${APP_PATH}/admin/searchDiscuss?&pageNum=${pageNum}";
    }
</script>