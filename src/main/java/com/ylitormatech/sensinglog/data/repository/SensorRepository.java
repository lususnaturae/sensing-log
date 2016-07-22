package com.ylitormatech.sensinglog.data.repository;

import com.mongodb.BasicDBObject;
import com.ylitormatech.sensinglog.data.entity.SensorData;
import com.ylitormatech.sensinglog.data.entity.SensorMessage;

import java.util.List;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
public interface SensorRepository {

    //public void add(SensorEntity sensorEntity);

    public void addMessage(SensorMessage message);
    public void addMessage(BasicDBObject message);

    public Integer removeMessages(Integer sensorId, Long start, Long end);

    public List<SensorData> findMessages(Integer sensorId, Long start, Long end);
}
