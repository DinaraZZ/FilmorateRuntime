drop table if exists users_friendship_status;
drop table if exists films_users_likes;
drop table if exists films_genres;
drop table if exists users;
drop table if exists films;
drop table if exists mpa;
drop table if exists genres;

create table if not exists genres
(
    id   serial4 primary key,
    name varchar not null
);

create table if not exists mpa
(
    id   serial4 primary key,
    name varchar(5) not null
);

create table if not exists films
(
    id           serial4 primary key,
    name         varchar(255) not null,
    description  varchar(200) not null,
    release_date date         not null,
    duration     int check ( duration > 0 ),
    mpa_id       int4         not null,
    foreign key (mpa_id) references mpa (id)
);

create table if not exists users
(
    id       serial4 primary key,
    login    varchar not null unique,
    email    varchar not null unique,
    name     varchar,
    birthday date check ( birthday <= current_date )
);

create table if not exists films_genres
(
    id       serial4 primary key,
    film_id  int4 not null,
    genre_id int4 not null,
    foreign key (film_id) references films (id),
    foreign key (genre_id) references genres (id)
);

create table if not exists films_users_likes
(
    id      serial4 primary key,
    film_id int4 not null,
    user_id int4 not null,
    foreign key (film_id) references films (id),
    foreign key (user_id) references users (id)
);

create table if not exists users_friendship_status
(
    id             serial4 primary key,
    first_user_id  int4 not null,
    second_user_id int4 not null,
    status         boolean default false,
    foreign key (first_user_id) references users (id),
    foreign key (second_user_id) references users (id),
    check ( first_user_id <> second_user_id )
);