<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>资源管理</title>
    <%@include file="t-link.jsp" %>
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/css/edit-md.css" rel="stylesheet">
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/css/resource.css" rel="stylesheet">
    <script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/resource.js"></script>
    <script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/qrcode.min.js"></script>
</head>
<body>
<%@include file="t-toast.jsp" %>
<div class="h-100 d-flex flex-column ">
    <%@include file="t-header.jsp" %>
    <div class="container" style="margin-top: 90px;padding-bottom: 30px">
        <ul class="nav nav-tabs" id="myTabs" role="tablist">
            <li class="nav-item" role="presentation">
                <a class="nav-link active" id="overview-tab" data-bs-toggle="tab" href="#overview" role="tab"
                   aria-controls="overview" aria-selected="true" onclick="ov.initOvData()">资源总览</a>
            </li>
            <li class="nav-item" role="presentation">
                <a class="nav-link" id="image-tab" data-bs-toggle="tab" href="#image" role="tab" aria-controls="image"
                   aria-selected="false">图片资源</a>
            </li>
            <li class="nav-item" role="presentation">
                <a class="nav-link" id="file-tab" data-bs-toggle="tab" href="#file" role="tab" aria-controls="file"
                   aria-selected="false">文件资源</a>
            </li>
            <li class="nav-item" role="presentation">
                <a class="nav-link" id="upload-tab" data-bs-toggle="tab" href="#upload" role="tab" aria-controls="file"
                   aria-selected="false">上传资源</a>
            </li>
        </ul>
        <div class="tab-content">
            <!-- 总览页面 -->
            <div class="tab-pane fade active show" id="overview" role="tabpanel" aria-labelledby="overview-tab">
                <div class="container mt-4">
                    <div class="row d-flex align-items-center justify-content-center">
                        <div style="width: 400px; height: 340px;" id="articleChart">

                        </div>

                    </div>
                    <div class="row d-flex align-items-center justify-content-center">
                        <div style="display: flex; flex-wrap: wrap; width: 600px;">
                            <div style="flex: 1 1 50%; box-sizing: border-box; padding: 10px;">
                                <p>文档共计: <span id="totalArticles" style="font-family: 'Courier New', Courier, monospace;">{{ ovData.totalArticles }}</span></p>
                            </div>
                            <div style="flex: 1 1 50%; box-sizing: border-box; padding: 10px;">
                                <p>图片共计: <span id="totalImages" style="font-family: 'Courier New', Courier, monospace;">{{ ovData.totalImages }}</span> 大小: <span id="totalImageSize" style="font-family: 'Courier New', Courier, monospace;">{{ convertSizeToResult(ovData.totalImagesSize) }}</span></p>
                            </div>
                            <div style="flex: 1 1 50%; box-sizing: border-box; padding: 10px;">
                                <p>文件共计: <span id="totalFiles" style="font-family: 'Courier New', Courier, monospace;">{{ ovData.totalFiles }}</span> 大小: <span id="totalFileSize" style="font-family: 'Courier New', Courier, monospace;">{{ convertSizeToResult(ovData.totalFilesSize) }}</span></p>
                            </div>
                            <div style="flex: 1 1 50%; box-sizing: border-box; padding: 10px;">
                                <p>已用空间: <span id="usedSpace" style="font-family: 'Courier New', Courier, monospace;">{{ convertSizeToResult(ovData.usedSpace) }}</span></p>
                            </div>
                            <div style="flex: 1 1 50%; box-sizing: border-box; padding: 10px;">
                                <p>空间额度: <span id="spaceLimit" style="font-family: 'Courier New', Courier, monospace;">{{ convertSizeToResult(ovData.spaceLimit) }}</span></p>
                            </div>
                        </div>
                    </div>


                </div>
            </div>
            <script>
                var ov = new Vue({
                    el: '#overview',
                    data: {
                        ovData: {
                            totalArticles: 0,
                            totalImages: 0,
                            totalFiles: 0,

                            totalImagesSize: 0,
                            totalFilesSize: 0,
                            usedSpace: 0,
                            spaceLimit: 0
                        },
                        articleChart: null
                    },
                    methods: {
                        initOvData() {
                            axios.post('${pageContext.request.contextPath}/file/resourceInfo')
                                .then(response => {
                                    if (response.data.status === 0) {
                                        this.ovData = response.data.data;

                                        const tis = this.ovData.totalImagesSize;
                                        const tfs = this.ovData.totalFilesSize;
                                        const tls = this.ovData.spaceLimit;

                                        console.log(`tis:\${tis}tfs:\${tfs}tls\${tls}`)

                                        const chartDom = document.getElementById('articleChart');
                                        const myChart = echarts.init(chartDom);
                                        let option;

                                        option = {
                                            tooltip: {
                                                trigger: 'item'
                                            },
                                            legend: {
                                                top: '5%',
                                                left: 'center'
                                            },
                                            series: [
                                                {
                                                    name: '用户空间',
                                                    type: 'pie',
                                                    radius: ['40%', '70%'],
                                                    avoidLabelOverlap: false,
                                                    itemStyle: {
                                                        borderRadius: 5,
                                                        borderColor: '#fff',
                                                        borderWidth: 2
                                                    },
                                                    label: {
                                                        show: false,
                                                        position: 'center'
                                                    },
                                                    emphasis: {
                                                        label: {
                                                            show: true,
                                                            fontSize: 40,
                                                            fontWeight: 'bold'
                                                        }
                                                    },
                                                    labelLine: {
                                                        show: false
                                                    },
                                                    data: [
                                                        {value: tls - tis - tfs, name: '可用空间', itemStyle: {color: '#67C23A'}}, // 可用空间 - 绿色
                                                        {value: tfs, name: '文件占用', itemStyle: {color: '#F56C6C'}}, // 文件占用 - 红色
                                                        {value: tis, name: '图片占用', itemStyle: {color: '#F0A340'}},
                                                    ]
                                                }
                                            ]
                                        };

                                        option && myChart.setOption(option);
                                    } else {
                                        windowToastManager.showToast('failed', '请求文件列表失败!')
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求文件列表失败!')
                                });
                        },
                        convertSizeToResult(sizeInKB) {
                            if (sizeInKB < 0) {
                                return "N/A";
                            }

                            let result;
                            if (sizeInKB < 1024) {
                                result = sizeInKB + "KB";
                            } else if (sizeInKB < 1024 * 1024) {
                                result = (sizeInKB / 1024).toFixed(2) + "MB";
                            } else if (sizeInKB < 1024 * 1024 * 1024) {
                                result = (sizeInKB / (1024 * 1024)).toFixed(2) + "GB";
                            } else if (sizeInKB < 1024 * 1024 * 1024 * 1024) {
                                result = (sizeInKB / (1024 * 1024 * 1024)).toFixed(2) + "TB";
                            } else {
                                return;
                            }

                            return result;
                        },

                    },
                    mounted() {
                        this.initOvData();
                    }

                });


            </script>
            <!-- 图片页面 -->
            <div class="tab-pane fade" id="image" role="tabpanel" aria-labelledby="image-tab">
                <nav style="--bs-breadcrumb-divider: '>'; margin: 20px" aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li v-if="r1.name !== ''" class="breadcrumb-item v-link" aria-current="page">
                            <span @click="loadRoot" style="text-decoration: none">{{ r1.name }}</span>
                        </li>
                        <li v-if="r2.name !== ''" class="breadcrumb-item v-link" aria-current="page">
                            <span @click="loadCollection(r2.collectionId,r2.name)" style="text-decoration: none">{{ r2.name }}</span>
                        </li>
                        <li v-if="r3.name !== ''" class="breadcrumb-item v-link" aria-current="page">
                            <span @click="loadDocument(r3.documentId,r3.name)" style="text-decoration: none">{{ r3.name }}</span>
                        </li>
                    </ol>
                </nav>

                <ul class="list-group">


                    <li v-if="type === 1" v-for="item in items" class="list-group-item bg-body-secondary rounded-3">
                        <div class="d-flex justify-content-between ">
                            <button class="btn" @click="loadCollection(item.collectionId,item.collectionName)">
                                {{ item.collectionName }}
                            </button>
                            <div>
                                <span class="badge bg-primary">共计:{{ item.imageCount }}</span>
                                <span class="badge bg-success">大小: {{ item.sizeCount }}KB</span>
                                <button type="button" class="btn btn-outline-danger btn-sm ms-2"
                                        @click="deleteImg(1,item.collectionId,item.imageCount)">全部删除
                                </button>
                            </div>
                        </div>
                    </li>
                    <li v-if="type === 2" v-for="item in items" class="list-group-item bg-body-secondary rounded-3">
                        <div class="d-flex justify-content-between ">
                            <button class="btn" @click="loadDocument(item.documentId,item.documentName)">
                                {{ item.documentName }}
                            </button>
                            <div>
                                <span class="badge bg-primary">共计:{{ item.imageCount }}</span>
                                <span class="badge bg-success">大小: {{ item.sizeCount }}KB</span>
                                <button type="button" class="btn btn-outline-danger btn-sm ms-2"
                                        @click="deleteImg(2,item.documentId,item.imageCount)">全部删除
                                </button>
                            </div>
                        </div>
                    </li>
                    <li v-if="type === 3" v-for="item in items" class="list-group-item bg-body-secondary rounded-3">
                        <div class="d-flex justify-content-between ">
                            <button class="btn" @click="open(item.imageId)">
                                {{ item.imageName }}
                            </button>
                            <div>
                                <span class="badge bg-primary">创建时间:{{ formatJavaDate(item.imageCreateTime) }}</span>
                                <span class="badge bg-success">大小: {{ item.size }} KB</span>
                                <button type="button" class="btn btn-outline-info btn-sm ms-2"
                                        @mouseover="showPreview($event, item.imageId)" @mouseout="hidePreview()">预览
                                </button>
                                <button type="button" class="btn btn-outline-danger btn-sm ms-2"
                                        @click="deleteImg(3,item.imageId)">删除
                                </button>
                            </div>
                        </div>
                    </li>
                </ul>
                <!-- 删除图片 -->
                <div class="modal fade" id="deleteImage" tabindex="-1" aria-labelledby="deleteImageLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="deleteImageLabel">确认删除</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="deleteModalBody">
                                确定要删除这{{ count }}张图片吗？
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-danger" @click="doDeleteImg">确定删除</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <script>
                var x = new Vue({
                    el: '#image',
                    data: {
                        r1: {
                            name: '我的合集'
                        },
                        r2: {
                            name: '',
                            collectionId: ''
                        },
                        r3: {
                            name: '',
                            documentId: ''
                        },
                        items: [],
                        type: 1,
                        deleteData: {
                            type: 1,
                            itemId: '',
                        },
                        count: ''
                    },
                    methods: {
                        fetchData() {
                            if (this.type === 1) {
                                this.loadRoot()
                            } else if (this.type === 2) {
                                this.loadCollection(this.r2.collectionId)
                            } else {
                                this.loadDocument(this.r3.documentId)
                            }
                        },
                        loadRoot() {
                            axios.post('${pageContext.request.contextPath}/image/getAllCollectionsImages')
                                .then(async (response) => {
                                    if (response.data.status === 0) {
                                        this.items = response.data.data
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    windowToastManager.showToast('failed', error)
                                });
                            this.type = 1
                            this.r2 = {
                                name: '',
                                collectionId: ''
                            }
                            this.r3 = {
                                name: '',
                                documentId: ''
                            }

                        },
                        loadCollection(collectionId, collectionName) {
                            var data = {
                                collectionId: collectionId
                            }
                            axios.post('${pageContext.request.contextPath}/image/getImagesInCollection', data)
                                .then(async (response) => {
                                    if (response.data.status === 0) {
                                        this.items = response.data.data
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    windowToastManager.showToast('failed', error)
                                });
                            this.type = 2
                            this.r2 = {
                                name: collectionName,
                                collectionId: collectionId
                            }
                            this.r3 = {
                                name: '',
                                documentId: ''
                            }
                        },
                        loadDocument(documentId, documentName) {
                            var data = {
                                documentId: documentId
                            }
                            axios.post('${pageContext.request.contextPath}/image/getImagesInDocument', data)
                                .then(async (response) => {
                                    if (response.data.status === 0) {
                                        this.items = response.data.data
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    windowToastManager.showToast('failed', error)
                                });
                            this.type = 3

                            this.r3 = {
                                name: documentName,
                                documentId: documentId
                            }
                        },
                        showPreview(event, imageId) {
                            const imagePath = '${pageContext.request.contextPath}/image/getImage?imageId=' + imageId;

                            const previewImage = document.createElement('img');
                            previewImage.src = imagePath;
                            previewImage.style.position = 'absolute';

                            previewImage.style.width = '200px';
                            previewImage.style.height = 'auto';

                            const mouseX = event.clientX;
                            const mouseY = event.clientY;
                            previewImage.style.top = mouseY + 10 + 'px';
                            previewImage.style.left = mouseX - 210 + 'px';

                            previewImage.classList.add('preview-image');

                            document.body.appendChild(previewImage);
                        },
                        hidePreview() {
                            // 移除预览图元素
                            const previewImage = document.querySelector('.preview-image');
                            if (previewImage) {
                                document.body.removeChild(previewImage);
                            }
                        },
                        open(id) {
                            window.open('${pageContext.request.contextPath}/image/getImage?imageId=' + id);
                        },
                        deleteImg(type, id, count) {
                            this.deleteData.type = type;
                            this.deleteData.itemId = id;

                            if (type !== 3) {
                                this.count = count + "";
                            } else {
                                this.count = "";
                            }

                            var modal = new bootstrap.Modal(document.getElementById('deleteImage'), {
                                keyboard: false
                            });
                            modal.show();
                        },
                        doDeleteImg() {
                            var modal = bootstrap.Modal.getInstance(document.getElementById('deleteImage'));
                            modal.hide();

                            axios.post('${pageContext.request.contextPath}/image/deleteImage', this.deleteData)
                                .then(async (response) => {

                                    await this.fetchData();
                                    if (response.data.status === 0) {
                                        windowToastManager.showToast('success', '删除成功!')
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    windowToastManager.showToast('failed', error)
                                });
                        },
                        formatJavaDate(javaDate) {
                            const date = new Date(javaDate + '');
                            const year = date.getFullYear();
                            const month = String(date.getMonth() + 1).padStart(2, '0');
                            const day = String(date.getDate()).padStart(2, '0');
                            return year + '-' + month + '-' + day;
                        },
                    },
                    mounted() {
                        this.fetchData();
                    }
                })
            </script>
            <!-- 资源页面 -->
            <div class="tab-pane fade" id="file" role="tabpanel" aria-labelledby="file-tab">

                <div>
                    <!-- Button to Toggle Search Area -->
                    <div @click="toggleSearch" class="d-flex align-items-center" style="cursor: pointer;">
                        <hr class="flex-grow-1 hr-hover" />
                        <i :class="isSearchVisible ? 'bi bi-chevron-up' : 'bi bi-chevron-down'" style="font-size: 18px;margin: 0 10px;"></i>
                        <span>搜索</span>
                    </div>

                    <!-- Search Area (Initially Hidden) -->
                    <div v-show="isSearchVisible" class="mb-3">
                        <!-- First Row -->
                        <div class="row mb-3">
                            <div class="col-2">
                                <input type="text" class="form-control" placeholder="文件名" v-model="searchData.fileName" @input="handleSearch" spellcheck="false">
                            </div>
                            <div class="col-1">
                                <select class="form-select" v-model="searchData.fileType" @change="handleSearch">
                                    <option value="">任意</option>
                                    <option v-for="type in fileTypes" :value="type.typeField">{{ type.typeName }}</option>
                                </select>
                            </div>
                            <div class="col-3">
                                <div class="d-flex">
                                    <label for="startDate" style="margin-top: 9px;margin-right: 10px">开始日期</label>
                                    <input id="startDate" type="date" class="form-control" v-model="searchData.startTime" @change="handleSearch" style="width: 200px">
                                </div>
                            </div>
                            <div class="col-3">
                                <div class="d-flex">
                                    <label for="endDate" style="margin-top: 9px;margin-right: 10px">结束日期</label>
                                    <input id="endDate" type="date" class="form-control" v-model="searchData.endTime" @change="handleSearch" style="width: 200px">
                                </div>
                            </div>
                            <div class="col-2">
                                <select class="form-select" v-model="searchData.pageSize" @change="handleSearch">
                                    <option value="10">10条/页</option>
                                    <option value="15">15条/页</option>
                                    <option value="20">20条/页</option>
                                    <option value="30">30条/页</option>
                                    <option value="50">50条/页</option>
                                </select>
                            </div>
                        </div>
                        <!-- Second Row -->
                        <div class="row mb-3">
                            <div class="col-3">
                            </div>
                            <div class="col-3">
                                <div class="d-flex">
                                    <label for="minSize" style="margin-top: 6px;margin-right: 10px">最小MB</label>
                                    <input id="minSize" type="number" class="form-control" placeholder="最小MB" v-model="searchData.minSize" @input="handleSearch" style="width: 150px">
                                </div>
                            </div>
                            <div class="col-3">
                                <div class="d-flex">
                                    <span for="maxSize" style="margin-top: 6px;margin-right: 10px">最大MB</span>
                                    <input id="maxSize" type="number" class="form-control" placeholder="最大MB" v-model="searchData.maxSize" @input="handleSearch" style="width: 150px">
                                </div>
                            </div>
                            <div class="col-2">
                                <button class="btn btn-secondary me-2" @click="resetSearchData">重置</button>
                                <button class="btn btn-primary" @click="doSearch">搜索</button>
                            </div>
                        </div>
                    </div>
                </div>













                <!-- 查询列表 -->
                <table class="table border border-1 border-secondary-subtle table-hover">
                    <thead>
                    <tr class="table-secondary">
                        <th class="col-4 sortable">名称</th>
                        <th class="col-1 sortable">文件类型</th>
                        <th class="col-2 sortable">上传日期</th>
                        <th class="col-2 sortable">占用大小</th>
                        <th class="col-1">权限</th>
                        <th class="col-2">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="file in files">
                        <td style="max-width: 400px;overflow: hidden">
                            <a :href="`${pageContext.request.contextPath}/file/d/\${file.fileId}`"
                               target="_blank"
                               class="link-s"
                               :title="convertTitle(file.originalFilename,file.fileNote,file.fileMd5)"
                               style="white-space: nowrap;"
                               v-html="highlightKeyword(file.originalFilename)">
                            </a>
                        </td>
                        <td>{{ file.fileTypeName }}</td>
                        <td>{{ formatJavaDate(file.uploadedAt) }}</td>
                        <td>{{ convertSizeToResult(file.fileSize) }}</td>
                        <td>{{ translatePermission(file.permission) }}</td>
                        <td>
                            <button class="btn btn-sm btn-outline-primary"
                                    @click="modifyFilePermission(file.fileId,file.permission,file.authCode,file.fileNote,file.fileType)">
                                修改
                            </button>
                            <button class="btn btn-sm btn-outline-danger"
                                    @click="deleteFile(file.fileId,file.originalFilename)">删除
                            </button>
                            <button class="btn btn-sm btn-outline-primary"
                                    @click="visitResource(file.fileId, file.isAuthCode, file.authCode, file.originalFilename, convertSizeToResult(file.fileSize), file.fileMd5)">查看
                            </button>
                        </td>
                    </tr>

                    </tbody>
                </table>
                <!-- 分页 -->
                <nav aria-label="Page navigation">
                    <ul class="pagination" style=" display: flex;justify-content: center;">
                        <li class="page-item" :class="{ 'disabled': currentPage === 1 }">
                            <a class="page-link" @click="lastPage" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>

                        <!-- 显示第一页 -->
                        <li v-if="currentPage !== 1" class="page-item" :class="{ 'active': 1 === currentPage }">
                            <a class="page-link" @click="gotoPage(1)">1</a>
                        </li>

                        <!-- 显示前两页省略号 -->
                        <li v-if="currentPage > 5" class="page-item disabled"><span class="page-link">...</span>
                        </li>
                        <li v-if="currentPage === 5" class="page-item">
                            <a class="page-link" @click="gotoPage(2)">2</a>
                        </li>

                        <!-- 显示当前页前两页 -->
                        <li v-for="page in [currentPage - 2, currentPage - 1]" :key="page" v-if="page > 1"
                            class="page-item">
                            <a class="page-link" @click="gotoPage(page)">{{ page }}</a>
                        </li>

                        <!-- 显示当前页 -->
                        <li class="page-item active">
                            <a class="page-link" @click="gotoPage(currentPage)">{{ currentPage }}</a>
                        </li>

                        <!-- 显示当前页后两页 -->
                        <li v-for="page in [currentPage + 1, currentPage + 2]" :key="page" v-if="page <= totalPages"
                            class="page-item">
                            <a class="page-link" @click="gotoPage(page)">{{ page }}</a>
                        </li>

                        <!-- 显示后两页省略号 -->
                        <li v-if="currentPage < totalPages - 4" class="page-item disabled"><span class="page-link">...</span>
                        </li>
                        <li v-if="currentPage === totalPages - 4" class="page-item">
                            <a class="page-link" @click="gotoPage(totalPages-1)">{{ totalPages-1 }}</a>
                        </li>

                        <!-- 显示最后一页 -->
                        <li v-if="currentPage !== totalPages && totalPages >= 2 && (totalPages-currentPage) >2"
                            class="page-item"
                            :class="{ 'active': totalPages === currentPage }">
                            <a class="page-link" @click="gotoPage(totalPages)">{{ totalPages }}</a>
                        </li>

                        <li class="page-item" :class="{ 'disabled': currentPage === totalPages }">
                            <a class="page-link" @click="nextPage" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </ul>
                </nav>
                <!-- 访问控制 -->
                <div class="modal fade" id="visitControl" tabindex="-1" aria-labelledby="visitControlLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div id="modifyForm" style="padding-top: 30px">
                                <!-- 文件备注 -->
                                <div class="mb-3 w-75 mx-auto">
                                    <label for="m-fileNote" class="form-label">文件备注</label>
                                    <input type="text" class="form-control" id="m-fileNote" name="fileNote"
                                           v-model="modify_file.fileNote" autocomplete="off">
                                </div>

                                <!-- 文件类型（下拉框） -->
                                <div class="mb-3 w-75 mx-auto">
                                    <label for="m-fileType" class="form-label">文件类型</label>
                                    <i class="bi bi-info-circle"
                                       title="该字段决定了浏览器从链接访问该资源的效果,例如,一个图片如果选择为图片类型,那么你能在浏览器预览该资源,而非作为文件下载,如果选择默认,那么文件默认行为是下载"></i>
                                    <select class="form-select" id="m-fileType" name="fileType"
                                            v-model="modify_file.fileType">
                                        <option value="default">默认</option>
                                        <c:forEach var="fileType" items="${fileTypeList}">
                                            <option value="<c:out value='${fileType.typeField}'/>">
                                                <c:out value="${fileType.typeName}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="mb-3 w-75 mx-auto">
                                    <div class="form-check">
                                        <input type="checkbox" class="form-check-input" id="m-publicCheckbox"
                                               onchange="modifyTogglePublicOptions()" v-model="modify_file.isPublic">
                                        <label class="form-check-label" for="m-publicCheckbox">公开</label>
                                    </div>
                                </div>

                                <div id="m-publicOptions">
                                    <div class="mb-3 w-75 mx-auto">
                                        <div class="form-check">
                                            <input type="checkbox" class="form-check-input" id="m-codeAccessCheckbox"
                                                   onchange="modifyTogglePublicOptions()"
                                                   v-model="modify_file.isAuthCode">
                                            <label class="form-check-label"
                                                   for="m-codeAccessCheckbox">授权码访问</label>
                                        </div>
                                    </div>

                                    <div id="m-codeAccessOptions" style="display: none;">
                                        <div class="mb-3 w-75 mx-auto">
                                            <label for="m-authorizationCode" class="form-label">设置授权码</label>
                                            <input type="text" class="form-control" id="m-authorizationCode"
                                                   name="authorizationCode" v-model="modify_file.authCode">
                                        </div>
                                    </div>
                                </div>

                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                                    </button>
                                    <button class="btn btn-danger" @click="doModifyFilePermission">更改
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 删除控制 -->
                <div class="modal fade" id="deleteFileResource" tabindex="-1" aria-labelledby="deleteFileResourceLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="deleteFileResourceLabel">确认删除</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="deleteResourceBody" style="word-break: break-all;">
                                确定要删除 `<span style="font-family: 'Consolas', monospace;">{{ delete_file.fileName }}</span>` 文件吗？
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-danger" @click="doDeleteFile">确定删除
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 显示文件 -->
                <div class="modal fade" id="qrcodeModal" tabindex="-1" aria-labelledby="qrcodeModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="qrcodeModalLabel">文件信息</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <!-- 用于展示二维码的容器 -->
                                <div id="qrcode-container" style="padding-left:105px"></div>
                                <!-- 用于展示文件信息的容器 -->
                                <div style="margin-top: 15px;font-family: 'Consolas', monospace;">
                                    <p style="word-break: break-all;"><strong>文件名称:</strong> {{ visit_file.originalFilename }}</p>
                                    <p><strong>文件大小:</strong> {{ visit_file.convertedFileSize }}</p>
                                    <p><strong>文件哈希值:</strong> {{ visit_file.fileMd5 }}</p>
                                    <p v-if="visit_file.isAuthCode"><strong>访问授权码:</strong> {{ visit_file.authCode }}</p>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <script>
                var fi = new Vue({
                    el: '#file',

                    data: {
                        isSearchVisible: false,
                        searchData: {
                            page: 1,
                            pageSize:10,
                            fileName:'',
                            fileType:'',
                            startTime:'',
                            endTime:'',
                            minSize:0,
                            maxSize:9999999
                        },
                        currentPage: 1,
                        totalPages: 1,


                        files: [],
                        modify_file: {
                            fileId: '',
                            isPublic: true,
                            isAuthCode: false,
                            authCode: '',
                            fileNote: '',
                            fileType: ''

                        },
                        delete_file: {
                            fileId: '',
                            fileName: ''
                        },
                        visit_file: {
                            fileId: '',
                            isAuthCode: false,
                            authCode: '',
                            originalFilename: '',
                            convertedFileSize: '',
                            fileMd5: '',
                        },
                        fileTypes:null,
                        debouncedSearch: null
                    },
                    methods: {
                        fetchData() {
                            this.doSearch()
                        },
                        modifyFilePermission(fileId, permission, authCode, fileNote, fileType) {
                            this.modify_file.fileId = fileId
                            this.modify_file.isPublic = permission === 0
                            this.modify_file.isAuthCode = permission === 1
                            this.modify_file.authCode = authCode
                            this.modify_file.fileNote = fileNote
                            this.modify_file.fileType = fileType

                            modifyTogglePublicOptionsVue(permission === 0, permission === 1)

                            var modal = new bootstrap.Modal(document.getElementById('visitControl'), {
                                keyboard: false
                            });
                            modal.show();
                        },
                        doModifyFilePermission() {
                            if (this.modify_file.isPublic === false && this.modify_file.isAuthCode === true && (this.modify_file.authCode === '' || this.modify_file.authCode === null)) {
                                windowToastManager.showToast('failed', '请输入授权码!')
                                return
                            }

                            var modal = bootstrap.Modal.getInstance(document.getElementById('visitControl'));
                            modal.hide();

                            axios.post('${pageContext.request.contextPath}/file/modify', this.modify_file)
                                .then(async (response) => {

                                    await this.fetchData();
                                    if (response.data.status === 0) {
                                        windowToastManager.showToast('success', '更改成功!')
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    windowToastManager.showToast('failed', error)
                                });
                        },
                        deleteFile(fileId, originalFilename) {
                            this.delete_file.fileId = fileId
                            this.delete_file.fileName = originalFilename

                            var modal = new bootstrap.Modal(document.getElementById('deleteFileResource'), {
                                keyboard: false
                            });
                            modal.show();
                        },
                        doDeleteFile() {
                            var modal = bootstrap.Modal.getInstance(document.getElementById('deleteFileResource'));
                            modal.hide();

                            axios.post('${pageContext.request.contextPath}/file/delete', this.delete_file)
                                .then(async (response) => {

                                    await this.fetchData();
                                    if (response.data.status === 0) {
                                        windowToastManager.showToast('success', '删除成功!')
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    windowToastManager.showToast('failed', error)
                                });
                        },
                        visitResource(fileId, isAuthCode, authCode, originalFilename, convertedFileSize, fileMd5) {
                            this.visit_file = {
                                fileId: fileId,
                                isAuthCode: isAuthCode,
                                authCode: authCode,
                                originalFilename: originalFilename,
                                convertedFileSize: convertedFileSize,
                                fileMd5: fileMd5
                            }
                            // 获取二维码容器元素
                            var qrcodeContainer = document.getElementById('qrcode-container');

                            var urlSuffix = authCode ? "?authCode=" + authCode : ""

                            qrcodeContainer.innerHTML = '';

                            var qrcode = new QRCode(qrcodeContainer, {
                                text: `${requestUrlPrefix}/file/d/\${fileId}\${urlSuffix}`,
                                width: 256,
                                height: 256
                            });

                            // 打开独立的生成二维码窗口
                            $('#qrcodeModal').modal('show');
                        },
                        formatJavaDate(javaDate) {
                            const date = new Date(javaDate);
                            const year = date.getFullYear();
                            const month = String(date.getMonth() + 1).padStart(2, '0');
                            const day = String(date.getDate()).padStart(2, '0');
                            const hours = String(date.getHours()).padStart(2, '0');
                            const minutes = String(date.getMinutes()).padStart(2, '0');
                            return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes;
                        },
                        translatePermission(value) {
                            switch (value) {
                                case 0:
                                    return '公开';
                                case 1:
                                    return '授权码';
                                case 2:
                                    return '私有';
                                default:
                                    return value;
                            }
                        },
                        convertSizeToResult(sizeInKB) {
                            if (sizeInKB <= 0) {
                                return "N/A";
                            }

                            let result;
                            if (sizeInKB < 1024) {
                                result = sizeInKB + "KB";
                            } else if (sizeInKB < 1024 * 1024) {
                                result = (sizeInKB / 1024).toFixed(2) + "MB";
                            } else if (sizeInKB < 1024 * 1024 * 1024) {
                                result = (sizeInKB / (1024 * 1024)).toFixed(2) + "GB";
                            } else if (sizeInKB < 1024 * 1024 * 1024 * 1024) {
                                result = (sizeInKB / (1024 * 1024 * 1024)).toFixed(2) + "TB";
                            } else {
                                return;
                            }

                            return result;
                        },
                        convertTitle(fileName, fileNote, fileMD5) {
                            return "文件名:" + fileName + "\n备注:  " + fileNote + "\nMD5: " + fileMD5
                        },
                        doSearch() {
                            this.validateSearchData();
                            axios.post('${pageContext.request.contextPath}/file/search', this.searchData)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        this.files = response.data.data.files;

                                        var pages = response.data.data.totalPages
                                        //如果页码有变化
                                        if (this.totalPages !== pages) {
                                            //如果超出总页数大小,那么需要跳转当前页码为最大页码
                                            var oldPage = this.currentPage
                                            this.currentPage = Math.min(this.currentPage, pages)
                                            //跳转页码需要重新加载新页码的用户数据
                                            if (oldPage !== this.currentPage) {
                                                this.searchData.page = this.currentPage
                                                this.doSearch()
                                            }
                                        }
                                        this.totalPages = pages
                                    } else {
                                        windowToastManager.showToast('failed', '请求文件列表失败!')
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求文件列表失败!')
                                });
                        },
                        nextPage() {
                            if (this.currentPage < this.totalPages) {
                                this.goToPage(this.currentPage + 1);
                            }
                        },
                        lastPage() {
                            if (this.currentPage > 1) {
                                this.goToPage(this.currentPage - 1);
                            }
                        },
                        gotoPage(page) {
                            if (page >= 1 && page <= this.totalPages && page !== this.currentPage) {
                                this.goToPage(page);
                            }
                        },
                        goToPage(page) {
                            this.currentPage = page;
                            this.searchData.page = page;

                            this.doSearch()
                        },
                        toggleSearch() {
                            this.isSearchVisible = !this.isSearchVisible;
                        },
                        resetSearchData(){
                            this.searchData={
                                page: this.searchData.page,
                                pageSize:10,
                                fileName:'',
                                fileType:'',
                                startTime:'',
                                endTime:'',
                                minSize:0,
                                maxSize:9999999
                            }

                            this.doSearch()
                        },
                        validateSearchData(){
                            if(this.searchData.minSize == null || this.searchData.minSize === ''){
                                this.searchData.minSize = 0
                            }
                            if(this.searchData.maxSize == null || this.searchData.maxSize === ''){
                                this.searchData.maxSize = 99999999999
                            }
                        },
                        handleSearch() {
                            this.debouncedSearch();
                        },
                        highlightKeyword(fileName) {
                            const keyword = this.searchData.fileName; // 获取搜索关键字
                            if (!keyword || keyword === '') {
                                return fileName; // 如果关键字为空，返回原文件名
                            }

                            let highlightedFileName = '';
                            let position = 0;
                            let lowerFileName = fileName.toLowerCase(); // 创建小写版本的文件名用于查找
                            let lowerKeyword = keyword.toLowerCase();   // 创建小写版本的关键字用于查找
                            let index;

                            // 在整个文件名中查找关键字（忽略大小写）
                            while ((index = lowerFileName.indexOf(lowerKeyword, position)) !== -1) {
                                // 添加从当前位置到找到关键字前的内容
                                highlightedFileName += fileName.slice(position, index);
                                // 添加高亮后的关键字，保留原始大小写
                                highlightedFileName += `<span style="color: red;background-color: #ffdfdf">\${fileName.slice(index, index + keyword.length)}</span>`;
                                // 更新位置到关键字之后
                                position = index + keyword.length;
                            }

                            // 添加剩余部分（如果有的话）
                            highlightedFileName += fileName.slice(position);

                            return highlightedFileName;
                        }
                    },

                    mounted() {
                        axios.post('${pageContext.request.contextPath}/file/types')
                            .then(async (response) => {
                                await this.fetchData();
                                if (response.data.status === 0) {
                                    this.fileTypes = response.data.data
                                } else {
                                    windowToastManager.showToast('failed', response.data.message)
                                }
                            })
                            .catch(error => {
                                windowToastManager.showToast('failed', error)
                            });

                        this.fetchData();
                    },
                    created() {
                        console.log("DS")
                        // 初始化 debounce 函数
                        this.debouncedSearch = debounce(() => {
                            this.doSearch();
                        }, 500);
                    }
                })
                function debounce(func, delay) {
                    let timer = null;
                    return function (...args) {
                        clearTimeout(timer);
                        timer = setTimeout(() => {
                            func.apply(this, args);
                        }, delay);
                    };
                }

            </script>
            <!-- 上传页面 -->
            <div class="tab-pane fade" id="upload" role="tabpanel" aria-labelledby="overview-tab">
                <div class="modal-content w-50 mx-auto">
                    <div class="modal-body" style="margin-top: 50px">
                        <form id="fileUploadForm" enctype="multipart/form-data">
                            <!-- 文件上传 -->
                            <div class="mb-3 w-75 mx-auto">
                                <input type="file" class="form-control" id="fileInput" name="file">
                            </div>
                            <!-- 文件备注 -->
                            <div class="mb-3 w-75 mx-auto">
                                <label for="fileNote" class="form-label">文件备注</label>
                                <input type="text" class="form-control" id="fileNote" name="fileNote"
                                       v-model="upload_file.fileNote" autocomplete="off">
                            </div>

                            <!-- 文件类型（下拉框） -->
                            <div class="mb-3 w-75 mx-auto">
                                <label for="fileType" class="form-label">文件类型</label>
                                <i class="bi bi-info-circle"
                                   title="该字段决定了浏览器从链接访问该资源的效果,例如,一个图片如果选择为图片类型,那么你能在浏览器预览该资源,而非作为文件下载,如果选择默认,那么文件默认行为是下载"></i>
                                <select class="form-select" id="fileType" name="fileType"
                                        v-model="upload_file.fileType" autocomplete="off">
                                    <option value="default" selected>默认</option>
                                    <c:forEach var="fileType" items="${fileTypeList}">
                                        <option value="<c:out value='${fileType.typeField}'/>">
                                            <c:out value="${fileType.typeName}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <!-- 是否公开及授权码访问 -->
                            <div class="mb-3 w-75 mx-auto">
                                <div class="form-check">
                                    <input type="checkbox" class="form-check-input" id="publicCheckbox"
                                           onchange="togglePublicOptions()" name="isPublic"
                                           v-model="upload_file.isPublic">
                                    <label class="form-check-label" for="publicCheckbox">公开</label>
                                </div>
                            </div>

                            <div id="publicOptions">
                                <div class="mb-3 w-75 mx-auto">
                                    <div class="form-check">
                                        <input type="checkbox" class="form-check-input" id="codeAccessCheckbox"
                                               onchange="togglePublicOptions()" name="isAuthCode"
                                               v-model="upload_file.isAuthCode">
                                        <label class="form-check-label" for="codeAccessCheckbox">授权码访问</label>
                                    </div>
                                </div>

                                <!-- 设置授权码 -->
                                <div id="codeAccessOptions" style="display: none;">
                                    <div class="mb-3 w-75 mx-auto">
                                        <label for="authorizationCode" class="form-label">设置授权码</label>
                                        <input type="text" class="form-control" id="authorizationCode" name="authCode"
                                               v-model="upload_file.authCode" autocomplete="off">
                                    </div>
                                </div>
                            </div>
                        </form>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" @click="uploadFile">上传</button>
                    </div>
                </div>
                <div class="modal fade" id="progressbarModal" tabindex="-1" aria-labelledby="progressbarModalLab"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="progress">
                                <div id="progressBar" class="progress-bar" role="progressbar" style="width: 0;"
                                     aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <script>
                var up = new Vue({
                    el: '#upload',
                    data: {
                        upload_file: {
                            isPublic: '',
                            isAuthCode: '',
                            authCode: '',
                            fileNote: '',
                            fileType: 'default'
                        },
                        progressbarModal: '',
                    },
                    methods: {
                        init() {
                            this.progressbarModal = new bootstrap.Modal(document.getElementById('progressbarModal'), {
                                keyboard: false
                            })
                        },
                        uploadFile() {
                            var fileInput = document.getElementById('fileInput');

                            if (!fileInput || !fileInput.files || fileInput.files.length === 0) {
                                windowToastManager.showToast('failed', '请选择要上传的文件!');
                                return;
                            }

                            if (this.upload_file.isPublic === false && this.upload_file.isAuthCode === true && (this.upload_file.authCode === '' || this.upload_file.authCode === null)) {
                                windowToastManager.showToast('failed', '请输入授权码!')
                                return
                            }

                            var form = document.getElementById('fileUploadForm');
                            var formData = new FormData(form);

                            axios.get('${pageContext.request.contextPath}/user/getRemainingSpace')
                                .then((response) => {
                                    var remainingSpaceKB = response.data.data.remainingSpace;
                                    var fileSizeKB = Math.ceil(fileInput.files[0].size / 1024);

                                    if (fileSizeKB > remainingSpaceKB) {
                                        windowToastManager.showToast('failed', '文件大小超出剩余可用空间，请尝试扩容空间或者删除其他无用的文件');
                                        return;
                                    }

                                    this.progressbarModal.show();

                                    axios.post('${pageContext.request.contextPath}/file/upload', formData, {
                                        onUploadProgress: progressEvent => {
                                            const progress = Math.round((progressEvent.loaded / progressEvent.total) * 100);
                                            document.getElementById('progressBar').style.width = progress + '%';
                                            document.getElementById('progressBar').setAttribute('aria-valuenow', progress);
                                        },
                                    }).then((response) => {
                                        var data = response.data

                                        if (data.status === 0) {
                                            document.getElementById("fileUploadForm").reset();
                                            windowToastManager.showToast('success', '上传成功!')
                                            fi.fetchData()
                                        } else {
                                            windowToastManager.showToast('failed', response.data.message)
                                        }
                                    }).catch((error) => {
                                        windowToastManager.showToast('failed', error.data)
                                    }).finally(() => {
                                        setTimeout(() => {
                                            this.progressbarModal.hide();
                                        }, 500);
                                    });
                                })
                                .catch((error) => {
                                    windowToastManager.showToast('failed', '获取剩余可用空间失败!');
                                });
                        }
                    },
                    mounted() {
                        this.init();
                    }
                })
            </script>
        </div>
    </div>


</div>
</body>
</html>