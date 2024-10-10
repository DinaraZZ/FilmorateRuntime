package com.practice.filmorate.storage.impl;

import com.practice.filmorate.exception.NotFoundException;
import com.practice.filmorate.exception.ValidationException;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.model.Genre;
import com.practice.filmorate.model.Mpa;
import com.practice.filmorate.storage.FilmStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final LocalDate MIN_RELEASE_DATE =
            LocalDate.of(1895, 12, 28);
    private static final String SELECT_ALL = """
            select films.id, films.name, films.description,
                   films.release_date, films.duration, films.mpa_id, mpa.name mpa_name
            from FILMS join mpa on films.mpa_id = mpa.id
            """;
    private static final String SELECT_ID = """
            select films.id
            from films
            """;

    @Override
    public Film add(Film entity) {
        entityCheck(entity);

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> map = Map.of(
                "name", entity.getName(),
                "description", entity.getDescription(),
                "release_date", entity.getReleaseDate(),
                "duration", entity.getDuration(),
                "mpa_id", entity.getMpa().getId()
        );

        int id = insert.executeAndReturnKey(map).intValue();
        entity.setId(id);
        return entity;
    }

    @Override
    public Film update(Film entity) { // ?? строка, условия(выполнение до след.ошибки)
        // неизменяемое поле - только id
        Film film = findById(entity.getId()).orElseThrow(() ->
                new NotFoundException("Фильм не найден"));
        int filmId = film.getId();

        // Обновление таблицы FILMS
        String update = "update films set %s = ? where id = ?";
        try {
            if (!film.getName().equals(entity.getName()) && !entity.getName().isBlank()) {
                jdbcTemplate.update(String.format(update, "name"), entity.getName(), filmId);
            }
            if (!film.getDescription().equals(entity.getDescription()) && !entity.getDescription().isBlank()) {
                descriptionCheck(entity);
                jdbcTemplate.update(String.format(update, "description"), entity.getDescription(), filmId);
            }
            if (!film.getReleaseDate().equals(entity.getReleaseDate()) && entity.getReleaseDate() != null) {
                releaseDateCheck(entity);
                jdbcTemplate.update(String.format(update, "release_date"), entity.getReleaseDate(), filmId);
            }
            if (film.getDuration() != entity.getDuration()) {
                durationCheck(entity);
                jdbcTemplate.update(String.format(update, "duration"), entity.getDuration(), filmId);
            }
            if (film.getMpa().getId() != entity.getMpa().getId() && entity.getMpa() != null) {
                // mpa check добавить
                jdbcTemplate.update(String.format(update, "mpa_id"), entity.getMpa().getId(), filmId);
                // нужно ли добавлять условие, если json mpa кинули только name? (если только id - работает)
            }
        } catch (ValidationException e) {
            String reverseUpdate = """
                    update films 
                    set name = ?, description = ?, 
                    release_date = ?, duration = ?, mpa_id = ?
                    where id = ?
                    """;
            jdbcTemplate.update(reverseUpdate,
                    film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), film.getMpa().getId(),
                    filmId
            );
            throw new ValidationException(e.getMessage()); //? не попадает
        }

        // при каких условиях меняется likes?

        // Обновление таблицы GENRES
        Set<Genre> oldGenres = film.getGenres();
        Set<Genre> newGenres = entity.getGenres();
        for (Genre newGenre : newGenres) {
            if (!oldGenres.contains(newGenre)) {
                // проверить, существует ли такой жанр
                jdbcTemplate.update("insert into films_genres(film_id, genre_id) values (?,?)",
                        filmId, newGenre.getId());
            }
        }
        for (Genre oldGenre : oldGenres) {
            if (!newGenres.contains(oldGenre)) {
                jdbcTemplate.update("delete from films_genres where film_id = ? and genre_id = ?",
                        filmId, oldGenre.getId());
            }
        }

        return entity;
    }

    @Override
    public Optional<Film> findById(int id) {
        return jdbcTemplate.queryForStream(
                        SELECT_ALL + " where films.id = ?", this::mapRow, id)
                .findFirst();
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(SELECT_ALL, this::mapRow);
    }

    private void entityCheck(Film entity) {
        nameCheck(entity);
        descriptionCheck(entity);
        releaseDateCheck(entity);
        durationCheck(entity);
    }

    private void nameCheck(Film entity) {
        if (entity.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
    }

    private void descriptionCheck(Film entity) {
        if (entity.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
    }

    private void releaseDateCheck(Film entity) {
        if (entity.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }

    private void durationCheck(Film entity) {
        if (entity.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

    Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Список жанров по id фильма
        String genresSelect = """
                select genres.id genre_id, genres.name genre_name
                from genres
                         join films_genres on genres.id = films_genres.genre_id
                where films_genres.film_id = ?
                """;

        List<Genre> genres = jdbcTemplate.query(genresSelect,
                (rs1, rowNum1) -> {
                    return Genre.builder()
                            .id(rs1.getInt("genre_id"))
                            .name(rs1.getString("genre_name"))
                            .build();
                }, rs.getInt("id"));

        Set<Genre> genreSet = new TreeSet<>(genres);

        // Список id пользователей, которые поставили лайк фильму
        String likesSelect = """
                select user_id
                from films_users_likes
                where film_id = ?
                """;
        List<Integer> likes = jdbcTemplate.query(likesSelect,
                (rs1, rowNum1) -> {
                    return rs1.getInt("user_id");
                }, rs.getInt("id"));
        Set<Integer> likesSet = new TreeSet<>(likes);

        // Полностью собранный объект Film
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(Mpa.builder()
                        .id(rs.getInt("mpa_id"))
                        .name(rs.getString("mpa_name"))
                        .build())
                .genres(genreSet)
                .likes(likesSet)
                .build();
    }
}
