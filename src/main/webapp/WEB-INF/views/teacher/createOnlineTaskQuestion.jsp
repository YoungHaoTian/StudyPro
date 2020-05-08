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
            <li><a href="javascript:void(0)">作业管理</a></li>
            <li><a href="javascript:void(0)">新增题目</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">
                新增题目 Create Data
            </div>
            <form id="questionData" action="" method="POST">
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="title" class="control-label wk-filed-label">题目: </label>
                                <div class="input-group">
                                    <input required="required" id="title" name="title" type="text"
                                           class="form-control wk-long-2col-input" placeholder="请输入题目"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-inline">
                            <div class="form-group">
                                <label for="itemA" class="control-label wk-filed-label">选项A: </label>
                                <div class="input-group">
                                    <input required="required" id="itemA" name="itemA" type="text"
                                           class="form-control wk-normal-input" placeholder="请输入选项A"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="itemB" class="control-label wk-filed-label">选项B: </label>
                                <div class="input-group">
                                    <input required="required" id="itemB" name="itemB" type="text"
                                           class="form-control wk-normal-input" placeholder="请输入选项B"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="itemC" class="control-label wk-filed-label">选项C: </label>
                                <div class="input-group">
                                    <input required="required" id="itemC" name="itemC" type="text"
                                           class="form-control wk-normal-input" placeholder="请输入选项C"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="itemD" class="control-label wk-filed-label">选项D: </label>
                                <div class="input-group">
                                    <input required="required" id="itemD" name="itemD" type="text"
                                           class="form-control wk-normal-input" placeholder="请输入选项D"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-inline">
                            <div class="form-group">
                                <label for="answer" class="control-label wk-filed-label">答案: </label>
                                <select class="selectpicker" id="answer" name="answer">
                                    <option value="A">A</option>
                                    <option value="B">B</option>
                                    <option value="C">C</option>
                                    <option value="D">D</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-inline">
                            <div class="form-group">
                                <label for="score" class="control-label wk-filed-label">分值: </label>
                                <div class="input-group">
                                    <input required="required" id="score" name="score" type="text" maxlength="3"
                                           oninput="value=value.replace(/[^\d]/g,'')"
                                           class="form-control wk-normal-input" placeholder="请输入分值"/>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </form>
        </div>
        <div class="panel-footer wk-panel-footer" style="margin-bottom: 50px">
            <button type="button"
                    class="btn btn-info createTaskQuestionBatch">批量导入
            </button>
            <button type="button" class="btn btn-info" onclick="createTaskQuestion()" style="margin-left: 20px">
                提&nbsp;&nbsp;交
            </button>
            <button type="button" class="btn btn-info" onclick="window.history.back()"
                    style="margin-left: 20px">返&nbsp;&nbsp;回
            </button>
            <button type="button" class="btn btn-info" onclick="$('#questionData')[0].reset()"
                    style="margin-left: 20px">重&nbsp;&nbsp;填
            </button>
        </div>
    </div>
</div>
<div class="modal fade" id="createOnlineTaskQuestionModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: blue">批量导入题目</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="file" class="col-sm-2 control-label">选择文件:</label>
                        <div class="col-sm-9">
                            <input type="file" id="file" name="file" class="form-control">

                        </div>
                        <a class="btn btn-link" href="${APP_PATH}/teacher/downloadTaskQuestionTemplate">点击下载模板</a>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="createOnlineTaskQuestion">添加</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    function createTaskQuestionBatch() {
        let taskId =${taskId};
        window.location.href = "${APP_PATH}/teacher/createOnlineTaskQuestionBatch/" + taskId;
    }

    function createTaskQuestion() {
        let title = $("#title").val();
        let itemA = $("#itemA").val();
        let itemB = $("#itemB").val();
        let itemC = $("#itemC").val();
        let itemD = $("#itemD").val();
        let answer = $("#answer").val();
        let score = $("#score").val();
        if (title.trim() === "") {
            layer.msg("题目不能为空，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (title.indexOf(" ") === 0) {
            layer.msg("题目不能以空格开头，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (itemA.trim() === "" || itemB.trim() === "" || itemC.trim() === "" || itemD.trim() === "") {
            layer.msg("选项内容不能为空，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (itemA.indexOf(" ") === 0 || itemB.indexOf(" ") === 0 || itemC.indexOf(" ") === 0 || itemD.indexOf(" ") === 0) {
            layer.msg("选项内容不能以空格开头，请重新输入", {time: 2000, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (score.trim() === "") {
            layer.msg("分值不能为空，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //发送ajax请求
        $.ajax({
            url: "${APP_PATH}/teacher/saveOnlineTaskQuestion",
            type: "POST",
            dataType: "json",
            data: {
                "title": title.trim(),
                "itemA": itemA.trim(),
                "itemB": itemB.trim(),
                "itemC": itemC.trim(),
                "itemD": itemD.trim(),
                "answer": answer.trim(),
                "score": score,
                "onlineTaskId": ${id}
            },
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("添加题目成功", {time: 1000, icon: 1}, function () {
                    });
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    }

    //批量导入题目
    $(".createTaskQuestionBatch").on("click", function () {
        $("#createOnlineTaskQuestionModal").modal({
                backdrop: "static"
            }
        );
    });
    $("#createOnlineTaskQuestion").on("click", function () {
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
        formData.append("taskId", ${taskId});
        $.ajax({
            url: "${APP_PATH}/teacher/taskQuestionBatchImport",
            type: 'POST',
            dataType: 'json',
            crossDomain: true, // 如果用到跨域，需要后台开启CORS
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 3000, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("作业题目批量导入成功", {time: 1000, icon: 1}, function () {
                        $("#createOnlineTaskQuestionModal").modal('hide');
                    });
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    })

</script>