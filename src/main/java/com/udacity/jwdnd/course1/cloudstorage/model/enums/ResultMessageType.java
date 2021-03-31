package com.udacity.jwdnd.course1.cloudstorage.model.enums;

public enum ResultMessageType {
    ERROR("error message"),
    SUCCESS("success message");

    private final String messageType;

    ResultMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }
}
