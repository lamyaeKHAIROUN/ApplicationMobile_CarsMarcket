package com.example.carsappproject.Entities;

public class User {

    private String fullName,email,pseudo,tel;

    public User() {
    }

    public User(String fullName, String email, String pseudo, String tel) {
        this.fullName = fullName;
        this.email = email;
        this.pseudo = pseudo;
        this.tel = tel;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
