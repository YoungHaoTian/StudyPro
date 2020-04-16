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
    <link rel="stylesheet" href="${APP_PATH}/resources1/ztree/zTreeStyle.css">
    <script type="text/javascript" src="${APP_PATH}/resources1/js/jquery-3.1.1.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${APP_PATH}/resources1/css/wukong-ui.css">
    <link rel="stylesheet" href="${APP_PATH}/resources1/bootstrap/css/bootstrap-select.min.css">
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap-select.min.js"></script>
    <style>
        .tree li {
            list-style-type: none;
            cursor: pointer;
        }
    </style>
</head>

<body>
<div class="row">
    <div class="col-lg-12">
        <ul class="breadcrumb wk-breadcrumb">
            <li><a href="javascript:void(0)">大学生学习平台</a></li>
            <li><a href="javascript:void(0)">课程章节管理</a></li>
            <li><a href="javascript:void(0)">编辑章节</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">
                编辑章节 Create Data
            </div>
            <form action="" method="POST" enctype="multipart/form-data">
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group col-md-10">
                                <div class="container-fluid col-md-10">
                                    <div class="sidebar">
                                        <div class="tree">
                                            <ul style="padding-left:0px;" class="list-group">
                                                <li class="list-group-item">
                                                    <a href="javascript:void(0)"><span
                                                            class="glyphicon glyphicon-tasks"></span>&nbsp;所授课程及章节</a>
                                                </li>
                                                <c:forEach items="${courses}" var="course" varStatus="statu">
                                                <c:if test="${course.id==courseId&&courseId!=0}">
                                                <li class="list-group-item ${course.id}">
                                                    </c:if>
                                                    <c:if test="${course.id!=courseId||courseId==0}">
                                                <li class="list-group-item tree-closed">
                                                    </c:if>
                                                    <span class="glyphicon glyphicon-tasks"></span>&nbsp;${course.name}(${course.college.name})
                                                    <span class="badge"
                                                          style="float:right">${course.chapters.size()}</span>
                                                    <c:if test="${!empty course.chapters}">
                                                    <c:if test="${course.id==courseId&&courseId!=0}">
                                                    <ul style="margin-top:10px;" class="${course.id}">
                                                        </c:if>
                                                        <c:if test="${course.id!=courseId||courseId==0}">
                                                        <ul style="margin-top:10px;display:none;" class="${course.id}">
                                                            </c:if>
                                                            <c:forEach items="${course.chapters}" var="courseChapter">
                                                            <c:if test="${courseChapter.id==chapter.id}">
                                                            <li style="height:30px;">
                                                                <a href="javascript:void(0)" class="chapter"
                                                                   style="color: red"><span
                                                                        class="glyphicon glyphicon-tags"></span>&nbsp;${courseChapter.title}
                                                                </a>
                                                                </c:if>
                                                                <c:if test="${courseChapter.id!=chapter.id}">
                                                            <li style="height:30px;">
                                                                <a href="javascript:void(0)" class="chapter"><span
                                                                        class="glyphicon glyphicon-tags"></span>&nbsp;${courseChapter.title}
                                                                </a>
                                                                </c:if>
                                                                </c:forEach>
                                                        </ul>
                                                        </c:if>
                                                        </li>
                                                        </c:forEach>
                                                    </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="courseId" class="control-label wk-filed-label">章节所属课程: </label>
                                <select class="selectpicker" id="courseId" name="courseId">
                                    <option value="0" selected="selected">请选择章节所属课程</option>
                                    <c:forEach items="${courses}" var="course">
                                        <c:choose>
                                            <c:when test="${course.id == chapter.courseId}">
                                                <option value="${course.id}"
                                                        selected="selected">${course.name}(${course.college.name})
                                                </option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${course.id}">${course.name}(${course.college.name})</option>
                                            </c:otherwise>
                                        </c:choose>

                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="title" class="control-label wk-filed-label">章节标题: </label>
                                <div class="input-group">
                                    <input required="required" id="title" name="title" maxlength="100"
                                           class="form-control wk-long-2col-input" value="${chapter.title}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="content" class="control-label wk-filed-label">章节内容: </label>
                                <div class="input-group">
                                    <textarea required="required" id="content" name="content"
                                              class="form-control wk-long-2col-input">${chapter.content}</textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel-footer wk-panel-footer">
                    <button type="button" class="btn btn-info" onclick="updateChapter();">提&nbsp;&nbsp;交</button>
                    <button type="button" class="btn btn-info" onclick="back()" style="margin-left: 20px">返&nbsp;&nbsp;回
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
<script src="${APP_PATH}/resources1/ztree/jquery.ztree.all-3.5.min.js"></script>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    $(function () {
        $(".list-group-item").click(function () {
            if ($(this).find("ul")) {
                $(this).toggleClass("tree-closed");
                if ($(this).hasClass("tree-closed")) {
                    $("ul", this).hide("fast");
                } else {
                    $("ul", this).show("fast");
                }
            }
        });
    });

    function updateChapter() {
        let courseId = $("#courseId").val();
        let title = $("#title").val();
        let content = $("#content").val();
        if (courseId === "0") {
            layer.msg("你还没有选择章节所属课程", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (title.trim() === "") {
            layer.msg("章节标题不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (title.indexOf(" ") === 0) {
            layer.msg("章节标题不能以空格开头", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (title.trim().length > 100) {
            layer.msg("章节标题不能超过100", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (content.trim() === "") {
            layer.msg("章节内容不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        let loadingIndex = layer.msg('处理中', {icon: 16});
        $.ajax({
            url: "${APP_PATH}/teacher/updateChapter/${chapter.id}",
            type: "POST",
            // contentType: "application/json",//不使用contentType: “application/json”则data可以是对象,使用contentType: “application/json”则data只能是json字符串
            dataType: "json",
            data: {
                "courseId": courseId,
                "title": title.trim(),
                "content": content.trim()
            },
            success: function (result) {
                layer.close(loadingIndex);
                console.log(result);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    console.log("success");
                    layer.msg("章节修改成功", {time: 1500, icon: 6}, function () {
                    });
                    window.location.href = "${APP_PATH}/teacher/editChapter/${chapter.id}?courseId=" + courseId + "&pageNum=${pageNum}&preCourseId=${preCourseId}";
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    }

    function back() {
        window.location.href = "${APP_PATH}/teacher/searchChapter?courseId=${preCourseId}&pageNum=${pageNum}";
    }
</script>