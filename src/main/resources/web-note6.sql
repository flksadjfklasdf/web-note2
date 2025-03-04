DROP DATABASE IF EXISTS web_note2;

CREATE DATABASE web_note2;

USE web_note2;

CREATE TABLE user
(
    user_id            VARCHAR(255) PRIMARY KEY,
    username           VARCHAR(255) NOT NULL UNIQUE,
    password_hash      VARCHAR(255) NOT NULL,
    email              VARCHAR(255),
    user_type          INT          NOT NULL,
    status             INT          NOT NULL,
    created_at         DATETIME     NOT NULL,
    profile_picture_id VARCHAR(255),
    last_login_ip      VARCHAR(255),
    last_login_at      DATETIME,
    login_attempts     INT,
    storage_limit      INT
);

CREATE TABLE collection
(
    collection_id          VARCHAR(255) PRIMARY KEY,
    user_id                VARCHAR(255) NOT NULL,
    collection_name        VARCHAR(255) NOT NULL,
    collection_description TEXT,
    collection_type        VARCHAR(255),
    is_public              BOOLEAN      NOT NULL,
    created_at             DATETIME     NOT NULL,
    INDEX idx_user_id (user_id)
);

CREATE TABLE document
(
    document_id      VARCHAR(255) PRIMARY KEY,
    collection_id    VARCHAR(255) NOT NULL,
    user_id          VARCHAR(255) NOT NULL,
    document_name    VARCHAR(255) NOT NULL,
    document_type    VARCHAR(255),
    document_content TEXT         NOT NULL,
    created_at       DATETIME     NOT NULL,
    updated_at       DATETIME     NOT NULL,
    sort_order       INT          DEFAULT 9999,
    INDEX idx_user_id (user_id),
    INDEX idx_coll_id (collection_id),
    INDEX order_idx(sort_order)
);

CREATE TABLE image
(
    image_id      VARCHAR(255) PRIMARY KEY,
    document_id   VARCHAR(255),
    collection_id VARCHAR(255),
    user_id       VARCHAR(255),
    image_name    VARCHAR(255),
    image_size    INT,
    created_at    DATETIME NOT NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_coll_id (collection_id),
    INDEX idx_docu_id (document_id)
);

CREATE TABLE file
(
    file_id           VARCHAR(255) PRIMARY KEY,
    user_id           VARCHAR(255) NOT NULL,
    original_filename VARCHAR(255),
    local_filename    VARCHAR(255) NOT NULL,
    file_size         INT          NOT NULL,
    uploaded_at       DATETIME     NOT NULL,
    permission        INT          NOT NULL,
    auth_code         VARCHAR(255),
    file_status       INT,
    file_md5          VARCHAR(255),
    file_type         VARCHAR(255),
    file_note         VARCHAR(255),
    INDEX idx_user_id (user_id)
);

CREATE TABLE comment
(
    comment_id      VARCHAR(255) PRIMARY KEY,
    user_id         VARCHAR(255) NOT NULL,
    target_document VARCHAR(255) NOT NULL,
    comment_content VARCHAR(255) NOT NULL,
    created_at      DATETIME     NOT NULL,
    INDEX idx_user_id (user_id)
);

CREATE TABLE notification
(
    notification_id      VARCHAR(255) PRIMARY KEY,
    notification_user_id VARCHAR(255),
    notification_content TEXT,
    notification_time    DATETIME,
    notification_status  INT,
    INDEX idx_user_id (notification_user_id)
);

CREATE TABLE system_config
(
    config_item  VARCHAR(255) PRIMARY KEY,
    config_name  VARCHAR(255),
    config_value VARCHAR(255),
    config_range VARCHAR(255),
    INDEX idx_config_name (config_name)
);

CREATE TABLE application
(
    app_id             VARCHAR(255) PRIMARY KEY,
    app_user_id        VARCHAR(255) NOT NULL,
    app_type           VARCHAR(255) NOT NULL,
    app_value          VARCHAR(255),
    app_content        VARCHAR(255),
    app_data           TEXT,
    app_status         INT,
    app_submit_date    DATETIME     NOT NULL,
    app_deal_date      DATETIME,
    app_deal_user_id   VARCHAR(255),
    app_deal_user_name VARCHAR(255),
    INDEX idx_user_id (app_user_id)
);

CREATE TABLE user_config
(
    config_id    VARCHAR(255) PRIMARY KEY,
    user_id      VARCHAR(255) NOT NULL,
    config_key   VARCHAR(255) NOT NULL,
    config_value VARCHAR(255),
    created_at   DATETIME     NOT NULL,
    updated_at   DATETIME     NOT NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_config_key (config_key)
);

CREATE TABLE session
(
    session_id  VARCHAR(255) PRIMARY KEY,
    user_id     VARCHAR(255) NOT NULL,
    create_date DATETIME     NOT NULL,
    expiry_date DATETIME     NOT NULL,
    create_info TEXT,
    INDEX idx_user_id (user_id),
    INDEX idx_create_date (create_date),
    INDEX idx_expiry_date (expiry_date)
);

CREATE TABLE ip_ban (
                        ip VARCHAR(45) PRIMARY KEY,
                        ban_type INT(10) NOT NULL,
                        start_time DATETIME NOT NULL,
                        end_time DATETIME NULL DEFAULT NULL,
                        reason VARCHAR(255) NULL ,
                        created_at TIMESTAMP,
                        INDEX idx_ban_type (ban_type),
                        INDEX idx_start_time (start_time) ,
                        INDEX idx_end_time (end_time)
);

CREATE TABLE file_type (
                           id CHAR(32) PRIMARY KEY,
                           type_name_en VARCHAR(255) NOT NULL,
                           type_name_cn VARCHAR(255) NOT NULL,
                           content_type VARCHAR(255) NOT NULL
);


INSERT INTO system_config(config_item, config_name, config_value, config_range)
VALUES ('system_init', '初始化系统', '1', NULL),
       ('register_mode', '允许用户注册', '1', NULL),
       ('user_space', '用户初始储存空间', '10GB', NULL);
INSERT INTO `file_type` (`id`, `type_name_en`, `type_name_cn`, `content_type`) VALUES ('1d14e552201b4114900869c755c3c796', 'html', '网页', 'text/html');
INSERT INTO `file_type` (`id`, `type_name_en`, `type_name_cn`, `content_type`) VALUES ('65a1653a10dd46129e73db23c6e159ed', 'pdf', 'PDF', 'application/pdf');
INSERT INTO `file_type` (`id`, `type_name_en`, `type_name_cn`, `content_type`) VALUES ('9832704033e44936b8d03e67538365ec', 'video', '视频', 'video/mp4');
INSERT INTO `file_type` (`id`, `type_name_en`, `type_name_cn`, `content_type`) VALUES ('c5c5028927514bdaa6abc8baf126f1f6', 'music', '音乐', 'audio/mpeg');
INSERT INTO `file_type` (`id`, `type_name_en`, `type_name_cn`, `content_type`) VALUES ('c623ee478f4c4821898bc759daa3f4bc', 'image', '图片', 'image/png');

