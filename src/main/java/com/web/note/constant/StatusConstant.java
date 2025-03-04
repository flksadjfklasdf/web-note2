package com.web.note.constant;

public class StatusConstant {
    //返回正常
    public static final int OK = 0;
    //没有执行有效的操作
    public static final int DO_NOTHING = 10;
    //用户输入不合法
    public static final int INVALID_INPUT = 1400;
    // 用户可用空间不足
    public static final int INSUFFICIENT_SPACE = 1401;
    //未登录
    public static final int UN_LOGIN = 1481;
    //未授权
    public static final int UN_AUTH = 1482;
    //页面未找到
    public static final int PAGE_NOT_FOUND = 1483;
    //服务错误
    public static final int SERVICE_ERROR = 1499;
    //没有找到结果
    public static final int NO_RESULT = 1001;
    //应用未初始化
    public static final int NO_INIT = 1420;
    //操作被拒绝
    public static final int REQUEST_REFUSED = 1430;
    //404
    public static final int STATUS_404 = 404;
    //业务逻辑错误
    public static final int BUSINESS_ERROR=1500;


    //------------------------------------内部
    public static final int LOGIN_ATTEMPTS_TO_MANY = 10001;
}
