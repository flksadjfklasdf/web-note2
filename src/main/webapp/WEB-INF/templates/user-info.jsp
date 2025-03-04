<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <jsp:include page="t-link.jsp"/>
    <link href="${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/css/person.css" rel="stylesheet">
    <title>Title</title>
</head>
<body>
<jsp:include page="t-header.jsp"/>
<jsp:include page="t-toast.jsp"/>
<div id="u-content" class="container-f">
    <div class="d-flex" id="u-c-content">
        <div class="sidebar border border-right p-0 bg-body-tertiary" id="u-c-sidebar">
            <div class="offcanvas-md offcanvas-end bg-body-tertiary" tabindex="-1" id="sidebarMenu"
                 aria-labelledby="sidebarMenuLabel">
                <div class="offcanvas-body d-md-flex flex-column p-0 pt-lg-3 overflow-y-auto">
                    <ul class="nav flex-column">
                        <li class="th-nav-link" :class="{ 'active': selected === 2 }">
                            <a class="nav-link d-flex align-items-center gap-2 active th-link" href="#u-t-basic"
                               data-bs-toggle="tab" role="tab" @click="select(2)">
                                <i class="bi bi-file-earmark-person-fill"></i>
                                <span>基本信息</span>
                            </a>
                        </li>
                        <li class="th-nav-link" :class="{ 'active': selected === 3 }">
                            <a class="nav-link d-flex align-items-center gap-2 th-link" href="#u-t-personalization"
                               data-bs-toggle="tab" role="tab" @click="select(3)">
                                <i class="bi bi-person-heart"></i>
                                <span>个性化</span>
                            </a>
                        </li>
                        <li class="th-nav-link" :class="{ 'active': selected === 4 }">
                            <a class="nav-link d-flex align-items-center gap-2 th-link" href="#u-t-settings"
                               data-bs-toggle="tab" role="tab" @click="select(4)">
                                <i class="bi bi-gear-wide-connected"></i>
                                <span>设置</span>
                            </a>
                        </li>
                        <li class="th-nav-link" :class="{ 'active': selected === 5 }">
                            <a class="nav-link d-flex align-items-center gap-2 th-link" href="#u-t-account"
                               data-bs-toggle="tab" role="tab" @click="select(5)">
                                <i class="bi bi-shield-lock-fill"></i>
                                <span>账号安全</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="d-flex flex-column flex-fill tab-content" id="u-pane">
            <div class="tab-pane fade active show" id="u-t-basic" role="tabpanel" aria-labelledby="overview-tab">
                <div class="user-info-container" style="font-size: 20px">
                    <h2 class="mt-3 mb-3">个人资料</h2>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">用户名:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ basicInfo.username }}</span>
                    </div>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">用户身份:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ basicInfo.userRole }}</span>
                    </div>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">文档集合数量:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ basicInfo.collectionCount }}</span>
                    </div>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">文章数量:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ basicInfo.articleCount }}</span>
                    </div>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">文件累计大小:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ addCommas(basicInfo.totalFileSize) }} KB</span>
                    </div>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">用户空间使用情况:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ addCommas(basicInfo.totalFileSize) }} KB / {{ addCommas(basicInfo.totalAvailableSpace) }} KB ({{ calculateProgressPercentage(basicInfo.totalAvailableSpace, basicInfo.totalFileSize).toFixed(2) }}%)</span>
                        <div class="d-flex align-items-center">
                            <div class="progress flex-grow-1" style="margin-right: 10px;">
                                <div class="progress-bar" role="progressbar" :style="{ width: calculateProgressPercentage(basicInfo.totalAvailableSpace, basicInfo.totalFileSize) + '%' , backgroundColor: calculateProgressBarColor(basicInfo.totalAvailableSpace, basicInfo.totalFileSize)}"
                                     aria-valuenow="50" aria-valuemin="0" aria-valuemax="100"></div>
                            </div>
                            <button @click="expandStorage" class="btn btn-link" style="padding: 0; text-decoration: underline; cursor: pointer;">申请扩容</button>
                        </div>
                    </div>




                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">用户注册时间:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ formatJavaDate(basicInfo.registrationDate) }}</span>
                    </div>
                </div>
            </div>
            <div class="tab-pane fade" id="u-t-personalization" role="tabpanel" aria-labelledby="overview-tab">
                <div class="user-info-container" style="font-size: 20px">
                    <h2 class="mt-3 mb-3">样式</h2>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">代码样式:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ customerInfo.codeStyle }}</span>
                        <button @click="changeCodeStyle" class="btn btn-link" data-bs-toggle="modal" style="padding: 0; text-decoration: underline; cursor: pointer;">更改
                        </button>
                    </div>
                </div>
            </div>
            <div class="tab-pane fade" id="u-t-settings" role="tabpanel" aria-labelledby="overview-tab">
                <div class="user-info-container" style="font-size: 20px">
                    <h2 class="mt-3 mb-3">个人设置</h2>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">最大登录保存时长:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ customerInfo.keepLoginMaxDays }}天</span>
                        <button @click="modifyKeepDays" class="btn btn-link"
                                style="padding: 0; text-decoration: underline; cursor: pointer;">更改
                        </button>
                    </div>
                </div>
            </div>
            <div class="tab-pane fade" id="u-t-account" role="tabpanel" aria-labelledby="overview-tab">
                <div class="user-info-container" style="font-size: 20px">
                    <h2 class="mt-3 mb-3">安全信息</h2>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">用户名:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ securityInfo.username }}</span>
                    </div>

                    <div class="border border-dark-subtle p-3 mb-2">
                        <span style="font-weight: bold; font-family: monospace;">邮箱:</span>
                        <span style="font-family: monospace;" class="hover-effect">{{ securityInfo.email }}</span>
                        <span style="font-family: monospace;">
                        <button @click="changeUserEmail" class="btn btn-link" data-bs-toggle="modal" data-bs-target="#changePasswordModal"
                                style="padding: 0; text-decoration: underline; cursor: pointer;">
                            {{ securityInfo.email ? '更改' : '添加' }}
                        </button>
                    </span>
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
            <!-- 添加/修改邮箱 -->
            <div class="modal fade" id="changeUserEmail" tabindex="-1" aria-labelledby="changeUserEmailLabel"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div id="changeEmailForm" style="padding-top: 30px">
                            <div class="mb-3 w-75 mx-auto">
                                <p style="font-weight: bold; font-size: 18px;">该功能暂未开放</p>
                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 修改代码样式 -->
            <div class="modal fade" id="changeCodeStyle" tabindex="-1" aria-labelledby="changeCodeStyleLabel" aria-hidden="true" >
                <div class="modal-dialog" >
                    <div class="modal-content" style="width: 700px">
                        <div id="changeCodeStyleForm" style="padding: 30px">
                            <label for="styleSelect" style="font-size: 18px; display: inline-block;">代码样式:</label>
                            <select class="form-control" id="styleSelect" style="width: 200px; display: inline-block;" v-model="customerInfoTemp.codeStyle">
                                <option value="default">default</option>
                                <option value="coy">coy</option>
                                <option value="dark">dark</option>
                                <option value="funky">funky</option>
                                <option value="okaidia">okaidia</option>
                                <option value="solarized-light">solarized-light</option>
                                <option value="tomorrow-night">tomorrow-night</option>
                                <option value="twilight">twilight</option>
                            </select>
                            <br/>
                            <br/>
                            <div id="example-area">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                            <button class="btn btn-danger" @click="saveCodeStyle">保存</button>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 修改最大保存登录天数 -->
            <div class="modal fade" id="modifyKeepDays" tabindex="-1" aria-labelledby="modifyKeepDaysLabel"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div id="modifyKeepDaysForm" style="padding-top: 30px">
                            <div class="mb-3 w-75 mx-auto">
                                <label for="cp-user-pwd" class="form-label">最大天数</label>
                                <input type="text" class="form-control" id="mx-kp-dys" name="MaxKeepdays"
                                       v-model="customerInfoTemp.keepLoginMaxDays" autocomplete="off">
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消
                                </button>
                                <button class="btn btn-danger" @click="doChangeKeepDays">保存</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script>
        var us = new Vue({
            el: '#u-c-content',
            data: {
                selected: 2,

                mainInfo: {},
                basicInfo: {
                    username:null,
                    userRole:null,
                    collectionCount:null,
                    articleCount:null,
                    totalFileSize:null,
                    totalAvailableSpace:null,
                    registrationDate:null
                },
                customerInfo: {
                    codeStyle:"",
                    keepLoginMaxDays:""
                },
                customerInfoTemp: {
                    codeStyle:"",
                    keepLoginMaxDays:""
                },
                settingsInfo: {
                    keepLoginMaxDays:null
                },

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
                select(index) {
                    this.selected = index;
                    if (index === 2) {
                        this.loadBasicInfo();
                    } else if (index === 3) {
                        this.loadCustomerInfo();
                    } else if (index === 4) {
                        this.loadSettingInfo();
                    } else if (index === 5) {
                        this.loadSecurityInfo();
                    }
                },
                loadBasicInfo() {
                    axios.get('${pageContext.request.contextPath}/user/getUserBasicInfo')
                        .then(response => {
                            if (response.data.status === 0) {
                                this.basicInfo = response.data.data;
                            } else {
                                windowToastManager.showToast('failed', '请求失败:' + response.data.message);
                            }
                        })
                        .catch(error => {
                            windowToastManager.showToast('failed', '请求失败:' + error);
                        });
                },

                loadCustomerInfo() {
                    this.loading = true;
                    axios.post('${pageContext.request.contextPath}/userConfig/getConfig')
                        .then(response => {
                            this.customerInfo = response.data.data.configInfo;
                            if(this.customerInfo.codeStyle==null){
                                this.customerInfo.codeStyle="okaidia"
                            }
                        })
                        .catch(error => {
                            windowToastManager.showToast('failed', '请求失败:' + error);
                        });
                },
                loadSettingInfo() {
                    this.loadCustomerInfo()
                },
                changeCodeStyle(){
                    var modal = new bootstrap.Modal(document.getElementById('changeCodeStyle'), {
                        keyboard: false
                    });

                    this.customerInfoTemp.codeStyle=this.customerInfo.codeStyle;

                    modal.show();
                    this.updateExample(null)
                },
                updateExample(style){

                    let newCSSLink = `${applicationScope.resUrlPrefix}${pageContext.request.contextPath}/static/js/md-show/prism-\${style}.css`;


                    let exampleCode=
                        `\`\`\`javascript\nfunction bubbleSort(arr) {
    const len = arr.length;
    for (let i = 0; i < len - 1; i++) {
        for (let j = 0; j < len - 1 - i; j++) {
            if (arr[j] > arr[j + 1]) {
                // 交换位置
                const temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
    return arr;
}

// 示例用法
const unsortedArray = [5, 3, 8, 4, 2];
const sortedArray = bubbleSort(unsortedArray);
console.log(sortedArray); // 输出 [2, 3, 4, 5, 8]
\`\`\``;
                    let converter=getShowdown()

                    const htmlContent1 = converter.makeHtml(exampleCode);

                    const previewElementLeft = document.getElementById("example-area");
                    previewElementLeft.innerHTML = htmlContent1;

                    Prism.highlightAll();

                    initClipboard()

                    if(style!=null){
                        var stylesheet = document.getElementById('codeStyleCss');
                        stylesheet.href = newCSSLink;
                    }

                },
                saveCodeStyle() {
                    // 创建 FormData 对象
                    const formData = new FormData();
                    // 将配置值添加到 FormData 对象中
                    formData.append('configValue', this.customerInfoTemp.codeStyle);

                    // 发送 POST 请求
                    axios.post('${pageContext.request.contextPath}/userConfig/saveCodeStyleConfig', formData)
                        .then(response => {
                            if (response.data.status === 0) {
                                windowToastManager.showToast('success', '修改成功');
                                $('#changeCodeStyle').modal('hide');
                                this.loadCustomerInfo()
                            } else {
                                windowToastManager.showToast('failed', '请求失败:' + response.data.message);
                            }
                        })
                        .catch(error => {
                            windowToastManager.showToast('failed', '请求失败:' + error);
                        });
                },

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
                changeUserEmail(){
                    $('#changeUserEmail').modal('show');
                },
                modifyKeepDays(){
                    $('#modifyKeepDays').modal('show')
                },
                doChangeKeepDays() {
                    // 校验参数是否为整数且大于0
                    const keepLoginMaxDays = parseInt(this.customerInfoTemp.keepLoginMaxDays);
                    if (isNaN(keepLoginMaxDays) || keepLoginMaxDays <= 0) {
                        windowToastManager.showToast('failed', '请输入大于0的整数');
                        return;
                    }

                    const formData = new FormData();
                    // 将配置值添加到 FormData 对象中
                    formData.append('keepLoginMaxDays', keepLoginMaxDays+"");
                    axios.post('${pageContext.request.contextPath}/userConfig/saveMaxKeepDays', formData)
                        .then(response => {
                            if (response.data.status === 0) {
                                windowToastManager.showToast('success', '修改成功');
                                $('#modifyKeepDays').modal('hide');
                                this.loadCustomerInfo();
                            } else {
                                windowToastManager.showToast('failed', '修改失败:' + response.data.message);
                            }
                        })
                        .catch(error => {
                            windowToastManager.showToast('failed', '修改失败:' + error);
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
                addCommas(value) {
                    value=value+""

                    return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                },
                calculateProgressPercentage(maxStorageSize,usedSpace) {
                    return (usedSpace / maxStorageSize) * 100;
                },
                calculateProgressBarColor(maxStorageSize, usedSpace) {
                    const progressPercentage = (usedSpace / maxStorageSize) * 100;
                    if (progressPercentage >= 85) {
                        return 'red';
                    } else if (progressPercentage >= 60) {
                        return 'orange';
                    } else {
                        return '';
                    }
                },

                expandStorage() {
                    // 扩容逻辑，此处为示例
                    alert("申请扩容功能暂未开放");
                }
            },
            watch: {
                'customerInfoTemp.codeStyle': {
                    handler(newValue, oldValue) {
                        this.updateExample(newValue);
                    },
                }
            },
            mounted() {
                this.loadBasicInfo();
            }
        });
    </script>
</div>
</body>
</html>
