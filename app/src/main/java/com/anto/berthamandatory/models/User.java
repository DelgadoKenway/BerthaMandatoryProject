package com.anto.berthamandatory.models;

public class User {
    private int id;
    private String mail;
    private String Password;
    public User(){

    }
    public User(int ID, String email, String pass){
        id = ID;
        mail = email;
        Password= pass;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

}