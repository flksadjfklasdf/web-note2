function getShowdown() {

    var languageBar = {
        type: 'output',
        filter: function (html, converter, options) {
            // 使用正则表达式找到代码块
            var regex = /<pre><code([\s\S]*?)>([\s\S]*?)<\/code><\/pre>/g;

            return html.replace(regex, function (match, attributes, code) {

                var classRegex = /class=["'][^"']*?\blanguage-([^"'\s]*)/;
                var matchClass = attributes.match(classRegex);
                var codeLanguage = matchClass ? matchClass[1] : '';

                return '<div class="code-container">' +
                    '<div class="code-description" ><span class="code-type">' +
                    codeLanguage +
                    '</span><i class="bi bi-files code-copy" title="复制代码"></i></div>' +
                    '<pre><code' + attributes + '>' + code + '</code></pre>' +
                    '</div>';
            });
        }
    };
    return new showdown.Converter({
        tables: true,        // Enable table support
        strikethrough: true, // Enable strikethrough support
        tasklists: true,     // Enable task list support
        simplifiedAutoLink: true, // Enable autolink support
        headerLevelStart: 2,  // Start headers from level 2
        extensions: [languageBar]
    });
}

var clipboard = null; // 保留一个全局变量用于存储剪贴板实例

function initClipboard() {
    // 如果已存在剪贴板实例，先销毁它
    if (clipboard) {
        clipboard.destroy();
    }
    // 创建新的剪贴板实例
    clipboard = new ClipboardJS('.code-copy', {
        text: function (trigger) {
            var codeBlock = trigger.parentNode.nextElementSibling.querySelector('code');
            return codeBlock.innerText;
        }
    });
}