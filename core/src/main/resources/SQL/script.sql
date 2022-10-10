create database whatishappen default charset utf8mb4 default collate utf8mb4_general_ci;

use whatishappen;

create table if not exists roles(
                                    `id` tinyint auto_increment primary key,
                                    `name` varchar(64) not null
)Engine=INNODB;

insert into `roles` values (default, 'ROLE_DEFAULT');
insert into `roles` values (default, 'ROLE_MOD');
insert into `roles` values (default, 'ROLE_ADMIN');

create table if not exists users(
                                    `id` bigint auto_increment primary key,
                                    `first_name` varchar(64) not null,
                                    `last_name` varchar(64) not null,
                                    `email` varchar(128) unique not null,
                                    `username` varchar(30) unique not null,
                                    `password` varchar(256) not null,
                                    `role_id` tinyint default 1,
                                    `created_at` datetime default now(),
                                    `updated_at` datetime default now(),

                                    constraint foreign key (`role_id`) references roles(`id`)
)Engine=INNODB;

create table if not exists categories
(
    `id`         bigint auto_increment primary key,
    `name` varchar(64) not null,
    `password`   varchar(64) default null,
    `is_public`   boolean default true,
    `created_at` datetime    default now()
)Engine=INNODB;

insert into `categories` values (default, 'Business', null, true, default);
insert into `categories` values (default, 'Entertainment', null, true, default);
insert into `categories` values (default, 'General', null, true, default);
insert into `categories` values (default, 'Health', null, true, default);
insert into `categories` values (default, 'Science', null, true, default);
insert into `categories` values (default, 'Sport', null, true, default);
insert into `categories` values (default, 'Technology', null, true, default);

create table if not exists tokens(
                                     `id` bigint auto_increment primary key,
                                     `user_id` bigint not null,
                                     `refresh_token` varchar(1024) not null,
                                     `revoked` boolean not null,
                                     `created_at` datetime default now(),

                                     constraint foreign key (`user_id`) references `users`(`id`)
)Engine=INNODB;