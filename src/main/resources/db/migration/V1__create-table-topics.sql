create table topics(

    id bigint not null auto_increment,
    title varchar(100) not null,
    message varchar(100) not null,
    status varchar(100) not null,
    course varchar(100) not null,
    creation varchar(100) not null,
    user_id bigint not null,

    primary key(id)

);
