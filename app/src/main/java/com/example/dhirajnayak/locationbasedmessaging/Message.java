package com.example.dhirajnayak.locationbasedmessaging;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dhirajnayak on 9/20/17.
 */

public class Message implements Serializable {
    private String sender,message,firstName,LastName, region;
    private int isRead, isLocked, messageId;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    private Date date;

    public String getFirstName() {
        return firstName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRead() {
        return isRead;
    }

    public void setRead(int read) {
        isRead = read;
    }

    public int getLocked() {
        return isLocked;
    }

    public void setLocked(int locked) {
        isLocked = locked;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
