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
            <li><a href="javascript:void(0)">章节课件查看</a></li>
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
            <div style="position: absolute;top: -11px;left: 200px;">
                <form class="navbar-form navbar-right" role="search"
                      action="" method="post">
                    <div class="form-group">
                        <label for="minTime" class="control-label wk-filed-label" style="margin-top: 20px">上传时间:</label>
                        <input type="date" class="form-control" name="name" id="minTime"
                               value="${minTime}"/>
                        <label for="maxTime" class="control-label wk-filed-label" style="margin-top: 20px">到:</label>
                        <input type="date" class="form-control" name="name" id="maxTime"
                               value="${maxTime}"/>
                    </div>
                    <div class="form-group" style="margin-left: 20px">
                        <button type="button" id="search" class="btn btn-success search" data-toggle="tooltip"
                                data-placement="left" title="查询章节课件" style="margin-right: 20px">
                            <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                            查询
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
            <table class="table table-striped table-hover">
                <thead>
                <tr class="info">
                    <th>文件名</th>
                    <th>上传时间</th>
                    <th>文件类型</th>
                    <th>选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${files}" var="file">
                    <tr>
                        <td>${file.path.substring(file.path.indexOf("_")+1)}</td>
                        <td><fmt:formatDate value="${file.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        <td>课件</td>
                        <td>
                            <button class="btn btn-success btn-sm downloadFile" data-toggle="tooltip"
                                    data-placement="left"
                                    title="下载当前文档" fileId="${file.id}" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>
                                下载文档
                            </button>
                            <button class="btn btn-primary btn-sm editFile" data-toggle="tooltip" data-placement="left"
                                    title="重新上传当前文档" fileId="${file.id}" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                重新上传
                            </button>
                            <a class="btn btn-danger btn-sm deleteFile" data-toggle="tooltip" data-placement="left"
                               title="删除当前文档" fileId="${file.id}">
                                <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                                删除
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                <c:forEach items="${videos}" var="video">
                    <tr>
                        <td>${video.path.substring(video.path.indexOf("_")+1)}</td>
                        <td><fmt:formatDate value="${video.recordTime}" pattern="yyyy-MM-dd  HH:mm:ss"/></td>
                        <td>视频</td>
                        <td>
                            <button class="btn btn-success btn-sm viewVideo" data-toggle="tooltip" data-placement="left"
                                    title="在线观看该视频" videoId="${video.id}" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                                在线观看
                            </button>
                            <button class="btn btn-primary btn-sm editVideo" data-toggle="tooltip" data-placement="left"
                                    title="重新上传当前视频" videoId="${video.id}" style="margin-right: 20px">
                                <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                重新上传
                            </button>
                            <a class="btn btn-danger btn-sm deleteVideo" data-toggle="tooltip" data-placement="left"
                               title="删除当前视频" videoId="${video.id}">
                                <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                                删除
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="panel-footer wk-panel-footer" style="margin-top:50px">
                <button type="button" class="btn btn-info" onclick="back()">返&nbsp;&nbsp;回</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="fileDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title">文档删除</h5>
            </div>
            <div class="modal-body">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="file_delete_btn">确定</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="videoDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title">视频删除</h5>
            </div>
            <div class="modal-body">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="video_delete_btn">确定</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    let ids = "";
    let id = "";
    //删除单个文档
    $(".deleteFile").on("click", function () {
        id = $(this).attr("fileId");
        console.log(id);
        //删除课程时弹出确认框
        let name = $(this).parents("tr").find("td:eq(0)").text();
        console.log(name);
        let message = "确定删除当前文档【" + name + "】吗？";

        $("#fileDeleteModal .modal-body").text(message);
        $("#fileDeleteModal").modal({
                backdrop: "static"
            }
        );
    });
    $("#file_delete_btn").on("click", function () {
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //确认，发送ajax请求删除即可
        $.ajax({
            url: "${APP_PATH}/teacher/deleteCourseFile",
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
                    window.location.href = "${APP_PATH}/teacher/viewChapterFiles/${id}";
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        })
    });

    //编辑文档
    $(".editFile").on("click", function () {
        let id = $(this).attr("fileId");
        let minTime = $("#minTime").val().trim();
        let maxTime = $("#maxTime").val().trim();
        window.location.href = "${APP_PATH}/teacher/editCourseFile/" + id + "?pageNum=0&chapterId=${id}&pageNumber=${pageNum}&minTime=" + minTime + "&maxTime=" + maxTime + "&pageNum=${pageNum}";
    });
    //下载文档
    $(".downloadFile").on("click", function () {
        let id = $(this).attr("fileId");
        window.location.href = "${APP_PATH}/teacher/downloadCourseFile/" + id;
    })

    //删除单个视频
    $(".deleteVideo").on("click", function () {
        id = $(this).attr("videoId");
        console.log(id);
        //删除课程时弹出确认框
        let name = $(this).parents("tr").find("td:eq(0)").text();
        console.log(name);
        let message = "确定删除当前视频【" + name + "】吗？";

        $("#videoDeleteModal .modal-body").text(message);
        $("#videoDeleteModal").modal({
                backdrop: "static"
            }
        );
    });
    $("#video_delete_btn").on("click", function () {
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //确认，发送ajax请求删除即可
        $.ajax({
            url: "${APP_PATH}/teacher/deleteCourseVideo",
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
                    window.location.href = "${APP_PATH}/teacher/viewChapterFiles/${id}";
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        })
    });

    //重新上传视频
    $(".editVideo").on("click", function () {
        let id = $(this).attr("videoId");
        let minTime = $("#minTime").val().trim();
        let maxTime = $("#maxTime").val().trim();
        window.location.href = "${APP_PATH}/teacher/editCourseVideo/" + id + "?pageNum=0&chapterId=${id}&pageNumber=${pageNum}&minTime=" + minTime + "&maxTime=" + maxTime + "&pageNum=${pageNum}";
    });
    //在线观看
    $(".viewVideo").on("click", function () {
        let id = $(this).attr("videoId");
        window.location.href = "${APP_PATH}/teacher/viewCourseVideo/" + id + "?pageNum=0";
    })

    //查询按钮
    $("#search").on("click", function () {
        let minTime = $("#minTime").val().trim();
        let maxTime = $("#maxTime").val().trim();
        window.location.href = "${APP_PATH}/teacher/viewChapterFiles/${id}?minTime=" + minTime + "&maxTime=" + maxTime + "&pageNum=${pageNum}";
    });

    function back() {
        window.location.href = "${APP_PATH}/teacher/searchChapter?pageNum=${pageNum}";
    }
</script>