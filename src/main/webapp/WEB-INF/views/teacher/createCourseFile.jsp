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
    <link rel="stylesheet" href="${APP_PATH}/resources1/ztree/zTreeStyle.css">
    <script type="text/javascript" src="${APP_PATH}/resources1/js/jquery-3.1.1.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap-select.min.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap.min.js"></script>
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
            <li><a href="javascript:void(0)">课程文件管理</a></li>
            <li><a href="javascript:void(0)">上传课程文档</a></li>
        </ul>
    </div>
</div>
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">
                上传文档 Create Data
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
                                                <li class="list-group-item tree-closed">
                                                    <a href="javascript:void(0)"><span
                                                            class="glyphicon glyphicon-tasks"></span>&nbsp;选择所授课程及章节</a>
                                                </li>
                                                <c:forEach items="${courses}" var="course" varStatus="statu">
                                                <c:if test="${statu.first}">
                                                <li class="list-group-item">
                                                    </c:if>
                                                    <c:if test="${!statu.first}">
                                                <li class="list-group-item tree-closed">
                                                    </c:if>
                                                    <span class="glyphicon glyphicon-tasks"></span>&nbsp;${course.name}(${course.college.name})
                                                    <span class="badge"
                                                          style="float:right">${course.chapters.size()}</span>
                                                    <c:if test="${!empty course.chapters}">
                                                    <c:if test="${statu.first}">
                                                    <ul style="margin-top:10px;">
                                                        </c:if>
                                                        <c:if test="${!statu.first}">
                                                        <ul style="margin-top:10px;display:none;">
                                                            </c:if>
                                                            <c:forEach items="${course.chapters}" var="chapter">
                                                                <li style="height:30px;">
                                                                    <a href="javascript:void(0)" class="chapter"
                                                                       text="${course.name}(${course.college.name}) > ${chapter.title}"
                                                                       chapterId="${chapter.id}"
                                                                       courseId="${course.id}"><span
                                                                            class="glyphicon glyphicon-tags"></span>&nbsp;${chapter.title}
                                                                    </a>
                                                                </li>
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
                                <label for="chapter" class="control-label wk-filed-label">所选课程及章节: </label>
                                <div class="input-group">
                                    <input required="required" id="chapter" name="chapter" chapterId="0" courseId="0"
                                           type="text"
                                           readonly="readonly"
                                           class="form-control wk-long-2col-input"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="file" class="control-label wk-filed-label">选择文件: </label>
                                <div class="input-group">
                                    <input required="required" id="file" name="file" type="file"
                                           class="form-control wk-long-2col-input"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel-footer wk-panel-footer">
                    <button type="button" class="btn btn-info" onclick="createCourseFile();">提&nbsp;&nbsp;交</button>
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
    $(".chapter").on("click", function () {
        $("#chapter").val($(this).attr("text"));
        $("#chapter").attr("chapterId", $(this).attr("chapterId"));
        $("#chapter").attr("courseId", $(this).attr("courseId"));
    });

    function createCourseFile() {
        let chapterId = $("#chapter").attr("chapterId");
        let courseId = $("#chapter").attr("courseId");
        let file = $("#file").val();
        if (chapterId === "0" || courseId === "0") {
            layer.msg("你还没有选择视频所属课程及章节", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (file === "") {
            layer.msg("请选择需要上传的文件", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //上传文件
        let formData = new FormData();
        let loadingIndex = layer.msg('处理中', {icon: 16});
        formData.append("file", $("#file")[0].files[0]);
        formData.append("chapterId", chapterId);
        formData.append("courseId", courseId);
        $.ajax({
            url: "${APP_PATH}/teacher/saveCourseFile",
            type: 'POST',
            dataType: 'json',
            crossDomain: true, // 如果用到跨域，需要后台开启CORS
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("文档上传成功", {time: 1000, icon: 1}, function () {
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