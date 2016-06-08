package com.ylitormatech.sensinglog.service;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
public interface ApiKeyService {
    public void createApiKey(String key);
    public void removeApiKey(String key);
    public void updateApiKey(String key, boolean isActivated);
    public boolean getAPIKeyId(String id);
}
