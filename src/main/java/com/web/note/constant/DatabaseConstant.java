package com.web.note.constant;

import java.util.HashMap;
import java.util.Map;

public class DatabaseConstant {
    public static final int USER_OK = 0;
    public static final int USER_DISABLE = 1;
    public static final int USER_TO_PERMIT = 2;

    public static final boolean COLLECTION_PUBLIC = true;
    public static final boolean COLLECTION_PRIVATE = false;

    public static final String DOCUMENT_HTML = "html";
    public static final String DOCUMENT_MARKDOWN = "markdown";

    public static final String CONTENT_EMPTY = "";
    public static final int ATTEMPT_ZERO = 0;


    public static final int FILE_RESOURCE_OK = 0;

    public static final int FILE_PERMISSION_PUBLIC = 0;
    public static final int FILE_PERMISSION_AUTH_CODE = 1;
    public static final int FILE_PERMISSION_PRIVATE = 2;


    public static final int COMMON_USER = 0;
    public static final int SYSTEM_ADMIN = 1;
    public static final int BUSINESS_ADMIN = 2;

    //系统设置常量
    public static final String CONFIG_REGISTER_MODE = "register_mode";
    public static final int CONFIG_REGISTER_VALUE_PERMIT = 0;
    public static final int CONFIG_REGISTER_VALUE_DENY = 1;
    public static final int CONFIG_REGISTER_VALUE_APPLY = 2;

    public static final String CONFIG_USER_SPACE = "user_space";

    public static final String DEFAULT_SYSTEM_ADMIN_NAME = "administrator";


    public static final String SYSTEM_CONFIG_SYSTEM_INIT = "system_init";
    public static final int SYSTEM_CONFIG_SYSTEM_INIT_OK = 0;
    public static final int SYSTEM_CONFIG_SYSTEM_INIT_ADMIN = 1;
    public static final int SYSTEM_CONFIG_SYSTEM_INIT_SYSTEM = 2;

    public static final String REGISTER_APPLICATION="register_application";
    public static final int APP_NOT_HANDLE=0;
    public static final int APP_DENY=1;
    public static final int APP_PERMIT=2;
    public static final int APP_FAILED=10;

    public static final String FILE_TYPE_FILE="default";
    public static final String FILE_TYPE_IMAGE="image";
    public static final String FILE_TYPE_HTML="html";
    public static final String FILE_TYPE_VIDEO="video";
    public static final String FILE_TYPE_MUSIC="music";

    public static final String[] CODE_STYLE_LIST ={"coy","dark","default","funky","okaidia","solarized-light","tomorrow-night","twilight"};


    public static final String CODE_STYLE="codeStyle";
    public static final String KEEP_LOGIN_MAX_DAYS="keepLoginMaxDays";

    public static final HashMap<String,String> USER_CONFIG;
    static{
        USER_CONFIG=new HashMap<>();
        USER_CONFIG.put(CODE_STYLE,"okaidia");
        USER_CONFIG.put(KEEP_LOGIN_MAX_DAYS,"7");
    }

}
