package com.udacity.jwdnd.course1.cloudstorage.model;

import com.udacity.jwdnd.course1.cloudstorage.model.enums.ResultMessageType;

public class ResultMessage {

    private String message;
    private ResultMessageType messageType;

    public ResultMessage() {
    }

    public ResultMessage(String message, ResultMessageType messageType) {
        this.message = message;
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(ResultMessageType messageType) {
        this.messageType = messageType;
    }
}
