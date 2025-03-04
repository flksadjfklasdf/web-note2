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
    <form action="${pageContext.request.contextPath}/user/login" method="POST">
        <div class="mb-3 text-center">
            <a href="${pageContext.request.contextPath}/index.html"><img src="${pageContext.request.contextPath}/static/img/_5e0c44de-4e2e-4187-8b47-16e5597cd3bc.jpg" class="img-fluid" style="width: 100px; height: 100px; border-radius: 10px; object-fit: cover; overflow: hidden;" alt="Your Image"></a>
        </div>

        <div class="mb-3 row text-center">
            <h1 class="h3 mb-3 fw-normal">请登录</h1>
        </div>

        <div class="mb-3 row w-100 text-center">
            <div class="col-sm-9 w-100">
                <input type="text" class="form-control rounded w-100" name="userName" id="exampleFormControlInput1" placeholder="请输入用户名"/>
            </div>
        </div>

        <div class="mb-3 row w-100">
            <div class="col-sm-9 w-100">
                <input type="password" class="form-control rounded w-100" name="password" id="exampleFormControlTextarea1" placeholder="请输入密码">
            </div>
        </div>

    <c:if test="${sessionScope.get('enableCaptcha') != null && sessionScope.get('enableCaptcha') == true}">
        <div class="mb-3 row w-100 d-flex align-items-center">
            <div class="col-sm-6">
                <input type="text" class="form-control rounded" name="captcha-text" placeholder="请输入验证码">
            </div>
            <div class="col-sm-6">
                <img id="captchaImage" src="${pageContext.request.contextPath}/captcha/generate" alt="Captcha Image" class="img-fluid" style="height: 40px; object-fit: cover;cursor: pointer" onclick="refreshCaptcha()">
            </div>
        </div>
    </c:if>

        <div class="d-flex justify-content-between form-check text-start my-3">
            <div class="form-check">
                <input class="form-check-input" type="checkbox" name="keep-login" value="keep-login" id="flexCheckDefault">
                <label class="form-check-label" for="flexCheckDefault">
                    一直保持登录
                </label>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/signin.html" class="text-decoration-none text-decoration-underline:hover me-5">去注册</a>
            </div>
        </div>

        <button class="btn btn-primary w-100 py-2" type="submit">登录</button>
    </form>

<c:if test="${sessionScope.get('message') != null}">
    <p style="color: red">${sessionScope.get('message')}</p>
</c:if>
    <% session.removeAttribute("message"); %>
</main>
<script>
    function refreshCaptcha(){
        document.getElementById("captchaImage").src = "${pageContext.request.contextPath}/captcha/generate?" + new Date().getTime();
    }
</script>
</body>
</html>
