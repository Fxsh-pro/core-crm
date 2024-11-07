CREATE TABLE IF NOT EXISTS operator
(
    id       serial PRIMARY KEY,
    login    VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    name     VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS customer
(
    id        serial PRIMARY KEY,
    tg_id     BIGINT       NOT NULL UNIQUE,
    firstName VARCHAR(50)  NOT NULL UNIQUE,
    lastName  VARCHAR(256) NOT NULL,
    userName  VARCHAR(100)
);



CREATE TABLE IF NOT EXISTS chat
(
    id         serial PRIMARY KEY,
    tg_chat_id BIGINT UNIQUE NOT NULL,
    creator_by BIGINT        not null,
    created_at BIGINT        not null,
    status     VARCHAR(64)   NOT NULL
);

CREATE TABLE IF NOT EXISTS message
(
    id         serial PRIMARY KEY,
    chat_id  BIGINT REFERENCES chat (id),
    created_at BIGINT      not null,
    created_by BIGINT      not null,
    text       TEXT        NOT NULL,
    type       VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS operator_dialog
(
    id         serial PRIMARY KEY,
    support_id BIGINT REFERENCES operator (id),
    chat_id  BIGINT REFERENCES chat (id)
);

