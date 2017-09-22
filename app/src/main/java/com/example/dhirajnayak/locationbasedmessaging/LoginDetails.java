package com.example.dhirajnayak.locationbasedmessaging;

import java.io.Serializable;

/**
 * Created by dhirajnayak on 9/18/17.
 */

public class LoginDetails implements Serializable {
    private int code;
    private String message,token,userName;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
