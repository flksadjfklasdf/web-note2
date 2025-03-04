<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/md-edit/simplemde.min.css" rel="stylesheet">
<c:choose><c:when test="${not empty sessionScope.codeStyle}"><link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/md-show/prism-${sessionScope.codeStyle}.css" rel="stylesheet"  id="codeStyleCss"></c:when><c:otherwise><link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/md-show/prism-okaidia.css" rel="stylesheet"  id="codeStyleCss"></c:otherwise></c:choose>
<link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/md-show/md-style.css" rel="stylesheet">
<link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/md-show/prism-line-numbers.min.css" rel="stylesheet">
<link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/css/normal.css" rel="stylesheet">
<link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/bootstrap/font/bootstrap-icons.css" rel="stylesheet">
<link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/css/theme/theme-basic.css" rel="stylesheet">
<script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
<script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/md-edit/simplemde.min.js"></script>
<script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/md-show/showdown.min.js"></script>
<script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/md-show/prism.js"></script>
<script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/md-show/markdown-show.js"></script>
<script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/md-show/clipboard.min.js"></script>
<script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/jquery.min.js"></script>
<script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/vue/vue.js"></script>
<script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/vue/axios.min.js"></script>
<script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/chart.js"></script>
<script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/echarts.min.js"></script>