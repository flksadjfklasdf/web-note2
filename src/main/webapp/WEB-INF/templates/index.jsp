<%@ page contentType="text/html;charset=UTF-8"%>
<!doctype html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>笔迹阁</title>
        <%@include file="t-link.jsp" %>
        <style>
            #main{
                height: 100vh;
            }
        </style>
    </head>
<body>
<%@include file="t-header.jsp" %>
<div id="main" class="py-5 text-center container d-flex align-items-center">
    <div class="row py-lg-5">
        <div class="col-lg-6 col-md-8 mx-auto">
            <h1 class="fw-light">笔迹阁</h1>
            <p class="lead text-body-secondary">在知识的海洋中留下您独特的印记，笔迹阁是您智慧的归宿。将思考和灵感以 Markdown 的形式记录，轻松编辑与阅读，分享您的见解，启发他人的思考。</p>
            <p>
                <a href="#" class="btn btn-primary my-2">立即使用</a>
                <a href="javascript:scrollToExample()" class="btn btn-secondary my-2">欣赏效果</a>
            </p>
        </div>
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="col-lg-6">
            <div class="card border-primary mb-3 ml-3 bg-light">
                <div class="card-body example-show">
                    <div id="markdown-preview-left"></div>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <div class="card border-primary mb-3 ml-3 bg-light">
                <div class="card-body example-show">
                    <div id="markdown-preview-right"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function scrollToExample() {
        var exampleTextTop = document.getElementById('markdown-preview-left').getBoundingClientRect().top + window.scrollY;

        var offset = 100;
        var scrollToTop = exampleTextTop - offset;

        window.scrollTo({
            top: scrollToTop,
            behavior: 'smooth'
        });
    }
</script>
<script>
    axios.post('${pageContext.request.contextPath}/example/getExampleContent')
        .then(response => {
            var c1=response.data.data[0];
            var c2=response.data.data[1];

            let converter=getShowdown()

            const htmlContent1 = converter.makeHtml(c1);
            const htmlContent2 = converter.makeHtml(c2);

            const previewElementLeft = document.getElementById("markdown-preview-left");
            previewElementLeft.innerHTML = htmlContent1;

            const previewElementRight = document.getElementById("markdown-preview-right");
            previewElementRight.innerHTML = htmlContent2;

            Prism.highlightAll();

            initClipboard()
        })
</script>
</body>
</html>
