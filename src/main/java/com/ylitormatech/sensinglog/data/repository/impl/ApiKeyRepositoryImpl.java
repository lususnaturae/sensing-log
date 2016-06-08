package com.ylitormatech.sensinglog.data.repository.impl;

import com.ylitormatech.sensinglog.data.entity.ApiKeyEntity;
import com.ylitormatech.sensinglog.data.repository.ApiKeyRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
