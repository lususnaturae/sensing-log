package com.ylitormatech.sensinglog.data.repository.impl;

import com.ylitormatech.sensinglog.data.entity.ApiKeyEntity;
import com.ylitormatech.sensinglog.data.repository.ApiKeyRepository;
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

    @PersistenceContext
    EntityManager em;

    public void add(ApiKeyEntity apiKeyEntity) {
        em.persist(apiKeyEntity);
    }

    public void remove(String key){
        ApiKeyEntity apiKeyEntity;
        apiKeyEntity = findEntity(key);
        em.remove(apiKeyEntity);
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

}
