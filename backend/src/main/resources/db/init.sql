DROP TABLE IF EXISTS `cardofperson`;
DROP TABLE IF EXISTS `UserPrivilege`;
DROP TABLE IF EXISTS `blacklist`;
DROP TABLE IF EXISTS `user`;


CREATE TABLE `user`
(
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(25) NOT NULL COMMENT '用户名', 
    password VARCHAR(64) NOT NULL COMMENT '密码',
    id_number VARCHAR(25) NULL DEFAULT NULL COMMENT '身份证号',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    phone_number VARCHAR(20) NULL DEFAULT NULL COMMENT '手机号', 
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `cardofperson`
(
    user_id BIGINT NOT NULL COMMENT '人员ID',
    card_id BIGINT NOT NULL COMMENT '卡ID',
    PRIMARY KEY (user_id, card_id)
);

CREATE TABLE `UserPrivilege`
(
    user_id BIGINT NOT NULL COMMENT '人员ID',
    transcations BOOLEAN NOT NULL COMMENT '交易记录查看权限',
    transfer BOOLEAN NOT NULL COMMENT '转账权限',
    loss BOOLEAN NOT NULL COMMENT '挂失权限',
    PRIMARY KEY(user_id),
    FOREIGN KEY(user_id) REFERENCES user(id)
);

CREATE TABLE `blacklist`
(
    user_id BIGINT NOT NULL COMMENT '人员ID',
    reason VARCHAR(100) NOT NULL COMMENT '拉黑原因',
    PRIMARY KEY (user_id),
    foreign key (user_id) references user(id)
);