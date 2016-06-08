package com.ylitormatech.sensinglog.endpoint.impl;

import com.ylitormatech.sensinglog.endpoint.MessagingSensorEP;
import com.ylitormatech.sensinglog.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by mika on 8.6.2016.
 */
@Component("messagingSensorEP")
public class MessagingSensorEPImpl implements MessagingSensorEP {

    @Autowired
    SensorService sensorService;

    public String receive(String message) {

        System.out.println("Sensor sent: " + message);

        return "OK";
    }
}
