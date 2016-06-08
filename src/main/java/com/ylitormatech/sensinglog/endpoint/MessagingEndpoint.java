package com.ylitormatech.sensinglog.endpoint;

import org.springframework.messaging.Message;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
public interface MessagingEndpoint {

    public String receiveData(String message);

    public String newApiKey(String message);
    public String removeApiKey(String message);
    public String activateApiKey(String message);
    public String deactivateApiKey(String message);

    public Message<String> sensorHttpPost(Message<String> msg);
}
