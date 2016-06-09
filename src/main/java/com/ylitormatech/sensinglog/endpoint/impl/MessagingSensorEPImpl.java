package com.ylitormatech.sensinglog.endpoint.impl;

import com.ylitormatech.sensinglog.data.entity.SensorLogDataEntity;
import com.ylitormatech.sensinglog.endpoint.MessagingSensorEP;
import com.ylitormatech.sensinglog.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by mika on 8.6.2016.
 *
 * This module handle messages (sensor data) from sensors.
 */
@Component("messagingSensorEP")
public class MessagingSensorEPImpl implements MessagingSensorEP {

    @Autowired
    SensorService sensorService;

    public String receive(String message) {

        // Just for testing: This must be implemented as queue (or such) and
        // let a thread take care of the database writing. Make sure this
        // call returns as fast as possible!

        System.out.println("Sensor sent: " + message);

        SensorLogDataEntity slde = new SensorLogDataEntity();

        int commapos = message.indexOf(',');
        String strApiKey;
        String strMsg = null;

        if (commapos > 0) {
            strApiKey = message.substring(0, commapos);
        }
        else {
            strApiKey = message;
        }

        if (commapos < message.length()) {
            strMsg = message.substring(commapos + 1);
        }

        sensorService.addSensorData(strApiKey, strMsg);

        return "OK";
    }
}
