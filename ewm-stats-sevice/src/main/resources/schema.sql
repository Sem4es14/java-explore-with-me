DROP TABLE if EXISTS stats;

CREATE TABLE stats
(
    id       bigint        NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    uri      varchar(1024) NOT NULL,
    ip       varchar(255)  NOT NULL,
    datetime timestamp     NOT NULL,
    PRIMARY KEY (id)
);