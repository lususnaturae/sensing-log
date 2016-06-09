package com.ylitormatech.sensinglog.service.impl;

import com.ylitormatech.sensinglog.data.entity.SensorLogDataEntity;
import com.ylitormatech.sensinglog.data.entity.SensorLogEntity;
import com.ylitormatech.sensinglog.data.repository.SensorDataRepository;
import com.ylitormatech.sensinglog.data.repository.SensorRepository;
import com.ylitormatech.sensinglog.service.SensorService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
@Service
public class SensorServiceImpl implements SensorService {

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    SensorDataRepository sensorDataRepository;

    @Transactional(readOnly = false)
    public void createSensor(String apiKey) {
        SensorLogEntity sensorLogEntity = new SensorLogEntity();
        sensorLogEntity.setAPIKey(apiKey);
        //sensorLogEntity.setValue(Double.valueOf(value));
        sensorRepository.add(sensorLogEntity);

    }

    public List<SensorLogEntity> findAll() {
        return null;
    }

    public SensorLogEntity find(String apiKey) {
        return null;
    }

    @Transactional(readOnly = false)
    public SensorLogEntity update(SensorLogEntity sle) {
        return null;
    }

    @Transactional(readOnly = false)
    public SensorLogEntity delete(String apiKey) {
        return null;
    }

    @Transactional(readOnly = false)
    public void addSensorData(String apiKey, String data) {
        SensorLogEntity sensor = sensorRepository.find(apiKey);
        if (sensor != null) {
            SensorLogDataEntity sensData = new SensorLogDataEntity();
            sensData.setSensorid(sensor.getId());
            sensData.setData(data);
            sensData.setCreated(DateTime.now());

            sensorDataRepository.add(sensData);
        }
    }

}
