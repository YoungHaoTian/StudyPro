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
</head>

<body>
<div class="row">
    <div class="col-lg-12">
        <ul class="breadcrumb wk-breadcrumb">
            <li><a href="javascript:void(0)">大学生学习平台</a></li>
            <li><a href="javascript:void(0)">个人信息管理</a></li>
            <li><a href="javascript:void(0)">修改个人信息</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">查看学生信息 View Data</div>
            <form id="" action="" method="POST">
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="name" class="control-label wk-filed-label">学生姓名:
                                </label>
                                <div class="input-group">
                                    <input readonly="readonly" id="name" name="name" type="text"
                                           class="form-control wk-normal-input" value="${student.name}"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="number" class="control-label wk-filed-label">学生学号:
                                </label>
                                <div class="input-group">
                                    <input readonly="readonly" id="number" type="text"
                                           class="form-control wk-normal-input" value="${student.number}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="college" class="control-label wk-filed-label">所属学院:
                                </label>
                                <div class="input-group">
                                    <input readonly="readonly" id="college" type="text"
                                           class="form-control wk-normal-input" value="${student.college.name}"/>
                                </div>
                            </div>
                        </div>

                        <div class="form-inline">
                            <div class="form-group">
                                <label for="telephone" class="control-label wk-filed-label">联系电话:
                                </label>
                                <div class="input-group">
                                    <input readonly="readonly" id="telephone" name="telephone"
                                           class="form-control wk-normal-input" value="${student.telephone}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="idCardNo" class="control-label wk-filed-label">身份证号:
                                </label>
                                <div class="input-group">
                                    <input readonly="readonly" id="idCardNo" name="idCardNo" type="text"
                                           class="form-control wk-normal-input" value="${student.idCardNo}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="gender" class="control-label wk-filed-label">学生性别: </label>
                                <div class="input-group">
                                    <input readonly="readonly" id="gender" name="gender" type="text"
                                           class="form-control wk-normal-input" value="${student.gender==0?'男':'女'}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="account" class="control-label wk-filed-label">登录账户: </label>
                                <div class="input-group">
                                    <input readonly="readonly" id="account" name="account" type="text"
                                           class="form-control wk-normal-input" value="${student.account}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="password" class="control-label wk-filed-label">登录密码: </label>
                                <div class="input-group">
                                    <input readonly="readonly" id="password" name="password" type="text" maxlength="18"
                                           class="form-control wk-normal-input" value="${student.password}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="email" class="control-label wk-filed-label">邮箱地址: </label>
                                <div class="input-group">
                                    <input readonly="readonly" id="email" name="email" type="text"
                                           class="form-control wk-normal-input" value="${student.email}"/>
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