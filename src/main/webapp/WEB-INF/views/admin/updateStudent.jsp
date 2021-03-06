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
            <li><a href="javascript:void(0)">学生信息管理</a></li>
            <li><a href="javascript:void(0)">编辑学生信息</a></li>
        </ul>
    </div>
</div>

<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default wk-panel ">
            <div class="panel-heading">编辑学生信息 Update Data</div>
            <form id="studentData" action="" method="POST">
                <div class="panel-body">
                    <div class="row">
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="name" class="control-label wk-filed-label">学生姓名:</label>
                                <div class="input-group">
                                    <input required="required" id="name" name="name" type="text" maxlength="5"
                                           class="form-control wk-normal-input"
                                           value="${student.name.trim()=="0"?"":student.name.trim()}"
                                           placeholder="请输入学生姓名"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="number" class="control-label wk-filed-label">学生学号:</label>
                                <div class="input-group">
                                    <input required="required" id="number" name="number"
                                           oninput="value=value.replace(/[^\d]/g,'')" type="text" maxlength="18"
                                           class="form-control wk-normal-input"
                                           value="${student.number.trim()=="0"?"":student.number.trim()}"
                                           placeholder="请输入学生学号"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="collegeId" class="control-label wk-filed-label">所属学院:</label>
                                <select class="selectpicker" id="collegeId" name="collegeId">
                                    <option value="0">请选择所属学院</option>
                                    <c:forEach items="${colleges}" var="college">
                                        <c:choose>
                                            <c:when test="${college.id == student.collegeId}">
                                                <option value="${college.id}"
                                                        selected="selected">${college.name.trim()}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${college.id}">${college.name.trim()}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-inline">
                            <div class="form-group">
                                <label for="telephone" class="control-label wk-filed-label">联系电话:</label>
                                <div class="input-group">
                                    <input required="required" id="telephone" name="telephone" type="text"
                                           oninput="value=value.replace(/[^\d]/g,'')" maxlength="11"
                                           class="form-control wk-normal-input"
                                           value="${student.telephone.trim()=="0"?"":student.telephone.trim() }"
                                           placeholder="请输入联系电话"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="idCardNo" class="control-label wk-filed-label">身份证号:</label>
                                <div class="input-group">
                                    <input required="required" id="idCardNo" name="idCardNo" maxlength="18" type="text"
                                           class="form-control wk-normal-input"
                                           value="${student.idCardNo.trim()=="0"?"":student.idCardNo.trim() }"
                                           placeholder="请输入身份证号"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="gender" class="control-label wk-filed-label">学生性别: </label>
                                <select class="selectpicker" id="gender" name="gender">
                                    <c:choose>
                                        <c:when test="${student.gender == 0}">
                                            <option value="0" selected="selected">男</option>
                                            <option value="1">女</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="1" selected="selected">女</option>
                                            <option value="0">男</option>
                                        </c:otherwise>
                                    </c:choose>
                                </select>
                            </div>
                        </div>
                        <div class="form-inline">
                            <div class="form-group">
                                <label for="account" class="control-label wk-filed-label">登录账户: </label>
                                <div class="input-group">
                                    <input required="required" id="account" name="account" type="text" maxlength="18"
                                           class="form-control wk-normal-input"
                                           value="${student.account.trim()=="0"?"":student.account.trim() }"
                                           placeholder="请输入登录账户"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="password" class="control-label wk-filed-label">登录密码: </label>
                                <div class="input-group">
                                    <input required="required" id="password" name="password" type="text" maxlength="18"
                                           class="form-control wk-normal-input"
                                           value="${student.account.trim()=="0"?"":student.account.trim() }"
                                           placeholder="请输入登录密码"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="email" class="control-label wk-filed-label">邮箱: </label>
                                <div class="input-group">
                                    <input required="required" id="email" name="email" type="text" maxlength="30"
                                           class="form-control wk-normal-input"
                                           value="${student.email.trim()=="0"?"":student.email.trim() }"
                                           placeholder="请输入邮箱"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <div class="panel-footer wk-panel-footer" style="margin-bottom: 50px">
            <button type="button" class="btn btn-info" onclick="updateStudent()">提&nbsp;&nbsp;交</button>
            <button type="button" class="btn btn-info" onclick="back()" style="margin-left: 30px">
                返&nbsp;&nbsp;回
            </button>
            <button type="button" class="btn btn-info" onclick="$('#studentData')[0].reset();"
                    style="margin-left: 30px">
                重&nbsp;&nbsp;填
            </button>
        </div>
    </div>
</div>

</body>
</html>
<script src="${APP_PATH}/resources/js/layer/layer.js"></script>
<script type="text/javascript">
    //表单验证
    let phones = /^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\d{8}$/;
    let han = /^[\u4e00-\u9fa5]{2,5}$/;
    let accounts = /^[a-zA-Z0-9_-]{12,18}$/;
    let numbers = /^20\d{10,16}$/;
    let idCardNos = /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
    let emails = /^(\w+\.)*\w+\@+[0-9a-zA-Z]+\.(com|com.cn|edu|hk|cn|net)$/;
    let passwords = /^[a-zA-Z0-9]{6,18}$/;

    function updateStudent() {
        //对姓名的验证
        let name = $("#name").val();
        if ($.trim(name) === "") {
            layer.msg("学生姓名不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        } else if (name.indexOf(" ") !== -1) {
            layer.msg("学生姓名不能包含空格，请重新输入", {time: 2000, icon: 5, shift: 6}, function () {
            });
            return;
        } else if (!han.test(name)) {
            layer.msg("请输入正确的中文名字", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }

        //对学号的验证
        let number = $("#number").val();
        if ($.trim(number) === "") {
            layer.msg("学生学号不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        } else if (!numbers.test(number)) {
            layer.msg("学生学号是以20开头的12-18位纯数字组成，请重新输入", {time: 2500, icon: 5, shift: 6}, function () {
            });
            return;
        }

        //对学院的验证
        let collegeId = $("#collegeId").val();
        if (collegeId === "0") {
            layer.msg("请选择所属学院", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }

        //对联系电话的验证
        let telephone = $("#telephone").val();
        if ($.trim(telephone) === "") {
            layer.msg("联系电话不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        } else if (!phones.test(telephone)) {
            layer.msg("联系电话格式不正确，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }

        //对身份证号码的验证
        let idCardNo = $("#idCardNo").val();
        if ($.trim(idCardNo) === "") {
            layer.msg("身份证号码不能为空 ", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        } else if (!idCardNos.test(idCardNo)) {
            layer.msg("身份证号码格式错误，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        let gender = $("#gender").val();
        if (gender !== "0" && gender !== "1") {
            layer.msg("请选择正确的性别", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        //对账号的验证
        let account = $("#account").val();
        if ($.trim(account) === "") {
            layer.msg("登录账户不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        if (!accounts.test(account)) {
            layer.msg("账户是12-18位，且由数字、字母、下划线组成", {time: 2500, icon: 5, shift: 6}, function () {
            });
            return;
        }

        //对登录密码的验证
        let password = $("#password").val();
        if ($.trim(password) === "") {
            layer.msg("登录密码不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        } else if (password.indexOf(" ") !== -1) {
            layer.msg("登录密码不能包含空格，请重新输入", {time: 2000, icon: 5, shift: 6}, function () {
            });
            return;
        } else if (!passwords.test(password)) {
            layer.msg("登录密码只能由字母和数字组成，长度6-18位，请重新输入", {time: 2500, icon: 5, shift: 6}, function () {
            });
            return;
        }

        //对邮箱进行验证
        let email = $("#email").val();
        if ($.trim(email) === "") {
            layer.msg("邮箱不能为空", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        } else if (!emails.test(email)) {
            layer.msg("邮箱格式不正确，请重新输入", {time: 1500, icon: 5, shift: 6}, function () {
            });
            return;
        }
        let loadingIndex = layer.msg('处理中', {icon: 16});
        let data = $("#studentData").serialize();
        data = decodeURIComponent(data);
        // console.log(data);
        $.ajax({
            url: "${APP_PATH}/admin/editStudent/${student.id}",
            type: "POST",
            // contentType: "application/json",//不使用contentType: “application/json”则data可以是对象,使用contentType: “application/json”则data只能是json字符串
            dataType: "json",
            data: data,
            success: function (result) {
                layer.close(loadingIndex);
                console.log(result);
                if (result.code === 200) {
                    layer.msg(result.message, {time: 3000, icon: 5, shift: 6}, function () {
                    });
                }
                if (result.code === 100) {
                    console.log("success");
                    layer.msg("修改成功", {time: 1500, icon: 6}, function () {
                    });
                }
            },
            error: function () {
                layer.msg("网络异常，请稍后再试", {time: 1500, icon: 5, shift: 6}, function () {
                });
            }
        });
    }

    function back() {
        window.location.href = "${APP_PATH}/admin/searchStudent?pageNum=" +${pageNum};
        // window.history.back();
    }
</script>