<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <%@include file="t-link.jsp" %>
    <title>Error Page</title>
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title text-danger">Error</h5>
                    <p class="card-text">An error occurred with code: <strong>${errorCode}</strong></p>
                    <p class="card-text">Message: <strong>${message}</strong></p>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
