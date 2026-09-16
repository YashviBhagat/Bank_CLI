CREATE TABLE accounts (
    account_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pin VARCHAR(10) NOT NULL,
    balance NUMERIC(15,2) NOT NULL DEFAULT 0.00
);

CREATE TABLE transaction (
    transaction_id SERIAL PRIMARY KEY,
    from_account_id INTEGER REFERENCES account(account_id),
    to_account_id INTEGER REFERENCES account(account_id),
    transaction_type VARCHAR(20) NOT NULL CHECK (transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER')),
    transaction_amount NUMERIC(12, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);