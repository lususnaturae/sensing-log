package com.ylitormatech.sensinglog.data.repository.impl;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.ylitormatech.sensinglog.data.entity.SensorData;
import com.ylitormatech.sensinglog.data.entity.SensorMessage;
import com.ylitormatech.sensinglog.data.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
@Repository
public class SensorRepositoryImpl implements SensorRepository {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MongoTemplate mongo;

    @Override
    public void addMessage(SensorMessage message) {
        mongo.insert(message);
    }
    @Override
    public void addMessage(BasicDBObject message) {

        DBCollection collection = mongo.getCollection("sensorMessage");
        collection.insert(message);
    }

    @Override
    public Integer removeMessages(Integer sensorId, Long start, Long end) {

        Query query = new Query();
        if (start == 0 && end == 0) {   // Remove all for this sensor.
            query.addCriteria(
                    Criteria.where("sensorid").is(sensorId)
            );
        }
        else if (start == 0) {          // Remove up to the end time(stamp).
            query.addCriteria(
                    Criteria.where("sensorid").is(sensorId).andOperator(
                            Criteria.where("timestamp").lt(end)
                    )
            );
        }
        else if (end == 0) {            // Remove beginning from the start time(stamp).
            query.addCriteria(
                    Criteria.where("sensorid").is(sensorId).andOperator(
                            Criteria.where("timestamp").gt(start)
                    )
            );
        }
        else {                          // Remove between start and end time(stamps).
            query.addCriteria(
                    Criteria.where("sensorid").is(sensorId).andOperator(
                            Criteria.where("timestamp").gt(start),
                            Criteria.where("timestamp").lt(end)
                    )
            );
        }

        //WriteResult result = mongo.remove(query, SensorMessage.class);
        //return result.getN();
        //mongo.remove();
        //mongo.remove( {"sensorId" : sensorId });
        //return false;

        return (mongo.remove(query, "sensorMessage")).getN();
    }

    @Override
    public List<SensorData> findMessages(Integer sensorId, Long start, Long end) {

        //{"sensorid":2,"timestamp":1234567890,"datatype":"ACC","value":{"x":0.4,"y":0.3,"z":0.3}}
        //{"sensorid":2,"timestamp":1234567890,"datatype":"HUM","value":20}

        Query query = new Query();
        query.addCriteria(
            Criteria.where("sensorid").is(sensorId).andOperator(
                Criteria.where("timestamp").gt(start),
                Criteria.where("timestamp").lt(end)
            )
        );

        List<SensorData> messages =  mongo.find(query, SensorData.class, "sensorMessage");

        return messages;
    }
}
