<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="utf-8">
    <%@include file="t-link.jsp" %>
    <title>${collection.collectionName}</title>
</head>
<body>
<%@include file="t-header.jsp" %>
<main class="container" style="margin-top: 70px">
    <div class="my-3 p-3 bg-body rounded shadow-sm">
        <h6 class="border-bottom pb-2 mb-0">文档:</h6>
        <c:forEach var="document" items="${documents}">
            <div class="d-flex text-body-secondary pt-3">
                <i class="bi bi-file-earmark-text pt-1" style="font-size: 32px; vertical-align: middle; margin-right: 5px;"></i>
                <div class="border border-dark-subtle p-2 mb-2 w-100">
                    <div class="d-flex justify-content-between">
                        <strong class="text-gray-dark"><c:out value="${document.documentName}"/></strong>
                        <fmt:formatDate value="${document.updatedAt}" pattern="yyyy-MM-dd HH:mm" var="formattedDate" />
                        <a class="link-blue"><c:out value="${formattedDate}更新"/></a>
                        <a href="${pageContext.request.contextPath}/rd/${document.documentId}.html">浏览</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</main>
</body>
</html>
