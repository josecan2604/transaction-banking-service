CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users (

    id UUID PRIMARY KEY
        DEFAULT gen_random_uuid(),

    username VARCHAR(255)
        NOT NULL
        UNIQUE,

    password VARCHAR(255)
        NOT NULL
);

CREATE TABLE ledger_transactions (

    id UUID PRIMARY KEY
        DEFAULT gen_random_uuid(),

    user_id UUID
        NOT NULL,

    amount NUMERIC(19,4)
        NOT NULL,

    currency VARCHAR(3)
        NOT NULL,

    description VARCHAR(500)
        NOT NULL,

    counterparty_iban VARCHAR(34)
        NOT NULL,

    created_at TIMESTAMP
        NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transaction_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE INDEX idx_transactions_user_created_at
ON ledger_transactions(user_id, created_at);