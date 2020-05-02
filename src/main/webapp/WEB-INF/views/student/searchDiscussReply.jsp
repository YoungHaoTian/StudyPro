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
            <li><a href="javascript:void(0)">讨论管理</a></li>
            <li><a href="javascript:void(0)">回复查询</a></li>
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
                        <label for="name" class="control-label wk-filed-label"
                               style="margin-top: 20px">回复人:</label>
                        <input type="text" maxlength="5"
                               class="form-control" id="name" name="name" placeholder="请输入回复人名字"
                               value="${sessionScope.discussPostParameterMap.get("name")}">
                    </div>
                    <div class="form-group">
                        <label for="content" class="control-label wk-filed-label"
                               style="margin-top: 20px">回复内容:</label>
                        <input type="text" maxlength="5"
                               class="form-control" id="content" name="content" placeholder="请输入回复内容"
                               value="${sessionScope.discussPostParameterMap.get("content")}">
                    </div>
                    <div class="form-group">
                        <label for="minTime" class="control-label wk-filed-label" style="margin-top: 20px">回复时间:</label>
                        <input type="date" class="form-control" name="name" id="minTime"
                               value="${sessionScope.discussPostParameterMap.get("minTime")}"/>
                        <label for="maxTime" class="control-label wk-filed-label" style="margin-top: 20px">到:</label>
                        <input type="date" class="form-control" name="name" id="maxTime"
                               value="${sessionScope.discussPostParameterMap.get("maxTime")}"/>
                    </div>
                    <div class="form-group" style="margin-left: 20px">
                        <button type="button" id="search" class="btn btn-success search" data-toggle="tooltip"
                                data-placement="left" title="点击查询回复信息" style="margin-right: 20px">
                            <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                            查询
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default  wk-panel">
            <table class="table table-striped table-hover" style="table-layout:fixed;">
                <thead>
                <tr class="info">
                    <th>回复内容</th>
                    <th style="width:200px">回复人</th>
                    <th style="width:200px">回复时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${pageInfo.list}" var="post">
                    <tr>
                        <td>${post.content}</td>
                        <c:if test="${post.student!=null}">
                            <td>${post.student.name}</td>
                        </c:if>
                        <c:if test="${post.student==null}">
                            <c:if test="${post.studentId==0}">
                                <td><span style="color: red; ">老师</span></td>
                            </c:if>
                            <c:if test="${post.studentId!=0}">
                                <td><span style="color: blue; ">不存在</span></td>
                            </c:if>
                        </c:if>
                        <c:if test="${post.recordTime!=null}">
                            <td><fmt:formatDate value="${post.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        </c:if>
                        <c:if test="${post.recordTime==null}">
                            <td>未录入</td>
                        </c:if>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="panel-footer wk-panel-footer" style="margin-top:50px">
                <button type="button" class="btn btn-info replyBtn"
                        postId="${id}" title="回复该讨论"
                        style="margin-right: 20px">
                    回&nbsp;&nbsp;复
                </button>
                <button type="button" class="btn btn-info"
                        onclick="window.location.href='${APP_PATH}/student/searchDiscussInfo?courseId=${courseId}'">
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
                            <a href="${APP_PATH}/student/searchDiscussReply/${id}?pageNum=1&courseId=${courseId}">首页</a>
                        </li>
                        <li>
                            <a href="${APP_PATH}/student/searchDiscussReply/${id}?pageNum=${pageInfo.pageNum-1}&courseId=${courseId}"
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
                            <li>
                                <a href="${APP_PATH }/student/searchDiscussReply/${id}?pageNum=${page_Num }&courseId=${courseId}">${page_Num }</a>
                            </li>
                        </c:if>
                    </c:forEach>

                    <c:if test="${pageInfo.hasNextPage }">
                        <li>
                            <a href="${APP_PATH }/student/searchDiscussReply/${id}?pageNum=${pageInfo.pageNum+1 }&courseId=${courseId}"
                               aria-label="Next"> <span aria-hidden="true">&raquo;</span>
                            </a></li>
                        <li>
                            <a href="${APP_PATH }/student/searchDiscussReply/${id}?pageNum=${pageInfo.pages}&courseId=${courseId}">末页</a>
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
    <div class="row">
        <div class="col-sm-2 col-md-offset-6">
            <input type="number" class="form-control" id="pageNum" placeholder="跳转到...">
        </div>
        <button type="button" class="btn btn-success toPage" style="margin-left: 20px">确定跳转</button>
    </div>
</c:if>
</body>
<div class="modal fade" id="createReplyModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">回复讨论</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="replyContent" class="col-sm-2 control-label">回复内容：</label>
                        <div class="col-sm-9">
                            <textarea type="text" name="replyContent" class="form-control" id="replyContent"
                                      placeholder="请输入回复内容"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="reply_add_btn">提交</button>
            </div>
        </div>
    </div>
</div>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
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
        window.location.href = "${APP_PATH}/student/searchDiscussReply/${id}?courseId=${courseId}&pageNum=" + pageNum;
    });

    $(".replyBtn").on("click", function () {

        $("#createReplyModal").modal({
                backdrop: "static"
            }
        );
    });

    $("#reply_add_btn").on("click", function () {
        let postId = $(".replyBtn").attr("postId");
        let content = $("#replyContent").val().trim();
        if (content === "") {
            layer.msg("回复内容不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //发送ajax请求删除
        $.ajax({
            url: "${APP_PATH}/student/saveDiscussReply/${id}",
            type: "POST",
            dataType: "json",
            data: {
                "content": content
            },
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("新增回复成功", {time: 1000, icon: 1}, function () {
                    });
                    window.location.reload(true);
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });
    //查询按钮
    $("#search").on("click", function () {
        let name = $("#name").val().trim();
        let minTime = $("#minTime").val().trim();
        let maxTime = $("#maxTime").val().trim();
        let content = $("#content").val().trim();
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //发送ajax请求
        $.ajax({
            url: "${APP_PATH}/student/searchDiscussPostByTerm/${id}",
            type: "POST",
            dataType: "json",
            data: {
                "name": name,
                "minTime": minTime,
                "maxTime": maxTime,
                "content": content
            },
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("查询成功", {time: 1000, icon: 1}, function () {
                    });
                    window.location.href = "${APP_PATH}/student/searchDiscussReply/${id}?courseId=${courseId}";
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });
</script>