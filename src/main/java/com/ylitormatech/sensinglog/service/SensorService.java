package com.ylitormatech.sensinglog.service;

import com.mongodb.BasicDBObject;
import com.ylitormatech.sensinglog.data.entity.SensorData;

import java.util.List;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
public interface SensorService {

    //public void createSensorDataEntity(String id,String value);

    public void saveMessage(String message);
    public void saveMessage(BasicDBObject message);

    public Integer removeMessages(Integer sensorId, Long start, Long end);

    public List<SensorData> getMessages(Integer sensorId, Long start, Long end);
}
