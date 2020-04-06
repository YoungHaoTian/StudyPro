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
    <script type="text/javascript" src="${APP_PATH}/resources1/js/jquery-3.1.1.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${APP_PATH}/resources1/css/wukong-ui.css">
    <link rel="stylesheet" href="${APP_PATH}/resources1/bootstrap/css/bootstrap-select.min.css">
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap-select.min.js"></script>
    <style>
        th, td {
            text-align: center;
        }
    </style>
</head>

<body>
<div class="row">
    <div class="col-lg-12">
        <ul class="breadcrumb wk-breadcrumb">
            <li><a href="#">大学生学习平台</a></li>
            <li><a href="#">教师信息管理</a></li>
            <li><a href="#">教师信息查询</a></li>
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
                        <label for="number" class="control-label wk-filed-label" style="margin-top: 20px">教师编号:</label>
                        <input type="text" oninput="value=value.replace(/[^\d]/g,'')" maxlength="18"
                               class="form-control" id="number" name="number" placeholder="教师编号"
                               value="${sessionScope.teacherQueryCriteria.get("number")}">
                    </div>
                    <div class="form-group">
                        <label for="collegeId" class="control-label wk-filed-label"
                               style="margin-top: 20px">所属学院:</label>
                        <select class="selectpicker" id="collegeId" name="collegeId">
                            <option value="0">请选择所属学院</option>
                            <c:forEach items="${colleges}" var="college">
                                <c:choose>
                                    <c:when test="${college.id == sessionScope.teacherQueryCriteria.get('collegeId')}">
                                        <option value="${college.id}" selected="selected">${college.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${college.id}">${college.name}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="courseId" class="control-label wk-filed-label"
                               style="margin-top: 20px">教授课程:</label>
                        <select class="selectpicker" id="courseId" name="courseId">
                            <option value="0">请选择教授课程</option>
                            <c:forEach items="${courses}" var="course">
                                <%--<option value="${course.id}">${course.name}</option>--%>
                                <c:choose>
                                    <c:when test="${course.id == sessionScope.teacherQueryCriteria.get('courseId')}">
                                        <option value="${course.id}" selected="selected">${course.name}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${course.id}">${course.name}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <button type="button" id="search" class="btn btn-success search" data-toggle="tooltip"
                                data-placement="left" title="查询学生" style="margin-right: 20px">
                            <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                            查询
                        </button>
                    </div>
                    <div class="form-group">
                        <button type="button" class="btn btn-danger batchDelete" data-toggle="tooltip"
                                data-placement="left"
                                title="批量删除学生">
                            <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                            批量删除
                        </button>
                    </div>
                </div>
            </div>
            <!-- 搜索 end -->
        </div>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default  wk-panel">
            <table class="table table-striped table-hover">
                <thead>
                <tr class="info">
                    <th>
                        <input type="checkbox" id="select_all"/>
                        <label for="select_all" style="margin-bottom: 0px;font-weight: 200">全选</label>
                    </th>
                    <th>教师姓名</th>
                    <th>教师编号</th>
                    <th>所属学院</th>
                    <th>教授课程</th>
                    <th>联系电话</th>
                    <th>身份证号</th>
                    <th>性别</th>
                    <th>登录账户</th>
                    <th>邮箱</th>
                    <th>选择操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${pageInfo.list}" var="teacher" varStatus="statu">
                    <%--<c:if test="${statu.index%2==0}">
                        <tr class="success">
                    </c:if>
                    <c:if test="${statu.index%2!=0}">
                        <tr class="warning">
                    </c:if>--%>
                    <th>
                        <label>
                            <input teaId="${teacher.id}" type="checkbox" class="select_item"/>
                        </label>
                    </th>
                    <c:if test="${teacher.name!=null}">
                        <td>${teacher.name.trim()=="0"?"未录入":(teacher.name.trim()==""?"未录入":teacher.name) }</td>
                    </c:if>
                    <c:if test="${teacher.name==null}">
                        <td>未录入</td>
                    </c:if>
                    <c:if test="${teacher.number!=null}">
                        <td>${teacher.number.trim()=="0"?"未录入":(teacher.number.trim()==""?"未录入":teacher.number) }</td>
                    </c:if>
                    <c:if test="${teacher.number==null}">
                        <td>未录入</td>
                    </c:if>
                    <c:if test="${teacher.collegeId!=null}">
                        <td>${teacher.collegeId==0?"未录入":teacher.college.name }</td>
                    </c:if>
                    <c:if test="${teacher.collegeId==null}">
                        <td>未录入</td>
                    </c:if>
                    <c:if test="${teacher.courseId!=null}">
                        <td>${teacher.courseId==0?"未录入":teacher.course.name }</td>
                    </c:if>
                    <c:if test="${teacher.courseId==null}">
                        <td>未录入</td>
                    </c:if>
                    <c:if test="${teacher.telephone!=null}">
                        <td>${teacher.telephone.trim()=="0"?"未录入":(teacher.telephone.trim()==""?"未录入":teacher.telephone) }</td>
                    </c:if>
                    <c:if test="${teacher.telephone==null}">
                        <td>未录入</td>
                    </c:if>
                    <c:if test="${teacher.idCardNo!=null}">
                        <td>${teacher.idCardNo.trim()=="0"?"未录入":(teacher.idCardNo.trim()==""?"未录入":teacher.idCardNo) }</td>
                    </c:if>
                    <c:if test="${teacher.idCardNo==null}">
                        <td>未录入</td>
                    </c:if>
                    <c:if test="${teacher.gender!=null}">
                        <td>${teacher.gender==0?"男":"女" }</td>
                    </c:if>
                    <c:if test="${teacher.gender==null}">
                        <td>未录入</td>
                    </c:if>
                    <c:if test="${teacher.account!=null}">
                        <td>${teacher.account.trim()=="0"?"未录入":(teacher.account.trim()==""?"未录入":teacher.account) }</td>
                    </c:if>
                    <c:if test="${teacher.account==null}">
                        <td>未录入</td>
                    </c:if>
                    <c:if test="${teacher.email!=null}">
                        <td>${teacher.email.trim()=="0"?"未录入":(teacher.email.trim()==""?"未录入":teacher.email) }</td>
                    </c:if>
                    <c:if test="${teacher.email==null}">
                        <td>未录入</td>
                    </c:if>
                    <td>
                        <button class="btn btn-primary btn-sm edit" type="button"
                                data-toggle="tooltip" data-placement="left"
                                title="编辑当前教师" edit-id="${teacher.id}" style="margin-right: 20px">
                            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                            编辑
                        </button>
                        <a class="btn btn-danger btn-sm delete" type="button" data-toggle="tooltip"
                           data-placement="left"
                           title="删除当前教师" del-id="${teacher.id}">
                            <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                            删除
                        </a>
                    </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
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
                    <li><a href="${APP_PATH}/admin/searchTeacher?pageNum=1">首页</a></li>
                    <li><a href="${APP_PATH}/admin/searchTeacher?pageNum=${pageInfo.pageNum-1}"
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
                        <li><a href="${APP_PATH }/admin/searchTeacher?pageNum=${page_Num }">${page_Num }</a></li>
                    </c:if>
                </c:forEach>

                <c:if test="${pageInfo.hasNextPage }">
                    <li><a href="${APP_PATH }/admin/searchTeacher?pageNum=${pageInfo.pageNum+1 }"
                           aria-label="Next"> <span aria-hidden="true">&raquo;</span>
                    </a></li>
                    <li><a href="${APP_PATH }/admin/searchTeacher?pageNum=${pageInfo.pages}">末页</a></li>
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
    <button type="button" class="btn btn-success" onclick="toPage()" style="margin-left: 20px">确定跳转</button>
</div>

<div class="modal fade" id="teacherDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title">老师删除</h5>
            </div>
            <div class="modal-body">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="teacher_delete_btn">确定</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="teacherBatchDeleteModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title">老师批量删除</h5>
            </div>
            <div class="modal-body">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="teacher_batchDelete_btn">确定</button>
            </div>
        </div>
    </div>
</div>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    let ids = "";
    let id = "";

    function toPage() {
        //获取到将要跳转的页面值
        let pageNum = $("#pageNum").val();
        let total =${pageInfo.pages };
        if (pageNum.trim() === "" || total < pageNum || pageNum < 0) {
            layer.msg("错误的跳转页码，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        window.location.href = "${APP_PATH}/admin/searchTeacher?pageNum=" + pageNum;
    }

    //编辑教师信息
    $(".edit").on("click", function () {
        let id = $(this).attr("edit-id");
        window.location.href = "${APP_PATH}/admin/updateTeacher/" + id + "?pageNum=${pageInfo.pageNum}";
    });

    //删除单个教师
    $(".delete").on("click", function () {
        id = $(this).attr("del-id");
        console.log(id);
        //删除教师时弹出确认框
        let name = $(this).parents("tr").find("td:eq(0)").text();
        let message = "确定删除老师 【" + name + "】 的信息吗？";
        if (name === "未录入") {
            message = "确定删除该老师的信息吗？";
        }
        $("#teacherDeleteModal .modal-body").text(message);
        $("#teacherDeleteModal").modal({
                backdrop: "static"
            }
        );
    });
    //确认删除教师
    $("#teacher_delete_btn").on("click", function () {
        let loadingIndex = layer.msg('处理中', {icon: 16});
        console.log(id);
        //确认，发送ajax请求删除即可
        $.ajax({
            url: "${APP_PATH}/admin/deleteTeacher",
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
                    window.location.href = "${APP_PATH}/admin/searchTeacher?pageNum=${pageInfo.pageNum }";
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        })
    });


    //批量删除教师
    //全选按钮
    $("#select_all").on("click", (function () {
        //attr获取checked是undefined;
        //我们这些dom原生的属性；attr获取自定义属性的值；
        //prop修改和读取dom原生属性的值
        $(".select_item").prop("checked", $(this).prop("checked"));
        let checked = $(this).prop("checked");
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
        $.each($(".select_item:checked"), function () {
            //组装教师id字符串
            ids += $(this).attr("teaId") + "-";
        });
        console.log(ids);
        if (ids.trim() === "") {
            layer.msg("操作失败，你未选择任何教师", {time: 1500, icon: 5, shift: 6}, function () {
            });
        } else {
            //去除删除的id多余的"-"
            ids = ids.substring(0, ids.length - 1);
            console.log(ids);

            $("#teacherBatchDeleteModal .modal-body").text("你确定要删除这些老师信息吗？");
            $("#teacherBatchDeleteModal").modal({
                backdrop: "static"
            });
        }
    });
    //确认批量删除按钮
    $("#teacher_batchDelete_btn").on("click", function () {
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //发送ajax请求删除
        $.ajax({
            url: "${APP_PATH}/admin/deleteTeacherBatch",
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
                    window.location.href = "${APP_PATH}/admin/searchTeacher?pageNum=${pageInfo.pageNum }";
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
        let number = $("#number").val().trim()
        let collegeId = $("#collegeId").val();
        let courseId = $("#courseId").val();
        let loadingIndex = layer.msg('处理中', {icon: 16});
        //发送ajax请求
        $.ajax({
            url: "${APP_PATH}/admin/searchTeacherByTerm",
            type: "POST",
            dataType: "json",
            data: {
                "number": number,
                "collegeId": collegeId,
                "courseId": courseId
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
                    window.location.href = "${APP_PATH}/admin/searchTeacher";
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    });

    //编辑教师信息
    $(".edit").on("click", function () {
        let id = $(this).attr("edit-id");
        window.location.href = "${APP_PATH}/admin/updateTeacher/" + id + "?pageNum=${pageInfo.pageNum}";
    })
</script>
</body>
</html>