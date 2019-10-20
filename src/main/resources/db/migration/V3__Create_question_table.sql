CREATE TABLE question
(
    id bigint AUTO_INCREMENT PRIMARY KEY,
    title varchar(50),
    description text,
    gmt_create long,
    gmt_modified long,
    creator bigint,
    comment_count int,
    view_count int,
    like_count int,
    tag varchar(256)
);