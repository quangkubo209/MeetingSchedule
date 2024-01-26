package org.example.request;

import lombok.Getter;

public class RegisterRequest {

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    private String action;

    public void setUsername(String username) {
        this.username = username;
    }

    @Getter
    private String username;
    private String password;

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullname() {
        return fullname;
    }

    private String fullname;
    @Getter
    private String role;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // getters và setters
}
