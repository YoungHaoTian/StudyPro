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
    <script type="text/javascript" src="${APP_PATH}/resources1/js/jquery-3.1.1.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${APP_PATH}/resources1/css/wukong-ui.css">
    <link rel="stylesheet" href="${APP_PATH}/resources1/bootstrap/css/bootstrap-select.min.css">
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap-select.min.js"></script>
    <style>
        th, td {
            text-align: center;
            height: 30px;
            border: #CCCCCC 1px solid;
        }

        td {
            word-wrap: break-word;
        }
    </style>
</head>

<body>
<div class="row">
    <div class="col-lg-12">
        <ul class="breadcrumb wk-breadcrumb">
            <li><a href="javascript:void(0)">大学生学习平台</a></li>
            <li><a href="javascript:void(0)">作业管理</a></li>
            <li><a href="javascript:void(0)">作业题目查询</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel wk-table-tools-panel">
            <div class="panel-heading">
                工具栏 Tools
                <div class="form-group" style="position: absolute;top: 6px;right: 240px;">
                    <button type="button" class="btn btn-danger batchDelete" data-toggle="tooltip"
                            data-placement="left"
                            title="批量删除作业">
                        <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                        批量删除
                    </button>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default  wk-panel">
            <table class="table table-striped table-hover">
                <thead>
                <tr class="info">
                    <th style="width:80px">
                        <input type="checkbox" id="select_all"/>
                        <label for="select_all" style="margin-bottom: 0px;font-weight: 200">全选</label>
                    </th>
                    <th style="width:80px">
                        序号
                    </th>
                    <th>题目</th>
                    <th>选项A</th>
                    <th>选项B</th>
                    <th>选项C</th>
                    <th>选项D</th>
                    <th style="width: 50px;"><span style="color:green;">答案</span></th>
                    <th style="width: 50px;"><span style="color:red;">分值</span></th>
                    <th style="width:150px;">选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${questions}" var="question" varStatus="statu">
                    <tr>
                        <th>
                            <label>
                                <input questionId="${question.id}" type="checkbox" class="select_item"/>
                            </label>
                        </th>
                        <td>${statu.index+1}</td>
                        <td>${question.title}</td>
                        <td>${question.itemA}</td>
                        <td>${question.itemB}</td>
                        <td>${question.itemC}</td>
                        <td>${question.itemD}</td>
                        <td><span style="color:green;">${question.answer}</span></td>
                        <td><span style="color:red;">${question.score}</span></td>
                        <td>
                            <button type="button" class="btn btn-info edit"
                                    data-toggle="tooltip" questionId="${question.id}"
                                    data-placement="left" title="修改该题目">
                                <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                修改题目
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="panel-footer wk-panel-footer" style="margin-bottom: 50px">
            <button type="button" class="btn btn-info" onclick="back();">
                返&nbsp;&nbsp;回
            </button>
        </div>
    </div>
</div>
<div class="modal fade" id="questionBatchDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">题目批量删除</h5>
            </div>
            <div class="modal-body" style="text-align: center">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="question_batchDelete_btn">确定</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    let ids = "";
    //批量删除题目
    //全选按钮
    $("#select_all").on("click", (function () {
        $(".select_item").prop("checked", $(this).prop("checked"));
        let checked = $(this).prop("checked");
        console.log(checked);
        console.log(checked === true);
        if (checked === true) {
            $(this).parent("th").children("label").text("取消");
        } else {
            $(this).parent("th").children("label").text("全选");
        }
    }));
    //单选按钮
    $(".select_item").on("click", function () {
        let flag = $(".select_item:checked").length == $(".select_item").length;
        if (flag) {
            $("#select_all").parent("th").children("label").text("取消");
        } else {
            $("#select_all").parent("th").children("label").text("全选");
        }
        $("#select_all").prop("checked", flag);
    });
    //点击批量删除按钮
    $(".batchDelete").on("click", function () {
        ids = "";
        $.each($(".select_item:checked"), function () {
            //组装课程id字符串
            ids += $(this).attr("questionId") + "-";
        });
        console.log(ids);
        if (ids.trim() === "") {
            layer.msg("操作失败，你未选择任何题目", {time: 1500, icon: 5, shift: 6}, function () {
            });
        } else {
            //去除删除的id多余的"-"
            ids = ids.substring(0, ids.length - 1);
            console.log(ids);
            $("#questionBatchDeleteModal .modal-body").text("你确定要删除这些题目吗？");
            $("#questionBatchDeleteModal").modal({
                backdrop: "static"
            });
        }
    });
    $("#question_batchDelete_btn").on("click", function () {
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //发送ajax请求删除
        $.ajax({
            url: "${APP_PATH}/teacher/deleteTaskQuestionBatch",
            type: "POST",
            dataType: "json",
            data: {
                "ids": ids
            },
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("批量删除成功", {time: 1000, icon: 1}, function () {
                    });
                    window.setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                    <%--window.location.href = "${APP_PATH}/teacher/searchTaskQuestion/${taskId}?pageNum=${pageInfo.pageNum}&pageNumber=${pageNumber}";--%>
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });

    function back() {
        if (${page!=null}) {
            window.location.href = "${APP_PATH}/teacher/searchChapterTask/${chapterId}?pageNum=${page}&courseId=${courseId}";
        } else {
            window.location.href = "${APP_PATH}/teacher/searchTask?pageNum=${pageNumber}";
        }
    }

    $(".edit").on("click", function () {
        let questionId = $(this).attr("questionId");
        if (${page!=null}) {
            window.location.href = "${APP_PATH }/teacher/editOnlineTaskQuestion/" + questionId + "?page=${page}&courseId=${courseId}&chapterId=${chapterId}";
        } else {
            window.location.href = "${APP_PATH}/teacher/editOnlineTaskQuestion/" + questionId + "?pageNumber=${pageNumber}";
        }
    });
</script>