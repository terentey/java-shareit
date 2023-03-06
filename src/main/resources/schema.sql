CREATE TABLE IF NOT EXISTS users
(
    id    INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    CONSTRAINT user_email_index UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS items
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id     INTEGER REFERENCES users (id),
    name        VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    available   BOOLEAN NOT NULL
);
CREATE TABLE IF NOT EXISTS comments
(
    id      INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text    VARCHAR                     NOT NULL,
    item_id INTEGER REFERENCES items (id),
    user_id INTEGER REFERENCES users (id),
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
CREATE TABLE IF NOT EXISTS bookings
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status   VARCHAR                     NOT NULL,
    item_id  INTEGER REFERENCES items (id),
    user_id INTEGER REFERENCES users (id)
);