insert into mpa (id, name)
values (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');

insert into genres (id, name)
values (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

-- Значения для тестов
/*
insert into FILMS(name, description, release_date, duration, mpa_id)
values ('First Movie', 'First description', '2000-01-01', 111, 1),
       ('Second Movie', 'Second description', '2000-02-02', 122, 2);

insert into USERS(login, email, name, birthday)
values ('login1', 'email1@gmail.com', 'Name', '2000-01-01'),
       ('login2', 'email2@gmail.com', 'Name', '2000-01-02'),
       ('login3', 'email3@gmail.com', 'Name', '2000-01-03'),
       ('login4', 'email4@gmail.com', 'Name', '2000-01-04'),
       ('login5', 'email5@gmail.com', 'Name', '2000-01-05'),
       ('login6', 'email6@gmail.com', 'Name', '2000-01-06'),
       ('login7', 'email7@gmail.com', 'Name', '2000-01-07'),
       ('login8', 'email8@gmail.com', 'Name', '2000-01-08'),
       ('login9', 'email9@gmail.com', 'Name', '2000-01-09'),
       ('login10', 'email10@gmail.com', 'Name', '2000-01-10');

insert into FILMS_USERS_LIKES(film_id, user_id)
values (1, 1), (1, 3);

insert into FILMS_GENRES(film_id, genre_id)
values (1, 1),
       (1, 2);

insert into users_friendship_status(first_user_id, second_user_id, status)
values (1, 2, false),
       (1, 4, false),
       (1, 5, true),
       (1, 7, true),
       (2, 1, true),
       (6, 1, true),
       (8, 1, false),
       (10, 1, false);*/
