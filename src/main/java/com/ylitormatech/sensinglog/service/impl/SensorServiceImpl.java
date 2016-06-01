package com.ylitormatech.sensinglog.service.impl;

import com.ylitormatech.sensinglog.data.entity.SensorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ylitormatech.sensinglog.data.repository.SensorRepository;
import com.ylitormatech.sensinglog.service.SensorService;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
@Service
public class SensorServiceImpl implements SensorService {

    @Autowired
    SensorRepository sensorRepository;

    @Transactional(readOnly = false)
    public void createSensorDataEntity(String id,String value) {
        SensorEntity sensorEntity = new SensorEntity();
        sensorEntity.setAPIKey(id);
        sensorEntity.setValue(Double.valueOf(value));
        sensorRepository.add(sensorEntity);
    }
}
