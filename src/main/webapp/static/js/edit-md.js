/*
* 需要 editor
* 需要 DOMPurify
* 需要 Prism
* 非必要 imageModal
* */



var showLineNumber = true;

function change() {
    const markdownContent = editor.getValue();

    const converter = getShowdown()

    const htmlContent = converter.makeHtml(markdownContent);

    const sanitizedHtmlContent = DOMPurify.sanitize(htmlContent);

    const previewElement = document.getElementById("markdown-view");
    previewElement.innerHTML = sanitizedHtmlContent;

    if(showLineNumber){
        toggleLineNumbers()
    }

    Prism.highlightAll();

    initClipboard()
    if(imageModal){
        imageModal.updateClickEvents();
    }
}

function toggleLineNumbers() {
    const pe = document.getElementById("markdown-view");
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

function toggleLayout(value) {
    var a1 = document.getElementById('a1');
    var a2 = document.getElementById('a2');

    if (value === 1) {
        a1.style.width = '100%';
        a1.style.display = 'block';
        a2.style.display = 'none';
    } else if (value === 2) {
        a1.style.display = 'none';
        a2.style.width = '100%';
        a2.style.display = 'block';
    } else if (value === 3) {
        a1.style.width = '50%';
        a2.style.width = '50%';
        a1.style.display = 'block';
        a2.style.display = 'block';
    }
}

// 增加字体大小
function increaseFontSize() {
    var currentSize = parseInt(editor.getFontSize(), 10);
    editor.setFontSize(currentSize + 2 + "px");
}

// 减小字体大小
function decreaseFontSize() {
    var currentSize = parseInt(editor.getFontSize(), 10);
    editor.setFontSize(currentSize - 2 + "px");
}
function link(){
    editor.insert("[链接](https://www.example.com)");
}

function strong(){
    var selectedText = editor.getSelectedText();

    var replacementText = "**"+selectedText.trim() + "**";

    editor.session.replace(editor.getSelectionRange(), replacementText);
}

function italicize() {
    // 获取当前选中的文本内容
    var selectedText = editor.getSelectedText();

    // 进行倾斜文本的拼接替换操作
    var replacementText = "*" + selectedText.trim() + "*";

    // 替换选中文本
    editor.session.replace(editor.getSelectionRange(), replacementText);
}

function addNumbering() {
    // 获取光标所在行的行号
    var cursorRow = editor.getSelectionRange().start.row;

    // 获取光标所在行的内容
    var currentLineContent = editor.session.getLine(cursorRow);

    // 在行首添加标号，并保留原始内容
    var numberedLine = (cursorRow + 1) + ". " + currentLineContent;

    // 替换光标所在行的内容
    editor.session.replace(
        new ace.Range(cursorRow, 0, cursorRow, currentLineContent.length),
        numberedLine
    );

    // 移动光标到下一行
    editor.gotoLine(cursorRow + 2, 0);
}
function addList() {
    // 获取光标所在行的行号
    var cursorRow = editor.getSelectionRange().start.row;

    // 获取光标所在行的内容
    var currentLineContent = editor.session.getLine(cursorRow);

    // 在行首添加标号，并保留原始内容
    var numberedLine ="- " + currentLineContent;

    // 替换光标所在行的内容
    editor.session.replace(
        new ace.Range(cursorRow, 0, cursorRow, currentLineContent.length),
        numberedLine
    );

    // 移动光标到下一行
    editor.gotoLine(cursorRow + 2, 0);
}

function addCheckBox() {
    // 获取光标所在行的行号
    var cursorRow = editor.getSelectionRange().start.row;

    // 获取光标所在行的内容
    var currentLineContent = editor.session.getLine(cursorRow);

    // 在行首添加标号，并保留原始内容
    var numberedLine ="- [x] " + currentLineContent;

    // 替换光标所在行的内容
    editor.session.replace(
        new ace.Range(cursorRow, 0, cursorRow, currentLineContent.length),
        numberedLine
    );

    // 移动光标到下一行
    editor.gotoLine(cursorRow + 2, 0);
}


function addStrikethroughToSelectedText() {
    // 获取当前选中的文本内容
    var selectedText = editor.getSelectedText();

    // 在选中文本前后添加删除线
    var strikethroughText = "~~" + selectedText + "~~";

    // 替换选中文本
    editor.session.replace(editor.getSelectionRange(), strikethroughText);
}

function addUnderline() {
    // 获取当前选中的文本内容
    var selectedText = editor.getSelectedText();

    // 在选中文本前后添加删除线
    var strikethroughText = "<u>" + selectedText + "</u>";

    // 替换选中文本
    editor.session.replace(editor.getSelectionRange(), strikethroughText);
}

function insertCodeArea() {

    var tableText = '```javascript\n\n```';
    editor.insert(tableText);
}




function findMarkdownHeadingLine(aceEditor, headingIndex) {
    const editorContent = aceEditor.getValue();
    const lines = editorContent.split('\n');
    let headingCounter = 0;
    let codeBlockDelimiterCount = 0; // 用于计数遇到的 '```' 数量

    for (let i = 0; i < lines.length; i++) {
        const line = lines[i];

        // 检查是否是代码块分隔符，以 '```' 开头的行
        if (line.trim().startsWith('```')) {
            codeBlockDelimiterCount++;
        }

        // 判断当前是否在代码块内
        const inCodeBlock = codeBlockDelimiterCount % 2 === 1;

        if (!inCodeBlock) {
            // 检查是否是 Markdown 标题，例如 #, ##, ### 开头的行
            if (line.trim().match(/^#+.*/)) {
                if (headingCounter === headingIndex) {
                    return i; // 返回该标题的行号
                }
                headingCounter++;
            }
        }
    }
    return -1; // 如果未找到则返回 -1
}

function findHtmlHeadingElementByIndex(headingIndex) {
    const headings = document.querySelectorAll('h1, h2, h3, h4, h5, h6'); // 查找所有标题元素
    if (headingIndex >= 0 && headingIndex < headings.length) {
        return headings[headingIndex];
    }
    return null; // 如果未找到则返回 null
}
function scrollToAceLine(aceEditor, lineNumber) {
    aceEditor.scrollToLine(lineNumber, true, true, function () {});
    aceEditor.gotoLine(lineNumber + 1); // 跳转到指定行
}
function scrollToHtmlElement(element) {
    if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
}


function delegateClickEventToHeadings() {
    // 假设渲染的标题都在某个容器内，比如 <div id="markdown-content">
    const container = document.getElementById('markdown-view');

    console.log("添加了事件");
    console.log(container);

    // 在容器上添加点击事件监听器
    container.addEventListener('click', (event) => {
        // 只有在按下 Ctrl 键时才执行操作
        if (!event.ctrlKey) return;

        const heading = event.target.closest('h1, h2, h3, h4, h5, h6');
        if (heading) {
            const index = Array.from(container.querySelectorAll('h1, h2, h3, h4, h5, h6')).indexOf(heading);
            if (index !== -1) {
                onHeadingClick(index,1); // 调用点击事件处理函数
            }
        }
    });

    // 监听 Ctrl 键的按下和释放，用于添加或移除特殊样式
    document.addEventListener('keydown', (event) => {
        if (event.key === 'Control') {
            document.body.classList.add('ctrl-pressed');
        }
    });

    document.addEventListener('keyup', (event) => {
        if (event.key === 'Control') {
            document.body.classList.remove('ctrl-pressed');
        }
    });
}



function onHeadingClick(headingIndex,action) {
    // 在点击标题时，可以执行你需要的操作
    console.log('Clicked heading index:', headingIndex);

    // 在 ACE 编辑器中找到对应行号
    if (action === 1 || action === 3) {
        const lineNumber = findMarkdownHeadingLine(editor, headingIndex);
        console.log("编辑器行号:" + lineNumber)
        if (lineNumber !== -1) {
            scrollToAceLine(editor, lineNumber); // 滚动到该行
        }
    }

    // 在 HTML 中找到对应元素

    if (action === 2 || action === 3) {
        const headingElement = findHtmlHeadingElementByIndex(headingIndex);
        console.log("html元素:" + headingElement)
        if (headingElement) {
            scrollToHtmlElement(headingElement); // 滚动到该元素
        }
    }
}







// 判断是否为标题行且不在代码块内
function isHeadingLine(aceEditor, lineNumber) {
    const editorContent = aceEditor.getValue();
    const lines = editorContent.split('\n');

    let codeBlockDelimiterCount = 0;
    for (let i = 0; i <= lineNumber; i++) {
        const line = lines[i];

        if (line.trim().startsWith('```')) {
            codeBlockDelimiterCount++;
        }
    }
    const inCodeBlock = codeBlockDelimiterCount % 2 === 1;

    if (inCodeBlock) return false;

    const lineContent = lines[lineNumber].trim();
    return /^#+.*/.test(lineContent);
}

// 获取标题的索引
function getHeadingIndex(aceEditor, targetLineNumber) {
    const editorContent = aceEditor.getValue();
    const lines = editorContent.split('\n');
    let headingCounter = 0;
    let codeBlockDelimiterCount = 0;

    for (let i = 0; i < lines.length; i++) {
        const line = lines[i];

        if (line.trim().startsWith('```')) {
            codeBlockDelimiterCount++;
        }
        const inCodeBlock = codeBlockDelimiterCount % 2 === 1;

        if (!inCodeBlock) {
            if (line.trim().match(/^#+.*/)) {
                if (i === targetLineNumber) {
                    return headingCounter;
                }
                headingCounter++;
            }
        }
    }
    return -1;
}


// 为标题添加点击事件
function addClickEventToAceHeadings(aceEditor) {
    aceEditor.on('click', function(e) {

        const position = e.getDocumentPosition();
        const lineNumber = position.row;

        if (isHeadingLine(aceEditor, lineNumber)) {
            const headingIndex = getHeadingIndex(aceEditor, lineNumber);
            if (headingIndex !== -1) {
                onHeadingClick(headingIndex,2);
            }
        }
    });
}

// 初始化功能
function initializeAceHeadingInteractivity() {
    addClickEventToAceHeadings(editor);
}

