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
            <li><a href="javascript:void(0)">学生信息管理</a></li>
            <li><a href="javascript:void(0)">批量导入信息</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">
                批量导入学生 Batch Create Data
            </div>
            <form action="" method="POST" enctype="multipart/form-data">
                <div class="panel-body">
                    <div class="row">

                        <div class="form-inline">
                            <div class="form-group">
                                <label for="file" class="control-label wk-filed-label">选择文件: </label>
                                <div class="input-group">
                                    <input required="required" name="file" type="file" id="file"
                                           class="form-control wk-long-2col-input"/>

                                </div>
                                <div class="input-group">
                                    <a class="btn btn-link" href="${APP_PATH}/admin/downloadStudentTemplate">点击下载模板</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel-footer wk-panel-footer">
                    <button class="btn btn-info" type="button" onclick="upload()">提&nbsp;&nbsp;交</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">

    function upload() {
        let file = $("#file").val();
        if (file === "") {
            layer.msg("请选择需要上传的文件", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //判断上传文件的后缀名
        let strExtension = file.substr(file.lastIndexOf('.') + 1);
        if (strExtension !== "xls" && strExtension !== "xlsx") {
            layer.msg("请选择excel文档文件", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //上传文件
        let formData = new FormData();
        let loadingIndex = layer.msg('处理中', {icon: 16});
        formData.append("file", $("#file")[0].files[0]);
        $.ajax({
            url: "${APP_PATH}/admin/studentDataImport",
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
                    layer.msg("学生批量导入成功", {time: 1000, icon: 1}, function () {
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
</body>
</html>