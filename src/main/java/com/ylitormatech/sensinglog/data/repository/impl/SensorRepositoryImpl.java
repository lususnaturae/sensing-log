package com.ylitormatech.sensinglog.data.repository.impl;


import org.springframework.stereotype.Repository;
import com.ylitormatech.sensinglog.data.entity.SensorLogEntity;
import com.ylitormatech.sensinglog.data.repository.SensorRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
@Repository
public class SensorRepositoryImpl implements SensorRepository {

    @PersistenceContext
    EntityManager em;

    public void add(SensorLogEntity sensorLogEntity) {
        em.persist(sensorLogEntity);
    }

    public SensorLogEntity find(String apiKey) {

        return em.createQuery("FROM SensorLogEntity sle " +
                "WHERE sle.APIKey=:apiKey", SensorLogEntity.class).setParameter("apiKey",apiKey).getSingleResult();

    }
}
