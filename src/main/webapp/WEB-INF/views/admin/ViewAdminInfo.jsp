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
</head>

<body>
<div class="row">
    <div class="col-lg-12">
        <ul class="breadcrumb wk-breadcrumb">
            <li><a href="javascript:void(0)">大学生学习平台</a></li>
            <li><a href="javascript:void(0)">个人信息管理</a></li>
            <li><a href="javascript:void(0)">个人信息查看</a></li>
        </ul>
    </div>
</div>
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">查看管理员信息 View Data</div>
            <form id="adminData" action="" method="POST">
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="name" class="control-label wk-filed-label">管理员姓名:
                                </label>
                                <div class="input-group">
                                    <input readonly="readonly" id="name" name="name" type="text"
                                           class="form-control wk-normal-input" value="${admin.name}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="account" class="control-label wk-filed-label">登录账户: </label>
                                <div class="input-group">
                                    <input rreadonly="readonly" id="account" name="account" type="text"
                                           class="form-control wk-normal-input" value="${admin.account}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="password" class="control-label wk-filed-label">登录密码: </label>
                                <div class="input-group">
                                    <input readonly="readonly" id="password" name="password" type="text"
                                           class="form-control wk-normal-input" value="${admin.password}"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="telephone" class="control-label wk-filed-label">联系电话:</label>
                                <div class="input-group">
                                    <input readonly="readonly" id="telephone" name="telephone" type="text"
                                           oninput="value=value.replace(/[^\d]/g,'')" maxlength="11"
                                           class="form-control wk-normal-input"
                                           value="${admin.telephone.trim()=="0"?"":(admin.telephone.trim()==""?"":admin.telephone) }"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="email" class="control-label wk-filed-label">邮箱: </label>
                                <div class="input-group">
                                    <input readonly="readonly" id="email" name="email" type="text" maxlength="20"
                                           class="form-control wk-normal-input"
                                           value="${admin.email.trim()=="0"?"":(admin.email.trim()==""?"":admin.email) }"/>
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