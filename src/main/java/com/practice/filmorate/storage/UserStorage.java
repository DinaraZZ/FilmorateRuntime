package com.practice.filmorate.storage;

import com.practice.filmorate.model.User;

import java.util.List;


public interface UserStorage extends Storage<User>{

    void addFriend(int userId, int friendId);
    void deleteFriend(int userId, int friendId);
    List<User> findAllFriends(int userId);
}
