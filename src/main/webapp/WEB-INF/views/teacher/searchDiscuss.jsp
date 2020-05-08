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
            <li><a href="javascript:void(0)">讨论查询</a></li>
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
                                    <c:when test="${course.id == sessionScope.discussQueryCriteria.get('courseId')}">
                                        <option value="${course.id}"
                                                selected="selected">${course.name}(${course.college.name})
                                        </option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${course.id}">${course.name}(${course.college.name})</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="title" class="control-label wk-filed-label"
                               style="margin-top: 20px">讨论标题:</label>
                        <input type="text" maxlength="18"
                               class="form-control" id="title" name="title" placeholder="请输入讨论标题"
                               value="${sessionScope.discussQueryCriteria.get("title")}">
                    </div>
                    <div class="form-group">
                        <label for="minTime" class="control-label wk-filed-label" style="margin-top: 20px">发布时间:</label>
                        <input type="date" class="form-control" name="name" id="minTime"
                               value="${sessionScope.discussQueryCriteria.get("minTime")}"/>
                        <label for="maxTime" class="control-label wk-filed-label" style="margin-top: 20px">到:</label>
                        <input type="date" class="form-control" name="name" id="maxTime"
                               value="${sessionScope.discussQueryCriteria.get("maxTime")}"/>
                    </div>
                    <div class="form-group" style="margin-left: 20px">
                        <button type="button" id="search" class="btn btn-success search" data-toggle="tooltip"
                                data-placement="left" title="查询讨论" style="margin-right: 20px">
                            <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                            查询
                        </button>
                    </div>
                    <div class="form-group">
                        <button type="button" class="btn btn-danger batchDelete" data-toggle="tooltip"
                                data-placement="left"
                                title="批量删除讨论">
                            <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                            批量删除
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
        <div class="panel panel-default wk-panel">
            <table class="table table-striped table-hover" style="table-layout:fixed;">
                <thead>
                <tr class="info">
                    <th style="width:80px">
                        <input type="checkbox" id="select_all"/>
                        <label for="select_all" style="margin-bottom: 0px;font-weight: 200">全选</label>
                    </th>
                    <th>讨论标题</th>
                    <th>讨论内容</th>
                    <th>所属课程(学院)</th>
                    <th style="width:200px">发布日期</th>
                    <th style="width:300px">选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${pageInfo.list}" var="discuss">
                    <tr>
                        <th>
                            <label>
                                <input discussId="${discuss.id}" type="checkbox" class="select_item"/>
                            </label>
                        </th>
                        <td>${discuss.title}</td>
                        <td>${discuss.content}</td>
                        <c:if test="${discuss.course!=null}">
                            <td>${discuss.course.name.trim()=="0"?"课程未设置名称":(discuss.course.name.trim()==""?"课程未设置名称":discuss.course.name) }(${discuss.course.college.name})</td>
                        </c:if>
                        <c:if test="${discuss.course==null}">
                            <td>未录入</td>
                        </c:if>
                        <c:if test="${discuss.recordTime!=null}">
                            <td><fmt:formatDate value="${discuss.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        </c:if>
                        <c:if test="${discuss.recordTime==null}">
                            <td>未录入</td>
                        </c:if>
                        <td>
                            <button type="button" class="btn btn-info btn-sm viewReplyBtn"
                                    data-toggle="tooltip" viewId="${discuss.id}"
                                    data-placement="left" title="查看该讨论的所有回复" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                                查看
                            </button>
                            <button type="button" class="btn btn-info btn-sm edit"
                                    data-toggle="tooltip" discussId="${discuss.id}"
                                    data-placement="left" title="编辑改讨论的信息" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                编辑
                            </button>
                        </td>
                    </tr>
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
                        <li><a href="${APP_PATH}/teacher/searchDiscuss?pageNum=1">首页</a></li>
                        <li><a href="${APP_PATH}/teacher/searchDiscuss?pageNum=${pageInfo.pageNum-1}"
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
                            <li><a href="${APP_PATH }/teacher/searchDiscuss?pageNum=${page_Num }">${page_Num }</a>
                            </li>
                        </c:if>
                    </c:forEach>

                    <c:if test="${pageInfo.hasNextPage }">
                        <li><a href="${APP_PATH }/teacher/searchDiscuss?pageNum=${pageInfo.pageNum+1 }"
                               aria-label="Next"> <span aria-hidden="true">&raquo;</span>
                        </a></li>
                        <li><a href="${APP_PATH }/teacher/searchDiscuss?pageNum=${pageInfo.pages}">末页</a></li>
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
<div class="modal fade" id="discussBatchDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">批量删除讨论</h5>
            </div>
            <div class="modal-body" style="text-align: center">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="discuss_batchDelete_btn">确定</button>
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
        window.location.href = "${APP_PATH}/teacher/searchDiscuss?pageNum=" + pageNum;
    });
    $(".viewReplyBtn").on("click", function () {
        let discussId = $(this).attr("viewId");
        window.location.href = "${APP_PATH}/teacher/searchDiscussReply/" + discussId + "?pageNumber=${pageInfo.pageNum }";
    });
    $(".edit").on("click", function () {
        let discussId = $(this).attr("discussId");
        window.location.href = "${APP_PATH}/teacher/editDiscuss/" + discussId + "?pageNumber=${pageInfo.pageNum }";
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
            url: "${APP_PATH}/teacher/searchDiscussByTerm",
            type: "POST",
            dataType: "json",
            data: {
                "courseId": courseId,
                "title": title,
                "minTime": minTime,
                "maxTime": maxTime
            },
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("查询成功", {time: 1000, icon: 1}, function () {
                        window.location.href = "${APP_PATH}/teacher/searchDiscuss";
                    });
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });
    //批量删除讨论
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
        if (flag) {
            $("#select_all").parent("th").children("label").text("取消");
        } else {
            $("#select_all").parent("th").children("label").text("全选");
        }
    });
    //点击批量删除按钮
    $(".batchDelete").on("click", function () {
        ids = "";
        $.each($(".select_item:checked"), function () {
            //组装课程id字符串
            ids += $(this).attr("discussId") + "-";
        });
        console.log(ids);
        if (ids.trim() === "") {
            layer.msg("操作失败，你未选择任何内容", {time: 1500, icon: 5, shift: 6}, function () {
            });
        } else {
            //去除删除的id多余的"-"
            ids = ids.substring(0, ids.length - 1);
            console.log(ids);
            $("#discussBatchDeleteModal .modal-body").text("你确定要删除这些讨论信息吗？");
            $("#discussBatchDeleteModal").modal({
                backdrop: "static"
            });
        }
    });
    $("#discuss_batchDelete_btn").on("click", function () {
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //发送ajax请求删除
        $.ajax({
            url: "${APP_PATH}/teacher/deleteDiscussBatch",
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
                        window.location.reload();
                    });
                    <%--window.location.href = "${APP_PATH}/teacher/searchDiscuss?&pageNum=" + ${pageInfo.pageNum};--%>
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });
</script>