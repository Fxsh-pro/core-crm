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
    tg_id     INT          NOT NULL UNIQUE,
    firstName VARCHAR(50)  NOT NULL UNIQUE,
    lastName  VARCHAR(256) NOT NULL,
    userName  VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS chat
(
    id         serial PRIMARY KEY,
    tg_chat_id INT         NOT NULL,
    creator_by INT         NOT NULL,
    created_at INT         NOT NULL,
    status     VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS message
(
    id         serial PRIMARY KEY,
    chat_id    INT REFERENCES chat (id),
    created_at INT         NOT NULL,
    created_by INT         NOT NULL,
    text       TEXT        NOT NULL,
    type       VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS operator_dialog
(
    id          serial PRIMARY KEY,
    operator_id INT REFERENCES operator (id),
    chat_id     INT REFERENCES chat (id)
);