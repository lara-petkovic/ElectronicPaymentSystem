INSERT INTO accounts (id, pan, number, merchant_account, bank_identifier_code, merchant_password, merchant_id, expiration_date, card_holder_name, security_code, balance)
VALUES
    (-1, '6666666666666664', '1234567890', false, 'BIC001', null, null, '11/26', 'Dusan Sudjic', '456', 10000.0),
    (-2, '6666678901234567', '2345678901', false, 'BIC002', null, null, '11/26', 'Jane Smith Acquirer', '456', 1500.0),
    (-3, '6666789012345678', '3456789012', false, 'BIC003', null, null, '10/24', 'Alice Brown Acquirer', '789', 1200.0);
INSERT INTO bank_identifier_number(id)
VALUES ('6666');