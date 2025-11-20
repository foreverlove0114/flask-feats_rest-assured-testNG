package com.restaurant.models;

public class User {
    private String nickname;
    private String email;
    private String contact;
    private String fullAddress;
    private String password;

    public User() {}

    public User(String nickname, String email, String contact, String fullAddress, String password) {
        this.nickname = nickname;
        this.email = email;
        this.contact = contact;
        this.fullAddress = fullAddress;
        this.password = password;
    }

    // Getters and Setters
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getFullAddress() { return fullAddress; }
    public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}