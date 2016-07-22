package com.ylitormatech.sensinglog.service.impl;

import com.mongodb.BasicDBObject;
import com.ylitormatech.sensinglog.data.entity.SensorData;
import com.ylitormatech.sensinglog.data.entity.SensorMessage;
import com.ylitormatech.sensinglog.data.repository.SensorRepository;
import com.ylitormatech.sensinglog.service.SensorService;
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




    /*@Transactional(readOnly = false)
    public void createSensorDataEntity(String id,String value) {
        SensorEntity sensorEntity = new SensorEntity();
        sensorEntity.setAPIKey(id);
        sensorEntity.setValue(Double.valueOf(value));
        sensorRepository.add(sensorEntity);

    }*/

    @Transactional(readOnly = false)
    public void saveMessage(String message) {

        SensorMessage sensorMessage = new SensorMessage(message);
        sensorRepository.addMessage(sensorMessage);
    }

    @Transactional(readOnly = false)
    public void saveMessage(BasicDBObject message) {
        if (message != null) {
            sensorRepository.addMessage(message);
        }
    }

    @Transactional(readOnly = false)
    public Integer removeMessages(Integer sensorId, Long start, Long end) {
        // Remove all messages for specified sensor, and/or at the given time(stamp) range.
        // If start == 0, remove all up to 'end' time.
        // If end == 0, remove all from 'start' time onward.
        // If start == end == 0, remove all.
        // Method return number of removed documents, or zero if nono is removed.

        if (sensorId <= 0 || (end < start && end != 0) ) {
            return 0;
        }

        return sensorRepository.removeMessages(sensorId, start, end);
    }


    @Transactional(readOnly = false)
    public List<SensorData> getMessages(Integer sensorId, Long start, Long end) {

        if (sensorId <= 0 || end < start && end != 0) {
            return null;
        }

        return sensorRepository.findMessages(sensorId, start, end);
    }


}
