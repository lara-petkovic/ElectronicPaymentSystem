INSERT INTO accounts (id, pan, number, merchant_account, bank_identifier_code, merchant_password, merchant_id, expiration_date, card_holder_name, security_code, balance)
VALUES
    ('1', '1234567890123456', '1234567890', true, 'BIC001', 'password123', 'MERCHANT001', '12/25', 'John Doe Acquirer', '123', 1000.0),
    ('2', '2345678901234560', '2345678901', false, 'BIC002', null, null, '11/26', 'Dusan Sudjic', '456', 1500.0),
    ('3', '3456789012345678', '3456789012', true, 'BIC003', 'password789', 'MERCHANT003', '10/24', 'Alice Brown Acquirer', '789', 1200.0);