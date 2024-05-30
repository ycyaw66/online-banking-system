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