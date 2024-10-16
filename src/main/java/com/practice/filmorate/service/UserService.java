package com.practice.filmorate.service;

import com.practice.filmorate.exception.NotFoundException;
import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Optional<User> findById(int id) {
        return userStorage.findById(id);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(int userId, int friendId) {
        User user = getById(userId);
        User friend = getById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public User deleteFriend(int userId, int friendId) {
        User user = getById(userId);
        User friend = getById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        return user;
    }

    public List<User> findFriends(int userId) {
        User user = getById(userId);
        Set<Integer> userFriends = user.getFriends();
        if (userFriends.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        return userFriends.stream()
                .map(friendId -> getById(friendId))
                .toList();
    }

    public List<User> findCommonFriends(int firstId, int secondId) {
        User firstUser = getById(firstId);
        User secondUser = getById(secondId);

        Set<Integer> firstFriends = firstUser.getFriends();
        Set<Integer> secondFriends = secondUser.getFriends();

        if (firstFriends.isEmpty() || secondFriends.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        Set<Integer> commonFriends = new HashSet<>(firstFriends);
        commonFriends.retainAll(secondFriends);
        System.out.println(commonFriends);

        return commonFriends.stream()
                .map(friendId -> getById(friendId)) // ????????????????????
                .toList();
    }

    private User getById(int id) {
        return userStorage.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
    }
}