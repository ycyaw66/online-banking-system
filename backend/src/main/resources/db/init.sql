drop table if exists `credit_card_admin` cascade;
drop table if exists `credit_card_application` cascade;
drop table if exists `credit_card_bill` cascade;
drop table if exists `credit_card_inspector` cascade;
drop table if exists `credit_card` cascade;

drop table if exists `administrator` cascade;
drop table if exists `cashier` cascade;
drop table if exists `deposite_card` cascade;
drop table if exists `demand_deposit` cascade;
drop table if exists `fixed_deposit` cascade;
drop table if exists `property` cascade;
drop table if exists `statement` cascade;
drop table if exists `account` cascade;
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
    time DATE NOT NULL COMMENT '交易时间',
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

create table `cashier` (
    `id` bigint(10) zerofill not null auto_increment ,
    `username` varchar(63) not null,
    `password` varchar(255) not null,
    `authority` int not null default 0,
     primary key (`id`),
     unique (`username`, `password`),
    check(authority in (0,1,2,3))
) engine=innodb charset=utf8mb4;

-- 储蓄账号
create table `account` (
    `id` bigint not null,
    `name` varchar(63) not null,
    `phonenumber` varchar(11) not null,
    `citizenid` varchar(18) not null,
    `status` int not null default 1,
    `card_id` bigint zerofill not null,
    `password` varchar(64) not null,
    primary key (`id`),
    foreign key(`id`) references card(`card_id`),
    unique(card_id),
    check ( `status` in (1,2,3))
) engine=innodb charset=utf8mb4;

create table `deposite_card` (
    `id` bigint auto_increment not null,
    `type` int not null,
    `accountid`  bigint  not null,
    primary key (`id`),
    foreign key (accountid)references account(id),
    check (`type` in (1,2) )
) engine=innodb charset=utf8mb4;

create table `property` (
    `id` bigint   not null auto_increment,
    `accountid` bigint  not null,
    `type` int not null,
    primary key (`id`),
    foreign key (accountid)references account(id) on delete cascade on update cascade,
    check ( `type` in (1, 2) )
) engine=innodb charset=utf8mb4;

create table `demand_deposit` (
    `propertyid` bigint   not null ,
    `accountid`  bigint  not null,
    `amount` decimal(15,2) not null default 0.00,
    `date` bigint not null,
    `base` decimal(15,2) not null default 0.00,
    check(amount >= 0),
    foreign key (`propertyid`) references `property`(`id`) on delete cascade on update cascade,
    foreign key (`accountid`) references `account`(`id`) on delete cascade on update cascade
) engine=innodb charset=utf8mb4;

create table `fixed_deposit` (
    `propertyid` bigint   not null ,
    `accountid`  bigint  not null,
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
    `accountid`  bigint  not null,
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

DROP TABLE IF EXISTS `history_operation_record`;
DROP TABLE IF EXISTS `foreign_currency_account`;
DROP TABLE IF EXISTS `trade_record`;
DROP TABLE IF EXISTS `foreign_currency_rate`;
DROP TABLE IF EXISTS `foreign_currency`;
DROP TABLE IF EXISTS `data_operator`;
DROP TABLE IF EXISTS `fc_administrator`;

-- 关于如何构造字符类型的id，可以用时间戳去掉中间的多余字符，再加上一个数字
-- 2020-01-01 00:00:00->"20200101000000"+"000000"


CREATE TABLE `data_operator`
(
    data_operator_id VARCHAR(20) NOT NULL COMMENT '主键ID',
    username VARCHAR(25) NOT NULL COMMENT '用户名', 
    password VARCHAR(64) NOT NULL COMMENT '密码',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    phone_number VARCHAR(20) NULL DEFAULT NULL COMMENT '手机号', 
    add_permission BOOLEAN DEFAULT FALSE,
    delete_permission BOOLEAN DEFAULT FALSE,
    update_permission BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (data_operator_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `foreign_currency`
(
    fc_id VARCHAR(20) NOT NULL COMMENT '外币ID',
    fc_name VARCHAR(20) NOT NULL COMMENT '外币名称', 
    PRIMARY KEY (fc_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- 外币汇率表 + 外币信息表 = 原来的外币汇率表 做了一个拆分

CREATE TABLE `foreign_currency_rate`
(
    fc_id VARCHAR(20) NOT NULL COMMENT '外币ID',
    fc_date Datetime NOT NULL COMMENT '汇率日期',
    fc_rate NUMERIC(7, 4) NOT NULL COMMENT '外币汇率',
    PRIMARY KEY (fc_id, fc_date),
    FOREIGN KEY (fc_id) REFERENCES `foreign_currency`(fc_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- 用户id需要根据别组的情况去改，目前先不写外键约束，后续再补,user_id 可有可无

-- 这里买入则RMB->外币,RMB减少，这么处理主要是汇率会变，只记录一个交易金额不好算

CREATE TABLE `trade_record`
(
    trade_id VARCHAR(20) NOT NULL COMMENT '交易ID',
    fc_id VARCHAR(20) NOT NULL COMMENT '外币ID',
    trade_time Datetime NOT NULL COMMENT '交易日期',
    amount_cny NUMERIC(12, 2) NOT NULL COMMENT '交易金额-RMB',
    amount_foreign_currency NUMERIC(12, 2) NOT NULL COMMENT '交易金额-外币',
    user_id VARCHAR(20) NOT NULL COMMENT '用户ID', 
    credit_card_id VARCHAR(20) NOT NULL COMMENT '信用卡ID',
    is_buy_in BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (trade_id),
    FOREIGN KEY (fc_id) REFERENCES `foreign_currency`(fc_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- 信用卡id需要根据别组的情况去改，目前先不写外键约束，后续再补

CREATE TABLE `foreign_currency_account`
(
    credit_card_id VARCHAR(20) NOT NULL COMMENT '信用卡ID',
    fc_id VARCHAR(20) NOT NULL COMMENT '外币ID',
    amount NUMERIC(12, 2) NOT NULL COMMENT '额度',
    PRIMARY KEY (fc_id, credit_card_id),
    FOREIGN KEY (fc_id) REFERENCES `foreign_currency`(fc_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `history_operation_record`
(
    record_id VARCHAR(20) NOT NULL COMMENT '记录ID',
    data_operator_id VARCHAR(20) NOT NULL COMMENT '操作员ID',
    fc_id VARCHAR(20) NOT NULL COMMENT '外币ID',
    operation VARCHAR(20) NOT NULL COMMENT '操作类型',
    old_rate NUMERIC(7, 4) COMMENT '原本汇率',
    new_rate NUMERIC(7, 4) NOT NULL COMMENT '现汇率',
    dest_date Datetime NOT NULL COMMENT '目标日期',
    operation_time Datetime NOT NULL COMMENT '操作日期',
    PRIMARY KEY (record_id),
    FOREIGN KEY (fc_id) REFERENCES `foreign_currency`(fc_id),
    FOREIGN KEY (data_operator_id) REFERENCES `data_operator`(data_operator_id) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


drop table if exists `loan`;
drop table if exists `form`;
drop table if exists `report`;
drop table if exists `officer`; 
drop table if exists `reminder`; 

-- 创建申请表单表
CREATE TABLE form (
    form_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    id_number CHAR(18) NOT NULL,
    gender ENUM('male', 'female') NOT NULL,
    emotion ENUM('single', 'married') NOT NULL,
    income INT NOT NULL,
    address VARCHAR(200) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(64) NOT NULL,
    education VARCHAR(64) NOT NULL,
    purpose VARCHAR(200) NOT NULL,
    statement TEXT(2000) NOT NULL
);

-- 创建征信报告表
CREATE TABLE report (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    credit_limit DECIMAL(12, 2) NOT NULL,
    date DATE NOT NULL
);

-- 创建贷款审查员表
CREATE TABLE officer (
    officer_id INT AUTO_INCREMENT PRIMARY KEY,
    officer_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    username CHAR(20) NOT NULL,
    password CHAR(64) NOT NULL,
    permissions ENUM('small', 'large') NOT NULL
);

-- 创建贷款表
CREATE TABLE loan (
    loan_id INT AUTO_INCREMENT PRIMARY KEY,
    borrow_id INT NOT NULL,
    card_id INT NOT NULL,
    officer_id INT,
    amount NUMERIC(12, 2) NOT NULL,
    rate NUMERIC(4, 2) NOT NULL,
    term INT NOT NULL,
    status ENUM('application', 'declined', 'repayment', 'settled', 'overdue') NOT NULL,
    date_applied DATE NOT NULL,
    date_approved DATE,
    form_id INT NOT NULL,
    FOREIGN KEY (officer_id) REFERENCES officer(officer_id),
    FOREIGN KEY (form_id) REFERENCES form(form_id)
);



-- 创建还款提醒表
CREATE TABLE reminder (
    reminder_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    time INT NOT NULL
);