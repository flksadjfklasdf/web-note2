<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>文档</title>
    <%@include file="t-link.jsp" %>
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/css/document.css" rel="stylesheet">
</head>
<body class="bg-body-tertiary">
<%@include file="t-header.jsp" %>
<%@include file="t-toast.jsp" %>
<main>
    <div class="album py-5" style="margin-top: 50px">

        <div class="container">
            <div>
                <ul class="nav nav-tabs" id="myTabs" role="tablist">
                    <li class="nav-item" role="presentation">
                        <a class="nav-link active" id="my-document-tab" data-bs-toggle="tab" href="#my-document"
                           role="tab"
                           aria-controls="my-document" aria-selected="true">我的文档</a>
                    </li>
                    <li class="nav-item" role="presentation" style="display: none">
                        <a class="nav-link" id="like-tab" data-bs-toggle="tab" href="#like-document" role="tab"
                           aria-controls="like" aria-selected="false">收藏文档</a>
                    </li>
                </ul>
            </div>
            <div id="my-co" class="tab-content" style="padding-top: 15px">
                <div class="tab-pane container-fluid show active" id="my-document" role="tabpanel" aria-labelledby="overview-tab">
                    <div id="co-list" class="row row-cols-1 row-cols-md-3 g-3">

                        <div class="col" style="width: 420px;min-width: 420px;">
                            <div class="card shadow-sm">
                                <div class="card-body d-flex flex-column align-items-center justify-content-center" style="height: 162px">
                                    <a class="create-document-link" @click="createDocument"><i class="bi bi-plus-circle-dotted" style="font-size: 5em;"></i></a>
                                </div>
                            </div>
                        </div>
                        <!--渲染开始-->
                        <div v-for="item in collections" :key="item.collectionId" class="col" style="width: 420px;min-width: 420px">
                            <div class="card shadow-sm">
                                <div class="card-body d-flex flex-column">
                                    <h4 style="font-family: 'SimHei', sans-serif;" class="text-truncate">{{ item.collectionName }}</h4>
                                    <div class="description">
                                        <p style="font-family: 'SimSun', sans-serif;" class="card-text text-truncate">{{ item.collectionDescription }}</p>
                                    </div>
                                    <div class="d-flex justify-content-between align-items-center mt-2">
                                        <div class="btn-group">
                                            <a :href="`${pageContext.request.contextPath}/rc/\${item.collectionId}.html`" target="_blank" class="btn btn-sm btn-outline-secondary">浏览</a>
                                            <a :href="`${pageContext.request.contextPath}/edit/\${item.collectionId}.html`" class="btn btn-sm btn-outline-secondary">编辑</a>
                                            <a :href="`${pageContext.request.contextPath}/collection/export?collectionId=\${item.collectionId}`" class="btn btn-sm btn-outline-secondary">导出</a>
                                            <a @click="deleteDocument(item.collectionId, item.collectionName)" class="btn btn-sm btn-outline-danger">删除</a>
                                        </div>
                                        <span class="badge bg-success">{{ item.collectionType }}</span>
                                        <a @click="modifyDocument(item.collectionId)" class="badge btn bg-info">{{ item.isPublic ? 'public' : 'private' }}</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!--渲染结束-->
                    </div>
                </div>
                <!--删除文档-->
                <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="deleteModalLabel">确认删除</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="deleteModalBody">
                                确定要删除'{{ de_collection.collectionName }}'文档吗？
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-danger" @click="doDeleteDocument">确定删除</button>
                            </div>
                        </div>
                    </div>
                </div>
                <!--创建文档-->
                <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true" >
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div style="margin: 20px">
                                <label for="name" class="form-label">文档名称</label>
                                <input type="text" class="form-control" id="name" name="collectionName" v-model="cr_collection.collectionName">

                                <label for="description" class="form-label">描述</label>
                                <input type="text" class="form-control" id="description" name="collectionDescription" v-model="cr_collection.collectionDescription">

                                <label class="form-label">文档类型</label>
                                <br/>

                                <input type="radio" class="btn-check" name="options-base" id="option5" autocomplete="off" value="markdown" v-model="cr_collection.collectionType">
                                <label class="btn" for="option5">markdown</label>

                                <input type="radio" class="btn-check" name="options-base" id="option6" autocomplete="off" value="html" v-model="cr_collection.collectionType">
                                <label class="btn" for="option6">html</label>


                                <br/><br/>
                                <input class="form-check-input" type="checkbox" value="" id="defaultCheck1" checked v-model="cr_collection.isPublic">
                                <label class="form-check-label" for="defaultCheck1">
                                    公开
                                </label>
                                <br/><br/>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                <button class="btn btn-danger" @click="doCreateDocument">创建</button>
                            </div>
                        </div>
                    </div>
                </div>
                <!--更改文档信息-->
                <div class="modal fade" id="modifyModal" tabindex="-1" aria-labelledby="modifyModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div style="margin: 20px">
                                <label for="m-name" class="form-label">名称</label>
                                <input type="text" class="form-control" id="m-name" name="collectionName" v-model="mo_collection.collectionName">

                                <label for="m-description" class="form-label">描述</label>
                                <input type="text" class="form-control" id="m-description" name="collectionDescription" v-model="mo_collection.collectionDescription">

                                <br/>
                                <input class="form-check-input" type="checkbox" value="" id="m-defaultCheck1" name="isPublic" v-model="mo_collection.isPublic">
                                <label class="form-check-label" for="m-defaultCheck1">
                                    公开
                                </label>
                                <br/><br/>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                <button type="submit" class="btn btn-danger" @click="doModifyDocument">更改</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--收藏-->
            <div class="tab-content">
                <div class="tab-pane container-fluid" id="like-document" role="tabpanel" aria-labelledby="overview-tab">
                    <div class="row row-cols-1 row-cols-md-3 g-3">
                        <div class="col"  style="min-width: 370px">
                            <div class="card shadow-sm">
                                <div class="card-body d-flex flex-column">
                                    <h4>文档1</h4>
                                    <div class="description">
                                        <p class="card-text">这是文档1的fafasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsadfsadfasfsadfasdfasdf</p>
                                    </div>
                                    <div class="d-flex justify-content-between align-items-center mt-2">
                                        <div class="btn-group">
                                            <a class="btn btn-sm btn-outline-secondary">浏览</a>
                                            <a class="btn btn-sm btn-outline-secondary">导出</a>
                                            <a href="javascript:removeDocument('abcdefg')" class="btn btn-sm btn-outline-warning">移除</a>
                                        </div>
                                        <span class="badge bg-success">html</span>
                                        <span class="badge bg-danger">我</span>
                                        <small class="text-body-secondary">9 分钟之前</small>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col"  style="min-width: 370px">
                            <div class="card shadow-sm">
                                <div class="card-body d-flex flex-column">
                                    <h4>文档1</h4>
                                    <div class="description">
                                        <p class="card-text">这是文档1的fafasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsadfsadfasfsadfasdfasdf</p>
                                    </div>
                                    <div class="d-flex justify-content-between align-items-center mt-2">
                                        <div class="btn-group">
                                            <a class="btn btn-sm btn-outline-secondary">浏览</a>
                                            <a class="btn btn-sm btn-outline-secondary">导出</a>
                                            <a href="javascript:removeDocument('abcdefg')" class="btn btn-sm btn-outline-warning">移除</a>
                                        </div>
                                        <span class="badge bg-success">html</span>
                                        <span class="badge bg-danger">我</span>
                                        <small class="text-body-secondary">9 分钟之前</small>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col"  style="min-width: 370px">
                            <div class="card shadow-sm">
                                <div class="card-body d-flex flex-column">
                                    <h4>文档1</h4>
                                    <div class="description">
                                        <p class="card-text">这是文档1的fafasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfsadfsadfasfsadfasdfasdf</p>
                                    </div>
                                    <div class="d-flex justify-content-between align-items-center mt-2">
                                        <div class="btn-group">
                                            <a class="btn btn-sm btn-outline-secondary">浏览</a>
                                            <a class="btn btn-sm btn-outline-secondary">导出</a>
                                            <a href="javascript:removeDocument('abcdefg')" class="btn btn-sm btn-outline-warning">移除</a>
                                        </div>
                                        <span class="badge bg-success">html</span>
                                        <span class="badge bg-danger">我</span>
                                        <small class="text-body-secondary">9 分钟之前</small>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="removeModal" tabindex="-1" aria-labelledby="removeModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="removeModalLabel">确认取消收藏</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="removeModalBody">
                                确定要取消收藏这个文档吗？
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-danger" onclick="doRemoveDocument()">确定移除</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</main>
<script>
    var baseUrl='${pageContext.request.contextPath}';
</script>
<script>
    new Vue({
        el: '#my-co',
        data: {
            collections: [],
            mo_collection: {
                collectionName:'',
                collectionDescription:'',
                isPublic:false
            },
            cr_collection:{
                collectionName:'',
                collectionDescription:'',
                collectionType:'markdown',
                isPublic:false
            },
            de_collection:{
                collectionId:'',
                collectionName:''
            }
        },
        methods: {
            fetchData() {
                axios.post('${pageContext.request.contextPath}/collection/getAll')
                    .then(response => {
                        this.collections = response.data.data;
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        windowToastManager.showToast('failed',error)
                    });
            },
            deleteDocument(collectionId, collectionName) {

                this.de_collection={
                    collectionId:collectionId,
                    collectionName:collectionName
                }

                var modal = new bootstrap.Modal(document.getElementById('deleteModal'), {
                    keyboard: false
                });
                modal.show();
            },
            doDeleteDocument(){
                axios.post('${pageContext.request.contextPath}/collection/delete', this.de_collection)
                    .then(async (response) => {

                        console.log('Response:', response.data);
                        if(response.data.status === 0){
                            windowToastManager.showToast('success','删除成功')
                        }else{
                            windowToastManager.showToast('failed',response.data.message)
                        }

                        await this.fetchData();
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        windowToastManager.showToast('failed',error)
                    });
                var modal = bootstrap.Modal.getInstance(document.getElementById('deleteModal'));
                modal.hide();
            },
            modifyDocument(id) {
                const selectedCollection = this.collections.find(item => item.collectionId === id);

                this.mo_collection = { ...selectedCollection };

                var modal = new bootstrap.Modal(document.getElementById('modifyModal'), {
                    keyboard: false
                });
                modal.show();
            },
            doModifyDocument() {

                if(this.mo_collection.collectionName === ''){
                    windowToastManager.showToast('failed',"名称不允许为空")
                    return;
                }
                axios.post('${pageContext.request.contextPath}/collection/modify', this.mo_collection)
                    .then(async (response) => {

                        console.log('Response:', response.data);
                        await this.fetchData();
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });
                var modal = bootstrap.Modal.getInstance(document.getElementById('modifyModal'));
                modal.hide();
            },
            createDocument(){
                this.cr_collection={
                    collectionName:'',
                    collectionDescription:'',
                    collectionType:'markdown',
                    isPublic:false
                }

                var modal = new bootstrap.Modal(document.getElementById('createModal'), {
                    keyboard: false
                });
                modal.show();
            },
            doCreateDocument(){

                if(this.cr_collection.collectionName === ''){
                    windowToastManager.showToast('failed',"名称不允许为空")
                    return;
                }

                axios.post('${pageContext.request.contextPath}/collection/create', this.cr_collection)
                    .then(async (response) => {

                        console.log('Response:', response.data);

                        await this.fetchData();
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });
                var modal = bootstrap.Modal.getInstance(document.getElementById('createModal'));
                modal.hide();
            }
        },
        mounted() {
            this.fetchData();
        }
    });

</script>
</body>
</html>