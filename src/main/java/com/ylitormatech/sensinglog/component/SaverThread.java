package com.ylitormatech.sensinglog.component;

import com.ylitormatech.sensinglog.service.SensorService;

/**
 * Created by mika on 10.6.2016.
 */
public class SaverThread implements Runnable {
    private String message;
    private SensorService sensorService;

    public SaverThread(SensorService sensServ, String msg) {
        this.sensorService = sensServ;
        this.message = msg;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start: Message = " + message);
        processSave();
        System.out.println(Thread.currentThread().getName() + " End");
    }

    /* Message data contains two items separated by comma:
        1. ApiKey
        2. Message data
       Separate them and pass to sensorService to be added in databse.
    */
    private void processSave() {
        if ( sensorService != null && !message.isEmpty() ) {

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
        }
    }
}
