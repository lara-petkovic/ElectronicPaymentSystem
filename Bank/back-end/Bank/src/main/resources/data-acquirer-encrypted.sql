INSERT INTO accounts (id, pan, number, merchant_account, bank_identifier_code, merchant_password, merchant_id, expiration_date, card_holder_name, security_code, balance)
VALUES
    (-1, 'InlRIsHyHqm5h8YqBe6Equv6RauqSXWmQyl5M25VNUY=', '170001064479900050', true, 'BIC001', '$2a$10$ritqEmRYd.bE7PEXtgNQL.6y39EYAPOjhA/H/01Z5bKErTLyP6Yq6', 'MERCHANT001', 'WVJL+JajY/qAdwmLC4ERQw==', 'Dusan Sudjic', 'aB4G78iNEEdvfeKpvjgCEQ==', 1000.0),
    (-2, '7XDzG5nNcLieqftjUjwr5CRmMdiUDUUPkzw8+wIYykQ=', '2345678901', false, 'BIC002', null, null, '2DslEYx0LEE+8l5216QxOg==', 'Dusan Sudjic', 'Iu71wtJDoOOZvYIbRm8fuA==', 1500.0),
    (-3, '/O1X5MwaI8tC0lAsyQoO3+HYgKnTEKu92AQLpUr2luI=', '3456789012', true, 'BIC003', '$2a$10$kCRNB9HZOwUJyaV9XTiMHeRlkX4Nn1YqFT1io38GmaFoO.afCBVeG', 'MERCHANT003', 'dG61g/Jsf6pty3cUXFwptQ==', 'Alice Brown Acquirer', 'Lp92u2rtarKXiL66nBO33A==', 1200.0);
INSERT INTO bank_identifier_number(id)
VALUES ('2345');