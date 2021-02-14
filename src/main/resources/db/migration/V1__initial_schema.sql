CREATE TABLE stonks
(
    id        serial primary key,
    symbol    VARCHAR(255) NOT NULL,
    value     numeric      NOT NULL,
    timestamp timestamptz  NOT NULL default now()
);