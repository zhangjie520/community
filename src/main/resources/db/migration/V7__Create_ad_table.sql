create table ad
(
    id bigint auto_increment primary key,
    title varchar(256) default  null ,
    url varchar(512) default  null ,
    img varchar(256) default null,
    gmt_start bigint,
    gmt_end bigint,
    gmt_create bigint,
    gmt_modify bigint,
    status int,
    pos varchar(10) not null
);