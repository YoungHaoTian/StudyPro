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
                    <th>题目</th>
                    <th>选项A</th>
                    <th>选项B</th>
                    <th>选项C</th>
                    <th>选项D</th>
                    <th>答案</th>
                    <th>分值</th>
                    <th>选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${pageInfo.list}" var="question">
                    <tr>
                        <th>
                            <label>
                                <input questionId="${question.id}" type="checkbox" class="select_item"/>
                            </label>
                        </th>
                        <td>${question.title}</td>
                        <td>${question.itemA}</td>
                        <td>${question.itemB}</td>
                        <td>${question.itemC}</td>
                        <td>${question.itemD}</td>
                        <td>${question.answer}</td>
                        <td>${question.score}</td>
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
            <div class="panel-footer wk-panel-footer" style="margin-top:50px">
                <button type="button" class="btn btn-info" onclick="back();">
                    返&nbsp;&nbsp;回
                </button>
            </div>
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
                        <li>
                            <c:if test="${page==null}">
                                <a href="${APP_PATH}/teacher/searchTaskQuestion/${taskId}?pageNum=1&pageNumber=${pageNumber}">首页</a>
                            </c:if>
                            <c:if test="${page!=null}">
                                <a href="${APP_PATH}/teacher/searchTaskQuestion/${taskId}?pageNum=1&page=${page}&courseId=${courseId}&chapterId=${chapterId}">首页</a>
                            </c:if>
                        </li>
                        <li>
                            <c:if test="${page==null}">
                                <a href="${APP_PATH}/teacher/searchTaskQuestion/${taskId}?pageNum=${pageInfo.pageNum-1}&pageNumber=${pageNumber}"
                                   aria-label="Previous"><span aria-hidden="true">&laquo;</span>
                                </a>
                            </c:if>
                            <c:if test="${page!=null}">
                                <a href="${APP_PATH}/teacher/searchTaskQuestion/${taskId}?pageNum=${pageInfo.pageNum-1}&page=${page}&courseId=${courseId}&chapterId=${chapterId}"
                                   aria-label="Previous"><span aria-hidden="true">&laquo;</span>
                                </a>
                            </c:if>
                        </li>
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
                            <li>
                                <c:if test="${page==null}">
                                    <a href="${APP_PATH }/teacher/searchTaskQuestion/${taskId}?pageNum=${page_Num }&pageNumber=${pageNumber}">${page_Num }</a>
                                </c:if>
                                <c:if test="${page!=null}">
                                    <a href="${APP_PATH }/teacher/searchTaskQuestion/${taskId}?pageNum=${page_Num }&page=${page}&courseId=${courseId}&chapterId=${chapterId}">${page_Num }</a>
                                </c:if>
                            </li>
                        </c:if>
                    </c:forEach>
                    <c:if test="${pageInfo.hasNextPage }">
                        <li>
                            <c:if test="${page==null}">
                                <a href="${APP_PATH }/teacher/searchTaskQuestion/${taskId}?pageNum=${pageInfo.pageNum+1 }&pageNumber=${pageNumber}"
                                   aria-label="Next"> <span aria-hidden="true">&raquo;</span>
                                </a>
                            </c:if>
                            <c:if test="${page!=null}">
                                <a href="${APP_PATH }/teacher/searchTaskQuestion/${taskId}?pageNum=${pageInfo.pageNum+1 }&page=${page}&courseId=${courseId}&chapterId=${chapterId}"
                                   aria-label="Next"> <span aria-hidden="true">&raquo;</span>
                                </a>
                            </c:if>
                        </li>
                        <li>
                            <c:if test="${page==null}">
                                <a href="${APP_PATH }/teacher/searchTaskQuestion/${taskId}?pageNum=${pageInfo.pages}&pageNumber=${pageNumber}">末页</a>
                            </c:if>
                            <c:if test="${page!=null}">
                                <a href="${APP_PATH }/teacher/searchTaskQuestion/${taskId}?pageNum=${pageInfo.pages}&page=${page}&courseId=${courseId}&chapterId=${chapterId}">末页</a>
                            </c:if>
                        </li>
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
<div class="modal fade" id="questionBatchDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">作题目批量删除</h5>
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
        if (${page!=null}) {
            window.location.href = "${APP_PATH }/teacher/searchTaskQuestion/${taskId}?pageNum=" + pageNum + "&page=${page}&courseId=${courseId}&chapterId=${chapterId}";
        } else {
            window.location.href = "${APP_PATH}/teacher/searchTaskQuestion/${taskId}?pageNum=" + pageNum + "&pageNumber=${pageNumber}";
        }
    });

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
        $("#select_all").prop("checked", flag);
    });
    //点击批量删除按钮
    $(".batchDelete").on("click", function () {
        ids="";
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
                    <%--window.location.href = "${APP_PATH}/teacher/searchTaskQuestion/${taskId}?pageNum=${pageInfo.pageNum}&pageNumber=${pageNumber}";--%>
                    window.location.reload(true);
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
            window.location.href = "${APP_PATH }/teacher/editTaskQuestion/" + questionId + "?pageNum=${pageInfo.pageNum}&page=${page}&courseId=${courseId}&chapterId=${chapterId}";
        } else {
            window.location.href = "${APP_PATH}/teacher/editTaskQuestion/" + questionId + "?pageNum=${pageInfo.pageNum}&pageNumber=${pageNumber}";
        }
    });
</script>