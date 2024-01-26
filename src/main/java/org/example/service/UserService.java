package org.example.service;

import com.google.gson.JsonObject;
import org.example.mappers.UserMapper;
import org.example.mappers.UserSessionsMapper;
import org.example.models.User;
import org.example.models.UserSessions;
import org.example.request.LoginRequest;
import org.example.request.LogoutRequest;
import org.example.request.RegisterRequest;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserService {

    private UserMapper userMapper;
    private UserSessionsMapper userSessionsMapper;

    public JsonObject register(RegisterRequest request) {
        JsonObject response = new JsonObject();

        try {
            if (userMapper.existsByUsername(request.getUsername())) {
                response.addProperty("code", "REGISTER_CONFLICT");
                response.addProperty("error_message", "username is existed");
                return response;
            }

            if (!isValidRole(request.getRole())) {
                response.addProperty("code", "REGISTER_NOT_FOUND");
                response.addProperty("error_message", "role is invalid");
                return response;
            }

            User newUser = new User();
            newUser.setUsername(request.getUsername());
            newUser.setPassword(request.getPassword());
            newUser.setFullname(request.getFullname());
            newUser.setRole(request.getRole());

            userMapper.insertUser(newUser);

            response.addProperty("code", "REGISTER_CREATED");
            response.addProperty("session", "example");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.addProperty("code", "INTERNAL_SERVER_ERROR");
            response.addProperty("error_message", "Internal Server Error");
            return response;
        }
    }

    private boolean isValidRole(String role) {
        return "teacher".equals(role) || "student".equals(role);
    }

    public JsonObject login(LoginRequest request) {
        JsonObject response = new JsonObject();
        Optional<User> userOptional = userMapper.findByUsernameAndPassword(request.getUsername(), request.getPassword());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // log in successfully
            response.addProperty("code", "LOGIN_OK");
            response.addProperty("fullname", user.getFullname());
            response.addProperty("role", user.getRole());
            return response;
        } else {
            // login failed
            response.addProperty("code", "UNAUTHORIZED");
            response.addProperty("error_message", "username or password is incorrect");
            return response;
        }
    }

    public JsonObject logout(LogoutRequest request) {
        JsonObject response = new JsonObject();
        // Tìm phiên làm việc dựa trên sessionKey
        UserSessions userSession = userSessionsMapper.findBySessionKey(request.getSession());

        if (userSession != null) {
            // Hủy phiên làm việc bằng cách đặt expiresAt là thời điểm hiện tại hoặc một giá trị tương lai
            userSession.setExpiresAt(LocalDateTime.now().plusMinutes(30)); // Ví dụ: hủy sau 30 phút
            userSessionsMapper.save(userSession);

            // log out success
            response.addProperty("code", "LOGOUT_OK");
        } else {
            // session not found
            response.addProperty("code", "LOGOUT_FAILED");
            response.addProperty("message", "Session not found");
        }

        return response;
    }
}
