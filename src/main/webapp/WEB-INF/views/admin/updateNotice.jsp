<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
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
            <li><a href="#">大学生学习平台</a></li>
            <li><a href="#">公告信息管理</a></li>
            <li><a href="#">编辑公告</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">
                编辑公告 Create Data
            </div>
            <form id="noticeData" action="" method="POST">
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="title" class="control-label wk-filed-label">公告标题: </label>
                                <div class="input-group">
                                    <input required="required" id="title" name="title" type="text"
                                           class="form-control wk-long-2col-input" value="${notice.title}"
                                           placeholder="请输入公告标题"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="content" class="control-label wk-filed-label">公告内容: </label>
                                <div class="input-group">
                                    <textarea required="required" id="content" name="content"
                                              class="form-control wk-long-2col-input"
                                              placeholder="请输入公告内容">${notice.content}</textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="panel-footer wk-panel-footer">
                    <button type="button" class="btn btn-info" onclick="updateNotice()">提&nbsp;&nbsp;交</button>
                    <button type="button" class="btn btn-info" onclick="back();"
                            style="margin-left: 30px">
                        返&nbsp;&nbsp;回
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    function updateNotice() {
        let title = $("#title").val();
        let content = $("#content").val();
        if (title.trim() === "") {
            layer.msg("公告标题不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (title.indexOf(" ") === 0) {
            layer.msg("公告标题不能以空格开头，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (content.trim() === "") {
            layer.msg("公告内容不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        let loadingIndex = layer.msg('处理中', {icon: 16});
        let data = $("#noticeData").serialize();
        data = decodeURIComponent(data);
        $.ajax({
            url: "${APP_PATH}/admin/editNotice/${notice.id}",
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
        window.location.href="${APP_PATH}/admin/searchNotice?&pageNum=${pageNum}";
    }
</script>