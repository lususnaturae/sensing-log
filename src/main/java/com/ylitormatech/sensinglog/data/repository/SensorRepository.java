package com.ylitormatech.sensinglog.data.repository;

import com.ylitormatech.sensinglog.data.entity.SensorLogEntity;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
public interface SensorRepository {

    public void add(SensorLogEntity sensorLogEntity);

    public SensorLogEntity find(String apiKey);
}
