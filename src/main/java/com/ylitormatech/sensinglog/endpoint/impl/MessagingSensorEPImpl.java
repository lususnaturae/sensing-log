package com.ylitormatech.sensinglog.endpoint.impl;

import com.ylitormatech.sensinglog.component.SaverThread;
import com.ylitormatech.sensinglog.endpoint.MessagingSensorEP;
import com.ylitormatech.sensinglog.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mika on 8.6.2016.
 *
 * This module handle messages (sensor data) from sensors.
 */
@Component("messagingSensorEP")
public class MessagingSensorEPImpl implements MessagingSensorEP {

    @Autowired
    SensorService sensorService;

    //private Thread[] saveWorkers;
    private int workerPoolSize = 3;
    private ExecutorService saveWorkers = null;

    // See root.xml for the PostConstruct bean.
    // This is run after any constructors and sertters, but before anything else.
    @PostConstruct
    public void initSensorThreads() throws Exception {
        System.out.println("CREATING MessagingSensorEP: setting up thread pool...");
        /*saveWorkers = new Thread[workerPoolSize];
        for (int i=0; i < workerPoolSize; i++) {
            saveWorkers[i] = new Thread(new SaverThread());
            saveWorkers[i].start();
        }*/
        saveWorkers = Executors.newFixedThreadPool(workerPoolSize);
        System.out.println("CREATING MessagingSensorEP: Done!");
    }

    // This is run just before the class is destroyed.
    // NOTICE: The application must be close by Exit !!!, not by Stop !!!!
    @PreDestroy
    public void cleanSensorThreads() throws Exception {
        System.out.println("DESTROY MessagingSensorEP: running down thread pool...");
        /*for (Thread worker : saveWorkers) {
            worker.setRunning(false);
        }

        // Now join with each thread to make sure we give them time to stop gracefully
        for (Thread worker : saveWorkers) {
            worker.join();  // May want to use the one that allows a millis for a timeout
        }*/
        saveWorkers.shutdown();
        while (!saveWorkers.isTerminated()) { }
        System.out.println("DESTROY MessagingSensorEP: Done!");
    }


    public String receive(String message) {

        Runnable saver = new SaverThread(sensorService, message);
        saveWorkers.execute(saver);

        // Just for testing: This must be implemented as queue (or such) and
        // let a thread take care of the database writing. Make sure this
        // call returns as fast as possible!

        /*
        System.out.println("Sensor sent: " + message);

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
        */
        return "OK";
    }
}
