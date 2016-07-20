package com.ylitormatech.sensinglog.data.repository.impl;


import com.mongodb.DBCursor;
import com.mongodb.WriteResult;
import com.ylitormatech.sensinglog.data.entity.SensorMessage;
import com.ylitormatech.sensinglog.data.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
@Repository
public class SensorRepositoryImpl implements SensorRepository {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MongoTemplate mongo;

    /*public void add(SensorEntity sensorEntity) {
        em.persist(sensorEntity);
    }*/

    public void addMessage(SensorMessage message) {
        mongo.insert(message);
    }

    @Override
    public Integer removeMessages(Integer sensorId, Long start, Long end) {

        Query query = new Query(Criteria.where("sensorid").is(sensorId));

        WriteResult result = mongo.remove(query, SensorMessage.class);
        return result.getN();
        //mongo.remove();
        //mongo.remove( {"sensorId" : sensorId });

        //return false;
    }
}
