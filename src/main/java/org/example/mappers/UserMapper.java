package org.example.mappers;

import org.example.models.User;

import java.util.Optional;

public interface UserMapper {
    User getUser(int id);
    Optional<User> findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);

    void insertUser(User newUser);




}

