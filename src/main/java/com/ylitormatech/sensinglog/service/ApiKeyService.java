package com.ylitormatech.sensinglog.service;

import java.util.List;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
public interface ApiKeyService {
    public void createApiKey(String key);
    public List<String> getApiKeys();
    public void updateApiKey(String key, boolean isActivated);
    public void removeApiKey(String key);
    public boolean testAPIKey(String key);
    public Integer getSensorIdByApiKey(String key);
    public Integer checkSensorValidityByApiKey(String key);
}
