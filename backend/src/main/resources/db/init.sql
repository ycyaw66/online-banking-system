DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `cardofperson`;

CREATE TABLE `user`
(	(
    id BIGINT NOT NULL COMMENT '主键ID',	    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(25) NOT NULL COMMENT '用户名', 	    username VARCHAR(25) NOT NULL COMMENT '用户名', 
    password VARCHAR(25) NOT NULL COMMENT '密码',	    password VARCHAR(25) NOT NULL COMMENT '密码',
    id_number INT NULL DEFAULT NULL COMMENT '身份证号',	    id_number VARCHAR(25) NULL DEFAULT NULL COMMENT '身份证号',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',	    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    phone_number VARCHAR(20) NULL DEFAULT NULL COMMENT '手机号', 	    phone_number VARCHAR(20) NULL DEFAULT NULL COMMENT '手机号', 
    PRIMARY KEY (id)	    PRIMARY KEY (id)
);	) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

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