package com.ylitormatech.sensinglog.endpoint;

import org.springframework.messaging.Message;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
public interface MessagingEndpoint {

    public String receive(String message);

    public Message<String> sensorHttpPost(Message<String> msg);
}
