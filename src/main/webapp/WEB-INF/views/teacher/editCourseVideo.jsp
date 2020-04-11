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
</head>

<body>
<div class="row">
    <div class="col-lg-12">
        <ul class="breadcrumb wk-breadcrumb">
            <li><a href="javascript:void(0)">大学生学习平台</a></li>
            <li><a href="javascript:void(0)">课程文件管理</a></li>
            <li><a href="javascript:void(0)">重新上传视频</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">
                重新上传视频 ReUpload Video
            </div>
            <form action="" method="POST" enctype="multipart/form-data">
                <div class="panel-body">
                    <div class="row">
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
                    <button type="button" class="btn btn-info" onclick="updateCourseVideo();">提&nbsp;&nbsp;交</button>
                    <button type="button" class="btn btn-info" onclick="back();">反&nbsp;&nbsp;回</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    function updateCourseVideo() {
        let file = $("#file").val();
        if (file === "") {
            layer.msg("请选择需要上传的文件", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //判断上传文件的后缀名
        let strExtension = file.substr(file.lastIndexOf('.') + 1);
        if (strExtension !== "mp4" && strExtension !== "avi") {
            layer.msg("请上传mp4或avi格式的视频文件", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //上传文件
        let formData = new FormData();
        let loadingIndex = layer.msg('处理中', {icon: 16});
        formData.append("file", $("#file")[0].files[0]);
        formData.append("courseVideoId", ${id});
        $.ajax({
            url: "${APP_PATH}/teacher/updateCourseVideo",
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
                    layer.msg("视频上传成功", {time: 1000, icon: 1}, function () {
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
        window.location.href = "${APP_PATH}/teacher/searchCourseVideo?pageNum=${pageNum}";
    }
</script>