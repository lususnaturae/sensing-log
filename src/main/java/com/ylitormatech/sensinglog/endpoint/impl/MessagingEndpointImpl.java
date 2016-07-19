package com.ylitormatech.sensinglog.endpoint.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.ylitormatech.sensinglog.data.component.SaverThread;
import com.ylitormatech.sensinglog.endpoint.MessagingEndpoint;
import com.ylitormatech.sensinglog.service.ApiKeyService;
import com.ylitormatech.sensinglog.service.SensorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    // ========================================================================================
    private final int workerPoolSize = 3;
    private ExecutorService saveWorkers = null;

    // See root.xml for the PostConstruct bean.
    // This is run after any constructors and sertters, but before anything else.
    @PostConstruct
    public void initSensorThreads() throws Exception {
        System.out.println("CREATING MessagingEndpointImpl: setting up thread pool...");
        saveWorkers = Executors.newFixedThreadPool(workerPoolSize);
        System.out.println("CREATING MessagingEndpointImpl: Done!");
    }

    // This is run just before the class is destroyed.
    // NOTICE: The application must be close by Exit !!!, not by Stop !!!!
    @PreDestroy
    public void cleanSensorThreads() throws Exception {
        System.out.println("DESTROY MessagingEndpointImpl: running down thread pool...");
        saveWorkers.shutdown();
        while (!saveWorkers.isTerminated()) { }
        System.out.println("DESTROY MessagingEndpointImpl: Done!");
    }
    // ========================================================================================

    public String newApiKey(String message) {
        /*
        String message: Json-formatted string containing necessary sensor data to
        store incoming data from the sensor, and handle the data reading requests
        from user. The content of the message is:
        - ApiKey:   Currently used apikey (sensor sending id).
        - SensorId: Sensor database id.
        - Sensor data types: List of data types sensor (is allowed) to send.

        Notes:
        - ApiKey can be changed. It only identifies the sensor during the
        sending operation and is valid until reset. The ApiKey sensor id
        will be replaces with the SensorId when data is saved in database.
        - SensorId is the permanent unique id given for the sensor by the system.
        It is not visible to outside the core application.
         */
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
        logger.debug("receiveData(): datamessage: " + message);
        /*
        *  Message from sensor: Save it to Mongo db.
        */
        Runnable saver = new SaverThread(apiKeyService, sensorService, message);
        saveWorkers.execute(saver);

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
            if(apiKeyService.testAPIKey(map.get("id"))) {
                logger.debug(className + " ApiKey found");

                //sensorService.createSensorDataEntity(map.get("id"), map.get("value"));
                // message here should contain the apikey in it. Now they are
                // separated: What is the content of value?
                // TODO
                Runnable saver = new SaverThread(apiKeyService, sensorService, map.get("value"));
                saveWorkers.execute(saver);

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
