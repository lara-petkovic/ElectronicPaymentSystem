INSERT INTO payment_option(id, address, option, port)
VALUES
    (1, '/api/transaction', 'Crypto', '8088'),
    (2, '/api/payments', 'Card', '8052'),
    (3, '/api/payments/qr', 'QR Code', '8052'),
    (4, '/api/notification/pay', 'PayPal', '8089');