DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `cardofperson`;

CREATE TABLE `user`
(
    id BIGINT AUTO_INCREMENT NOT NULL COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    identityNumber VARCHAR(18) NOT NULL COMMENT '身份证号',
    age INT NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
);

CREATE TABLE 'cardofperson'
(
    userId BIGINT NOT NULL COMMENT '人员ID',
    cardId BIGINT NOT NULL COMMENT '卡ID',
    PRIMARY KEY (userId, cardId)
);

CREATE TABLE 'UserPrivilege'
(
    userId BIGINT NOT NULL COMMENT '人员ID',
    transcations BOOLEAN NOT NULL COMMENT '交易记录查看权限',
    transfer BOOLEAN NOT NULL COMMENT '转账权限',
    loss BOOLEAN NOT NULL COMMENT '挂失权限',
    PRIMARY KEY (userId)
    foreign key (userId) references user(id)
);

CREATE TABLE 'blacklist'
(
    userId BIGINT NOT NULL COMMENT '人员ID',
    reason VARCHAR(100) NOT NULL COMMENT '拉黑原因',
    PRIMARY KEY (userId)
    foreign key (userId) references user(id)
)