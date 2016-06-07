package com.ylitormatech.sensinglog.endpoint.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.ylitormatech.sensinglog.endpoint.MessagingEndpoint;
import com.ylitormatech.sensinglog.service.ApiKeyService;
import com.ylitormatech.sensinglog.service.SensorService;
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

    @Autowired
    SensorService sensorService;

    @Autowired
    ApiKeyService apiKeyService;


    public String newApiKey(String message) {
        /*
        *  Here comes ApiKey handling
        */
        apiKeyService.createApiKey(message);

        System.out.println("OK OK OK OK OK OK " + message);
        return "OK";
    }


    public String receive(String message) {
        /*
        *  Here comes ApiKey handling
        */
        System.out.println("OK OK OK OK OK OK " + message);
        return "OK";
    }

    public Message<String> sensorHttpPost(Message<String> msg) {

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
            String key = map.get("id");

            boolean isUsed = apiKeyService.getAPIKeyId(key);
            if(isUsed) {
                sensorService.createSensorDataEntity(map.get("id"), map.get("value"));
                return MessageBuilder.withPayload("ok")
                        .copyHeadersIfAbsent(msg.getHeaders())
                        .setHeader(STATUSCODE_HEADER, HttpStatus.OK)
                        .build();
            }
        }
        return MessageBuilder.withPayload("error")
                .copyHeadersIfAbsent(msg.getHeaders())
                .setHeader(STATUSCODE_HEADER, HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

}
