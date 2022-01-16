INSERT INTO tb_account (balance, account_type, financial_institution)
VALUES (2500.00, 0, 'Banco Pub');
INSERT INTO tb_account (balance, account_type, financial_institution)
VALUES (200.00, 1, 'Banco Future');
INSERT INTO tb_account (balance, account_type, financial_institution)
VALUES (15000.00, 2, 'Banco Java');

INSERT INTO tb_income (value, receiving_date, expected_receiving_date, description, income_type, account_id)
VALUES (1000,
        '2021-07-13',
        '2021-07-15',
        'ref 07/2021',
        2,
        1);
INSERT INTO tb_income (value, receiving_date, expected_receiving_date, description, income_type, account_id)
VALUES (100,
        '2021-08-13',
        '2021-07-13',
        'ref 08/2021',
        1,
        2);
INSERT INTO tb_income (value, receiving_date, expected_receiving_date, description, income_type, account_id)
VALUES (3000,
        '2022-01-05',
        '2022-01-05',
        'ref 01/2022',
        0,
        3);


INSERT INTO tb_expense (value, payment_date, due_date, expense_type, account_id)
VALUES (150,
        '2022-01-15',
        '2021-12-20',
        0,
        1);
INSERT INTO tb_expense (value, payment_date, due_date, expense_type, account_id)
VALUES (500,
        '2021-01-10',
        '2021-02-17',
        2,
        2);
INSERT INTO tb_expense (value, payment_date, due_date, expense_type, account_id)
VALUES (1000,
        '2022-01-01',
        '2022-01-10',
        3,
        3);