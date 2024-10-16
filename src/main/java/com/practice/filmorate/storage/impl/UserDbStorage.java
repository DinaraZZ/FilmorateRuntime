package com.practice.filmorate.storage.impl;

import com.practice.filmorate.exception.NotFoundException;
import com.practice.filmorate.exception.ValidationException;
import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_ALL = "select * from users";

    @Transactional
    @Override
    public User add(User entity) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> map = Map.of(
                "login", entity.getLogin(),
                "email", entity.getEmail(),
                "name", entity.getName(),
                "birthday", entity.getBirthday()
        );

        int id = insert.executeAndReturnKey(map).intValue();
        entity.setId(id);

        // Добавление в таблицу USERS_FRIENDSHIP_STATUS
        Set<Integer> sortedFriends = new TreeSet<>(entity.getFriends());
        for (Integer friend : sortedFriends) {
            findById(friend)
                    .orElseThrow(() -> new NotFoundException("Друг не найден (id: " + friend + ")"));
        }

        entity.setFriends(sortedFriends);

        jdbcTemplate.batchUpdate("""
                        insert into users_friendship_status(first_user_id, second_user_id, status)
                        values (?, ?, ?)
                        """,
                sortedFriends, sortedFriends.size(), (ps, friend) -> {
                    ps.setInt(1, id);
                    ps.setInt(2, friend);
                    ps.setBoolean(3, false);
                }
        );

        return entity;
    }

    @Override
    public User update(User entity) {
        User user = findById(entity.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        int userId = user.getId();

        // Обновление таблицы USERS
        String update = """
                update users
                set login = ?,
                email = ?,
                name = ?,
                birthday = ?
                where id = ?
                """;
        jdbcTemplate.update(update, entity.getLogin(), entity.getEmail(), entity.getName(),
                entity.getBirthday(), userId);

        // Обновление таблицы USERS_FRIENDSHIP_STATUS
        Set<Integer> newFriends = entity.getFriends();
        if (newFriends != null) {
            // id : 1
            // новые друзья: (4), (5), (6), (8), 9
            // старые друзья:
            // 1 2 false -> удалить
            // 1 (4) false -> оставить
            // 1 (5) true -> оставить
            // 1 7 true -> оставить
            String friendSelect = """
                    select ufs.second_user_id friend_id, ufs.status status
                    from users_friendship_status ufs
                    where first_user_id = ?
                    """;
            Map<Integer, Boolean> friendsMap = jdbcTemplate.query(friendSelect, new Object[]{userId},
                            (RowMapper<Map.Entry<Integer, Boolean>>) (rs, rowNum) ->
                                    new HashMap.SimpleEntry<>(rs.getInt("friend_id"),
                                            rs.getBoolean("status")))
                    .stream()
                    .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(),
                            entry.getValue()), HashMap::putAll);

        /*for (Integer i : friendsMap.keySet()) {
            System.out.println(userId + " " + i + " " + friendsMap.get(i));
        }*/

            for (Integer friendId : friendsMap.keySet()) {
                boolean exists = false;
                for (Integer newFriendId : newFriends) {
                    if (friendId == newFriendId) {
                        exists = true;
                        break;
                    }
                }
                if (exists) {
                    newFriends.remove(friendId);
                } else {
                    boolean status = friendsMap.get(friendId);
                    if (!status) {
                        jdbcTemplate.update("""
                                        delete from users_friendship_status
                                        where first_user_id = ? and second_user_id = ?
                                        """,
                                userId, friendId
                        );
                    }
                }
            }

            // id : 1
            // новые друзья: (4), (5), (6), (8), 9
            // старые друзья:
            // 2 1 true -> оставить
            // (6) 1 true -> оставить
            // (8) 1 false -> оставить, поменять статус на true
            // 10 1 false -> оставить
            friendSelect = """
                    select ufs.first_user_id friend_id, ufs.status status
                    from users_friendship_status ufs
                    where second_user_id = ?
                    """;

            friendsMap = jdbcTemplate.query(friendSelect, new Object[]{userId},
                            (RowMapper<Map.Entry<Integer, Boolean>>) (rs, rowNum) ->
                                    new HashMap.SimpleEntry<>(rs.getInt("friend_id"),
                                            rs.getBoolean("status")))
                    .stream()
                    .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(),
                            entry.getValue()), HashMap::putAll);

        /*for (Integer i : friendsMap.keySet()) {
            System.out.println(i + " " + userId + " " + friendsMap.get(i));
        }*/

            for (Integer friendId : friendsMap.keySet()) {
                boolean exists = false;
                for (Integer newFriendId : newFriends) {
                    if (friendId == newFriendId) {
                        exists = true;
                        break;
                    }
                }
                if (exists) {
                    boolean status = friendsMap.get(friendId);
                    if (!status) {
                        jdbcTemplate.update("""
                                        update users_friendship_status
                                        set status = true
                                        where first_user_id = ? and second_user_id = ?
                                        """,
                                friendId, userId);
                    }
                    newFriends.remove(friendId);
                }
            }

            if (newFriends.size() > 0) {
                jdbcTemplate.batchUpdate("""
                                insert into 
                                users_friendship_status(first_user_id, second_user_id, status) 
                                values (?,?,?)
                                """,
                        newFriends, newFriends.size(), (ps, friend) -> {
                            ps.setInt(1, userId);
                            ps.setInt(2, friend);
                            ps.setBoolean(3, false);
                        });
            }
            entity.getFriends().addAll(user.getFriends());
        } else {
            entity.setFriends(user.getFriends());
        }

        return entity;
    }

    @Override
    public Optional<User> findById(int id) {
        return jdbcTemplate.queryForStream(SELECT_ALL + " where id = ?", this::mapRow, id)
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL, this::mapRow);
    }

    private User mapRow(ResultSet rs, int rowNum) throws SQLException {
        int userId = rs.getInt("id");
        // Список id друзей
        // 1 false 2 (у 1-го в друзьях: 2, у 2-го в друзьях: -)
        // 1 true 2 (у 1-го в друзьях: 2, у 2-го в друзьях: 1)

        // друзья, которым кинул дружбу пользователь
        String friendSelect = """
                select ufs.second_user_id friend_id
                from users_friendship_status ufs
                where first_user_id = ?
                """;
        List<Integer> friends1 = jdbcTemplate.query(friendSelect,
                (rs1, rowNum1) -> rs1.getInt("friend_id"), userId);

        // друзья, заявку которых принял пользователь
        friendSelect = """
                select ufs.first_user_id friend_id
                from users_friendship_status ufs
                where second_user_id = ? and status = true
                """;
        List<Integer> friends2 = jdbcTemplate.query(friendSelect,
                (rs1, rowNum1) -> rs1.getInt("friend_id"), userId);

        Set<Integer> friends = new TreeSet<>();
        friends.addAll(friends1);
        friends.addAll(friends2);

        return User.builder()
                .id(userId)
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(friends)
                .build();


    }
}
