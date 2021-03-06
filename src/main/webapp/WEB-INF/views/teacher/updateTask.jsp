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
            <li><a href="javascript:void(0)">作业管理</a></li>
            <li><a href="javascript:void(0)">编辑作业</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">
                编辑作业 Update Data
            </div>
            <form id="taskData" action="" method="POST" enctype="multipart/form-data">
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group col-md-10">
                                <div class="container-fluid col-md-10">
                                    <div class="sidebar">
                                        <div class="tree">
                                            <ul style="padding-left:0px;" class="list-group">
                                                <li class="list-group-item tree-closed">
                                                    <a href="javascript:void(0)"><span
                                                            class="glyphicon glyphicon-tasks"></span>&nbsp;选择所授课程及章节</a>
                                                </li>
                                                <c:forEach items="${courses}" var="course" varStatus="statu">
                                                    <c:set var="mark" value="0"/>
                                                <c:forEach items="${course.chapters}" var="chapter">
                                                <c:if test="${chapter.id==task.chapterId}">
                                                <li class="list-group-item">
                                                        <c:set var="mark" value="1"/>
                                                    </c:if>
                                                    </c:forEach>
                                                    <c:if test="${mark==0}">
                                                <li class="list-group-item tree-closed">
                                                    </c:if>
                                                    <span class="glyphicon glyphicon-tasks"></span>&nbsp;${course.name}:${course.college.name}
                                                    <span class="badge"
                                                          style="float:right">${course.chapters.size()}</span>
                                                    <c:if test="${mark!=0}">
                                                    <ul style="margin-top:10px;">
                                                        </c:if>
                                                        <c:if test="${mark==0}">
                                                        <ul style="margin-top:10px;display:none;">
                                                            </c:if>
                                                            <c:forEach items="${course.chapters}" var="chapter">
                                                                <c:if test="${chapter.id==task.chapterId}">
                                                                    <li style="height:30px;background-color: #f7f7f7"
                                                                        class="chapter"
                                                                        text="${course.name}(${course.college.name}) > ${chapter.title}"
                                                                        chapterId="${chapter.id}">
                                                                        <a href="javascript:void(0)" style="color: red"><span
                                                                                class="glyphicon glyphicon-tags"></span>&nbsp;${chapter.title}
                                                                        </a>
                                                                    </li>
                                                                </c:if>
                                                                <c:if test="${chapter.id!=task.chapterId}">
                                                                    <li style="height:30px;background-color: #f7f7f7"
                                                                        class="chapter"
                                                                        text="${course.name}(${course.college.name})-->${chapter.title}"
                                                                        chapterId="${chapter.id}">
                                                                        <a href="javascript:void(0)"><span
                                                                                class="glyphicon glyphicon-tags"></span>&nbsp;${chapter.title}
                                                                        </a>
                                                                    </li>
                                                                </c:if>
                                                            </c:forEach>
                                                        </ul>
                                                        </li>
                                                        </c:forEach>
                                                    </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="chapter" style="margin-right: 0px;"
                                       class="control-label wk-filed-label">所选课程及章节: </label>
                                <div class="input-group">
                                    <div id="chapter" name="chapter" chapterId="${task.chapterId}">
                                        <span style="color:red;">${task.courseChapter.course.name}(${task.courseChapter.course.college.name})-->${task.courseChapter.title}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="title" class="control-label wk-filed-label">作业标题: </label>
                                <div class="input-group">
                                    <textarea required="required" id="title" name="title"
                                              class="form-control wk-long-2col-input">${task.title}</textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <div class="panel-footer wk-panel-footer" style="margin-bottom: 50px">
            <button type="button" class="btn btn-info" onclick="updateTask();">提&nbsp;&nbsp;交</button>
            <button type="button" class="btn btn-info" style="margin-left: 20px"
                    onclick="back()">返&nbsp;&nbsp;回
            </button>
            <button type="button" class="btn btn-info" style="margin-left: 20px"
                    onclick="$('#taskData')[0].reset()">重&nbsp;&nbsp;填
            </button>
        </div>
    </div>
</div>

</body>
</html>
<script src="${APP_PATH}/resources1/ztree/jquery.ztree.all-3.5.min.js"></script>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    $(function () {
        let isChapter = false;
        $(".chapter").click(function () {
            $(".chapter").children("a").css("color", "");
            $(this).children("a").css("color", "red");
            isChapter = true;
        });
        $(".list-group-item").click(function () {
            if (!isChapter) {
                if ($(this).find("ul")) {
                    $(this).toggleClass("tree-closed");
                    if ($(this).hasClass("tree-closed")) {
                        $("ul", this).hide("fast");
                    } else {
                        $("ul", this).show("fast");
                    }
                }
            }
            isChapter = false;
        });
    });
    $(".chapter").on("click", function () {
        $("#chapter").children("span").text($(this).attr("text"));
        $("#chapter").attr("chapterId", $(this).attr("chapterId"));
    });

    function updateTask() {
        let chapterId = $("#chapter").attr("chapterId");
        let title = $("#title").val();
        if (chapterId === "0") {
            layer.msg("你还没有选择所属课程及章节", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (title.trim() === "") {
            layer.msg("作业标题不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        let loadingIndex = layer.msg('处理中', {icon: 16});
        let url;
        if (${type.equals("online")}) {
            url = "${APP_PATH}/teacher/updateOnlineTask/${task.id}";
        }
        if (${type.equals("offline")}) {
            url = "${APP_PATH}/teacher/updateOfflineTask/${task.id}";
        }
        $.ajax({
            url: url,
            type: "POST",
            // contentType: "application/json",//不使用contentType: “application/json”则data可以是对象,使用contentType: “application/json”则data只能是json字符串
            dataType: "json",
            data: {
                "chapterId": chapterId,
                "title": title.trim()
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
                    layer.msg("作业修改成功", {time: 1500, icon: 6}, function () {
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
        if (0 ===${pageNum}) {
            window.location.href = "${APP_PATH}/teacher/searchChapterTask/${chapterId}?pageNum=${pageNumber}&courseId=${courseId}";
        } else {
            window.location.href = "${APP_PATH}/teacher/searchTask?pageNum=${pageNum}";
        }
    }
</script>