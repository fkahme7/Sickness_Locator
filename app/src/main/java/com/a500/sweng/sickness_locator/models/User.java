package com.a500.sweng.sickness_locator.models;

public class User {
    public String name;
    public String email;

    public User() {}

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
}
