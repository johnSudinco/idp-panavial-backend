package com.idp_core.idp_core.application.dto;

public class RegisterResponse {
    private Long userId;
    private String username;
    private String email;
    private String name;
    private String lastname;
    private String identification;
    private String phone;
    private String message;

    public RegisterResponse() {}

    public RegisterResponse(Long userId, String username, String email, String name, String lastname, String identification, String phone, String message) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.name = name;
        this.lastname = lastname;
        this.identification = identification;
        this.phone = phone;
        this.message = message;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getIdentification() { return identification; }
    public void setIdentification(String identification) { this.identification = identification; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone ; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

