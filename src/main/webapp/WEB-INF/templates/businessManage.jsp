<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="t-link.jsp" %>
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/css/person.css" rel="stylesheet">
    <title>业务管理</title>
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
                               href="#a-t-setting" data-bs-toggle="tab" role="tab" @click="select(1)">
                                <i class="bi bi-gear-wide-connected"></i>
                                <span>业务配置</span>
                            </a>
                        </li>
                        <li class="th-nav-link" :class="{ 'active': selected === 2 }">
                            <a class="nav-link d-flex align-items-center gap-2 th-link" href="#a-t-manage"
                               data-bs-toggle="tab" role="tab" @click="select(2)">
                                <i class="bi bi-person"></i>
                                <span>用户管理</span>
                            </a>
                        </li>
                        <li class="th-nav-link" :class="{ 'active': selected === 3 }">
                            <a class="nav-link d-flex align-items-center gap-2 th-link" href="#a-app-reg"
                               data-bs-toggle="tab" role="tab" @click="select(3)">
                                <i class="bi bi-chat-left-text"></i>
                                <span>注册申请</span>
                            </a>
                        </li>
                        <li class="th-nav-link" :class="{ 'active': selected === 4 }">
                            <a class="nav-link d-flex align-items-center gap-2 th-link" href="#a-t-application"
                               data-bs-toggle="tab" role="tab" @click="select(4)">
                                <i class="bi bi-chat-right-text-fill"></i>
                                <span>其他申请</span>
                            </a>
                        </li>
                        <li class="th-nav-link" :class="{ 'active': selected === 5 }">
                            <a class="nav-link d-flex align-items-center gap-2 th-link" href="#a-t-admin"
                               data-bs-toggle="tab" role="tab" @click="select(5)">
                                <i class="bi bi-shield-lock-fill"></i>
                                <span>个人管理</span>
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
            <div class="tab-pane fade active show" id="a-t-setting" role="tabpanel" aria-labelledby="overview-tab">
                <span style="color: #76757b">用户配置</span><br/><br/>
                <div class="d-inline-flex justify-content-between align-items-center" style="margin-right: 200px">
                    <span style="width: 100px">用户注册:</span>
                    <select class="form-select" v-model="config.user.register" aria-label="Default select example"
                            style="width:150px">
                        <option value="0" selected>允许</option>
                        <option value="1">禁止</option>
                        <option value="2">需要申请</option>
                    </select>
                </div>
                <div class="d-inline-flex justify-content-between align-items-center">
                    <span style="width: 100px">初始储存:</span>
                    <input class="form-control" v-model="config.user.space" type="text"
                           style="width: 100px;margin-right: 10px">
                    <select class="form-select" v-model="config.user.unit" aria-label="Default select example"
                            style="width:80px">
                        <option value="MB" selected>MB</option>
                        <option value="GB">GB</option>
                    </select>
                </div>
                <br/><br/>
                <span style="color: #76757b">用户配置</span><br/><br/>
                <div class="col-1">
                    <button class="btn btn-success" @click="updateConfig">保存</button>
                </div>
            </div>
            <script>
                var um = new Vue({
                    el: '#a-t-setting',
                    data: {
                        config: {
                            user: {
                                register: 1,
                                space: 10,
                                unit: 'GB'
                            }
                        },
                    },
                    methods: {
                        updateConfig() {
                            axios.post('${pageContext.request.contextPath}/config/updateBConfig', this.config)
                                .then(response => {
                                    if (response.data.status !== 0) {
                                        windowToastManager.showToast('failed', response.data.message)
                                        this.getConfig();
                                    } else {
                                        windowToastManager.showToast('success', "更改成功!")
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                    this.getConfig();
                                });
                        },
                        getConfig() {
                            axios.post('${pageContext.request.contextPath}/config/getBConfig', this.config)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        this.config = response.data.data;
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                });
                        }
                    },
                    mounted() {
                        this.getConfig();
                    }
                })
            </script>

            <div class="tab-pane fade" id="a-t-manage" role="tabpanel" aria-labelledby="overview-tab">
                <div id="app" class="container mt-4">
                    <!-- 第一排：操作按钮 -->
                    <div class="row mb-3">
                        <div class="col-1">
                            <button class="btn btn-primary" @click="addUser">添加</button>
                        </div>
                        <div class="col-1">
                            <button :disabled="selectedUsers.length === 0" class="btn btn-danger" @click="deleteUser">删除</button>
                        </div>
                        <div class="col-1">
                            <button :disabled="selectedUsers.length === 0" class="btn btn-danger" @click="disableUser">禁用</button>
                        </div>
                        <div class="col-1">
                            <button :disabled="selectedUsers.length === 0" class="btn btn-success" @click="enableUser">启用</button>
                        </div>
                    </div>
                    <!-- 第二排：用户信息输入框 -->
                    <div class="row mb-3">
                        <div class="col-2">
                            <input type="text" class="form-control" placeholder="用户名" v-model="searchData.username">
                        </div>
                        <div class="col-2">
                            <input type="text" class="form-control" placeholder="邮箱" v-model="searchData.email">
                        </div>
                        <div class="col-2">
                            <select class="form-select" v-model="searchData.status">
                                <option value="all" selected>所有状态</option>
                                <option value="enable">正常</option>
                                <option value="disable">禁用</option>
                            </select>
                        </div>
                        <div class="col-2">
                            <select class="form-select" v-model="searchData.sort">
                                <option value="username0" selected>用户名-正序</option>
                                <option value="username1">用户名-倒序</option>
                                <option value="createdAt0">创建日期-正序</option>
                                <option value="createdAt1">创建日期-倒序</option>
                                <option value="lastLoginAt0">上次登录日期-正序</option>
                                <option value="lastLoginAt1">上次登录日期-倒序</option>
                            </select>
                        </div>
                        <div class="col-2">
                            <button class="btn btn-secondary me-2" @click="searchClear">清空</button>
                            <button class="btn btn-primary" @click="doSearch">搜索</button>
                        </div>
                    </div>
                    <!-- 用户表格 -->
                    <table class="table border border-1 border-secondary-subtle table-hover">
                        <thead>
                        <tr class="table-secondary">
                            <th class="col-1">
                                <input class="form-check-input" type="checkbox" v-model="selectAll"
                                       @change="selectAllUsers">
                            </th>
                            <th class="col-2">用户名</th>
                            <th class="col-2">状态</th>
                            <th class="col-2">注册时间</th>
                            <th class="col-2">上次登录</th>
                            <th class="col-2">选项</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="user in users" :key="user.id" :class="{ 'table-danger': user.status === 1 }">
                            <th scope="row">
                                <input class="form-check-input" type="checkbox" v-model="selectedUsers"
                                       :value="user.userId" @change="show">
                            </th>
                            <td>{{ user.username }}</td>
                            <td>{{ toStatus(user.status) }}</td>
                            <td>{{ formatJavaDate(user.createdAt) }}</td>
                            <td>{{ formatJavaDate(user.lastLoginAt) }}</td>
                            <td>
                                <a class="link-blue" @click="editUser(user.userId)">编辑</a>
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
                </div>
                <div id="modal-div">
                    <div class="modal fade" id="deleteUser" tabindex="-1" aria-labelledby="deleteUserLabel"
                         aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="deleteUserLabel">确认删除</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                </div>
                                <div class="modal-body" id="deleteUserBody">
                                    确定要删除这些用户吗？
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                                    </button>
                                    <button type="button" class="btn btn-danger" @click="doDeleteUser">确定删除
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal fade" id="disableUser" tabindex="-1" aria-labelledby="disableUserLabel"
                         aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="disableUserLabel">确认禁用</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                </div>
                                <div class="modal-body" id="disableUserBody">
                                    确定要禁用这些用户吗？
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                                    </button>
                                    <button type="button" class="btn btn-danger" @click="doDisableUser">确定禁用
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal fade" id="enableUser" tabindex="-1" aria-labelledby="enableUserLabel"
                         aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="enableUserLabel">确认启用</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                </div>
                                <div class="modal-body" id="enableUserBody">
                                    确定要启用这些用户吗？
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                                    </button>
                                    <button type="button" class="btn btn-danger" @click="doEnableUser">确定启用
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal fade" id="addUser" tabindex="-1" aria-labelledby="addUserLabel"
                         aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div id="addForm" style="padding-top: 30px">
                                    <!-- 用户账号 -->
                                    <div class="mb-3 w-75 mx-auto">
                                        <label for="a-user-username" class="form-label">用户名</label>
                                        <input type="text" class="form-control" id="a-user-username" name="username"
                                               v-model="add_user.username" autocomplete="off">
                                    </div>
                                    <!-- 密码 -->
                                    <div class="mb-3 w-75 mx-auto">
                                        <label for="a-user-pwd" class="form-label">密码</label>
                                        <input type="password" class="form-control" id="a-user-pwd" name="password"
                                               v-model="add_user.password" autocomplete="off">
                                    </div>

                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                                        </button>
                                        <button class="btn btn-danger" @click="doAddUser">添加</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal fade" id="modifyUser" tabindex="-1" aria-labelledby="modifyUserLabel"
                         aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div id="modifyForm" style="padding-top: 30px">
                                    <!-- 用户密码 -->
                                    <div class="mb-3 w-75 mx-auto">
                                        <label for="m-user-pwd" class="form-label">用户密码</label>
                                        <input type="password" class="form-control" id="m-user-pwd" name="password"
                                               v-model="modify_user.password" autocomplete="off">
                                    </div>

                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                                        </button>
                                        <button class="btn btn-danger" @click="doEditUser">更改</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <script>
                var adUser = new Vue({
                    el: '#a-t-manage',
                    data: {
                        users: [],
                        selectedUsers: [],
                        selectAll: false,
                        userEdit: null,

                        searchData: {
                            username: '',
                            email: '',
                            status: 'all',
                            sort: 'username0',
                            page: 1
                        },
                        currentPage: 1,
                        totalPages: 1,

                        add_user: {
                            username: '',
                            password: ''
                        },
                        modify_user: {
                            userId: '',
                            password: ''
                        }
                    },
                    methods: {
                        initData() {
                            this.currentPage = 1;
                            this.searchData.page = 1;
                            this.doSearch()
                        },
                        selectAllUsers() {
                            if (this.selectAll) {
                                this.selectedUsers = this.users.map(user => user.userId);
                            } else {
                                this.selectedUsers = [];
                            }
                            this.logSelectedUsers();
                        },
                        logSelectedUsers() {
                            console.log("选中的用户ID集合:", this.selectedUsers);
                        },
                        show() {
                            console.log("选中的用户ID集合:", this.selectedUsers);
                        },

                        addUser() {
                            this.add_user = {
                                username: '',
                                password: ''
                            }
                            this.showModal("addUser")
                        },
                        doAddUser() {
                            axios.post('${pageContext.request.contextPath}/user/add', this.add_user)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        windowToastManager.showToast('success', '添加成功!')
                                        this.hideModal("addUser")
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
                        deleteUser() {
                            this.showModal("deleteUser")
                        },
                        doDeleteUser() {
                            axios.post('${pageContext.request.contextPath}/user/delete', this.selectedUsers)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        windowToastManager.showToast('success', '删除成功!')
                                        this.hideModal("deleteUser")
                                        this.doSearch()
                                        this.clearSelected()
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                });
                        },
                        disableUser() {
                            this.showModal("disableUser")
                        },
                        doDisableUser() {
                            axios.post('${pageContext.request.contextPath}/user/disable', this.selectedUsers)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        windowToastManager.showToast('success', '禁用成功!')
                                        this.hideModal("disableUser")
                                        this.doSearch()
                                        this.clearSelected()
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                });
                        },
                        enableUser() {
                            this.showModal("enableUser")
                        },
                        doEnableUser() {
                            axios.post('${pageContext.request.contextPath}/user/enable', this.selectedUsers)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        windowToastManager.showToast('success', '启用成功!')
                                        this.hideModal("enableUser")
                                        this.doSearch()
                                        this.clearSelected()
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                });
                        },
                        editUser(userId) {
                            this.showModal("modifyUser")
                            this.modify_user = {
                                userId: userId,
                                password: ''
                            }
                        },
                        doEditUser() {
                            axios.post('${pageContext.request.contextPath}/user/modify', this.modify_user)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        windowToastManager.showToast('success', '更改成功!')
                                        this.hideModal("modifyUser")
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
                        searchClear() {
                            this.searchData = {
                                username: '',
                                email: '',
                                status: 'all',
                                sort: 'username0',
                                page: this.searchData.page
                            }
                        },
                        doSearch() {
                            axios.post('${pageContext.request.contextPath}/user/search', this.searchData)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        this.users = response.data.data.users;
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
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
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
                        clearSelected() {
                            this.selectedUsers = []
                            this.selectAll = false
                        },
                        hideModal(modal) {
                            var m = bootstrap.Modal.getInstance(document.getElementById(modal));
                            m.hide();
                        },
                        showModal(modal) {
                            var m = new bootstrap.Modal(document.getElementById(modal), {
                                keyboard: false
                            });
                            m.show();
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
                        toStatus(status) {
                            if (status === 0) {
                                return "正常"
                            }
                            if (status === 1) {
                                return "禁用"

                            }
                            return "N/A"
                        }
                    },

                    mounted() {
                        this.initData()
                    }
                })
            </script>

            <div class="tab-pane fade" id="a-app-reg" role="tabpanel" aria-labelledby="overview-tab">
                <div id="app-reg" class="container mt-4">
                    <!-- 第一排：操作按钮 -->
                    <div class="row mb-3">
                        <div class="col-1">
                            <button :disabled="selectedApps.length === 0" class="btn btn-success" @click="permit">同意</button>
                        </div>
                        <div class="col-1">
                            <button :disabled="selectedApps.length === 0" class="btn btn-danger" @click="deny">拒绝</button>
                        </div>
                    </div>
                    <!-- 第二排：用户信息输入框 -->
                    <div class="row mb-3">
                        <div class="col-2">
                            <select class="form-select" v-model="searchData.status">
                                <option value="all" selected>所有</option>
                                <option value="deny">已拒绝</option>
                                <option value="permit">已同意</option>
                                <option value="noHandle">未处理</option>
                                <option value="failed">失败</option>
                            </select>
                        </div>
                        <div class="col-2">
                            <button class="btn btn-secondary me-2" @click="searchClear">清空</button>
                            <button class="btn btn-primary" @click="doSearch">搜索</button>
                        </div>
                    </div>
                    <!-- 用户表格 -->
                    <table class="table border border-1 border-secondary-subtle table-hover">
                        <thead>
                        <tr class="table-secondary">
                            <th class="col-1">
                            </th>
                            <th class="col-2">用户名</th>
                            <th class="col-2">申请时间</th>
                            <th class="col-2">申请类型</th>
                            <th class="col-1">状态</th>
                            <th class="col-2">处理时间</th>
                            <th class="col-2">处理人</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr v-for="app in apps" :key="app.id" :class="{ 'table-danger': app.appStatus === 1, 'table-success': app.appStatus === 2, 'table-warning': app.appStatus === 10}">
                            <th scope="row">
                                <input :disabled="app.appStatus !== 0" class="form-check-input" type="checkbox" v-model="selectedApps"
                                       :value="app.appId">
                            </th>
                            <td>{{ app.username }}</td>
                            <td>{{ formatJavaDate(app.appSubmitDate) }}</td>
                            <td>{{ app.appType }}</td>
                            <td>{{ toStatus(app.appStatus) }}</td>
                            <td>{{ formatJavaDate(app.dealDate) }}</td>
                            <td>{{ app.dealUser }}</td>
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
                </div>
                <div id="modal-div2">
                    <div class="modal fade" id="denyApp" tabindex="-1" aria-labelledby="denyAppLabel"
                         aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="denyAppLabel">确认拒绝</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                </div>
                                <div class="modal-body" id="denyAppBody">
                                    确定要拒绝这些用户的申请吗？
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                                    </button>
                                    <button type="button" class="btn btn-danger" @click="doDenyApp">确定拒绝
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal fade" id="permitApp" tabindex="-1" aria-labelledby="permitAppLabel"
                         aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="permitAppLabel">确认同意</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                </div>
                                <div class="modal-body" id="permitAppBody">
                                    确定要同意这些用户的申请吗？
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                                    </button>
                                    <button type="button" class="btn btn-danger" @click="doPermitApp">确定同意
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <script>
                var regApp = new Vue({
                    el: '#a-app-reg',
                    data: {
                        apps: [],
                        selectedApps: [],
                        selectAll: false,

                        searchData: {
                            status: 'all',
                            page: 1
                        },
                        currentPage: 1,
                        totalPages: 1,
                    },
                    methods: {
                        initData() {
                            this.currentPage = 1;
                            this.searchData.page = 1;
                            this.doSearch()
                        },
                        selectAllApps() {
                            if (this.selectAll) {
                                this.selectedApps = this.apps.map(app => app.appId);
                            } else {
                                this.selectedApps = [];
                            }
                            this.logSelectedApps();
                        },
                        logSelectedApps() {
                            console.log("选中的用户ID集合:", this.selectedApps);
                        },
                        permit(){
                            this.showModal("permitApp")
                        },
                        doPermitApp(){
                            axios.post('${pageContext.request.contextPath}/application/permit', this.selectedApps)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        windowToastManager.showToast('success', '同意成功!')
                                        this.hideModal("permitApp")
                                        this.initData()
                                        this.clearSelected()
                                        adUser.doSearch();
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                });
                        },
                        deny(){
                            this.showModal("denyApp")
                        },
                        doDenyApp(){
                            axios.post('${pageContext.request.contextPath}/application/deny', this.selectedApps)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        windowToastManager.showToast('success', '拒绝成功!')
                                        this.hideModal("denyApp")
                                        this.initData()
                                        this.clearSelected()
                                    } else {
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    windowToastManager.showToast('failed', '请求失败!')
                                });
                        },
                        searchClear() {
                            this.searchData = {
                                status: 'all',
                                page: this.searchData.page
                            }
                        },
                        doSearch() {
                            axios.post('${pageContext.request.contextPath}/application/getApplication', this.searchData)
                                .then(response => {
                                    if (response.data.status === 0) {
                                        this.apps = response.data.data.apps;
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
                                        windowToastManager.showToast('failed', response.data.message)
                                    }
                                })
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
                        clearSelected() {
                            this.selectedApps = []
                            this.selectAll = false
                        },
                        hideModal(modal) {
                            var m = bootstrap.Modal.getInstance(document.getElementById(modal));
                            m.hide();
                        },
                        showModal(modal) {
                            var m = new bootstrap.Modal(document.getElementById(modal), {
                                keyboard: false
                            });
                            m.show();
                        },
                        formatJavaDate(javaDate) {
                            if(javaDate===null || javaDate===''){
                                return "无"
                            }

                            const date = new Date(javaDate);
                            const year = date.getFullYear();
                            const month = String(date.getMonth() + 1).padStart(2, '0');
                            const day = String(date.getDate()).padStart(2, '0');
                            const hours = String(date.getHours()).padStart(2, '0');
                            const minutes = String(date.getMinutes()).padStart(2, '0');
                            return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes;
                        },
                        toStatus(status) {
                            if (status === 0) {
                                return "未处理"
                            }
                            if (status === 1) {
                                return "已拒绝"
                            }
                            if (status === 2) {
                                return "已同意"
                            }
                            if(status === 10){
                                return "失败"
                            }
                            return "N/A"
                        }
                    },

                    mounted() {
                        this.initData()
                    }
                })
            </script>


            <div class="tab-pane fade" id="a-t-application" role="tabpanel" aria-labelledby="overview-tab">
                4
            </div>
            <div class="tab-pane fade" id="a-t-admin" role="tabpanel" aria-labelledby="overview-tab">
                <div class="user-info-container" style="font-size: 20px">
                    <h2 class="mt-3 mb-3">安全信息</h2>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">用户名:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ securityInfo.username }}</span>
                    </div>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">你目前的ip地址:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ securityInfo.ip }}</span>
                    </div>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">上次登录的ip地址:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ securityInfo.lastIp }}</span>
                    </div>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">上次登录时间:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ formatJavaDate(securityInfo.lastLogin) }}</span>
                    </div>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">密码:</span>
                        <span style="font-family: monospace;">
                            <button @click="modifyPassword" class="btn btn-link" data-bs-toggle="modal" data-bs-target="#changePasswordModal"
                                    style="padding: 0; text-decoration: underline; cursor: pointer;">更改
                            </button>
                        </span>
                    </div>
                </div>
                <!-- 修改密码 -->
                <div class="modal fade" id="changeUserPassword" tabindex="-1" aria-labelledby="changeUserPasswordLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div id="changePasswordForm" style="padding-top: 30px">
                                <div class="mb-3 w-75 mx-auto">
                                    <label for="cp-user-pwd" class="form-label">用户密码</label>
                                    <input type="password" class="form-control" id="cp-user-pwd" name="password"
                                           v-model="changePassword.password" autocomplete="off">
                                </div>
                                <div class="mb-3 w-75 mx-auto">
                                    <label for="cp-user-confirm-pwd" class="form-label">再次输入密码</label>
                                    <input type="password" class="form-control" id="cp-user-confirm-pwd" name="confirm_password"
                                           v-model="changePassword.confirmPassword" autocomplete="off">
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                                    </button>
                                    <button class="btn btn-danger" @click="doModifyPassword">更改密码</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <script>
                var um = new Vue({
                    el: '#a-t-admin',
                    data: {
                        securityInfo: {
                            username: null,
                            email: null,
                            ip: null,
                            lastIp: null,
                            lastLogin: null
                        },
                        changePassword:{
                            password:null,
                            confirmPassword:null
                        }
                    },
                    methods: {
                        loadSecurityInfo() {
                            axios.get('${pageContext.request.contextPath}/user/getSecurityInfo')
                                .then(response => {
                                    if (response.data.status === 0) {
                                        this.securityInfo = response.data.data;
                                    } else {
                                        windowToastManager.showToast('failed', '请求失败:' + response.data.message);
                                    }
                                })
                                .catch(error => {
                                    windowToastManager.showToast('failed', '请求失败:' + error);
                                });
                        },
                        modifyPassword() {
                            this.changePassword = {
                                password: null,
                                confirmPassword: null
                            };
                            $('#changeUserPassword').modal('show');
                        },
                        doModifyPassword() {
                            if(this.changePassword.password!==this.changePassword.confirmPassword){
                                windowToastManager.showToast('failed', '两次密码输入不一致!');
                                return
                            }
                            axios.post('${pageContext.request.contextPath}/user/modifyMyPassword', { password: this.changePassword.password })
                                .then(response => {
                                    if(response.data.status===0){
                                        windowToastManager.showToast('success', '修改成功');
                                        $('#changeUserPassword').modal('hide');
                                    }else{
                                        windowToastManager.showToast('failed', '修改失败:'+response.data.message);
                                    }
                                })
                                .catch(error => {
                                    windowToastManager.showToast('failed', '修改失败:'+error);
                                });
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
                    },
                    mounted() {
                        this.loadSecurityInfo();
                    }
                })
            </script>
        </div>
    </div>
</div>
</body>
</html>
