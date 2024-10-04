package com.practice.filmorate.service;

import com.practice.filmorate.exception.NotFoundException;
import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
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

    public User addFriend(int userId, int friendId) {
        Optional<User> userOpt = userStorage.findById(userId);
        Optional<User> friendOpt = userStorage.findById(friendId);
        if (userOpt.isPresent() && friendOpt.isPresent()) {
            User user = userOpt.get();
            User friend = friendOpt.get();

            Set<Integer> userFriends = user.getFriends();
            userFriends.add(friendId);
//            user.setFriends(userFriends);

            Set<Integer> friendFriends = friend.getFriends();
            friendFriends.add(userId);
//            friend.setFriends(friendFriends);

//            userStorage.update(user);
//            userStorage.update(friend);
            return user;
        } else throw new NotFoundException("Пользователь не найден");
    }

    public User deleteFriend(int userId, int friendId) {
        Optional<User> userOpt = userStorage.findById(userId);
        Optional<User> friendOpt = userStorage.findById(friendId);
        if (userOpt.isPresent() && friendOpt.isPresent()) {
            User user = userOpt.get();
            User friend = friendOpt.get();

            Set<Integer> userFriends = user.getFriends();
            userFriends.remove(friendId);
//            user.setFriends(userFriends);

            Set<Integer> friendFriends = friend.getFriends();
            friendFriends.remove(userId);
//            friend.setFriends(friendFriends);

//            userStorage.update(user);
//            userStorage.update(friend);
            return user;
        } else throw new NotFoundException("Пользователь не найден");
    }

    public List<User> findFriends(int userId) {
        Optional<User> userOpt = userStorage.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Set<Integer> userFriends = user.getFriends();

            if (userFriends.isEmpty()) {
                return Collections.EMPTY_LIST;
            }

            return userFriends.stream()
                    .map(friendId -> findById(friendId).get()) // ????????????????????
                    .toList();
        } else throw new NotFoundException("Пользователь не найден");
    }

    public List<User> findCommonFriends(int firstId, int secondId) {
        Optional<User> firstUserOpt = userStorage.findById(firstId);
        Optional<User> secondUserOpt = userStorage.findById(secondId);
        if (firstUserOpt.isPresent() && secondUserOpt.isPresent()) {
            User firstUser = firstUserOpt.get();
            User secondUser = secondUserOpt.get();

            Set<Integer> firstFriends = firstUser.getFriends();
            Set<Integer> secondFriends = secondUser.getFriends();

            if (firstFriends.isEmpty() || secondFriends.isEmpty()) {
                return Collections.EMPTY_LIST;
            }

            Set<Integer> commonFriends = new HashSet<>(firstFriends);
            commonFriends.retainAll(secondFriends);

            return commonFriends.stream()
                    .map(friendId -> findById(friendId).get()) // ????????????????????
                    .toList();
        } else throw new NotFoundException("Пользователь не найден");
    }
}
