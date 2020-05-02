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
    <link rel="stylesheet" href="${APP_PATH}/resources1/ztree/zTreeStyle.css">
    <script type="text/javascript" src="${APP_PATH}/resources1/js/jquery-3.1.1.js"></script>
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${APP_PATH}/resources1/css/wukong-ui.css">
    <link rel="stylesheet" href="${APP_PATH}/resources1/bootstrap/css/bootstrap-select.min.css">
    <script type="text/javascript" src="${APP_PATH}/resources1/bootstrap/js/bootstrap-select.min.js"></script>
    <style>
        .tree li {
            list-style-type: none;
            cursor: pointer;
        }
    </style>
</head>

<body>
<div class="row">
    <div class="col-lg-12">
        <ul class="breadcrumb wk-breadcrumb">
            <li><a href="javascript:void(0)">大学生学习平台</a></li>
            <li><a href="javascript:void(0)">个人信息管理</a></li>
            <li><a href="javascript:void(0)">查看个人信息</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">查看个人信息 View Data</div>
            <form>
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group col-md-10">
                                <div class="container-fluid col-md-10">
                                    <div class="sidebar">
                                        <div class="tree">
                                            <ul style="padding-left:0px;" class="list-group">
                                                <li class="list-group-item tree-closed">
                                                    <a href="javascript:void(0)"><span
                                                            class="glyphicon glyphicon-tasks"></span>&nbsp;所授课程及章节</a>
                                                </li>
                                                <c:forEach items="${courses}" var="course" varStatus="statu">
                                                <c:if test="${statu.first}">
                                                <li class="list-group-item">
                                                    </c:if>
                                                    <c:if test="${!statu.first}">
                                                <li class="list-group-item tree-closed">
                                                    </c:if>
                                                    <span class="glyphicon glyphicon-tasks"></span>&nbsp;${course.name}(${course.college.name})
                                                    <span class="badge"
                                                          style="float:right">${course.chapters.size()}</span>
                                                    <c:if test="${!empty course.chapters}">
                                                    <c:if test="${statu.first}">
                                                    <ul style="margin-top:10px;">
                                                        </c:if>
                                                        <c:if test="${!statu.first}">
                                                        <ul style="margin-top:10px;display:none;">
                                                            </c:if>
                                                            <c:forEach items="${course.chapters}" var="chapter">
                                                                <li style="height:30px;">
                                                                    <a href="javascript:void(0)" class="chapter"
                                                                       text="${course.name}:${course.college.name}:${chapter.title}"
                                                                       chapterId="${chapter.id}"
                                                                       courseId="${course.id}"><span
                                                                            class="glyphicon glyphicon-tags"></span>&nbsp;${chapter.title}
                                                                    </a>
                                                                </li>
                                                            </c:forEach>
                                                        </ul>
                                                        </c:if>
                                                        </li>
                                                        </c:forEach>
                                                    </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
            <form>
                <div class="panel-body">
                    <div class="row" style="text-align: left">
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="name" class="control-label wk-filed-label">教师姓名:</label>
                                <div class="input-group">
                                    <input required="required" id="name" name="name" type="text"
                                           class="form-control wk-normal-input" value="${teacher.name}"
                                           readonly="readonly"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="number" class="control-label wk-filed-label">教师编号:</label>
                                <div class="input-group">
                                    <input id="number" name="number" type="text" readonly="readonly"
                                           class="form-control wk-normal-input" value="${teacher.number}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="college" class="control-label wk-filed-label">所属学院:</label>
                                <div class="input-group">
                                    <input id="college" name="college" type="text" readonly="readonly"
                                           class="form-control wk-normal-input" value="${teacher.college.name}"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-inline">
                            <div class="form-group">
                                <label for="telephone" class="control-label wk-filed-label"> 联系电话:</label>
                                <div class="input-group">
                                    <input required="required" id="telephone" name="telephone" type="text"
                                           class="form-control wk-normal-input" value="${teacher.telephone}"
                                           readonly="readonly"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="gender" class="control-label wk-filed-label">教师性别: </label>
                                <div class="input-group">
                                    <input required="required" id="gender" name="gender" type="text"
                                           class="form-control wk-normal-input" value="${teacher.gender==0?'男':'女'}"
                                           readonly="readonly"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="idCardNo" class="control-label wk-filed-label">身份证号:</label>
                                <div class="input-group">
                                    <input required="required" id="idCardNo" name="idCardNo" type="text"
                                           class="form-control wk-normal-input" value="${teacher.idCardNo}"
                                           readonly="readonly"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="account" class="control-label wk-filed-label">登录账户: </label>
                                <div class="input-group">
                                    <input required="required" id="account" name="account" type="text"
                                           class="form-control wk-normal-input" value="${teacher.account}"
                                           readonly="readonly"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="password" class="control-label wk-filed-label">登录密码: </label>
                                <div class="input-group">
                                    <input required="required" id="password" name="password" type="text"
                                           class="form-control wk-normal-input" value="${teacher.password}"
                                           readonly="readonly"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="email" class="control-label wk-filed-label">邮箱地址: </label>
                                <div class="input-group">
                                    <input required="required" id="email" name="email" type="text"
                                           class="form-control wk-normal-input" value="${teacher.email}"
                                           readonly="readonly"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>

        </div>
    </div>
</div>
</body>
</html>
<script src="${APP_PATH}/resources1/ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
    $(function () {
        $(".list-group-item").click(function () {
            if ($(this).find("ul")) {
                $(this).toggleClass("tree-closed");
                if ($(this).hasClass("tree-closed")) {
                    $("ul", this).hide("fast");
                } else {
                    $("ul", this).show("fast");
                }
            }
        });
    });
</script>