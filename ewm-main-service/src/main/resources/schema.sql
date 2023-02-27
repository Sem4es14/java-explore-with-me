ALTER TABLE if EXISTS events
    DROP CONSTRAINT if EXISTS fk_categories_events;
ALTER TABLE if EXISTS events
    DROP CONSTRAINT if EXISTS fk_users_events;
ALTER TABLE if EXISTS compilations_events
    DROP CONSTRAINT if EXISTS fk_compilations_events_events;
ALTER TABLE if EXISTS compilations_events
    DROP CONSTRAINT if EXISTS fk_compilations_events_compilations;
ALTER TABLE if EXISTS requests
    DROP CONSTRAINT if EXISTS fk_requests_events;
ALTER TABLE if EXISTS requests
    DROP CONSTRAINT if EXISTS fk_requests_users;
ALTER TABLE if EXISTS comments
    DROP CONSTRAINT if EXISTS fk_comments_events;
ALTER TABLE if EXISTS comments
    DROP CONSTRAINT if EXISTS fk_comments_authors;

DROP TABLE if EXISTS categories CASCADE;
DROP TABLE if EXISTS users CASCADE;
DROP TABLE if EXISTS events CASCADE;
DROP TABLE if EXISTS requests CASCADE;
DROP TABLE if EXISTS compilations CASCADE;
DROP TABLE if EXISTS compilations_events CASCADE;
DROP TABLE if EXISTS comments CASCADE;

CREATE TABLE categories
(
    category_id BIGINT       NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (category_id)
);

CREATE TABLE users
(
    user_id BIGINT       NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    email   VARCHAR(255) NOT NULL UNIQUE,
    name    VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE requests
(
    request_id BIGINT       NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    created    TIMESTAMP    NOT NULL,
    status     VARCHAR(255) NOT NULL,
    event_id   BIGINT       NOT NULL,
    requester  BIGINT       NOT NULL,
    PRIMARY KEY (request_id)
);

CREATE TABLE events
(
    event_id           BIGINT        NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    title              VARCHAR(255)  NOT NULL,
    description        VARCHAR(1024) NOT NULL,
    annotation         VARCHAR(1024) NOT NULL,
    created_on         TIMESTAMP     NOT NULL,
    event_date         TIMESTAMP     NOT NULL,
    published_on       TIMESTAMP,
    paid               BOOLEAN       NOT NULL,
    longitude          FLOAT         NOT NULL,
    latitude           FLOAT         NOT NULL,
    state              VARCHAR(255)  NOT NULL,
    request_moderation BOOLEAN       NOT NULL DEFAULT true,
    participant_limit  BIGINT        NOT NULL DEFAULT 0,
    category_id        BIGINT        NOT NULL,
    initiator          BIGINT        NOT NULL,
    PRIMARY KEY (event_id)
);

CREATE TABLE compilations
(
    compilation_id BIGINT       NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    title          VARCHAR(255) NOT NULL,
    pinned         BOOLEAN      NOT NULL DEFAULT false,
    PRIMARY KEY (compilation_id)
);

CREATE TABLE compilations_events
(
    event_id       BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    PRIMARY KEY (event_id, compilation_id)
);

CREATE TABLE comments
(
    comment_id bigint       NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    author_id  bigint       NOT NULL,
    event_id   bigint       NOT NULL,
    created_at timestamp    NOT NULL,
    edited_at  timestamp,
    text       varchar(255) NOT NULL,
    PRIMARY KEY (comment_id)
);

ALTER TABLE if EXISTS events
    ADD CONSTRAINT fk_categories_events FOREIGN KEY (category_id)
        REFERENCES categories (category_id);
ALTER TABLE if EXISTS events
    ADD CONSTRAINT fk_users_events FOREIGN KEY (initiator)
        REFERENCES users (user_id);

ALTER TABLE if EXISTS requests
    ADD CONSTRAINT fk_requests_events FOREIGN KEY (event_id)
        REFERENCES events (event_id);
ALTER TABLE if EXISTS requests
    ADD CONSTRAINT fk_requests_users FOREIGN KEY (requester)
        REFERENCES users (user_id);

ALTER TABLE if EXISTS compilations_events
    ADD CONSTRAINT fk_compilations_events_events
        FOREIGN KEY (event_id) REFERENCES events (event_id);

ALTER TABLE if EXISTS compilations_events
    ADD CONSTRAINT fk_compilations_events_compilations
        FOREIGN KEY (compilation_id) REFERENCES compilations (compilation_id);

ALTER TABLE if EXISTS comments
    ADD CONSTRAINT fk_comments_events
        FOREIGN KEY (event_id) REFERENCES events (event_id);

ALTER TABLE if EXISTS comments
    ADD CONSTRAINT fk_comments_authors
        FOREIGN KEY (author_id) REFERENCES users (user_id);