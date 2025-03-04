<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%@include file="t-link.jsp" %>
    <style>
        body {
            height: 100vh;
        }

        .container {
            text-align: center;
        }

        .card {
            width: 100%;
            max-width: 400px;
            margin: 0 auto;
        }

    </style>
</head>
<body class="d-flex align-items-center">
<%@include file="t-toast.jsp" %>
<div class="container mt-5">
    <div class="row">
        <div class="col-md-6 offset-md-3 offset-sm-2">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">请输入授权码</h5>
                    <div class="form-group" style="margin-top: 20px;margin-bottom: 20px">
                        <input type="text" class="form-control" id="authCodeInput">
                    </div>
                    <button class="btn btn-primary" onclick="redirectToDownload()">下载</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function showMessageAlert(message) {
        if (message && message.trim() !== "") {

            var decodedMessage = base64Decode(message);
            windowToastManager.showToast('failed', decodedMessage);
        }
    }

    document.addEventListener("DOMContentLoaded", function () {
        var encodedMessage = "${message}";
        showMessageAlert(encodedMessage);
    });

    function redirectToDownload() {
        var authCode = document.getElementById("authCodeInput").value;
        var encodedFileId = "${fileId}";

        if (encodedFileId) {

            var fileId = base64Decode(encodedFileId);

            window.location.href = "d/" + fileId + "?authCode=" + encodeURIComponent(authCode);
        } else {
            console.error("FileId is missing or invalid.");
        }
    }

    function base64Decode(encodedString) {
        try {
            const decodedString = atob(encodedString);

            const byteArray = new Uint8Array(decodedString.length);
            for (let i = 0; i < decodedString.length; i++) {
                byteArray[i] = decodedString.charCodeAt(i);
            }

            return new TextDecoder('utf-8').decode(byteArray);
        } catch (error) {
            console.error('解码失败：', error);
            return null;
        }
    }
</script>


</body>
</html>
