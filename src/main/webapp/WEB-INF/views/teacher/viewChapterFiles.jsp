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
                                data-placement="left" title="查询章节课件">
                            <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                            查询
                        </button>
                    </div>
                    <div class="form-group" style="margin-left: 20px">
                        <button type="button" id="createVideo" class="btn btn-primary" data-toggle="tooltip"
                                data-placement="left" title="为该章节添加视频">
                            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                            添加视频
                        </button>
                    </div>
                    <div class="form-group" style="margin-left: 20px">
                        <button type="button" id="createFile" class="btn btn-primary" data-toggle="tooltip"
                                data-placement="left" title="为该章节添加课件">
                            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                            添加课件
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
                    <th style="width: 80px">序号</th>
                    <th>文件名</th>
                    <th style="width: 200px">上传时间</th>
                    <th style="width: 80px">文件类型</th>
                    <th style="width: 500px">选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:set var="index" value="1"/>
                <c:forEach items="${files}" var="file">
                    <tr>
                        <td>${index}</td>
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
                                <span class="glyphicon glyphicon-cloud-upload" aria-hidden="true"></span>
                                重新上传
                            </button>
                            <button class="btn btn-danger btn-sm deleteFile" data-toggle="tooltip" data-placement="left"
                                    title="删除当前文档" fileId="${file.id}">
                                <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                                删除
                            </button>
                        </td>
                    </tr>
                    <c:set var="index" value="${index+1}"/>
                </c:forEach>
                <c:forEach items="${videos}" var="video">
                    <tr>
                        <td>${index}</td>
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
                                <span class="glyphicon glyphicon-cloud-upload" aria-hidden="true"></span>
                                重新上传
                            </button>
                            <button class="btn btn-danger btn-sm deleteVideo" data-toggle="tooltip"
                                    data-placement="left"
                                    title="删除当前视频" videoId="${video.id}">
                                <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                                删除
                            </button>
                        </td>
                    </tr>
                    <c:set var="index" value="${index+1}"/>
                </c:forEach>
                </tbody>
            </table>

        </div>
        <div class="panel-footer wk-panel-footer">
            <button type="button" class="btn btn-info" onclick="back()">返&nbsp;&nbsp;回</button>
        </div>
    </div>
</div>
<div class="modal fade" id="fileDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">文档删除</h5>
            </div>
            <div class="modal-body" style="text-align: center">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="file_delete_btn">确定</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="videoDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">视频删除</h5>
            </div>
            <div class="modal-body" style="text-align: center">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="video_delete_btn">确定</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="createCourseVideoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: blue">章节视频添加</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="video" class="col-sm-2 control-label">选择文件:</label>
                        <div class="col-sm-9">
                            <input type="file" id="video" name="video" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="createCourseVideoBtn">添加</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="createCourseFileModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: blue">章节课件添加</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="file" class="col-sm-2 control-label">选择文件:</label>
                        <div class="col-sm-9">
                            <input type="file" id="file" name="file" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="createCourseFileBtn">添加</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="fileUpdateModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">章节课件重新上传</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="file1" class="col-sm-2 control-label">选择文件:</label>
                        <div class="col-sm-9">
                            <input type="file" id="file1" name="file1" fileId="0" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="file_update_btn">确定</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="videoUpdateModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title" style="color: red">章节视频重新上传</h5>
            </div>
            <div class="modal-body" style="text-align: center">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="video1" class="col-sm-2 control-label">选择文件:</label>
                        <div class="col-sm-9">
                            <input type="file" id="video1" name="video1" videoId="0" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="video_update_btn">确定</button>
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
        let name = $(this).parents("tr").find("td:eq(1)").text();
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
                    layer.msg("文档删除成功", {time: 1000, icon: 1}, function () {
                    });
                    window.setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                    <%--window.location.href = "${APP_PATH}/teacher/viewChapterFiles/${id}?pageNum=${pageNum}&courseId=${courseId}";--%>
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
        $("#file1").attr("fileId", id);
        $("#fileUpdateModal").modal({
            backdrop: "static"
        });
    });
    $("#file_update_btn").on("click", function () {
        let file = $("#file1").val();
        let fileId = $("#file1").attr("fileId");
        if (file === "") {
            layer.msg("请选择需要上传的文件", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //上传文件
        let formData = new FormData();
        let loadingIndex = layer.msg('处理中', {icon: 16});
        formData.append("file", $("#file1")[0].files[0]);
        formData.append("courseFileId", fileId);
        $.ajax({
            url: "${APP_PATH}/teacher/updateCourseFile",
            type: 'POST',
            dataType: 'json',
            crossDomain: true, // 如果用到跨域，需要后台开启CORS
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("文档重新上传成功", {time: 1000, icon: 1}, function () {
                    });
                    window.setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
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
        let name = $(this).parents("tr").find("td:eq(1)").text();
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
                    layer.msg("视频删除成功", {time: 1000, icon: 1}, function () {
                    });
                    window.setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                    <%--window.location.href = "${APP_PATH}/teacher/viewChapterFiles/${id}?pageNum=${pageNum}&courseId=${courseId}";--%>
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
        $("#video1").attr("videoId", id);
        $("#videoUpdateModal").modal({
            backdrop: "static"
        });
    });
    $("#video_update_btn").on("click", function () {
        let file = $("#video1").val();
        let videoId = $("#video1").attr("videoId");
        if (file === "") {
            layer.msg("请选择需要上传的文件", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //判断上传文件的后缀名
        let strExtension = file.substr(file.lastIndexOf('.') + 1);
        if (strExtension !== "mp4" && strExtension !== "avi") {
            layer.msg("请上传mp4或avi格式的视频文件", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //上传文件
        let formData = new FormData();
        let loadingIndex = layer.msg('处理中', {icon: 16});
        formData.append("file", $("#video1")[0].files[0]);
        formData.append("courseVideoId", videoId);
        $.ajax({
            url: "${APP_PATH}/teacher/updateCourseVideo",
            type: 'POST',
            dataType: 'json',
            crossDomain: true, // 如果用到跨域，需要后台开启CORS
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("视频重新上传成功", {time: 1000, icon: 1}, function () {
                    });
                    window.setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });
    //在线观看
    $(".viewVideo").on("click", function () {
        let id = $(this).attr("videoId");
        window.location.href = "${APP_PATH}/teacher/viewCourseVideo/" + id + "?pageNum=0";
    });
    //查询按钮
    $("#search").on("click", function () {
        let minTime = $("#minTime").val().trim();
        let maxTime = $("#maxTime").val().trim();
        window.location.href = "${APP_PATH}/teacher/viewChapterFiles/${id}?courseId=${courseId}&minTime=" + minTime + "&maxTime=" + maxTime + "&pageNum=${pageNum}";
    });

    function back() {
        window.location.href = "${APP_PATH}/teacher/searchChapter?courseId=${courseId}&pageNum=${pageNum}";
    }
    //上传视频
    $("#createVideo").on("click", function () {
        $("#createCourseVideoModal").modal({
                backdrop: "static"
            }
        );
    });
    $("#createCourseVideoBtn").on("click", function () {
        let chapterId =${id};
        let courseId =${courseId};
        let file = $("#video").val();
        if (file === "") {
            layer.msg("请选择需要上传的文件", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //判断上传文件的后缀名
        let strExtension = file.substr(file.lastIndexOf('.') + 1);
        if (strExtension !== "mp4" && strExtension !== "avi") {
            layer.msg("请上传mp4或avi格式的视频文件", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //上传文件
        let formData = new FormData();
        let loadingIndex = layer.msg('处理中', {icon: 16});
        formData.append("file", $("#video")[0].files[0]);
        formData.append("chapterId", chapterId);
        formData.append("courseId", courseId);
        $.ajax({
            url: "${APP_PATH}/teacher/saveCourseVideo",
            type: 'POST',
            dataType: 'json',
            crossDomain: true, // 如果用到跨域，需要后台开启CORS
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("视频上传成功", {time: 1000, icon: 1}, function () {
                    });
                    window.setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                    // window.location.reload(true);
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });
    //上传文档
    $("#createFile").on("click", function () {
        $("#createCourseFileModal").modal({
                backdrop: "static"
            }
        );
    });
    $("#createCourseFileBtn").on("click", function () {
        let chapterId =${id};
        let courseId =${courseId};
        let file = $("#file").val();
        if (file === "") {
            layer.msg("请选择需要上传的文件", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //上传文件
        let formData = new FormData();
        let loadingIndex = layer.msg('处理中', {icon: 16});
        formData.append("file", $("#file")[0].files[0]);
        formData.append("chapterId", chapterId);
        formData.append("courseId", courseId);
        $.ajax({
            url: "${APP_PATH}/teacher/saveCourseFile",
            type: 'POST',
            dataType: 'json',
            crossDomain: true, // 如果用到跨域，需要后台开启CORS
            data: formData,
            processData: false,
            contentType: false,
            success: function (result) {
                layer.close(loadingIndex);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 1500, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    layer.msg("文档上传成功", {time: 1000, icon: 1}, function () {
                    });
                    window.setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                    // window.location.reload(true);
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });
</script>