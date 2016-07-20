package com.ylitormatech.sensinglog.data.repository;

import com.ylitormatech.sensinglog.data.entity.SensorMessage;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
public interface SensorRepository {

    //public void add(SensorEntity sensorEntity);

    public void addMessage(SensorMessage message);

    public Integer removeMessages(Integer sensorId, Long start, Long end);
}
