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
            <li><a href="javascript:void(0)">课程章节管理</a></li>
            <li><a href="javascript:void(0)">章节查询</a></li>
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
            <div style="position: absolute;top: -2px;right: 150px;">
                <form class="navbar-form navbar-right" role="search" action="" method="post">
                    <div class="form-group">
                        <button type="button" class="btn btn-danger batchDelete" data-toggle="tooltip"
                                data-placement="left"
                                title="批量删除章节">
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
                    <th>章节标题</th>
                    <th>章节内容</th>
                    <th>所属课程(学院)</th>
                    <th style="width:200px">发布日期</th>
                    <th style="width:400px">选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${chapters}" var="chapter">
                    <tr>
                        <th>
                            <label>
                                <input chapterId="${chapter.id}" type="checkbox" class="select_item"/>
                            </label>
                        </th>
                        <td>${chapter.title}</td>
                        <td>${chapter.content}</td>
                        <c:if test="${chapter.course!=null}">
                            <td>${chapter.course.name.trim()=="0"?"课程未设置名称":(chapter.course.name.trim()==""?"课程未设置名称":chapter.course.name) }(${chapter.course.college.name})</td>
                        </c:if>
                        <c:if test="${chapter.course==null}">
                            <td>未录入</td>
                        </c:if>
                        <c:if test="${chapter.recordTime!=null}">
                            <td><fmt:formatDate value="${chapter.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        </c:if>
                        <c:if test="${chapter.recordTime==null}">
                            <td>未录入</td>
                        </c:if>
                        <td>
                            <button type="button" class="btn btn-info viewFiles"
                                    data-toggle="tooltip" chapterId="${chapter.id}"
                                    data-placement="left" title="查看该章节下所有的课件" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                                查看课件
                            </button>
                            <button type="button" class="btn btn-info viewTask"
                                    data-toggle="tooltip" chapterId="${chapter.id}"
                                    data-placement="left" title="查看该章节下所有的作业" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-tasks" aria-hidden="true"></span>
                                查看作业
                            </button>
                            <button type="button" class="btn btn-info edit"
                                    data-toggle="tooltip" chapterId="${chapter.id}" courseId="${chapter.courseId}"
                                    data-placement="left" title="编辑该章节信息" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                编辑
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="panel-footer wk-panel-footer" style="margin-top: 50px">
                <button type="button" class="btn btn-info"
                        onclick="window.location.href='${APP_PATH}/teacher/searchCourse?pageNum=${pageNum}'"
                        style="margin-left: 20px">返&nbsp;&nbsp;回
                </button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="chapterBatchDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">批量删除章节</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <span>删除这些章节信息将删除该章节的所有课件，你确定要这么做吗？</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="chapter_batchDelete_btn">确定</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    let ids = "";
    $(".viewFiles").on("click", function () {
        let chapterId = $(this).attr("chapterId");
        console.log(chapterId);
        window.location.href = "${APP_PATH}/teacher/viewChapterFiles/" + chapterId + "?pageNum=${pageNum}&courseId=${courseId}";
    });
    $(".viewTask").on("click", function () {
        let chapterId = $(this).attr("chapterId");
        console.log(chapterId);
        window.location.href = "${APP_PATH}/teacher/searchChapterTask/" + chapterId + "?pageNum=${pageNum}&courseId=${courseId}";
    });
    $(".edit").on("click", function () {
        let chapterId = $(this).attr("chapterId");
        let courseId = $(this).attr("courseId");
        window.location.href = "${APP_PATH}/teacher/editChapter/" + chapterId + "?preCourseId=" + courseId + "&pageNum=${pageNum}";
    });
    //批量删除章节
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
            ids += $(this).attr("chapterId") + "-";
        });
        console.log(ids);
        if (ids.trim() === "") {
            layer.msg("操作失败，你未选择任何内容", {time: 1500, icon: 5, shift: 6}, function () {
            });
        } else {
            //去除删除的id多余的"-"
            ids = ids.substring(0, ids.length - 1);
            console.log(ids);

            $("#chapterBatchDeleteModal").modal({
                backdrop: "static"
            });
        }
    });
    $("#chapter_batchDelete_btn").on("click", function () {
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //发送ajax请求删除
        $.ajax({
            url: "${APP_PATH}/teacher/deleteChapterBatch",
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
                    <%--window.location.href = "${APP_PATH}/teacher/searchChapter?courseId=${courseId}";--%>
                    window.location.reload(true);
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });
</script>