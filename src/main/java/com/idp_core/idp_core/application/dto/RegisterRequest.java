package com.idp_core.idp_core.application.dto;

import jakarta.persistence.Column;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private boolean twoFactor;
    private String twoFactorCode;
    private String name;
    private String lastname;
    private String identification;
    private String phone;
    // Constructor vacío (necesario para serialización/deserialización en Spring)
    public RegisterRequest() {}

    // Constructor completo
    public RegisterRequest(String username, String email, String password,boolean twoFactor,String name,String lastname,String identification,String phone) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.twoFactor= twoFactor;
        this.name= name;
        this.lastname= lastname;
        this.identification= identification;
        this.phone= phone;
    }

    // Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public boolean getTwoFactor() {
        return twoFactor;
    }

    public void setTwoFactor(boolean twoFactor) {
        this.twoFactor = twoFactor;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getIdentification() {
        return identification;
    }
    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    // toString (útil para logs y debugging)
    @Override
    public String toString() {
        return "RegisterRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}
