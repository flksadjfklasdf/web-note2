function initViewMD(content) {
    const markdownContent = content

    const converter = getShowdown()

    const htmlContent = converter.makeHtml(markdownContent);

    const sanitizedHtmlContent = DOMPurify.sanitize(htmlContent);

    const previewElement = document.getElementById("view-content");
    previewElement.innerHTML = sanitizedHtmlContent;

    toggleLineNumbers()
    Prism.highlightAll();

    initClipboard()
    if(imageModal){
        imageModal.updateClickEvents();
    }
}
function initViewHtml(content) {
    //const sanitizedHtmlContent = DOMPurify.sanitize(content);
    const previewElement = document.getElementById("view-content");
    previewElement.innerHTML = content;
}

function collect(){
    var elementById = document.getElementById('collect-ico');

    if(elementById.classList.contains("bi-star")){
        elementById.classList.remove('bi-star')
        elementById.classList.add('bi-star-fill')
    }else{
        elementById.classList.remove('bi-star-fill')
        elementById.classList.add('bi-star')
    }
}
function toggleLineNumbers() {
    const pe = document.getElementById("view-content");
    var codeBlocks = pe.querySelectorAll('code');
    codeBlocks.forEach(function(code) {
        if (code.classList.contains('line-numbers')) {
            code.classList.remove('line-numbers');
            code.parentNode.classList.remove('line-numbers');
        } else {
            code.classList.add('line-numbers');
            code.parentNode.classList.add('line-numbers');
        }
    });
}