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
(7, 1);

INSERT INTO `card` (card_id, card_type) VALUES
(3, 0);

INSERT INTO `card` (card_id, card_type) VALUES
(8, 1);

INSERT INTO `card` (card_id, card_type) VALUES
(4, 0);

INSERT INTO `card` (card_id, card_type) VALUES
(9, 1);

INSERT INTO `card` (card_id, card_type) VALUES
(5, 0);

INSERT INTO `card` (card_id, card_type) VALUES
(10, 1);

INSERT INTO `card` (card_id, card_type) VALUES
(6, 0);

INSERT INTO `card` (card_id, card_type) VALUES
(11, 1);

-- end of card

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(1, 2);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(1, 7);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(2, 3);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(2, 8);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(3, 4);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(3, 9);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(4, 5);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(4, 10);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(5, 6);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(5, 11);

--end of cardofperson

-- 用户权限

INSERT INTO `user_privilege` (user_id, payment, transfer, receive) VALUES
    (1, true, true, true); 

INSERT INTO `user_privilege` (user_id, payment, transfer, receive) VALUES
    (2, true, true, true); 

INSERT INTO `user_privilege` (user_id, payment, transfer, receive) VALUES
    (3, true, true, true); 

INSERT INTO `user_privilege` (user_id, payment, transfer, receive) VALUES
    (4, true, true, true); 

INSERT INTO `user_privilege` (user_id, payment, transfer, receive) VALUES
    (5, true, true, true); 

INSERT INTO `admin` (id, username, password, role) VALUES
(1, "admin", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", "ADMIN"); 

--end of admin

INSERT INTO `credit_card` (id, id_number, password, card_limit, loan, is_lost) VALUES
(2, "134015851838831650", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 
1000.00, 0.00, 0);

INSERT INTO `credit_card` (id, id_number, password, card_limit, loan, is_lost) VALUES
(3, "134015851838831651", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 
1000.00, 0.00, 0);

INSERT INTO `credit_card` (id, id_number, password, card_limit, loan, is_lost) VALUES
(4, "134015851838831652", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 
1000.00, 0.00, 0);

INSERT INTO `credit_card` (id, id_number, password, card_limit, loan, is_lost) VALUES
(5, "134015851838831653", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 
1000.00, 0.00, 0);

INSERT INTO `credit_card` (id, id_number, password, card_limit, loan, is_lost) VALUES
(6, "134015851838831654", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 
1000.00, 0.00, 0);

--end of credit card

INSERT INTO `credit_card_inspector` (id, name, password, permission) VALUES
(1, "a4", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 2);

-- 储蓄

insert into `cashier`(username, password, authority)values
('a1', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',3);

-- 储蓄账号

insert into `account`(id,name, phonenumber, citizenid ,card_id, password) values
(7,'testAccount1','12967777593','134015851838831650',1,'8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');

insert into `account`(id,name, phonenumber, citizenid ,card_id, password) values
(8,'testAccount2','12967777594','134015851838831651',2,'8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');

insert into `account`(id,name, phonenumber, citizenid ,card_id, password) values
(9,'testAccount3','12967777595','134015851838831652',3,'8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');

insert into `account`(id,name, phonenumber, citizenid ,card_id, password) values
(10,'testAccount4','12967777596','134015851838831653',4,'8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');

insert into `account`(id,name, phonenumber, citizenid ,card_id, password) values
(11,'testAccount5','12967777597','134015851838831654',5,'8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');

insert into `deposite_card`(id, type, accountid)values
(1, 1, 7);

insert into `deposite_card`(id, type, accountid)values
(2, 2, 8);

insert into `deposite_card`(id, type, accountid)values
(3, 1, 9);

insert into `deposite_card`(id, type, accountid)values
(4, 2, 10);

insert into `deposite_card`(id, type, accountid)values
(5, 1, 11);

insert into `property` (id, accountid, type) VALUES
    (1, 7, 1);

insert into `property` (id, accountid, type) VALUES
    (2, 8, 1);

insert into `property` (id, accountid, type) VALUES
    (3, 9, 1);

insert into `property` (id, accountid, type) VALUES
    (4, 10, 1);

insert into `property` (id, accountid, type) VALUES
    (5, 11, 1);

insert into `demand_deposit` (propertyid, accountid, amount, date, base) VALUES
    (1, 7, 1000, 1718105369006, 0);

insert into `demand_deposit` (propertyid, accountid, amount, date, base) VALUES
    (2, 8, 1000, 1718105369006, 0);

insert into `demand_deposit` (propertyid, accountid, amount, date, base) VALUES
    (3, 9, 1000, 1718105369006, 0);

insert into `demand_deposit` (propertyid, accountid, amount, date, base) VALUES
    (4, 10, 1000, 1718105369006, 0);

insert into `demand_deposit` (propertyid, accountid, amount, date, base) VALUES
    (5, 11, 1000, 1718105369006, 0);


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