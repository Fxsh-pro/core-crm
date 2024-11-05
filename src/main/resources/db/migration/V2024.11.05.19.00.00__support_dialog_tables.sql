CREATE TABLE IF NOT EXISTS operator
(
    id       serial PRIMARY KEY,
    login    VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    name     VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS dialog
(
    id         serial PRIMARY KEY,
    creator_by BIGINT      not null,
    created_at BIGINT      not null,
    status     VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS message
(
    id         serial PRIMARY KEY,
    dialog_id  BIGINT REFERENCES dialog (id),
    created_at BIGINT      not null,
    created_by BIGINT      not null,
    text       TEXT        NOT NULL,
    type       VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS operator_dialog
(
    id         serial PRIMARY KEY,
    support_id BIGINT REFERENCES operator (id),
    dialog_id  BIGINT REFERENCES dialog (id)
);