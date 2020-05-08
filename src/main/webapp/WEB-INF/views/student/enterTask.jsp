<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
            <li><a href="javascript:void(0);">大学生学习平台</a></li>
            <li><a href="javascript:void(0);">作业管理</a></li>
            <li><a href="javascript:void(0);">完成作业</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel">
            <div class="panel-heading">完成作业 Finishing Task</div>
            <form id="taskForm" action="" method="POST">
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline" style="margin-bottom: 20px">
                            <div class="form-group">
                                <span class="control-label wk-filed-label"
                                      style="font-size: 15px;color: blue">作业题目：${onlineTask.title}<span
                                        style="color: green; ">（总分：${total}）</span>
                                </span><br><br>
                                <span class="control-label wk-filed-label"
                                      style="color: red">发布时间：<fmt:formatDate
                                        value="${onlineTask.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></span>
                            </div>
                        </div>
                        <c:forEach items="${sessionScope.questions}" var="question" varStatus="statu">
                            <div class="form-inline">
                                <div class="form-group">
                                    <span class="control-label wk-filed-label"
                                          style="color: #0B58A8">${statu.index+1}、${question.title}&nbsp;(${question.score}分)&nbsp;
                                    </span>
                                </div>
                                <div class="form-group">
                                    <select class="MyAnswer" style="color:red" name="${question.id}">
                                        <option value="0">请选择</option>
                                        <option value="A">A</option>
                                        <option value="B">B</option>
                                        <option value="C">C</option>
                                        <option value="D">D</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-inline">
                                <label class="control-label wk-filed-label"></label>
                                <div class="form-group">
                                    A、${question.itemA}<br>
                                    B、${question.itemB}<br>
                                    C、${question.itemC}<br>
                                    D、${question.itemD}<br>

                                    <span style="display: none"
                                          class="${question.id} answer">答案：${question.answer}</span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </form>
        </div>
        <div class="panel-footer wk-panel-footer" style="margin-bottom: 50px">
            <button type="button" class="btn btn-info"
                    id="submitBtn" <%--data-toggle="modal" data-target="#submitTaskModal"--%>
                    onclick="">提&nbsp;&nbsp;交
            </button>
            <button type="button" class="btn btn-info"
                    onclick="back();"
                    style="margin-left: 20px">返&nbsp;&nbsp;回
            </button>
        </div>
    </div>
</div>
<div class="modal fade" id="submitTaskModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">作业提交</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <span>你确定要提交这次作业吗？</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="submit_task_btn">提交</button>
            </div>
        </div>
    </div>
</div>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    $(function () {
        if (${finish}) {
            $(".answer").css("display", "");
            // $(".answer").css("color", "red");
        }
    });
    $("#submitBtn").on("click", function () {
        $("#submitTaskModal").modal({
                backdrop: "static"
            }
        );
    });
    $("#submit_task_btn").on("click", function () {
        let formData = $("#taskForm").serialize();
        let loadingIndex = layer.msg('处理中', {icon: 16});
        $.ajax({
            url: "${APP_PATH}/student/saveStudentTask/${taskId}",
            type: "POST",
            dataType: "json",
            data: formData,
            success: function (result) {
                layer.close(loadingIndex);
                console.log(result);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                        $('#submitTaskModal').modal('hide')
                    });
                }
                if (result.code === 100) {
                    $(".answer").css("color", "black");
                    let data = result.data;
                    let j = 0, len = data.length;
                    for (; j < len - 2; j++) {
                        console.log(data[j]);
                        $("." + data[j]).css("color", "red");
                    }
                    $(".answer").css("display", "");
                    layer.msg("作业提交成功，总分：" + data[len - 1] + "        得分：" + data[len - 2], {
                        time: 2000,
                        icon: 6
                    }, function () {
                        $('#submitTaskModal').modal('hide')
                    });
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });

    function back() {
        if (${flag==1}) {
            window.location.href = "${APP_PATH}/student/searchStudentTaskInfo?courseId=${courseId}";
        } else {
            if (${pageNum!=null}) {
                window.location.href = "${APP_PATH}/student/searchTaskInfo/${chapterId}?courseId=${courseId}&pageNum=${pageNum}";
            } else {
                window.location.href = "${APP_PATH}/student/searchTaskInfo/${chapterId}?courseId=${courseId}";
            }
        }
    }
</script>

</body>
</html>