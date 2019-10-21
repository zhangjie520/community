create table question
(
    id bigint auto_increment primary key,
    title varchar(50),
    description text,
    gmt_create long,
    gmt_modified long,
    creator bigint,
    comment_count int default 0,
    view_count int default 0,
    like_count int default 0,
    tag varchar(256)
);