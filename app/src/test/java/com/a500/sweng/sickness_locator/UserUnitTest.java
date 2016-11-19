package com.a500.sweng.sickness_locator;

import com.a500.sweng.sickness_locator.models.User;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserUnitTest {
    @Test
    public void test_name() throws Exception {
        String userName = "Test User";
        User user = new User();
        user.setName(userName);

        assertEquals(user.getName(), userName );
        assertNotEquals(user.getName(), "username");
    }

    @Test
    public void test_email() throws Exception {
        String userEmail = "test@test.com";
        User user = new User();
        user.setEmail(userEmail);

        assertEquals(user.getEmail(), userEmail );
        assertNotEquals(user.getName(), "username@username.com");
    }

    @Test
    public void test_dob() throws Exception {
        String userDOB = "12/31/1970";
        User user = new User();
        user.setDob(userDOB);

        assertEquals(user.getDob(), userDOB);
        assertNotEquals(user.getDob(), "10/10/1982");
    }

    @Test
    public void test_gender() throws Exception {
        String userGender = "Female";
        User user = new User();
        user.setGender(userGender);
        assertEquals(user.getGender(), userGender);
        assertNotEquals(user.getDob(), "Male");
    }
}