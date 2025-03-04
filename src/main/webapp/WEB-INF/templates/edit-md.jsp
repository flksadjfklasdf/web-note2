<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="en" data-bs-theme="auto">
<head>
    <meta charset="utf-8">
    <%@include file="t-link.jsp" %>
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/css/edit-md.css" rel="stylesheet">
    <script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/security/purify.min.js"></script>
    <script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/edit-md.js"></script>
    <script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/ace/src-min-noconflict/ace.js"></script>
    <script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/tabulator/tabulator.js"></script>
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/tabulator/tabulator.css" rel="stylesheet">
    <script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/md-edit/tabulatorTable.js"></script>
    <title>编辑 - <c:out value="${collections.collectionName}"/></title>
</head>
<body>
<%@include file="t-toast.jsp" %>
<%@include file="t-image.jsp" %>
<div id="main-a">
    <div style="position: fixed;left: 0;width: 280px;height: 100vh;z-index: 2;">
        <div id="content-view" class="rounded-4 d-flex flex-column p-3 border border-3 border-secondary"
             style="width: 280px; height: 100vh; overflow-y: auto;">
            <div class="d-flex justify-content-between border-bottom">
                <a href="#" class="d-flex align-items-center pb-3 link-body-emphasis text-decoration-none">
                    <span class="fs-5 fw-semibold">目录</span>
                </a>
                <div class="d-flex align-items-center pb-3">
                    <a @click="editSort = !editSort"
                       class="pointer link-body-emphasis text-decoration-none" style="margin-right: 20px" title="编辑顺序">
                        <i class="bi bi-sort-alpha-up" :style="{ 'color' : editSort ? 'orange' : '' }"></i>
                    </a>
                    <a @click="createDocument"
                       class="pointer link-body-emphasis text-decoration-none " title="新增">
                        <i class="bi bi-plus-circle"></i>
                    </a>
                </div>
            </div>

            <ul class="list-unstyled ps-0">
                <li class="mb-1">
                    <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed"
                            data-bs-toggle="collapse" data-bs-target="#home-collapse" aria-expanded="true">
                        合集
                    </button>
                    <div class="collapse show" id="home-collapse">
                        <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
                            <!--列表渲染开始-->
                            <li v-for="(document,i) in documents_title" :key="document.documentId" :class="{ 'active': document.documentId === document_edit.documentId }"
                                class="d-flex justify-content-between menu-item pointer"  @click="editDocument(document.documentId,i)">
                                <a style="width: 130px;overflow: hidden;white-space: nowrap;"
                                   :title="`\${document.documentName}`"
                                   class="pointer link-body-emphasis d-inline-flex text-decoration-none rounded">{{
                                    document.documentName }}</a>
                                <div class="d-inline-flex">
                                    <a @click.stop="modifyDocument(document.documentId, document.documentName,i)"
                                       class="pointer menu-ico link-body-emphasis text-decoration-none rounded"><i
                                            class="bi" :class="{ 'bi-pencil' : !editSort , 'bi-arrow-down' : editSort }"></i></a>
                                    <a @click.stop="deleteDocument(document.documentId, document.documentName,i)"
                                       class="pointer menu-ico link-body-emphasis text-decoration-none rounded"><i
                                            class="bi" :class="{ 'bi-trash' : !editSort , 'bi-arrow-up' : editSort }"></i></a>
                                </div>
                            </li>
                        </ul>
                    </div>
                </li>
            </ul>
        </div>
    </div>

    <div style="position: fixed;left: 300px;right:0;height: 100vh;z-index: 2;" v-show="document_edit.documentId != '' ">
        <div class="mb-3" style="padding:10px;height: 50px">

            <div class="btn-group">
                <button type="button" class="btn btn-outline-secondary" title="字体-" onclick="decreaseFontSize()">
                    <i class="bi bi-sort-alpha-down"></i>
                    <span class="visually-hidden">Button</span>
                </button>
                <button type="button" class="btn btn-outline-secondary" title="字体+" onclick="increaseFontSize()">
                    <i class="bi bi-sort-alpha-up"></i>
                    <span class="visually-hidden">Button</span>
                </button>
            </div>

            <div class="btn-group">
                <button type="button" class="btn btn-outline-secondary" title="加粗" onclick="strong()">
                    <i class="bi bi-type-bold"></i>
                    <span class="visually-hidden">Button</span>
                </button>
                <button type="button" class="btn btn-outline-secondary" title="倾斜" onclick="italicize()">
                    <i class="bi bi-type-italic"></i>
                    <span class="visually-hidden">Button</span>
                </button>
                <button type="button" class="btn btn-outline-secondary" title="下划线" onclick="addUnderline()">
                    <i class="bi bi-type-underline"></i>
                    <span class="visually-hidden">Button</span>
                </button>
                <button type="button" class="btn btn-outline-secondary" title="删除线"
                        onclick="addStrikethroughToSelectedText()">
                    <i class="bi bi-type-strikethrough"></i>
                    <span class="visually-hidden">Button</span>
                </button>
                <button type="button" class="btn btn-outline-secondary" title="标号" onclick="addNumbering()">
                    <i class="bi bi-list-ol"></i>
                    <span class="visually-hidden">Button</span>
                </button>
                <button type="button" class="btn btn-outline-secondary" title="列表" onclick="addList()">
                    <i class="bi bi-card-list"></i>
                    <span class="visually-hidden">Button</span>
                </button>
                <button type="button" class="btn btn-outline-secondary" title="复选框" onclick="addCheckBox()">
                    <i class="bi bi-card-checklist"></i>
                    <span class="visually-hidden">Button</span>
                </button>
                <button type="button" class="btn btn-outline-secondary" title="超链接" onclick="link()">
                    <i class="bi bi-link"></i>
                    <span class="visually-hidden">Button</span>
                </button>
                <button type="button" class="btn btn-outline-secondary" title="代码块" onclick="insertCodeArea()">
                    <i class="bi bi-code-slash"></i>
                    <span class="visually-hidden">Button</span>
                </button>
                <button type="button" class="btn btn-outline-secondary" title="图片" onclick="vx.uploadImage()">
                    <i class="bi bi-image"></i>
                    <span class="visually-hidden">Button</span>
                </button>
                <button type="button" class="btn btn-outline-secondary" title="视频" onclick="vx.selectVideo()">
                    <i class="bi bi-film"></i>
                    <span class="visually-hidden">Button</span>
                </button>
<%--                <button type="button" class="btn btn-outline-secondary" title="表格" onclick="insertTable()">
                    <i class="bi bi-border-all"></i>
                    <span class="visually-hidden">Button</span>
                </button>--%>

                <button type="button" class="btn btn-outline-secondary" title="表格" onclick="vx.editTable()">
                    <i class="bi bi-border-all"></i>
                    <span class="visually-hidden">Button</span>
                </button>
            </div>
            <div class="btn-group">
                <button type="button" class="btn btn-outline-secondary" title="编辑视图" onclick="toggleLayout(1)">
                    <i class="bi bi-vector-pen"></i>
                    <span class="visually-hidden">Button</span>
                </button>
                <button type="button" class="btn btn-outline-secondary" title="两侧视图" onclick="toggleLayout(3)">
                    <i class="bi bi-input-cursor"></i>
                    <span class="visually-hidden">Button</span>
                </button>
                <button type="button" class="btn btn-outline-secondary" title="效果视图" onclick="toggleLayout(2)">
                    <i class="bi bi-eye"></i>
                    <span class="visually-hidden">Button</span>
                </button>
            </div>

            <button onclick="vx.toggleWrapMode()" title="自动换行"
                    :class="{
                            'btn btn-outline-secondary': !isWrapEnabled,
                            'btn btn-outline-primary': isWrapEnabled
                            }">
                <i <%--:style="{ color: isWrapEnabled ? 'blue' : '' }"--%> class="bi bi-text-wrap"></i>
                <span class="visually-hidden">Toggle Wrap Mode</span>
            </button>

            <button :disabled="!isChanged" type="button" title="保存"
                    :class="{ 'btn btn-outline-secondary': !isChanged, 'btn btn-outline-danger': isChanged }"
                    onclick="vx.saveEditDocument(true)">
                <i :style="{ color: isChanged ? 'red' : '' }" class="bi bi-save"></i>
                <span class="visually-hidden">Button</span>
            </button>

            <button onclick="vx.toggleLineNumber()" title="显示行号"
                    :class="{
                            'btn btn-outline-secondary': !showLineNumber,
                            'btn btn-outline-primary': showLineNumber
                            }">
                <i class="bi bi-list-ol"></i>
                <span class="visually-hidden">Toggle Wrap Mode</span>
            </button>


        </div>
        <div class="w-100" style="display: flex;height: calc(100% - 85px);margin-right: 50px">
            <div id="a1" style="margin-left: 10px;margin-right: 10px;width: 50%">
                <div class="form-control no-resize h-100" style="outline: none;resize: none"
                     id="code_area" rows="3"></div>
            </div>
            <div id="a2" style="margin-left: 10px; margin-right: 30px; width: 50%">
                <div id="markdown-view" class="md-view form-control no-resize h-100" style="height: 100%; overflow-y: auto;">
                </div>
            </div>
        </div>
    </div>
    <%--删除文档--%>
    <div  class="modal fade" id="deleteDocument" tabindex="-1" aria-labelledby="deleteDocumentLabel"
          aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="deleteDocumentLabel">确认删除</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                            aria-label="Close"></button>
                </div>
                <div class="modal-body" id="deleteDocumentBody">
                    确定要删除'{{ de_document.documentName }}'文档吗？
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                    </button>
                    <button type="button" class="btn btn-danger" @click="doDeleteDocument">删除
                    </button>
                </div>
            </div>
        </div>
    </div>
    <!--新建文档-->
    <div class="modal fade" id="createDocument" tabindex="-1" aria-labelledby="createDocumentLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" style="padding: 30px">
                <label for="cr_documentName" class="form-label">文档名称</label>
                <input type="text" class="form-control" id="cr_documentName"
                       v-model="cr_document.documentName">

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                    </button>
                    <button class="btn btn-danger" @click="doCreateDocument">创建</button>
                </div>
            </div>
        </div>
    </div>
    <!--更改文档属性-->
    <div class="modal fade" id="modifyDocument" tabindex="-1" aria-labelledby="modifyDocumentLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" style="padding: 30px">
                <label for="mo_collectionName" class="form-label">文档名称</label>
                <input type="text" class="form-control" id="mo_collectionName"
                       v-model="mo_document.documentName">

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                    </button>
                    <button class="btn btn-danger" @click="doModifyDocument">更改</button>
                </div>
            </div>
        </div>
    </div>
    <!--上传图片-->
    <div class="modal fade" id="uploadImage" tabindex="-1" aria-labelledby="uploadImageLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" style="padding: 30px">
                <form id="uploadForm" enctype="multipart/form-data">
                    <div class="mb-3 w-75 mx-auto">
                        <input type="file" class="form-control" id="imageInput" name="image">
                        <input type="hidden" name="documentId" v-model="document_edit.documentId">
                    </div>
                </form>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                    </button>
                    <button class="btn btn-danger" @click="doUploadImage">上传</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 视频插入 -->
    <div class="modal fade" id="insertVideoModal" tabindex="-1" aria-labelledby="insertVideoLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" style="padding: 30px">
                <label for="videoUrlInput" class="form-label">视频URL</label>
                <input type="text" class="form-control" id="videoUrlInput" v-model="videoUrl">

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                    <button class="btn btn-danger" @click="doInsertVideo">插入</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 编辑表格 -->
    <div class="modal fade" id="editTableModal" tabindex="-1" aria-labelledby="editTableLabel" aria-hidden="true">
        <div class="modal-dialog" style="width: 1460px;max-width: 1460px">
            <div class="modal-content" style="padding: 30px;width: 1460px" >
                <span>编辑表格</span>
                <div id="table-area" style="width: 1400px">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                    <button class="btn btn-danger" @click="doEditTable">确定</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var vx = new Vue({
        el: '#main-a',
        data: {
            editSort:false,
            collection: {
                collectionId: '<c:out value="${collections.collectionId}"/>',
            },
            documents_title: [],
            document_edit: {
                documentId: '',
                documentContent: ''
            },
            de_document: {
                documentId: '',
                documentName: ''
            },
            mo_document: {
                documentId: '',
                documentName: ''
            },
            cr_document: {
                collectionId: '',
                documentName: '',
            },
            isWrapEnabled : false,
            videoUrl: '',
            isIgnoreChange: false,
            isChanged: false,
            showLineNumber:true
        },
        methods: {
            fetchData(first) {
                axios.post('${pageContext.request.contextPath}/document/getDocumentTitleAll', this.collection)
                    .then(response => {
                        this.documents_title = response.data.data;
                        if(first){
                            const i = Number(window.location.hash.substring(1));
                            if(this.documents_title.length === 0){
                                return
                            }
                            this.editDocument(this.documents_title[i].documentId,i)
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        windowToastManager.showToast('failed', '请求文章列表失败!')
                    });
            },
            deleteDocument(documentId, documentName,index) {
                if(this.editSort){
                    this.moveUp(index);
                    return;
                }
                this.de_document = {
                    documentId: documentId,
                    documentName: documentName
                }

                var modal = new bootstrap.Modal(document.getElementById('deleteDocument'), {
                    keyboard: false
                });
                modal.show();
            },
            doDeleteDocument() {
                axios.post('${pageContext.request.contextPath}/document/delete', this.de_document)
                    .then(async (response) => {

                        console.log('Response:', response.data);
                        if (response.data.status === 0) {
                            windowToastManager.showToast('success', '删除成功')

                            if (this.de_document.documentId === this.document_edit.documentId) {
                                this.clearEdit()
                            }
                        } else {
                            windowToastManager.showToast('failed', response.data.message)
                        }
                        await this.fetchData();
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        windowToastManager.showToast('failed', '删除文章失败')
                    });

                var modal = bootstrap.Modal.getInstance(document.getElementById('deleteDocument'));
                modal.hide();
            },
            modifyDocument(documentId, documentName,index) {
                if(this.editSort){
                    this.moveDown(index);
                    return;
                }
                this.mo_document = {
                    documentId: documentId,
                    documentName: documentName
                }

                var modal = new bootstrap.Modal(document.getElementById('modifyDocument'), {
                    keyboard: false
                });
                modal.show();
            },
            doModifyDocument() {
                axios.post('${pageContext.request.contextPath}/document/modify', this.mo_document)
                    .then(async (response) => {

                        if (response.data.status !== 0) {
                            windowToastManager.showToast('failed', response.data.message)
                        }
                        await this.fetchData();
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });

                var modal = bootstrap.Modal.getInstance(document.getElementById('modifyDocument'));
                modal.hide();
            },
            createDocument() {
                this.cr_document = {
                    collectionId: this.collection.collectionId,
                    documentName: '',
                };

                var modal = new bootstrap.Modal(document.getElementById('createDocument'), {
                    keyboard: false
                });
                modal.show();
            },
            doCreateDocument() {
                axios.post('${pageContext.request.contextPath}/document/create', this.cr_document)
                    .then(async (response) => {
                        if (response.data.status !== 0) {
                            windowToastManager.showToast('failed', response.data.message)
                        }
                        await this.fetchData();
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });

                var modal = bootstrap.Modal.getInstance(document.getElementById('createDocument'));
                modal.hide();
            },
            editDocument(documentId,i) {

                if (this.document_edit.documentId !== '') {
                    this.saveEditDocument(false)
                }

                var json = {
                    documentId: documentId,
                }

                axios.post('${pageContext.request.contextPath}/document/getDocumentById', json)
                    .then(async response => {
                        var data = response.data.data;
                        this.document_edit.documentId = data.documentId;
                        this.document_edit.documentContent = data.documentContent;

                        history.replaceState(null, null, `#\${i}`);
                        //window.location.hash=`#\${i}`

                        this.isChanged = false;
                        this.isIgnoreChange = true
                        editor.setValue(data.documentContent)

                        var lines = editor.session.getLength();
                        var lastLine = lines - 1;
                        var lastColumn = editor.session.getLine(lastLine).length;

                        editor.gotoLine(lastLine + 1, lastColumn);
                        editor.focus();

                        editor.session.getUndoManager().reset()
                        this.isIgnoreChange = false
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });
            },
            saveEditDocument(show) {

                if (this.document_edit.documentId === '' || this.isChanged === false) {
                    return
                }

                this.document_edit.documentContent = editor.getValue();

                axios.post('${pageContext.request.contextPath}/document/save', this.document_edit)
                    .then(async response => {
                        if (response.data.status === 0) {
                            if (show) {
                                this.isChanged = false;
                                windowToastManager.showToast('success', '保存成功')
                            }
                        } else {
                            windowToastManager.showToast('failed', response.data.message)
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        windowToastManager.showToast('failed', error)
                    });
            },
            uploadImage() {
                var form = document.getElementById('uploadForm');
                form.querySelector('#imageInput').value = null;

                var modal = new bootstrap.Modal(document.getElementById('uploadImage'), {
                    keyboard: false
                });
                modal.show();
            },
            doUploadImage() {
                var form = document.getElementById('uploadForm');
                var formData = new FormData(form);

                axios.post('${pageContext.request.contextPath}/image/upload', formData)
                    .then(response => {
                        var imageId = response.data.data.imageId;

                        if (response.data.status === 0) {
                            editor.insert("<img src=\"${imgUrlPrefix}${pageContext.request.contextPath}/i/g/" + imageId + "\" class=\"custom-img\">");
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });

                var modal = bootstrap.Modal.getInstance(document.getElementById('uploadImage'));
                modal.hide();
            },
            selectVideo() {
                this.videoUrl = '';
                var modal = new bootstrap.Modal(document.getElementById('insertVideoModal'), {
                    keyboard: false
                });
                modal.show();
            },
            doInsertVideo() {
                var url = this.videoUrl
                editor.insert(`<video controls crossorigin playsinline class="plyr" style ="height:250px;display:block;margin:10px">\n\t<source src="\${url}" type="video/mp4" />\n</video>`);
                var modal = bootstrap.Modal.getInstance(document.getElementById('insertVideoModal'));
                modal.hide();
            },
            clearEdit() {
                this.document_edit = {
                    documentId: '',
                    documentName: '',
                    documentContent: ''
                }
            },
            editTable(){
                this.videoUrl = '';
                var modal = new bootstrap.Modal(document.getElementById('editTableModal'), {
                    keyboard: false
                });
                modal.show();

                var selectedText = editor.getSelectedText();

                if(selectedText === ''){
                    var tabledata = [
                        {id:1, name:"Oli Bob", col:"red", dob:""},
                    ];

                    table = new Tabulator("#table-area", {
                        movableRows:true,
                        movableColumns:true,
                        data:tabledata,
                        layout:"fitColumns",
                        columns:[
                            {title:"Name", field:"name",editor:true,contextMenu:cellContextMenu,  headerSort: false},
                            {title:"Favourite Color", field:"col",editor:true,contextMenu:cellContextMenu,  headerSort: false},
                            {title:"Date Of Birth", field:"dob",editor:true,contextMenu:cellContextMenu, headerSort: false},
                        ],
                    });
                } else{
                    table = createTabulatorFromMarkdown(selectedText,"#table-area")
                }
            },
            doEditTable(){
                var s = exportTableToMarkdown(table);
                editor.session.replace(editor.getSelectionRange(), s);
                var modal = bootstrap.Modal.getInstance(document.getElementById('editTableModal'));
                modal.hide();
            },
            toggleWrapMode() {
                this.isWrapEnabled = !this.isWrapEnabled;
                editor.getSession().setUseWrapMode(this.isWrapEnabled);
            },
            toggleLineNumber(){
                this.showLineNumber=!this.showLineNumber
                showLineNumber=this.showLineNumber
                change()
            },
            moveUp(index) {
                if (index > 0) {
                    this.swapItems(index, index - 1);
                }
            },
            moveDown(index) {
                if (index < this.documents_title.length - 1) {
                    this.swapItems(index, index + 1);
                }
            },
            swapItems(fromIndex, toIndex) {
                orderExchange={
                    documentId1:this.documents_title[fromIndex].documentId,
                    documentId2:this.documents_title[toIndex].documentId
                }

                axios.post('${pageContext.request.contextPath}/document/exchangeOrder',orderExchange)
                    .then(async (response) => {
                        if (response.data.status !== 0) {
                            windowToastManager.showToast('failed', response.data.message)
                        }
                        await this.fetchData();
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });
            }
        },
        mounted() {
            this.fetchData(true);
        }
    });
</script>
<script>
    var editor = ace.edit("code_area");
    editor.setTheme("ace/theme/chrome");
    editor.session.setMode("ace/mode/markdown");

    editor.container.style.backgroundColor = "white";

    editor.on("change", function () {
        if (!vx.isIgnoreChange) {
            vx.isChanged = true;
        }
        change();
    });

    editor.container.addEventListener('paste', function (event) {
        var items = (event.clipboardData || event.originalEvent.clipboardData).items;

        for (var i = 0; i < items.length; i++) {
            if (items[i].type.indexOf('image') !== -1) {
                var imageFile = items[i].getAsFile();

                handlePastedImage(imageFile);

                event.preventDefault();
                break;
            }
        }
    });

    document.addEventListener('keydown', function (event) {
        if (event.ctrlKey && event.key === 's') {
            event.preventDefault();
            vx.saveEditDocument(true);
        }
    });


    window.addEventListener('beforeunload', function (e) {
        if (vx.isChanged) {
            var confirmationMessage = '确定要离开吗？未保存的更改将丢失。';
            e.returnValue = confirmationMessage; // 兼容旧版浏览器
            return confirmationMessage;
        }
    });

    function handlePastedImage(imageFile) {
        var formData = new FormData();
        formData.append('image', imageFile);

        formData.append('documentId', vx.document_edit.documentId);

        axios.post('${pageContext.request.contextPath}/image/upload', formData)
            .then(response => {
                if (response.data.status === 0) {
                    var imageId = response.data.data.imageId;
                    editor.insert("<img src=\"${imgUrlPrefix}${pageContext.request.contextPath}/i/g/" + imageId + "\" class=\"custom-img\">");
                }else{
                    windowToastManager.showToast('failed', response.data.message)
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    delegateClickEventToHeadings()
    initializeAceHeadingInteractivity()
</script>


</body>
</html>
