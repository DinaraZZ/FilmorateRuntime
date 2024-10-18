package com.practice.filmorate.storage.impl;

import com.practice.filmorate.exception.NotFoundException;
import com.practice.filmorate.exception.ValidationException;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.model.Genre;
import com.practice.filmorate.model.Mpa;
import com.practice.filmorate.storage.FilmStorage;
import com.practice.filmorate.storage.GenreStorage;
import com.practice.filmorate.storage.MpaStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    private static final String SELECT_ALL = """
            select films.id, films.name, films.description,
                   films.release_date, films.duration, films.mpa_id, mpa.name mpa_name
            from FILMS join mpa on films.mpa_id = mpa.id
            """;

    @Transactional
    @Override
    public Film add(Film entity) {
        mpaStorage.findById(entity.getMpa().getId())
                .orElseThrow(() -> new ValidationException("MPA не найден"));

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

        if (entity.getGenres() != null) {
            Set<Genre> sortedGenres = new TreeSet<>(entity.getGenres());
            entity.setGenres(sortedGenres);

            jdbcTemplate.batchUpdate("insert into films_genres(film_id, genre_id) values (?,?)",
                    sortedGenres, sortedGenres.size(), (ps, genre) -> {
                        ps.setInt(1, id);
                        ps.setInt(2, genre.getId());
                    });
        }

        if (entity.getLikes() != null) {
            Set<Integer> sortedLikes = new TreeSet<>(entity.getLikes());
            entity.setLikes(sortedLikes);

            jdbcTemplate.batchUpdate("""
                    insert into films_users_likes(film_id, user_id)
                    values (?, ?)
                    """, sortedLikes, sortedLikes.size(), (ps, like) -> {
                ps.setInt(1, id);
                ps.setInt(2, like);
            });
        }

        return entity;
    }

    @Transactional
    @Override
    public Film update(Film entity) { // ? удаление старых жанров
        // неизменяемое поле - только id
        Film film = findById(entity.getId()).orElseThrow(() ->
                new NotFoundException("Фильм не найден"));
        int filmId = film.getId();

        // Обновление таблицы FILMS
        mpaStorage.findById(entity.getMpa().getId())
                .orElseThrow(() -> new ValidationException("MPA не найден"));
        String update = """
                update films
                set name = ?,
                description = ?,
                release_date = ?,
                duration = ?,
                mpa_id = ?
                where id = ?
                """;
        jdbcTemplate.update(update, entity.getName(), entity.getDescription(), entity.getReleaseDate(),
                entity.getDuration(), entity.getMpa().getId(), filmId);

        // Обновление таблицы LIKES
        /*Set<Integer> oldLikes = film.getLikes();
        Set<Integer> newLikes = entity.getLikes();
        for (Integer newLike : newLikes) {
            // добавить проверку юзера по айди
            // добавить условие
            jdbcTemplate.update("insert into films_users_likes(film_id, user_id) values(?,?)",
                    filmId, newLike);
        }
        for (Integer oldLike : oldLikes) {
            jdbcTemplate.update("delete from films_users_likes where film_id = ? and user_id = ?",
                    filmId, oldLike);
        }*/

        // Обновление таблицы GENRES
        Set<Genre> genres = entity.getGenres();

        if (genres != null) {
            Set<Genre> newGenres = new TreeSet<>(genres);
            jdbcTemplate.batchUpdate("insert into films_genres(film_id, genre_id) values (?,?)",
                    newGenres, newGenres.size(), (ps, genre) -> {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genre.getId());
                    });
            entity.getGenres().addAll(film.getGenres());
        } else {
            entity.setGenres(film.getGenres());
        }
        return entity;
    }

    @Override
    public Optional<Film> findById(int id) {
        return jdbcTemplate.query(
                        SELECT_ALL + " where films.id = ?", this::mapRow, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(SELECT_ALL, this::mapRow);
    }

    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Список жанров по id фильма
        int filmId = rs.getInt("id");

        List<Genre> genres = genreStorage.findAllByFilmId(filmId);
        Set<Genre> genreSet = new TreeSet<>(genres);

        // Список id пользователей, которые поставили лайк фильму
        String likesSelect = """
                select user_id
                from films_users_likes
                where film_id = ?
                """;
        List<Integer> likes = jdbcTemplate.query(likesSelect,
                (rs1, rowNum1) -> rs1.getInt("user_id"), filmId);
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
