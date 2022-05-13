DROP TABLE IF EXISTS `user`;
CREATE TABLE user
(
    `id`        CHAR(36) PRIMARY KEY NOT NULL,
    `username`  CHAR(64)             NOT NULL,
    `password`  CHAR(64)             NOT NULL,
    `openid`    CHAR(64),
    `gender`    CHAR(2),
    `money`     CHAR(64),
    `avatar`    DOUBLE,
    `timestamp` DATETIME(0)          NOT NULL
);

DROP TABLE IF EXISTS `achievement`;
CREATE TABLE `achievement`
(
    `id`            CHAR(36) PRIMARY KEY NOT NULL,
    `win_match`     int(11)              NULL DEFAULT NULL,
    `failure_match` int(11)              NULL DEFAULT NULL,
    `sum`           int(11)              NULL DEFAULT NULL,
    `user_id`       CHAR(36)             NULL DEFAULT NULL
);

