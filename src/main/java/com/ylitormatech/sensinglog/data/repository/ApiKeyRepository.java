package com.ylitormatech.sensinglog.data.repository;

import com.ylitormatech.sensinglog.data.entity.ApiKeyEntity;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
public interface ApiKeyRepository {

    public void add(ApiKeyEntity apiKeyEntity);
    public void remove(String key);
    public void update(ApiKeyEntity apiKeyEntity);
    public ApiKeyEntity findEntity(String apikey);
    public boolean find(String apikey);

    public Integer findSensorIdByApiKey(String key);
    public Integer checkSensorValidity(String apikey);
}
