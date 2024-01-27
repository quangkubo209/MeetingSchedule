package org.example.models;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserSessions {
    private int id;
    private int userId;
    private String sessionKey;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    // Getters và Setters
}
