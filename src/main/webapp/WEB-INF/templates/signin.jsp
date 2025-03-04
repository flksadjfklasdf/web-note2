<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en" data-bs-theme="auto">
<head>
    <meta charset="utf-8">
    <%@include file="t-link.jsp" %>
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/css/sign-in.css" rel="stylesheet">
    <title>登录</title>
</head>
<body class="d-flex align-items-center py-4 bg-body-tertiary">

<main class="form-signin w-100 m-auto">
    <div id="signin-data">
        <div class="mb-3 text-center">
            <a href="${pageContext.request.contextPath}/index.html"><img src="${pageContext.request.contextPath}/static/img/_5e0c44de-4e2e-4187-8b47-16e5597cd3bc.jpg" class="img-fluid" style="width: 100px; height: 100px; border-radius: 10px; object-fit: cover; overflow: hidden;" alt="Your Image"></a>
        </div>

        <div class="mb-3 row text-center">
            <h1 class="h3 mb-3 fw-normal">注册</h1>
        </div>
        <div class="mb-3 row w-100">
            <div class="col-sm-9 w-100">
                <input type="text" class="form-control rounded" id="exampleFormControlInput1" v-model="formData.username" placeholder="请输入用户名"/>
            </div>
        </div>
        <div class="mb-3 row w-100">
            <div class="col-sm-9 w-100">
                <input type="password" class="form-control rounded" id="exampleFormControlTextarea1" v-model="formData.password" placeholder="请输入密码">
            </div>
        </div>
        <div class="mb-3 row w-100">
            <div class="col-sm-9 w-100">
                <input type="password" class="form-control rounded" id="exampleFormControlTextarea2" v-model="apassword"  placeholder="请再次输入密码">
            </div>
        </div>

        <div class="d-flex justify-content-between form-check text-start my-3">
            <div class="form-check">
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/login.html" class="text-decoration-none text-decoration-underline:hover me-5">返回登录</a>
            </div>
        </div>
        <button class="btn btn-primary w-100 py-2" @click="submit">注册</button>
        <span style="color:red;">{{ message }}</span>
    </div>
    <script>
        var sx=new Vue({
            el:"#signin-data",
            data:{
                formData:{
                    username:"",
                    password:""
                },
                apassword:"",
                message:""
            },
            methods:{
                submit(){
                    axios.post('${pageContext.request.contextPath}/user/signin', this.formData)
                        .then((response) => {
                            console.log(response)
                            if (response.data.status === 0) {
                                window.location.href="${pageContext.request.contextPath}/login.html"
                            } else {
                                this.message=response.data.message
                            }
                        })
                        .catch(error => {
                            this.message=error.message
                        });
                }
            }
        })
    </script>
</main>

</body>
</html>
