CREATE TABLE board
(
    bno     BIGINT AUTO_INCREMENT NOT NULL,
    title   VARCHAR(255)          NULL,
    content VARCHAR(255)          NULL,
    writer  VARCHAR(255)          NULL,
    CONSTRAINT pk_board PRIMARY KEY (bno)
);