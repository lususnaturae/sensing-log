package com.ylitormatech.sensinglog.data.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

/**
 * Created by mika on 10.7.2016.
 */

// Simple class to handle sensor messages in MongoDB.

@Document       // This is same to MongoDB as @Entity is for MySql (== persistable).
public class SensorMessage {
    @Id
    private Long msgId;
    private String message;     // sensor message as Json-format.

    public SensorMessage(String msg) {
        this.message = msg;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SensorMessage[" + msgId + "]: \"" +
                message + "\"";
    }
}
