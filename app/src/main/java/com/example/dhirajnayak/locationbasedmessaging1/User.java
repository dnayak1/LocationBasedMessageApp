package com.example.dhirajnayak.locationbasedmessaging;

import java.io.Serializable;

/**
 * Created by dhirajnayak on 9/21/17.
 */

public class User implements Serializable {
    private String userName,firstName,lastName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
