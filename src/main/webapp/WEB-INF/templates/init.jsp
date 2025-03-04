<%--
  Created by IntelliJ IDEA.
  User: 25058
  Date: 2023/11/17
  Time: 10:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="t-link.jsp"/>
    <title>系统初始化</title>
</head>
<body class="bg-body-tertiary">
<div class="container">
    <%@include file="t-toast.jsp" %>
    <main style="padding-left: 400px;padding-right: 400px">
        <div class="py-2 text-center">
            <h2>初始化系统</h2>
            <p class="lead">要使系统能够正常使用,你必须初始化以下信息</p>
        </div>

        <div class="py-2 text-center">
            <div class="text-center">
                <h5 class="mb-3">重新设置系统管理员密码,务必牢记账号密码</h5>
                <div class="text-center" id="form">
                    <div class="row g-3 text-center mt-5" style="margin-left: 100px;margin-right: 100px">
                        <div class="col-12">
                            <div class="input-group has-validation text-center">
                                <input type="text" class="form-control" id="username" placeholder="用户名 大于10位"
                                       v-model="formData.username">
                            </div>
                        </div>

                        <div class="col-12 text-center">
                            <input type="password" class="form-control" id="password" placeholder="密码 大于10位"
                                   v-model="formData.password">
                        </div>

                        <div class="col-12 text-center">
                            <input type="password" class="form-control" id="apassword" placeholder="请再次输入密码"
                                   v-model="apassword">
                        </div>

                        <div class="col-12 text-center">
                            <button class="btn btn-primary w-100 py-2" @click="initSystemAdmin">重置</button>
                        </div>
                    </div>
                </div>
                <script>
                    var ov = new Vue({
                        el: '#form',
                        data: {
                            formData: {
                                username: '',
                                password: ''

                            },
                            apassword: ''
                        },
                        methods: {
                            initSystemAdmin() {
                                if (this.formData.username.length < 10) {
                                    windowToastManager.showToast('failed', '用户名长度必须大于10!')
                                }
                                if (this.formData.password.length < 10) {
                                    windowToastManager.showToast('failed', '密码长度必须大于10!')
                                }
                                if (this.formData.password !== this.apassword) {
                                    windowToastManager.showToast('failed', '两次输入密码不一致!')
                                }


                                axios.post('${pageContext.request.contextPath}/user/initSystemAdmin', this.formData)
                                    .then(response => {
                                        if (response.data.status === 0) {
                                            window.location.href = "${pageContext.request.contextPath}/login.html";
                                        } else {
                                            windowToastManager.showToast('failed', '操作失败!')
                                        }
                                    })
                                    .catch(error => {
                                        console.error('Error:', error);
                                        windowToastManager.showToast('failed', '操作失败!')
                                    });
                            },
                        },
                    });
                </script>
            </div>
        </div>
    </main>
</div>
</body>
</html>