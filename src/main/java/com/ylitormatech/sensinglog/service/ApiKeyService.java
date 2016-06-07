package com.ylitormatech.sensinglog.service;

/**
 * Created by Perttu Vanharanta on 1.6.2016.
 */
public interface ApiKeyService {
    public void createApiKey(String key);
    public boolean getAPIKeyId(String id);
}
