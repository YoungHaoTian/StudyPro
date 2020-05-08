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
            <li><a href="javascript:void(0)">作业完成情况</a></li>
        </ul>
    </div>
</div>
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel wk-table-tools-panel">
            <div class="panel-heading">
                工具栏 Tools
            </div>
            <!-- 搜索 start -->
            <div style="position: absolute;top: -11px;left: 240px;">
                <div class="navbar-form navbar-right" role="search">
                    <div class="form-group">
                        <label for="name" class="control-label wk-filed-label"
                               style="margin-top: 20px">学生姓名:</label>
                        <input type="text" maxlength="5" value="${name}"
                               class="form-control" name="name" id="name" placeholder="学生姓名">
                    </div>
                    <div class="form-group">
                        <label for="number" class="control-label wk-filed-label"
                               style="margin-top: 20px">学生学号:</label>
                        <input type="text" oninput="value=value.replace(/[^\d]/g,'')" maxlength="18" value="${number}"
                               class="form-control" name="number" id="number" placeholder="学生学号">
                    </div>
                    <div class="form-group" style="margin-left: 20px">
                        <button type="button" id="search" class="btn btn-success" data-toggle="tooltip"
                                data-placement="left" title="查询">
                            <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                            查询
                        </button>
                    </div>
                    <div class="form-group" style="margin-left: 20px">
                        <button type="button" class="btn btn-danger batchReFinish" data-toggle="tooltip"
                                data-placement="left"
                                title="批量重新完成">
                            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                            批量重做
                        </button>
                    </div>
                    <c:if test="${type.equals('offline')}">
                        <div class="form-group" style="margin-left: 20px">
                            <button type="button" class="btn btn-info downloadFiles" data-toggle="tooltip"
                                    data-placement="left"
                                    title="下载当前页面学生的任务文件">
                                <span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>
                                下载当前页面学生的任务文件
                            </button>
                        </div>
                    </c:if>
                </div>

            </div>
            <!-- 搜索 end -->
        </div>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel">
            <table class="table table-striped table-hover">
                <thead>
                <tr class="info">
                    <th style="width:80px">
                        <input type="checkbox" id="select_all"/>
                        <label for="select_all" style="margin-bottom: 0px;font-weight: 200">全选</label>
                    </th>
                    <th>学生姓名</th>
                    <th>学生学号</th>
                    <th>完成情况</th>
                    <th style="width: 200px;">完成时间</th>
                    <th>
                        <c:if test="${type.equals('online')}">
                            分数<span style="color:red;">/总分</span>
                        </c:if>
                        <c:if test="${type.equals('offline')}">
                            文件
                        </c:if>
                    </th>
                    <th style="width: 200px;">选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${students}" var="student" varStatus="statu">
                    <tr>
                        <th>
                            <label>
                                <input studentId="${student.id}" type="checkbox" class="select_item"/>
                            </label>
                        </th>
                        <td>${student.name}</td>
                        <td>${student.number}</td>
                        <c:if test="${type.equals('online')}">
                            <c:if test="${studentOnlineTaskMap.get(student.id)==null}">
                                <td><span style="color:red;">待完成</span></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </c:if>
                            <c:if test="${studentOnlineTaskMap.get(student.id)!=null}">
                                <td><span style="color:green;">已完成</span></td>
                                <td><span style="color: red"><fmt:formatDate
                                        value="${studentOnlineTaskMap.get(student.id).recordTime}"
                                        pattern="yyyy-MM-dd  HH:mm:ss"/></span></td>
                                <td>${studentOnlineTaskMap.get(student.id).score}<span
                                        style="color: red">/${total}</span></td>
                                <td>
                                    <button type="button" class="btn btn-danger refinish"
                                            data-toggle="tooltip"
                                            studentTaskId="${studentOnlineTaskMap.get(student.id).id}"
                                            data-placement="left" title="让该同学重新完成该任务">
                                        <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                        打回重做
                                    </button>
                                </td>
                            </c:if>
                        </c:if>
                        <c:if test="${type.equals('offline')}">
                            <c:if test="${studentOfflineTaskMap.get(student.id)==null}">
                                <td><span style="color:red;">待完成</span></td>
                                <td></td>
                                <td></td>
                                <td></td>
                            </c:if>
                            <c:if test="${studentOfflineTaskMap.get(student.id)!=null}">
                                <c:set var="path" value="${studentOfflineTaskMap.get(student.id).path.split('_',3)}"/>
                                <td><span style="color:green;">已完成</span></td>
                                <td><span style="color: red"><fmt:formatDate
                                        value="${studentOfflineTaskMap.get(student.id).recordTime}"
                                        pattern="yyyy-MM-dd  HH:mm:ss"/></span></td>
                                <td>
                                    <a class="btn btn-link"
                                       href="${APP_PATH}/teacher/downloadOfflineTaskFile/${studentOfflineTaskMap.get(student.id).id}">${path[2]}</a>
                                </td>
                                <td>
                                    <button type="button" class="btn btn-danger refinish"
                                            data-toggle="tooltip"
                                            studentTaskId="${studentOfflineTaskMap.get(student.id).id}"
                                            data-placement="left" title="让该同学重新完成该任务">
                                        <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                        打回重做
                                    </button>
                                </td>
                            </c:if>
                        </c:if>
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
<div class="modal fade" id="batchReFinishModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">作业批量打回重做</h5>
            </div>
            <div class="modal-body" style="text-align: center">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="batchReFinish_btn">确定</button>
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
    $(".batchReFinish").on("click", function () {
        ids = "";
        $.each($(".select_item:checked"), function () {
            ids += $(this).attr("studentId") + "-";
        });
        console.log(ids);
        if (ids.trim() === "") {
            layer.msg("操作失败，你未选择任何学生", {time: 1500, icon: 5, shift: 6}, function () {
            });
        } else {
            //去除删除的id多余的"-"
            ids = ids.substring(0, ids.length - 1);
            console.log(ids);
            $("#batchReFinishModal .modal-body").text("你确定要让这些同学重做该作业吗？");
            $("#batchReFinishModal").modal({
                backdrop: "static"
            });
        }
    });
    $("#batchReFinish_btn").on("click", function () {
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //发送ajax请求删除
        $.ajax({
            url: "${APP_PATH}/teacher/taskReFinishBatch",
            type: "POST",
            dataType: "json",
            data: {
                "ids": ids,
                "type": "${type}",
                "taskId":${taskId}
            },
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("处理成功", {time: 1000, icon: 1}, function () {
                        window.location.reload();
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
        if (${page==null}) {
            window.location.href = "${APP_PATH}/teacher/searchChapterTask/${chapterId}?pageNum=${pageNum}&courseId=${courseId}";
        } else {
            window.location.href = "${APP_PATH}/teacher/searchTask?pageNum=${page}&type=${type}";
        }
    }

    //重新完成作业
    $(".refinish").on("click", function () {
        let studentTaskId = $(this).attr("studentTaskId");
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //发送ajax请求删除
        $.ajax({
            url: "${APP_PATH}/teacher/taskReFinish/" + studentTaskId,
            type: "POST",
            dataType: "json",
            data: {
                "type": "${type}"
            },
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("处理成功", {time: 1000, icon: 1}, function () {
                        window.location.reload();
                    });
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });

    //查询
    $("#search").on("click", function () {
        let name = $("#name").val();
        let number = $("#number").val();
        if (${page==null}) {
            window.location.href = "${APP_PATH}/teacher/viewTaskFinish/${taskId}?pageNum=${pageNum}&chapterId=${chapterId}&type=${type}&courseId=${courseId}&name=" + name + "&number=" + number;
        } else {
            window.location.href = "${APP_PATH}/teacher/viewTaskFinish/${taskId}?page=${page}&chapterId=${chapterId}&type=${type}&courseId=${courseId}&name=" + name + "&number=" + number;
        }
    });

    $(".downloadFiles").on("click", function () {
        window.location.href = "${APP_PATH}/teacher/downloadFiles?courseId=${courseId}&chapterId=${chapterId}&taskId=${taskId}"
    });
</script>