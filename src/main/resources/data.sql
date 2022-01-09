INSERT INTO tb_account (balance, account_type, financial_institution)
VALUES (2500.00, 0, 'Banco Inter');
INSERT INTO tb_account (balance, account_type, financial_institution)
VALUES (200.00, 1, 'Banco do Brasil');
INSERT INTO tb_account (balance, account_type, financial_institution)
VALUES (15000.00, 2, 'Banco C6');

INSERT INTO tb_income (value, receiving_date, expected_receiving_date, description, income_type, account_id)
VALUES (100,
        '2021-07-13T00:00:00',
        '2021-07-13T00:00:00',
        'test',
        1,
        1);
INSERT INTO tb_income (value, receiving_date, expected_receiving_date, description, income_type, account_id)
VALUES (100,
        null,
        '2022-01-10T00:00:00Z',
        'test',
        2,
        2);

INSERT INTO tb_expense (value, payment_date, due_date, expense_type, account_id)
VALUES (150,
        '2022-01-1T00:00:00',
        '2022-01-13T00:00:00',
        1,
        1);
INSERT INTO tb_expense (value, payment_date, due_date, expense_type, account_id)
VALUES (500,
        '2022-01-1T00:00:00',
        '2022-01-10T00:00:00',
        2,
        1);
