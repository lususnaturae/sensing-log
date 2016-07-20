package com.ylitormatech.sensinglog.data.repository.impl;

import com.ylitormatech.sensinglog.data.entity.ApiKeyEntity;
import com.ylitormatech.sensinglog.data.repository.ApiKeyRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
@Repository
public class ApiKeyRepositoryImpl implements ApiKeyRepository{

    Logger logger = Logger.getLogger(this.getClass().getName());

    @PersistenceContext
    EntityManager em;

    public void add(ApiKeyEntity apiKeyEntity) {
        em.persist(apiKeyEntity);
    }

    public ApiKeyEntity remove(Integer sensorId){
        ApiKeyEntity apiKeyEntity = null;

        String hql = "FROM ApiKeyEntity a WHERE a.sensorId = :sensorId";
        Query query = em.createQuery(hql);
        query.setParameter("sensorId", sensorId);

        try {
            apiKeyEntity = (ApiKeyEntity) query.getSingleResult();
        }
        catch (Exception e) {
            logger.debug("ApiKeyRepositoryImpl: Cannot remove sensor with sensorId: " + sensorId);
        }
        return apiKeyEntity;

        //em.remove(apiKeyEntity);
    }

    public void update(ApiKeyEntity apiKeyEntity) {em.merge(apiKeyEntity);}

    public ApiKeyEntity findEntity(String apikey){
        return em.createQuery("FROM ApiKeyEntity a WHERE a.apikey=:apikey", ApiKeyEntity.class)
                .setParameter("apikey", apikey)
                .getSingleResult();
    }

    public boolean find(String apikey){
        List<ApiKeyEntity>  apiKeyEntityList= em.createQuery("FROM ApiKeyEntity a WHERE a.apikey=:apikey")
                .setParameter("apikey", apikey)
                .getResultList();
        if(!apiKeyEntityList.isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public Integer findSensorIdByApiKey(String apikey) {
        ApiKeyEntity ake = null;
        int id = 0;

        try {
            ake = em.createQuery("FROM ApiKeyEntity a WHERE a.apikey=:apikey", ApiKeyEntity.class)
                    .setParameter("apikey", apikey)
                    .getSingleResult();
            if (ake != null) {
                id = ake.getSensorId();
            }
        }
        catch (Exception e) {
            logger.error("ApiKey-table query exception: " + e.getMessage());
        }
        return id;
    }

    @Override
    public Integer checkSensorValidity(String apikey) {
        ApiKeyEntity ake = null;
        int id = 0;

        try {
            ake = em.createQuery("FROM ApiKeyEntity a WHERE a.apikey=:apikey", ApiKeyEntity.class)
                    .setParameter("apikey", apikey)
                    .getSingleResult();
            if (ake != null && ake.isActivated()) {
                id = ake.getSensorId();
            }
        }
        catch (Exception e) {
            logger.error("ApiKey-table query exception: " + e.getMessage());
        }
        return id;
    }
}
