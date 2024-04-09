package com.example.quizapp.Models;

public class AccountModel  {
    private String id;
    private String email;
    private String username;
    private String photo;

    public AccountModel() {
    }

    public AccountModel(String id, String email, String username, String photo) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
