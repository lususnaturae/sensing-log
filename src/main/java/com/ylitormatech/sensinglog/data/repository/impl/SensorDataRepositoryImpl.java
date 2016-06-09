package com.ylitormatech.sensinglog.data.repository.impl;

import com.ylitormatech.sensinglog.data.entity.SensorLogDataEntity;
import com.ylitormatech.sensinglog.data.repository.SensorDataRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by mika on 9.6.2016.
 */
@Repository
public class SensorDataRepositoryImpl implements SensorDataRepository {

    @PersistenceContext
    EntityManager em;


    public void add(SensorLogDataEntity slde) {
        if (slde == null) {
            return;
        }

        em.persist(slde);
    }
}
