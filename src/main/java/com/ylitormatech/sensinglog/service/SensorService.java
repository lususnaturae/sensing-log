package com.ylitormatech.sensinglog.service;

import com.ylitormatech.sensinglog.data.entity.SensorLogEntity;

import java.util.List;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
public interface SensorService {

    // Create sensor (as SensorLogDataEntity):
    public void createSensor(String apiKey);

    public List<SensorLogEntity> findAll(/*user cred must match the one in sensor*/);

    public SensorLogEntity find(String apiKey /*, user cred*/);

    public SensorLogEntity update(SensorLogEntity sle /*,user cred*/);

    public SensorLogEntity delete(String apiKey /*, user cred*/);

    // Add data for sensor (as SensorLogDataEntity; comes from sensor):
    public void addSensorData(String apiKey, String data);
}
