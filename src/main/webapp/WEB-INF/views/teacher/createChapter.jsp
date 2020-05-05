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
            <li><a href="javascript:void(0)">新增章节</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">
                新增章节 Create Data
            </div>
            <form id="chapterData" action="" method="POST" enctype="multipart/form-data">
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group col-md-10">
                                <div class="container-fluid col-md-10">
                                    <div class="sidebar">
                                        <div class="tree">
                                            <ul style="padding-left:0px;" class="list-group">
                                                <li class="list-group-item" style="color: red">
                                                    <span class="glyphicon glyphicon-tasks"></span>&nbsp;${course.name}(${course.college.name})
                                                    <span class="badge"
                                                          style="float:right">${course.chapters.size()}</span>
                                                    <c:if test="${!empty course.chapters}">
                                                        <ul style="margin-top:10px;">
                                                            <c:forEach items="${course.chapters}" var="chapter"
                                                                       varStatus="statu">
                                                                <c:if test="${!statu.last}">
                                                                    <li style="height:30px;">
                                                                        <a href="javascript:void(0)"
                                                                           class="chapter"><span
                                                                                class="glyphicon glyphicon-tags"></span>&nbsp;${chapter.title}
                                                                        </a>
                                                                    </li>
                                                                </c:if>
                                                                <c:if test="${statu.last}">
                                                                    <li style="height:30px;">
                                                                        <a href="javascript:void(0)"
                                                                           style="color: green"
                                                                           class="chapter"><span
                                                                                class="glyphicon glyphicon-tags"></span>&nbsp;${chapter.title}
                                                                        </a>
                                                                    </li>
                                                                </c:if>

                                                            </c:forEach>
                                                        </ul>
                                                    </c:if>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="title" class="control-label wk-filed-label">章节标题: </label>
                                <div class="input-group">
                                    <input required="required" id="title" name="title" maxlength="100"
                                           class="form-control wk-long-2col-input"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="content" class="control-label wk-filed-label">章节内容: </label>
                                <div class="input-group">
                                    <textarea required="required" id="content" name="content"
                                              class="form-control wk-long-2col-input"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <div class="panel-footer wk-panel-footer">
            <button type="button" class="btn btn-info" onclick="createChapter();">提&nbsp;&nbsp;交</button>
            <button type="button" class="btn btn-info" onclick="window.history.back()"
                    style="margin-left: 20px">返&nbsp;&nbsp;回
            </button>
            <button type="button" class="btn btn-info" onclick="$('#chapterData')[0].reset()"
                    style="margin-left: 20px">重&nbsp;&nbsp;填
            </button>
        </div>
    </div>
</div>

</body>
</html>
<script src="${APP_PATH}/resources1/ztree/jquery.ztree.all-3.5.min.js"></script>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    function createChapter() {
        let title = $("#title").val();
        let content = $("#content").val();
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
        if (content.trim() === "") {
            layer.msg("章节内容不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        let loadingIndex = layer.msg('处理中', {icon: 16});
        $.ajax({
            url: "${APP_PATH}/teacher/saveChapter",
            type: "POST",
            // contentType: "application/json",//不使用contentType: “application/json”则data可以是对象,使用contentType: “application/json”则data只能是json字符串
            dataType: "json",
            data: {
                "courseId": ${courseId},
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
                    layer.msg("章节添加成功", {time: 1500, icon: 6}, function () {
                    });
                    window.setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                    <%--window.location.href = "${APP_PATH}/teacher/createChapter?courseId=${courseId}";--%>
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    }
</script>