package com.ylitormatech.sensinglog.service;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
public interface ApiKeyService {
    public void createApiKey(String key);
    public void updateApiKey(String key, boolean isActivated);
    public Boolean removeApiKey(String key);
    public Boolean testAPIKey(String key);
    public Integer getSensorIdByApiKey(String key);
    public Integer checkSensorValidityByApiKey(String key);
}
