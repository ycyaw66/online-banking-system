DELETE FROM `user`;
DELETE FROM `card`;
DELETE FROM `cardofperson`;
DELETE FROM `credit_card`;


INSERT INTO `card` (card_id, card_type) VALUES
(1, 2);
-- system card!! don't remove it !!!


INSERT INTO `user` (username, password, id_number, email, phone_number) VALUES
('zxy', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 
'134015851838831650', 'test1@zju.edu.cn', '12967777593');


INSERT INTO `user` (username, password, id_number, email, phone_number) VALUES
('fbz', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 
'134015851838831651', 'test2@zju.edu.cn', '12967777594');

-- end of user example


INSERT INTO `card` (card_id, card_type) VALUES
(2, 0);

-- end of card

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(1, 1);

INSERT INTO `cardofperson` (user_id, card_id) VALUES
(1, 2);

--end of cardofperson

INSERT INTO `credit_card` (id, id_number, password, card_limit, loan, is_lost) VALUES
(1, "134015851838831650", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 
1000.00, 0.00, 0);

--end of credit card

INSERT INTO `admin` (id, username, password, role) VALUES
(1, "fbz", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", "ADMIN"); 

--end of admin

INSERT INTO `credit_card_inspector` (id, name, password, permission) VALUES
(1, "fbz", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 1);

-- 储蓄

insert into `administrator` (username, password, salt) values
('testadmin', '4739ee3bd29e4f415da8ba9298a087e0fdc9c61378420ba8fbbab298bd74c4df','123');

insert into `cashier`(username, password, authority,salt)values
('testcashier', '4739ee3bd29e4f415da8ba9298a087e0fdc9c61378420ba8fbbab298bd74c4df',2,'123');

insert into `account`(id,name, phonenumber, citizenid ,card_id, password, salt) values
(000000000001,'testaccount','131313131',210204199802011234,1000000000000000,'4739ee3bd29e4f415da8ba9298a087e0fdc9c61378420ba8fbbab298bd74c4df','123');

insert into `deposite_card`(id, type, accountid)values
(1000000000000000,1,000000000001);


Insert into `rate`values
(0.2,1.15,1.35,1.45,1.65,1.95,2.00);

-- 外汇

INSERT INTO foreign_currency (fc_id, fc_name) VALUES ('RMB', '人民币');
INSERT INTO foreign_currency (fc_id, fc_name) VALUES ('USD', '美元');
INSERT INTO foreign_currency (fc_id, fc_name) VALUES ('EUR', '欧元');
INSERT INTO foreign_currency (fc_id, fc_name) VALUES ('GBP', '英镑');

INSERT INTO foreign_currency_rate (fc_id, fc_date, fc_rate) VALUES ('RMB', '2020-01-01 00:00:00', '1.0000'),('RMB', '2020-01-02 00:00:00', '2.0000'),
('USD', '2020-01-01 00:00:00', '6.0000'), ('EUR', '2020-01-01 00:00:00', '7.0000'), ('GBP', '2020-01-01 00:00:00', '8.0000');

INSERT INTO data_operator (data_operator_id, username, password, email, phone_number) VALUES ('1', 'qqqqq', '123456', 'data_operator_1@gmail.com', '12345678901');

-- 插入Administrator表的数据
INSERT INTO `fc_administrator` (admin_id, username, password, email, phone_number)
VALUES
    ('20230530000001', '123', '123', 'admin1@example.com', '1234567890'),
    ('20230530000002', 'admin2', 'password123', 'admin2@example.com', '0987654321');

INSERT INTO trade_record (trade_id, fc_id, trade_time, amount_cny, amount_foreign_currency, user_id, credit_card_id, is_buy_in) VALUES
    ('20230530000001', 'RMB', '2024-05-30 12:00:00', 100.00, 100.00, '0', '1234567890', TRUE);

    
INSERT INTO trade_record (trade_id, fc_id, trade_time, amount_cny, amount_foreign_currency, user_id, credit_card_id, is_buy_in) VALUES
    ('20230530000002', 'RMB', '2024-06-2 12:00:00', 100.00, 100.00, '0', '1234567890', TRUE);

-- 贷款
INSERT INTO officer (officer_id, officer_name, phone_number, username, password, permissions) VALUES
    (0, "A3", "15544443333", "A3", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", "LARGE"); 