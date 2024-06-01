drop table if exists `credit_card_admin` cascade;
drop table if exists `credit_card_application` cascade;
drop table if exists `credit_card_bill` cascade;
drop table if exists `credit_card_inspector` cascade;
drop table if exists `credit_card` cascade;
DROP TABLE IF EXISTS `admin` cascade;
DROP TABLE IF EXISTS `cardofperson` cascade;
DROP TABLE IF EXISTS `user_privilege` cascade;
DROP TABLE IF EXISTS `blacklist` cascade;
DROP TABLE IF EXISTS `history` cascade;
DROP TABLE IF EXISTS `user` cascade;
DROP TABLE IF EXISTS `card` cascade;

CREATE TABLE `card`
(
    card_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '卡ID',
    card_type INTEGER NOT NULL COMMENT '卡类型',
    PRIMARY KEY(card_id)
);

CREATE TABLE `user`
(
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(25) UNIQUE NOT NULL COMMENT '用户名', 
    password VARCHAR(64) NOT NULL COMMENT '密码',
    id_number VARCHAR(25) NULL DEFAULT NULL COMMENT '身份证号',
    email VARCHAR(50) UNIQUE NOT NULL COMMENT '邮箱',
    phone_number VARCHAR(20) NULL DEFAULT NULL COMMENT '手机号', 
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `cardofperson`
(
    user_id BIGINT NOT NULL COMMENT '人员ID',
    card_id BIGINT NOT NULL COMMENT '卡ID',
    FOREIGN KEY(user_id) REFERENCES user(id),
    FOREIGN KEY(card_id) REFERENCES card(card_id),
    PRIMARY KEY (user_id, card_id)
);

CREATE TABLE `user_privilege`
(
    user_id BIGINT NOT NULL COMMENT '人员ID',
    payment BOOLEAN NOT NULL COMMENT '支付权限',
    transfer BOOLEAN NOT NULL COMMENT '转账权限',
    receive BOOLEAN NOT NULL COMMENT '收款权限',
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

CREATE TABLE `history`
(
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    card_id BIGINT NOT NULL COMMENT '卡ID',
    target_card BIGINT NOT NULL COMMENT '对方卡ID',
    amount DECIMAL(15, 2) NOT NULL COMMENT '交易金额',
    time BIGINT NOT NULL COMMENT '交易时间戳',
    remark VARCHAR(64) NOT NULL COMMENT '备注',
    FOREIGN KEY(card_id) REFERENCES card(card_id),
    FOREIGN KEY(target_card) REFERENCES card(card_id),
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `admin`
(
    `id` int not null auto_increment,
    `username` varchar(50) not null,
    `password` varchar(64) not null,
    `role` varchar(64) not null, 
    primary key (`id`)
);

CREATE TABLE `credit_card` (
    `id` bigint not null,
    `id_number` varchar(50) not null,
    `password` varchar(64),
    `card_limit` DECIMAL(15, 2) not null default 0,
    `loan` DECIMAL(15, 2) not null default 0,
    `is_lost` int not null default 0,
    primary key(`id`),
    FOREIGN KEY(`id`) REFERENCES card(`card_id`),
    check(is_lost in (0, 1))
)engine=innodb DEFAULT charset=utf8mb4;

create table `credit_card_application` (
    `id` int not null auto_increment,
    `id_number` varchar(50) not null,
    `credit_card_id` bigint,
    `amount` DECIMAL(15, 2) not null default 0,
    `type` int not null,
    `status` int not null,
    `password` varchar(64),
    primary key (`id`),
    FOREIGN KEY(`credit_card_id`) REFERENCES credit_card(`id`)
) engine=innodb charset=utf8mb4;

create table `credit_card_bill` (
    `id` int not null auto_increment,
    `credit_card_id` bigint not null,
    `amount` DECIMAL(15, 2) not null default 0,
    `bill_date` DATE,
    primary key (`id`),
    FOREIGN KEY(`credit_card_id`) REFERENCES credit_card(`id`)
) engine=innodb charset=utf8mb4;

create table `credit_card_admin` (
  `id` int not null auto_increment,
  `name` varchar(50) not null,
  `password` varchar(64) not null,
  primary key (`id`)
) engine=innodb charset=utf8mb4;

create table `credit_card_inspector` (
    `id` int not null auto_increment,
    `name` varchar(50) not null,
    `password` varchar (50) not null,
    `permission` int not null,
    primary key(`id`),
    check(`permission` in (1, 2))
)engine=innodb charset=utf8mb4;
