INSERT INTO accounts (id, pan, number, merchant_account, bank_identifier_code, merchant_password, merchant_id, expiration_date, card_holder_name, security_code, balance)
VALUES
    (-1, 'Uok6kOHstLliQkjPzMDrY64rfo/p4t80KBOi6AVQTyA=', '1234567890', false, 'BIC001', null, null, '2DslEYx0LEE+8l5216QxOg==', 'Dusan Sudjic', 'Iu71wtJDoOOZvYIbRm8fuA==', 10000.0),
    (-2, 'z+tpnDOC1oqCiaFJhCGlhtwNZjmxguzhjAfT3rAYRm8=', '2345678901', false, 'BIC002', null, null, '2DslEYx0LEE+8l5216QxOg==', 'Jane Smith Acquirer', 'Iu71wtJDoOOZvYIbRm8fuA==', 1500.0),
    (-3, 'vMAhEzBxvj+I+fFUjYNTu0mJZbNjm0nomiVwDAnFuUY=', '3456789012', false, 'BIC003', null, null, 'dG61g/Jsf6pty3cUXFwptQ==', 'Alice Brown Acquirer', 'Lp92u2rtarKXiL66nBO33A==', 1200.0);
INSERT INTO bank_identifier_number(id)
VALUES ('6666');