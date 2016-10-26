package com.a500.sweng.sickness_locator.models;

public class User {
    public String name;
    public String email;
    public String dob;
    public String gender;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
