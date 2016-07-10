package com.ylitormatech.sensinglog.endpoint.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.ylitormatech.sensinglog.endpoint.MessagingEndpoint;
import com.ylitormatech.sensinglog.service.ApiKeyService;
import com.ylitormatech.sensinglog.service.SensorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
@Component("messagingEndpoint")
public class MessagingEndpointImpl implements MessagingEndpoint {

    private static final String STATUSCODE_HEADER = "http_statusCode";

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    SensorService sensorService;

    @Autowired
    ApiKeyService apiKeyService;


    public String newApiKey(String message) {
        String className = "newApiKey";
        logger.debug(className + " key: "+ message);

        apiKeyService.createApiKey(message);
        return "OK";
    }

    public String removeApiKey(String message) {
        String className = "removeApiKey";
        logger.debug(className + " key: "+ message);

        apiKeyService.removeApiKey(message);
        return "OK";
    }

    public String activateApiKey(String message) {
        String className = "activateApiKey";
        logger.debug(className + " key: "+ message);

        apiKeyService.updateApiKey(message, true);
        return "OK";
    }
    public String deactivateApiKey(String message) {
        String className = "deactivateApiKey";
        logger.debug(className + " key: "+ message);

        apiKeyService.updateApiKey(message, false);
        return "OK";
    }


    public String receiveData(String message) {
        String className = "receiveData";
        logger.debug(className + " datamessage: "+ message);
        /*
        *  Here comes data handling
        */

        // message is json-formatted.
        // TODO: separate token from message and check validity
        // TODO: fetch individual data items from message and save to MongoDB as object

        sensorService.saveMessage(message);


        return "OK";
    }

    public Message<String> sensorHttpPost(Message<String> msg) {
        String className = "sensorHttpPost";
        logger.debug(className + " datamessage: "+ msg.getPayload());

        Map<String, String> map = new HashMap<String, String>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.
                WRITE_DATES_AS_TIMESTAMPS , false);

        try {
            map = mapper.readValue(msg.getPayload(), new TypeReference<Map<String, String>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return MessageBuilder.withPayload("Error")
                    .copyHeadersIfAbsent(msg.getHeaders())
                    .setHeader(STATUSCODE_HEADER, HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

        if(map.containsKey("id") && map.containsKey("value")) {
            logger.debug(className + " id and value exist");
            if(apiKeyService.getAPIKeyId(map.get("id"))) {
                logger.debug(className + " ApiKey found");
                sensorService.createSensorDataEntity(map.get("id"), map.get("value"));
                return MessageBuilder.withPayload("ok")
                        .copyHeadersIfAbsent(msg.getHeaders())
                        .setHeader(STATUSCODE_HEADER, HttpStatus.OK)
                        .build();
            }
            logger.debug(className + " Apikey not found");
        }

        return MessageBuilder.withPayload("error")
                .copyHeadersIfAbsent(msg.getHeaders())
                .setHeader(STATUSCODE_HEADER, HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

}
