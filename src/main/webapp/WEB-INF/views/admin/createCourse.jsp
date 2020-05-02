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
            <form id="courseData" action="" method="POST">
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="name" class="control-label wk-filed-label">课程名称:</label>
                                <div class="input-group">
                                    <input required="required" id="name" name="name" type="text" maxlength="30"
                                           class="form-control wk-normal-input"
                                           placeholder="请输入课程名称"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="number" class="control-label wk-filed-label">课程编号:</label>
                                <div class="input-group">
                                    <input required="required" id="number" name="number" type="text" maxlength="18"
                                           class="form-control wk-normal-input"
                                           placeholder="请输入课程编号"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="collegeId" class="control-label wk-filed-label">所属学院:</label>
                                <select class="selectpicker" id="collegeId" name="collegeId">
                                    <option value="0">请选择所属学院</option>
                                    <c:forEach items="${colleges}" var="college">
                                        <option value="${college.id}">${college.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="teacherId" class="control-label wk-filed-label">授课教师:</label>
                                <select class="selectpicker" id="teacherId" name="teacherId">
                                    <option value="0">请选择授课教师</option>
                                    <c:forEach items="${teachers}" var="teacher">
                                        <option value="${teacher.id}">${teacher.name}:${teacher.college.name}:${teacher.number}</option>
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


            </form>
        </div>
        <div class="panel-footer wk-panel-footer">
            <button type="button" class="btn btn-info" onclick="createCourse()">提&nbsp;&nbsp;交</button>
            <button type="button" class="btn btn-info" onclick="$('#courseData')[0].reset();"
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
    let numbers = /^[a-zA-Z0-9]{6,18}$/;

    // let names = /^[a-zA-Z0-9\u0020\u3000\u4e00-\u9fa5]+$/;
    function createCourse() {
        let name = $("#name").val();
        let number = $("#number").val();
        let intro = $("#intro").val();
        let collegeId = $("#collegeId").val();
        let teacherId = $("#teacherId").val();
        if (name.trim() === "") {
            layer.msg("课程名称不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (name.indexOf(" ") === 0) {
            layer.msg("课程名称不能以空格开头，请重新输入", {time: 2000, icon: 5, shift: 6}, function () {
            });
            return;
        }
        /*if (!names.test(name)) {
            layer.msg("课程名由数字、字母、中文组成", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }*/
        if (number.trim() === "") {
            layer.msg("课程编号不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (number.indexOf(" ") !== -1) {
            layer.msg("课程编号不能包含空格，请重新输入", {time: 2500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (!numbers.test(number)) {
            layer.msg("课程编号由6-18位字母和数字组成，请重新输入", {time: 2500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (collegeId === "0") {
            layer.msg("请选择所属学院", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (teacherId === "0") {
            layer.msg("请选择授课教师", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (intro.trim() === "") {
            layer.msg("课程介绍不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        let loadingIndex = layer.msg('处理中', {icon: 16});
        let data = $("#courseData").serialize();
        data = decodeURIComponent(data);
        $.ajax({
            url: "${APP_PATH}/admin/saveCourse",
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
                    layer.msg("课程添加成功", {time: 1500, icon: 6}, function () {
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