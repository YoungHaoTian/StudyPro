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
    <link rel="stylesheet" href="${APP_PATH}/resources1/css/wukong-ui.css">
    <link rel="stylesheet" href="${APP_PATH}/resources1/bootstrap/css/bootstrap-select.min.css">
    <script type="text/javascript" src="${APP_PATH}/resources1/js/jquery-3.1.1.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap-select.min.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap.min.js"></script>
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
            <li><a href="javascript:void(0);">大学生学习平台</a></li>
            <li><a href="javascript:void(0);">课程信息管理</a></li>
            <li><a href="javascript:void(0);">我的课程</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel wk-table-tools-panel">
            <div class="panel-heading">
                工具栏 Tools
            </div>
            <div style="position: absolute;top:-1px;right: 200px;">
                <form class="navbar-form navbar-right" role="search">
                    <div class="form-group">
                        <button type="button" class="btn btn-success goJoinCourse" data-toggle="tooltip"
                                data-placement="left"
                                title="去加入课程">
                            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                            去添加课程
                        </button>
                    </div>
                    <div class="form-group" style="margin-left: 20px">
                        <button type="button" class="btn btn-danger removeCourseBatch" data-toggle="tooltip"
                                data-placement="left"
                                title="批量移除课程">
                            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            批量移除课程
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
                    <th style="width: 80px">
                        <input type="checkbox" id="select_all"/>
                        <label for="select_all" style="margin-bottom: 0px;font-weight: 200">全选</label>
                    </th>
                    <th>课程名称</th>
                    <th style="width: 150px">课程编号</th>
                    <th style="width: 300px">授课教师(<span style="color: green">所属学院</span>)</th>
                    <th>课程简介</th>
                    <th style="width: 300px">选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${pageInfo.list}" var="course">
                    <tr>
                        <th>
                            <label>
                                <input courseId="${course.id}" type="checkbox" class="select_item"/>
                            </label>
                        </th>
                        <td>${course.name}</td>
                        <td>${course.number}</td>
                        <td>${course.teacher.name}(<span style="color: green">${course.teacher.college.name}</span>)
                        </td>
                        <td>${course.intro}</td>
                        <td>
                            <button class="btn btn-success btn-sm viewCourseFile" data-toggle="tooltip"
                                    data-placement="left"
                                    title="查看该课程文件" courseId="${course.id}" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                                查看课程文件
                            </button>
                            <button class="btn btn-danger btn-sm removeCourse" data-toggle="tooltip"
                                    data-placement="left"
                                    title="移除当前课程" courseId="${course.id}">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                                移除课程
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
                        <li><a href="${APP_PATH}/student/searchMyCourseInfo?pageNum=1">首页</a></li>
                        <li><a href="${APP_PATH}/student/searchMyCourseInfo?pageNum=${pageInfo.pageNum-1}"
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
                            <li><a href="${APP_PATH }/student/searchMyCourseInfo?pageNum=${page_Num }">${page_Num }</a>
                            </li>
                        </c:if>
                    </c:forEach>

                    <c:if test="${pageInfo.hasNextPage }">
                        <li><a href="${APP_PATH }/student/searchMyCourseInfo?pageNum=${pageInfo.pageNum+1 }"
                               aria-label="Next"> <span aria-hidden="true">&raquo;</span>
                        </a></li>
                        <li><a href="${APP_PATH }/student/searchMyCourseInfo?pageNum=${pageInfo.pages}">末页</a></li>
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
<div class="modal fade" id="courseRemoveModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">移除课程</h5>
            </div>
            <div class="modal-body" style="text-align: center">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="course_remove_btn">确定</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="courseBatchRemoveModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">批量移除课程</h5>
            </div>
            <div class="modal-body" style="text-align: center">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="course_batchRemove_btn">确定</button>
            </div>
        </div>
    </div>
</div>
<script>
</script>
</body>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    let ids = "";
    let id = "";
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
        window.location.href = "${APP_PATH}/student/searchMyCourseInfo?pageNum=" + pageNum;
    });
    //批量移除课程
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
    //点击批量移除按钮
    $(".removeCourseBatch").on("click", function () {
        $.each($(".select_item:checked"), function () {
            //组装课程id字符串
            ids += $(this).attr("courseId") + "-";
        });
        console.log(ids);
        if (ids.trim() === "") {
            layer.msg("操作失败，你未选择任何课程", {time: 1500, icon: 5, shift: 6}, function () {
            });
        } else {
            //去除删除的id多余的"-"
            ids = ids.substring(0, ids.length - 1);
            console.log(ids);
            $("#courseBatchRemoveModal .modal-body").text("你确定要移除这些课程吗？");
            $("#courseBatchRemoveModal").modal({
                backdrop: "static"
            });
        }
    });
    $("#course_batchRemove_btn").on("click", function () {
        let loadingIndex = layer.msg('处理中', {icon: 16});
        $.ajax({
            url: "${APP_PATH}/student/removeCourseBatch",
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
                    layer.msg("批量移除课程成功", {time: 1000, icon: 1}, function () {
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
    $(".goJoinCourse").on("click", function () {
        window.location.href = "${APP_PATH}/student/searchCourseInfo";
    });

    $(".removeCourse").on("click", function () {
        id = $(this).attr("courseId");
        console.log(id);
        let name = $(this).parents("tr").find("td:eq(0)").text();
        console.log(name);
        let message = "确定移除课程 【" + name + "】 吗？";
        if (name === "") {
            message = "确定移除该课程吗？";
        }
        $("#courseRemoveModal .modal-body").text(message);
        $("#courseRemoveModal").modal({
                backdrop: "static"
            }
        );
    });
    $("#course_remove_btn").on("click", function () {
        let loadingIndex = layer.msg('处理中', {icon: 16});
        $.ajax({
            url: "${APP_PATH}/student/removeCourse",
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
                    layer.msg("移除课程成功", {time: 1000, icon: 1}, function () {
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

    $(".viewCourseFile").on("click", function () {
        let courseId = $(this).attr("courseId");
        console.log(courseId);
        window.location.href = "${APP_PATH}/student/searchChapter?courseId=" + courseId + "&pageNum=${pageInfo.pageNum}";
    })
</script>