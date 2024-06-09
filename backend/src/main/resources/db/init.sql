drop table if exists `credit_card_admin` cascade;
drop table if exists `credit_card_application` cascade;
drop table if exists `credit_card_bill` cascade;
drop table if exists `credit_card_inspector` cascade;
drop table if exists `credit_card` cascade;

drop table if exists `administrator` cascade;
drop table if exists `cashier` cascade;
drop table if exists `account` cascade;
drop table if exists `deposite_card` cascade;
drop table if exists `property` cascade;
drop table if exists `demand_deposit` cascade;
drop table if exists `fixed_deposit` cascade;
drop table if exists `statement` cascade;
drop table if exists `rate` cascade;

DROP TABLE IF EXISTS `admin` cascade;
DROP TABLE IF EXISTS `cardofperson` cascade;
DROP TABLE IF EXISTS `user_privilege` cascade;
DROP TABLE IF EXISTS `blacklist` cascade;
DROP TABLE IF EXISTS `history` cascade;
DROP TABLE IF EXISTS `user` cascade;
DROP TABLE IF EXISTS `card` cascade;




-- 用户的基本信息的表 
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

-- 用户卡的表，包括信用卡和储蓄卡
CREATE TABLE `card`
(
    card_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '卡ID',
    card_type INTEGER NOT NULL COMMENT '卡类型',
    PRIMARY KEY(card_id)
);

-- 用户拥有卡的表
CREATE TABLE `cardofperson`
(
    user_id BIGINT NOT NULL COMMENT '人员ID',
    card_id BIGINT NOT NULL COMMENT '卡ID',
    FOREIGN KEY(user_id) REFERENCES user(id),
    FOREIGN KEY(card_id) REFERENCES card(card_id),
    PRIMARY KEY (user_id, card_id)
);

-- 用户权限表
CREATE TABLE `user_privilege`
(
    user_id BIGINT NOT NULL COMMENT '人员ID',
    payment BOOLEAN NOT NULL COMMENT '支付权限',
    transfer BOOLEAN NOT NULL COMMENT '转账权限',
    receive BOOLEAN NOT NULL COMMENT '收款权限',
    PRIMARY KEY(user_id),
    FOREIGN KEY(user_id) REFERENCES user(id)
);

-- 黑名单
CREATE TABLE `blacklist`
(
    user_id BIGINT NOT NULL COMMENT '人员ID',
    reason VARCHAR(100) NOT NULL COMMENT '拉黑原因',
    PRIMARY KEY (user_id),
    foreign key (user_id) references user(id)
);

-- 交易历史记录
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

-- 管理员
CREATE TABLE `admin`
(
    `id` int not null auto_increment,
    `username` varchar(50) not null,
    `password` varchar(64) not null,
    `role` varchar(64) not null, 
    primary key (`id`)
);

-- 信用卡
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

-- 信用卡的申请
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

-- 信用卡账单
create table `credit_card_bill` (
    `id` int not null auto_increment,
    `id_number` varchar(50) not null, 
    `credit_card_id` bigint not null,
    `amount` DECIMAL(15, 2) not null default 0,
    `bill_date` DATE,
    primary key (`id`),
    FOREIGN KEY(`credit_card_id`) REFERENCES credit_card(`id`)
) engine=innodb charset=utf8mb4;

-- 信用卡审查员
create table `credit_card_inspector` (
    `id` int not null auto_increment,
    `name` varchar(50) not null,
    `password` varchar (64) not null,
    `permission` int not null,
    primary key(`id`),
    check(`permission` in (1, 2))
)engine=innodb charset=utf8mb4;

create table `administrator` (
    `id` bigint(10) zerofill not null auto_increment ,
    `username` varchar(63) not null,
    `password` varchar(255) not null,
    `salt` varchar(255) not null,
    primary key (`id`)
)  AUTO_INCREMENT=2000000000 engine=innodb charset=utf8mb4;

create table `cashier` (
    `id` bigint(10) zerofill not null auto_increment ,
    `username` varchar(63) not null,
    `password` varchar(255) not null,
    `authority` int not null default 0,
    `salt` varchar(255) not null,
     primary key (`id`),
     unique (`username`, `password`),
    check(authority in (0,1,2,3))
)  AUTO_INCREMENT=1000000000 engine=innodb charset=utf8mb4;

-- 储蓄账号
create table `account` (
    `id` bigint(12)   zerofill not null auto_increment ,
    `name` varchar(63) not null,
    `phonenumber` varchar(12) not null,
    `citizenid` varchar(18) not null,
    `status` int not null default 1,
    `card_id` bigint(14)   zerofill not null,
    `password` varchar(255) not null,
    `salt` varchar(255) not null,
    primary key (`id`),
    unique(card_id),
    check ( `status` in (1,2,3))
) engine=innodb charset=utf8mb4;

create table `deposite_card` (
    `id` bigint(16)   zerofill not null auto_increment ,
    `type` int not null,
    `accountid`  bigint(12)   zerofill not null,
    primary key (`id`),
    foreign key (accountid)references account(id) on delete cascade on update cascade,
    check (`type` in (1,2) )
) AUTO_INCREMENT=1000000000000000 engine=innodb charset=utf8mb4;

create table `property` (
    `id` bigint   not null auto_increment,
    `accountid` bigint(12)   zerofill not null,
    `type` int not null,
    primary key (`id`),
    foreign key (accountid)references account(id) on delete cascade on update cascade,
    check ( `type` in (1, 2) )
) engine=innodb charset=utf8mb4;

create table `demand_deposit` (
    `propertyid` bigint   not null ,
    `accountid`  bigint(12)   zerofill not null,
    `amount` decimal(15,2) not null default 0.00,
    `date` bigint not null,
    `base` decimal(15,2) not null default 0.00,
    check(amount >= 0),
    foreign key (`propertyid`) references `property`(`id`) on delete cascade on update cascade,
    foreign key (`accountid`) references `account`(`id`) on delete cascade on update cascade
) engine=innodb charset=utf8mb4;

create table `fixed_deposit` (
    `propertyid` bigint   not null ,
    `accountid`  bigint(12)   zerofill not null,
    `amount` decimal(15,2) not null default 0.00,
    `date` bigint not null,
    `length` int not null,
    `interestrate` decimal(4,2) not null default 0.00,
    `autocontinue` bool default true,
    check(amount >= 0),
    foreign key (`propertyid`) references `property`(`id`) on delete cascade on update cascade,
    foreign key (`accountid`) references `account`(`id`) on delete cascade on update cascade
) engine=innodb charset=utf8mb4;

create table `statement` (
    `accountid`  bigint(12)   zerofill not null,
    `amount` decimal(15,2) not null default 0.00,
    `date` bigint not null,
    `type` int not null default 0,
    `traced` bigint not null,
    check ( `type` in (1,2,3,4,5,6,7,8) ),
    foreign key (`accountid`) references `account`(`id`) on delete cascade on update cascade
) engine=innodb charset=utf8mb4;

create table `rate`(
    `demand_rate` decimal(3,2),
    `_3month_rate` decimal(3,2),
    `_6month_rate` decimal(3,2),
    `_1year_rate`decimal(3,2),
    `_2year_rate`decimal(3,2),
    `_3year_rate`decimal(3,2),
    `_5year_rate`decimal(3,2)
)engine=innodb charset=utf8mb4;