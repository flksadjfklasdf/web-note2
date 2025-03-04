<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="t-link.jsp" %>
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/css/person.css" rel="stylesheet">
    <script src="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/tabulator/tabulator.js"></script>
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/tabulator/tabulator.css" rel="stylesheet">
    <title>Title</title>
</head>
<body>
<%@include file="t-toast.jsp" %>
<%@include file="t-header.jsp" %>
<div id="u-content" class="container-f">
    <div class="d-flex" id="u-c-content">
        <div class="sidebar border border-right p-0 bg-body-tertiary" id="u-c-sidebar">
            <div class="offcanvas-md offcanvas-end bg-body-tertiary" tabindex="-1" id="sidebarMenu"
                 aria-labelledby="sidebarMenuLabel">
                <div class="offcanvas-body d-md-flex flex-column p-0 pt-lg-3 overflow-y-auto">
                    <ul class="nav flex-column">
                        <li class="th-nav-link" :class="{ 'active': selected === 1 }">
                            <a class="nav-link d-flex align-items-center gap-2 active th-link" aria-current="page"
                               href="#s-t-setting" data-bs-toggle="tab" role="tab" @click="select(1)">
                                <i class="bi bi-gear-wide-connected"></i>
                                <span>系统配置</span>
                            </a>
                        </li>
                        <li class="th-nav-link" :class="{ 'active': selected === 2 }">
                            <a class="nav-link d-flex align-items-center gap-2 th-link" href="#s-t-manage"
                               data-bs-toggle="tab" role="tab" @click="select(2)">
                                <i class="bi bi-shield-lock-fill"></i>
                                <span>管理员管理</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
            <script>
                var us = new Vue({
                    el: '#sidebarMenu',
                    data: {
                        selected: 1
                    },
                    methods: {
                        select(index) {
                            this.selected = index;
                        }
                    }
                });
            </script>
        </div>
        <div class="d-flex flex-column flex-fill tab-content" id="u-pane">
            <div class="tab-pane fade active show" id="s-t-setting" role="tabpanel" aria-labelledby="overview-tab">
                <div class="user-info-container" style="font-size: 20px">
                    <h2 class="mt-3 mb-3">参数设置</h2>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <button @click="editFileType()" class="btn btn-link"
                                style="padding: 0; text-decoration: underline; cursor: pointer;">更改文件类型表
                        </button>
                    </div>
                </div>

                <div class="modal fade" id="editFileType" tabindex="-1" aria-labelledby="editFileTypeLabel" aria-hidden="true">
                    <div class="modal-dialog" style="width: 1460px;max-width: 1460px">
                        <div class="modal-content" style="padding: 30px;width: 1460px" >
                            <span>编辑表格</span>
                            <div id="table-area" style="width: 1400px">
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-info" @click="addFileType()">新增</button>
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-danger" @click="doEditFileType">确定更改
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                var ac = new Vue({
                    el: '#s-t-setting',
                    data: {
                        table:null
                    },
                    methods: {
                        editFileType(){

                            $('#editFileType').modal('show')


                            var TB = this.table;
                            let cellContextMenuX = [
                                {
                                    label: "新增行",
                                    action: function(e, cell) {
                                        var newData = { id: TB.getData().length + 1, name: "", col: "", dob: "" };
                                        this.table.addData(newData);
                                    }
                                },
                                {
                                    label: "删除行",
                                    action: function(e, cell) {
                                        var row = cell.getRow();
                                        row.delete();
                                    }
                                }
                            ]
                            axios.post('${pageContext.request.contextPath}/system/config/fileTypes/query')
                                .then(response => {
                                    if (response.data.status === 0) {
                                        let data = response.data.data;
                                        let tabledata = []
                                        for (let i = 0; i < data.length; i++) {
                                            tabledata[i]={
                                                id:i+1,
                                                name:data[i].typeField,
                                                col:data[i].typeName,
                                                dob:data[i].contentType

                                            }
                                        }

                                        this.table = new Tabulator("#table-area", {
                                            data:tabledata,
                                            layout:"fitColumns",
                                            columns:[
                                                {title:"文件名称", field:"name",editor:true,contextMenu:cellContextMenuX,  headerSort: false},
                                                {title:"文件类型", field:"col",editor:true,contextMenu:cellContextMenuX,  headerSort: false},
                                                {title:"Content-Type", field:"dob",editor:true,contextMenu:cellContextMenuX, headerSort: false},
                                            ],
                                        });
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                });
                        },
                        addFileType(){
                            var TB = this.table;
                            var newData = { id: TB.getData().length + 1, name: "", col: "", dob: "" };
                            TB.addData(newData);
                        },
                        doEditFileType(){
                            let tableData = this.table.getData();

                            let dataToSend = tableData.map(item => ({
                                typeField: item.name,
                                typeName: item.col,
                                contentType: item.dob
                            }));

                            axios.post('${pageContext.request.contextPath}/system/config/fileTypes/update',dataToSend)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        windowToastManager.showToast('success', "更新成功!")

                                        var modal = bootstrap.Modal.getInstance(document.getElementById('editFileType'));
                                        modal.hide();
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                });
                        }

                    }
                });
            </script>
            <div class="tab-pane fade" id="s-t-manage" role="tabpanel" aria-labelledby="overview-tab">
                <button class="btn btn-sm btn-outline-primary" @click="addAdmin">添加</button>

                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th scope="col">序号</th>
                        <th scope="col">用户名</th>
                        <th scope="col">角色</th>
                        <th scope="col">状态</th>
                        <th scope="col">创建日期</th>
                        <th scope="col">操作</th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr class="table-danger">
                        <th scope="row">1</th>
                        <td>{{ systemAdmin.username }}</td>
                        <td>系统管理员</td>
                        <td>正常</td>
                        <td>{{ formatJavaDate(systemAdmin.createdAt) }}</td>
                        <td>
                            <a class="btn btn-sm btn-outline-primary" @click="editAdmin(systemAdmin.userId,systemAdmin.username,systemAdmin.userType)">编辑</a>
                        </td>
                    </tr>

                    <tr v-for="(admin, index) in admins" class="table-info">
                        <th :scope="'row' + index">{{ index + 2 }}</th>
                        <td>{{ admin.username }}</td>
                        <td>业务管理员</td>
                        <td>{{ getStatus(admin.status) }}</td>
                        <td>{{ formatJavaDate(admin.createdAt) }}</td>
                        <td>
                            <a class="btn btn-sm btn-outline-primary" @click="editAdmin(admin.userId,admin.username,admin.userType)">编辑</a>
                            <a class="btn btn-sm btn-outline-danger" @click="deleteAdmin(admin.userId,admin.username)">删除</a>
                            <a class="btn btn-sm btn-outline-danger" @click="disableAdmin(admin.userId,admin.username)">{{ getStatusAction(admin.status) }}</a>
                        </td>
                    </tr>
                    </tbody>
                </table>



                <div class="modal fade" id="deleteAdmin" tabindex="-1" aria-labelledby="deleteAdminLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="deleteAdminLabel">确认删除</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="deleteAdminBody">
                                确定要删除{{ delete_admin.username }}管理员吗？
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-danger" @click="doDeleteAdmin">确定删除
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="disableAdmin" tabindex="-1" aria-labelledby="disableAdminLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="disableAdminLabel">确认更改</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="disableAdminBody">
                                确定要更改{{ disable_admin.username }}管理员吗？
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                <button type="button" class="btn btn-danger" @click="doDisableAdmin">确定更改
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="addAdmin" tabindex="-1" aria-labelledby="addAdminLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div id="addForm" style="padding-top: 30px">
                                <!-- 管理员账号 -->
                                <div class="mb-3 w-75 mx-auto">
                                    <label for="a-admin-username" class="form-label">管理员用户名</label>
                                    <input type="text" class="form-control" id="a-admin-username" name="username"
                                           v-model="add_admin.username" autocomplete="off">
                                </div>
                                <!-- 管理员密码 -->
                                <div class="mb-3 w-75 mx-auto">
                                    <label for="a-admin-pwd" class="form-label">管理员密码</label>
                                    <input type="password" class="form-control" id="a-admin-pwd" name="password"
                                           v-model="add_admin.password" autocomplete="off">
                                </div>

                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                                    </button>
                                    <button class="btn btn-danger" @click="doAddAdmin">添加</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="modifyAdmin" tabindex="-1" aria-labelledby="modifyAdminLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div id="modifyForm" style="padding-top: 30px">
                                <!-- 管理员账号 -->
                                <div v-if="edit_admin.userType === 1" class="mb-3 w-75 mx-auto">
                                    <label for="m-admin-username" class="form-label">管理员用户名</label>
                                    <input type="text" class="form-control" id="m-admin-username" name="username"
                                           v-model="edit_admin.username" autocomplete="off">
                                </div>
                                <!-- 管理员密码 -->
                                <div class="mb-3 w-75 mx-auto">
                                    <label for="m-admin-pwd" class="form-label">管理员密码</label>
                                    <input type="password" class="form-control" id="m-admin-pwd" name="password"
                                           v-model="edit_admin.password" autocomplete="off">
                                </div>

                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                                    </button>
                                    <button class="btn btn-danger" @click="doEditAdmin">更改</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <script>
                var am = new Vue({
                    el: '#s-t-manage',
                    data: {
                        systemAdmin: {
                            userId:'',
                            username:''
                        },
                        admins:null,
                        add_admin:{
                            username:'',
                            password:''
                        },
                        edit_admin:{
                            userId:'',
                            userType:0,
                            username:'',
                            password:''
                        },
                        disable_admin:{
                            username:'',
                            userId:''
                        },
                        delete_admin:{
                            username:'',
                            userId:''
                        }
                    },
                    methods: {
                        initData(){
                            axios.post('${pageContext.request.contextPath}/admin/getSystemAdmin')
                                .then(response => {
                                    if (response.data.status === 0) {
                                        this.systemAdmin = response.data.data;
                                    } else {
                                        windowToastManager.showToast('failed', '请求失败!',response.data.message)
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                });

                            axios.post('${pageContext.request.contextPath}/admin/getBusinessAdmin')
                                .then(response => {
                                    if (response.data.status === 0) {
                                        this.admins = response.data.data;
                                    } else {
                                        windowToastManager.showToast('failed', '请求失败!',response.data.message)
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                });
                        },
                        addAdmin(){
                            var modal = new bootstrap.Modal(document.getElementById('addAdmin'), {
                                keyboard: false
                            });
                            modal.show();
                        },
                        doAddAdmin(){
                            axios.post('${pageContext.request.contextPath}/admin/add',this.add_admin)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        windowToastManager.showToast('success', '添加成功!')
                                        var modal = bootstrap.Modal.getInstance(document.getElementById('addAdmin'));
                                        modal.hide();
                                        this.initData()
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                    this.clearAdd()
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                });


                        },
                        editAdmin(userId,username,userType){
                            this.edit_admin.userId=userId;
                            this.edit_admin.userType=userType;
                            this.edit_admin.username=username;

                            console.log(this.edit_admin)

                            var modal = new bootstrap.Modal(document.getElementById('modifyAdmin'), {
                                keyboard: false
                            });
                            modal.show();
                        },
                        doEditAdmin(){
                            axios.post('${pageContext.request.contextPath}/admin/modify',this.edit_admin)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        windowToastManager.showToast('success', '更改成功!')
                                        var modal = bootstrap.Modal.getInstance(document.getElementById('modifyAdmin'));
                                        modal.hide();
                                        this.initData()
                                        this.clearEdit()
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }

                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                });
                        },
                        disableAdmin(userId,username){
                            this.disable_admin.userId=userId;
                            this.disable_admin.username=username;

                            var modal = new bootstrap.Modal(document.getElementById('disableAdmin'), {
                                keyboard: false
                            });
                            modal.show();
                        },
                        doDisableAdmin(){
                            axios.post('${pageContext.request.contextPath}/admin/disable',this.disable_admin)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        this.initData()
                                        var modal = bootstrap.Modal.getInstance(document.getElementById('disableAdmin'));
                                        modal.hide();
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                });
                        },
                        deleteAdmin(userId,username){
                            this.delete_admin.userId=userId;
                            this.disable_admin.username=username;

                            var modal = new bootstrap.Modal(document.getElementById('deleteAdmin'), {
                                keyboard: false
                            });
                            modal.show();
                        },
                        doDeleteAdmin(){
                            axios.post('${pageContext.request.contextPath}/admin/delete',this.delete_admin)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        windowToastManager.showToast('success', '删除成功!')
                                        var modal = bootstrap.Modal.getInstance(document.getElementById('deleteAdmin'));
                                        modal.hide();
                                        this.initData()
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                });
                        },
                        getStatus(status){
                            if(status===0){
                                return "正常"
                            }
                            if(status===1){
                                return "禁用"
                            }
                        },
                        getStatusAction(status){
                            if(status===0){
                                return "禁用"
                            }
                            if(status===1){
                                return "启用"
                            }
                        },
                        formatJavaDate(javaDate) {
                            const date = new Date(javaDate + '');
                            const year = date.getFullYear();
                            const month = String(date.getMonth() + 1).padStart(2, '0');
                            const day = String(date.getDate()).padStart(2, '0');
                            return year + '-' + month + '-' + day;
                        },
                        clearEdit(){
                            this.edit_admin={
                                userId:'',
                                userType:0,
                                username:'',
                                password:''
                            }
                        },
                        clearAdd(){
                            this.add_admin={
                                username:'',
                                password:''
                            }
                        }

                    },
                    mounted() {
                        this.initData()
                    }
                });
            </script>
        </div>
    </div>
</div>
</body>
</html>
