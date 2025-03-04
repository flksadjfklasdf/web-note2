<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en" data-bs-theme="auto">
<head>
    <meta charset="utf-8">
    <%@include file="t-link.jsp" %>
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/css/edit-md.css"
          rel="stylesheet">
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/quill/quill.snow.css"
          rel="stylesheet">
    <script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/edit-html.js"></script>
    <script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/quill/quill.min.js"></script>
    <title>编辑 - <c:out value="${collections.collectionName}"/></title>
</head>
<body>
<%@include file="t-toast.jsp" %>
<div id="content-view"  style="display: flex; height: 100vh;">
    <div class="d-flex flex-column min-vh-100">
        <div class="rounded-4 d-flex flex-column p-3 border border-3 border-secondary"
             style="width: 280px; height: 100vh; overflow-y: auto;">
            <div class="d-flex justify-content-between border-bottom">
                <a href="#" class="d-flex align-items-center pb-3 link-body-emphasis text-decoration-none">
                    <span class="fs-5 fw-semibold">目录</span>
                </a>
                <a @click="createDocument"
                   class="pointer d-flex align-items-center pb-3 link-body-emphasis text-decoration-none ">
                    <i class="bi bi-plus-circle"></i>
                </a>
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
                            <li v-for="document in documents_title" :key="document.documentId"
                                class="d-flex justify-content-between menu-item">
                                <a @click="editDocument(document.documentId)"
                                   style="width: 130px;overflow: hidden;white-space: nowrap;"
                                   :title="`\${document.documentName}`"
                                   :class="{ 'active': document.documentId === document_edit.documentId }"
                                   class="pointer link-body-emphasis d-inline-flex text-decoration-none rounded">{{
                                    document.documentName }}</a>
                                <div class="d-inline-flex">
                                    <a @click="modifyDocument(document.documentId, document.documentName)"
                                       class="pointer menu-ico link-body-emphasis text-decoration-none rounded"><i
                                            class="bi bi-pencil"></i></a>
                                    <a @click="deleteDocument(document.documentId, document.documentName)"
                                       class="pointer menu-ico link-body-emphasis text-decoration-none rounded"><i
                                            class="bi bi-trash"></i></a>
                                </div>
                            </li>
                            <!--列表渲染结束-->
                        </ul>
                        <%--删除文档--%>
                        <div class="modal fade" id="deleteDocument" tabindex="-1" aria-labelledby="deleteDocumentLabel"
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
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div style="height: 90vh;width: 100%;margin: 20px" v-show="document_edit.documentId != '' ">
        <div id="toolbar">
            <select class="ql-size" title="字体大小">
                <option value="small"></option>
                <option selected></option>
                <option value="large"></option>
                <option value="huge"></option>
            </select>
            <select class="ql-header" title="标题">
                <option value="1"></option>
                <option value="2"></option>
                <option value="3"></option>
                <option value="4"></option>
                <option value="5"></option>
                <option value="6"></option>
                <option selected></option>
            </select>
            <button class="ql-bold" title="加粗(ctrl+B)"></button>
            <button class="ql-italic" title="倾斜(ctrl+i)"></button>
            <button class="ql-underline" title="下划线(ctrl+u)"></button>
            <button class="ql-strike" title="删除线"></button>
            <select class="ql-color" title="字体颜色"></select>
            <select class="ql-background" title="字体背景色"></select>
            <button class="ql-blockquote" title="引用"></button>
            <button class="ql-code-block" title="代码块"></button>
            <button class="ql-list" value="ordered" title="列表"></button>
            <button class="ql-list" value="bullet" title="列表"></button>
            <button class="ql-script" value="sub" title="上标"></button>
            <button class="ql-script" value="super" title="下标"></button>
            <button class="ql-indent" value="-1" title="缩进-"></button>
            <button class="ql-indent" value="+1" title="缩进+"></button>
            <button class="ql-direction" value="rtl" title="对齐方式"></button>
            <select class="ql-align" title="对齐方式"></select>>
            <button title="插入图片" onclick="vx.uploadImage()">
                <i class="bi bi-image"></i>
            </button>
            <button class="ql-video" title="插入视频"></button>
            <button class="ql-link" title="插入链接"></button>
            <button class="ql-clean" title="清除格式"></button>
            <button :disabled="!isChanged" class="ql-custom-button" title="保存" onclick="vx.saveEditDocument(true)">
                <i class="bi bi-save"></i>
            </button>
        </div>
        <div id="editor">
        </div>
    </div>
</div>
<script>
    var vx = new Vue({
        el: '#content-view',
        data: {
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
            isIgnoreChange: false,
            isChanged: false,
        },
        methods: {
            fetchData() {
                axios.post('${pageContext.request.contextPath}/document/getDocumentTitleAll', this.collection)
                    .then(response => {
                        this.documents_title = response.data.data;
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        windowToastManager.showToast('failed', '请求文章列表失败!')
                    });
            },
            deleteDocument(documentId, documentName) {
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
                            if(this.de_document.documentId === this.document_edit.documentId){
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
            modifyDocument(documentId, documentName) {
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

                        console.log('Response:', response.data);
                        await this.fetchData();
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });

                var modal = bootstrap.Modal.getInstance(document.getElementById('createDocument'));
                modal.hide();
            },
            editDocument(documentId) {

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

                        console.log(data)

                        this.isChanged = false;
                        this.isIgnoreChange=true
                        editor.clipboard.dangerouslyPasteHTML(data.documentContent);
                        this.isIgnoreChange=false
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });
            },
            saveEditDocument(show) {

                if (this.document_edit.documentId === '' || this.isChanged === false) {
                    return
                }

                this.document_edit.documentContent = editor.root.innerHTML;

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
                            var imageUrl = "${imgUrlPrefix}/image/getImage?imageId=" + imageId

                            const range = editor.getSelection();
                            editor.insertEmbed(range.index, 'image', imageUrl);
                        } else {
                            windowToastManager.showToast('failed', "上传失败!")
                        }
                    })
                    .catch(error => {
                        windowToastManager.showToast('failed', "上传失败!")
                    });

                var modal = bootstrap.Modal.getInstance(document.getElementById('uploadImage'));
                modal.hide();
            },
            clearEdit() {
                this.document_edit = {
                    documentId: '',
                    documentName: '',
                    documentContent: ''
                }
            }
        },
        mounted() {
            this.fetchData();
        }
    });
</script>
<script>
    var editor = new Quill('#editor', {
        modules: {
            toolbar: '#toolbar'
        },
        theme: 'snow',
    });

    editor.on('text-change', function(delta, oldDelta, source) {
        if(!vx.isIgnoreChange) {
            vx.isChanged = true;
        }
    });

    editor.clipboard.addMatcher('img', function(node, delta) {
        var imageUrl = node.src;

        if (imageUrl.startsWith('http://') || imageUrl.startsWith('https://')) {
            return delta;
        }

        delta.ops = [];

        handlePastedImage(imageUrl);
        return delta;
    });

    document.addEventListener('keydown', function(event) {
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

    function handlePastedImage(imageUrl) {
        fetch(imageUrl)
            .then(response => response.blob())
            .then(blob => {
                var formData = new FormData();
                formData.append('image', blob);
                formData.append('documentId', vx.document_edit.documentId);

                axios.post('${pageContext.request.contextPath}/image/upload', formData)
                    .then(response => {
                        var imageId = response.data.data.imageId;

                        if (response.data.status === 0) {
                            var imageUrl = "${imgUrlPrefix}/image/getImage?imageId=" + imageId;

                            const range = editor.getSelection();
                            editor.insertEmbed(range.index, 'image', imageUrl);
                        } else {
                            windowToastManager.showToast('failed', "上传失败!");
                        }
                    })
                    .catch(error => {
                        windowToastManager.showToast('failed', "上传失败!");
                    });
            })
            .catch(error => {
                console.error('Error fetching image:', error);
            });
    }
</script>

</body>
</html>
