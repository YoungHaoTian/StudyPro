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
        th, td {
            /*vertical-align: middle;*/
            text-align: center;
            height: 30px;
            width: 50%;
            vertical-align: middle;
            border: #CCCCCC 1px solid;
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
            <div class="panel-body">
                <div class="row">
                    <table class="table table-striped table-hover table-bordered"
                           style="width: 500px;margin: 0 auto">
                        <thead>
                        <tr class="danger">
                            <th colspan=2>查看个人信息</th>
                        </tr>
                        <tr class="info">
                            <th colspan=2>教师：<span style="color: red">${teacher.name}</span>的信息如下</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td style="text-align: right">教师姓名：</td>
                            <td style="text-align: left">${teacher.name}</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">教师编号：</td>
                            <td style="text-align: left">${teacher.number}</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">所属学院：</td>
                            <td style="text-align: left">${teacher.college.name}</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">联系电话：</td>
                            <td style="text-align: left">${teacher.telephone}</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">身份证号码：</td>
                            <td style="text-align: left">${teacher.idCardNo}</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">性别：</td>
                            <td style="text-align: left">${teacher.gender=="0"?"男":"女"}</td>
                        </tr>

                        <tr>
                            <td style="text-align: right">登录账户：</td>
                            <td style="text-align: left">${teacher.account}</td>
                        </tr>
                        <tr>
                            <td style="text-align: right">登录密码：</td>
                            <td style="text-align: left">${teacher.password}</td>
                        </tr>

                        <tr>
                            <td style="text-align: right">邮箱地址：</td>
                            <td style="text-align: left">${teacher.email}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="panel-footer wk-panel-footer" style="margin-bottom: 50px">
            <button type="button" class="btn btn-info"
                    onclick="window.location.href='${APP_PATH}/teacher/editTeacherInfo'">
                修改个人信息
            </button>
        </div>
    </div>
</div>
</body>
</html>

