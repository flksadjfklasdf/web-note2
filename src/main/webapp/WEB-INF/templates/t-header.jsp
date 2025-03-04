<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="fixed-top p-3 text-bg-dark">
    <div class="container-fluid">
        <div class="d-flex align-items-center justify-content-between justify-content-lg-start">
            <ul class="nav me-lg-auto mb-2 justify-content-center mb-0">
                <li><a href="${pageContext.request.contextPath}/index.html" class="nav-link px-2 text-white">首页</a>
                </li>
                <li><a href="${pageContext.request.contextPath}/document.html" class="nav-link px-2 text-white">文档</a>
                </li>
                <li><a href="${pageContext.request.contextPath}/resource.html" class="nav-link px-2 text-white">资源</a>
                </li>
                <li class="d-none d-md-block"><a href="#" class="nav-link px-2 text-white">常见问题</a></li>
                <li class="d-none d-md-block"><a href="#" class="nav-link px-2 text-white">关于我们</a></li>
            </ul>
            <form class="mb-0 me-3 d-none d-md-block" role="search">
                <input type="search" class="search-form form-control form-control-dark text-bg-dark"
                       placeholder="搜索..." aria-label="搜索">
            </form>
            <c:if test="${user != null}">
                <div class="text-end dropdown" id="userButtons">
                    <a href="#" class="btn btn-info me-2 dropdown-toggle" id="userBtn" role="button"
                       data-bs-toggle="dropdown" aria-expanded="false">
                        <c:out value="${user.getUsername()}"/></a>
                    <ul class="dropdown-menu">
                        <c:if test="${user.getUserType() == 0}">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/me.html">个人信息</a>
                            </li>
                            <li><a class="dropdown-item" href="#">个人消息</a></li>
                        </c:if>
                        <c:if test="${user.getUserType() == 1}">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/systemManage.html">系统管理</a></li>
                        </c:if>
                        <c:if test="${user.getUserType() == 2}">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/businessManage.html">业务管理</a></li>
                        </c:if>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/user/logout">退出登录</a>
                        </li>
                    </ul>
                </div>
            </c:if>
            <c:if test="${user == null}">
                <div class="text-end">
                    <a href="${pageContext.request.contextPath}/login.html" type="button" class="btn btn-success me-2">登录</a>
                    <a href="${pageContext.request.contextPath}/signin.html" type="button"
                       class="btn btn-danger">注册</a>
                </div>
            </c:if>
        </div>
    </div>
</header>