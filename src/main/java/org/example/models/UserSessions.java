package org.example.models;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserSessions {
    private long id;
    private long userId;
    private String sessionKey;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    private User user;

    // Getter and Setter methods
}
