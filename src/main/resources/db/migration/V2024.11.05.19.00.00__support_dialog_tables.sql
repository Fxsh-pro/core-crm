CREATE TABLE IF NOT EXISTS operator
(
    id       serial PRIMARY KEY,
    login    VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    name     VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS customer
(
    id           serial PRIMARY KEY,
    channel_id   BIGINT       NOT NULL,
    channel_type VARCHAR(20)  NOT NULL,
    firstName    VARCHAR(50)  NOT NULL,
    lastName     VARCHAR(256) NOT NULL,
    userName     VARCHAR(100),
    UNIQUE (channel_id, channel_type)
);

CREATE TABLE IF NOT EXISTS chat
(
    id           serial PRIMARY KEY,
    chat_id      BIGINT      NOT NULL,
    channel_type VARCHAR(20) NOT NULL,
    creator_by   INT         NOT NULL,
    created_at   INT         NOT NULL,
    status       VARCHAR(64) NOT NULL,
    UNIQUE (chat_id, channel_type)
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