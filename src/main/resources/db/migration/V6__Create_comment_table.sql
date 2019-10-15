CREATE TABLE comment
(
    id bigint AUTO_INCREMENT PRIMARY KEY,
    parent_id bigint NOT NULL,
    type int NOT NULL,
    commentor int NOT NULL,
    content varchar(1024),
    gmt_create bigint NOT NULL,
    gmt_modified bigint NOT NULL,
    like_count bigint DEFAULT 0
);