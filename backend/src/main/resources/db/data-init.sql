DELETE FROM `user`;
DELETE FROM `card`;
DELETE FROM `cardofperson`;
DELETE FROM `credit_card`;


INSERT INTO `card` (card_id, card_type) VALUES
(1, 2);
-- system card!! don't remove it !!!


INSERT INTO `user` (username, password, id_number, email, phone_number) VALUES
('a1', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 
'134015851838831650', 'test1@zju.edu.cn', '12967777593');


INSERT INTO `user` (username, password, id_number, email, phone_number) VALUES
('a2', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 
'134015851838831651', 'test2@zju.edu.cn', '12967777594');

INSERT INTO `user` (username, password, id_number, email, phone_number) VALUES
('a3', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 
'134015851838831652', 'test3@zju.edu.cn', '12967777595');

INSERT INTO `user` (username, password, id_number, email, phone_number) VALUES
('a4', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 
'134015851838831653', 'test4@zju.edu.cn', '12967777596');

INSERT INTO `user` (username, password, id_number, email, phone_number) VALUES
('a5', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 
'134015851838831654', 'test5@zju.edu.cn', '12967777597');

-- end of user example

INSERT INTO `card` (card_id, card_type) VALUES
(2, 0);

INSERT INTO `card` (card_id, card_type) VALUES
(3, 1);

INSERT INTO `card` (card_id, card_type) VALUES
(4, 0);

INSERT INTO `card` (card_id, card_type) VALUES
(5, 1);

INSERT INTO `card` (card_id, card_type) VALUES
(6, 0);

INSERT INTO `card` (card_id, card_type) VALUES
(7, 1);

INSERT INTO `card` (card_id, card_type) VALUES
(8, 0);

INSERT INTO `card` (card_id, card_type) VALUES
(9, 1);

INSERT INTO `card` (card_id, card_type) VALUES
(10, 0);

INSERT INTO `card` (card_id, card_type) VALUES
(11, 1);

-- end of card

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(1, 2);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(1, 3);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(2, 4);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(2, 5);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(3, 6);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(3, 7);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(4, 8);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(4, 9);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(5, 10);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(5, 11);

--end of cardofperson

INSERT INTO `admin` (id, username, password, role) VALUES
(1, "admin", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", "ADMIN"); 

--end of admin

INSERT INTO `credit_card` (id, id_number, password, card_limit, loan, is_lost) VALUES
(1, "134015851838831650", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 
1000.00, 0.00, 0);

INSERT INTO `credit_card` (id, id_number, password, card_limit, loan, is_lost) VALUES
(2, "134015851838831651", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 
1000.00, 0.00, 0);

INSERT INTO `credit_card` (id, id_number, password, card_limit, loan, is_lost) VALUES
(3, "134015851838831652", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 
1000.00, 0.00, 0);

INSERT INTO `credit_card` (id, id_number, password, card_limit, loan, is_lost) VALUES
(4, "134015851838831653", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 
1000.00, 0.00, 0);

INSERT INTO `credit_card` (id, id_number, password, card_limit, loan, is_lost) VALUES
(5, "134015851838831654", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 
1000.00, 0.00, 0);

--end of credit card

INSERT INTO `credit_card_inspector` (id, name, password, permission) VALUES
(1, "a4", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 2);

-- 储蓄

insert into `cashier`(username, password, authority)values
('a1', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',3);

-- 储蓄账号

insert into `account`(id,name, phonenumber, citizenid ,card_id, password) values
(1,'testAccount1','12967777593','134015851838831650',1000000000000000,'8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');

insert into `account`(id,name, phonenumber, citizenid ,card_id, password) values
(2,'testAccount2','12967777594','134015851838831651',1000000000000001,'8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');

insert into `account`(id,name, phonenumber, citizenid ,card_id, password) values
(3,'testAccount3','12967777595','134015851838831652',1000000000000002,'8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');

insert into `account`(id,name, phonenumber, citizenid ,card_id, password) values
(4,'testAccount4','12967777596','134015851838831653',1000000000000003,'8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');

insert into `account`(id,name, phonenumber, citizenid ,card_id, password) values
(5,'testAccount5','12967777597','134015851838831654',1000000000000004,'8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');

insert into `deposite_card`(id, type, accountid)values
(1000000000000000, 1, 1);

insert into `deposite_card`(id, type, accountid)values
(1000000000000001, 2, 2);

insert into `deposite_card`(id, type, accountid)values
(1000000000000002, 1, 3);

insert into `deposite_card`(id, type, accountid)values
(1000000000000003, 2, 4);

insert into `deposite_card`(id, type, accountid)values
(1000000000000004, 1, 5);


Insert into `rate`values
(0.2,1.15,1.35,1.45,1.65,1.95,2.00);

-- 外汇

INSERT INTO foreign_currency (fc_id, fc_name) VALUES ('RMB', '人民币');
INSERT INTO foreign_currency (fc_id, fc_name) VALUES ('USD', '美元');
INSERT INTO foreign_currency (fc_id, fc_name) VALUES ('EUR', '欧元');
INSERT INTO foreign_currency (fc_id, fc_name) VALUES ('GBP', '英镑');

INSERT INTO foreign_currency_rate (fc_id, fc_date, fc_rate) VALUES ('RMB', '2020-01-01 00:00:00', '1.0000'),('RMB', '2020-01-02 00:00:00', '2.0000'),
('USD', '2020-01-01 00:00:00', '6.0000'), ('EUR', '2020-01-01 00:00:00', '7.0000'), ('GBP', '2020-01-01 00:00:00', '8.0000');

INSERT INTO data_operator (data_operator_id, username, password, email, phone_number) VALUES 
('1', 'a5', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'data_operator_1@gmail.com', '12345678901');

INSERT INTO trade_record (trade_id, fc_id, trade_time, amount_cny, amount_foreign_currency, user_id, credit_card_id, is_buy_in) VALUES
    ('20230530000001', 'RMB', '2024-05-30 12:00:00', 100.00, 100.00, '0', '1234567890', TRUE);

    
INSERT INTO trade_record (trade_id, fc_id, trade_time, amount_cny, amount_foreign_currency, user_id, credit_card_id, is_buy_in) VALUES
    ('20230530000002', 'RMB', '2024-06-2 12:00:00', 100.00, 100.00, '0', '1234567890', TRUE);

-- 贷款
INSERT INTO officer (officer_id, officer_name, phone_number, username, password, permissions) VALUES
    (1, "a3", "15544443333", "a3", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", "LARGE"); 