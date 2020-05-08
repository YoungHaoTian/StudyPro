<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <li><a href="javascript:void(0)">作业查询</a></li>
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
            <div style="position: absolute;top: -11px;left: 150px;">
                <form class="navbar-form navbar-right" role="search" action="" method="post">
                    <div class="form-group">
                        <label for="courseId" class="control-label wk-filed-label"
                               style="margin-top: 20px">所属课程:</label>
                        <select id="courseId" class="selectpicker" name="courseId">
                            <option value="0">请选择所属课程</option>
                            <c:forEach items="${courses}" var="course">
                                <c:choose>
                                    <c:when test="${course.id == sessionScope.taskQueryCriteria.get('courseId')}">
                                        <option value="${course.id}"
                                                selected="selected">${course.name}:${course.college.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${course.id}">${course.name}:${course.college.name}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="title" class="control-label wk-filed-label"
                               style="margin-top: 20px;margin-left: 0px;">作业标题:</label>
                        <input type="text" maxlength="18"
                               class="form-control" id="title" name="title" placeholder="请输入作业标题"
                               value="${'online'.equals(sessionScope.taskType)?sessionScope.onlineTaskQueryCriteria.get('title'):sessionScope.offlineTaskQueryCriteria.get('title')}">
                    </div>
                    <div class="form-group">
                        <label for="minTime" class="control-label wk-filed-label"
                               style="margin-top: 20px;margin-left: 0px;">发布时间:</label>
                        <input type="date" class="form-control" name="name" id="minTime"
                               value="${'online'.equals(sessionScope.taskType)?sessionScope.onlineTaskQueryCriteria.get('minTime'):sessionScope.offlineTaskQueryCriteria.get('minTime')}"/>
                        <label for="maxTime" class="control-label wk-filed-label"
                               style="margin-top: 20px;margin-left: 0px;">到:</label>
                        <input type="date" class="form-control" name="name" id="maxTime"
                               value="${'online'.equals(sessionScope.taskType)?sessionScope.onlineTaskQueryCriteria.get('maxTime'):sessionScope.offlineTaskQueryCriteria.get('maxTime')}"/>
                    </div>
                    <div class="form-group" style="margin-left: 20px">
                        <button type="button" id="search" class="btn btn-success search" data-toggle="tooltip"
                                data-placement="left" title="查询讨论" style="margin-right: 20px;margin-left: 0px;">
                            <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                            查询
                        </button>
                    </div>
                    <div class="form-group">
                        <button type="button" class="btn btn-primary viewOnlineTask" data-toggle="tooltip"
                                data-placement="left" style="margin-right: 20px"
                                title="查看线上作业">
                            <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                            线上作业
                        </button>
                    </div>
                    <div class="form-group">
                        <button type="button" class="btn btn-primary viewOfflineTask" data-toggle="tooltip"
                                data-placement="left"
                                title="查看线下作业">
                            <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                            线下作业
                        </button>
                    </div>
                </form>
            </div>
            <!-- 搜索 end -->
        </div>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default  wk-panel">
            <table class="table table-striped table-hover" style="table-layout:fixed;">
                <thead>
                <tr class="info">
                    <th style="width:80px">
                        序号
                    </th>
                    <th>作业标题</th>
                    <th>所属章节</th>
                    <th style="width:300px">所属课程：学院</th>
                    <th style="width:200px">发布时间</th>
                    <c:if test="${'offline'.equals(sessionScope.taskType)}">
                        <th style="width:200px">选择操作</th>
                    </c:if>
                    <c:if test="${'online'.equals(sessionScope.taskType)}">
                        <th style="width:350px">选择操作</th>
                    </c:if>
                </tr>
                </thead>
                <tbody>
                <c:set var="index" value="1"/>
                <c:forEach items="${pageInfo.list}" var="task">
                    <tr>
                        <td>
                                ${index}
                        </td>
                        <td>${task.title}</td>
                        <td>${task.courseChapter.title}</td>
                        <td>${task.courseChapter.course.name}：${task.courseChapter.course.college.name}</td>
                        <td><fmt:formatDate value="${task.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        <c:if test="${'online'.equals(sessionScope.taskType)}">
                            <td>
                                <button type="button" class="btn btn-danger btn-sm deleteOnlineBtn"
                                        data-toggle="tooltip" onlineTaskId="${task.id}"
                                        data-placement="left" title="删除该作业" style="margin-right: 20px">
                                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>

                                </button>
                                <button type="button" class="btn btn-info btn-sm updateOnlineBtn"
                                        data-toggle="tooltip" onlineTaskId="${task.id}"
                                        data-placement="left" title="编辑该作业" style="margin-right: 20px">
                                    <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>

                                </button>
                                <button type="button" class="btn btn-info btn-sm searchQuestionBtn"
                                        data-toggle="tooltip" onlineTaskId="${task.id}"
                                        data-placement="left" title="查看该作业的题目" style="margin-right: 20px">
                                    <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>

                                </button>
                                <button type="button" class="btn btn-info btn-sm createQuestionBtn"
                                        data-toggle="tooltip" onlineTaskId="${task.id}"
                                        data-placement="left" title="录入该作业的题目" style="margin-right: 20px">
                                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>

                                </button>
                                <button type="button" class="btn btn-success btn-sm viewTaskFinishBtn"
                                        data-toggle="tooltip" onlineTaskId="${task.id}"
                                        chapterId="${task.courseChapter.id}" courseId="${task.courseChapter.course.id}"
                                        data-placement="left" title="查看作业完成情况">
                                    <span class="glyphicon glyphicon-tasks" aria-hidden="true"></span>

                                </button>
                            </td>
                        </c:if>
                        <c:if test="${'offline'.equals(sessionScope.taskType)}">
                            <td>
                                <button type="button" class="btn btn-danger btn-sm deleteOfflineBtn"
                                        data-toggle="tooltip" offlineTaskId="${task.id}"
                                        courseId="${task.courseChapter.course.id}"
                                        chapterId="${task.courseChapter.id}"
                                        data-placement="left" title="删除该作业" style="margin-right: 20px">
                                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                                </button>
                                <button type="button" class="btn btn-info btn-sm updateOfflineBtn"
                                        data-toggle="tooltip" offlineTaskId="${task.id}"
                                        data-placement="left" title="编辑该作业" style="margin-right: 20px">
                                    <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                </button>
                                <button type="button" class="btn btn-success btn-sm viewTaskFinishBtn"
                                        data-toggle="tooltip" offlineTaskId="${task.id}"
                                        chapterId="${task.courseChapter.id}" courseId="${task.courseChapter.course.id}"
                                        data-placement="left" title="查看作业完成情况">
                                    <span class="glyphicon glyphicon-tasks" aria-hidden="true"></span>
                                </button>
                            </td>
                        </c:if>
                    </tr>
                    <s:set var="index" value="${index+1}"/>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<c:if test="${!empty pageInfo.list}">
    <!--显示分页信息-->
    <div class="row">
        <!--分页文字信息  -->
        <div class="col-md-6 col-md-offset-2">当前
            <kbd>${pageInfo.pageNum }</kbd>
            页，总<kbd>${pageInfo.pages }</kbd>
            页，总<kbd>${pageInfo.total }</kbd>
            条记录
        </div>
    </div>
    <!-- 分页条信息 -->
    <div class="row">
        <div class="col-md-6 col-md-offset-6">
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <c:if test="${pageInfo.hasPreviousPage }">
                        <li><a href="${APP_PATH}/teacher/searchTask?pageNum=1">首页</a></li>
                        <li><a href="${APP_PATH}/teacher/searchTask?pageNum=${pageInfo.pageNum-1}"
                               aria-label="Previous"><span aria-hidden="true">&laquo;</span>
                        </a></li>
                    </c:if>
                    <c:if test="${!pageInfo.hasPreviousPage}">
                        <li><a href="javascript:void(0)" style="pointer-events: none">首页</a></li>
                        <li><a href="javascript:void(0)" style="pointer-events: none"
                               aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
                        </a></li>
                    </c:if>

                    <c:forEach items="${pageInfo.navigatepageNums }" var="page_Num">
                        <c:if test="${page_Num == pageInfo.pageNum }">
                            <li class="active"><a href="javascript:void(0)">${page_Num }</a></li>
                        </c:if>
                        <c:if test="${page_Num != pageInfo.pageNum }">
                            <li><a href="${APP_PATH }/teacher/searchTask?pageNum=${page_Num }">${page_Num }</a>
                            </li>
                        </c:if>
                    </c:forEach>

                    <c:if test="${pageInfo.hasNextPage }">
                        <li><a href="${APP_PATH }/teacher/searchTask?pageNum=${pageInfo.pageNum+1 }"
                               aria-label="Next"> <span aria-hidden="true">&raquo;</span>
                        </a></li>
                        <li><a href="${APP_PATH }/teacher/searchTask?pageNum=${pageInfo.pages}">末页</a></li>
                    </c:if>
                    <c:if test="${!pageInfo.hasNextPage}">
                        <li><a href="javascript:void(0)" style="pointer-events: none"
                               aria-label="Next"> <span aria-hidden="true">&raquo;</span>
                        </a></li>
                        <li><a href="javascript:void(0)" style="pointer-events: none">末页</a></li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </div>
    <!-- 页面跳转信息 -->
    <div class="row" style="margin-bottom: 50px">
        <div class="col-sm-2 col-md-offset-6">
            <input type="number" class="form-control" id="pageNum" placeholder="跳转到...">
        </div>
        <button type="button" class="btn btn-success toPage" style="margin-left: 20px">确定跳转</button>
    </div>
</c:if>
<div class="modal fade" id="onlineTaskDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">线上作业删除</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <span>删除当前作业将删除对应的题目，你确定这么做吗？</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="onlineTask_delete_btn">确定</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="offlineTaskDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">线下作业删除</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <span>删除当前作业将删除对应的文件，你确定这么做吗？</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="offlineTask_delete_btn">确定</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="taskBatchDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">作业批量删除</h5>
            </div>
            <div class="modal-body" style="text-align: center">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="task_batchDelete_btn">确定</button>
            </div>
        </div>
    </div>
</div>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    let courseId = "";
    let chapterId = "";
    let id = "";
    //删除单个作业
    $(".deleteOnlineBtn").on("click", function () {
        id = $(this).attr("onlineTaskId");
        console.log(id);
        //删除课程时弹出确认框
        let message = "删除当前作业将删除对应的题目，你确定这么做吗？";
        $("#onlineTaskDeleteModal .modal-body").text(message);
        $("#onlineTaskDeleteModal").modal({
                backdrop: "static"
            }
        );
    });
    $("#onlineTask_delete_btn").on("click", function () {
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //确认，发送ajax请求删除即可
        $.ajax({
            url: "${APP_PATH}/teacher/deleteOnlineTask",
            type: "POST",
            dataType: "json",
            data: {
                "id": id
            },
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("删除成功", {time: 1000, icon: 1}, function () {window.location.reload();
                    });
                    <%--window.location.href = "${APP_PATH}/teacher/searchTask?pageNum=${pageInfo.pageNum }";--%>
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        })
    });
    //删除线下作业
    $(".deleteOfflineBtn").on("click", function () {
        id = $(this).attr("offlineTaskId");
        courseId = $(this).attr("courseId");
        chapterId = $(this).attr("chapterId");
        console.log(id);
        //删除课程时弹出确认框
        $("#offlineTaskDeleteModal").modal({
                backdrop: "static"
            }
        );
    });
    $("#offlineTask_delete_btn").on("click", function () {
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //确认，发送ajax请求删除即可
        $.ajax({
            url: "${APP_PATH}/teacher/deleteOfflineTask",
            type: "POST",
            dataType: "json",
            data: {
                "id": id,
                "courseId": courseId,
                "chapterId": chapterId
            },
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("删除成功", {time: 1000, icon: 1}, function () {
                        window.location.reload();
                    });
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        })
    });

    //页面跳转
    $(".toPage").on("click", function () {
        //获取到将要跳转的页面值
        let pageNum = $("#pageNum").val();
        let total =${pageInfo.pages };
        if (pageNum.trim() === "" || total < pageNum || pageNum <= 0) {
            layer.msg("错误的跳转页码，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (pageNum ==${pageInfo.pageNum}) {
            layer.msg("当前已经是第" + pageNum + "页", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        window.location.href = "${APP_PATH}/teacher/searchTask?pageNum=" + pageNum;
    });
    //查询按钮
    $("#search").on("click", function () {
        let courseId = $("#courseId").val().trim();
        let title = $("#title").val().trim();
        let minTime = $("#minTime").val().trim();
        let maxTime = $("#maxTime").val().trim();
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //发送ajax请求
        $.ajax({
            url: "${APP_PATH}/teacher/searchTaskByTerm",
            type: "POST",
            dataType: "json",
            data: {"courseId": courseId, "title": title, "minTime": minTime, "maxTime": maxTime},
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("查询成功", {time: 1000, icon: 1}, function () {
                        window.location.href = "${APP_PATH}/teacher/searchTask";
                    });
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });

    $(".updateOnlineBtn").on("click", function () {
        let id = $(this).attr("onlineTaskId");
        window.location.href = "${APP_PATH}/teacher/editTask/" + id + "?pageNum=${pageInfo.pageNum}&type=online";
    });

    $(".updateOfflineBtn").on("click", function () {
        let id = $(this).attr("offlineTaskId");
        window.location.href = "${APP_PATH}/teacher/editTask/" + id + "?pageNum=${pageInfo.pageNum}&type=offline";
    });
    $(".createQuestionBtn").on("click", function () {
        let id = $(this).attr("onlineTaskId");
        window.location.href = "${APP_PATH}/teacher/createOnlineTaskQuestion/" + id;
    });

    $(".searchQuestionBtn").on("click", function () {
        let id = $(this).attr("onlineTaskId");
        window.location.href = "${APP_PATH}/teacher/searchOnlineTaskQuestion/" + id + "?pageNumber=${pageInfo.pageNum}";
    });

    $(".viewOnlineTask").on("click", function () {
        window.location.href = "${APP_PATH}/teacher/searchTask?taskType=online";
    });

    $(".viewOfflineTask").on("click", function () {
        window.location.href = "${APP_PATH}/teacher/searchTask?taskType=offline";
    });

    $(".viewTaskFinishBtn").on("click", function () {
        let id;
        let taskType = "${sessionScope.taskType}";
        let chapterId;
        let courseId;
        if (taskType === "online") {
            id = $(this).attr("onlineTaskId");
            chapterId = $(this).attr("chapterId");
            courseId = $(this).attr("courseId");
        }
        if (taskType === "offline") {
            id = $(this).attr("offlineTaskId");
            chapterId = $(this).attr("chapterId");
            courseId = $(this).attr("courseId");
        }
        console.log(id);
        window.location.href = "${APP_PATH}/teacher/viewTaskFinish/" + id + "?type=" + taskType + "&page=${pageInfo.pageNum}&courseId=" + courseId + "&chapterId=" + chapterId;
    });
</script>
</body>
</html>