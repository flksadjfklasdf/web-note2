<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <%@include file="t-link.jsp" %>
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/css/edit-md.css" rel="stylesheet">
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/css/view.css" rel="stylesheet">
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/quill/quill.snow.css" rel="stylesheet">
    <script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/view.js"></script>
    <script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/security/purify.min.js"></script>

    <title><c:out value="${documentName}"/></title>
</head>
<body style="background-color: #F6F6F6">
<%@include file="t-header.jsp" %>
<%@include file="t-toast.jsp" %>
<%@include file="t-image.jsp" %>
<div id="document-content" style="background-color: #FFFFFF;border: 2px solid #BBBBBB;" class="album py-5 w-100 container">
    <div class="btn-group">
        <button type="button" class="btn btn-outline-secondary" title="收藏" onclick="collect()">
            <i id="collect-ico" class="bi bi-star"></i>
            <span class="visually-hidden">Button</span>
        </button>
        <button type="button" class="btn btn-outline-secondary">
            <i v-if="document.documentType === 'markdown'" class="bi bi-filetype-md" title="导出为markdown文件"  @click="downloadMarkdownFile()"></i>
            <i v-if="document.documentType === 'html'" class="bi bi-filetype-html" title="导出为html文件" @click="downloadHtmlFile()"></i>
            <span class="visually-hidden">Button</span>
        </button>
        <div style="margin-left: 100px">这篇文章来自于<a class="badge btn bg-info"><c:out value="${editorName}"/></a> <a
                class="badge btn bg-success" id="din"><c:out value="${documentName}"/></a></div>
    </div>
    <br/>
    <br/>
    <div id="view-content" :class="{ 'md-view': isMarkdown }" class="p-5" style="border: 2px solid #DFDFDF;background-color: #F6F6F6;">

    </div>
</div>
<script>
    let x = new Vue({
        el: '#document-content',
        data: {
            document: {
                documentId: '<c:out value="${documentId}"/>',
            },
            isMarkdown:false,
        },
        methods: {
            downloadMarkdownFile() {
                const blob = new Blob([this.document.documentContent], { type: 'text/plain' });
                const link = document.createElement('a');
                link.href = URL.createObjectURL(blob);
                link.download = document.getElementById("din").innerHTML+".md";
                link.style.display = 'none';
                document.body.appendChild(link);
                link.click();
                URL.revokeObjectURL(link.href);
                document.body.removeChild(link);
            },
            downloadHtmlFile() {

            },
            fetchData() {
                axios.post('${pageContext.request.contextPath}/document/getDocumentById', this.document)
                    .then(response => {
                        let data = response.data.data;
                        this.document = data
                        let content = data.documentContent;
                        if (data.documentType === 'markdown') {
                            initViewMD(content)
                            this.isMarkdown=true
                        } else if (data.documentType === 'html') {
                            initViewHtml(content)
                        }

                    })
                    .catch(error => {
                        console.error('Error:', error);
                        windowToastManager.showToast('failed', error)
                    });
            },
        },
        mounted() {
            this.fetchData();
        }
    });
</script>
</body>
</html>